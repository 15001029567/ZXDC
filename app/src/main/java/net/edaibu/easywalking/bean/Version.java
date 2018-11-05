package net.edaibu.easywalking.bean;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class Version extends BaseBean {

    private VersionBean data;

    public VersionBean getData() {
        return data;
    }

    public void setData(VersionBean data) {
        this.data = data;
    }

    public static class VersionBean{
        //true 强制更新 false 非强制更新
        private boolean enforce;
        //下载地址
        private String download_url;
        //更新的变更日志，简易的HTML代码
        private String change_log;

        public boolean isEnforce() {
            return enforce;
        }

        public void setEnforce(boolean enforce) {
            this.enforce = enforce;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getChange_log() {
            return change_log;
        }

        public void setChange_log(String change_log) {
            this.change_log = change_log;
        }
    }
}
