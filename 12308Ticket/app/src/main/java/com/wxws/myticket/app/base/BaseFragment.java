package com.wxws.myticket.app.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.bus.entity.VersionInfo;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.dialog.LoadingProgressDialog;
import com.wxws.myticket.my.Entity.LoginResponsePara;
import com.wxws.myticket.my.activity.LoginActivity;

import pl.droidsonroids.gif.GifImageView;

/**
 * desc: fragment 基类
 * Date: 2016/10/9 10:31
 *
 * @auther: lixiangxiang
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    protected View view;
    protected FrameLayout flLeft;
    protected TextView barTitle;
    protected FrameLayout titleBarRight;
    protected View tvRight;
    protected LoginResponsePara userInfo;
    protected BaseActivity mActivity;
    public LinearLayout mLlLayoutLoading;
    protected GifImageView mGivGif;
    protected LinearLayout mLlTips;
    protected ImageView mIvTips;
    protected TextView mTvTips;
    protected Button mBtTodo;
    public LoadingProgressDialog mLoadingPopWindow;
    protected VersionInfo mVersionInfo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingPopWindow = new LoadingProgressDialog(mActivity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    /**
     * 通过id 获取控件
     *
     * @param id
     * @param <V>
     * @return
     */
    protected <V extends View> V getViewById(@IdRes int id) {
        return (V) view.findViewById(id);
    }

    /**
     * 标题视图
     */
    protected void bindTitleViews() {
        flLeft = getViewById(R.id.fl_left);
        barTitle = getViewById(R.id.bar_title);
        titleBarRight = getViewById(R.id.title_bar_right);
        tvRight = getViewById(R.id.tv_right);

        titleBarRight.setVisibility(View.GONE);
        flLeft.setVisibility(View.GONE);
        flLeft.setOnClickListener(this);
        titleBarRight.setOnClickListener(this);
    }

    //获取用户信息
    protected void getUserInfo() {
        userInfo = (LoginResponsePara) PreferencesUtils.readDataObject(getActivity(), ConfigConstants.USERINFO);
    }

    //获取版本信息
    protected void getVersionInfo() {
        mVersionInfo = (VersionInfo) PreferencesUtils.readDataObject(mActivity, ConfigConstants.VERSIONINFO);
    }
    /**
     * 判断用户是否已经登陆
     */
    protected boolean checkLogin() {
        getUserInfo();// 不可删除。。。
        return !StringUtils.isNullOrEmpty(PreferencesUtils.getDataString(getActivity(), ConfigConstants.AUTHTOKEN));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_left:
                InputMethod.closeInputMethod(getActivity());
                ToastManager.getInstance().cancelToast();
                getActivity().finish();
                break;
        }
    }

    /**
     * 绑定loadingview
     */
    public void bindLoadingView() {
        mLlLayoutLoading = (LinearLayout) view.findViewById(R.id.layout_loading);
        mGivGif = (GifImageView) view.findViewById(R.id.giv_gifimg);
        mLlTips = (LinearLayout) view.findViewById(R.id.layout_tips);
        mIvTips = (ImageView) view.findViewById(R.id.iv_tips);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);
        mBtTodo = (Button) view.findViewById(R.id.bt_todo);
    }

    /**
     * show loading
     */
    public void showLoading() {
        if (mLlLayoutLoading != null) {
            mLlLayoutLoading.setVisibility(View.VISIBLE);
        }
        if (mGivGif != null) {
            mGivGif.setVisibility(View.VISIBLE);
        }
        if (mLlTips != null) {
            mLlTips.setVisibility(View.GONE);
        }
    }

    /**
     * dismiss all loading layout,not only loading
     */
    public void dismissLoading() {

        if (mLlLayoutLoading != null) {
            mLlLayoutLoading.setVisibility(View.GONE);
        }
    }

    /**
     * 除优惠券外显示无数据
     */
    public void showErrorOrNoData(String tips,int img){
        showErrorOrNoData(tips,R.string.login,img,false);
    }


    /**
     * @param tips    可以传入需要展示的 text
     * @param btntext 传入需要展示的 按钮 的文字 string  id
     * @param tipsImg 传入 背景图片 id
     */
    public void showErrorOrNoData(String tips, int btntext, int tipsImg) {
        showErrorOrNoData(tips, btntext, tipsImg, false);
    }

    /**
     * @param tips
     * @param btntext
     * @param tipsImg
     * @param btnShow 控制按钮是否显示
     */
    public void showErrorOrNoData(String tips, final int btntext, int tipsImg, boolean btnShow) {
        String btnStr = getResources().getString(btntext);
        if (mLlLayoutLoading == null || mGivGif == null || mLlTips == null || mIvTips == null || mTvTips == null || mBtTodo == null) {
            return;
        }
        showLoadingOrError(false);
        mTvTips.setText(tips);
        mBtTodo.setText(btnStr);
        mIvTips.setImageResource(tipsImg);

        if (btnShow) {
            mBtTodo.setVisibility(View.VISIBLE);
        } else {
            mBtTodo.setVisibility(View.GONE);
        }
        mBtTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (btntext) {
                    case R.string.net_refresh_again:
                        clickReload();
                        break;
                    case R.string.net_open_settings:
                        // 打开网络设置
                        openNetSettings();
                        break;
                    case R.string.net_return_pre_page:
                        getActivity().finish();
                        break;
                    case R.string.tips_login_order:
                        mActivity.startActivityWithAnim(LoginActivity.class);
                        break;
                    case R.string.go_login :
                        mActivity.startActivityWithAnim(LoginActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 控制 加载页
     *
     * @param isLoading
     */
    private void showLoadingOrError(boolean isLoading) {
        if (isLoading) {
            mLlLayoutLoading.setVisibility(View.VISIBLE);
            mGivGif.setVisibility(View.VISIBLE);
            mLlTips.setVisibility(View.GONE);
        } else {
            mLlLayoutLoading.setVisibility(View.VISIBLE);
            mGivGif.setVisibility(View.GONE);
            mLlTips.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打开网络设置
     */
    private void openNetSettings() {
        Intent intent = null;
        try {
            // 判断手机系统的版本 即API大于10 就是3.0或以上版本
            if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            startActivity(intent);
        } catch (Exception ex) {
        }
        // 更改界面显示为点击重试
        showErrorOrNoData(getString(R.string.net_click_reload), R.string.click_reload, R.mipmap.base_offerline);
    }


    /**
     * click to reload
     */
    protected void clickReload() {

    }

    /**
     * 启动时使用FADE IN or OUT 动画
     *
     * @param intent
     */
    public void startActivityWithAnim(Intent intent) {
        getActivity().startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }

    /**
     * 启动时使用FADE IN or OUT 动画
     */
    public void startActivityForResultWithAnim(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }
}
