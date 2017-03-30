package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;

/**
 * desc:
 * Date: 2016-11-24 17:45
 *
 * @author jiangyan
 */
public class WalletRechargeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletrecharge);
        initTitle();
        initView();
    }

    private void initTitle(){
        bindTitleViews();
        barTitle.setText("充值");
        flTitleBarRight.setVisibility(View.VISIBLE);
        tvRight.setText("充值说明");


    }

    private void initView(){

        EditText etIncomeMoney = (EditText) findViewById(R.id.et_walletrecharge_incomemoney);
        Button btIncome = (Button) findViewById(R.id.bt_walletrecharge_income);
    }
}