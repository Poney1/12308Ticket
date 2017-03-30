package com.wxws.myticket.order.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;

/**
 * desc: 我要包车订单详情
 * TODO 出发到达城市，车辆信息返回缺失，已反馈后台。
 * Date: 2016-11-01 18:41
 *
 * @author jiangyan
 */
public class BaocheOrderdetailsActivity extends BaseActivity {

    TextView mTvOrderNo;
    TextView mTvTotalPay;
    TextView mTvBecity;
    TextView mTvEncity;
    TextView mTvStartTime;
    TextView mTvEndTime;
    TextView mTvStartPlace;
    TextView mTvEndPlace;
    TextView mTvCarType;
    TextView mTvPassengerAcount;
    TextView mTvInvoiceDetail;
    TextView mTvConsigneeName;
    TextView mTvConsigneeAddress;
    LinearLayout mLlInvoiceLayout;

    String mBizType;
    String mOrderId;
    OrderdetailsResponsePara mOrderDetails;
    private LinearLayout mLlGobackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baocheorderdetails);
        initCommonView();
        getDataFromPre();
        initView();
        getData();
    }

    private void initCommonView() {
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("订单详情");
    }

    private void getDataFromPre() {
        mBizType = getIntent().getStringExtra("bizType");
        mOrderId = getIntent().getStringExtra("orderId");
    }

    private void initView() {
        mTvOrderNo = (TextView) findViewById(R.id.tv_bcorderdetails_orderno);
        mTvTotalPay = (TextView) findViewById(R.id.tv_bcorder_totalpay);
        mTvBecity = (TextView) findViewById(R.id.tv_becity_bcorder);
        mTvEncity = (TextView) findViewById(R.id.tv_encity_bcorder);
        mTvStartTime = (TextView) findViewById(R.id.tv_bcorder_starttime);
        mLlGobackLayout = (LinearLayout) findViewById(R.id.ll_bcorder_gobacklayout);
        mTvEndTime = (TextView) findViewById(R.id.tv_bcorder_endtime);

        mTvStartPlace = (TextView) findViewById(R.id.tv_bcorder_startplace);
        mTvEndPlace = (TextView) findViewById(R.id.tv_bcorder_endplace);
        mTvCarType = (TextView) findViewById(R.id.tv_bcorder_cartype);
        mTvPassengerAcount = (TextView) findViewById(R.id.tv_bcorder_passengernum);

        mLlInvoiceLayout = (LinearLayout) findViewById(R.id.ll_bcorder_invoicelayout);
        mTvInvoiceDetail = (TextView) findViewById(R.id.tv_bcorder_invoicedetail);
        mTvConsigneeName = (TextView) findViewById(R.id.tv_bcorder_consigneename);
        mTvConsigneeAddress = (TextView) findViewById(R.id.tv_bcorder_consigneeaddress);

    }

    private void getData() {
        HttpRequestManager.getInstance().getOrderdetails(mOrderId, mBizType + "", new ApiSubscriber<JsonResult<OrderdetailsResponsePara>>(BaocheOrderdetailsActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<OrderdetailsResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    mOrderDetails = result.getObject();
                    initData();
                } else {
                    showErrorOrNoData(getString(R.string.net_abnormal_error), R.string.net_refresh_again, R.mipmap.base_not_network);
                }
            }
        });
    }

    private void initData() {

        mTvOrderNo.setText(mOrderDetails.getOrderId());
        mTvTotalPay.setText(ArithUtils.mul(mOrderDetails.getOrderMoney(), 0.01) + "");
        mTvBecity.setText(mOrderDetails.getFromCityName());
        mTvEncity.setText(mOrderDetails.getToCityName());
        mTvStartTime.setText(mOrderDetails.getDepartDate());

        if ("1".equals(mOrderDetails.getIsGoBack())) {
            mTvEndTime.setText(mOrderDetails.getGoBackTime());
            mLlGobackLayout.setVisibility(View.VISIBLE);
        } else {
            mLlGobackLayout.setVisibility(View.GONE);
        }

        mTvStartPlace.setText(mOrderDetails.getFromStationName());
        mTvEndPlace.setText(mOrderDetails.getToStationName());
        mTvCarType.setText(mOrderDetails.getVehicleType());
        mTvPassengerAcount.setText(String.valueOf(mOrderDetails.getCount()));

        if (null != mOrderDetails.getInvoiceTitle() || null != mOrderDetails.getRecipientsName() || null != mOrderDetails.getDeliveryAddress()) {
            mLlInvoiceLayout.setVisibility(View.VISIBLE);
            mTvInvoiceDetail.setText(mOrderDetails.getInvoiceTitle());
            mTvConsigneeName.setText(mOrderDetails.getRecipientsName());
            mTvConsigneeAddress.setText(mOrderDetails.getDeliveryAddress());

        } else {
            mLlInvoiceLayout.setVisibility(View.GONE);
        }


    }
}