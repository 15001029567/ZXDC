package net.edaibu.easywalking.application;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.Util;

public class MyApplication extends Application {

    public static Gson gson;
    public static MyApplication application;
    public static SPUtil spUtil;
    public void onCreate() {
        super.onCreate();
        application = this;
        gson = new Gson();
        spUtil = SPUtil.getInstance(this);

        //初始化bugly异常捕获
        initBugly();

        //初始化百度地图
        initMap();
    }


    /**
     * 初始化bugly异常捕获
     */
    private void initBugly(){
        try {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
            strategy.setAppChannel(Util.getChannel(this));  //设置渠道
            strategy.setAppVersion(Util.getVersionCode(this)+"");      //App的版本
            CrashReport.initCrashReport(this, "52c6ece492", false, strategy);
        }catch (Exception e){

        }
    }


    /**
     * 初始化百度地图
     */
    private void initMap(){
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);
    }
}
