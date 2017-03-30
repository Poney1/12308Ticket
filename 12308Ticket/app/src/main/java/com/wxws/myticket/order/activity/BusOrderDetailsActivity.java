package com.wxws.myticket.order.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.my.activity.H5CommonActivity;
import com.wxws.myticket.order.adapter.BusOrderDetailsPassengerAdapter;
import com.wxws.myticket.order.adapter.BusOrderDetailsRefundAdapter;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;
import com.wxws.myticket.order.utils.OrderStatusUtils;
import com.wxws.myticket.order.widgets.CommonListView;

import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeBus;

/**
 * desc: 汽车票及直通车 订单详情
 * Date: 2016-11-01 18:40
 *
 * @author jiangyan
 */
public class BusOrderDetailsActivity extends BaseActivity {

    private String mBizType;//业务类型
    private String mOrderId;// 订单id
    private BusOrderDetailsPassengerAdapter mPassengerAdapter;// 乘客信息adapter
    private BusOrderDetailsRefundAdapter mRefundAdapter;// 退款信息adapter
    private OrderdetailsResponsePara mOrderDetails;// 获取的订单详情信息

    //车次信息，公共
    private TextView mTvCarType;
    private TextView mTvStatus;
    private TextView mTvStartDate;
    private TextView mTvStartCity;
    private TextView mTvStartStation;
    private TextView mTvStartTime;
    private TextView mTvEndCity;
    private TextView mTvEndStation;
    private TextView mTvTotalPrice;
    private TextView mTvCouponPrice;
    private TextView mTvInsurancePrice;
    //private TextView mTvInsuranceNum; TODO
    private TextView mTvFeePrice;
    private TextView mTvFullPrice;
    private TextView mTvHalfPrice;
    private TextView mTvFullNum;
    private TextView mTvHalfNum;
    private TextView mTvWithchildNum;

    // 扫码相关
    private LinearLayout mLlQCPecode;
    private LinearLayout mLlZTCmap;
    private LinearLayout mLlZTCecode;

    //旅客短信信息
    private LinearLayout mLlTips;
    private TextView mTvTipsInfo;

    //乘车信息
    private LinearLayout mLlCcLayout;
    private TextView mTvCcDriver;
    private TextView mTvCcPhone;
    private TextView mTvCcCarNum;
    private TextView mTvCcTime;
    private TextView mTvCcSite;

    // 乘客信息layout
    private LinearLayout mLlPassengerLayout;

    // 购票人信息
    private LinearLayout mLlGprLayout;
    private TextView mTvGprName;
    private TextView mTvGprPhone;

    // 取票人信息
    private LinearLayout mLlQprLayout;
    private TextView mTvQprName;
    private TextView mTvQprIdCard;
    private TextView mTvQprPhone;

    // 退款信息
    private LinearLayout mLlRefundLayout;

