package net.edaibu.easywalking.http;

import android.os.Handler;

import net.edaibu.easywalking.bean.UserInfo;
import net.edaibu.easywalking.http.base.BaseRequst;
import net.edaibu.easywalking.http.base.Http;
import net.edaibu.easywalking.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

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
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_ACCESS_TOKEN_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }
}
