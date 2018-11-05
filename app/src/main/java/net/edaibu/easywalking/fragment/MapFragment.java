package net.edaibu.easywalking.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.BikeInfo;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.persenter.MapPersenter;
import net.edaibu.easywalking.persenter.MapPersenterImpl;
import net.edaibu.easywalking.utils.NetUtils;
import net.edaibu.easywalking.utils.map.GetLocation;
import java.util.ArrayList;
import java.util.List;
/**
 * 首页地图fragment
 */
public class MapFragment extends BaseFragment implements MapPersenter, OnGetGeoCoderResultListener,View.OnClickListener {

    private MapPersenterImpl mapPersenter;
    private MapView mMapView;
    public BaiduMap mBaiduMap;
    //是否是第一次定位
    private boolean isFis = true;
    private GeoCoder mSearch = null;
    //中心点坐标
    private LatLng finishLng, finishLng2;
    //地图上的marker图标
    private BitmapDescriptor bitmap;
    public void onCreate(Bundle savedInstanceState) {
        //初始化MVP接口
        initPersenter();
        //绘制个性化地图
        mapPersenter.setMapCustomFile();
        super.onCreate(savedInstanceState);
    }

    View view=null;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        //初始化控件
        initView();
        //初始化传感器
        mapPersenter.initOritationListener(mBaiduMap);
        //开始定位
        startLocation();
        return view;
    }


    /**
     * 初始化MVP接口
     */
    private void initPersenter(){
        mapPersenter=new MapPersenterImpl(mActivity,MapFragment.this);
    }


    /**
     * 初始化地图各属性
     */
    private void initView(){
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
        // 根据经纬度搜索
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        view.findViewById(R.id.img_location).setOnClickListener(this);
        view.findViewById(R.id.img_service).setOnClickListener(this);

    }


    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                //定位成功
                case HandlerConstant.MAP_LOCATION_SUCCESS:
                    if (mBaiduMap == null || mBaiduMap.getLocationData() == null) {
                        clearTask();
                        break;
                    }
                    if (isFis) {
                        isFis = false;
                        finishLng2 = finishLng = new LatLng(mBaiduMap.getLocationData().latitude, mBaiduMap.getLocationData().longitude);
                        //根据经纬度去定位
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(finishLng));
                    }
                    break;
            }
            return false;
        }
    });


    /**
     * 根据经纬度定位
     * @param geoCodeResult
     */
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        clearTask();
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult.getLocation()));
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(19f), 500);
        //获取当前位置的车辆信息
        mapPersenter.getLocationBike(finishLng.latitude,finishLng.longitude);
    }


    /**
     * 绘制附近的车辆图标
     * @param list
     */
    private void setBikeMark(List<BikeInfo.BikeInfoList> list) {
        for (int i = 0, len = list.size(); i < len; i++) {
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.img_bike);
            MarkerOptions op = new MarkerOptions().position(new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude())).icon(bitmap).zIndex(i).animateType(MarkerOptions.MarkerAnimateType.grow);
            mBaiduMap.addOverlay(op);
        }
    }

    /**
     * 绘制电子围栏
     */
    private void setFencing(Fanceing fanceing) {
        for (int i = 0; i < fanceing.getData().getBigFence().size(); i++) {
            //电子围栏经纬度集合
            List<LatLng> fencingList = new ArrayList<>();
            for (int j = 0; j < fanceing.getData().getBigFence().get(i).size(); j++) {
                fencingList.add(new LatLng(fanceing.getData().getBigFence().get(i).get(j).get(1), fanceing.getData().getBigFence().get(i).get(j).get(0)));
            }
            //画电子围栏
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_road_red_arrow);
            List<BitmapDescriptor> textureList = new ArrayList<>();
            textureList.add(bitmap);
            List<Integer> textureIndexs = new ArrayList<>();
            textureIndexs.add(0);
            PolylineOptions ooPolyline11 = new PolylineOptions().width(15).points(fencingList).dottedLine(true).customTextureList(textureList).textureIndex(textureIndexs).zIndex(2);
            mBaiduMap.addOverlay(ooPolyline11);
        }
    }


    /**
     * 绘制禁停区域
     */
    private void banStopCar(Fanceing fanceing) {
        for (int i = 0; i < fanceing.getData().getForbidArea().size(); i++) {
            List<LatLng> list = new ArrayList<>();
            for (int j = 0; j < fanceing.getData().getForbidArea().get(i).size(); j++) {
                list.add(new LatLng(fanceing.getData().getForbidArea().get(i).get(j).get(1), fanceing.getData().getForbidArea().get(i).get(j).get(0)));
            }
            OverlayOptions ooPolygon = new PolygonOptions().points(list).stroke(new Stroke(7, 0xAAF9DA14)).fillColor(0x8AF0EEE1);
            mBaiduMap.addOverlay(ooPolygon);
        }
    }


    /**
     * 绘制禁行区域
     */
    private void banWalkCar(Fanceing fanceing) {
        for (int i = 0; i < fanceing.getData().getSuperblock().size(); i++) {
            List<LatLng> list = new ArrayList<>();
            for (int j = 0; j < fanceing.getData().getSuperblock().get(i).size(); j++) {
                list.add(new LatLng(fanceing.getData().getSuperblock().get(i).get(j).get(1), fanceing.getData().getSuperblock().get(i).get(j).get(0)));
            }
            OverlayOptions ooPolygon = new PolygonOptions().points(list).stroke(new Stroke(7, 0xAAEE4D4C)).fillColor(0x8AEED4D5);
            mBaiduMap.addOverlay(ooPolygon);
        }
    }


    /**
     * 展示电子围栏，禁停区等
     * @param fanceing
     */
    public void showFencing(Fanceing fanceing) {
        setFencing(fanceing);
        banStopCar(fanceing);
        banWalkCar(fanceing);
    }

    /**
     * 显示附近的车辆信息
     * @param list
     */
    public void showLocationBike(List<BikeInfo.BikeInfoList> list) {
        setBikeMark(list);
    }

    /**
     * 展示加载滚动条
     */
    public void showLoding(String msg) {
        showProgress(msg,true);
    }

    /**
     * 关闭加载滚动条
     */
    public void closeLoding() {
       clearTask();
    }

    @Override
    public void showToast(String msg) {
        showMsg(msg);
    }


    /**
     * 定位
     */
    public void startLocation() {
        if (!isFis) {
            return;
        }
        //判断网络能否使用
        if (!NetUtils.isNetConnected(mActivity)) {
            showMsg(getString(R.string.network_can_not_be_accessed_please_check_the_network_connection));
            return;
        }
        showProgress(getString(R.string.locating),true);
        GetLocation.getInstance().stopLocation();
        GetLocation.getInstance().startLocation(mActivity, mHandler, mBaiduMap);
    }


    /**
     * 清空map上遮盖物
     */
    public void clearMap() {
        if (null != mBaiduMap) {
            mBaiduMap.clear();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mapPersenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapPersenter.onStop();
    }

    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //删除handler中的消息
        removeHandler(mHandler);
        if(null!=bitmap){
            bitmap.recycle();
            bitmap=null;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
