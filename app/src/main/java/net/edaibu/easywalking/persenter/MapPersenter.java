package net.edaibu.easywalking.persenter;

import com.baidu.mapapi.model.LatLng;

import net.edaibu.easywalking.bean.BikeList;
import net.edaibu.easywalking.bean.Fanceing;

import java.util.List;

/**
 * 地图MapFragment 的接口
 */
public interface MapPersenter extends BasePersenter{


    /**
     * 定位成功
     * @param latLng
     */
    void locationSuccess(LatLng latLng);


    /**
     * 显示电子围栏，禁停区，禁行区等
     * @param fanceing
     */
     void showFencing(Fanceing fanceing);


    /**
     * 显示附近的车辆信息
     * @param list
     */
    void showLocationBike(List<BikeList.BikeInfoList> list);

}
