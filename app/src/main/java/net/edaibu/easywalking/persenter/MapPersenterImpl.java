package net.edaibu.easywalking.persenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.bean.Fanceing;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.LogUtils;


/**
 * 地图MapFragment 的接口
 */
public class MapPersenterImpl {

    private Activity activity;

    private MapPersenter mapPersenter;

    public MapPersenterImpl(Activity activity,MapPersenter mapPersenter){
        this.activity=activity;
        this.mapPersenter=mapPersenter;
    }


    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //关闭进度条
            mapPersenter.closeLoding();
            switch (msg.what){
                //获取电子围栏等数据
                case HandlerConstant.FIND_FENCING_SUCCESS:
                      final Fanceing fanceing= (Fanceing) msg.obj;
                      if(null==fanceing){
                          break;
                      }
                      if(fanceing.isSussess()){
                          mapPersenter.showFencing(fanceing);
                      }else{
                          mapPersenter.showToast(fanceing.getMsg());
                      }
                      break;
                case HandlerConstant.REQUST_ERROR:
                     mapPersenter.showToast(activity.getString(R.string.http_error));
                     break;
                case HandlerConstant.GET_DATA_ERROR:
                     mapPersenter.showToast(activity.getString(R.string.Data_parsing_error_please_try_again_later));
                     break;
            }
            return false;
        }
    });


    /**
     * 查询电子围栏等
     * @param bikeCode
     */
    public void findFencing(String bikeCode){
        HttpMethod.findFencing(bikeCode,mHandler);
    }
}
