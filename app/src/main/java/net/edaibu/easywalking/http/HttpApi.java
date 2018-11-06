package net.edaibu.easywalking.http;

import net.edaibu.easywalking.bean.BaseBean;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.bean.BikeList;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.bean.UserInfo;
import net.edaibu.easywalking.bean.Version;

import java.util.Map;

import okhttp3.ResponseBody;
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


    /**
     * 获取短信验证码
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.GET_CODE)
    Call<BaseBean> getSmsCode(@FieldMap Map<String, String> map);


    /**
     * 登陆
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.LOGIN)
    Call<ResponseBody> login(@FieldMap Map<String, String> map);


    /**
     * 查询电子围栏，禁停区，禁行区
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.FIND_FENCING)
    Call<Fanceing> findFencing(@FieldMap Map<String, String> map);



    /**
     * 查询附近500米内车辆
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.FIND_CARLIST)
    Call<BikeList> getLocationBike(@FieldMap Map<String, String> map);


    /**
     * 查询最新版本
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.FIND_VERSION)
    Call<Version> getNewVersion(@FieldMap Map<String, String> map);


    /**
     * 根据编码查询车辆详情
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.GETCAR_BYCODE)
    Call<BikeBean> getBikeByCode(@FieldMap Map<String, String> map);
}
