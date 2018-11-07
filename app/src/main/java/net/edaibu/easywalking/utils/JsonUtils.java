package net.edaibu.easywalking.utils;

import android.text.TextUtils;

import net.edaibu.easywalking.bean.BikeBean;

import org.json.JSONObject;

/**
 * json解析工具类
 */
public class JsonUtils {
    
    public static BikeBean getBikeBean(String msg){
        if(TextUtils.isEmpty(msg)){
            return null;
        }
        BikeBean bikeBean=new BikeBean();
        try {
            final JSONObject jsonObject = new JSONObject(msg);
            bikeBean.setCode(jsonObject.getInt("code"));
            bikeBean.setMsg(jsonObject.getString("msg"));
            final JSONObject json = new JSONObject(jsonObject.getString("data"));
            bikeBean.setImei(json.isNull("imei") ? bikeBean.getImei(): json.getString("imei"));
            bikeBean.setLatitude(json.isNull("latitude") ? bikeBean.getLatitude() : json.getDouble("latitude"));
            bikeBean.setLongitude(json.isNull("longitude") ? bikeBean.getLongitude() : json.getDouble("longitude"));
            bikeBean.setBikeCode(json.isNull("bikeCode") ? bikeBean.getBikeCode() : json.getString("bikeCode"));
            bikeBean.setBikeCode(json.isNull("bikecode") ? bikeBean.getBikeCode() : json.getString("bikecode"));
            bikeBean.setBikeNumber(json.isNull("bikeNumber") ? bikeBean.getBikeNumber() : json.getString("bikeNumber"));
            bikeBean.setBikeNumber(json.isNull("bikenumber") ? bikeBean.getBikeNumber() : json.getString("bikenumber"));
            bikeBean.setResserveId(json.isNull("resserveId") ? bikeBean.getResserveId() : json.getString("resserveId"));
            bikeBean.setResserveId(json.isNull("reserveDataId") ? bikeBean.getResserveId() : json.getString("reserveDataId"));
            bikeBean.setReserveDate(json.isNull("createDate") ? bikeBean.getReserveDate() : json.getLong("createDate"));
            bikeBean.setReserveDate(json.isNull("reserveDate") ? bikeBean.getReserveDate() : json.getLong("reserveDate"));
            bikeBean.setFreeTime(json.isNull("freeTime") ? bikeBean.getFreeTime() : json.getInt("freeTime"));
            bikeBean.setFreeTime(json.isNull("reserveFreeTime") ? bikeBean.getFreeTime() : json.getInt("reserveFreeTime"));
            bikeBean.setReserveCost(json.isNull("totalCost") ? bikeBean.getReserveCost() : json.getInt("totalCost"));
            bikeBean.setReserveCost(json.isNull("reserveCost") ? bikeBean.getReserveCost() : json.getInt("reserveCost"));
            bikeBean.setCyclingId(json.isNull("id") ? bikeBean.getCyclingId() : json.getString("id"));
            bikeBean.setCyclingId(json.isNull("cyclingDataId") ? bikeBean.getCyclingId() : json.getString("cyclingDataId"));
            bikeBean.setCyclingStartDate(json.isNull("cyclingStartDate") ? bikeBean.getCyclingStartDate() : json.getLong("cyclingStartDate"));
            bikeBean.setCyclingStartDate(json.isNull("createDate") ? bikeBean.getCyclingStartDate() : json.getLong("createDate"));
            bikeBean.setCyclingCost(json.isNull("cyclingCost") ? bikeBean.getCyclingCost() : json.getInt("cyclingCost"));
            bikeBean.setStatus(json.isNull("status") ? bikeBean.getStatus() : json.getInt("status"));
            bikeBean.setDateTime(json.isNull("dateTime") ? bikeBean.getDateTime() : json.getLong("dateTime"));
            bikeBean.setLockStatus(json.isNull("lockStatus") ? bikeBean.getLockStatus() : json.getInt("lockStatus"));
            bikeBean.setBiketype(json.isNull("biketype") ? bikeBean.getBiketype() : json.getInt("biketype"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return bikeBean;
    }
}
