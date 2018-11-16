package net.edaibu.easywalking.persenter.scan;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.google.zxing.Result;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.BikeBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.JsonUtils;
import net.edaibu.easywalking.utils.scan.cameras.CameraManager;
import net.edaibu.easywalking.view.DialogView;
/**
 * 扫码的MVP接口类
 */
public class ScanPersenterImpl {

    private Activity activity;

    private ScanPersenter scanPersenter;
    // 开关光灯
    private boolean isTorchOn = true;
    public ScanPersenterImpl(Activity activity,ScanPersenter scanPersenter){
        this.activity=activity;
        this.scanPersenter=scanPersenter;
    }

    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            scanPersenter.closeLoding();
            switch (msg.what){
                //查询车辆信息回执
                case HandlerConstant.GET_BIKE_BYCODE_SUCCESS:
                      final BikeBean bikeBean=JsonUtils.getBikeBean(msg.obj.toString());
                      if(null==bikeBean){
                          break;
                      }
                      if(bikeBean.isSussess()){
                          scanPersenter.getBikeBean(bikeBean);
                      }else{
                          switch (bikeBean.getCode()){
                              //有未支付订单
                              case 435:
                                   break;
                              //车辆有问题，或者不能用
                              case 416:
                                   DialogView dialogView = new DialogView(activity,bikeBean.getMsg(), activity.getString(R.string.confirm), null, null, null);
                                   dialogView.show();
                                   break;
                               default:
                                   scanPersenter.showToast(bikeBean.getMsg());
                                   break;
                          }
                          scanPersenter.showToast(bikeBean.getMsg());
                      }
                      break;
                case HandlerConstant.REQUST_ERROR:
                     scanPersenter.showToast(activity.getString(R.string.http_error));
                     break;
                case HandlerConstant.GET_DATA_ERROR:
                     scanPersenter.showToast(activity.getString(R.string.Data_parsing_error_please_try_again_later));
                     break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 扫描二维码的结果
     * @param result
     */
    public void scanResult(Result result){
        if (null == result) {
            scanPersenter.showToast(activity.getString(R.string.scan_failed_try_again));
            return;
        }
        String resultString = result.getText();
        if (!TextUtils.isEmpty(resultString)) {
            resultString = resultString.replace(" ", "");
            if (resultString.indexOf("zxbike") != -1) {
                String bikeCode = resultString.substring(resultString.length() - 7, resultString.length());
                //根据车辆编码查询车辆信息
                getBikeByCode(bikeCode);
            } else {
                scanPersenter.showToast(activity.getString(R.string.please_scan_right_qr_code));
            }
        } else {
            scanPersenter.showToast(activity.getString(R.string.scan_failed_try_again));
        }
    }


    /**
     * 输入车辆编码后提交
     * @param arr
     */
    public void setBikeCode(char[] arr){
        if (null != arr) {
            if (arr.length < 7) {
                scanPersenter.showToast(activity.getString(R.string.please_enter_full_bike_number));
            } else {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < arr.length; i++) {
                    sb.append(arr[i]);
                }
                String bikeCode = sb.toString();
                //根据车辆编码查询车辆信息
                getBikeByCode(bikeCode);
            }
        } else {
            scanPersenter.showToast(activity.getString(R.string.please_enter_bike_bumber));
        }
    }


    /**
     * 根据车辆编码查询车辆信息
     */
    public void getBikeByCode(String bikeCode){
        scanPersenter.showLoding(activity.getString(R.string.loading));
        HttpMethod.getBikeByCode(bikeCode,mHandler);
    }

    /**
     * 开关灯设置
     *
     * @param b
     */
    public void openLight(boolean b) {
        if (isTorchOn) {//开灯
            isTorchOn = false;
            CameraManager.start();
        } else {//关灯
            isTorchOn = true;
            CameraManager.stop();
        }
    }

    public void onDestory(){
        mHandler.removeCallbacksAndMessages(null);
        scanPersenter=null;
    }
}
