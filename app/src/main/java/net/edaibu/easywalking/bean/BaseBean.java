package net.edaibu.easywalking.bean;

/**
 * 网络请求状态实体类
 */

public class BaseBean {
    private int code;
    private String msg;
    private String content;

    public BaseBean(){}

    public BaseBean(int code, String msg, String content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSussess() {
        if (code == 200) {
            return true;
        }
        return false;
    }

}
