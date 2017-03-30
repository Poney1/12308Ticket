package com.wxws.myticket.my.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.CommonUtil;
import com.wxws.myticket.common.utils.Md5Utils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * desc: 忘记密码
 * Date: 2016-11-01 10:45
 *
 * @author jiangyan
 */
public class FindPswActivity extends BaseActivity {

    private EditText mEtPhone, mEtNewpsw, mEtConfirmpsw, mEtCode;
    private Button mBtGetCode;
    private Button mBtSubmit;
    private String mNonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpsw);
        bindTitleViews();
        barTitle.setText(R.string.find_pwd);
        bindViews();
    }

    private void bindViews() {
        mEtPhone = (EditText) findViewById(R.id.et_findpsw_phone);
        // 自动弹出软键盘
        mEtPhone.setFocusableInTouchMode(true);
        mEtPhone.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) mEtPhone.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEtPhone, 0);
            }
        }, 998);
        mEtCode = (EditText) findViewById(R.id.et_findpsw_verify);
        mBtGetCode = (Button) findViewById(R.id.bt_findpsw_getcode);
        mBtSubmit = (Button) findViewById(R.id.bt_findpsw_submit);
        mEtNewpsw = (EditText) findViewById(R.id.et_findpsw_newpsw);
        mEtConfirmpsw = (EditText) findViewById(R.id.et_findpsw_confirmpsw);
        mBtSubmit.setOnClickListener(this);
        mBtGetCode.setOnClickListener(this);

        mBtSubmit.setEnabled(false);
        mBtGetCode.setEnabled(false);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //获取验证码按钮
                if (StringUtils.isPhone(s + "") && (getResources().getString(R.string.re_get_verify).equals(mBtGetCode.getText().toString()) || getResources().getString(R.string.get_verify).equals(mBtGetCode.getText().toString()))) {
                    mBtGetCode.setEnabled(true);
                } else {
                    mBtGetCode.setEnabled(false);
                }
                //是否显示确认按钮
                if (StringUtils.isPhone(s + "") && !TextUtils.isEmpty(mEtCode.getText())) {
                    mBtSubmit.setEnabled(true);
                } else {
                    mBtSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //是否显示确认按钮
                if (StringUtils.isPhone(mEtPhone.getText().toString()) && StringUtils.isPsw(mEtNewpsw.getText().toString()) && StringUtils.isPsw(mEtConfirmpsw.getText().toString()) && !TextUtils.isEmpty(mEtCode.getText())) {
                    mBtSubmit.setEnabled(true);
                } else {
                    mBtSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mEtCode.addTextChangedListener(textWatcher);
        mEtNewpsw.addTextChangedListener(textWatcher);
        mEtConfirmpsw.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_findpsw_getcode:
                getValidCode();
                break;
            case R.id.bt_findpsw_submit:
                if (validPost()) {
                    resetPsw();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void getValidCode() {

        String mobile = mEtPhone.getText().toString();
        if (StringUtils.isNullOrEmpty(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.mobile_required);
            return;
        }
        if (!StringUtils.isPhone(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.not_phone_);
            return;
        }
        mBtGetCode.setEnabled(false);
        final TimeCount time = new TimeCount(1 * 60 * 1000, 1000);// 构造CountDownTimer对象
        time.start();

        HttpRequestManager.getInstance().getCode("2", mobile, new ApiSubscriber<JsonResult<String>>(FindPswActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    ToastManager.getInstance().showToast(getApplicationContext(), "验证码获取成功!");
                    mNonce = result.getObject();
                } else {
                    time.cancel();
                    mBtGetCode.setEnabled(true);
                    mBtGetCode.setText(R.string.re_get_verify);
                    ToastManager.getInstance().showToast(getApplicationContext(), result.getResultMsg());
                }
            }
        });

    }

    /**
     * 重置密码
     */
    public void resetPsw() {
        String md5psw = Md5Utils.getMd5Str(mEtNewpsw.getText().toString());
        HttpRequestManager.getInstance().resetPsw(mEtCode.getText().toString(), mEtPhone.getText().toString(), mNonce,md5psw, new ApiSubscriber<JsonResult<String>>(FindPswActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    ToastManager.getInstance().showToast(getApplicationContext(), "密码重置成功");
                    finish();
                } else {
                    ToastManager.getInstance().showToast(getApplicationContext(), result.getResultMsg());
                }
            }
        });
    }

    /**
     * 验证用户提交信息
     *
     * @return
     */
    public boolean validPost() {
        String mobile = mEtPhone.getText().toString();
        String validcode = mEtCode.getText().toString();
        if (StringUtils.isNullOrEmpty(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.froget_phone_hint);
            return false;
        }
        if (!StringUtils.requireLength(mEtNewpsw.getText().toString(), 6, 20)) {
            ToastManager.getInstance().showToast(this, R.string.new_pwd_hint);
            return false;
        }
        if (!StringUtils.requireLength(mEtConfirmpsw.getText().toString(), 6, 20)) {
            ToastManager.getInstance().showToast(this, R.string.confirm_new_pwd_hint);
            return false;
        }
        if (StringUtils.isNullOrEmpty(validcode)) {
            ToastManager.getInstance().showToast(this, R.string.valid_code_required);
            return false;
        }
        if (!StringUtils.isPhone(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.not_phone_);
            return false;
        }
        //两次密码必须输入一致
        if (!mEtNewpsw.getText().toString().equals(mEtConfirmpsw.getText().toString())) {
            ToastManager.getInstance().showToast(this, "两次密码输入不一致,请重新输入");
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
                style.setSpan(new ForegroundColorSpan(Color.RED), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new AbsoluteSizeSpan(CommonUtil.dip2px(FindPswActivity.this, 16f)), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mBtGetCode.setText(style);
            }
        }
    }

}