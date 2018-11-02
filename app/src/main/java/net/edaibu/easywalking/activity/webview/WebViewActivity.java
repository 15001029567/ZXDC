package net.edaibu.easywalking.activity.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.edaibu.easywalking.R;
import net.edaibu.easywalking.activity.BaseActivity;

/**
 * 展示h5页面
 */
public class WebViewActivity extends BaseActivity {

    private WebView webView;
    private TextView tvTitle;
    private ProgressBar pg;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        loadH5();
    }


    /**
     * 初始化控件
     */
    @SuppressLint("JavascriptInterface")
    private void initView(){
        tvTitle = (TextView) findViewById(R.id.tv_head);
        webView = (WebView) findViewById(R.id.webview);
        pg=(ProgressBar) findViewById(R.id.progressBar);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        // 控制webView缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(mContext, "QiQi");
        webView.setWebViewClient(new webViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                return false;
            }
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    pg.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    /**
     * 加载h5页面
     */
    private void loadH5(){
        final int type=getIntent().getIntExtra("type",-1);
        String url = null;
        switch (type){
            case 1:
                 url="http://m.edaibu.net/app/agreement.html";
                 break;
        }
        webView.loadUrl(url);
    }


    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
