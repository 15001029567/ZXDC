package net.edaibu.easywalking.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.BaseActivity;
import net.edaibu.easywalking.activity.MainActivity;
import net.edaibu.easywalking.application.MyApplication;
import net.edaibu.easywalking.bean.BaseBean;
import net.edaibu.easywalking.http.HandlerConstant;
import net.edaibu.easywalking.http.HttpMethod;
import net.edaibu.easywalking.utils.SPUtil;
import net.edaibu.easywalking.view.ClickTextView;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 登陆
 * Created by Administrator on 2017/1/14 0014.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etPhone, etCode;
    private ClickTextView tvSendCode;
    //计数器
    private Timer mTimer;
    private int time = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        //判断验证码秒数是否超过一分钟
        checkTime();
    }

    /**
     * 初始化
     */
    private void initView() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_login_cancel);
        etPhone = (EditText) findViewById(R.id.et_login_phone);
        etCode = (EditText) findViewById(R.id.et_login_code);
        tvSendCode = (ClickTextView) findViewById(R.id.tv_login_sendCode);
        ClickTextView tvLogin = (ClickTextView) findViewById(R.id.tv_login_submit);
        TextView tvService = (TextView) findViewById(R.id.tv_login_agreement);
        final TextView tvPhoneNumberLine= (TextView) findViewById(R.id.tv_phone_number_line);
        final TextView tvMsgCodeLine= (TextView) findViewById(R.id.tv_msg_code_line);
        tvCancel.setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvService.setOnClickListener(this);
        //判断是否有焦点
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                changeColorByFocus(hasFocus,tvPhoneNumberLine);
            }
        });
        //判断是否有焦点
        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                changeColorByFocus(hasFocus,tvMsgCodeLine);
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==11){
                    tvSendCode.setBackground(getResources().getDrawable(R.drawable.code_pressed));
                }else{
                    if(time==0){
                        tvSendCode.setBackground(getResources().getDrawable(R.drawable.code_normal));
                    }
                }
            }
        });

    }

    /**
     * 根据焦点改变背景颜色
     * @param hasFocus
     * @param tv
     */
    private void changeColorByFocus(boolean hasFocus, TextView tv) {
        if (hasFocus) {
            tv.setBackgroundColor(getResources().getColor(R.color.main_color));
        } else {
            tv.setBackgroundColor(getResources().getColor(R.color.color_dddddd));
        }
    }


    @Override
    public void onClick(View v) {
        final String mobile = etPhone.getText().toString().trim();
        final  String code = etCode.getText().toString().trim();
        switch (v.getId()) {
            //发送短信验证码
            case R.id.tv_login_sendCode:
                if (TextUtils.isEmpty(mobile)) {
                    showMsg(getString(R.string.p_set_phone));
                } else if(mobile.length()<11){
                    showMsg(getString(R.string.Please_enter_the_full_cell_phone_number));
                }else {
                    showProgress(getString(R.string.get_msg_code),true);
                    HttpMethod.getSmsCode(mobile,"1",mHandler);
                }
                break;
            //服务协议
            case R.id.tv_login_agreement:
//                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
//                intent.putExtra("type", 5);
//                startActivity(intent);
                break;
            //登陆
            case R.id.tv_login_submit:
                if (TextUtils.isEmpty(mobile)) {
                    showMsg(getString(R.string.p_set_phone));
                } else if (TextUtils.isEmpty(code)) {
                    showMsg(getString(R.string.please_print_message_code));
                } else {
                    showProgress(getString(R.string.logining),true);
                    HttpMethod.login(mobile,code,mHandler);
                }
                break;
            //关闭登陆界面
            case R.id.tv_login_cancel:
                finish();
                break;
            default:
                break;
        }
    }


    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //动态改变验证码秒数
                case 0x001:
                    tvSendCode.setText(time + getString(R.string.second));
                    tvSendCode.setBackground(getResources().getDrawable(R.drawable.code_pressed));
                    break;
                case 0x002:
                    if (null != mTimer) {
                        mTimer.cancel();
                    }
                    tvSendCode.setText(R.string.get_code);
                    MyApplication.spUtil.removeMessage("stoptime");
                    break;
                 //获得短信验证码
                case HandlerConstant.GET_SMS_CODE_SUCCESS:
                     clearTask();
                     BaseBean baseBean = (BaseBean) msg.obj;
                     if (baseBean == null) {
                        break;
                     }
                     if (baseBean.isSussess()) {
                        //先保存计时时间
                        MyApplication.spUtil.addString("stoptime", String.valueOf((System.currentTimeMillis() + 60000)));
                        time = 60;
                        startTime();
                     } else {
                        showMsg(baseBean.getMsg());
                     }
                    break;
                //登陆返回
                case HandlerConstant.LOGIN_SUCCESS:
                     clearTask();
                     final String message= (String) msg.obj;
                     if(TextUtils.isEmpty(message)){
                         break;
                     }
                     loginSuccess(message);
                    break;
                case HandlerConstant.REQUST_ERROR:
                     clearTask();
                     showMsg(getString(R.string.http_error));
                    break;
                case HandlerConstant.GET_DATA_ERROR:
                     clearTask();
                     showMsg(getString(R.string.Data_parsing_error_please_try_again_later));
                     break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 登陆成功
     * @param message
     */
    private void loginSuccess(String message){
        try {
            JSONObject jsonObject=new JSONObject(message);
            if(jsonObject.getInt("code")!=200){
                showMsg(jsonObject.getString("msg"));
                return;
            }
            JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));
            //保存token
            MyApplication.spUtil.addString(SPUtil.ACCESS_TOKEN, jsonObject1.getString("access_token"));
            MyApplication.spUtil.addString(SPUtil.AUTH_TOKEN, jsonObject1.getString("auth_token"));
            setClass(MainActivity.class);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 动态改变验证码秒数
     */
    private void startTime() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (time <= 0) {
                    mHandler.sendEmptyMessage(0x002);
                } else {
                    --time;
                    mHandler.sendEmptyMessage(0x001);
                }
            }
        }, 0, 1000);
    }


    /**
     * 判断验证码秒数是否超过一分钟
     */
    private void checkTime() {
        String stoptime = MyApplication.spUtil.getString("stoptime");
        if (!TextUtils.isEmpty(stoptime)) {
            int t = (int) ((Double.parseDouble(stoptime) - System.currentTimeMillis()) / 1000);
            if (t > 0) {
                time = t;
                startTime();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        //删除handler中的消息
        removeHandler(mHandler);
        super.onDestroy();
    }
}

