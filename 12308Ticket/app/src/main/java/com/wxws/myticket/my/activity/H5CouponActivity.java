package com.wxws.myticket.my.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.H5UrlConstants;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.my.Entity.CouponResponsePara;

/**
 * desc:
 * Date: 2016-11-08 16:48
 *
 * @author jiangyan
 */
public class H5CouponActivity extends BaseActivity {

    private TextView mTvLineType;
    private TextView mTvQuanType;
    private TextView mTvTime;
    private TextView mTvPrice;
    private WebView mWbView;
    private CouponResponsePara mCouponPara;
    private ProgressBar mPb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5coupon);
        initTitle();
        getDataFromPre();
        initView();
        initWebView();
        initData();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("券详情页");
    }

    private void getDataFromPre() {
        mCouponPara = (CouponResponsePara) getIntent().getBundleExtra("couponParaBundle").getSerializable("couponPara");
    }

    private void initView() {

        mTvLineType = (TextView) findViewById(R.id.tv_h5coupon_linetype);
        mTvQuanType = (TextView) findViewById(R.id.tv_h5coupon_quantype);
        mTvTime = (TextView) findViewById(R.id.tv_h5coupon_time);
        mTvPrice = (TextView) findViewById(R.id.tv_h5coupon_price);
        mPb = (ProgressBar) findViewById(R.id.coupon_progress);
        mWbView = (WebView) findViewById(R.id.wv_h5coupon_webview);

        Button btBuyTicket = (Button) findViewById(R.id.bt_h5coupon_buyticket);
        btBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 发出事件，跳转去购票，应该根据不同类型做不同跳转
            }
        });
    }

    private void initData() {
        mTvLineType.setText(mCouponPara.getName());
        mTvTime.setText(DateTimeUtil.getDateyyyyMMdd(mCouponPara.getCreateTime()) + "至" + DateTimeUtil.getDateyyyyMMdd(mCouponPara.getExpireTime()));

        Log.e("mudo", "H5优惠券：" + H5UrlConstants.COUPON_DETAILS + mCouponPara.getCode());
        mWbView.loadUrl(H5UrlConstants.COUPON_DETAILS + mCouponPara.getCode());//TODO 优惠券详情+优惠券code
        mTvQuanType.setText("");//只显示name，其他的不处理。

        if (mCouponPara.getType().equals("1")) {//1 为面额，2为折扣
            mTvPrice.setText(mCouponPara.getCoinsAmt());
        } else {
            mTvPrice.setText(StringUtils.mul(mCouponPara.getDisctVal(), "0.1") + "折");
        }
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
    }

}