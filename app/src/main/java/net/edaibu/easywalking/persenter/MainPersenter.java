package net.edaibu.easywalking.persenter;

import net.edaibu.easywalking.service.BleService;

/**
 * 首页MainActivity 的接口
 */
public interface MainPersenter extends BasePersenter{


    /**
     * 初始化蓝牙服务
     * @param bleService
     */
     void initBleService(BleService bleService);
}
