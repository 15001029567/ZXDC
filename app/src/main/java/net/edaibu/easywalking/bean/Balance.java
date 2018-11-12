package net.edaibu.easywalking.bean;

import java.io.Serializable;

/**
 * 结算还车
 * Created by Administrator on 2018/11/12.
 */

public class Balance extends BaseBean{

    private balanceData data;

    public balanceData getData() {
        return data;
    }

    public void setData(balanceData data) {
        this.data = data;
    }

    public static class balanceData implements Serializable {
        //车牌号
        private String bikeCode;
        //车编号
        private String bikeNumber;
        //骑行花费(单位：分)
        private double cyclingCost;
        //骑行单Id
        private String cyclingDataId;
        //骑行结束日期
        private long cyclingEndDate=0;
        //骑行结束纬度
        private double cyclingEndLatitude;
        //骑行结束经度
        private double cyclingEndLongitude;
        //骑行开始时间
        private long cyclingStartDate=0;
        //骑行开始纬度
        private double cyclingStartLatitude;
        //骑行开始经度
        private double cyclingStartLongitude;
        //预约和骑行花费明细单编号
        private String detailId;
        //IMEI号
        private String imei;
        //是否即时支付成功(意义不大);0:默认(即时支付成功);1:未即时支付成功;
        private int payState;
        //预约花费
        private double reserveCost;
        //预约单Id
        private String reserveDataId;
        //预约(开始)时间
        private String reserveDate;
        //预约结束时间
        private String reserveEndDate;
        //预约赠送时长(单位：毫秒)
        private long reserveFreeTime=0;
        //预约时所在纬度
        private double reserveLatitude;
        //预约时所在经度
        private double reserveLongitude;
        //预约骑行花费总额
        private double totalCost;
        //骑行距离
        private int totalkm;
        //支付单号;如果非空，表明需存在未支付的情形;
        private String accountOrderNo;
        //用户余额
        private double balance;
        //支付方式
        private String payTypeStr;
        //优惠券扣减金额(单位：分)
        private int couponAmount;
        //还需多少费用
        private int debt;
        //是不是景区车(是true,不是false)
        private boolean scenicBick;
        //收费标准
        private String chargeItem;
        //挪车费
        private String punishCost;
        //红包车奖励金额为零时不显示红包(单位:分)
        private double redcarAmount;
        //是否超出骑行围栏收费20元 , 1:已收取20元费用  ,0:未收取20元收费
        private int punishType;

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

        public double getCyclingCost() {
            return cyclingCost;
        }

        public void setCyclingCost(double cyclingCost) {
            this.cyclingCost = cyclingCost;
        }

        public String getCyclingDataId() {
            return cyclingDataId;
        }

        public void setCyclingDataId(String cyclingDataId) {
            this.cyclingDataId = cyclingDataId;
        }

        public long getCyclingEndDate() {
            return cyclingEndDate;
        }

        public void setCyclingEndDate(long cyclingEndDate) {
            this.cyclingEndDate = cyclingEndDate;
        }

        public double getCyclingEndLatitude() {
            return cyclingEndLatitude;
        }

        public void setCyclingEndLatitude(double cyclingEndLatitude) {
            this.cyclingEndLatitude = cyclingEndLatitude;
        }

        public double getCyclingEndLongitude() {
            return cyclingEndLongitude;
        }

        public void setCyclingEndLongitude(double cyclingEndLongitude) {
            this.cyclingEndLongitude = cyclingEndLongitude;
        }

        public long getCyclingStartDate() {
            return cyclingStartDate;
        }

        public void setCyclingStartDate(long cyclingStartDate) {
            this.cyclingStartDate = cyclingStartDate;
        }

        public double getCyclingStartLatitude() {
            return cyclingStartLatitude;
        }

        public void setCyclingStartLatitude(double cyclingStartLatitude) {
            this.cyclingStartLatitude = cyclingStartLatitude;
        }

        public double getCyclingStartLongitude() {
            return cyclingStartLongitude;
        }

        public void setCyclingStartLongitude(double cyclingStartLongitude) {
            this.cyclingStartLongitude = cyclingStartLongitude;
        }

        public String getDetailId() {
            return detailId;
        }

        public void setDetailId(String detailId) {
            this.detailId = detailId;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public int getPayState() {
            return payState;
        }

        public void setPayState(int payState) {
            this.payState = payState;
        }

        public double getReserveCost() {
            return reserveCost;
        }

        public void setReserveCost(double reserveCost) {
            this.reserveCost = reserveCost;
        }

        public String getReserveDataId() {
            return reserveDataId;
        }

        public void setReserveDataId(String reserveDataId) {
            this.reserveDataId = reserveDataId;
        }

        public String getReserveDate() {
            return reserveDate;
        }

        public void setReserveDate(String reserveDate) {
            this.reserveDate = reserveDate;
        }

        public String getReserveEndDate() {
            return reserveEndDate;
        }

        public void setReserveEndDate(String reserveEndDate) {
            this.reserveEndDate = reserveEndDate;
        }

        public long getReserveFreeTime() {
            return reserveFreeTime;
        }

        public void setReserveFreeTime(long reserveFreeTime) {
            this.reserveFreeTime = reserveFreeTime;
        }

        public double getReserveLatitude() {
            return reserveLatitude;
        }

        public void setReserveLatitude(double reserveLatitude) {
            this.reserveLatitude = reserveLatitude;
        }

        public double getReserveLongitude() {
            return reserveLongitude;
        }

        public void setReserveLongitude(double reserveLongitude) {
            this.reserveLongitude = reserveLongitude;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }

        public int getTotalkm() {
            return totalkm;
        }

        public void setTotalkm(int totalkm) {
            this.totalkm = totalkm;
        }

        public String getAccountOrderNo() {
            return accountOrderNo;
        }

        public void setAccountOrderNo(String accountOrderNo) {
            this.accountOrderNo = accountOrderNo;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getPayTypeStr() {
            return payTypeStr;
        }

        public void setPayTypeStr(String payTypeStr) {
            this.payTypeStr = payTypeStr;
        }

        public int getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(int couponAmount) {
            this.couponAmount = couponAmount;
        }

        public int getDebt() {
            return debt;
        }

        public void setDebt(int debt) {
            this.debt = debt;
        }

        public boolean isScenicBick() {
            return scenicBick;
        }

        public void setScenicBick(boolean scenicBick) {
            this.scenicBick = scenicBick;
        }

        public String getChargeItem() {
            return chargeItem;
        }

        public void setChargeItem(String chargeItem) {
            this.chargeItem = chargeItem;
        }

        public String getPunishCost() {
            return punishCost;
        }

        public void setPunishCost(String punishCost) {
            this.punishCost = punishCost;
        }

        public double getRedcarAmount() {
            return redcarAmount;
        }

        public void setRedcarAmount(double redcarAmount) {
            this.redcarAmount = redcarAmount;
        }

        public int getPunishType() {
            return punishType;
        }

        public void setPunishType(int punishType) {
            this.punishType = punishType;
        }
    }
}
