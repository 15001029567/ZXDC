package net.edaibu.easywalking.persenter;

import net.edaibu.easywalking.bean.BikeBean;

/**
 * 扫码的MVP接口类
 */
public interface ScanPersenter extends BasePersenter{

    /**
     * 获得车辆信息
     * @param bikeData
     */
    void getBikeBean(BikeBean.BikeData bikeData);
}
