package net.edaibu.easywalking.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import net.edaibu.easywalking.application.MyApplication;

import java.util.List;

public class SPUtil {

    public static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static char pad = '=';

    //是否是第一次打开APP
    public final static String IS_FIRST_OPEN="is_first_open";
    //当前定位的经纬度
    public final static String LOCATION_LAT="location_lat";
    public final static String LOCATION_LONG="location_long";
    //登陆的token
    public final static String ACCESS_TOKEN = "access_token";
    public final static String AUTH_TOKEN = "auth_token";
    //保存要发送的蓝牙命令类型
    public final static String SEND_BLE_STATUS="send_ble_status";
    //蓝牙的mac地址
    public final static String DEVICE_MAC="device_mac";

    private SharedPreferences shar;
    private Editor editor;
    public final static String USERMESSAGE = "zxdc";
    private static SPUtil sharUtil = null;
    @SuppressLint("WrongConstant")
    private SPUtil(Context context, String sharname) {
        shar = context.getSharedPreferences(sharname, Context.MODE_PRIVATE + Context.MODE_APPEND);
        editor = shar.edit();
    }

    public static SPUtil getInstance(Context context) {
        if (null == sharUtil) {
            sharUtil = new SPUtil(context, USERMESSAGE);
        }
        return sharUtil;
    }


    //添加String信息
    public void addString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    //添加int信息
    public void addInt(String key, Integer value) {
        editor.putInt(key, value);
        editor.commit();
    }

    //添加boolean信息
    public void addBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    //添加float信息
    public void addFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    //添加long信息
    public void addLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    //添加list
    public void addList(String key, List<Object> list) {
        editor.putString(key, MyApplication.gson.toJson(list));
        LogUtils.e(MyApplication.gson.toJson(list));
        editor.commit();
    }


    public void addObject(String key,Object object){
        editor.putString(key,MyApplication.gson.toJson(object));
        editor.commit();
    }


    public Object getObject(String key,Class myClass){
        final String value=shar.getString(key,null);
        if(!TextUtils.isEmpty(value)){
            return MyApplication.gson.fromJson(value,myClass);
        }
        return null;
    }


    public void removeMessage(String delKey) {
        editor.remove(delKey);
        editor.commit();
    }

    public void removeAll() {
        editor.clear();
        editor.commit();
    }

    public String getString(String key) {
        return shar.getString(key, "");
    }

    public Integer getInteger(String key) {
        return shar.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return shar.getBoolean(key, false);
    }

    public float getFloat(String key) {
        return shar.getFloat(key, 0);
    }

    public long getLong(String key) {
        return shar.getLong(key, 0);
    }

}
