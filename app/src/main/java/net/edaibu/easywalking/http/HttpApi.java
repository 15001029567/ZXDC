package net.edaibu.easywalking.http;

import net.edaibu.easywalking.bean.UserInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HttpApi {

    /**
     * 获取最新的accessToken
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.GET_ACCESS_TOKEN)
    Call<UserInfo> getAccessToken(@FieldMap Map<String, String> map);
}
