package net.edaibu.easywalking.activity.start;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.BaseActivity;
import net.edaibu.easywalking.activity.MainActivity;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.UserBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.SPUtil;

/**
 * 启动欢迎页面
 */
public class WellcomeActivity extends BaseActivity {
    private LinearLayout linearLayout;
    private Animation myAnimation_Alpha;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_wellcome);
        //查询最新的access_token
        getAccess_token();
        initView();
        initAnim();
    }

    private void initView() {
        linearLayout=(LinearLayout)findViewById(R.id.lin_wellcome);
    }

    private void initAnim() {
        myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
        myAnimation_Alpha.setDuration(2500);
        myAnimation_Alpha.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setClass(MainActivity.class);
                finish();
            }
        });
        linearLayout.setAnimation(myAnimation_Alpha);
        myAnimation_Alpha.start();
    }


    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                //最新的token回执
                case HandlerConstant.GET_ACCESS_TOKEN_SUCCESS:
                      UserBean userBean = (UserBean) msg.obj;
                      if (null == userBean) {
                          break;
                      }
                      if (userBean.isSussess()) {
                         MyApplication.spUtil.addString(SPUtil.ACCESS_TOKEN, userBean.getData().getAccess_token());
                         MyApplication.spUtil.addString(SPUtil.AUTH_TOKEN, userBean.getData().getAuth_token());
                      }
                      break;
                case HandlerConstant.REQUST_ERROR:
                      showMsg(getString(R.string.http_error));
                      break;
                 default:
                     break;
            }
            return false;
        }
    });


    /**
     * 查询最新的access_token
     */
    private void getAccess_token() {
        final String auth_token = MyApplication.spUtil.getString(SPUtil.AUTH_TOKEN);
        if (!TextUtils.isEmpty(auth_token)) {
            HttpMethod.getAccessToken(auth_token,mHandler);
        }
    }

}
