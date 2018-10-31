package net.edaibu.easywalking.utils.error;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class BuglyUtils {


    public static void setOpenLockFaile(){
        Throwable throwable=new Throwable("扫码开锁时，车锁开锁失败");
        CrashReport.postCatchedException(throwable);
    }

}
