package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.my.Entity.MessageListResponsePara;
import com.wxws.myticket.my.adapter.MessageAdapter;

import java.util.List;

/**
 * desc: 消息列表
 * Date: 2016-11-07 10:29
 *
 * @author jiangyan
 */
public class MessageActivity extends BaseActivity {

    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initTitle();
        initView();
        getData();
    }

    private void initTitle() {
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("消息");
    }

    private void initView() {
        ListView lvMessageList = (ListView) findViewById(R.id.lv_message_list);

        mMessageAdapter = new MessageAdapter(MessageActivity.this);
        lvMessageList.setAdapter(mMessageAdapter);

        lvMessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 跳转到H5页面
                Intent intent = new Intent(MessageActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", "http://www.baidu.com");
                startActivityWithAnim(intent);
            }
        });
    }

    private void getData() {
        showLoading();
        HttpRequestManager.getInstance().getMessageList(new ApiSubscriber<JsonResult<List<MessageListResponsePara>>>(MessageActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<List<MessageListResponsePara>> result) {
                if ("0000".equals(result.getResultCode())) {
                    dismissLoading();
                    if(result.getObject().size() != 0 || result.getObject() != null){
                        mMessageAdapter.refreshData(result.getObject());
                    }else{
                        showErrorOrNoData("", R.mipmap.base_message_order_null);
                    }

                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network, true);
                }
            }
        });
    }

    @Override
    public void clickReload() {
        super.clickReload();
        getData();
    }
}