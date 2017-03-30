package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.UrlEncodeUtils;
import com.wxws.myticket.my.Entity.CommonEvent;

import static com.wxws.myticket.my.Entity.CommonEvent.UPDATE_USERINFO;

/**
 * desc:
 * Date: 2016-11-07 18:17
 *
 * @author jiangyan
 */
public class UpdateNameActivity extends BaseActivity {

    private int mType;// 0 为姓名，1 为身份证号
    private TextView mTvTextinfo;
    private String mInputText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatename);
        getDataFromPre();
        initTitle();
        initView();
    }

    private void getDataFromPre() {
        mType = getIntent().getIntExtra("type", 0);
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("修改用户名");
    }

    private void initView() {

        mTvTextinfo = (TextView) findViewById(R.id.tv_updatename_textinfo);
        TextView tvTips = (TextView) findViewById(R.id.tv_updatename_tips);
        Button btSubmit = (Button) findViewById(R.id.bt_updatename_submit);

        if (mType == 0) {// 修改姓名
            mTvTextinfo.setHint(R.string.account_input_name);
            tvTips.setText(R.string.account_count_ten);
        } else {// 修改身份证号码
            mTvTextinfo.setHint(R.string.account_input_certificate);
            tvTips.setText(R.string.account_count_eighth);
        }

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToServer();
            }
        });
    }

    private void updateToServer() {
        mInputText = mTvTextinfo.getText().toString();

        if (mType == 0) {
            updateName();
        } else {
            updateIDcardNo();
        }
    }

    private void updateName() {
        if (StringUtils.isNullOrEmpty(mInputText)) {
            ToastManager.getInstance().showToast(this, getString(R.string.account_null));
            return;
        } else if (!StringUtils.isHaveChinaLength(mInputText, 10)) {
            ToastManager.getInstance().showToast(this, getString(R.string.account_count_ten));
            return;
        }

        String codeName = UrlEncodeUtils.doubleUrlEncode(mInputText);
        HttpRequestManager.getInstance().updateName(mInputText, new ApiSubscriber<JsonResult<String>>(UpdateNameActivity.this) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    RxBus.getInstance().post(new CommonEvent(UPDATE_USERINFO));
                    finish();
                }
                ToastManager.getInstance().showToast(UpdateNameActivity.this, result.getResultMsg());
            }
        });

    }

    private void updateIDcardNo() {//01第二代身份证；02第一代身份证；03护照；04港澳通行证；05台湾通行证
        if (StringUtils.isNullOrEmpty(mInputText)) {
            ToastManager.getInstance().showToast(this, getString(R.string.account_null));
            return;
        } else if (!StringUtils.isNumberLength(mInputText, 18)) {
            ToastManager.getInstance().showToast(this, getString(R.string.account_count_eighth));
            return;
        }

        HttpRequestManager.getInstance().updateIDCard("01", mInputText, new ApiSubscriber<JsonResult<String>>(UpdateNameActivity.this) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    RxBus.getInstance().post(new CommonEvent(UPDATE_USERINFO));
                    finish();
                }
                ToastManager.getInstance().showToast(UpdateNameActivity.this, result.getResultMsg());
            }
        });

    }
}