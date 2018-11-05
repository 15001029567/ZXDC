package net.edaibu.easywalking.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import net.edaibu.easywalking.activity.login.LoginActivity;
import net.edaibu.easywalking.application.MyApplication;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Administrator
 */
public class Util extends ClassLoader {
    /**
     * 判断是否输入表情符号
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }


    /**
     * 保留两位小数的double数据
     * @param d
     * @return
     */
    public static String setDouble(double d){
        final DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    /**
     * 只允许字母、数字和汉字
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean stringFilter(String str)throws PatternSyntaxException {
        String   regEx  =  "^[\\u4e00-\\u9fa5*a-zA-Z0-9]+$";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.matches();
    }


    /**
     * 获取当前系统的版本号
     *
     * @return
     */
    public static int getVersionCode(Context mContext) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取当前系统的版本名称
     *
     * @return
     */
    public static String getVersionName(Context mContext) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取渠道名称
     * @return
     */
    public static String getChannel(Context mContext){
        final ApplicationInfo appInfo;
        try {
            appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            final String utmSource = appInfo.metaData.get("SENSORS_ANALYTICS_UTM_SOURCE")+"";
            return utmSource;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 设备唯一id
     * @param context
     * @return
     */
    public static String getDeviceId(Context context){
        final TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        //设备唯一id
        return telephonyMgr.getDeviceId();
    }


    /**
     * 获取屏幕宽高
     * @param context
     * @param type
     * @return
     */
    public static int getDeviceWH(Context context,int type){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if(type==1){
            return width;
        }else{
            return height;
        }
    }


    /**
     * 判断是否登陆
     *
     * @return
     */
    public boolean isLogin(Context mContext) {
        if (TextUtils.isEmpty(MyApplication.spUtil.getString(SPUtil.ACCESS_TOKEN))) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }

}
