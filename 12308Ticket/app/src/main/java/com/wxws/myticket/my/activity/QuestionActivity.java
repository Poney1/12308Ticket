package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.H5UrlConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.QuestionResponsePara;
import com.wxws.myticket.my.adapter.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * Date: 2016-10-31 14:45
 *
 * @author jiangyan
 */
public class QuestionActivity extends BaseActivity {

    private QuestionAdapter mAdapter;
    private List<QuestionResponsePara> mQuestionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        RxBus.getInstance().register(this);
        initTitle();
        initView();
        if (checkLogin()) {
            getData();
        } else {
            showErrorOrNoData("您尚未登录，请先登录", R.string.go_login, R.mipmap.tip1, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void initTitle() {
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("我的提问");
    }

    private void initView() {
        ListView lvQuestions = (ListView) findViewById(R.id.lv_question_list);
        Button btToAsk = (Button) findViewById(R.id.bt_question_toask);
        mAdapter = new QuestionAdapter(QuestionActivity.this);
        lvQuestions.setAdapter(mAdapter);
        lvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuestionActivity.this, H5CommonActivity.class);
                Log.e("mudo", "跳转的url " + H5UrlConstants.QUESTION_DETAILS + mQuestionList.get(position).getId());
                intent.putExtra("URL", H5UrlConstants.QUESTION_DETAILS + mQuestionList.get(position).getId());// TODO H5页面显示问题详情+questionId
                startActivityWithAnim(intent);
            }
        });

        btToAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithAnim(ToAskActivity.class);
            }
        });


    }

    private void getData() {
        showLoading();
        HttpRequestManager.getInstance().getQuestionList(new ApiSubscriber<JsonResult<List<QuestionResponsePara>>>(QuestionActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<List<QuestionResponsePara>> result) {
                if ("0000".equals(result.getResultCode())) {
                    dismissLoading();
                    if (result.getObject() != null) {//TODO 处理
                        mQuestionList.clear();
                        mQuestionList.addAll(result.getObject());

                        mAdapter.refreshData(result.getObject());

                    } else {

                        showErrorOrNoData("暂无提问信息", R.mipmap.base_message_order_null);

                    }
                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network, true);
                }


            }
        });
    }

    @Subscribe
    public void refreshData(CommonEvent event) {
        if (event.getFlag() == CommonEvent.QUESTION_REFRESH) {
            getData();
        }
    }

    @Override
    public void clickReload() {
        super.clickReload();
        getData();
    }
}