package net.edaibu.easywalking.persenter.main;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.service.BleService;
import net.edaibu.easywalking.utils.JsonUtils;
import net.edaibu.easywalking.utils.bletooth.SendBleAgreement;

/**
 * 首页MainActivity 的接口
 */
public class MainPersenterImpl {

    private FragmentActivity activity;
    private MainPersenter mainPersenter;
    //蓝牙服务对象
    public BleService bleService = null;
    public MainPersenterImpl(FragmentActivity activity,MainPersenter mainPersenter){
        this.activity=activity;
        this.mainPersenter=mainPersenter;
    }

    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            mainPersenter.closeLoding();
            switch (msg.what){
                //扫码开锁后生成骑行单
                case HandlerConstant.GET_ORDER_BY_SCAN_SUCCESS:
                     final BikeBean bikeBean= JsonUtils.getBikeBean(msg.obj.toString());
                     if(null==bikeBean){
                         break;
                     }
                     if(bikeBean.isSussess()){
                         mainPersenter.getOrderByScan(bikeBean);
                     }else{
                         mainPersenter.showToast(bikeBean.getMsg());
                     }
                      break;
                case HandlerConstant.REQUST_ERROR:
                      mainPersenter.showToast(activity.getString(R.string.http_error));
                      break;
                case HandlerConstant.GET_DATA_ERROR:
                     mainPersenter.showToast(activity.getString(R.string.Data_parsing_error_please_try_again_later));
                     break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 初始化蓝牙服务
     */
    public void initBleService() {
        Intent bindIntent = new Intent(activity, BleService.class);
        activity.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            bleService = ((BleService.LocalBinder) rawBinder).getService();
            BluetoothAdapter bluetoothAdapter = bleService.createBluetoothAdapter();
            SendBleAgreement.getInstance().init(bleService,bluetoothAdapter);
            mainPersenter.initBleService(bleService);
        }

        public void onServiceDisconnected(ComponentName classname) {
        }
    };


    /**
     * 开启fragment
     * @param fg
     */
    public void showFragment(final Fragment fg, final boolean b, final int containerViewId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (b) {
            if (!fg.isAdded()) {
                fragmentTransaction.setCustomAnimations(R.anim.fragment_in_bottom,0);
                fragmentTransaction.add(containerViewId, fg);
            }
        } else {
            if (fg.isAdded()) {
                fragmentTransaction.setCustomAnimations(R.anim.fragment_out_bottom,0);
                fragmentTransaction.remove(fg);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 扫码开锁后生成骑行单接口
     * @param bikeCode
     */
    public void getOrderByScan(String bikeCode){
        HttpMethod.getOrderByScan(bikeCode,mHandler);
    }


    /**
     * 断开蓝牙连接
     */
    public void disconnect(){
        if(null!=bleService){
            bleService.disconnect();
        }
    }

    public void onDestory(){
        //关闭蓝牙服务
        activity.unbindService(mServiceConnection);
        //释放Handler
        mHandler.removeCallbacksAndMessages(null);
        mainPersenter=null;
    }

}
