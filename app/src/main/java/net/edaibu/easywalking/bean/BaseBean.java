package net.edaibu.easywalking.bean;

import java.io.Serializable;

/**
 * 网络请求状态实体类
 */

public class BaseBean implements Serializable{
    private int code;
    private String msg;

    public BaseBean(){}

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

    public boolean isSussess() {
        if (code == 200) {
            return true;
        }
        return false;
    }

}
