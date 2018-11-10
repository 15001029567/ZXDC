package net.edaibu.easywalking.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.persenter.main.MainPersenter;
import net.edaibu.easywalking.utils.JsonUtils;
import net.edaibu.easywalking.utils.TimerUtil;
import net.edaibu.easywalking.utils.map.GetRoutePlan;

/**
 * 预约车辆的fragment
 */
public class BespokeFragment extends BaseFragment implements View.OnClickListener{

    //预约对象
    private BikeBean bikeBean;
    private TextView tvAddress,tvBikeCode,tvDistance,tvTime,tvTimeDes;
    private MainPersenter mainPersenter;
    //是否锁屏或者进入桌面
    private String IS_CLOSE_PHONE="IS_CLOSE_PHONE";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        registerBoradcastReceiver();
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bespoke, container, false);
        tvAddress=(TextView)view.findViewById(R.id.tv_address);
        tvBikeCode=(TextView)view.findViewById(R.id.tv_bikecode);
        tvDistance=(TextView)view.findViewById(R.id.tv_distance);
        tvTime=(TextView)view.findViewById(R.id.tv_fb_time);
        tvTimeDes=(TextView)view.findViewById(R.id.tv_fb_time_des);
        view.findViewById(R.id.rel_fb).setOnClickListener(this);
        //展示数据
        showData();
        return view;
    }



    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                //获取订单信息
                case HandlerConstant.GET_ORDER_INFO_SUCCESS:
                    bikeBean= JsonUtils.getBikeBean(msg.obj.toString());
                    if(null==bikeBean){
                        break;
                    }
                    if(bikeBean.isSussess()){

                    }
                    break;
            }
            return false;
        }
    });

    /**
     * 展示数据
     */
    private int hours, minutes, seconds, time, time2,totalSeconds=-1;
    private TimerUtil timerUtil;
    public void showData(){
        if(null==bikeBean){
            return;
        }
        //反地理编码查询地址
        reverseGeoCode(new LatLng(bikeBean.getLatitude(),bikeBean.getLongitude()));
        tvBikeCode.setText(bikeBean.getBikeCode());
        /**
         *  l1:服务器时间与预约开始时间差值
         *  time：免费预约剩余时间，初始为15min
         *  time2：计费时间
         */
        time = bikeBean.getFreeTime();
        Long l1 = bikeBean.getDateTime() - bikeBean.getReserveDate();
        if (l1 < time) {
            Long l2 = (time - l1) / 1000;
            time = l2.intValue();
            time2=0;
        } else {
            Long l3 = (l1 - time) / 1000;
            time2 = l3.intValue();
            time = 0;
        }
        //开启计时
        if(null!=timerUtil){
            timerUtil.stop();
        }
        timerUtil=new TimerUtil(0, 1000, new TimerUtil.TimerCallBack() {
            public void onFulfill() {
                timerTask();
            }
        });
        timerUtil.start();
    }


    /**
     * 开始计时器
     */
    private void timerTask(){
        totalSeconds++;
        if (time > 0) {
            time--;
            hours = time / 3600;
            minutes = (time - hours * 3600) / 60;
            seconds = time - hours * 3600 - minutes * 60;
        } else {
            time2++;
            hours = time2 / 3600;
            minutes = (time2 - hours * 3600) / 60;
            seconds = time2 - hours * 3600 - minutes * 60;
        }
        mHandler.post(new Runnable() {
            public void run() {
                if(!isAdded()){
                    return;
                }
                if(bikeBean.getReserveCost()==0){
                    tvTimeDes.setText(getString(R.string.free_money));
                    tvTime.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                }else{
                    tvTimeDes.setText(getString(R.string.charging)+String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    tvTime.setText(((int)(bikeBean.getReserveCost()/100))+getString(R.string.primary));
                }
            }
        });
    }


    public void setBikeBean(BikeBean bikeBean,MainPersenter mainPersenter){
        this.bikeBean=bikeBean;
        this.mainPersenter=mainPersenter;
    }


    public void onClick(View v) {
        switch (v.getId()){
            //关闭预约界面
            case R.id.rel_fb:
                 mainPersenter.closeBespokeUI();
                 break;
        }
    }


    /**
     * 注册广播
     */
    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        //路径规划广播
        myIntentFilter.addAction(GetRoutePlan.ACTION_GETROUTE_SUCCES);
        //锁屏广播，由系统发出
        myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        //点击home键广播，由系统发出
        myIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mActivity.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                //路径规划广播
                case GetRoutePlan.ACTION_GETROUTE_SUCCES:
                      final int distance=intent.getIntExtra("distance",0);
                      final int time=intent.getIntExtra("time",0);
                      if (isAdded()) {
                          tvDistance.setText(distance+"");
                          if(time<60){
                              tvTime.setText(time+"");
                              tvTimeDes.setText(getString(R.string.second));
                          }else{
                              tvTime.setText((time/60)+"");
                              tvTimeDes.setText(getString(R.string.minute_clock));
                          }
                      }
                      break;
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS://点击home键广播
                     final String reason = intent.getStringExtra("reason");
                     if (TextUtils.equals(reason, "homekey")) {
                        MyApplication.spUtil.addBoolean(IS_CLOSE_PHONE,true);
                     }
                     break;
                case Intent.ACTION_SCREEN_OFF://锁屏广播
                     MyApplication.spUtil.addBoolean(IS_CLOSE_PHONE,true);
                     break;
                default:
                    break;
            }
        }
    };

    /**
     * 反地理编码
     * @param latLng
     */
    protected void reverseGeoCode(LatLng latLng) {
        // 创建地理编码检索实例
        GeoCoder geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result != null) {
                    tvAddress.setText(result.getAddress());
                }
            }
            // 地理编码查询结果回调函数
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    tvAddress.setText(getString(R.string.cannot_find_this_address));
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        // 释放地理编码检索实例
        geoCoder.destroy();
    }


    /**
     * 查询订单信息
     */
    public void getOrderInfo(){
        HttpMethod.getOrderInfo(mHandler);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.spUtil.getBoolean(IS_CLOSE_PHONE)){
            getOrderInfo();
            MyApplication.spUtil.removeMessage(IS_CLOSE_PHONE);
        }
    }


    public void onDestroy() {
        super.onDestroy();
        //关闭广播
        mActivity.unregisterReceiver(mBroadcastReceiver);
    }

}
