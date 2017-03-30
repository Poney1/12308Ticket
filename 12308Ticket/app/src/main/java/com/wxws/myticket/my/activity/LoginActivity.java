package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.Md5Utils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.UrlEncodeUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.LoginInfo;
import com.wxws.myticket.my.Entity.LoginResponsePara;
import com.wxws.myticket.my.Entity.TrainAccountInfo;
import com.wxws.myticket.my.Entity.TrainLoginResponseModel;
import com.wxws.myticket.my.widgets.view.ClearEditText;

import java.util.HashMap;
import java.util.UUID;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * desc: 登录页面
 */
public class LoginActivity extends BaseActivity {

    private Button mBtQQLogin;
    private Button mBtWechatLogin;
    private Button mBtWeiboLogin;
    Platform platform;
    private int mThridType;//1微信；2QQ；3微博；

    private String mLocalUserName;

    private String token;
    private TrainAccountInfo mTrainAccount;
    private CheckBox mCbPswLogin;
    private CheckBox mCbVerifyLogin;
    private ClearEditText mCetPhone;
    private ClearEditText mCetPsw;
    private CheckBox mCbIspswVisible;
    private CheckBox mCbRemePsw;
    private TextView mTvForgetPsw;
    private EditText mEtVerifyCode;
    private Button mBtGetVerifycode;
    private LinearLayout mLlPswLoginLayout;
    private LinearLayout mLlVerLoginLayout;
    private LinearLayout mLlothLoginLayout;
    private Button mBtLogin;
    private String mNonce;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            String openId = bundle.getString("userId");
            String userName = bundle.getString("userName");
            String userIcon = bundle.getString("userIcon");

            ThridLogin(openId, userIcon, userName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RxBus.getInstance().register(this);

        initTitle();
        initView();
        getLoginInfo();

        ShareSDK.initSDK(this);
        token = UUID.randomUUID().toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 后退
        RxBus.getInstance().post(new CommonEvent(CommonEvent.LOGIN_BACK));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
        RxBus.getInstance().unRegister(this);
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("登录");
        flTitleBarRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        flTitleBarRight.setOnClickListener(this);
        tvRight.setTextSize(14f);
        tvRight.setText("注册");
        tvRight.setTextColor(getResources().getColor(R.color.app_style));
        tvRight.setBackgroundResource(R.drawable.title_right_bg);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(20, 20, 20, 20);
        tvRight.setLayoutParams(layoutParams);
    }

