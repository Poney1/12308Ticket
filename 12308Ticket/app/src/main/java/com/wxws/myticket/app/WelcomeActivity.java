package com.wxws.myticket.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.umeng.analytics.MobclickAgent;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.entity.VersionInfo;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.common.utils.function.ManifestMetaDataUtil;
import com.wxws.myticket.common.utils.store.PreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;

/**
 * desc: 欢迎页
 * Date: 2016/10/10 14:32
 *
 * @auther: lixiangxiang
 */
public class WelcomeActivity extends BaseActivity implements OnGestureListener {

    private ViewFlipper flipper;
    /**
     * 手机密度
     */
    private float scale;
    private int imagePadding;
    private ImageView[] imageViews;
    /**
     * 小圆点 父控件
     */
    private LinearLayout dotsBox;
    private LinearLayout llWelcome;
    private FrameLayout flFirst;
    private ImageView imgOpen;
    //是否第一次进入
    private boolean isFirst = true;
    // 是否点击进入
    private boolean isClickGo = false;
    private InnerHandler handler ;
    //默认选中第一页
    private int i = 0;
    private GestureDetector detector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        scale = getResources().getDisplayMetrics().density;

        initView();
        initData();
        initSdk();
        handler.sendEmptyMessageDelayed(0, 5000);

    }

    private void initSdk() {
        //初始化shareSdk
        ShareSDK.initSDK(this);
    }

    private void initData() {
        getVersionInfo();
        if (mVersionInfo != null){
            isFirst = false;
        }
        isFirstEnter(isFirst);

        handler = new InnerHandler(this);

        if (!isFirst){
            handler.sendEmptyMessageDelayed(0,5000);
        }
        getVersionFromService();

    }

    private void initView() {
        llWelcome = getViewById(R.id.ll_welcome);
        flFirst = getViewById(R.id.fl_first);
        imgOpen = getViewById(R.id.img_open);

        flipper =  getViewById(R.id.ViewFlipper1);
        flipper.addView(addImageView(R.mipmap.welcome_1));
        flipper.addView(addImageView(R.mipmap.welcome_2));
        flipper.addView(addImageView(R.mipmap.welcome_3));
        flipper.addView(addImageView(R.mipmap.welcome_4));
        dotsBox = getViewById(R.id.dots);

        detector = new GestureDetector(this);
        imgOpen.setOnClickListener(this);
        setDots();
    }

    private View addImageView(int id) {
        ImageView iv = new ImageView(this);
        try {
            iv.setBackgroundResource(id);
        } catch (OutOfMemoryError error) {
        }
        return iv;
    }

    private void isFirstEnter(boolean first){
        if (first){
            flFirst.setVisibility(View.VISIBLE);
            llWelcome.setVisibility(View.GONE);
        }else {
            flFirst.setVisibility(View.GONE);
            llWelcome.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 设置底部圆点
     */
    public void setDots() {
        final int imageCount = flipper.getChildCount();   // 圆点数量
        int imageParams = (int) (scale * 10 + 0.5f);//图标大小， XP与DP转换，适应不同分辨率
        imagePadding = (int) (scale * 8 + 0.5f);//图标间距
        imageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imageParams, imageParams, 0);
            lp.setMargins(imagePadding, 0, imagePadding, imagePadding);
            ImageView mImageView = new ImageView(WelcomeActivity.this);
            mImageView.setLayoutParams(lp);
            imageViews[i] = mImageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.mipmap.welcome_on);
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.welcome_off);
            }
            imageViews[i].setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            dotsBox.addView(imageViews[i]);
        }
    }

    //获取版本信息
    private void getVersionFromService(){
        int versionCode = ManifestMetaDataUtil.getVersionCode(WelcomeActivity.this);
        HttpRequestManager.getInstance().getVersion(versionCode,new ApiSubscriber<JsonResult<VersionInfo>>(WelcomeActivity.this) {
            @Override
            public void onNext(JsonResult<VersionInfo> version) {
                if ("0000".equals(version.getResultCode())){
                    VersionInfo temp = version.getObject();
                    if (temp == null){
                        return;
                    }

                    if (!isFirst || isClickGo) {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    PreferencesUtils.saveDataObject(WelcomeActivity.this, ConfigConstants.VERSIONINFO,temp);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_open:
                isClickGo = true;
                startActivityWithAnim(MainActivity.class);
                finish();
                break;
        }

    }

    private static class InnerHandler extends Handler {
        WeakReference<WelcomeActivity> activityWeakReference;

        public InnerHandler(WelcomeActivity welcomeActivity) {
            this.activityWeakReference = new WeakReference<>(welcomeActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WelcomeActivity welcomeActivity = activityWeakReference.get();
            if (welcomeActivity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    welcomeActivity.startActivityWithAnim(MainActivity.class);
                    welcomeActivity.finish();
                    break;
                case 1:
                    welcomeActivity.imgOpen.setVisibility(View.VISIBLE);
                    welcomeActivity.dotsBox.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (flipper != null) {
            this.flipper.removeAllViews();
            this.flipper.clearAnimation();
            this.flipper = null;
        }
        if (handler != null) {
            handler.removeMessages(0);
            handler.removeCallbacksAndMessages(null);
        }

        //超过5秒强制回收
         if (mLoadingPopWindow!=null){
             if (mLoadingPopWindow.isShowing())
             mLoadingPopWindow.dismiss();
             mLoadingPopWindow.cancel();
         }
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector != null) {
            return detector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null) {
            return false;
        }
        if (e1.getX() - e2.getX() > 120) {
            if (i < 3) {
                i++;
                setImage(i);
                this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_right_in));
                this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_left_out));
                this.flipper.showNext();
                if (i == 3) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(1);
                            super.run();
                        }
                    }.start();
                }
            }
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            if (i > 0) {
                i--;
                setImage(i);
                this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_left_in));
                this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_right_out));
                this.flipper.showPrevious();
                imgOpen.setVisibility(View.GONE);
                dotsBox.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return false;
    }

    /**
     * 修改点状态
     * @param i
     */
    private void setImage(int i) {
        for (int j = 0; j < 4; j++) {
            if (j == i) {
                imageViews[j].setBackgroundResource(R.mipmap.welcome_on);
            } else {
                imageViews[j].setBackgroundResource(R.mipmap.welcome_off);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(WelcomeActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(WelcomeActivity.this);
    }
}
