package net.edaibu.easywalking.utils.bletooth;

import android.content.Intent;
import net.edaibu.easywalking.service.BleService;
import net.edaibu.easywalking.utils.LogUtils;
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
    //蓝牙服务类对象
    private BleService mService;
    private android.os.Handler mHandler = new android.os.Handler();

    public static SendBleAgreement getInstance() {
        return sendBleAgreement;
    }

    public void init(BleService mService) {
        this.mService = mService;
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
                        boolean b = mService.writeRXCharacteristic(bleByte, isTimeOut);
                        //如果发送失败，就重发一次
                        if (!b) {
                            b = mService.writeRXCharacteristic(bleByte, isTimeOut);
                            if (!b) {
                                LogUtils.e("发送数据第二次失败");
                                mService.stopTimeOut();
                                Intent intent = new Intent(mService.ACTION_INTERACTION_TIMEOUT);
                                mService.sendBroadcast(intent);
                            }
                        }
                    }
                },200);
            }
        });
    }
}