package com.wxws.myticket.bus.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;

/**
 * desc: 包车协议显示页面
 * Date: 2016-11-18 14:19
 *
 * @author jiangyan
 */
public class BaocheProtocolActivity extends BaseActivity implements OnClickListener {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_rent_protocol);
        bindTitleViews();
        bindLoadingView();
        showLoading();
        initView();

    }

    private void initView() {
        barTitle.setText("包车协议");
        mWebView = (WebView) findViewById(R.id.rent_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          mLlLayoutLoading.setVisibility(View.VISIBLE);
                                          super.onPageStarted(view, url, favicon);
                                      }

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          super.onPageFinished(view, url);
                                          mLlLayoutLoading.setVisibility(View.GONE);
                                      }
                                  }
        );

        HttpRequestManager.getInstance().getBaocheProtocol(new ApiSubscriber<JsonResult<String>>(BaocheProtocolActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    String url = result.getObject();
                    if (url != null) {
                        mWebView.loadUrl(url);
                        dismissLoading();
                    }
                }else{
                    //TODO showErrorOrNoData("");
                }
            }
        });
    }
}
