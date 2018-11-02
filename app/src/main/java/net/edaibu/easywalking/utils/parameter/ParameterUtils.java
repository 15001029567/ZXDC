package net.edaibu.easywalking.utils.parameter;

import android.content.Context;
import android.text.TextUtils;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.utils.NetUtils;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.utils.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共参数
 * Created by Administrator on 2017/2/4 0004.
 */

public class ParameterUtils {

    public static Map<String, String> getParameter(Map<String, String> map,int type) {

        Context mContext = MyApplication.application;
        Map<String, String> maps = new HashMap<>();
        //手机型号
        String device_type = android.os.Build.MODEL.replace("+","").replace(" ","").replace("'","");
        //操作系统版本
        String os_version = android.os.Build.VERSION.RELEASE.replace("+","").replace(" ","");
        //手机品牌
        String device_name = android.os.Build.BRAND.replace("+","").replace(" ","");
        //app版本号
        int app_version = Util.getVersionCode(mContext);
        //网络类型
        String network_type = NetUtils.getNetworkState(mContext);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put("device_id", Util.getDeviceId(mContext));
        map.put("device_type", device_type);
        map.put("os_name", "Android");
        map.put("os_version", os_version);
        map.put("device_name", device_name);
        map.put("app_version", String.valueOf(app_version));
        map.put("device_mac", "no");
        map.put("network_type", network_type);
        map.put("device_width", String.valueOf(Util.getDeviceWH(mContext,1)));
        map.put("device_height", String.valueOf(Util.getDeviceWH(mContext,2)));

        if (TextUtils.isEmpty(map.get("latitude")) && TextUtils.isEmpty(map.get("longitude"))) {
            map.put("latitude", MyApplication.spUtil.getString(SPUtil.LOCATION_LAT));
            map.put("longitude", MyApplication.spUtil.getString(SPUtil.LOCATION_LONG));
        }
        map.put("timestamp", (System.currentTimeMillis()/1000)+"");
        map.put("channel", Util.getChannel(mContext));
        map.put("access_token", MyApplication.spUtil.getString(SPUtil.ACCESS_TOKEN));
        map.remove("signature");

        //排序
        final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            public int compare(final Map.Entry<String, String> o1, final Map.Entry<String, String> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });

        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> item : infos) {
            if (!TextUtils.isEmpty(item.getKey())) {
                if (TextUtils.isEmpty(item.getValue())) {
                    item.setValue("");
                }
                maps.put(item.getKey(), item.getValue());
                buf.append(item.getKey() + "=" + item.getValue());
                buf.append("&");

            }
        }
        String data = buf.substring(0, buf.toString().length() - 1);
        final String skey = "9K%^JNCQtb*eKh#b";
        //加密
        try {
            byte[] encryptResultStr = BackAES.encrypt(data, skey, 1);
            if(type==1){
                maps.put("signature", new String(encryptResultStr).replace("+","%2B"));
            }else{
                maps.put("signature", new String(encryptResultStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

}
