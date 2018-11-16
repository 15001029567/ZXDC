package net.edaibu.easywalking.application;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import net.edaibu.easywalking.utils.ActivitysLifecycle;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.Util;
import net.edaibu.easywalking.utils.error.CockroachUtil;

public class MyApplication extends Application {

    public static Gson gson;
    public static MyApplication application;
    public static SPUtil spUtil;
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);
        application = this;
        gson = new Gson();
        spUtil = SPUtil.getInstance(this);

        //开启小强
        CockroachUtil.install();

        //初始化bugly异常捕获
//        initBugly();

        //管理Activity
        registerActivityLifecycleCallbacks(ActivitysLifecycle.getInstance());
    }


    /**
     * 初始化bugly异常捕获
     */
    private void initBugly(){
        try {
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
            strategy.setAppChannel(Util.getChannel(this));  //设置渠道
            strategy.setAppVersion(Util.getVersionCode(this)+"");      //App的版本
            CrashReport.initCrashReport(this, "773cf12d7d", false, strategy);
        }catch (Exception e){

        }
    }
}
