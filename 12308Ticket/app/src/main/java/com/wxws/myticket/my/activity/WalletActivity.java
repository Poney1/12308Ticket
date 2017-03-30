package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;

/**
 * desc: 我的钱包
 * Date: 2016-11-24 10:18
 *
 * @author jiangyan
 */
public class WalletActivity extends BaseActivity {

    private TextView mTvBalance;
    private TextView mTvRechargeMoney;
    private TextView mTvGiftMoney;
    private View mFreezeLine;
    private LinearLayout mLlFreeze;
    private TextView mTvFreezeMoney;
    private boolean hasFreeze = true;
    private LinearLayout mLlTips;
    private TextView mTvTips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initTitle();
        initView();
        getData();
    }

    private void initTitle() {
        ImageView ivBack = (ImageView) findViewById(R.id.iv_wallet_back);
        ImageView ivQuestion = (ImageView) findViewById(R.id.iv_wallet_question);
        ImageView ivDetails = (ImageView) findViewById(R.id.iv_wallet_details);

        ivBack.setOnClickListener(this);
        ivQuestion.setOnClickListener(this);
        ivDetails.setOnClickListener(this);
    }

    private void initView() {

        mTvBalance = (TextView) findViewById(R.id.tv_wallet_balance);
        mTvRechargeMoney = (TextView) findViewById(R.id.tv_wallet_rechargemoney);
        mTvGiftMoney = (TextView) findViewById(R.id.tv_wallet_giftmoney);

        mFreezeLine = findViewById(R.id.view_line);
        mLlFreeze = (LinearLayout) findViewById(R.id.ll_wallet_freeze);
        mTvFreezeMoney = (TextView) findViewById(R.id.tv_wallet_freezemoney);

        mLlTips = (LinearLayout) findViewById(R.id.ll_wallet_tips);
        mTvTips = (TextView) findViewById(R.id.tv_wallet_tips);

        Button btRecharge = (Button) findViewById(R.id.bt_wallet_recharge);
        btRecharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_wallet_back:
                finish();
                break;
            case R.id.iv_wallet_question:// TODO 余额问题

                break;
            case R.id.iv_wallet_details:// TODO 明细
                startActivityWithAnim(WalletDetailsActivity.class);
                break;
            case R.id.bt_wallet_recharge://TODO 点击充值
                startActivityWithAnim(WalletRechargeActivity.class);
                break;
        }
    }

    private void getData() {
        //TODO 网络获取钱包信息
        initData();
    }

    private void initData() {
        mTvBalance.setText("100");
        mTvRechargeMoney.setText("100");
        mTvGiftMoney.setText("100");
        if (hasFreeze) {
            mFreezeLine.setVisibility(View.VISIBLE);
            mLlFreeze.setVisibility(View.VISIBLE);
            mTvFreezeMoney.setText("100");

            mLlTips.setVisibility(View.VISIBLE);
        } else {
            mFreezeLine.setVisibility(View.GONE);
            mLlFreeze.setVisibility(View.GONE);

            mLlTips.setVisibility(View.GONE);
        }
    }
}