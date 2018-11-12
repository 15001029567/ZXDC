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
     * 扫码开锁后获取骑行单
     * @param bikeBean
     */
    void showCycling(BikeBean bikeBean,boolean isScan);


    /**
     * 查询订单信息
     */
    void getOrderInfo();

    /**
     * 获取订单后展示订单界面
     * @param bikeBean
     */
    void showOrderInfo(BikeBean bikeBean);


    /**
     * 随机预约时，先查询车辆信息
     * @param bikeBean
     */
    void showBespoke(BikeBean bikeBean);


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
