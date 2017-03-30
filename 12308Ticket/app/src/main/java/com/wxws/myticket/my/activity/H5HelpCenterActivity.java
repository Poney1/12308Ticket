package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.utils.ToastManager;

/**
 * desc: 帮助中心，使用H5
 * Date: 2016-11-09 15:57
 *
 * @author jiangyan
 */
public class H5HelpCenterActivity extends BaseActivity {

    private WebView mWbView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5helpcenter);

        mProgressBar = (ProgressBar) findViewById(R.id.helpcenter_progressbar);
        mWbView = (WebView) findViewById(R.id.wv_helpcenter);
        initWebView();
        Log.e("mudo", "enter helpcenter");
        //http://192.168.1.37:8080/TestDemo/index.jsp
        mWbView.loadUrl("http://192.168.1.35:8421/help");//TODO 帮助中心地址和交互http://192.168.1.35:8421/help
    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT >= 11) {
            mWbView.removeJavascriptInterface("searchBoxJavaBridge_");
            mWbView.removeJavascriptInterface("accessibility");
            mWbView.removeJavascriptInterface("accessibilityTraversal");
        }
        WebSettings webSettings = mWbView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);//允许访问文件
        webSettings.setSupportZoom(true);//设置支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式（不使用缓存，只从网络获取数据.）
        webSettings.setBuiltInZoomControls(false);//启动内置缩放
        webSettings.setLoadsImagesAutomatically(true);//自动加载图片
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setLoadWithOverviewMode(true);//自适应屏幕
        webSettings.setDomStorageEnabled(true);// 是否开启本地DOM存储
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//https 5.0以上不可访问问题
        }

        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + ";company:12308");//userAgent,方便后端判断

        mWbView.setWebChromeClient(new WebChromeClient() {//进度条的加载

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWbView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //默认不接受证书，修改为接受。
                //super.onReceivedSslError(view, handler, error);

                handler.proceed();
            }
        });

        mWbView.addJavascriptInterface(new JSInterface(), "nativeFunc");

    }

    class JSInterface {
        @JavascriptInterface
        public void jumpToQQ() { //QQ客服
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=800071964";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                ToastManager.getInstance().showToast(H5HelpCenterActivity.this, R.string.not_install_qq);
            }

            ToastManager.getInstance().showToast(getApplicationContext(), "原生QQ客服");
        }

        @JavascriptInterface
        public void jumpToWechat() { // 微信客服
            Log.e("mudo", "connect to wechat");
            ToastManager.getInstance().showToast(getApplicationContext(), "原生微信客服");
        }

        @JavascriptInterface
        public void jumpToCSPhone() { // 客服热线

            ToastManager.getInstance().showToast(getApplicationContext(), "原生客服热线");
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:075582867116"));
            startActivityWithAnim(callIntent);
        }

        @JavascriptInterface
        public void backToNative() { // 退出当前页面
            Log.e("mudo", "out of activity");
            finish();
        }
    }

}