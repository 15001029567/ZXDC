package net.edaibu.easywalking.persenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.BikeInfo;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
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

    public MapPersenterImpl(Activity activity,MapPersenter mapPersenter){
        this.activity=activity;
        this.mapPersenter=mapPersenter;
    }


    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //关闭进度条
            mapPersenter.closeLoding();
            switch (msg.what){
                //获取当前位置的车辆
                case HandlerConstant.GET_LOCATION_BIKE_SUCCESS:
                      final BikeInfo bikeInfo= (BikeInfo) msg.obj;
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
                      final Fanceing fanceing= (Fanceing) msg.obj;
                      if(null==fanceing){
                          break;
                      }
                      if(fanceing.isSussess()){
                          mapPersenter.showFencing(fanceing);
                      }else{
                          mapPersenter.showToast(fanceing.getMsg());
                      }
                      break;
                case HandlerConstant.REQUST_ERROR:
                     mapPersenter.showToast(activity.getString(R.string.http_error));
                     break;
                case HandlerConstant.GET_DATA_ERROR:
                     mapPersenter.showToast(activity.getString(R.string.Data_parsing_error_please_try_again_later));
                     break;
                 default:
                     break;
            }
            return false;
        }
    });


    /**
     * 传感器监听
     * @param mBaiduMap
     */
    public void initOritationListener(final BaiduMap mBaiduMap) {
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
        HttpMethod.findFencing(bikeCode,mHandler);
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


    public void onStart(){
        myOrientationListener.start();
    }

    public void onStop() {
        myOrientationListener.stop();
    }
}
