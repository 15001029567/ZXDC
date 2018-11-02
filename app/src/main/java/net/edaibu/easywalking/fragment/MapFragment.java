package net.edaibu.easywalking.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.utils.NetUtils;
import net.edaibu.easywalking.utils.map.GetLocation;
import net.edaibu.easywalking.utils.map.MyOrientationListener;

/**
 * 首页地图fragment
 */
public class MapFragment extends BaseFragment implements OnGetGeoCoderResultListener {

    private MapView mMapView;
    public BaiduMap mBaiduMap;
    //是否是第一次定位
    private boolean isFis = true;
    private GeoCoder mSearch = null;
    //中心点坐标
    private LatLng finishLng, finishLng2;
    //地图传感器
    private MyOrientationListener myOrientationListener;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        //初始化地图各属性
        initMap();
        //初始化传感器
        initOritationListener();
        //开始定位
        startLocation();
        return view;
    }


    /**
     * 初始化地图各属性
     */
    private void initMap(){
        mBaiduMap = mMapView.getMap();
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
        // 根据经纬度搜索
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
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
    }


    /**
     * 传感器监听
     **/
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(mActivity);
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    public void onOrientationChanged(float x) {
                        if (mBaiduMap == null || mBaiduMap.getLocationData() == null) {
                            return;
                        }
                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mBaiduMap.getLocationData().accuracy)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction((int) x)
                                .latitude(mBaiduMap.getLocationData().latitude)
                                .longitude(mBaiduMap.getLocationData().longitude).build();
                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);
                    }
                });
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


    @Override
    public void onStart() {
        super.onStart();
        myOrientationListener.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        myOrientationListener.stop();
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
    }
}
