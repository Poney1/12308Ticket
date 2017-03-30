package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.Base64Utils;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.RSAUtils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.TrainAccountInfo;
import com.wxws.myticket.my.Entity.TrainLoginResponseModel;
import com.wxws.myticket.my.widgets.view.ClearEditText;


/**
 * desc:
 * Date: 2016-08-18 10:39
 *
 * @author jiangyan
 */
public class TrainLoginActivity extends BaseActivity {

    private ClearEditText cet_account;
    private ClearEditText cet_psw;
    private CheckBox cb_isvisible;
    private Button bt_login;
    private CheckBox cb_agree;
    private TrainAccountInfo mTrainAccountInfo;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trainlogin);
        bindTitleViews();
        barTitle.setText("登录12306");
        initView();
        mTrainAccountInfo = (TrainAccountInfo) PreferencesUtils.readDataObject(TrainLoginActivity.this, ConfigConstants.TRAIN_LOGININFO);
        if (null != mTrainAccountInfo) {
            initData();
        }
    }

    private void initView() {

        cet_account = (ClearEditText) findViewById(R.id.cet_trainlogin_account);
        cet_psw = (ClearEditText) findViewById(R.id.cet_trainlogin_psw);
        cb_isvisible = (CheckBox) findViewById(R.id.cb_trainlogin_isvisible);
        bt_login = (Button) findViewById(R.id.bt_trainlogin_login);
        cb_agree = (CheckBox) findViewById(R.id.cb_trainlogin_agree);
        TextView tv_protocal = (TextView) findViewById(R.id.tv_trainlogin_protocal);

        tv_protocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 跳转给定的URL，由H5提供
//                Intent intent = new Intent(TrainLoginActivity.this, Html5Activity.class);
//                intent.putExtra("URL", Constants.HCPTGDGXY);
//                startActivityWithAnim(intent);
            }
        });

        cet_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cet_account.getText().toString().equals("")) {
                    cet_psw.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cet_account.getText().toString().equals("")) {
                    cet_psw.setText("");
                }
            }
        });

        //显示关闭密码
        cb_isvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cet_psw.setInputType(InputType.TYPE_CLASS_TEXT
                        | (isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                cet_psw.setSelection(cet_psw.getText().length());
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkdata()) {
                    loginTrain();
                }
            }
        });
    }

    private void initData() {
        cet_account.setText(mTrainAccountInfo.getAccount());
    }

    private boolean checkdata() {

        if (StringUtils.isNullOrEmpty(cet_account)) {
            ToastManager.getInstance().showToast(TrainLoginActivity.this, "请输入12306账号");
            return false;
        }

        if (StringUtils.isNullOrEmpty(cet_psw)) {
            ToastManager.getInstance().showToast(TrainLoginActivity.this, "请输入密码");
            return false;
        }

        if (!cb_agree.isChecked()) {
            ToastManager.getInstance().showToast(TrainLoginActivity.this, "请阅读并同意《12306托管代购协议》");
            return false;
        }
        return true;
    }

    private void loginTrain() {
        mLoadingPopWindow.showLoadingDialog();
        InputMethod.closeInputMethod(TrainLoginActivity.this);

        String psw = cet_psw.getText().toString();
        byte[] encryptByte = RSAUtils.encrypt(psw.getBytes());
        String dddPsw = Base64Utils.base64EncodeUrlSafe(encryptByte);

        HttpRequestManager.getInstance().bindTrain(cet_account.getText().toString(), dddPsw, new ApiSubscriber<JsonResult<TrainLoginResponseModel>>(TrainLoginActivity.this) {
            @Override
            public void onNext(JsonResult<TrainLoginResponseModel> result) {
                mLoadingPopWindow.dismiss();
                if ("0000".equals(result.getResultCode())) {

                    if (result.getObject().getIsPass() == 1) {
                        // 12306密码账号正确
                        TrainAccountInfo trainAccountInfo = new TrainAccountInfo();
                        trainAccountInfo.setTrainId(result.getObject().getId());
                        trainAccountInfo.setAccount(result.getObject().getTrainAccount());
                        trainAccountInfo.setPassword(cet_psw.getText().toString());
                        if (result.getObject().getIsPass() == 1) {
                            trainAccountInfo.setPass(true);
                        } else {
                            trainAccountInfo.setPass(false);
                        }

                        if (checkLogin()) {
                            // 说明已经绑定
                            trainAccountInfo.setHasbind(true);
                        } else {
                            // 说明暂时没有绑定
                            trainAccountInfo.setHasbind(false);
                        }
                        PreferencesUtils.saveDataObject(TrainLoginActivity.this, ConfigConstants.TRAIN_LOGININFO, trainAccountInfo);
                        RxBus.getInstance().post(new CommonEvent(CommonEvent.TRAIN_LOGINSUCCESS));
                        finish();
                    } else {
                        // 12306账号密码错误
                        ToastManager.getInstance().showToast(TrainLoginActivity.this, "账号名或密码错误，请确认后重试！");
                    }
                } else {
                    ToastManager.getInstance().showToast(TrainLoginActivity.this, result.getResultMsg());
                }
            }
        });

    }


}