package net.edaibu.easywalking.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子围栏，禁停区等
 */
public class Fanceing extends BaseBean {

    private FanceingList data;

    public FanceingList getData() {
        return data;
    }

    public void setData(FanceingList data) {
        this.data = data;
    }

    public static class FanceingList implements Serializable{
        private List<List<List<Double>>> forbidArea=new ArrayList<>();
        private List<List<List<Double>>> superblock=new ArrayList<>();
        private List<List<List<Double>>> bigFence=new ArrayList<>();
        private List<List<List<Double>>> parkArea=new ArrayList<>();

        public List<List<List<Double>>> getForbidArea() {
            return forbidArea;
        }

        public void setForbidArea(List<List<List<Double>>> forbidArea) {
            this.forbidArea = forbidArea;
        }

        public List<List<List<Double>>> getSuperblock() {
            return superblock;
        }

        public void setSuperblock(List<List<List<Double>>> superblock) {
            this.superblock = superblock;
        }

        public List<List<List<Double>>> getBigFence() {
            return bigFence;
        }

        public void setBigFence(List<List<List<Double>>> bigFence) {
            this.bigFence = bigFence;
        }

        public List<List<List<Double>>> getParkArea() {
            return parkArea;
        }

        public void setParkArea(List<List<List<Double>>> parkArea) {
            this.parkArea = parkArea;
        }
    }
}
