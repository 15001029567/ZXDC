package net.edaibu.easywalking.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.utils.LogUtils;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.TimerUtil;
import net.edaibu.easywalking.utils.bletooth.AesUtils;
import net.edaibu.easywalking.utils.bletooth.ByteStringHexUtil;
import net.edaibu.easywalking.utils.bletooth.ByteUtil;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 蓝牙Service
 */
public class BleService extends Service {
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_CHAR_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_CHAR_UUID = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
    /**
     * 开始扫描信标
     */
    public final static String ACTION_START_SCAN_MARK = "net.edaibu.adminapp.ACTION_START_SCAN_MARK";

    /**
     * 扫描到了信标
     */
    public final static String ACTION_SCAN_MARK_SUCCESS = "net.edaibu.adminapp.ACTION_SCAN_MARK_SUCCESS";

    /**
     * 没扫描到信标
     */
    public final static String ACTION_SCAN_MARK_FAILURE = "net.edaibu.adminapp.ACTION_SCAN_MARK_FAILURE";
    /**
     * 断开连接
     */
    public final static String ACTION_GATT_DISCONNECTED = "net.edaibu.adminapp.ACTION_GATT_DISCONNECTED";
    /**
     * 接收到了数据
     */
    public final static String ACTION_DATA_AVAILABLE = "net.edaibu.adminapp.ACTION_DATA_AVAILABLE";
    /**
     * 发送接到的数据的KEY
     */
    public final static String ACTION_EXTRA_DATA = "net.edaibu.adminapp.ACTION_EXTRA_DATA";
    /**
     * 通道建立成功
     */
    public final static String ACTION_ENABLE_NOTIFICATION_SUCCES = "net.edaibu.adminapp.ACTION_ENABLE_NOTIFICATION_SUCCES";
    /**
     * 没有发现指定蓝牙
     */
    public final static String ACTION_NO_DISCOVERY_BLE = "net.edaibu.adminapp.ACTION_NO_DISCOVERY_BLE";
    /**
     * 数据交互超时
     */
    public final static String ACTION_INTERACTION_TIMEOUT = "net.edaibu.adminapp.ACTION_INTERACTION_TIMEOUT";

    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice bleDevice;
    public int connectionState = STATE_DISCONNECTED;
    //连接断开
    public static final int STATE_DISCONNECTED = 0;
    //开始连接
    public static final int STATE_CONNECTING = 1;
    //连接成功
    public static final int STATE_CONNECTED = 2;
    //蓝牙名称
    private String bleName;
    //timeOut：发送命令超时         scanTime:扫描蓝牙超时
    private long timeOut = 1000 * 7, scanTime = 1000 * 15;
    private TimerUtil timerUtil, startUtil, scanMarkTimeOut;
    private Handler handler = new Handler();
    //信标的蓝牙名称
    private String markName = "ZX-PARK";

