package net.edaibu.easywalking.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.fragment.MapFragment;
import net.edaibu.easywalking.service.BleService;
import net.edaibu.easywalking.utils.ActivitysLifecycle;
import net.edaibu.easywalking.utils.LogUtils;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.bletooth.BleStatus;
import net.edaibu.easywalking.utils.bletooth.ParseBleDataTask;
import net.edaibu.easywalking.utils.bletooth.SendBleAgreement;
import net.edaibu.easywalking.view.DialogView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity{

    //蓝牙指令状态
    private int BLE_STATUS= BleStatus.BLE_NORMAL_STATE;
    //蓝牙服务对象
    public BleService mService = null;
    //蓝牙适配器
    public BluetoothAdapter mBtAdapter = null;
    //自定义dialog
    public DialogView dialogView;
    //地图的fragment
    private MapFragment mapFragment=new MapFragment();
    private Handler mHandler=new Handler();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化蓝牙服务
        initBleService();
        //注册广播
        registerBoradcastReceiver();
        showFragment(mapFragment, true, R.id.fragment_map);
    }


    /**
     * 初始化蓝牙服务
     */
    private void initBleService() {
        Intent bindIntent = new Intent(this, BleService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((BleService.LocalBinder) rawBinder).getService();
            mBtAdapter = mService.createBluetoothAdapter();
            SendBleAgreement.getInstance().init(mService);
        }

        public void onServiceDisconnected(ComponentName classname) {
        }
    };


    /**
     * 注册广播
     */
    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BleService.ACTION_NO_SCAN_BLE_DEVICE);//扫描不到指定蓝牙设备
        myIntentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);//蓝牙断开连接
        myIntentFilter.addAction(BleService.ACTION_ENABLE_NOTIFICATION_SUCCES);//初始化通信通道成功
        myIntentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);//获取到锁的回执数据
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private boolean isConnect = true;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                //扫描不到指定蓝牙设备
                case BleService.ACTION_NO_SCAN_BLE_DEVICE:
                      clearTask();
                      dialogView = new DialogView(dialogView, mContext, getString(R.string.can_not_find_bluttooth_please_close_bike_and_connect_custom_service), getString(R.string.known), null, null, null);
                      dialogView.show();
                      break;
                //蓝牙连接断开
                case BleService.ACTION_GATT_DISCONNECTED:
                      final int status=intent.getIntExtra("status",0);
                      //重连一次蓝牙
                      if (status!=0) {
                          if(isConnect) {
                              LogUtils.e("重新连接一次蓝牙!");
                              isConnect=false;
                              mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    mService.connect(MyApplication.spUtil.getString(SPUtil.DEVICE_MAC));
                                 }
                             },500);
                             return;
                         }else{
                            isConnect=true;
                         }
                      }
                      clearTask();
                      break;
                //蓝牙通信通道成功
                case BleService.ACTION_ENABLE_NOTIFICATION_SUCCES:
                      LogUtils.e("蓝牙初始化通信通道成功");
                      //发送认证命令
                      SendBleAgreement.getInstance().sendBleData(BleStatus.BLE_NORMAL_STATE);
                      break;
                //获取到锁的回执数据
                case BleService.ACTION_DATA_AVAILABLE:
                      final byte[] bleData = intent.getByteArrayExtra(BleService.ACTION_EXTRA_DATA);
                      final int resultCode= ParseBleDataTask.parseData(mContext,bleData);
                      lockResult(resultCode);
                      break;
                  default:
                      break;
            }
        }
    };


    /**
     * 根据锁回执的数据进行处理
     */
    private void lockResult(int resultCode){
        if(resultCode!=BleStatus.BLE_CERTIFICATION_SUCCESS){
            this.BLE_STATUS=resultCode;
        }
        switch (resultCode){
            case -1:
                 clearTask();
                 break;
            //认证成功
            case BleStatus.BLE_CERTIFICATION_SUCCESS:
                  sendBleCmd(BLE_STATUS);
                  break;
            default:
                  break;
        }
    }


    /**
     * 发送蓝牙指令
     * @param status：蓝牙指令类型
     */
    private void sendBleCmd(int status){
        this.BLE_STATUS=status;
        //扫描并连接蓝牙
        if (mService.connectionState != mService.STATE_CONNECTED){
            mService.connectScan("");
            return;
        }
        SendBleAgreement.getInstance().sendBleData(status);
    }

    /**
     * 开启fragment
     * @param fg
     */
    private void showFragment(final Fragment fg, final boolean b, final int containerViewId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
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


    // 按两次退出
    protected long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showMsg(getString(R.string.press_one_more_time_to_exit_app));
                exitTime = System.currentTimeMillis();
            } else {
                releaseResource();
                ActivitysLifecycle.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 释放资源
     */
    private void releaseResource(){
        //断开蓝牙
        mService.disconnect();
        //关闭服务
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResource();
    }
}
