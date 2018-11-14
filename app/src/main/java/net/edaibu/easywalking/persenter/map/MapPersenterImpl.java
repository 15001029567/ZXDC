package net.edaibu.easywalking.persenter.map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.BikeList;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.bean.Parking;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.Constant;
import net.edaibu.easywalking.utils.NetUtils;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.map.GetLocation;
import net.edaibu.easywalking.utils.map.MyOrientationListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 地图MapFragment 的接口
 */
public class MapPersenterImpl {

    private Activity activity;
    private MapPersenter mapPersenter;
    //地图传感器
    private MyOrientationListener myOrientationListener;
    private BaiduMap mBaiduMap;
    //手机是否锁屏
    private boolean IS_CLOSE_PHONE=false;
    //是否是第一次定位
    private boolean isFis=true;
    public MapPersenterImpl(Activity activity,MapPersenter mapPersenter){
        this.activity=activity;
        this.mapPersenter=mapPersenter;
    }

    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                //定位成功
                case HandlerConstant.MAP_LOCATION_SUCCESS:
                     if (mBaiduMap == null || mBaiduMap.getLocationData() == null) {
                         //关闭进度条
                         mapPersenter.closeLoding();
                         GetLocation.isOPen(activity);
                        break;
                     }
                     if(isFis){
                         isFis=false;
                         final LatLng latLng = new LatLng(mBaiduMap.getLocationData().latitude, mBaiduMap.getLocationData().longitude);
                         mapPersenter.locationSuccess(latLng);
                     }
                //获取当前位置的车辆
                case HandlerConstant.GET_LOCATION_BIKE_SUCCESS:
                      //关闭进度条
                      mapPersenter.closeLoding();
                      final BikeList bikeInfo= (BikeList) msg.obj;
                      if(null==bikeInfo){
                          break;
                      }
                      if(bikeInfo.isSussess()){
                          mapPersenter.showLocationBike(bikeInfo.getData());
                      }else{
                          mapPersenter.showToast(bikeInfo.getMsg());
                      }
                      break;
                //获取电子围栏等数据
                case HandlerConstant.FIND_FENCING_SUCCESS:
                      try {
                          final Fanceing fanceing= (Fanceing) msg.obj;
                          if(null==fanceing){
                              break;
                          }
                          if(fanceing.isSussess()){
                              mapPersenter.showFencing(fanceing);
                          }else{
                              mapPersenter.showToast(fanceing.getMsg());
                          }
                      }catch (Exception e){
                          e.printStackTrace();
                      }
                      break;
                //获取附近的停放点
                case HandlerConstant.GET_PARKING_SUCCESS:
                      final Parking parking= (Parking) msg.obj;
                      if(null==parking){
                          break;
                      }
                      if(parking.isSussess()){

                      }else{
                          mapPersenter.showToast(parking.getMsg());
                      }
                      break;
                case HandlerConstant.REQUST_ERROR:
                     //关闭进度条
                     mapPersenter.closeLoding();
                     mapPersenter.showToast(activity.getString(R.string.http_error));
                     break;
                case HandlerConstant.GET_DATA_ERROR:
                     //关闭进度条
                     mapPersenter.closeLoding();
                     mapPersenter.showToast(activity.getString(R.string.Data_parsing_error_please_try_again_later));
                     break;
                 default:
                     break;
            }
            return false;
        }
    });


    /**
     * 获取当前位置的车辆信息
     */
    public void getLocationBike(Double latitude, Double longtitude) {
        HttpMethod.getLocationBike(String.valueOf(latitude), String.valueOf(longtitude), mHandler);
    }

    /**
     * 查询电子围栏等
     * @param bikeCode
     */
    public void findFencing(String bikeCode){
        clearMap();
        HttpMethod.findFencing(bikeCode,mHandler);
    }


    /**
     * 查询附近的停放点
     */
    public void getParking(){
        String strLat=MyApplication.spUtil.getString(SPUtil.LOCATION_LAT);
        String strLon=MyApplication.spUtil.getString(SPUtil.LOCATION_LONG);
        HttpMethod.getParking(strLat,strLon,mHandler);
    }


    /**
     * 定位
     */
    public void startLocation() {
        //判断网络能否使用
        if (!NetUtils.isNetConnected(activity)) {
            mapPersenter.showToast(activity.getString(R.string.network_can_not_be_accessed_please_check_the_network_connection));
            return;
        }
        mapPersenter.showLoding(activity.getString(R.string.locating));
        isFis=true;
        GetLocation.getInstance().stopLocation();
        GetLocation.getInstance().startLocation(activity, mHandler, mBaiduMap);
    }


    /**
     * 获取当前的经纬度
     * @return
     */
    public LatLng getNewLatLng(){
        String strLat=MyApplication.spUtil.getString(SPUtil.LOCATION_LAT);
        String strLon=MyApplication.spUtil.getString(SPUtil.LOCATION_LONG);
        if(TextUtils.isEmpty(strLat) || TextUtils.isEmpty(strLon)){
            return null;
        }
        final double lat = Double.parseDouble(strLat);
        final double lon = Double.parseDouble(strLon);
        return new LatLng(lat,lon);
    }



    /**
     * 注册广播
     */
    public void register() {
        IntentFilter myIntentFilter = new IntentFilter();
        //监听网络
        myIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //锁屏广播，由系统发出
        myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        //点击home键广播，由系统发出
        myIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        // 注册广播监听
        activity.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case ConnectivityManager.CONNECTIVITY_ACTION:
                      startLocation();
                      break;
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS://点击home键广播
                    final String reason = intent.getStringExtra("reason");
                    if (TextUtils.equals(reason, "homekey")) {
                        IS_CLOSE_PHONE=true;
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF://锁屏广播
                    IS_CLOSE_PHONE=true;
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 清空map上遮盖物
     */
    public void clearMap() {
        if (null != mBaiduMap) {
            mBaiduMap.clear();
        }
    }

    /**
     * 传感器监听
     * @param mBaiduMap
     */
    public void initOritationListener(final BaiduMap mBaiduMap) {
        this.mBaiduMap=mBaiduMap;
        myOrientationListener = new MyOrientationListener(activity);
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
     *
     * 设置个性化地图config文件路径
     */
    public void setMapCustomFile() {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = activity.getAssets().open("customConfigdir/custom_config_1009.json");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = activity.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/custom_config_1009.json");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/custom_config_1009.json");
    }


    public void onResume(){
        if(Constant.PLAY_STATUS==1 && IS_CLOSE_PHONE){
            getParking();
            IS_CLOSE_PHONE=false;
        }
    }

    public void onStart(){
        myOrientationListener.start();
    }

    public void onStop() {
        myOrientationListener.stop();
    }

    public void onDestory(){
        //关闭广播
        activity.unregisterReceiver(mBroadcastReceiver);
        //释放Handler
        mHandler.removeCallbacksAndMessages(null);
        mapPersenter=null;
    }
}
