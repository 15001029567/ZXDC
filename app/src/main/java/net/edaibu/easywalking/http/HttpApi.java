package net.edaibu.easywalking.http;

import net.edaibu.easywalking.bean.BaseBean;
import net.edaibu.easywalking.bean.BikeList;
import net.edaibu.easywalking.bean.CancleNum;
import net.edaibu.easywalking.bean.DiyBean;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.bean.UserBean;
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
    Call<UserBean> getAccessToken(@FieldMap Map<String, String> map);


    /**
     * 查询用户详情
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.GET_USERINFO)
    Call<UserBean> getUserInfo(@FieldMap Map<String, String> map);


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
    Call<ResponseBody> getBikeByCode(@FieldMap Map<String, String> map);


    /**
     * 扫码开锁后生成骑行单接口
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.SCAN_OPLOCK)
    Call<ResponseBody> getOrderByScan(@FieldMap Map<String, String> map);


    /**
     * 查询DIY数据
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.FIND_DIY)
    Call<DiyBean> getDiyList(@FieldMap Map<String, String> map);


    /**
     * 设置DIY旋轮
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.SET_DAZZLE)
    Call<BaseBean> setDiy(@FieldMap Map<String, String> map);


    /**
     * 查询订单信息
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.GET_ORDER_INFO)
    Call<ResponseBody> getOrderInfo(@FieldMap Map<String, String> map);


    /**
     * 随机预约时，先查询车辆信息
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.RANDOM_BESPOKE)
    Call<ResponseBody> getBikeByRandom(@FieldMap Map<String, String> map);


    /**
     * 确定预约车辆
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.CONFIRM_BESPOKE)
    Call<ResponseBody> confirmBespoke(@FieldMap Map<String, String> map);


    /**
     * 获取免费预约还剩多少次
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.GET_BESPOKE_CANCLE_NUM)
    Call<CancleNum> getCancleNum(@FieldMap Map<String, String> map);


    /**
     * 取消预约
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST(HttpConstant.CANCLE_BESPEAK)
    Call<BaseBean> cancleBespoke(@FieldMap Map<String, String> map);
}
