package net.edaibu.easywalking.persenter.main;

import net.edaibu.easywalking.bean.Balance;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.persenter.BasePersenter;
import net.edaibu.easywalking.service.BleService;

/**
 * 首页MainActivity 的接口
 */
public interface MainPersenter extends BasePersenter {


    /**
     * 初始化蓝牙服务
     * @param bleService
     */
     void initBleService(BleService bleService);


    /**
     * 发送蓝牙指令
     * @param status
     */
    void sendBleCmd(int status);


    /**
     * 设置车辆/订单对象
     * @param bikeBean
     */
    void setBikeBean(BikeBean bikeBean);


    /**
     * 扫码开锁后获取骑行单
     */
    void showCycling(boolean isScan);


    /**
     * 查询订单信息
     */
    void getOrderInfo();

    /**
     * 获取订单后展示订单界面
     */
    void showOrderInfo();


    /**
     * 随机预约时，先查询车辆信息
     */
    void showBespoke();


    /**
     * 预约成功后扫描蓝牙并响铃
     */
    void bespokeBell();

    /**
     * 关闭预约fragment界面
     */
    void closeBespokeUI();


    /**
     * 结算还车
     * @param balance
     */
    void balance(Balance balance);
}
