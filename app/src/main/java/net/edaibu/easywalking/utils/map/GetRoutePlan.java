package net.edaibu.easywalking.utils.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import java.util.ArrayList;
import java.util.List;

/**
 * 路径规划
 * Created by Administrator on 2017/3/27 0027.
 */

public class GetRoutePlan implements OnGetRoutePlanResultListener {

    private BaiduMap mBaiduMap;
    private BitmapDescriptor bitmap,bitmap2;

    //路径规划对象
    private Overlay overlay1;

    public GetRoutePlan(BaiduMap baiduMap){
        this.mBaiduMap=baiduMap;
    }
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {

        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);

            List<LatLng> points1 = new ArrayList<LatLng>();
            final WalkingRouteLine w=result.getRouteLines().get(0);
            List<WalkingRouteLine.WalkingStep> list=w.getAllStep();
            for(WalkingRouteLine.WalkingStep l:list){
                points1.addAll(l.getWayPoints());
            }

            PolylineOptions ooPolyline1 = new PolylineOptions().width(10).color(MyApplication.application.getResources().getColor(R.color.main_color)).points(points1);
            overlay1=mBaiduMap.addOverlay(ooPolyline1);

            //构建起始位置图标
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.center_main);
            MarkerOptions option = new MarkerOptions().position(points1.get(0)).icon(bitmap);
            mBaiduMap.addOverlay(option);

            //构建终点位置图标
            bitmap2 = BitmapDescriptorFactory.fromResource(R.mipmap.img_bike);
            MarkerOptions option2 = new MarkerOptions().position(points1.get(points1.size()-1)).icon(bitmap2);
            mBaiduMap.addOverlay(option2);

            MapStatus mMapStatus = new MapStatus.Builder().target(points1.get(0)).zoom(16.8f).build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(19.0f),1000);
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.center_main);
        }

        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.img_bike);
        }
    }


    /**
     * 清除路线
     */
    public void clearRoutePlan(){
        if(overlay1!=null){
            overlay1.remove();
        }
    }


    public void close(){
        if(null!=bitmap && null!=bitmap2){
            bitmap.recycle();
            bitmap2.recycle();
            bitmap=null;
            bitmap2=null;
        }
    }
}
