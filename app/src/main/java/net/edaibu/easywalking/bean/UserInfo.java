package net.edaibu.easywalking.bean;

/**
 * Created by lyn on 2017/5/11.
 */

public class UserInfo extends BaseBean{
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private int type;
        private String channel;
        private String mobile;
        private String nickname;
        private double balance=0.0;
        private int prestige;
        private String head;
        private int status;
        private boolean bicycle=true;
        private String typeDesc;
        private String typeImage;
        private String bigImage;
        private String typeWeight;
        private String auth_token;
        private String access_token;
        private  int activityNum;
        private  double totalKm;
        private double normalBalance=0.0;
        private double saleBalance=0.0;
        private double redPacketBalance=0.0;
        private double redcarBalance=0.0;
        private double scenicBalance=0.0;
        private double cardBalance=0.0;
        private boolean exemptCreditFlag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getPrestige() {
            return prestige;
        }

        public void setPrestige(int prestige) {
            this.prestige = prestige;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isBicycle() {
            return bicycle;
        }

        public void setBicycle(boolean bicycle) {
            this.bicycle = bicycle;
        }

        public String getTypeDesc() {
            return typeDesc;
        }

        public void setTypeDesc(String typeDesc) {
            this.typeDesc = typeDesc;
        }

        public String getTypeImage() {
            return typeImage;
        }

        public void setTypeImage(String typeImage) {
            this.typeImage = typeImage;
        }

        public String getBigImage() {
            return bigImage;
        }

        public void setBigImage(String bigImage) {
            this.bigImage = bigImage;
        }

        public String getTypeWeight() {
            return typeWeight;
        }

        public void setTypeWeight(String typeWeight) {
            this.typeWeight = typeWeight;
        }

        public String getAuth_token() {
            return auth_token;
        }

        public void setAuth_token(String auth_token) {
            this.auth_token = auth_token;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public double getNormalBalance() {
            return normalBalance;
        }

        public void setNormalBalance(double normalBalance) {
            this.normalBalance = normalBalance;
        }

        public double getSaleBalance() {
            return saleBalance;
        }

        public void setSaleBalance(double saleBalance) {
            this.saleBalance = saleBalance;
        }

        public double getRedPacketBalance() {
            return redPacketBalance;
        }

        public void setRedPacketBalance(double redPacketBalance) {
            this.redPacketBalance = redPacketBalance;
        }

        public double getScenicBalance() {
            return scenicBalance;
        }

        public void setScenicBalance(double scenicBalance) {
            this.scenicBalance = scenicBalance;
        }

        public double getCardBalance() {
            return cardBalance;
        }

        public void setCardBalance(double cardBalance) {
            this.cardBalance = cardBalance;
        }

        public int getActivityNum() {
            return activityNum;
        }

        public void setActivityNum(int activityNum) {
            this.activityNum = activityNum;
        }

        public double getTotalKm() {
            return totalKm;
        }

        public void setTotalKm(double totalKm) {
            this.totalKm = totalKm;
        }

        public boolean isExemptCreditFlag() {
            return exemptCreditFlag;
        }

        public void setExemptCreditFlag(boolean exemptCreditFlag) {
            this.exemptCreditFlag = exemptCreditFlag;
        }

        public double getRedcarBalance() {
            return redcarBalance;
        }

        public void setRedcarBalance(double redcarBalance) {
            this.redcarBalance = redcarBalance;
        }
    }
}
