package com.wxws.myticket.order.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;

/**
 * desc: 约租车订单详情
 * Date: 2016-11-01 18:41
 *
 * @author jiangyan
 */
public class RentcarOrderDetailsActivity extends BaseActivity {

    private SimpleDraweeView mSdvIcon;
    private TextView mTvFromCity;
    private TextView mTvToCity;
    private ImageView mIvCall;
    private TextView mTvDate;
    private TextView mTvTime;
    private TextView mTvFromStation;
    private TextView mTvToStation;
    private TextView mTvPrice;
    private TextView mTvCarType;

    private TextView mTvLineInfo;
    private TextView mTvZFSM;
    private TextView mTvFWBZ;
    private TextView mTvQTSM;
    private ImageView mIvDown;
    private LinearLayout mLlLineInfoIsVisible;

    private LinearLayout mLlTime;
    private TextView mTvHour;
    private TextView mTvMinute;
    private TextView mTvSecond;
    private ImageView mIvWait;
    private TextView mTvCancel;

    private LinearLayout mLlRefund;
    private TextView mTvRefundMoney;
    private TextView mTvRefundOrderId;
    private TextView mTvRefundDate;
    private TextView mTvRefundType;
    private TextView mTvRefundRate;
    private TextView mTvRefundStatus;

    private Button mBtBottom;

    private String mBizType;
    private String mOrderId;
    private OrderdetailsResponsePara mOrderDetails;
    private boolean flag_visible = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentcarorderdetails);
        initCommonView();
        getDataFromPre();
        initView();
        getData();
    }

    private void initCommonView() {
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("订单状态");
    }

    private void getDataFromPre() {
        mBizType = getIntent().getStringExtra("bizType");//TODO
        mOrderId = getIntent().getStringExtra("orderId");
    }

    private void initView() {
        // 车次信息
        mSdvIcon = (SimpleDraweeView) findViewById(R.id.sdv_rentcarorderdetails_icon);
        mTvFromCity = (TextView) findViewById(R.id.tv_rentcarorderdetails_fromcity);
        mTvToCity = (TextView) findViewById(R.id.tv_rentcarorderdetails_tocity);
        mIvCall = (ImageView) findViewById(R.id.iv_rentcarorderdetails_callbus);
        mTvDate = (TextView) findViewById(R.id.tv_rentcarorderdetails_date);
        mTvTime = (TextView) findViewById(R.id.tv_rentcarorderdetails_time);
        mTvFromStation = (TextView) findViewById(R.id.tv_rentcarorderdetails_fromstation);
        mTvToStation = (TextView) findViewById(R.id.tv_rentcarorderdetails_tostation);
        mTvPrice = (TextView) findViewById(R.id.tv_rentcarorderdetails_money);
        mTvCarType = (TextView) findViewById(R.id.tv_rentcarorderdetails_cartype);

        // 资费声明等信息
        mTvLineInfo = (TextView) findViewById(R.id.tv_rentcarorderdetails_lineinfo);
        mTvZFSM = (TextView) findViewById(R.id.tv_rentcarorderdetails_zfsm);
        mTvFWBZ = (TextView) findViewById(R.id.tv_rentcarorderdetails_fwbz);
        mTvQTSM = (TextView) findViewById(R.id.tv_rentcarorderdetails_qtsm);
        mIvDown = (ImageView) findViewById(R.id.iv_rentcarorderdetails_down);
        mLlLineInfoIsVisible = (LinearLayout) findViewById(R.id.ll_rentcarorderdetails_lineinfovisible);

        // 中间显示状态、取消、等待等
        mLlTime = (LinearLayout) findViewById(R.id.ll_rentcarorderdetails_time);
        mTvHour = (TextView) findViewById(R.id.tv_rentcarorderdetails_hour);
        mTvMinute = (TextView) findViewById(R.id.tv_rentcarorderdetails_minute);
        mTvSecond = (TextView) findViewById(R.id.tv_rentcarorderdetails_second);
        mIvWait = (ImageView) findViewById(R.id.iv_rentcarorderdetails_wait);
        mTvCancel = (TextView) findViewById(R.id.tv_rentcarorderdetails_cancel);

        // 退款信息
        mLlRefund = (LinearLayout) findViewById(R.id.ll_rentcarorderdetails_refundifo);
        mTvRefundMoney = (TextView) findViewById(R.id.tv_rentcarorderdetails_refundprice);
        mTvRefundOrderId = (TextView) findViewById(R.id.tv_rentcarorderdetails_refundorder);
        mTvRefundDate = (TextView) findViewById(R.id.tv_rentcarorderdetails_refunddate);
        mTvRefundType = (TextView) findViewById(R.id.tv_rentcarorderdetails_refundtype);
        mTvRefundRate = (TextView) findViewById(R.id.tv_rentcarorderdetails_refundrate);
        mTvRefundStatus = (TextView) findViewById(R.id.tv_rentcarorderdetails_refundstatus);

        // 底部按钮
        mBtBottom = (Button) findViewById(R.id.bt_rentcarorderdetails_bottom);

        // 按钮点击事件
        mIvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 获取电话号码直接跳转拨打页面
            }
        });

        mIvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_visible) {
                    mLlLineInfoIsVisible.setVisibility(View.GONE);
                    flag_visible = false;
                } else {
                    mLlLineInfoIsVisible.setVisibility(View.VISIBLE);
                    flag_visible = true;
                }

            }
        });

        mBtBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 底部按钮，试试其他出行方式等。
            }
        });
    }

    private void getData() {
        HttpRequestManager.getInstance().getOrderdetails(mOrderId, mBizType + "", new ApiSubscriber<JsonResult<OrderdetailsResponsePara>>(RentcarOrderDetailsActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<OrderdetailsResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    mOrderDetails = result.getObject();
                    initData();
                } else {
                    // TODO 显示没有数据或出错
                    showErrorOrNoData(getString(R.string.net_abnormal_error), R.string.net_refresh_again, R.mipmap.base_not_network);
                }
            }
        });
    }

    private void initData() {
        //车次信息
        mSdvIcon.setImageURI(mOrderDetails.getBusLogoUrl());
        mTvFromCity.setText(mOrderDetails.getFromCityName());
        mTvToCity.setText(mOrderDetails.getToCityName());
        mTvDate.setText(mOrderDetails.getDepartTime());//TODO
        mTvTime.setText(mOrderDetails.getDepartTime());//TODO
        mTvFromStation.setText(mOrderDetails.getFromStationName());
        mTvToStation.setText(mOrderDetails.getToStationName());
        mTvPrice.setText(mOrderDetails.getOrderMoney() * 0.01 + "");
        mTvCarType.setText(mOrderDetails.getVehicleType());

        // TODO 资费声明等信息
        mTvZFSM.setText("资费声明");//TODO
        mTvFWBZ.setText("服务标准");//TODO
        mTvQTSM.setText("其他说明");//TODO

        // TODO 中间显示状态、取消、等待等
        mLlTime.setVisibility(View.GONE);
        mTvHour.setVisibility(View.GONE);
        mTvMinute.setVisibility(View.GONE);
        mTvSecond.setVisibility(View.GONE);

        mIvWait.setVisibility(View.GONE);
        mTvCancel.setVisibility(View.GONE);

        // TODO 退款信息
        mLlRefund.setVisibility(View.GONE);
        mTvRefundMoney.setText("123");
        mTvRefundOrderId.setText("123456");
        mTvRefundDate.setText("2016-10-11 12:20");
        mTvRefundType.setText("自主申请");
        mTvRefundRate.setText("退款比例 3%");
        mTvRefundStatus.setText("退款中");
    }
}