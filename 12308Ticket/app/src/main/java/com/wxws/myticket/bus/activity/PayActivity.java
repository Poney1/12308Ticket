package com.wxws.myticket.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.MainActivity;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.entity.FillOrderDescriptionEntity;
import com.wxws.myticket.bus.popupwindow.BookFillOrderPopWindow;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.interfaces.PayCallBackListener;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.order.entity.OrderListEvent;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;
import com.wxws.myticket.pay.PayMethod;
import com.wxws.myticket.pay.PaymentEntity;
import com.wxws.myticket.pay.adapter.PayMethodAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * desc: 汽车票 支付 activity
 * Date: 2016/10/25 10:11
 *
 * @auther: lixiangxiang
 */
public class PayActivity extends BaseActivity {

    /**
     * findview
     */
    private TextView tvBegin;
    private TextView tvTime;
    private TextView tvEndCity;
    private TextView tvBeginStation;
    private TextView tvEndStation;
    private TextView tvTotalPrice;
    private TextView tvTicketCount;
    private RelativeLayout rlShowDesc;
    private TextView tvShowDesc;
    private Button btnPay;
    private TextView tvRemianing;
    private TextView tvWeek;
    private TextView tvAllDistance;
    private TextView amountPayable;
    private ListViewForScrollView lvPayMethod;

