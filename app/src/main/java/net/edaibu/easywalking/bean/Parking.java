package net.edaibu.easywalking.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 附近停车点
 * Created by Administrator on 2017/7/5 0005.
 */

public class Parking extends  BaseBean{

    private List<ParkingBean> data;

    public List<ParkingBean> getData() {
        return data;
    }

    public void setData(List<ParkingBean> data) {
        this.data = data;
    }

    public static class ParkingBean implements Serializable{
        private String latitude;
        private String longitude;
        private String name;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
