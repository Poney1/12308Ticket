package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.BuildConfig;
import com.wxws.myticket.R;
import com.wxws.myticket.app.MainActivity;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.constants.H5UrlConstants;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.widgets.view.PersonCommonLayout;
import com.wxws.myticket.order.widgets.CommonDialog;
//import com.wxws.myticket.order.widgets.CommonDialog;

/**
 * desc:
 * Date: 2016-10-31 11:49
 *
 * @author jiangyan
 */
public class SettingActivity extends BaseActivity {

    private PersonCommonLayout mPclModifypsw;
    private TextView mTvLogout;
    private LinearLayout mLlModifypsw;
    private LinearLayout mLlLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initTitle();
        initView();
        checkLayout();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("设置");
    }

    private void initView() {

        mPclModifypsw = (PersonCommonLayout) findViewById(R.id.pcl_setting_modifypsw);
        PersonCommonLayout pclAbout12308 = (PersonCommonLayout) findViewById(R.id.pcl_setting_about12308);
        PersonCommonLayout pclShengm = (PersonCommonLayout) findViewById(R.id.pcl_setting_shengming);
        PersonCommonLayout pclProtocol = (PersonCommonLayout) findViewById(R.id.pcl_setting_userprotocol);
        PersonCommonLayout pclQQLine = (PersonCommonLayout) findViewById(R.id.pcl_setting_qqlink);

        mTvLogout = (TextView) findViewById(R.id.tv_setting_logout);
        TextView tvVersion = (TextView) findViewById(R.id.tv_setting_vesrion);
        tvVersion.setText(String.format(getString(R.string.setting_version), BuildConfig.VERSION_NAME));

        mLlModifypsw = (LinearLayout) findViewById(R.id.ll_setting_modifypsw);
        mLlLogout = (LinearLayout) findViewById(R.id.ll_setting_logout);

        mPclModifypsw.setOnClickListener(this);
        pclAbout12308.setOnClickListener(this);
        pclShengm.setOnClickListener(this);
        pclProtocol.setOnClickListener(this);
        pclQQLine.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
    }

    private void checkLayout() {
        if (checkLogin()) {
            mLlModifypsw.setVisibility(View.VISIBLE);
            mLlLogout.setVisibility(View.VISIBLE);
        } else {
            mLlModifypsw.setVisibility(View.GONE);
            mLlLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pcl_setting_modifypsw: // 修改密码
                startActivityWithAnim(ModifyPswActivity.class);
                break;
            case R.id.pcl_setting_about12308:// TODO 关于12308，地址

                Intent aboutIntent = new Intent(SettingActivity.this, H5CommonActivity.class);
                aboutIntent.putExtra("URL", H5UrlConstants.SETTING_ABOUT12308);
                startActivityWithAnim(aboutIntent);

                break;
            case R.id.pcl_setting_shengming:// TODO 免责声明，地址

                Intent shengmIntent = new Intent(SettingActivity.this, H5CommonActivity.class);
                shengmIntent.putExtra("URL", H5UrlConstants.SETTING_MZSM);
                startActivityWithAnim(shengmIntent);

                break;
            case R.id.pcl_setting_userprotocol:// TODO 用户协议,地址

                Intent userIntent = new Intent(SettingActivity.this, H5CommonActivity.class);
                userIntent.putExtra("URL", H5UrlConstants.SETTING_USERPROTOCOL);
                startActivityWithAnim(userIntent);

                break;
            case R.id.pcl_setting_qqlink:// 联系QQ客服
                jumpToQQ();
                break;
            case R.id.tv_setting_logout:
                logout();
                break;
        }
    }

    private void jumpToQQ() {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=800071964";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            ToastManager.getInstance().showToast(this, R.string.not_install_qq);
        }
    }

    private void logout() {

        new CommonDialog(SettingActivity.this).showDialog(getString(R.string.logout_tips), getString(R.string.confirm_logout_tips), new DialogCallBackListener() {

            @Override
            public void doPositiveClick() {
                PreferencesUtils.saveDataObject(SettingActivity.this, ConfigConstants.USERINFO, null);
                PreferencesUtils.saveDataObject(SettingActivity.this, ConfigConstants.TRAIN_LOGININFO, null);
                PreferencesUtils.putDataString(SettingActivity.this, ConfigConstants.AUTHTOKEN, null);

                startActivityWithAnim(MainActivity.class);
                RxBus.getInstance().post(new CommonEvent(CommonEvent.LOGIN_LOGOUT));

                checkLayout();
            }

            @Override
            public void doPositiveClick(String string) {
            }

            @Override
            public void doNegativeClick() {
            }
        });
    }
}