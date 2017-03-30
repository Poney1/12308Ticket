package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.my.Entity.BindListResponsePara;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.adapter.ThridBindAdapter;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-07 17:37
 *
 * @author jiangyan
 */
public class ThridBindActivity extends BaseActivity {

    private ThridBindAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thridbind);
        RxBus.getInstance().register(this);

        initTitle();
        initView();

        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void initTitle() {
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("账号绑定");
    }

    private void initView() {
        ListView lvThridBind = (ListView) findViewById(R.id.lv_thridbind);
        mAdapter = new ThridBindAdapter(ThridBindActivity.this);
        lvThridBind.setAdapter(mAdapter);
    }

    private void getData() {
        showLoading();
        HttpRequestManager.getInstance().getBindList(new ApiSubscriber<JsonResult<List<BindListResponsePara>>>(ThridBindActivity.this,ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<List<BindListResponsePara>> result) {
                if ("0000".equals(result.getResultCode())) {
                    dismissLoading();
                    mAdapter.refreshData(result.getObject());
                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network,true);
                }

            }
        });
    }

    @Subscribe
    public void refreshData(CommonEvent event) {
        if (event.getFlag() == CommonEvent.THRIDBIND_REFRESH) {
            getData();
        }
    }

    @Override
    public void clickReload() {
        super.clickReload();
        getData();
    }
}