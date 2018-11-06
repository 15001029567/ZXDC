package net.edaibu.easywalking.bean;

import java.io.Serializable;

public class BikeBean extends BaseBean{

    private BikeData data;

    public BikeData getData() {
        return data;
    }

    public void setData(BikeData data) {
        this.data = data;
    }

    public static class BikeData implements Serializable{
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
    }
}
