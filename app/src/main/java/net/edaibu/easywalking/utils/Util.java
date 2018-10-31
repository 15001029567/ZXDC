package net.edaibu.easywalking.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.view.DialogView;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Administrator
 */
public class Util extends ClassLoader {
    static double DEF_PI = 3.14159265359; // PI
    static double DEF_2PI = 6.28318530712; // 2*PI
    static double DEF_PI180 = 0.01745329252; // PI/180.0
    static double DEF_R = 6370693.5; // radius of earth
    public static DialogView dialogView;
    //当前网络是否可用
    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(null!=info && info.getState()== State.CONNECTED){
            return true;
        }
        return false;
    }


    /**
     * 设置手机网络
     */
    public static void setNetWork(final Context mContext) {
        if(null!=dialogView && dialogView.isShowing()){
            return;
        }
        dialogView = new DialogView(mContext, mContext.getString(R.string.network_can_not_be_accessed_please_check_the_network_connection), mContext.getString(R.string.configure), mContext.getString(R.string.cancel), new View.OnClickListener() {
            public void onClick(View v) {
                dialogView.dismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                mContext.startActivity(intent);
            }
        },null);
        dialogView.show();
    }

    /**
     * 计算两对经纬度的距离
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        System.out.println(distance);
        return distance;
    }


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
     * 抖动的动画特效
     * @return
     */
    public static Animation genDefaultAnimation() {
        TranslateAnimation animtion = new TranslateAnimation(0, 10, 0, 0);
        animtion.setInterpolator(new CycleInterpolator(3.0f));
        animtion.setDuration(1000);
        return animtion;
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
     * 网络类型
     *
     * @return
     */
    public static String getNetTypeName(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo == null) {
                return "无网络";
            }
            if (networkInfo != null && networkInfo.isAvailable()) {
                //获取网络类型
                int currentNetWork = networkInfo.getType();
                if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
                    if (networkInfo.getExtraInfo() != null) {
                        if (networkInfo.getExtraInfo().equals("cmnet")) {
                            return "移动CMNET网络";
                        }
                        if (networkInfo.getExtraInfo().equals("cmwap")) {
                            return "移动CMWAP网络";
                        }
                        if (networkInfo.getExtraInfo().equals("uniwap")) {
                            return "联通UNIWAP网络";
                        }
                        if (networkInfo.getExtraInfo().equals("3gwap")) {
                            return "联通3GWAP网络";
                        }
                        if (networkInfo.getExtraInfo().equals("3gnet")) {
                            return "联通3GNET网络";
                        }
                        if (networkInfo.getExtraInfo().equals("uninet")) {
                            return "联通UNINET网络";
                        }
                        if (networkInfo.getExtraInfo().equals("ctwap")) {
                            return "电信CTWAP网络";
                        }
                        if (networkInfo.getExtraInfo().equals("ctnet")) {
                            return "电信CTNET网络";
                        }
                    }
                    //WIFI网络类型
                } else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
                    return "wifi";
                }
            }
        }
        return "未知网络";
    }



    /**
     *获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*
    * 设置控件所在的位置YY，并且不改变宽高，
    * XY为绝对位置
    */
    public static void setLayout(View view,int x,int y) {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }


    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    /**
     *  两次点击按钮之间的点击间隔不能少于700毫秒
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
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
}
