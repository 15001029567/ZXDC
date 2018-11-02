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
     * 关锁中
     */
    public static final int BLE_CLOSE_LOCK_ING = 6;

    /**
     * 关锁成功
     */
    public static final int BLE_CLOSE_LOCK_SUCCESS = 7;

    /**
     * 关锁失败
     */
    public static final int BLE_CLOSE_LOCK_FAILURE = 8;

    /**
     * 闪灯或响铃中
     */
    public static final int BLE_FLASH_ING = 9;

    /**
     * 闪灯或响铃成功
     */
    public static final int BLE_FLASH_SUCCESS = 10;

    /**
     * 闪灯或响铃失败
     */
    public static final int BLE_FLASH_FAILURE = 11;

    /**
     * 结算锁车中
     */
    public static final int BLE_PAY_CLOSE_LOCK_ING = 12;

    /**
     * 结算锁车成功
     */
    public static final int BLE_PAY_CLOSE_LOCK_SUCCESS = 13;

    /**
     * 结算锁车失败
     */
    public static final int BLE_PAY_CLOSE_LOCK_FAILURE = 14;

    /**
     * 发送旋轮命令中
     */
    public static final int BLE_SEND_DIY_ING=15;

    /**
     * 发送旋轮命令成功
     */
    public static final int BLE_SEND_DIY_SUCCESS=16;

    /**
     * 发送炫轮命令失败
     */
    public static final int BLE_SEND_DIY_FAILURE=17;
}
