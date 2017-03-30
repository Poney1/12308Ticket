package com.wxws.myticket.train.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.interfaces.PayCallBackListener;
import com.wxws.myticket.pay.PayMethod;

/**
 * desc: 火车票支付
 * Date: 2016/10/20 18:25
 *
 * @auther: lixiangxiang
 */
public class TrainPayActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_pay);

        findViewById(R.id.tv_test_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayMethod.getDefault().getPayParam(TrainPayActivity.this, "xx","10", "xx", "xx", new PayCallBackListener() {
                    @Override
                    public void paySuccess() {

                    }

                    @Override
                    public void payFail() {

                    }
                });
            }
        });
    }
}
