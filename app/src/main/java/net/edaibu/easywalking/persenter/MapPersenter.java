package net.edaibu.easywalking.persenter;

import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.service.BleService;

/**
 * 地图MapFragment 的接口
 */
public interface MapPersenter extends BasePersenter{


    /**
     * 显示电子围栏，禁停区，禁行区等
     * @param fanceing
     */
     void showFencing(Fanceing fanceing);

}
