package net.edaibu.easywalking.persenter.scan;

import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.persenter.BasePersenter;

/**
 * 扫码的MVP接口类
 */
public interface ScanPersenter extends BasePersenter {

    /**
     * 获得车辆信息
     * @param bikeBean
     */
    void getBikeBean(BikeBean bikeBean);
}
