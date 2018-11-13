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
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.JsonUtils;
import net.edaibu.easywalking.utils.TimerUtil;

/**
 * 骑行界面的fragment
 */
public class CyclingFragment extends BaseFragment {

    //骑行对象
    private BikeBean bikeBean;
    private TextView tvTime, tvDistance,tvKa,tvBikeCode,tvMoney;
    //是否锁屏或者进入桌面
    private String IS_CLOSE_PHONE="IS_CLOSE_PHONE";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        registerBoradcastReceiver();
    }

    View view=null;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cycling, container, false);
        tvTime = (TextView) view.findViewById(R.id.tv_am_qiche);
        tvDistance = (TextView) view.findViewById(R.id.tv_am_distance);
        tvKa=(TextView)view.findViewById(R.id.tv_fc_ka);
        tvMoney = (TextView) view.findViewById(R.id.tv_am_money);
        tvBikeCode=(TextView)view.findViewById(R.id.tv_fc_bikeCode);
        //展示界面数据
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
     * 展示界面数据
     */
    private int time, totalSeconds=-1;
    private TimerUtil timerUtil;
    private void showData(){
        if(null==bikeBean){
            return;
        }
        //显示车辆编码
        tvBikeCode.setText(bikeBean.getBikeCode());
        if(bikeBean.getDateTime()==0){
            bikeBean.setDateTime(System.currentTimeMillis());
        }
        final Long aLong = (bikeBean.getDateTime() - bikeBean.getCyclingStartDate()) / 1000;
        time = aLong.intValue();
        //开启计时
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
        time++;
        totalSeconds++;
        final int hoursInt = time / 3600;
        final int minutesInt = (time - hoursInt * 3600) / 60;
        final int secondsInt = time - hoursInt * 3600 - minutesInt * 60;
        mHandler.post(new Runnable() {
            public void run() {
                if(!isAdded()){
                    return;
                }
                if(hoursInt==0){
                    tvTime.setText(String.format("%02d", minutesInt) + ":" + String.format("%02d", secondsInt));
                } else{
                    tvTime.setText(String.format("%02d", hoursInt) + ":" + String.format("%02d", minutesInt) + ":" + String.format("%02d", secondsInt));
                }
            }
        });

    }


    /**
     * 注册广播
     */
    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        //锁屏广播，由系统发出
        myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        //点击home键广播，由系统发出
        myIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mActivity.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
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
     * 查询订单信息
     */
    public void getOrderInfo(){
        HttpMethod.getOrderInfo(mHandler);
    }

    public void setBikeBean(BikeBean bikeBean){
        this.bikeBean=bikeBean;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.spUtil.getBoolean(IS_CLOSE_PHONE)){
            getOrderInfo();
            MyApplication.spUtil.removeMessage(IS_CLOSE_PHONE);
        }
    }

    @Override
    public void onDestroy() {
        if(timerUtil!=null){
            timerUtil.stop();
        }
        //关闭广播
        mActivity.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}