    // 底部按钮
    private LinearLayout mLlBottom;
    private TextView mTvLeftAmount;
    private TextView mTvLeftCount;
    private TextView mTvLeftAnchor;
    private LinearLayout mLlBtLeft;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busorderdetails);
        intiCommonView();
        getDataFromPre();
        initView();
        getData();
    }

    private void intiCommonView() {
        bindTitleViews();
        barTitle.setText("订单详情");

        bindLoadingView();
    }

    private void getDataFromPre() {
        mBizType = getIntent().getStringExtra("bizType");
        mOrderId = getIntent().getStringExtra("orderId");
    }

    private void initView() {
        //title中电话
        ImageView ivCall = (ImageView) findViewById(R.id.iv_busorderdetails_call);

        //车次信息，公共
        mTvCarType = (TextView) findViewById(R.id.tv_busorderdetails_bustype);
        mTvStatus = (TextView) findViewById(R.id.tv_busorderdetails_status);
        mTvStartDate = (TextView) findViewById(R.id.tv_busorderdetails_startdate);
        mTvStartCity = (TextView) findViewById(R.id.tv_busorderdetails_startcity);
        mTvStartStation = (TextView) findViewById(R.id.tv_busorderdetails_startstation);
        mTvStartTime = (TextView) findViewById(R.id.tv_busorderdetails_starttime);
        mTvEndCity = (TextView) findViewById(R.id.tv_busorderdetails_endcity);
        mTvEndStation = (TextView) findViewById(R.id.tv_busorderdetails_endstation);

        mTvTotalPrice = (TextView) findViewById(R.id.tv_busorderdetails_totalprice);
        mTvCouponPrice = (TextView) findViewById(R.id.tv_busorderdetails_couponprice);
        mTvInsurancePrice = (TextView) findViewById(R.id.tv_busorderdetails_insuranceprice);
        //mTvInsuranceNum = (TextView) findViewById(R.id.tv_busorderdetails_insurancenum);
        mTvFeePrice = (TextView) findViewById(R.id.tv_busorderdetails_feeprice);
        mTvFullPrice = (TextView) findViewById(R.id.tv_busorderdetails_fullprice);
        mTvHalfPrice = (TextView) findViewById(R.id.tv_busorderdetails_halfprice);
        mTvFullNum = (TextView) findViewById(R.id.tv_busorderdetails_fullnum);
        mTvHalfNum = (TextView) findViewById(R.id.tv_busorderdetails_halfnum);
        mTvWithchildNum = (TextView) findViewById(R.id.tv_busorderdetails_withchildnum);

        // TODO 汽车票扫码
        mLlQCPecode = (LinearLayout) findViewById(R.id.ll_busorderdetails_ecode);
        // TODO 直通车扫码
        mLlZTCmap = (LinearLayout) findViewById(R.id.ll_busorderdetails_map);
        mLlZTCecode = (LinearLayout) findViewById(R.id.ll_busorderdetails_eticket);

        // TODO 旅客短信信息，有则显示
        mLlTips = (LinearLayout) findViewById(R.id.ll_busorderdetails_tickettips);
        mTvTipsInfo = (TextView) findViewById(R.id.tv_busorderdetails_tickettipscontent);

        // 乘车退票指南
        TextView tvRefundTips = (TextView) findViewById(R.id.tv_busorderdetails_refundtips);

        // 乘车信息
        mLlCcLayout = (LinearLayout) findViewById(R.id.ll_busorderdetails_ccinfo);
        mTvCcDriver = (TextView) findViewById(R.id.tv_busorderdetails_ccdriver);
        mTvCcPhone = (TextView) findViewById(R.id.tv_busorderdetails_ccphone);
        mTvCcCarNum = (TextView) findViewById(R.id.tv_busorderdetails_cccarnum);
        mTvCcTime = (TextView) findViewById(R.id.tv_busorderdetails_cctime);
        mTvCcSite = (TextView) findViewById(R.id.tv_busorderdetails_ccsite);

        // 乘客信息
        mLlPassengerLayout = (LinearLayout) findViewById(R.id.ll_busorderdetails_passengerinfo);
        CommonListView clvPassengerList = (CommonListView) findViewById(R.id.clv_busorderdetails_passengerlist);

        // 购票人信息
        mLlGprLayout = (LinearLayout) findViewById(R.id.ll_busorderdetails_gprinfo);
        mTvGprName = (TextView) findViewById(R.id.tv_busorderdetails_gprname);
        mTvGprPhone = (TextView) findViewById(R.id.tv_busorderdetails_gprphone);

        // 取票人信息
        mLlQprLayout = (LinearLayout) findViewById(R.id.ll_busorderdetails_qprinfo);
        mTvQprName = (TextView) findViewById(R.id.tv_busorderdetails_qprname);
        mTvQprIdCard = (TextView) findViewById(R.id.tv_busorderdetails_qprcardnum);
        mTvQprPhone = (TextView) findViewById(R.id.tv_busorderdetails_qprphone);

        // 退款信息
        mLlRefundLayout = (LinearLayout) findViewById(R.id.ll_busorderdetails_refundlayout);
        CommonListView clvRefundList = (CommonListView) findViewById(R.id.clv_busorderdetails_refundlist);

        // 底部按钮
        mLlBottom = (LinearLayout) findViewById(R.id.ll_busorderdetails_bottom);
        mLlBtLeft = (LinearLayout) findViewById(R.id.ll_busorderdetails_btleft);
        mTvLeftAmount = (TextView) findViewById(R.id.tv_busorderdeatils_amount);
        mTvLeftCount = (TextView) findViewById(R.id.tv_busorderdetails_count);
        mTvLeftAnchor = (TextView) findViewById(R.id.tv_busorderdetails_anchor);
        Button btRight = (Button) findViewById(R.id.bt_busorderdetails_btright);

        mPassengerAdapter = new BusOrderDetailsPassengerAdapter(BusOrderDetailsActivity.this);
        clvPassengerList.setAdapter(mPassengerAdapter);

        mRefundAdapter = new BusOrderDetailsRefundAdapter(BusOrderDetailsActivity.this);
        clvRefundList.setAdapter(mRefundAdapter);

        ivCall.setOnClickListener(this);
        mLlQCPecode.setOnClickListener(this);
        mLlZTCmap.setOnClickListener(this);
        mLlZTCecode.setOnClickListener(this);
        tvRefundTips.setOnClickListener(this);
        mLlBtLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_busorderdetails_call: // 打电话
                // TODO 旧业务拨打客服电话，新业务直通车是其他的电话。
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "075582867116"));
                startActivityWithAnim(callIntent);

                break;
            case R.id.ll_busorderdetails_eticket:// 汽车票扫码
                // TODO 汽车票扫码
                Intent busIntent = new Intent(BusOrderDetailsActivity.this, H5CommonActivity.class);
                busIntent.putExtra("URL", mOrderDetails.getCheckTicketUrl());// TODO 检票跳转URL，后续判断
                startActivityWithAnim(busIntent);
                break;
            case R.id.ll_busorderdetails_map: // 直通车地图
                Intent mapIntent = new Intent(BusOrderDetailsActivity.this, BusMapActivity.class);
                mapIntent.putExtra("scheduleId", mOrderDetails.getScheduleId());
                startActivityWithAnim(mapIntent);
                break;
            case R.id.ll_busorderdetails_ecode:// 直通车扫码
                // TODO 直通车扫码
                Intent lineIntent = new Intent(BusOrderDetailsActivity.this, H5CommonActivity.class);
                lineIntent.putExtra("URL", mOrderDetails.getCheckTicketUrl());//TODO  检票跳转URL
                startActivityWithAnim(lineIntent);
                break;
            case R.id.tv_busorderdetails_refundtips:// 乘车退票指南
                Intent refundIntent = new Intent(BusOrderDetailsActivity.this, H5CommonActivity.class);
                refundIntent.putExtra("URL", mOrderDetails.getReturnTicketInfoUrl());// TODO 后台返回的数据，后续判断是否能正常显示。
                startActivityWithAnim(refundIntent);
                break;
            case R.id.ll_busorderdetails_btleft:// 底部左边按钮，弹出弹窗显示价格
                // TODO 弹窗显示价格
                break;
            case R.id.bt_busorderdetails_btright:// 底部右边按钮，去支付退款等，看具体使用情景
                if (((Button) v).getText().equals("去支付")) { //TODO 底部点击按钮

                } else {

                }
                break;
        }
    }

    private void getData() {

        HttpRequestManager.getInstance().getOrderdetails(mOrderId, mBizType, new ApiSubscriber<JsonResult<OrderdetailsResponsePara>>(BusOrderDetailsActivity.this, ApiSubscriber.NETVIEWLOADING) {
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

        // 根据数据及类型判断是否要显示数据，之后根据是否可见来显示数据
        // TODO 还需要判断底部按钮是否显示，根据状态来，后续处理。
        boolean hasQCPecode = mOrderDetails.getCheckTicketUrl() != null && (!mOrderDetails.getCheckTicketUrl().equals(""));

        boolean hasZTCecode = mOrderDetails.getCheckTicketUrl() != null && (!mOrderDetails.getCheckTicketUrl().equals(""));
        boolean hasZTCmap = mOrderDetails.getScheduleId() != null && (!mOrderDetails.getScheduleId().equals(""));

        boolean hasSMScontent = mOrderDetails.getTicketMsg() != null && (!mOrderDetails.getTicketMsg().equals(""));
        boolean hasTicketInfoList = mOrderDetails.getTicketInfos() != null && (!(mOrderDetails.getTicketInfos().size() == 0));

        if (mBizType.equals(bizTypeBus)) {// 汽车票
            if (hasQCPecode) {
                mLlQCPecode.setVisibility(View.VISIBLE);
            } else {
                mLlQCPecode.setVisibility(View.GONE);
            }
            mLlZTCecode.setVisibility(View.GONE);
            mLlZTCmap.setVisibility(View.GONE);
            mLlQprLayout.setVisibility(View.VISIBLE);
            mLlCcLayout.setVisibility(View.GONE);
            mLlGprLayout.setVisibility(View.GONE);

        } else {// 直通车

            if (hasZTCecode) {
                mLlZTCecode.setVisibility(View.VISIBLE);
            } else {
                mLlZTCecode.setVisibility(View.GONE);
            }

            if (hasZTCmap) {
                mLlZTCmap.setVisibility(View.VISIBLE);
            } else {
                mLlZTCmap.setVisibility(View.GONE);
            }

            mLlQCPecode.setVisibility(View.GONE);
            mLlQprLayout.setVisibility(View.GONE);
            mLlCcLayout.setVisibility(View.VISIBLE);
            mLlGprLayout.setVisibility(View.VISIBLE);
        }

        if (hasSMScontent) { // 短信内容
            mLlTips.setVisibility(View.VISIBLE);
        } else {
            mLlTips.setVisibility(View.GONE);
        }

        if (hasTicketInfoList) {// 退款信息list
            mLlRefundLayout.setVisibility(View.VISIBLE);
        } else {
            mLlRefundLayout.setVisibility(View.GONE);
        }

        // 车次信息，done
        mTvCarType.setText(mOrderDetails.getVehicleType());
        mTvStatus.setText(OrderStatusUtils.getOrderStatus(mOrderDetails.getOrderStatus()));
        mTvStartDate.setText(mOrderDetails.getDepartDate());
        mTvStartCity.setText(mOrderDetails.getFromCityName());
        mTvStartStation.setText(mOrderDetails.getFromStationName());
        mTvStartTime.setText(mOrderDetails.getDepartTime());
        mTvEndCity.setText(mOrderDetails.getToCityName());
        mTvEndStation.setText(mOrderDetails.getToStationName());
        mTvTotalPrice.setText("¥" + ArithUtils.mul(mOrderDetails.getOrderMoney(), 0.01));
        mTvCouponPrice.setText("¥" + ArithUtils.mul(mOrderDetails.getCouponMoney(), 0.01));
        mTvInsurancePrice.setText(ArithUtils.mul(mOrderDetails.getInsuranceMoney(), 0.01) + "/份");
        //mTvInsuranceNum
        mTvFeePrice.setText(ArithUtils.mul(mOrderDetails.getThirdFeeMoney(), 0.01) + "/张");
        mTvFullPrice.setText(ArithUtils.mul(mOrderDetails.getTicketMoney(), 0.01) + "/张");
        mTvHalfPrice.setText(ArithUtils.mul(mOrderDetails.getHalfTicketMoney(), 0.01) + "/张");
        mTvFullNum.setText(mOrderDetails.getAdultTicketCount() + "张");
        mTvHalfNum.setText(mOrderDetails.getHalfTicketCount() + "张");
        mTvWithchildNum.setText(mOrderDetails.getChildtTicketCount() + "张");

        // 旅客短信信息
        if (mLlTips.getVisibility() == View.VISIBLE) {
            mTvTipsInfo.setText(mOrderDetails.getTicketMsg());
        }

        // TODO 乘车信息，部分字段没数据
        if (mLlCcLayout.getVisibility() == View.VISIBLE) {
            mTvCcDriver.setText(mOrderDetails.getBusCompany());
            mTvCcPhone.setText(mOrderDetails.getBusMobile());
            mTvCcCarNum.setText(mOrderDetails.getBusNumber());
            mTvCcTime.setText(mOrderDetails.getDepartDate());
            mTvCcSite.setText(mOrderDetails.getByCarAddr());
        }

        // TODO 乘客信息,最后的保险几份没处理
        if (hasTicketInfoList) {//有乘客信息
            mLlPassengerLayout.setVisibility(View.VISIBLE);
            mPassengerAdapter.refreshData(mOrderDetails.getTicketInfos());
        } else {
            mLlPassengerLayout.setVisibility(View.GONE);
        }


        //TODO  购票人信息
        if (mLlGprLayout.getVisibility() == View.VISIBLE) {
            mTvGprName.setText("购票人姓名");
            mTvGprPhone.setText("购票人手机号码");
        }

        // 取票人信息
        if (mLlQprLayout.getVisibility() == View.VISIBLE) {
            mTvQprName.setText(mOrderDetails.getName());
            mTvQprIdCard.setText(mOrderDetails.getCertNo());
            mTvQprPhone.setText(mOrderDetails.getMobile());
        }


        //TODO  退款信息 部分没数据
        if (mLlRefundLayout.getVisibility() == View.VISIBLE) {
            mRefundAdapter.refreshData(mOrderDetails.getTicketInfos());
        }

        //TODO  底部按钮
        if (mLlBottom.getVisibility() == View.VISIBLE && mLlBtLeft.getVisibility() == View.VISIBLE) { // TODO 更加状态改变底部按钮
            mTvLeftAmount.setText(ArithUtils.mul(mOrderDetails.getOrderMoney(), 0.01) + "");
            mTvLeftCount.setText("(共" + mOrderDetails.getCount() + "人)");
        }

    }

    @Override
    public void clickReload() {
        super.clickReload();
        getData();
    }
}