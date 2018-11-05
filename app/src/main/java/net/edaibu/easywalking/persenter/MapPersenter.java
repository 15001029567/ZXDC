package net.edaibu.easywalking.persenter;

import net.edaibu.easywalking.bean.BikeInfo;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.service.BleService;

import java.util.List;

/**
 * 地图MapFragment 的接口
 */
public interface MapPersenter extends BasePersenter{


    /**
     * 显示电子围栏，禁停区，禁行区等
     * @param fanceing
     */
     void showFencing(Fanceing fanceing);


    /**
     * 显示附近的车辆信息
     * @param list
     */
    void showLocationBike(List<BikeInfo.BikeInfoList> list);

}
