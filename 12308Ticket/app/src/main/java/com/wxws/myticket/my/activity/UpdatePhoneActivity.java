package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.CommonUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.widgets.view.ClearEditText;

import java.util.UUID;

/**
 * desc:
 * Date: 2016-11-07 18:19
 *
 * @author jiangyan
 */
public class UpdatePhoneActivity extends BaseActivity {

    private Button mBtGetCode;
    private ClearEditText mCetPsw;
    private EditText mEtCode;
    private Button mBtSubmit;
    private ClearEditText mCetNetPhone;
    private String mPassword;
    private String mMobile;
    private String mValidateCode;
    private TimeCount mTime;
    private String mNonce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatephone);

        bindTitleViews();
        barTitle.setText(getString(R.string.account_change_phone_num));

        initView();
        initData();
    }

    /**
     * 获取输入数据
     */
    private void getInputData() {
        mPassword = mCetPsw.getText().toString().trim();
        mMobile = mCetNetPhone.getText().toString().trim();
        mValidateCode = mEtCode.getText().toString().trim();
    }

    private void initView() {
        mCetPsw = (ClearEditText) findViewById(R.id.cet_updatephone_psw);
        mCetNetPhone = (ClearEditText) findViewById(R.id.cet_updatephone_newphone);
        mEtCode = (EditText) findViewById(R.id.et_updatephone_code);
        mBtGetCode = (Button) findViewById(R.id.bt_updatephone_getcode);
        mBtSubmit = (Button) findViewById(R.id.bt_updatephone_submit);

        mBtGetCode.setEnabled(false);
        mBtSubmit.setEnabled(false);

        mCetNetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!StringUtils.isPhone(s + "")) {
                    mBtGetCode.setEnabled(false);
                } else {
                    mBtGetCode.setEnabled(true);
                }

                if (StringUtils.isPsw(mCetPsw.getText().toString()) && !TextUtils.isEmpty(mEtCode.getText()) && StringUtils.isPhone(mCetNetPhone.getText().toString().trim())) {
                    mBtSubmit.setEnabled(true);
                } else {
                    mBtSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        TextWatcher emptyWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isPsw(mCetPsw.getText().toString()) && !TextUtils.isEmpty(mEtCode.getText()) && StringUtils.isPhone(mCetNetPhone.getText().toString().trim())) {
                    mBtSubmit.setEnabled(true);
                } else {
                    mBtSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mCetPsw.addTextChangedListener(emptyWatcher);
        mEtCode.addTextChangedListener(emptyWatcher);
    }

    private void initData() {


        String mToken = UUID.randomUUID().toString();

        // 构造CountDownTimer对象
        mTime = new TimeCount(1 * 60 * 1000, 1000);
        mBtGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputData();
                if (!StringUtils.isNullOrEmpty(mMobile)) {

                    HttpRequestManager.getInstance().getCode("4", mMobile, new ApiSubscriber<JsonResult<String>>(UpdatePhoneActivity.this) {
                        @Override
                        public void onNext(JsonResult<String> result) {
                            if ("0000".equals(result.getResultCode())) {
                                mTime.start();
                                mBtGetCode.setEnabled(false);
                                mEtCode.requestFocus();
                                mNonce = result.getObject();
                            } else {
                                mBtGetCode.setEnabled(true);
                                mBtGetCode.setText(R.string.re_get_verify);
                                if (mTime != null) {
                                    mTime.cancel();
                                }
                            }
                            ToastManager.getInstance().showToast(UpdatePhoneActivity.this, result.getResultMsg());
                        }
                    });

                } else {
                    ToastManager.getInstance().showToast(UpdatePhoneActivity.this, "请先填写手机号码");
                }

            }
        });

        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getInputData();
                if (validate()) {

                    HttpRequestManager.getInstance().updatePhone(mPassword, mMobile, mNonce, mValidateCode, new ApiSubscriber<JsonResult<String>>(UpdatePhoneActivity.this) {
                        @Override
                        public void onNext(JsonResult<String> result) {
                            if ("0000".equals(result.getResultCode())) {
                                ToastManager.getInstance().showToast(UpdatePhoneActivity.this, "更换成功，请使用新手机号登录！");

                                PreferencesUtils.saveDataObject(UpdatePhoneActivity.this, ConfigConstants.USERINFO, null);
                                PreferencesUtils.saveDataObject(UpdatePhoneActivity.this, ConfigConstants.TRAIN_LOGININFO, null);
                                PreferencesUtils.putDataString(UpdatePhoneActivity.this, ConfigConstants.AUTHTOKEN, null);
                                PreferencesUtils.saveDataObject(UpdatePhoneActivity.this,ConfigConstants.LOGININFO,null);
                                RxBus.getInstance().post(new CommonEvent(CommonEvent.LOGIN_LOGOUT));

                                Intent intent = new Intent(UpdatePhoneActivity.this, LoginActivity.class);
                                intent.putExtra("isChange", true);
                                UpdatePhoneActivity.this.startActivity(intent);
                                UpdatePhoneActivity.this.finish();
                            } else {
                                ToastManager.getInstance().showToast(UpdatePhoneActivity.this, result.getResultMsg());
                            }
                        }
                    });

                }
            }
        });
    }

    private boolean validate() {

        if (StringUtils.isNullOrEmpty(mPassword)) {
            ToastManager.getInstance().showToast(UpdatePhoneActivity.this, getString(R.string.account_new_password_null));
            return false;
        }
        if (StringUtils.isNullOrEmpty(mMobile) || mMobile.length() != 11) {
            ToastManager.getInstance().showToast(UpdatePhoneActivity.this, getString(R.string.account_new_mobile_null));
            return false;
        }
        if (StringUtils.isNullOrEmpty(mValidateCode)) {
            ToastManager.getInstance().showToast(UpdatePhoneActivity.this, getString(R.string.account_validate_null));
            return false;
        }
        return true;
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mBtGetCode.setEnabled(true);
            mBtGetCode.setText(R.string.re_get_verify);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            int second = (int) (millisUntilFinished / 1000) % 60;
            if (second > 0) {
                String str = String.format(getString(R.string.remain_time), second);
                mBtGetCode.setText(str);

                String content = second + "s";

                int bstart = str.indexOf(content);
                int bend = bstart + content.length();

                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(Color.WHITE), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new AbsoluteSizeSpan(CommonUtil.dip2px(UpdatePhoneActivity.this, 16f)), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mBtGetCode.setText(style);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTime != null) {
            mTime.cancel();
        }
    }
}