package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;

import static com.wxws.myticket.R.id.webView;

/**
 * desc: 展示用h5activity
 * Date: 2016-11-08 15:01
 *
 * @author jiangyan
 */
public class H5CommonActivity extends BaseActivity {

    private String mURL;
    private String mTitle;
    private ProgressBar mPb;
    private WebView mWbView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);

        getDataFromPre();
        initTitle();
        initView();

        localURL(mWbView, mURL);
    }

    private void getDataFromPre(){
        mURL = getIntent().getStringExtra("URL");
    }

    private void initTitle(){
        bindTitleViews();
    }

    private void initView(){
        mPb = (ProgressBar) findViewById(R.id.progressbar);
        mWbView = (WebView) findViewById(webView);

        //填补js远程代码执行漏洞
        if (Build.VERSION.SDK_INT <= 11) {
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
                mTitle = title;
                barTitle.setText((TextUtils.isEmpty(title)) ? "" : title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mPb.setVisibility(View.GONE);
                } else {
                    if (mPb.getVisibility() == View.GONE) {
                        mPb.setVisibility(View.VISIBLE);
                    }
                    mPb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWbView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//此处能拦截超链接的url,即拦截href请求的内容.
                if (url.startsWith("tel:")) {//支持拨打电话
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("mailto:")) {//支持发送邮箱
                    MailTo mt = MailTo.parse(url);
                    Intent i = newEmailIntent(mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                    startActivity(i);
                    view.reload();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    private Intent newEmailIntent(String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }

    private void localURL(WebView webView, String url) {
        webView.loadUrl(url);
    }

}