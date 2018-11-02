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
     * 开锁中
     */
    public static final int BLE_OPEN_LOCK_ING = 2;

    /**
     * 开锁成功
     */
    public static final int BLE_OPEN_LOCK_SUCCESS = 3;

    /**
     * 开锁失败
     */
    public static final int BLE_OPEN_LOCK_FAILURE = 4;

    /**
     * 关锁中
     */
    public static final int BLE_CLOSE_LOCK_ING = 5;

    /**
     * 关锁成功
     */
    public static final int BLE_CLOSE_LOCK_SUCCESS = 6;

    /**
     * 关锁失败
     */
    public static final int BLE_CLOSE_LOCK_FAILURE = 7;

    /**
     * 闪灯或响铃中
     */
    public static final int BLE_FLASH_ING = 8;

    /**
     * 闪灯或响铃成功
     */
    public static final int BLE_FLASH_SUCCESS = 9;

    /**
     * 闪灯或响铃失败
     */
    public static final int BLE_FLASH_FAILURE = 10;

    /**
     * 结算锁车中
     */
    public static final int BLE_PAY_CLOSE_LOCK_ING = 11;

    /**
     * 结算锁车成功
     */
    public static final int BLE_PAY_CLOSE_LOCK_SUCCESS = 12;

    /**
     * 结算锁车失败
     */
    public static final int BLE_PAY_CLOSE_LOCK_FAILURE = 13;

    /**
     * 发送旋轮命令中
     */
    public static final int BLE_SEND_DIY_ING=14;

    /**
     * 发送旋轮命令成功
     */
    public static final int BLE_SEND_DIY_SUCCESS=15;

    /**
     * 发送炫轮命令失败
     */
    public static final int BLE_SEND_DIY_FAILURE=16;
}
