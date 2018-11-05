package net.edaibu.easywalking.utils.bletooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import net.edaibu.easywalking.service.BleService;
import net.edaibu.easywalking.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 发送指定的蓝牙协议
 */
public class SendBleAgreement {

    //单列对象
    private static SendBleAgreement sendBleAgreement = new SendBleAgreement();
    //线程池
    private ExecutorService fixedThreadPool_ble = Executors.newSingleThreadExecutor();
    //蓝牙服务对象
    public BleService bleService = null;
    //蓝牙适配器
    public BluetoothAdapter bluetoothAdapter = null;
    private android.os.Handler mHandler = new android.os.Handler();

    public static SendBleAgreement getInstance() {
        return sendBleAgreement;
    }

    public void init(BleService bleService,BluetoothAdapter bluetoothAdapter) {
        this.bleService = bleService;
        this.bluetoothAdapter=bluetoothAdapter;
    }

    /**
     *
     * @param bleStatus:指定的蓝牙协议
     */
    public void sendBleData(final int bleStatus){
        byte[] bleByte=null;
        boolean isTimeOut=false;
        switch (bleStatus){
            //获取认证命令
            case BleStatus.BLE_CERTIFICATION_ING:
                  bleByte=BleAgreement.checkLock("");
                  isTimeOut=true;
                  break;
            //获取开锁命令
            case BleStatus.BLE_OPEN_LOCK_ING:
                  bleByte=AesUtils.encrypt(BleAgreement.openLock(""));
                  isTimeOut=true;
                  break;
            //获取关锁命令
            case BleStatus.BLE_CLOSE_LOCK_ING:
                  bleByte=AesUtils.encrypt(BleAgreement.closeLock(""));
                  isTimeOut=true;
                  break;
            //获取闪动或响铃命令
            case BleStatus.BLE_FLASH_ING:
                  bleByte=AesUtils.encrypt(BleAgreement.findLock("",0x00));
                  isTimeOut=true;
                  break;
            //获取结算锁车命令
            case BleStatus.BLE_PAY_CLOSE_LOCK_ING:
                  bleByte=AesUtils.encrypt(BleAgreement.balanceCar(""));
                  isTimeOut=true;
                  break;
            //获取旋轮DIY命令
            case BleStatus.BLE_SEND_DIY_ING:
                  bleByte=AesUtils.encrypt(BleAgreement.sendDIY());
                  isTimeOut=true;
                  break;
              default:
                  break;
        }
        //发送蓝牙命令
        sendData(bleByte,isTimeOut);
    }


    /**
     * 发送蓝牙命令
     * @param bleByte:指定的蓝牙协议
     * @param isTimeOut:是否开启接收数据的超时计时器
     */
    private  void sendData(final byte[] bleByte,final boolean isTimeOut){
        fixedThreadPool_ble.execute(new Runnable() {
            public void run() {
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        boolean b = bleService.writeRXCharacteristic(bleByte, isTimeOut);
                        //如果发送失败，就重发一次
                        if (!b) {
                            b = bleService.writeRXCharacteristic(bleByte, isTimeOut);
                            if (!b) {
                                LogUtils.e("发送数据第二次失败");
                                bleService.stopTimeOut();
                                Intent intent = new Intent(bleService.ACTION_INTERACTION_TIMEOUT);
                                bleService.sendBroadcast(intent);
                            }
                        }
                    }
                },200);
            }
        });
    }


    /***
     * 判断蓝牙是否开启
     *
     * @return true 已开启 false 关闭
     */
    public boolean isEnabled(Activity activity) {
        //软饮用，防止内存泄漏
        SoftReference<Activity> activitySoftReference = new SoftReference<>(activity);
        // 确保蓝牙在设备上可以开启
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activitySoftReference.get().startActivity(enableBtIntent);
            return false;
        }
        return true;
    }
}
