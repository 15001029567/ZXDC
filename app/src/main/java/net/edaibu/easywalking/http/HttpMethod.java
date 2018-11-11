package net.edaibu.easywalking.http;

import android.os.Handler;

import net.edaibu.easywalking.bean.BaseBean;
import net.edaibu.easywalking.bean.BikeList;
import net.edaibu.easywalking.bean.CancleNum;
import net.edaibu.easywalking.bean.DiyBean;
import net.edaibu.easywalking.bean.DownLoad;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.bean.UserBean;
import net.edaibu.easywalking.bean.Version;
import net.edaibu.easywalking.http.base.BaseRequst;
import net.edaibu.easywalking.http.base.Http;
import net.edaibu.easywalking.utils.LogUtils;

import java.io.IOException;
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
        Http.getRetrofit().create(HttpApi.class).getAccessToken(map).enqueue(new Callback<UserBean>() {
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_ACCESS_TOKEN_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<UserBean> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 查询用户详情
     * @param auth_token
     * @param handler
     */
    public static void getUserInfo(String auth_token, final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("auth_token", auth_token);
        Http.getRetrofit().create(HttpApi.class).getUserInfo(map).enqueue(new Callback<UserBean>() {
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_USER_INFO_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<UserBean> call, Throwable t) {
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



    /**
     * 查询周围500米的车辆 使用百度坐标
     * @param lat
     * @param lon
     * @param handler
     */
    public static void getLocationBike(String lat,String lon,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("lat",lat);
        map.put("lon",lon);
        Http.getRetrofit().create(HttpApi.class).getLocationBike(map).enqueue(new Callback<BikeList>() {
            public void onResponse(Call<BikeList> call, Response<BikeList> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_LOCATION_BIKE_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }

            public void onFailure(Call<BikeList> call, Throwable t) {
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }



    /**
     * 查询最新版本
     * @param handler
     */
    public static void getNewVersion(final Handler handler) {
        Map<String, String> map = new HashMap<>();
        Http.getRetrofit().create(HttpApi.class).getNewVersion(map).enqueue(new Callback<Version>() {
            public void onResponse(Call<Version> call, Response<Version> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_VERSION_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<Version> call, Throwable t) {
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 文件下载
     * @param handler
     */
    public static void download(final DownLoad downLoad, final Handler handler) {
        Http.dowload(downLoad.getDownPath(), downLoad.getSavePath(),handler, new okhttp3.Callback() {
            public void onFailure(okhttp3.Call call, IOException e) {
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    sendMessage(handler, HandlerConstant.DOWNLOAD_SUCCESS, downLoad);
                }
            }
        });
    }


    /**
     * 根据编码查询车辆详情
     * @param bikeCode
     * @param handler
     */
    public static void getBikeByCode(String bikeCode,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("bikeCode",bikeCode);
        Http.getRetrofit().create(HttpApi.class).getBikeByCode(map).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_BIKE_BYCODE_SUCCESS, response.body().string());
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
     * 扫码开锁后生成骑行单接口
     * @param bikeCode
     * @param handler
     */
    public static void getOrderByScan(String bikeCode,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("bikeCode",bikeCode);
        Http.getRetrofit().create(HttpApi.class).getOrderByScan(map).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_ORDER_BY_SCAN_SUCCESS, response.body().string());
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
     * 查询DIY数据
     * @param handler
     */
    public static void getDiyList(final Handler handler) {
        Map<String, String> map = new HashMap<>();
        Http.getRetrofit().create(HttpApi.class).getDiyList(map).enqueue(new Callback<DiyBean>() {
            public void onResponse(Call<DiyBean> call, Response<DiyBean> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_DIY_LIST_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<DiyBean> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 设置DIY旋轮
     * @param userTemplateId
     * @param templateId
     * @param bikeWheelNum
     * @param handler
     */
    public static void setDiy(String userTemplateId,String templateId,int bikeWheelNum,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("userTemplateId",userTemplateId);
        map.put("templateId",templateId);
        map.put("bikeWheelNum",bikeWheelNum+"");
        Http.getRetrofit().create(HttpApi.class).setDiy(map).enqueue(new Callback<BaseBean>() {
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                try {
                    sendMessage(handler, HandlerConstant.SET_DIY_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
                }
            }

            public void onFailure(Call<BaseBean> call, Throwable t) {
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 查询订单信息
     * @param handler
     */
    public static void getOrderInfo(final Handler handler) {
        Map<String, String> map = new HashMap<>();
        Http.getRetrofit().create(HttpApi.class).getOrderInfo(map).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_ORDER_INFO_SUCCESS, response.body().string());
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
     * 随机预约时，先查询车辆信息
     * @param handler
     */
    public static void getBikeByRandom(final Handler handler) {
        Map<String, String> map = new HashMap<>();
        Http.getRetrofit().create(HttpApi.class).getBikeByRandom(map).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    sendMessage(handler, HandlerConstant.RANDOM_BESPOKE_SUCCESS, response.body().string());
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
     * 确定预约车辆
     * @param bikeCode
     * @param handler
     */
    public static void confirmBespoke(String bikeCode,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("bikeCode",bikeCode);
        Http.getRetrofit().create(HttpApi.class).confirmBespoke(map).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    sendMessage(handler, HandlerConstant.CONFIRM_BESPOKE_SUCCESS, response.body().string());
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
     * 获取免费预约还剩多少次
     * @param handler
     */
    public static void getCancleNum(final Handler handler) {
        Map<String, String> map = new HashMap<>();
        Http.getRetrofit().create(HttpApi.class).getCancleNum(map).enqueue(new Callback<CancleNum>() {
            public void onResponse(Call<CancleNum> call, Response<CancleNum> response) {
                try {
                    sendMessage(handler, HandlerConstant.GET_CANCLE_NUM_SUCCESS, response.body());
                }catch (Exception e){
                    e.printStackTrace();
                    sendMessage(handler, HandlerConstant.GET_DATA_ERROR, null);
                }
            }
            public void onFailure(Call<CancleNum> call, Throwable t) {
                LogUtils.e("查询数据报错："+t.getMessage());
                sendMessage(handler, HandlerConstant.REQUST_ERROR, null);
            }
        });
    }


    /**
     * 取消预约
     * @param reservationId
     * @param handler
     */
    public static void cancleBespoke(String reservationId,final Handler handler) {
        Map<String, String> map = new HashMap<>();
        map.put("reservationId",reservationId);
        Http.getRetrofit().create(HttpApi.class).cancleBespoke(map).enqueue(new Callback<BaseBean>() {
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                try {
                    sendMessage(handler, HandlerConstant.CANCLE_BESPOKE_SUCCESS, response.body());
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

}
