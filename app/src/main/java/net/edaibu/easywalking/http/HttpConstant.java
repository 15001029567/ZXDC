package net.edaibu.easywalking.http;

public class HttpConstant {

    // 正式IP
//    public static final String IP = "http://ui.zxbike.net/";

    //测试IP
     public static final String IP = "http://ui.zxbike.top/";

    //查询最新的access_token
    public static final String GET_ACCESS_TOKEN = "auth/login";

    //获取验证码
    public static final String GET_CODE = "sms/send";

    //登陆
    public static final String LOGIN = "sms/login";

    //查询电子围栏
    public static final String FIND_FENCING = "forbid/fence/query/gcj";

    //查询附近500米内车辆
    public static final String FIND_CARLIST = "bike/location/list/gcj";

    //查询最新版本
    public static final String FIND_VERSION = "version/update";
}
