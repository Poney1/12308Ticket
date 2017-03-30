package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxws.myticket.R;
import com.wxws.myticket.app.MainActivity;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.Md5Utils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.LoginInfo;

/**
 * desc: 修改密码
 * Date: 2016-11-07 13:45
 *
 * @author jiangyan
 */
public class ModifyPswActivity extends BaseActivity {

    private EditText mEtOldPsw;
    private EditText mEtNewPsw;
    private EditText mEtConfirmPsw;
    private String mOldpsw;
    private String mNewpsw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypsw);
        initTitle();
        initView();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("修改密码");
    }

    private void initView() {
        mEtOldPsw = (EditText) findViewById(R.id.et_modifypsw_oldpsw);
        mEtNewPsw = (EditText) findViewById(R.id.et_modifypsw_newpsw);
        mEtConfirmPsw = (EditText) findViewById(R.id.et_modifypsw_confirmpsw);
        Button btSubmit = (Button) findViewById(R.id.bt_modifypsw_submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validPost()) {
                    modifyPsw();
                }
            }
        });
    }

    private boolean validPost() {
        mOldpsw = mEtOldPsw.getText().toString();
        if (StringUtils.isNullOrEmpty(mOldpsw)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_old_required);
            return false;
        }
        mNewpsw = mEtNewPsw.getText().toString();
        if (StringUtils.isNullOrEmpty(mNewpsw)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_new_required);
            return false;
        }
        if (!StringUtils.requireLength(mNewpsw, 6, 20)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_format_error);
            return false;
        }
        String repwd = mEtConfirmPsw.getText().toString();
        if (StringUtils.isNullOrEmpty(repwd)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_new_required);
            return false;
        }
        if (!mNewpsw.equals(repwd)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_not_equal);
            return false;
        }
        if (mNewpsw.equals(mOldpsw)) {
            ToastManager.getInstance().showToast(this, R.string.pwd_not_equal_old);
            return false;
        }
        return true;
    }

    private void modifyPsw() {

        String md5old = Md5Utils.getMd5Str(mOldpsw);
        String md5new = Md5Utils.getMd5Str(mNewpsw);

        HttpRequestManager.getInstance().modifyPsw(md5old, md5new, new ApiSubscriber<JsonResult<String>>(ModifyPswActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {

                if ("0000".equals(result.getResultCode())) {
                    LoginInfo loginInfo = (LoginInfo) PreferencesUtils.readDataObject(ModifyPswActivity.this, ConfigConstants.LOGININFO);
                    loginInfo.setPassword("");
                    PreferencesUtils.saveDataObject(ModifyPswActivity.this, ConfigConstants.LOGININFO, loginInfo);
                    PreferencesUtils.saveDataObject(ModifyPswActivity.this, ConfigConstants.USERINFO, null);
                    PreferencesUtils.putDataString(ModifyPswActivity.this, ConfigConstants.AUTHTOKEN, null);

                    RxBus.getInstance().post(new CommonEvent(CommonEvent.UPDATE_PSW));
                    startActivityWithAnim(MainActivity.class);

                } else {
                    ToastManager.getInstance().showToast(ModifyPswActivity.this, result.getResultMsg());
                }

            }
        });

    }
}