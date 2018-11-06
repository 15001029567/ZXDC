package net.edaibu.easywalking.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.WinXinPay;

/**支付工具类
 * Created by Administrator on 2017/1/13 0013.
 */

public class PayUtils {
    private static PayUtils payUtils;
    private static Activity mActivity;
    private static IWXAPI api;

    public static PayUtils getInstance(Activity activity){
        if(payUtils==null){
            payUtils=new PayUtils();
        }
        mActivity=activity;
        return payUtils;
    }

    /**
     * 支付宝支付
     * @param handler
     */
    public  void alippay(final String paystr,final Handler handler){
        Runnable payRunnable = new Runnable() {
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(paystr, true);
                Message msg = new Message();
                msg.what = 0x001;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 微信支付
     * @param paystr
     * @param handler
     */
    public void weipay(final String paystr,final Handler handler){
        WinXinPay winXinPay = MyApplication.gson.fromJson(paystr, WinXinPay.class);
        wxpay(winXinPay);
    }

    //微信支付
    public static void wxpay(WinXinPay winXinPay){
        if(winXinPay==null){
            return;
        }
        api= WXAPIFactory.createWXAPI(mActivity, Constant.APP_ID);
        if( !api.isWXAppInstalled()){
            Toast.makeText(mActivity, R.string.please_install_the_WeChat_client_first,Toast.LENGTH_SHORT).show();
            return;
        }
        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);
        LogUtils.e(Constant.MCH_ID+"+++++");
        PayReq req = new PayReq();
        req.appId			= Constant.APP_ID;
        req.partnerId		= Constant.MCH_ID;
        req.prepayId		= winXinPay.getPrepayId();
        req.nonceStr		= winXinPay.getNonceStr();
        req.timeStamp		= winXinPay.getTimeStamp();
        req.packageValue	= "Sign=WXPay";
        req.sign			= winXinPay.getSign();
        api.sendReq(req);
    }
}
