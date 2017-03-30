package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.CommonEvent;

/**
 * desc: 我要提问
 * Date: 2016-11-07 18:48
 *
 * @author jiangyan
 */
public class ToAskActivity extends BaseActivity {

    private EditText mEtDetails;
    private TextView mTvNum;
    private EditText mEtQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toask);
        initTitle();
        initView();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("我要提问");
    }

    private void initView() {
        mEtQuestion = (EditText) findViewById(R.id.et_question_question);
        mEtDetails = (EditText) findViewById(R.id.et_toask_details);
        mTvNum = (TextView) findViewById(R.id.tv_toask_num);
        Button btSubmit = (Button) findViewById(R.id.bt_toask_submit);

        mEtDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 200) {
                    s.delete(200, s.length());
                    ToastManager.getInstance().showToast(ToAskActivity.this, "最多输入200个字符");
                }

                mTvNum.setText(s.length() + "/200");
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitToServer();
            }
        });
    }

    private void submitToServer() {

        if (mEtQuestion.getText().toString() == null || mEtQuestion.getText().toString().equals("")) {
            ToastManager.getInstance().showToast(getApplicationContext(), "请填写问题概述");
            return;
        }

        if (mEtDetails.getText().toString() == null || mEtDetails.getText().toString().equals("")) {
            ToastManager.getInstance().showToast(getApplicationContext(), "请填写问题详情");
            return;
        }

        HttpRequestManager.getInstance().toAsk(mEtQuestion.getText().toString(), mEtDetails.getText().toString(), new ApiSubscriber<JsonResult<String>>(ToAskActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    ToAskActivity.this.finish();
                    RxBus.getInstance().post(new CommonEvent(CommonEvent.QUESTION_REFRESH));
                    finish();
                }
                ToastManager.getInstance().showToast(ToAskActivity.this, result.getResultMsg());
            }
        });
    }
}