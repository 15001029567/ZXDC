package net.edaibu.easywalking.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.scan.ScanActivity;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.Balance;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.fragment.BespokeFragment;
import net.edaibu.easywalking.fragment.CyclingFragment;
import net.edaibu.easywalking.fragment.MapFragment;
import net.edaibu.easywalking.persenter.main.MainPersenter;
import net.edaibu.easywalking.persenter.main.MainPersenterImpl;
import net.edaibu.easywalking.service.BleService;
import net.edaibu.easywalking.utils.ActivitysLifecycle;
import net.edaibu.easywalking.utils.Constant;
import net.edaibu.easywalking.utils.LogUtils;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.StatusBarUtils;
import net.edaibu.easywalking.utils.Util;
import net.edaibu.easywalking.utils.WakeLockUtil;
import net.edaibu.easywalking.utils.bletooth.BleStatus;
import net.edaibu.easywalking.utils.bletooth.ParseBleDataTask;
import net.edaibu.easywalking.utils.bletooth.SendBleAgreement;
import net.edaibu.easywalking.view.DialogView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements MainPersenter,View.OnClickListener{

    private MainPersenterImpl mainPersenter;
    //蓝牙指令状态
    private int BLE_STATUS= BleStatus.BLE_NORMAL_STATE;
    //蓝牙服务对象
    public BleService bleService;
    //地图的fragment
    private MapFragment mapFragment=new MapFragment();
    //骑行界面的fragment
    private CyclingFragment cyclingFragment=new CyclingFragment();
    //预约界面的fragment
    private BespokeFragment bespokeFragment=new BespokeFragment();
    //车辆对象
    private static BikeBean bikeBean;
    private Handler mHandler=new Handler();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_main);
        //初始化MVP接口
        initPersenter();
        //初始化控件
        initView();
        //初始化蓝牙服务
        initBleService();
        //注册广播
        registerBoradcastReceiver();
        //保持CPU唤醒
        WakeLockUtil.getInstance().acquireWakeLock(mContext);
    }

    /**
     * 初始化MVP接口
     */
    private void initPersenter(){
        mainPersenter=new MainPersenterImpl(MainActivity.this,this);
        //打开地图fragment
        mapFragment.setMainCallBack(this);
        mainPersenter.showMapFragment(mapFragment);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        findViewById(R.id.img_scan).setOnClickListener(this);
        findViewById(R.id.img_bespoke).setOnClickListener(this);
    }

    /**
     * 初始化蓝牙服务
     */
    private void initBleService() {
        mainPersenter.initBleService();
    }


    /**
     * 按钮点击事件
     * @param v
     */
    public void onClick(View v) {
        if(!Util.isLogin(this)){
            return;
        }
        switch (v.getId()){
            //扫码用车
            case R.id.img_scan:
                  setClass(ScanActivity.class, Constant.SCAN_BACK);
                  break;
            //随机预约
            case R.id.img_bespoke:
                  mainPersenter.getRandomBespoke();
                  break;
        }
    }


    /**
     * 发送蓝牙指令
     * @param status：蓝牙指令类型
     */
    public void sendBleCmd(int status){
        this.BLE_STATUS=status;
        //扫描并连接蓝牙
        if (bleService.connectionState != bleService.STATE_CONNECTED){
            //保存要发送的蓝牙命令类型
            MyApplication.spUtil.addInt(SPUtil.SEND_BLE_STATUS,status);
            bleService.connectScan(bikeBean.getBikeNumber());
            return;
        }
        SendBleAgreement.getInstance().sendBleData(status,bikeBean.getImei(),"");
    }


    /**
     * 注册广播
     */
    private void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BleService.ACTION_NO_SCAN_BLE_DEVICE);//扫描不到指定蓝牙设备
        myIntentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);//蓝牙断开连接
        myIntentFilter.addAction(BleService.ACTION_ENABLE_NOTIFICATION_SUCCES);//初始化通信通道成功
        myIntentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);//获取到锁的回执数据
        myIntentFilter.addAction(BleService.ACTION_INTERACTION_TIMEOUT);//接收锁回执数据超时
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                //扫描不到指定蓝牙设备
                case BleService.ACTION_NO_SCAN_BLE_DEVICE:
                      clearTask();
                      DialogView dialogView = new DialogView(mContext,getString(R.string.not_find_bluttooth), getString(R.string.confirm), null, null, null);
                      dialogView.show();
                      break;
                //蓝牙连接断开
                case BleService.ACTION_GATT_DISCONNECTED:
                      clearTask();
                      final int status=intent.getIntExtra("status",0);
                      if(status!=0){
                          showMsg(getString(R.string.bluetooth_disconnected_please_try_again));
                      }
                      break;
                //蓝牙通信通道成功
                case BleService.ACTION_ENABLE_NOTIFICATION_SUCCES:
                      LogUtils.e("蓝牙初始化通信通道成功");
                      //发送认证命令
                      sendBleCmd(BleStatus.BLE_CERTIFICATION_ING);
                      break;
                //获取到锁的回执数据
                case BleService.ACTION_DATA_AVAILABLE:
                      final byte[] bleData = intent.getByteArrayExtra(BleService.ACTION_EXTRA_DATA);
                      final int resultCode= ParseBleDataTask.parseData(mContext,bleData);
                      lockResult(resultCode);
                      break;
                //接收锁回执数据超时
                case BleService.ACTION_INTERACTION_TIMEOUT:
                      clearTask();
                      showMsg(getString(R.string.Receive_data_timeout_please_try_again));
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
        BLE_STATUS=resultCode;
        switch (resultCode){
            //无任何数据回执
            case BleStatus.BLE_NORMAL_STATE:
                  clearTask();
                  break;
            //认证成功后
            case BleStatus.BLE_CERTIFICATION_SUCCESS:
                  BLE_STATUS=MyApplication.spUtil.getInteger(SPUtil.SEND_BLE_STATUS);
                  MyApplication.spUtil.removeMessage(SPUtil.SEND_BLE_STATUS);
                  if(BLE_STATUS!=0){
                      //先生成骑行单再开锁
                      if(BLE_STATUS==BleStatus.BLE_OPEN_LOCK_ING && Constant.PLAY_STATUS!=1){
                          //获取骑行单
                          mainPersenter.getOrderByScan(bikeBean.getBikeCode());
                          return;
                      }
                      sendBleCmd(BLE_STATUS);
                  }
                  break;
             //开锁成功
            case BleStatus.BLE_OPEN_LOCK_SUCCESS:
                  clearTask();
                  break;
            //开锁失败
            case BleStatus.BLE_OPEN_LOCK_FAILURE:
                  break;
             //临时关锁成功
            case BleStatus.BLE_CLOSE_LOCK_SUCCESS:
                  break;
            //临时关锁失败:当前处于禁停区
            case BleStatus.BLE_CLOSE_LOCK_FAILURE_JTQ:
                  break;
            //临时关锁失败:当前处于骑行围栏外
            case BleStatus.BLE_CLOSE_LOCK_FAILURE_QXWL:
                break;
            //结算关锁成功
            case BleStatus.BLE_PAY_CLOSE_LOCK_SUCCESS:
                  break;
             //结算锁车失败：当前处于禁停区
            case BleStatus.BLE_PAY_CLOSE_LOCK_FAILURE_JTQ:
                  break;
            //结算锁车失败：当前处于骑行围栏外
            case BleStatus.BLE_PAY_CLOSE_LOCK_FAILURE_QJWL:
                break;
            //强制锁车成功
            case BleStatus.FORCE_CLOSE_LOCK_SUCCESS:
                  break;
            //强制锁车失败
            case BleStatus.FORCE_CLOSE_LOCK_FAILURE:
                 break;
            //响铃成功
            case BleStatus.BLE_FLASH_SUCCESS:
                 clearTask();
                 break;
            //响铃失败
            case BleStatus.BLE_FLASH_FAILURE:
                  clearTask();
                  showMsg(getString(R.string.ring_failed));
                 break;
            default:
                  break;
        }
    }


    /**
     * 展示骑行界面
     * @param bikeBean
     * @param isScan：是否是退出后重新查询的订单
     */
    public void showCycling(BikeBean bikeBean,boolean isScan) {
        Constant.PLAY_STATUS=1;
        cyclingFragment.setBikeBean(bikeBean,this);
        mainPersenter.showFragment(cyclingFragment);
        //查询电子围栏
        mapFragment.findFencing(bikeBean.getBikeCode());
        //查询附近的停放点
        mapFragment.getParking();
        //发送蓝牙开锁
        if(!isScan){
            sendBleCmd(BLE_STATUS);
        }
    }


    /**
     * 查询订单信息
     */
    public void getOrderInfo() {
        mainPersenter.getOrderInfo();
    }


    /**
     * 获取订单后展示订单界面
     * @param bikeBean
     */
    public void showOrderInfo(BikeBean bikeBean) {
        //有预约单
        if(TextUtils.isEmpty(bikeBean.getCyclingId())){
            showBespoke(bikeBean);
            return;
        }
        //有骑行单
        showCycling(bikeBean,true);
    }


    /**
     * 展示预约界面
     * @param bikeBean
     */
    public void showBespoke(BikeBean bikeBean) {
        Constant.PLAY_STATUS=2;
        MainActivity.bikeBean=bikeBean;
        bespokeFragment.setBikeBean(bikeBean,this);
        //打开预约的界面
        mainPersenter.showFragment(bespokeFragment);
        //设置路径规划
        mapFragment.setRoutePlan(bikeBean.getLatitude(),bikeBean.getLongitude());
        //预约成功后扫描蓝牙并响铃
//        bespokeBell();
    }


    /**
     * 预约成功后扫描蓝牙并响铃
     */
    public void bespokeBell(){
//        if(Constant.PLAY_STATUS==2 && bleService.isDisconnect()){
//            if(TextUtils.isEmpty(bikeBean.getResserveId())){
//                return;
//            }
//            mHandler.postDelayed(new Runnable() {
//                public void run() {
//                    sendBleCmd(BleStatus.BLE_FLASH_ING);
//
//                }
//            },10000);
//        }

    }

    /**
     * 关闭预约fragment界面
     */
    public void closeBespokeUI(){
        Constant.PLAY_STATUS=0;
        //关闭预约界面
        mainPersenter.showFragment(bespokeFragment);
        //显示出附近的车辆
        mapFragment.showBikeMark();
        //断开蓝牙
        bleService.disconnect();
    }


    /**
     * 结算还车
     * @param balance
     */
    public void balance(Balance balance){
        Constant.PLAY_STATUS=0;
        mapFragment.clearMap();
        mainPersenter.showFragment(cyclingFragment);
        //断开蓝牙
        bleService.disconnect();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            //扫码回执
            case Constant.SCAN_BACK:
                 bikeBean= (BikeBean) data.getSerializableExtra("bikeBean");
                 showProgress(getString(R.string.opening_lock),false);
                 sendBleCmd(BleStatus.BLE_OPEN_LOCK_ING);
                 break;
            default:
                break;
        }
    }


    /**
     * 初始化蓝牙
     * @param bleService
     */
    public void initBleService(BleService bleService) {
        this.bleService=bleService;
    }

    /**
     * 展示加载滚动条
     */
    public void showLoding(String msg) {
        showProgress(msg,true);
    }

    /**
     * 关闭加载滚动条
     */
    public void closeLoding() {
        clearTask();
    }

    /**
     * 展示Toast数据
     * @param msg
     */
    public void showToast(String msg) {
        showMsg(msg);
    }


    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询用户详情
        mainPersenter.getUserInfo();
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
        mainPersenter.disconnect();
        //关闭广播
        unregisterReceiver(mBroadcastReceiver);
        mainPersenter.onDestory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResource();
    }

}
