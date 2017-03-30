package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;

/**
 * desc:
 * Date: 2016-11-24 14:45
 *
 * @author jiangyan
 */
public class WalletIncomeDetailsActivity extends BaseActivity {

    private TextView mTvId;
    private TextView mTvType;
    private TextView mTvMoney;
    private TextView mTvPayway;
    private TextView mTvTime;
    private TextView mTvBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletincomedetails);

        initTitle();
        initView();
        getData();
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("收支详情");
    }

    private void initView() {

        mTvId = (TextView) findViewById(R.id.tv_incomedetails_incomeid);
        mTvType = (TextView) findViewById(R.id.tv_incomedetails_type);
        mTvMoney = (TextView) findViewById(R.id.tv_incomedetails_incomemoney);
        mTvPayway = (TextView) findViewById(R.id.tv_incomedetails_incomeway);
        mTvTime = (TextView) findViewById(R.id.tv_incomedetails_time);
        mTvBalance = (TextView) findViewById(R.id.tv_incomedetails_balance);

    }

    private void getData() {
        // TODO 网络获取数据
        initData();
    }

    private void initData() {
        mTvId.setText("321456789");
        mTvType.setText("充值");
        mTvMoney.setText("¥110.0");
        mTvPayway.setText("工商银行");
        mTvTime.setText("2016-10-30 14:33:40");
        mTvBalance.setText("¥110.0");
    }
}