package net.edaibu.easywalking.http.base;


import android.text.TextUtils;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.UserBean;
import net.edaibu.easywalking.http.HttpApi;
import net.edaibu.easywalking.utils.LogUtils;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.parameter.ParameterUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lyn on 2017/4/13.
 */

public class LogInterceptor implements Interceptor {

    private final int ACCESS_TOKEN_TIME_OUT_CODE = 401;
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        LogUtils.e(String.format("request %s on %s%n%s", request.url(), request.method(), request.headers()));
        if (request.method().equals("POST")) {
            request = addParameter(request,1);
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String body = response.body().string();
        //如果ACCESS_TOKEN失效，自动重新获取一次
        final int code = getCode(body);
        if (code == ACCESS_TOKEN_TIME_OUT_CODE) {
            UserBean userBean = getAccessToken();
            if (userBean != null && userBean.isSussess()) {
                MyApplication.spUtil.addString(SPUtil.ACCESS_TOKEN, userBean.getData().getAccess_token());
                MyApplication.spUtil.addString(SPUtil.AUTH_TOKEN, userBean.getData().getAuth_token());
                request = addParameter(request,1);
                response = chain.proceed(request);
                body = response.body().string();
            }
        }
        LogUtils.e(String.format("response %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, body));
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), body)).build();
    }

    /**
     * 获取AccessToken
     */
    public UserBean getAccessToken() throws IOException {
        UserBean userBean = null;
        String auth_token = MyApplication.spUtil.getString(SPUtil.AUTH_TOKEN);
        if (!TextUtils.isEmpty(auth_token)) {
            Map<String, String> map = new HashMap<>();
            map.put("auth_token", auth_token);
            map = ParameterUtils.getParameter(map,2);
            userBean = Http.getRetrofitNoInterceptor().create(HttpApi.class).getAccessToken(map).execute().body();
        }
        return userBean;
    }

    /***
     * 添加公共参数
     */
    public Request addParameter(Request request,int type) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        FormBody formBody;
        Map<String, String> requstMap = new HashMap<>();
        if (request.body().contentLength() > 0 && request.body() instanceof FormBody) {
            formBody = (FormBody) request.body();
            //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
            for (int i = 0; i < formBody.size(); i++) {
                requstMap.put(formBody.name(i), formBody.value(i).replace("+","").replace(" ",""));
                LogUtils.e(request.url() + "参数:" + formBody.name(i) + "=" + formBody.value(i));
            }
        }
        requstMap = ParameterUtils.getParameter(requstMap,type);
        //添加公共参数
        for (String key : requstMap.keySet()) {
            bodyBuilder.addEncoded(key, requstMap.get(key));
        }
        formBody = bodyBuilder.build();
        request = request.newBuilder().post(formBody).build();
        return request;
    }

    public int getCode(String json) {
        int code = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("code")) {
                code = jsonObject.getInt("code");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

}
