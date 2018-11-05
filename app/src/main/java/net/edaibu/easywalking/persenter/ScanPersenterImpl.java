package net.edaibu.easywalking.persenter;

import android.app.Activity;
/**
 * 扫码的MVP接口类
 */
public class ScanPersenterImpl {

    private Activity activity;

    private ScanPersenter scanPersenter;

    public ScanPersenterImpl(Activity activity,ScanPersenter scanPersenter){
        this.activity=activity;
        this.scanPersenter=scanPersenter;
    }
}
