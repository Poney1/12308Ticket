package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.ToastManager;

/**
 * desc: 意见反馈页面
 * Date: 2016-10-31 14:23
 *
 * @author jiangyan
 */
public class FeedbackActivity extends BaseActivity {

    private int mType = 0;//0 建议，1 投诉
    private RadioButton mRbSuggest;
    private RadioButton mRbComplaint;
    private EditText mEtDeatils;
    private TextView mTvNum;
    private RadioGroup mRgFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initTitle();
        initView();
        initStatus();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("意见反馈");
    }

    private void initView() {
        mRgFeedback = (RadioGroup) findViewById(R.id.rg_feedback);
        mRbSuggest = (RadioButton) findViewById(R.id.rb_feedback_suggest);
        mRbComplaint = (RadioButton) findViewById(R.id.rb_feedback_complaint);

        mEtDeatils = (EditText) findViewById(R.id.et_feedback_details);
        mTvNum = (TextView) findViewById(R.id.tv_feedback_num);

        Button btSubmit = (Button) findViewById(R.id.bt_feedback_submit);

        mRgFeedback.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_feedback_suggest) {
                    mType = 0;
                } else {
                    mType = 1;
                }

                initStatus();
            }
        });

        mEtDeatils.addTextChangedListener(new TextWatcher() {
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
                    ToastManager.getInstance().showToast(FeedbackActivity.this, "最多输入200个字符");
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

    private void initStatus() {
        if (mType == 0) {
            mRbSuggest.setChecked(true);
            mRbComplaint.setChecked(false);
        } else {
            mRbSuggest.setChecked(false);
            mRbComplaint.setChecked(true);
        }

    }

    private void submitToServer() {
        if (null == mEtDeatils.getText().toString() || "".equals(mEtDeatils.getText().toString())) {
            ToastManager.getInstance().showToast(FeedbackActivity.this, "请输入反馈内容！");
            return;
        }
        HttpRequestManager.getInstance().feedback(mType + "", mEtDeatils.getText().toString(), new ApiSubscriber<JsonResult<String>>(FeedbackActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if (result.getResultCode().equals("0000")) {
                    FeedbackActivity.this.finish();
                }

                ToastManager.getInstance().showToast(FeedbackActivity.this, result.getResultMsg());
            }
        });


    }
}