    public class LocalBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    /**
     * 创建蓝牙适配器
     *
     * @return true  successful.
     */
    public BluetoothAdapter createBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter;
    }

    /**
     * 扫描并且连接
     *
     * @param bleName 蓝牙名
     */
    public void connectScan(String bleName) {
        if (mBluetoothAdapter == null || TextUtils.isEmpty(bleName)) {
            return;
        }
        this.bleName = bleName;
        //先关闭扫描
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        //开始扫描蓝牙
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        //开始扫描蓝牙
        startUtil();
    }


    private Handler mHandler=new Handler();
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (bleName.equals(device.getName())) {
                //关闭扫描计时器
                stopStartTime();
                //停止扫描
                stopScan(mLeScanCallback);
                //保存mac地址
                MyApplication.spUtil.addString(SPUtil.DEVICE_MAC, device.getAddress());
                //连接设备蓝牙
                mHandler.post(new Runnable() {
                    public void run() {
                        final boolean isConnect=connect(device.getAddress());
                        if(!isConnect){
                            broadcastUpdate(ACTION_GATT_DISCONNECTED,-1);
                        }
                    }
                });
            }
        }
    };


    /**
     * 停止蓝牙扫描
     */
    private void stopScan(BluetoothAdapter.LeScanCallback mLeScanCallback){
        if(null!=mBluetoothAdapter){
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    /**
     * 扫描蓝牙信标
     */
    public void scanMark() {
        if (mBluetoothAdapter == null || TextUtils.isEmpty(markName)) {
            return;
        }
        //先关闭扫描
        mBluetoothAdapter.stopLeScan(scanMarkCallback);
        //开始扫描蓝牙信标
        mBluetoothAdapter.startLeScan(scanMarkCallback);
        startMarkTimeOut();
    }

    private BluetoothAdapter.LeScanCallback scanMarkCallback = new BluetoothAdapter.LeScanCallback() {
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(null==device.getName() || TextUtils.isEmpty(device.getName())){
                return;
            }
            LogUtils.e("搜索到信标：" + device.getName() + "___" + device.getAddress());
            if (device.getName().contains(markName)) {
                //关闭扫描计时器
                stopMarkTimeOut();
                //停止扫描
                stopScan(scanMarkCallback);
                //发送扫到的广播
                broadcastUpdate(ACTION_SCAN_MARK_SUCCESS);
            }
        }
    };


    /**
     * 连接指定蓝牙
     *
     * @param address 蓝牙的地址
     */
    public boolean connect(final String address) {
        connectionState = STATE_CONNECTING;
        if (mBluetoothAdapter == null || TextUtils.isEmpty(address)) {
            return false;
        }
        bleDevice = mBluetoothAdapter.getRemoteDevice(address);
        if (bleDevice == null) {
            connectionState = STATE_DISCONNECTED;
            return false;
        }
        LogUtils.e("调connectGatt开始连接");
        mBluetoothGatt = bleDevice.connectGatt(BleService.this, false, mGattCallback);
        return true;
    }

    /**
     * 初始化通道
     *
     * @return
     */
    public void enableTXNotification() {
        try {
            if (mBluetoothGatt == null) {
                disconnect();
                return;
            }
            BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
            if (RxService == null) {
                disconnect();
                return;
            }
            BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_UUID);
            if (TxChar == null) {
                disconnect();
                return;
            }
            mBluetoothGatt.setCharacteristicNotification(TxChar, true);
            BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
            broadcastUpdate(ACTION_ENABLE_NOTIFICATION_SUCCES);
        } catch (Exception e) {
            //建立通道失败，发送没有找到蓝牙广播
            broadcastUpdate(ACTION_GATT_DISCONNECTED);
        }
    }


    /**
     * 传输蓝牙数据
     */
    public boolean writeRXCharacteristic(byte[] value, boolean isTimeOut) {
//        LogUtils.e(ByteStringHexUtil.bytesToHexString(AesUtils.decrypt(value))+"++++++++++++++++"+ByteStringHexUtil.bytesToHexString(value));
        try {
            BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
            if (RxService == null) {
                disconnect();
                return false;
            }
            BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
            if (RxChar == null) {
                disconnect();
                return false;
            }
            RxChar.setValue(value);
            //开启超时计时器
            if (isTimeOut) {
                startTimeOut();
            }
            return mBluetoothGatt.writeCharacteristic(RxChar);
        } catch (Exception e) {
            disconnect();
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 发送广播
     **/
    private void broadcastUpdate(String action) {
        Intent intent = new Intent(action);
        getApplication().sendBroadcast(intent);
    }


    /**
     * 发送蓝牙连接断开的广播
     * @param action
     * @param status:断开的状态
     */
    private void broadcastUpdate(String action,int status) {
        Intent intent = new Intent(action);
        intent.putExtra("status",status);
        getApplication().sendBroadcast(intent);
    }

    /**
     * 发送广播（携带接受到的值）
     **/
    private void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        intent.putExtra(ACTION_EXTRA_DATA, characteristic.getValue());
        getApplication().sendBroadcast(intent);
    }



    /**
     * 蓝牙连接交互回调
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            LogUtils.e("status="+status+"___________");
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                LogUtils.e("蓝牙连接成功");
                connectionState = STATE_CONNECTED;
                //去发现服务
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (null == mBluetoothGatt) {
                            return;
                        }
                        mBluetoothGatt.discoverServices();
                    }
                }, 700);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                LogUtils.e("蓝牙连接断开");
                //清除缓存
                refreshDeviceCache();
                //改为连接失败状态
                connectionState = STATE_DISCONNECTED;
                //关闭超时计时器
                stopTimeOut();
                //释放蓝牙GATT
                close();
                //发送蓝牙连接断开的广播
                broadcastUpdate(ACTION_GATT_DISCONNECTED,status);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //发现服务，建立通道
                enableTXNotification();
            } else {
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }
        }

        //接收数据
        public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic,int status) {
        }

        //接收数据
        public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
            if (!TX_CHAR_UUID.equals(characteristic.getUuid())) {
                 return;
            }
            byte[] txValue = characteristic.getValue();
            if(null==txValue){
                return;
            }
            LogUtils.e("接收数据=" + ByteStringHexUtil.bytesToHexString(txValue));
            if (ByteUtil.byteToInt(txValue[2]) != 128) {
                txValue = AesUtils.decrypt(txValue);
                LogUtils.e("解密数据=" + ByteStringHexUtil.bytesToHexString(txValue));
            }
            if (txValue.length > 0) {
                final int action = ByteUtil.byteToInt(txValue[2]);
                //128到132之间是开始关锁认证等常规操作,139是还车结算,    140是租用命令的回执     147表示锁正在忙，不能处理你发的命令    152是炫轮回执数据   143是强制还车
                if ((action >= 128 && action <= 132) || action == 139 || action == 140 || action == 145 || action==146 || action==147 || action==152 || action==143) {
                    stopTimeOut();
                }
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
    };

    /**
     * 扫描信标计时器
     **/
    private void startMarkTimeOut() {
        stopMarkTimeOut();
        scanMarkTimeOut = new TimerUtil(scanTime, new TimerUtil.TimerCallBack() {
            public void onFulfill() {
                //停止扫描
                stopScan(scanMarkCallback);
                //发送广播
                broadcastUpdate(ACTION_SCAN_MARK_FAILURE);
            }
        });
        scanMarkTimeOut.start();
    }

    /**
     * 关闭信标扫描计时器
     */
    private void stopMarkTimeOut() {
        if (null!=scanMarkTimeOut) {
            scanMarkTimeOut.stop();
        }
    }


    /**
     * 扫描15秒钟
     */
    private void startUtil() {
        stopStartTime();
        startUtil = new TimerUtil(scanTime, new TimerUtil.TimerCallBack() {
            public void onFulfill() {
                //停止扫描
                stopScan(mLeScanCallback);
                //关闭扫描计时器
                stopStartTime();
                //发送没有扫到蓝牙的广播
                broadcastUpdate(ACTION_NO_DISCOVERY_BLE);
            }
        });
        startUtil.start();
    }

    /**
     * 关闭扫描蓝牙计时器
     */
    private void stopStartTime() {
        if (null != startUtil) {
            startUtil.stop();
        }
    }


    /**
     * 开始计时超时时间
     **/
    private synchronized void startTimeOut() {
        stopTimeOut();
        timerUtil = new TimerUtil(timeOut, new TimerUtil.TimerCallBack() {
            public void onFulfill() {
                LogUtils.e("发送超时广播");
                broadcastUpdate(ACTION_INTERACTION_TIMEOUT);
            }
        });
        timerUtil.start();
    }

    /**
     * 发送命令超时计时
     **/
    public void stopTimeOut() {
        if (null!=timerUtil) {
            timerUtil.stop();
        }
    }


    /**
     * 清楚蓝牙缓存（反射）
     **/
    public boolean refreshDeviceCache() {
        if (mBluetoothGatt != null) {
            try {
                BluetoothGatt localBluetoothGatt = mBluetoothGatt;
                Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                if (localMethod != null) {
                    boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
                    return bool;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 断开BluetoothGatt连接
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        LogUtils.e("gatt释放了");
    }
}
