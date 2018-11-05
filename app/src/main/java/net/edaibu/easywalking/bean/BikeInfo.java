package net.edaibu.easywalking.bean;

import java.io.Serializable;
import java.util.List;

public class BikeInfo extends BaseBean {

    private List<BikeInfoList> data;

    public List<BikeInfoList> getData() {
        return data;
    }

    public void setData(List<BikeInfoList> data) {
        this.data = data;
    }

    public static class BikeInfoList implements Serializable{
        //维度
        private double latitude;
        //经度
        private double longitude;
        //加密的车辆编码
        private String bikenumber;
        //七位车辆编码
        private String bikecode;
        //imei号，用于蓝牙
        private String imei;
        private String image;

        private int biketype;

        private String citycode;

        //车辆状态1：可租用；2：故障中；3：使用中；4：检测中；5：预定；10：预占；8：低电量；9：工厂检测
        private int status;

        private int islock;

        private String companyno;

        //0不是红包车，1是红包车
        private int redBike;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getBikenumber() {
            return bikenumber;
        }

        public void setBikenumber(String bikenumber) {
            this.bikenumber = bikenumber;
        }

        public String getBikecode() {
            return bikecode;
        }

        public void setBikecode(String bikecode) {
            this.bikecode = bikecode;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getBiketype() {
            return biketype;
        }

        public void setBiketype(int biketype) {
            this.biketype = biketype;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIslock() {
            return islock;
        }

        public void setIslock(int islock) {
            this.islock = islock;
        }

        public String getCompanyno() {
            return companyno;
        }

        public void setCompanyno(String companyno) {
            this.companyno = companyno;
        }

        public int getRedBike() {
            return redBike;
        }

        public void setRedBike(int redBike) {
            this.redBike = redBike;
        }
    }
}
