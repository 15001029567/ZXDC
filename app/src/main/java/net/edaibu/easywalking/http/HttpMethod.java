package net.edaibu.easywalking.http;

import android.os.Handler;

import net.edaibu.easywalking.bean.BaseBean;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.bean.UserInfo;
import net.edaibu.easywalking.http.base.BaseRequst;
import net.edaibu.easywalking.http.base.Http;
import net.edaibu.easywalking.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpMethod extends BaseRequst {

    /**
     * 获取最新的accessToken
     */
    public static void getAccessToken(String auth_token, final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", auth_token);
        Http.getRetrofit().create(HttpApi.class).getAccessToken(map).enqueue(new Callback<UserInfo>() {
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_ACCESS_TOKEN_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<UserInfo> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 获取短信验证码
     * @param mobile
     * @param code_type
     * @param handler
     */
    public static void getSmsCode(String mobile, String code_type, final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code_type",code_type);
        Http.getRetrofit().create(HttpApi.class).getSmsCode(map).enqueue(new Callback<BaseBean>() {
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_SMS_CODE_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<BaseBean> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 登陆
     * @param mobile
     * @param smscode
     * @param handler
     */
    public static void login(String mobile, String smscode, final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("smscode", smscode);
        Http.getRetrofit().create(HttpApi.class).login(map).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    sendMessage(handler, HandlerConstant.LOGIN_SUCCESS, response.body().string());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 查询电子围栏
     * @param bikeCode
     * @param handler
     */
    public static void findFencing(String bikeCode,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("bikeCode",bikeCode);
        Http.getRetrofit().create(HttpApi.class).findFencing(map).enqueue(new Callback<Fanceing>() {
            public void onResponse(Call<Fanceing> call, Response<Fanceing> response) {
                try {
                    sendMessage(handler, HandlerConstant.FIND_FENCING_SUCCESS, response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }

            public void onFailure(Call<Fanceing> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }
}
