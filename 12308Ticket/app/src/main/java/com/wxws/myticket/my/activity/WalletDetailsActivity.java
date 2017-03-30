package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.my.Entity.WalletTypeEvent;
import com.wxws.myticket.my.adapter.WalletDetailsAdapter;
import com.wxws.myticket.my.widgets.popwin.WalletDetailsPopwindow;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * Date: 2016-11-24 13:47
 *
 * @author jiangyan
 */
public class WalletDetailsActivity extends BaseActivity {

    private WalletDetailsAdapter mAdapter;
    private ImageView mIvArrow;
    private WalletDetailsPopwindow mTypePop;

    private int defType = 1;
    private static final String TAG = "WalletDetailsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletdetails);
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
        ImageView ivBack = (ImageView) findViewById(R.id.iv_walletdetails_back);
        LinearLayout llShaixuan = (LinearLayout) findViewById(R.id.ll_walletdetails_shaixuan);
        mIvArrow = (ImageView) findViewById(R.id.iv_walletdetails_arrow);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletDetailsActivity.this.finish();
            }
        });

        llShaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 点击出来筛选框
                mIvArrow.setBackgroundResource(R.mipmap.icon_orderlist_shaixuan_up);

                mTypePop.showAtBottom(findViewById(R.id.rl_walletdetails_container));
            }
        });

        if (mTypePop == null) {
            mTypePop = new WalletDetailsPopwindow(WalletDetailsActivity.this, defType);
        }
        mTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mIvArrow.setBackgroundResource(R.mipmap.icon_orderlist_shaixuan_down);
            }
        });

    }

    private void initView() {
        ListView lvDetails = (ListView) findViewById(R.id.lv_walletdetails_list);
        mAdapter = new WalletDetailsAdapter(this);
        lvDetails.setAdapter(mAdapter);

        lvDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 点击跳转到详细页面
                startActivityWithAnim(WalletIncomeDetailsActivity.class);
            }
        });
    }

    private void getData() {
        //TODO 网络获取数据
        initData();
    }

    private void initData() {

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        mAdapter.refreshData(list);
    }

    @Subscribe
    public void updateListType(WalletTypeEvent event) {
        defType = event.getType();
        updateListView();

    }

    private void updateListView() {

        Log.d(TAG, "updateListType: " + defType);

        switch (defType) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
    }
}