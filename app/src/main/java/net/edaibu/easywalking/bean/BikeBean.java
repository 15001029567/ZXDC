package net.edaibu.easywalking.bean;

import java.io.Serializable;

public class BikeBean extends BaseBean {

    //预约订单id
    private String resserveId;
    //车牌号
    private String bikeCode;
    //车辆编码
    private String bikeNumber;
    //imei号
    private String imei;
    //预约订单创立时间
    private Long reserveDate=(long) 0;
    //预约赠送时间;单位:毫秒
    private int freeTime=0;
    //预约阶段的花费(单位:分)
    private double reserveCost=0;
    //车辆经纬度
    private Double latitude=0.0;
    private Double longitude=0.0;
    //骑行单ID
    private String cyclingId;
    //如果存在骑单Id,截止至当前时间的骑行花费
    private int cyclingCost=0;
    //骑行开始时间
    private Long cyclingStartDate=(long) 0;
    //车辆状态  1：可租用；2：故障中；3：使用中；4：检测中；5：预定；10：预占；8：低电量；9：工厂检测
    private int status;
    //服务器当前时间戳
    private long dateTime;
    //车锁状态(0关锁,1开锁,2未知)
    private int lockStatus;
    //车辆类型1:三代锁,2:马蹄锁
    private int biketype=1;
    //马蹄锁密钥
    private String btKey;
    //马蹄锁mac地址
    private String btMac;
    //挪车费
    private String punishAmount;
    //true：表示是景区车
    private boolean scenic;
    //景区车的收费标准
    private String priceAndUnit;
    //0不是红包车，1是红包车,2非运营区红包车
    private int redBike;
    // 运营区外红包车提示
    private String areaMsg;
    //运营区外违规费用
    private String rideFenceAmount;
    private int bikeversion;
    private int lockversion;

    public String getResserveId() {
        return resserveId;
    }

    public void setResserveId(String resserveId) {
        this.resserveId = resserveId;
    }

    public String getBikeCode() {
        return bikeCode;
    }

    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }

    public String getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Long getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Long reserveDate) {
        this.reserveDate = reserveDate;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public double getReserveCost() {
        return reserveCost;
    }

    public void setReserveCost(double reserveCost) {
        this.reserveCost = reserveCost;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCyclingId() {
        return cyclingId;
    }

    public void setCyclingId(String cyclingId) {
        this.cyclingId = cyclingId;
    }

    public int getCyclingCost() {
        return cyclingCost;
    }

    public void setCyclingCost(int cyclingCost) {
        this.cyclingCost = cyclingCost;
    }

    public Long getCyclingStartDate() {
        return cyclingStartDate;
    }

    public void setCyclingStartDate(Long cyclingStartDate) {
        this.cyclingStartDate = cyclingStartDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getBiketype() {
        return biketype;
    }

    public void setBiketype(int biketype) {
        this.biketype = biketype;
    }

    public String getBtKey() {
        return btKey;
    }

    public void setBtKey(String btKey) {
        this.btKey = btKey;
    }

    public String getBtMac() {
        return btMac;
    }

    public void setBtMac(String btMac) {
        this.btMac = btMac;
    }

    public String getPunishAmount() {
        return punishAmount;
    }

    public void setPunishAmount(String punishAmount) {
        this.punishAmount = punishAmount;
    }

    public boolean isScenic() {
        return scenic;
    }

    public void setScenic(boolean scenic) {
        this.scenic = scenic;
    }

    public String getPriceAndUnit() {
        return priceAndUnit;
    }

    public void setPriceAndUnit(String priceAndUnit) {
        this.priceAndUnit = priceAndUnit;
    }

    public int getRedBike() {
        return redBike;
    }

    public void setRedBike(int redBike) {
        this.redBike = redBike;
    }

    public String getAreaMsg() {
        return areaMsg;
    }

    public void setAreaMsg(String areaMsg) {
        this.areaMsg = areaMsg;
    }

    public String getRideFenceAmount() {
        return rideFenceAmount;
    }

    public void setRideFenceAmount(String rideFenceAmount) {
        this.rideFenceAmount = rideFenceAmount;
    }

    public int getBikeversion() {
        return bikeversion;
    }

    public void setBikeversion(int bikeversion) {
        this.bikeversion = bikeversion;
    }

    public int getLockversion() {
        return lockversion;
    }

    public void setLockversion(int lockversion) {
        this.lockversion = lockversion;
    }
}
