package net.edaibu.easywalking.utils.bletooth;

/**
 * 发送蓝牙指令的状态
 */
public class BleStatus {

    /**
     * 正常状态（没有做任何操作的状态）
     */
    public static final int BLE_NORMAL_STATE = 0;

    /**
     * 认证中
     */
    public static final int BLE_CERTIFICATION_ING = 1;

    /**
     * 认证成功
     */
    public static final int BLE_CERTIFICATION_SUCCESS= 2;

    /**
     * 开锁中
     */
    public static final int BLE_OPEN_LOCK_ING = 3;

    /**
     * 开锁成功
     */
    public static final int BLE_OPEN_LOCK_SUCCESS = 4;

    /**
     * 开锁失败
     */
    public static final int BLE_OPEN_LOCK_FAILURE = 5;

    /**
     * 临时关锁中
     */
    public static final int BLE_CLOSE_LOCK_ING = 6;

    /**
     * 临时关锁成功
     */
    public static final int BLE_CLOSE_LOCK_SUCCESS = 7;

    /**
     * 临时关锁失败:当前处于禁停区
     */
    public static final int BLE_CLOSE_LOCK_FAILURE_JTQ = 8;

    /**
     * 临时关锁失败:当前处于骑行围栏外
     */
    public static final int BLE_CLOSE_LOCK_FAILURE_QXWL = 9;

    /**
     * 闪灯或响铃中
     */
    public static final int BLE_FLASH_ING = 10;

    /**
     * 闪灯或响铃成功
     */
    public static final int BLE_FLASH_SUCCESS = 11;

    /**
     * 闪灯或响铃失败
     */
    public static final int BLE_FLASH_FAILURE = 12;

    /**
     * 结算锁车中
     */
    public static final int BLE_PAY_CLOSE_LOCK_ING = 13;

    /**
     * 结算锁车成功
     */
    public static final int BLE_PAY_CLOSE_LOCK_SUCCESS = 14;

    /**
     * 结算锁车失败：当前处于禁停区
     */
    public static final int BLE_PAY_CLOSE_LOCK_FAILURE_JTQ = 15;

    /**
     * 结算锁车失败：当前处于骑行围栏外
     */
    public static final int BLE_PAY_CLOSE_LOCK_FAILURE_QJWL = 16;


    /**
     * 强制锁车成功
     */
    public static final int FORCE_CLOSE_LOCK_SUCCESS=17;

    /**
     * 强制锁车失败
     */
    public static final int FORCE_CLOSE_LOCK_FAILURE=18;


}
