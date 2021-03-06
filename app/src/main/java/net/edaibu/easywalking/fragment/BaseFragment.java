package net.edaibu.easywalking.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import net.edaibu.easywalking.R;
import net.edaibu.easywalking.utils.error.CockroachUtil;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class BaseFragment extends Fragment {

    protected Activity mActivity;
    protected ProgressDialog progressDialog = null;
    public Dialog baseDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }


    /**
     * 跳转Activity
     * @param cls
     */
    protected void setClass(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        startActivity(intent);
    }

    /**
     * 自定义toast
     *
     * @param message
     */
    public void showMsg(String message) {
        if(null==mActivity){
            return;
        }
        Toast toast=Toast.makeText(mActivity, message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * loding进度条
     * @param msg
     * @param isClose
     */
    public void showProgress(String msg,boolean isClose) {
        if(null==mActivity){
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(isClose);
        progressDialog.show();
    }


    /**
     * 取消进度条
     */
    public void clearTask() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }


    /**
     * dialog弹框
     *
     * @param view
     */
    public Dialog dialogPop(View view, boolean b) {
        baseDialog = new Dialog(mActivity, R.style.ActionSheetDialogStyle);
        baseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        baseDialog.setTitle(null);
        baseDialog.setCancelable(b);
        baseDialog.setContentView(view);
        Window window = baseDialog.getWindow();
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        baseDialog.show();
        return baseDialog;
    }
    public void closeDialog() {
        if (baseDialog != null) {
            baseDialog.dismiss();
        }
    }


    /**
     * 删除handler中的消息
     * @param mHandler
     */
    public void removeHandler(Handler mHandler){
        if(null!=mHandler){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        CockroachUtil.install();
    }

    public void onDetach(){
        super.onDetach();
        mActivity = null;
    }

    public void onDestroy() {
        super.onDestroy();
//        CockroachUtil.clear();
    }
}
