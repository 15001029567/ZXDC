package net.edaibu.easywalking.persenter.main;

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
    void getOrderByScan(BikeBean bikeBean);
}
