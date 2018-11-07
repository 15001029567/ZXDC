package net.edaibu.easywalking.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DIY
 * Created by Administrator on 2017/11/15 0015.
 */

public class DiyBean extends BaseBean {

    private DazzleBean data;

    public DazzleBean getData() {
        return data;
    }

    public void setData(DazzleBean data) {
        this.data = data;
    }

    public static class DazzleBean implements Serializable{
        private List<templateListBean> templateList=new ArrayList<>();
        private userTemplateBean userTemplate;
        private List<templateDeatilBean> templateDeatil=new ArrayList<>();

        public List<templateListBean> getTemplateList() {
            return templateList;
        }

        public void setTemplateList(List<templateListBean> templateList) {
            this.templateList = templateList;
        }

        public userTemplateBean getUserTemplate() {
            return userTemplate;
        }

        public void setUserTemplate(userTemplateBean userTemplate) {
            this.userTemplate = userTemplate;
        }

        public List<templateDeatilBean> getTemplateDeatil() {
            return templateDeatil;
        }

        public void setTemplateDeatil(List<templateDeatilBean> templateDeatil) {
            this.templateDeatil = templateDeatil;
        }
    }


    public static class templateListBean implements Serializable{
        private String id;
        private String number;
        private String imgUrl;
        private String duration;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }


    public static class userTemplateBean implements Serializable{
        private String id;
        private String templateNum;
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTemplateNum() {
            return templateNum;
        }

        public void setTemplateNum(String templateNum) {
            this.templateNum = templateNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


    public static class templateDeatilBean implements Serializable{
        private String id;
        private String userId;
        private String userTemplateId;
        private String wheelNumber;
        private String wheelId;
        private int bikeWheelNum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserTemplateId() {
            return userTemplateId;
        }

        public void setUserTemplateId(String userTemplateId) {
            this.userTemplateId = userTemplateId;
        }

        public String getWheelId() {
            return wheelId;
        }

        public void setWheelId(String wheelId) {
            this.wheelId = wheelId;
        }

        public int getBikeWheelNum() {
            return bikeWheelNum;
        }

        public void setBikeWheelNum(int bikeWheelNum) {
            this.bikeWheelNum = bikeWheelNum;
        }

        public String getWheelNumber() {
            return wheelNumber;
        }

        public void setWheelNumber(String wheelNumber) {
            this.wheelNumber = wheelNumber;
        }
    }
}