    private List<PaymentEntity> data;
    private PayMethodAdapter payMethodAdapter;
    private String orderId;
    private String bizType;
    private String payMethod;
    private int payAmont = -1;//应付款
    private OrderdetailsResponsePara order;
    private BookFillOrderPopWindow bookPopwindow;
    private FillOrderDescriptionEntity fillDescEntity;
    private double fullPrice;
    private double halfPrice;
    private double serviceFee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_pay);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void getPreData() {
        orderId = getIntent().getStringExtra("orderId");
        bizType = getIntent().getStringExtra("bizType");
    }

    private void bindClick() {

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payAmont == -1){
                    ToastManager.getInstance().showToast(PayActivity.this,"支付金额不正确");
                    return;
                }
                payMethod =  data.get(payMethodAdapter.getSelectPay()).getChannelType();
                PayMethod.getDefault().getPayParam(PayActivity.this, payMethod, orderId, payAmont+"",bizType, new PayCallBackListener() {
                    @Override
                    public void paySuccess() {
                        ToastManager.getInstance().showToast(PayActivity.this,"success");
                    }

                    @Override
                    public void payFail() {

                    }
                });
            }
        });
        rlShowDesc.setOnClickListener(this);
    }

    private void initData() {

        barTitle.setText(getString(R.string.pay_submit));

        data = new ArrayList<>();

        payMethodAdapter = new PayMethodAdapter(PayActivity.this,data);

        lvPayMethod.setAdapter(payMethodAdapter);

        //价格详情
        bookPopwindow = new BookFillOrderPopWindow(PayActivity.this);

        fillDescEntity = new FillOrderDescriptionEntity();

        flTitleBarLeft.setEnabled(false);

        getPayMethod();

        getBusDetail();
    }


    private void initView() {
        bindTitleViews();

        bindLoadingView();
        tvRemianing = (TextView) findViewById(R.id.tv_time_remaining);
        tvWeek = (TextView) findViewById(R.id.tv_week);
        tvAllDistance = (TextView) findViewById(R.id.tv_all_distance);
        amountPayable = (TextView) findViewById(R.id.amount_payable);
        lvPayMethod = (ListViewForScrollView) findViewById(R.id.lv_pay_method);

        //出发汽车站
        tvBegin = (TextView) findViewById(R.id.tv_begin);
        //出发时间
        tvTime = (TextView) findViewById(R.id.tv_time);
        //到达城市
        tvEndCity = (TextView) findViewById(R.id.tv_end_city);
        //出发站点
        tvBeginStation = (TextView) findViewById(R.id.tv_begin_station);
        //到达站点
        tvEndStation = (TextView) findViewById(R.id.tv_end_station);

        //底部按钮
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvTicketCount = (TextView) findViewById(R.id.tv_ticket_count);
        rlShowDesc = (RelativeLayout) findViewById(R.id.rl_show_desc);
        tvShowDesc = (TextView) findViewById(R.id.tv_show_desc);
        btnPay = (Button) findViewById(R.id.btn_pay);
    }

    //支付方式
    private void getPayMethod(){
        HttpRequestManager.getInstance().clientPayType(new ApiSubscriber<JsonResult<List<PaymentEntity>>>(PayActivity.this) {
            @Override
            public void onNext(JsonResult<List<PaymentEntity>> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())){
                    if (listJsonResult.getObject()!=null && listJsonResult.getObject().size() > 0){
                        data.clear();
                        data.addAll(listJsonResult.getObject()) ;
                        Collections.sort(data);
                        payMethodAdapter.refreshData(data);
                    }
                }else {
                    ToastManager.getInstance().showToast(PayActivity.this,listJsonResult.getResultMsg());
                }
            }
        });
    }

    //获取详情
    private void getBusDetail(){
        HttpRequestManager.getInstance().getOrderdetails(orderId, bizType, new ApiSubscriber<JsonResult<OrderdetailsResponsePara>>(PayActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<OrderdetailsResponsePara> responseParaJsonResult) {
                if ("0000".equals(responseParaJsonResult.getResultCode())){
                    order = responseParaJsonResult.getObject();
                    flTitleBarLeft.setEnabled(true);
                    updateView();
                }
            }
        });
    }

    private void updateView() {
        refreshPriceDesc();
        payAmont = order.getOrderMoney();
        tvBegin.setText(order.getFromCityName());
        tvEndCity.setText(order.getToCityName());
        tvBeginStation.setText(order.getFromStationName());
        tvEndStation.setText(order.getToStationName());
        tvTime.setText(order.getDepartTime());
        tvWeek.setText(DateTimeUtil.getSpChineseMMDD(order.getDepartDate()));
        amountPayable.setText(String.valueOf(ArithUtils.div(payAmont,100.00)));
        tvTotalPrice.setText(String.valueOf(ArithUtils.div(payAmont,100.00)));
        tvTicketCount.setText(String.format(getString(R.string.bookTicket_count),order.getCount()+""));
        reMain =order.getPaySurplusTime();
        handler.sendEmptyMessage(0);
    }

    private int reMain ;//剩余时间
    //展示剩余时间
    private void showRemainTime(){
        reMain--;
        tvRemianing.setText(String.format(getString(R.string.pay_remain_time),String.valueOf(reMain/60),String.valueOf(reMain%60)));
        if (reMain == 0){
            handler.removeMessages(0);
            return;
        }
    }
    private boolean showDesc = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_show_desc:
                if (showDesc) {
                    if (bookPopwindow != null && bookPopwindow.isShowing()) {
                        bookPopwindow.dismiss();
                        showDesc = false;
                    }
                } else {
                    if (bookPopwindow != null && !bookPopwindow.isShowing()) {
                        bookPopwindow.showPop(findViewById(R.id.ll_bottom_pay));
                        showDesc = true;
                    }
                }
                tvShowDesc.setSelected(showDesc);
                break;
            case R.id.fl_left://不后退  与onBackPressed 一致
                onExitOrder();
                finish();
                break;
        }
    }

    private void refreshPriceDesc() {
        fullPrice = ArithUtils.divDouble(order.getTicketMoney(), 100);

        halfPrice = ArithUtils.divDouble(order.getHalfTicketMoney(), 100);
        serviceFee = ArithUtils.div(order.getTicketInfos().get(0).getThirdFeeMoney(), 100.00);
        fillDescEntity.setFullPrice(fullPrice + "");
        fillDescEntity.setFullCount(order.getAdultTicketCount());
        fillDescEntity.setHalfCount(order.getHalfTicketMoney());
        fillDescEntity.setCoupon(ArithUtils.div(order.getCouponMoney(), 100.00) + "");
        fillDescEntity.setHalfPrice(halfPrice + "");
        bookPopwindow.setDataForPop(fillDescEntity);
    }

    private Handler  handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    handler.sendEmptyMessageDelayed(0,1000);
                    showRemainTime();
                    break;
            }
            return false;
        }
    }) ;
    @Override
    public void onBackPressed() {
        onExitOrder();
    }
    //后退不支付
    private void onExitOrder(){
        Intent  intent  = new Intent(PayActivity.this, MainActivity.class);
        intent.putExtra("isPay",true);
        startActivityWithAnim(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
