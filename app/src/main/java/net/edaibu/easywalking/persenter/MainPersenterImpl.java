package net.edaibu.easywalking.persenter;

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
import net.edaibu.easywalking.service.BleService;
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
                fragmentTransaction.add(containerViewId, fg);
            }
        } else {
            if (fg.isAdded()) {
                fragmentTransaction.remove(fg);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 断开蓝牙连接
     */
    public void disconnect(){
        if(null!=bleService){
            bleService.disconnect();
        }
    }

    /**
     * 关闭蓝牙服务
     */
    public void closeService(){
        activity.unbindService(mServiceConnection);
    }


    public void onDestory(){
        mHandler.removeCallbacksAndMessages(null);
        mainPersenter=null;
    }

}
