package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.CommonUtil;
import com.wxws.myticket.common.utils.Md5Utils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.LoginInfo;
import com.wxws.myticket.my.widgets.view.ClearEditText;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * desc:
 * Date: 2016-11-01 10:46
 *
 * @author jiangyan
 */
public class RegistActivity extends BaseActivity {

    private String mToken;
    private ClearEditText mCetPhone;
    private CheckBox mCbAgree;
    private Button mBtGetCode;
    private Button mBtRegiste;
    private ClearEditText mCetPsw;
    private EditText mEtVerifyCode;
    private CheckBox mCbIsPswVisible;
    private TextView mTv12308Protocol;
    private TimeCount mTime;
    private String mNonce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        initTitle();
        initView();
        mToken = UUID.randomUUID().toString();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText(R.string.user_regist);
        flTitleBarRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.login);
        tvRight.setTextColor(getResources().getColor(R.color.app_style));
        tvRight.setBackgroundResource(R.drawable.title_right_bg);
        tvRight.setTextSize(14f);
        tvRight.setVisibility(View.VISIBLE);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(20, 20, 20, 20);
        tvRight.setLayoutParams(layoutParams);
    }

    private void initView() {
        mCetPhone = (ClearEditText) findViewById(R.id.cet_registe_phone);
        // 自动弹出软键盘
        mCetPhone.setFocusableInTouchMode(true);
        mCetPhone.requestFocus();

        Timer timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) mCetPhone.getContext().getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(mCetPhone, 0);
                }
            }, 998);
        } catch (Exception ex) {
        }

        mBtGetCode = (Button) findViewById(R.id.bt_registe_getcode);
        mEtVerifyCode = (EditText) findViewById(R.id.et_registe_verifycode);
        mCetPsw = (ClearEditText) findViewById(R.id.cet_registe_psw);
        mCbIsPswVisible = (CheckBox) findViewById(R.id.cb_registe_ispswvisible);
        mBtRegiste = (Button) findViewById(R.id.bt_registe);

        mCbAgree = (CheckBox) findViewById(R.id.cb_registe_agree);
        mTv12308Protocol = (TextView) findViewById(R.id.tv_registe_12308protocol);

        mBtRegiste.setEnabled(false);
        mBtGetCode.setEnabled(false);

        mBtGetCode.setOnClickListener(this);
        mBtRegiste.setOnClickListener(this);
        mTv12308Protocol.setOnClickListener(this);
        flTitleBarRight.setOnClickListener(this);

        mCetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!StringUtils.isPhone(s + "") || !mCbAgree.isChecked()) {
                    mBtGetCode.setEnabled(false);
                } else {
                    mBtGetCode.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && StringUtils.isPhone(mCetPhone.getText().toString() + "")) {
                    mBtGetCode.setEnabled(true);
                    mBtRegiste.setEnabled(true);
                } else {
                    mBtGetCode.setEnabled(false);
                    mBtRegiste.setEnabled(false);
                }
            }
        });

        mCbIsPswVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCetPsw.setInputType(InputType.TYPE_CLASS_TEXT
                        | (isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                mCetPsw.setSelection(mCetPsw.getText().length());
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isPsw(mCetPsw.getText().toString()) && !TextUtils.isEmpty(mEtVerifyCode.getText()) && mCbIsPswVisible.isChecked()) {
                    mBtRegiste.setEnabled(true);
                } else {
                    mBtRegiste.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mCetPsw.addTextChangedListener(textWatcher);
        mEtVerifyCode.addTextChangedListener(textWatcher);

        mCbIsPswVisible.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mCetPsw
                                .setTransformationMethod(HideReturnsTransformationMethod
                                        .getInstance());
                        return true;
                    case MotionEvent.ACTION_UP:
                        mCetPsw
                                .setTransformationMethod(PasswordTransformationMethod
                                        .getInstance());
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        mCetPsw
                                .setTransformationMethod(HideReturnsTransformationMethod
                                        .getInstance());
                        return true;
                    default:
                        break;
                }
                mCetPsw
                        .setTransformationMethod(PasswordTransformationMethod
                                .getInstance());
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_registe:
                if (validPost()) {
                    register();
                }
                break;
            case R.id.bt_registe_getcode:
                if ((mBtGetCode.getText().toString().trim()).contains("获取")) {
                    getValidCode();
                }
                break;
            case R.id.tv_registe_12308protocol:
                Intent intent = new Intent(RegistActivity.this, H5CommonActivity.class);
                //TODO H5 12308协议
                intent.putExtra("URL", "http://www.baidu.com");
                startActivityWithAnim(intent);
                break;
            case R.id.title_bar_right:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void getValidCode() {

        String mobile = mCetPhone.getText().toString();
        if (StringUtils.isNullOrEmpty(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.mobile_required);
            return;
        }
        if (!StringUtils.isPhone(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.not_phone_);
            return;
        }
        // 构造CountDownTimer对象
        mTime = new TimeCount(1 * 60 * 1000, 1000);
        HttpRequestManager.getInstance().getCode("1", mobile, new ApiSubscriber<JsonResult<String>>(RegistActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    mTime.start();
                    mBtGetCode.setEnabled(false);
                    mEtVerifyCode.requestFocus();
                    mNonce = result.getObject();
                } else {
                    mBtGetCode.setEnabled(true);
                    mBtGetCode.setText(R.string.re_get_verify);
                    if (mTime != null) {
                        mTime.cancel();
                    }
                }
                ToastManager.getInstance().showToast(getApplicationContext(), result.getResultMsg());
            }
        });
    }

    /**
     * 提交用户注册信息
     */
    public void register() {

        String md5Psw = Md5Utils.getMd5Str(mCetPsw.getText().toString());
        HttpRequestManager.getInstance().registe(mEtVerifyCode.getText().toString(), mCetPhone.getText().toString(),md5Psw, mNonce, new ApiSubscriber<JsonResult<String>>(RegistActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    ToastManager.getInstance().showToast(getApplicationContext(), R.string.register_succes);

                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setRempsw(false);
                    loginInfo.setUsername(mCetPhone.getText().toString());
                    PreferencesUtils.saveDataObject(RegistActivity.this, ConfigConstants.LOGININFO, loginInfo);

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
        String yzm = mEtVerifyCode.getText().toString();
        String passwd = mCetPsw.getText().toString();
        if (StringUtils.isNullOrEmpty(yzm)) {
            ToastManager.getInstance().showToast(this, R.string.valid_code_required);
            return false;
        }
        if (StringUtils.isNullOrEmpty(passwd)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_required);
            return false;
        }
        if (!StringUtils.requireLength(passwd, 6, 20)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_format_error);
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
                mBtGetCode.setEnabled(false);

                String content = second + "s";

                int bstart = str.indexOf(content);
                int bend = bstart + content.length();

                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(Color.WHITE), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new AbsoluteSizeSpan(CommonUtil.dip2px(RegistActivity.this, 16f)), bstart, bend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mBtGetCode.setText(style);
            }
        }
    }
}