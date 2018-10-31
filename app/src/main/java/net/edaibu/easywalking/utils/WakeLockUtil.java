package net.edaibu.easywalking.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 * 保持cpu唤醒
 * Created by Administrator on 2017/6/12 0012.
 */

public class WakeLockUtil {

    private static WakeLockUtil wakeLockUtil;
    private  PowerManager.WakeLock wakeLock = null;

    public static WakeLockUtil getInstance(){
        if(null==wakeLockUtil){
            wakeLockUtil=new WakeLockUtil();
        }
        return wakeLockUtil;
    }


    /**
     * 保持CPU唤醒
     * @param mContext
     */
    public void acquireWakeLock(Context mContext) {
        if (null == wakeLock){
            PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }


    /**
     * 关闭
     */
    public void releaseWakeLock(){
        if (null != wakeLock){
            wakeLock.release();
            wakeLock = null;
            LogUtils.e("电源锁释放了");
        }
    }
}
