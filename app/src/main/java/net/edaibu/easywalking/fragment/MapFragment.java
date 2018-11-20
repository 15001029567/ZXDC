package net.edaibu.easywalking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.bean.BikeList;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.bean.Parking;
import net.edaibu.easywalking.persenter.main.MainPersenter;
import net.edaibu.easywalking.persenter.map.MapPersenter;
import net.edaibu.easywalking.persenter.map.MapPersenterImpl;
import net.edaibu.easywalking.utils.Constant;
import net.edaibu.easywalking.utils.LogUtils;
import net.edaibu.easywalking.utils.Util;
import net.edaibu.easywalking.utils.map.GetRoutePlan;
import java.util.ArrayList;
import java.util.List;
/**
 * 首页地图fragment
 */
public class MapFragment extends BaseFragment implements MapPersenter,View.OnClickListener,BaiduMap.OnMarkerClickListener,BaiduMap.OnMapClickListener {

    private MapPersenterImpl mapPersenter;
    private MapView mMapView;
    public BaiduMap mBaiduMap;
    //中心图标
    private ImageView imgCenter;
    //中心点坐标
    private LatLng latLng;
    //地图上的marker图标
    private BitmapDescriptor bitmap;
    //路径规划对象
    private RoutePlanSearch rpSearch = null;
    private GetRoutePlan getRoutePlan;
    //附近车辆集合
    private List<BikeList.BikeInfoList> bikeList=new ArrayList<>();
    //停车点的marker集合
    private List<Marker> parkMarker = new ArrayList<>();
    private MainPersenter mainPersenter;
    public void onCreate(Bundle savedInstanceState) {
        //初始化MVP接口
        initPersenter();
        //绘制个性化地图
        mapPersenter.setMapCustomFile();
        MapView.setMapCustomEnable(true);
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
        mapPersenter.startLocation();
        //注册广播
        mapPersenter.register();
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
        imgCenter=(ImageView)view.findViewById(R.id.img_center);
        //隐藏缩放按钮
        mMapView.showZoomControls(false);
        //路径规划
        rpSearch = RoutePlanSearch.newInstance();
        getRoutePlan=new GetRoutePlan(mBaiduMap);
        rpSearch.setOnGetRoutePlanResultListener(getRoutePlan);
        //注册地图上覆盖物的点击事件
        mBaiduMap.setOnMarkerClickListener(this);
        //注册地图点击事件
        mBaiduMap.setOnMapClickListener(this);
        // 注册触摸事件
        mBaiduMap.setOnMapStatusChangeListener(new Maptouch());
        view.findViewById(R.id.img_location).setOnClickListener(this);
        view.findViewById(R.id.img_service).setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()){
            //重新定位
            case R.id.img_location:
                 mapPersenter.startLocation();
                 break;
            default:
                break;
        }
    }

    /**
     * 地图覆盖物点击事件
     * @param marker
     * @return
     */
    public boolean onMarkerClick(Marker marker) {
        if(null==marker){
            return true;
        }
        switch (Constant.PLAY_STATUS){
            //展示预约界面
            case 0:
                 final BikeBean bikeBean=new BikeBean(marker.getTitle(),marker.getPosition().latitude,marker.getPosition().longitude);
                 mainPersenter.setBikeBean(bikeBean);
                 mainPersenter.showBespoke();
                 break;
            //展示到停放点的路径规划
            case 1:
                 setRoutePlanByParking(marker.getPosition().latitude,marker.getPosition().longitude);
                 break;
             default:
                 break;
        }
        return true;
    }


    /**
     * 地图点击事件
     * @param latLng
     * @return
     */
    public void onMapClick(LatLng latLng) {
        switch (Constant.PLAY_STATUS){
            //处于骑行界面
            case 1:
                 break;
            //处于预约界面
            case 2:
                 mainPersenter.closeBespokeUI();
                 break;
             default:
                 break;
        }

    }
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }


    /**
     * 定位成功
     * @param latLng
     */
    public void locationSuccess(LatLng latLng) {
        clearTask();
        this.latLng=latLng;
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        if(Constant.PLAY_STATUS==0){
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(19f), 500);
            //获取当前位置的车辆信息
            mapPersenter.getLocationBike(latLng.latitude,latLng.longitude);
            //去查询订单信息
            mainPersenter.getOrderInfo();
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
    public void showLocationBike(List<BikeList.BikeInfoList> list) {
        bikeList=list;
        showBikeMark();
    }


    /**
     * 显示附近的车辆图标
     */
    public void showBikeMark(){
        mapPersenter.clearMap();
        for (int i = 0, len = bikeList.size(); i < len; i++) {
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.img_bike);
            MarkerOptions op = new MarkerOptions().position(new LatLng(bikeList.get(i).getLatitude(), bikeList.get(i).getLongitude())).icon(bitmap).zIndex(i).title(bikeList.get(i).getBikecode()).animateType(MarkerOptions.MarkerAnimateType.grow);
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
     * 显示附近的停放点
     * @param list
     */
    public void showParking(List<Parking.ParkingBean> list){
        //清空停放点的marker
        removeParking();
        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.parking);
        for (int i = 0, len = list.size(); i < len; i++) {
            MarkerOptions op = new MarkerOptions().position(new LatLng(Double.parseDouble(list.get(i).getLatitude()), Double.parseDouble(list.get(i).getLongitude()))).icon(bitmap).zIndex(i);
            Marker marker = (Marker) mBaiduMap.addOverlay(op);
            parkMarker.add(marker);
        }
    }


    /**
     * 清空停放点的marker
     */
    private void removeParking(){
        for (int i = 0; i < parkMarker.size(); i++) {
            if (parkMarker.get(i) != null) {
                parkMarker.get(i).remove();
            }
        }
    }


    /**
     * 设置预约时路径规划
     */
    public void setRoutePlan(double latitude,double longitude){
        mapPersenter.clearMap();
        PlanNode stNode = PlanNode.withLocation(mapPersenter.getNewLatLng());
        PlanNode enNode = PlanNode.withLocation(new LatLng(latitude, longitude));
        rpSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
    }


    /**
     * 设置骑行到停车点的路径规划
     */
    private void setRoutePlanByParking(double latitude, double longitude) {
        getRoutePlan.clearRoutePlan();
        PlanNode stNode = PlanNode.withLocation(mapPersenter.getNewLatLng());
        PlanNode enNode = PlanNode.withLocation(new LatLng(latitude, longitude));
        rpSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(enNode));
    }


    /**
     * 地图触摸状态改变监听(地图移动获取坐标)
     */
    public class Maptouch implements BaiduMap.OnMapStatusChangeListener {
        public void onMapStatusChange(MapStatus mapStatus) {
        }
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            // 计算距离
            final Double distance = Util.GetShortDistance(latLng.longitude, latLng.latitude, mapStatus.target.longitude, mapStatus.target.latitude);
            if(Constant.PLAY_STATUS==0 && distance>300){
                latLng=mapStatus.target;
                //查询附近的车辆信息
                mapPersenter.getLocationBike(latLng.latitude,latLng.longitude);
            }
            return;
        }
        public void onMapStatusChangeStart(MapStatus arg0) {
        }
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
        }
    }


    /**
     * 查询电子围栏等
     * @param bikeCode
     */
    public void findFencing(String bikeCode){
        mapPersenter.findFencing(bikeCode);
    }


    /**
     * 查询附近的停放点
     */
    public void getParking(){
        mapPersenter.getParking();
    }


    /**
     * 清空map上遮盖物
     */
    public void clearMap() {
        if (null != mBaiduMap) {
            mBaiduMap.clear();
        }
    }


    public void setMainCallBack(MainPersenter mainPersenter){
        this.mainPersenter=mainPersenter;
    }

    public void showLoding(String msg) {
        showProgress(msg,true);
    }

    public void closeLoding() {
       clearTask();
    }

    public void showToast(String msg) {
        showMsg(msg);
    }

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
        mapPersenter.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=bitmap){
            bitmap.recycle();
            bitmap=null;
        }
        mapPersenter.onDestory();
    }
}
