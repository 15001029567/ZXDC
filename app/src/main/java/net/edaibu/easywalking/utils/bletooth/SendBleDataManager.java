package net.edaibu.easywalking.utils.bletooth;

import net.edaibu.easywalking.service.BleService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 队列发送蓝牙命令
 * Created by lyn on 2017/4/28.
 */

public class SendBleDataManager {

    private static SendBleDataManager sendBleData = new SendBleDataManager();
    private ExecutorService fixedThreadPool_ble = Executors.newSingleThreadExecutor();
    private BleService mService;

    private SendBleDataManager() {
    }


    public static SendBleDataManager getInstance() {
        return sendBleData;
    }


    public void init(BleService mService) {
        this.mService = mService;
    }

    /**
     *
     * @param data  发送的蓝牙命令
     * @param isTimeOut  是否开启超时 计时器
     */
    public void sendData(final byte[] data,final boolean isTimeOut) {
        fixedThreadPool_ble.execute(new Runnable() {
            public void run() {

            }
        });
    }

}
