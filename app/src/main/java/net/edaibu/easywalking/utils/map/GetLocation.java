package net.edaibu.easywalking.utils.map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.utils.SPUtil;
/**
 * 地图定位
 * Created by Administrator on 2017/3/15 0015.
 */

public class GetLocation {

    private static GetLocation getLocation;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private Handler handler;
    private BaiduMap mBaiduMap;
    private Context mContext;

    public static GetLocation getInstance() {
        if (null == getLocation) {
            getLocation = new GetLocation();
        }
        return getLocation;
    }

    /**
     * 设置定位
     */
    public void startLocation(Context mContext, Handler handler,BaiduMap mBaiduMap) {
        if(null!=mLocClient){
            return;
        }
        this.handler = handler;
        this.mBaiduMap = mBaiduMap;
        this.mContext = mContext;
        mLocClient = new LocationClient(this.mContext);
        mLocClient.registerLocationListener(myListener);
        UiSettings mUiSetting=mBaiduMap.getUiSettings();
        //设置不允许3D地图
        mUiSetting.setOverlookingGesturesEnabled(false);
        // 开启定位图层
        this.mBaiduMap.setMyLocationEnabled(true);
        //设置不显示建筑物
        this.mBaiduMap.setBuildingsEnabled(false);
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        this.mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("gcj02"); // 设置火星坐标
        option.setScanSpan(8000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
            //GPS定位成功、网络定位成功、离线定位成功
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeOffLineLocation) {
                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                final Double longtitude = location.getLongitude();
                final Double latitude = location.getLatitude();
                //保存当前的经纬度数据
                MyApplication.spUtil.addString(SPUtil.LOCATION_LAT,  String.valueOf(latitude));
                MyApplication.spUtil.addString(SPUtil.LOCATION_LONG,  String.valueOf(longtitude));
            }
            Message message = new Message();
            message.what = HandlerConstant.MAP_LOCATION_SUCCESS;
            handler.sendMessage(message);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (null != mLocClient) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
            mLocClient=null;
        }
    }
}