    private void initView() {

        mCbPswLogin = (CheckBox) findViewById(R.id.cb_login_pswlogin);
        mCbVerifyLogin = (CheckBox) findViewById(R.id.cb_login_verifylogin);

        mCetPhone = (ClearEditText) findViewById(R.id.cet_login_phone);

        mCetPsw = (ClearEditText) findViewById(R.id.cet_login_psw);
        mCbIspswVisible = (CheckBox) findViewById(R.id.cb_login_ispswvisible);

        mEtVerifyCode = (EditText) findViewById(R.id.et_login_verifycode);
        mBtGetVerifycode = (Button) findViewById(R.id.bt_login_getcode);

        mCbRemePsw = (CheckBox) findViewById(R.id.cb_login_remepsw);
        mTvForgetPsw = (TextView) findViewById(R.id.tv_login_forgetpsw);

        mBtLogin = (Button) findViewById(R.id.bt_login_login);

        mLlPswLoginLayout = (LinearLayout) findViewById(R.id.ll_login_psw);
        mLlVerLoginLayout = (LinearLayout) findViewById(R.id.ll_login_verifycode);
        mLlothLoginLayout = (LinearLayout) findViewById(R.id.ll_login_otherlayout);

        mBtLogin.setOnClickListener(this);
        mBtGetVerifycode.setOnClickListener(this);
        mTvForgetPsw.setOnClickListener(this);

        // 第三方登录
        mBtQQLogin = (Button) findViewById(R.id.bt_login_qq);
        mBtWechatLogin = (Button) findViewById(R.id.bt_login_wechat);
        mBtWeiboLogin = (Button) findViewById(R.id.bt_login_weibo);

        mBtQQLogin.setOnClickListener(this);
        mBtWechatLogin.setOnClickListener(this);
        mBtWeiboLogin.setOnClickListener(this);

        // 选择用户名密码登录
        mCbPswLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mCbVerifyLogin.isChecked() && !isChecked) {
                    mCbPswLogin.setChecked(true);
                    return;
                }
                if (isChecked) { // 用户名密码登录
                    mCetPhone.setHint(R.string.user_name_hind);

                    mLlVerLoginLayout.setVisibility(View.GONE);
                    mLlPswLoginLayout.setVisibility(View.VISIBLE);
                    mLlothLoginLayout.setVisibility(View.VISIBLE);

                    mCbVerifyLogin.setChecked(false);

                }
            }
        });

        // 选择验证码登录
        mCbVerifyLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mCbPswLogin.isChecked() && !isChecked) {
                    mCbVerifyLogin.setChecked(true);
                    return;
                }
                //如果不是手机号码.清空输入框信息
                if (!StringUtils.isPhone(mCetPhone.getText().toString())) {
                    mCetPhone.setText("");
                }
                if (isChecked) {

                    mCetPhone.setHint("请输入您的手机号");

                    mLlVerLoginLayout.setVisibility(View.VISIBLE);
                    mLlPswLoginLayout.setVisibility(View.GONE);
                    mLlothLoginLayout.setVisibility(View.GONE);

                    mCbPswLogin.setChecked(false);
                }
            }
        });

        // 手机号码、用户名输入监听，如果删除输入的账户，密码同步删除。还可以做其他优化，看需求。
        mCetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCetPhone.getText().toString().equals("")) {
                    mCetPsw.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mCetPhone.getText().toString().equals("")) {
                    mCetPsw.setText("");
                }
            }
        });

        //显示关闭密码
        mCbIspswVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCetPsw.setInputType(InputType.TYPE_CLASS_TEXT
                        | (isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                mCetPsw.setSelection(mCetPsw.getText().length());
            }
        });

    }

    /**
     * 从本地保存的数据中 取出用户名和密码
     */
    public void getLoginInfo() {

        LoginInfo loginInfo = (LoginInfo) PreferencesUtils.readDataObject(LoginActivity.this, ConfigConstants.LOGININFO);
        if (null != loginInfo) {
            mCetPhone.setText(loginInfo.getUsername());
            mCbRemePsw.setChecked(loginInfo.isRempsw());
            if (mCbRemePsw.isChecked()) {
                mCetPsw.setText(loginInfo.getPassword());
            }
        }
    }

    /**
     * 获取验证码信息填写验证
     */
    private void CodeCheck() {
        String mobile = mCetPhone.getText().toString();
        if (StringUtils.isNullOrEmpty(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.mobile_required);
            return;
        }
        if (!StringUtils.isPhone(mobile)) {
            ToastManager.getInstance().showToast(this, R.string.not_phone_);
            return;
        }

        getCode("3", mCetPhone.getText().toString()); // 3 验证码登录
    }


    /**
     * 用户名密码验证用户提交信息
     */
    public void CommonCheck() {
        String name = mCetPhone.getText().toString().trim();
        String passwd = mCetPsw.getText().toString().trim();

        if (StringUtils.isNullOrEmpty(name)) {
            ToastManager.getInstance().showToast(LoginActivity.this, R.string.user_name_required);
            return;
        }
        if (StringUtils.isNullOrEmpty(passwd)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_required);
            return;
        }

        String psw = Md5Utils.getMd5Str(passwd); // 密码进行MD5
        commonLogin(psw, name);
    }

    /**
     * 验证码验证用户提交信息
     */
    private void SmsCheck() {
        String name = mCetPhone.getText().toString().trim();
        String passwd = mEtVerifyCode.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(name)) {
            ToastManager.getInstance().showToast(LoginActivity.this, R.string.user_name_required);
            return;
        }
        if (StringUtils.isNullOrEmpty(passwd)) {
            ToastManager.getInstance().showToast(this, R.string.valid_code_required);
            return;
        }
        if (passwd.length() < 6) {
            ToastManager.getInstance().showToast(this, "验证码长度不正确");
            return;
        }

        SmsLogin(name, passwd, mNonce);
    }

    /**
     * 获取短信验证码
     */
    private void getCode(String type, final String recipient) {

        final TimeCount time = new TimeCount(1 * 60 * 1000, 1000);//构造CountDownTimer对象

        HttpRequestManager.getInstance().getCode(type, recipient, new ApiSubscriber<JsonResult<String>>(LoginActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    time.start();
                    mBtGetVerifycode.setEnabled(false);
                    mNonce = result.getObject();
                } else {
                    mBtGetVerifycode.setEnabled(true);
                    mBtGetVerifycode.setText(R.string.re_get_verify);
                    if (time != null) {
                        time.cancel();
                    }
                }
                ToastManager.getInstance().showToast(getApplicationContext(), result.getResultMsg());
            }

        });
    }

    /**
     * 第三方登录授权
     */
    private void doOauthVerify(final String plat) {

        mLoadingPopWindow.showLoadingDialog();

        platform = ShareSDK.getPlatform(plat);

        // 每次点击都去请求授权，每次点击可以切换账号信息
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }

        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                mLoadingPopWindow.dismissLoadingDialog();

                String userId, userIcon, userName;
                userId = platform.getDb().getUserId();
                userIcon = platform.getDb().getUserIcon();
                userName = platform.getDb().getUserName();

                if (!StringUtils.isHaveSpecial(userName)) {
                    mLocalUserName = userName;
                    userName = "ticket12308";
                }

                if (StringUtils.isNullOrEmpty(userId)) {

                    ToastManager.getInstance().showToast(LoginActivity.this, "获取用户信息失败,请重试");
                } else {
                    //ThridLogin(userId, userName, userIcon); TODO 不能在里面直接进行网络操作，后续看是什么原因
                    Message msg = mHandler.obtainMessage();

                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("userName", userName);
                    bundle.putString("userIcon", userIcon);
                    msg.setData(bundle);
                    mHandler.sendMessageDelayed(msg, 200);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                mLoadingPopWindow.dismissLoadingDialog();
                ToastManager.getInstance().showToast(LoginActivity.this, "授权错误:" + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                mLoadingPopWindow.dismissLoadingDialog();
                ToastManager.getInstance().showToast(LoginActivity.this, "授权取消");
            }
        });
        //获取用户资料
        platform.showUser(null);

    }

    /**
     * 用户名密码登录
     */
    private void commonLogin(String password, String mobilePhone) {

        HttpRequestManager.getInstance().login(password, mobilePhone, new ApiSubscriber<JsonResult<LoginResponsePara>>(LoginActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<LoginResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    saveLoginInfo();
                    dealWithLoginResponse(result.getObject());

                } else {
                    ToastManager.getInstance().showToast(LoginActivity.this, result.getResultMsg());
                }
            }
        });
    }

    /**
     * 短信登录
     */
    private void SmsLogin(String mobilePhone, String captcha, String nonce) {
        HttpRequestManager.getInstance().smsLogin(mobilePhone, captcha, nonce, new ApiSubscriber<JsonResult<LoginResponsePara>>(LoginActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<LoginResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    saveLoginInfo();
                    dealWithLoginResponse(result.getObject());
                } else {
                    ToastManager.getInstance().showToast(LoginActivity.this, result.getResultMsg());
                }
            }
        });
    }

    /**
     * 第三方登录
     */
    private void ThridLogin(String openId, String nickName, String photo) {

        String codeNickName = UrlEncodeUtils.doubleUrlEncode(nickName);

        String codePhoto = UrlEncodeUtils.doubleUrlEncode(photo);

        HttpRequestManager.getInstance().thridLogin(mThridType + "", openId, codeNickName, codePhoto, new ApiSubscriber<JsonResult<LoginResponsePara>>(LoginActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<LoginResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    dealWithLoginResponse(result.getObject());
                } else {
                    ToastManager.getInstance().showToast(LoginActivity.this, result.getResultMsg());
                }

            }
        });

    }

    /**
     * 保存登录信息
     */
    private void saveLoginInfo() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(mCetPhone.getText().toString());
        if (mCbPswLogin.isChecked() && mCbRemePsw.isChecked()) {
            loginInfo.setRempsw(true);
            loginInfo.setPassword(mCetPsw.getText().toString());
        } else {
            loginInfo.setRempsw(false);
            loginInfo.setPassword("");
        }

        PreferencesUtils.saveDataObject(LoginActivity.this, ConfigConstants.LOGININFO, loginInfo);
    }

    /**
     * 处理登录返回的用户信息
     */
    private void dealWithLoginResponse(LoginResponsePara responsePara) {
        // 保存账户信息

        PreferencesUtils.saveDataObject(LoginActivity.this, ConfigConstants.USERINFO, responsePara);

        // 发出登录成功的事件
        RxBus.getInstance().post(new CommonEvent(CommonEvent.LOGIN_SUCCESS));

        // 检查火车票账号绑定
        checkBind();

    }

    private void checkBind() {
        mTrainAccount = (TrainAccountInfo) PreferencesUtils.readDataObject(LoginActivity.this, ConfigConstants.TRAIN_LOGININFO);
        if (null != mTrainAccount && (!mTrainAccount.isHasbind())) {
            // 有12306账号信息，且该12306账号没有绑定过，则进行绑定，否则不处理。

            HttpRequestManager.getInstance().bindTrain(mTrainAccount.getAccount(), mTrainAccount.getPassword(), new ApiSubscriber<JsonResult<TrainLoginResponseModel>>(LoginActivity.this, ApiSubscriber.NETNULL) {
                @Override
                public void onNext(JsonResult<TrainLoginResponseModel> result) {
                    if ("0000".equals(result.getResultCode())) {
                        if (result.getObject().getIsPass() == 1) {
                            // 通过绑定
                            mTrainAccount.setHasbind(true);
                            mTrainAccount.setTrainId(result.getObject().getId());
                            mTrainAccount.setPass(true);
                            PreferencesUtils.saveDataObject(LoginActivity.this, ConfigConstants.TRAIN_LOGININFO, mTrainAccount);
                            RxBus.getInstance().post(new CommonEvent(CommonEvent.TRAIN_LOGINSUCCESS));
                        }
                    } else {
                        ToastManager.getInstance().showToast(LoginActivity.this, result.getResultMsg());
                    }
                    finish();
                }
            });
        } else {
            RxBus.getInstance().post(new CommonEvent(CommonEvent.TRAIN_LOGINSUCCESS));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_left:
                // 后退
                RxBus.getInstance().post(new CommonEvent(CommonEvent.LOGIN_BACK));
                InputMethod.closeInputMethod(LoginActivity.this);
                ToastManager.getInstance().cancelToast();
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                finish();
                break;
            case R.id.title_bar_right://跳转去注册
                startActivityWithAnim(RegistActivity.class);
                break;
            case R.id.bt_login_getcode:// 获取验证码
                CodeCheck();
                break;
            case R.id.bt_login_login:
                if (mCbPswLogin.isChecked()) { // 账户密码登录
                    CommonCheck();
                } else if (mCbVerifyLogin.isChecked()) { // 短信验证码登录
                    SmsCheck();
                }
                InputMethod.closeInputMethod(LoginActivity.this);
                break;

            case R.id.tv_login_forgetpsw:// 忘记密码
                startActivityWithAnim(FindPswActivity.class);
                break;
            case R.id.bt_login_qq:
                if (ShareSDK.getPlatform(this, QZone.NAME).isClientValid()) {
                    mThridType = 2;
                    doOauthVerify(QZone.NAME);
                } else {
                    ToastManager.getInstance().showToast(getApplicationContext(), R.string.not_install_qq_login);
                }

                break;
            case R.id.bt_login_wechat:
                if (ShareSDK.getPlatform(this, Wechat.NAME).isClientValid()) {
                    mThridType = 1;
                    doOauthVerify(Wechat.NAME);
                } else {
                    ToastManager.getInstance().showToast(getApplicationContext(), R.string.not_install_wx_login);
                }
                break;
            case R.id.bt_login_weibo:
                System.out.println("微博登录");
                mThridType = 3;
                doOauthVerify(SinaWeibo.NAME);
                break;


        }
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mBtGetVerifycode.setEnabled(true);
            mBtGetVerifycode.setText(R.string.re_get_verify);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            int second = (int) (millisUntilFinished / 1000) % 60;
            if (second > 0) {
                String str = String.format(getString(R.string.remain_time), second);
                mBtGetVerifycode.setText(str);
            }
        }
    }

}
