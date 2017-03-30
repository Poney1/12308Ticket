package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.my.Entity.CouponResponsePara;
import com.wxws.myticket.my.adapter.CouponAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 优惠券选择、使用界面
 * Date: 2016-12-16 16:24
 *
 * @author jiangyan
 */
public class CouponSelectActivity extends BaseActivity {

    private String mAmount;
    private String mBizType;
    private CouponAdapter mAdapter;
    private List<CouponResponsePara> mCouponResponseParaList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couponselect);
        getDataFromPre();
        initTitle();
        initView();
        getCouponeList();
    }

    private void getDataFromPre() {
        mAmount = getIntent().getStringExtra("orderPrice");
        mBizType = getIntent().getStringExtra("bizType");
        Log.e("mudo", "价格" + mAmount);
        Log.e("mudo", "类型" + mBizType);
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("优惠券");
        bindLoadingView();
    }

    private void initView() {

        ListView lvCouponList = (ListView) findViewById(R.id.lv_couponselect_list);
        mAdapter = new CouponAdapter(CouponSelectActivity.this);
        lvCouponList.setAdapter(mAdapter);

        Button btUnUse = (Button) findViewById(R.id.bt_couponselect_unselect);
        btUnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 不选择优惠券返回
            }
        });

        lvCouponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO 选择优惠券返回
                mCouponResponseParaList.get(position).getCode();


            }
        });
    }

    private void getCouponeList() {

        showLoading();
        HttpRequestManager.getInstance().getUsableCoupon(mAmount, mBizType, new ApiSubscriber<JsonResult<List<CouponResponsePara>>>(CouponSelectActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<List<CouponResponsePara>> result) {
                if ("0000".equals(result.getResultCode())) {

                    if (result.getObject() != null && result.getObject().size() > 0) {
                        dismissLoading();
                        mCouponResponseParaList.clear();
                        mCouponResponseParaList.addAll(result.getObject());
                        initData();
                    } else {
                        showErrorOrNoData("", R.mipmap.base_coupon_null);
                    }

                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network, true);
                }
            }
        });
    }

    private void initData() {
        mAdapter.refreshData(mCouponResponseParaList);
    }
}