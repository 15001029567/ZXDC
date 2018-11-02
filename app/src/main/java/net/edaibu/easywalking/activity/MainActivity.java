package net.edaibu.easywalking.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.fragment.MapFragment;
import net.edaibu.easywalking.service.BleService;
import net.edaibu.easywalking.utils.ActivitysLifecycle;
import net.edaibu.easywalking.utils.bletooth.SendBleAgreement;
import net.edaibu.easywalking.view.DialogView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity{

    //蓝牙服务对象
    public BleService mService = null;
    //蓝牙适配器
    public BluetoothAdapter mBtAdapter = null;
    //自定义dialog
    public DialogView dialogView;
    //地图的fragment
    private MapFragment mapFragment=new MapFragment();
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
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                //扫描不到指定蓝牙设备
                case BleService.ACTION_NO_SCAN_BLE_DEVICE:
                      dialogView = new DialogView(dialogView, mContext, getString(R.string.can_not_find_bluttooth_please_close_bike_and_connect_custom_service), getString(R.string.known), null, null, null);
                      dialogView.show();
                      break;
                  default:
                      break;
            }
        }
    };

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
