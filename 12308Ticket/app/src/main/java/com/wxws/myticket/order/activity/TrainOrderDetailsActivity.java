package com.wxws.myticket.order.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.activity.H5CommonActivity;
import com.wxws.myticket.order.adapter.TrainOrderdetailsAdapter;
import com.wxws.myticket.order.entity.CancelOrderModel;
import com.wxws.myticket.order.entity.EndorseFailureModel;
import com.wxws.myticket.order.entity.TrainOrderDetailsTicketModel;
import com.wxws.myticket.order.entity.TrainOrderdetailsResponseModel;
import com.wxws.myticket.order.entity.TrainPriceModel;
import com.wxws.myticket.order.utils.TrainOrderStatusUtils;
import com.wxws.myticket.order.widgets.CommonDialog;
import com.wxws.myticket.order.widgets.CommonListView;
import com.wxws.myticket.train.activity.TrainListActivity;
import com.wxws.myticket.train.activity.TrainPayActivity;

import java.io.Serializable;


/**
 * desc: 火车票订单详情
 * Date: 2016-11-01 18:35
 *
 * @author jiangyan
 */
public class TrainOrderDetailsActivity extends BaseActivity {

    // 乘客类型 1:成人票，2:儿童票，3:学生票，4:残军票
    public final static int ADULT_TICKET = 1;
    public final static int CHILD_TICKET = 2;
    public final static int STUDENT_TICKET = 3;
    public final static int SOLDIER_TICKET = 4;

    // 顶部提示信息等
    private ScrollView sv_container;
    private LinearLayout ll_top_tips;
    private TextView tv_tips_info;

    // 改签票信息

    // 一般票信息
    private TextView tv_ticket_num;
    private TextView tv_status;
    private TextView tv_fromdate;
    private TextView tv_fromtime;
    private TextView tv_fromstation;
    private TextView tv_todate;
    private TextView tv_totime;
    private TextView tv_tostation;
    private TextView tv_traincode;
    private TextView tv_traintime;
    private CommonListView lv_ticketsinfo;

    //联系电话等
    private TextView tv_phone;
    private TextView tv_adultinsurance;
    private TextView tv_childinsurance;

    // 底部按钮
    private LinearLayout ll_buttonbottom;
    private LinearLayout lltraindetailsbtleft;
    private TextView tv_amount;
    private TextView tv_count;
    private Button bttraindeatilsbtright;

    // view无关
    private String orderNo;
    private TrainOrderdetailsResponseModel mOrderResponseInfo;
    private int times = 0;
    private LinearLayout ll_deleteOrder;
    private LinearLayout ll_refundlayout;
    private TrainOrderdetailsAdapter mTicketAdapter;
    private String mStatus;
    private TimeCount timeCount;
    private int mOrderType;
    private LinearLayout ll_childinsurance;
    private CommonDialog mDialog;

    Handler delayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                sv_container.smoothScrollTo(0, 0);
            }
        }
    };


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trainorderdetails);
        getUserInfo();
        orderNo = getIntent().getStringExtra("orderId");
        mOrderType = getIntent().getIntExtra("ordertype", 1);

        if (StringUtils.isNullOrEmpty(orderNo)) {
            finish();
            return;
        }
        initTitle();

        initView();
        getTrainOrderDetails();
    }

    private void initTitle() {
        bindTitleViews();
        //bindLoadingView();
        barTitle.setText(R.string.order_details);
    }

    private void initView() {

        sv_container = (ScrollView) findViewById(R.id.sv_trianorderdetails_scrollview);
        // 顶部提示信息
        tv_tips_info = (TextView) findViewById(R.id.tv_traindetails_tipsinfo);
        ll_top_tips = (LinearLayout) findViewById(R.id.ll_traindetails_toptips);
        ll_top_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrainOrderDetailsActivity.this, TrainOperateDetailsActivity.class);

                intent.putExtra("orderid", mOrderResponseInfo.getOrderId());
                intent.putExtra("ordernum", mOrderResponseInfo.getOrderNumber());
                intent.putExtra("amount", mOrderResponseInfo.getAmount());
                intent.putExtra("status", mOrderResponseInfo.getOrderStatus());

                Bundle bundle = new Bundle();
                bundle.putSerializable("ticketlist", (Serializable) mOrderResponseInfo.getTicketList());
                intent.putExtra("ticketbundle", bundle);

                startActivityWithAnim(intent);
            }
        });

        // 非改签票信息
        tv_ticket_num = (TextView) findViewById(R.id.tv_traindetails_ticketnum);
        tv_status = (TextView) findViewById(R.id.tv_traindetails_status);
        tv_fromdate = (TextView) findViewById(R.id.tv_traindetails_fromdate);
        tv_fromtime = (TextView) findViewById(R.id.tv_traindetails_fromtime);
        tv_fromstation = (TextView) findViewById(R.id.tv_traindetails_fromstation);
        tv_todate = (TextView) findViewById(R.id.tv_traindetails_todate);
        tv_totime = (TextView) findViewById(R.id.tv_traindetails_totime);
        tv_tostation = (TextView) findViewById(R.id.tv_traindetails_tostation);
        tv_traincode = (TextView) findViewById(R.id.tv_traindetails_trainline);
        tv_traintime = (TextView) findViewById(R.id.tv_traindetails_traintime);
        lv_ticketsinfo = (CommonListView) findViewById(R.id.lv_traindetails_ticketsinfo);


        // 联系手机/保险/退票等信息
        tv_phone = (TextView) findViewById(R.id.tv_traindetails_phone);
        tv_adultinsurance = (TextView) findViewById(R.id.tv_traindetails_adultinsurance);
        ll_childinsurance = (LinearLayout) findViewById(R.id.ll_orderdetails_childinsurance);
        tv_childinsurance = (TextView) findViewById(R.id.tv_traindetails_childinsurance);
        ll_refundlayout = (LinearLayout) findViewById(R.id.ll_traindetails_refundlayout);

        // 删除订单
        ll_deleteOrder = (LinearLayout) findViewById(R.id.ll_orderdetails_deleteorder);

        // 底部按钮
        ll_buttonbottom = (LinearLayout) findViewById(R.id.ll_orderdetails_bottom);
        lltraindetailsbtleft = (LinearLayout) findViewById(R.id.ll_traindetails_btleft);
        tv_amount = (TextView) findViewById(R.id.tv_traindetails_amount);
        tv_count = (TextView) findViewById(R.id.tv_traindetails_count);
        bttraindeatilsbtright = (Button) findViewById(R.id.bt_traindeatils_btright);

        ll_refundlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退票、改签须知 TODO H5跳转，H5提供地址
                Intent intent = new Intent(TrainOrderDetailsActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", "http://www.baidu.com");
                startActivityWithAnim(intent);
            }
        });

        ll_deleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteOrder();
            }
        });

        bttraindeatilsbtright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((Button) v).getText().equals("立即支付")) {
                    Intent intent = new Intent(TrainOrderDetailsActivity.this, TrainPayActivity.class);

                    if (mOrderType == 2) { // 如果是改签待支付
                        intent.putExtra("orderNo", orderNo);
                        intent.putExtra("isChange", true);
                    } else {
                        intent.putExtra("orderNo", orderNo);
                    }
                    startActivity(intent);
                } else if (((Button) v).getText().equals("购买返程")) {

                    Intent intent = new Intent(TrainOrderDetailsActivity.this, TrainListActivity.class);

                    intent.putExtra("startCity", mOrderResponseInfo.getToStationName());
                    intent.putExtra("startCode", mOrderResponseInfo.getToStationCode());
                    intent.putExtra("endCity", mOrderResponseInfo.getFromStationName());
                    intent.putExtra("endCode", mOrderResponseInfo.getFromStationCode());

                    if (tv_status.getText().toString().equals("等待出票")) {
                        intent.putExtra("startTime", DateTimeUtil.addDay(DateTimeUtil.getDateyyyyMMdd(mOrderResponseInfo.getFromTime()), 1));
                    } else {
                        intent.putExtra("startTime", DateTimeUtil.getSysDateYMD());
                    }
                    intent.putExtra("isOnlySpeed", false);

                    startActivity(intent);
                }

            }
        });

        mTicketAdapter = new TrainOrderdetailsAdapter(TrainOrderDetailsActivity.this, orderNo, mOrderType);
        lv_ticketsinfo.setAdapter(mTicketAdapter);
    }

    private void initData() {

        //传递子订单编号id
        mTicketAdapter.setOrder(mOrderResponseInfo);
        initStatus();
        // 非改签票信息
        if (null == mOrderResponseInfo.getOrderNumber() || "".equals(mOrderResponseInfo.getOrderNumber())) {
            ((TextView) findViewById(R.id.tv_orderdetails_text1)).setText(getString(R.string.orderlist_getticket_orderid));
            tv_ticket_num.setText(mOrderResponseInfo.getOrderId());

        } else {
            ((TextView) findViewById(R.id.tv_orderdetails_text1)).setText(getString(R.string.orderlist_getticket_num));
            tv_ticket_num.setText(mOrderResponseInfo.getOrderNumber());
        }

        tv_fromdate.setText(DateTimeUtil.getDateMMDD(mOrderResponseInfo.getFromTime()));
        tv_fromtime.setText(DateTimeUtil.getDateHHMM(mOrderResponseInfo.getFromTime()));
        tv_fromstation.setText(mOrderResponseInfo.getFromStationName());
        tv_todate.setText(DateTimeUtil.getDateMMDD(mOrderResponseInfo.getToTime()));
        tv_totime.setText(DateTimeUtil.getDateHHMM(mOrderResponseInfo.getToTime()));
        tv_tostation.setText(mOrderResponseInfo.getToStationName());
        tv_traincode.setText(mOrderResponseInfo.getTrainCode() + "");
        tv_traintime.setText(DateTimeUtil.minToHour(mOrderResponseInfo.getRunTimeMinute() + ""));

        mTicketAdapter.refreshData(mOrderResponseInfo.getTicketList());
        mTicketAdapter.setFromstationCode(mOrderResponseInfo.getFromStationCode(), mOrderResponseInfo.getFromStationName());

        // 联系手机/保险/退票等信息
        tv_phone.setText(mOrderResponseInfo.getMobile());
        if (mOrderResponseInfo.getAdultInsuranceNum().equals("0") || null == mOrderResponseInfo.getAdultInsuranceName()) {
            tv_adultinsurance.setText("未购买成人保险");
        } else {
            tv_adultinsurance.setText(mOrderResponseInfo.getAdultInsuranceName() + "x" + mOrderResponseInfo.getAdultInsuranceNum());
        }

        if (mOrderResponseInfo.getChildInsuranceNum().equals("0") || null == mOrderResponseInfo.getChildInsuranceName()) {
            ll_childinsurance.setVisibility(View.GONE);
            tv_childinsurance.setText("未购买儿童保险");
        } else {
            ll_childinsurance.setVisibility(View.VISIBLE);
            tv_childinsurance.setText(mOrderResponseInfo.getChildInsuranceName() + "x" + mOrderResponseInfo.getChildInsuranceNum());
        }

        flTitleBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvRight.getText().equals("取消改签")) {
                    cancelEndorse();
                } else {
                    cancelOrder();
                }

            }
        });

    }

    public void cancelEndorse() {// 取消改签

        if(mDialog == null){
            mDialog = new CommonDialog(TrainOrderDetailsActivity.this);
        }
        mDialog.showDialog("取消提示", "确定取消改签？", new DialogCallBackListener() {
            @Override
            public void doPositiveClick(String string) {
            }

            @Override
            public void doPositiveClick() {
                System.out.println("取消改签：" + mOrderResponseInfo.getOriginalOrderId() + ":" + orderNo);
//                mLoadingPopWindow.showPop(findViewById(R.id.ll_orderdetails_container));
                mLoadingPopWindow.showLoadingDialog();
                cancelEndorseToServer();
            }

            @Override
            public void doNegativeClick() {

            }
        });
    }



    public void cancelOrder() {// 取消订单

        mDialog.showDialog("取消提示", "确定取消订单？", new DialogCallBackListener() {
            @Override
            public void doPositiveClick(String string) {

            }

            @Override
            public void doPositiveClick() {
                mLoadingPopWindow.showLoadingDialog();
                cancelOrderToServer();

            }

            @Override
            public void doNegativeClick() {
                mDialog.dissmissDialog();
            }
        });

    }

    public void initStatus() {

        if (mOrderType == 1) {
            mStatus = TrainOrderStatusUtils.getCommonStatus(mOrderResponseInfo.getOrderStatus());
        } else {
            mStatus = TrainOrderStatusUtils.getEndorseStatus(mOrderResponseInfo.getOrderStatus());
        }

        tv_status.setText(mStatus);
        flTitleBarRight.setVisibility(View.GONE);
        switch (mOrderResponseInfo.getOrderStatus()) {

            case 1://抢票中,改签申请中
                // 顶部提示信息
                tv_tips_info.setText("正在拼命为您抢座，请耐心等候");
                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.GONE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.GONE);

                break;

            case 4://抢票失败,改签失败
            case 9:
                allToGray();
                // 顶部提示信息
                tv_tips_info.setText("温馨提示：很遗憾，抢票失败");
                // 删除订单
                ll_deleteOrder.setVisibility(View.VISIBLE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.GONE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.GONE);
                break;

            case 10://等待支付,改签待支付
            case 16:
            case 19:
                // 如果是等待支付，则取消订单处理
                flTitleBarRight.setVisibility(View.VISIBLE);
                if (mOrderType == 1) {
                    tvRight.setText("取消订单");
                } else {
                    tvRight.setText("取消改签");
                }

                // 顶部提示信息

                if (mOrderResponseInfo.getDeadlineTimeMillis() <= 0) {
                    bttraindeatilsbtright.setEnabled(false);
                    tv_tips_info.setText("抱歉，您的操作已超时，请重新购票。");
                } else {
                    // 构造CountDownTimer对象
                    timeCount = new TimeCount(mOrderResponseInfo.getDeadlineTimeMillis(), 1000);
                    timeCount.start();
                }

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);

                calculateTotal();

                break;
            case 11://订单关闭,改签取消
            case 17:
                allToGray();
                // 顶部提示信息
                tv_tips_info.setText("抱歉，您的操作已超时，请重新购票");

                // 删除订单
                ll_deleteOrder.setVisibility(View.VISIBLE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.GONE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.GONE);

                break;

            case 20://等待出票，改签待出票
            case 31:
            case 34:
            case 35:
            case 36:

                // 顶部提示信息
                tv_tips_info.setText("请您耐心等待，代理商会尽快为您出票。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");

                break;
            case 38://改签失败，改签失败
                // 顶部提示信息
                allToGray();
                tv_tips_info.setText("请凭购票时有效证件到火车票或代售点取票");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;

            case 39://出票失败，改签失败
                // 顶部提示信息
                tv_tips_info.setText("温馨提示，出票失败，没有足够的票。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.VISIBLE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.GONE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.GONE);
                break;
            case 40://出票成功，改签成功
                // 顶部提示信息
                tv_tips_info.setText("请凭购票时有效证件到火车票或代售点取票");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");

                break;
            case 41://已改签，已改签
                allToGray();
                // 顶部提示信息
                tv_tips_info.setText("改签成功，感谢您选择铁路出行！");//您的取票号E123456789

                // 删除订单
                ll_deleteOrder.setVisibility(View.VISIBLE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;

            case 45://已取票，已取票
                // 顶部提示信息
                tv_tips_info.setText("温馨提示，您已经取票成功。");//您于2016年8月30日 15:30 取票成功

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;

            case 51://退票中,退票中
                // 顶部提示信息
                tv_tips_info.setText("请您耐心等待，代理商会尽快为您退票。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;

            case 59://退票失败,退票失败
                // 顶部提示信息
                tv_tips_info.setText("退票失败，因铁路系统繁忙，请持有效购票证件到火车站办理。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;

            case 60://待退款,待退款
                // 顶部提示信息
                tv_tips_info.setText("请您耐心等待，代理商会尽快为您安排退款。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;
            case 50:
            case 61://退款中,退款中
                // 顶部提示信息
                tv_tips_info.setText("请您耐心等待，代理商会尽快为您退款。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;
            case 69://退款失败,退款失败
                // 顶部提示信息
                tv_tips_info.setText("退款失败，系统已重新发起退款，将在1~10工作日退回原账户，请耐心等待。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.GONE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.VISIBLE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.VISIBLE);
                bttraindeatilsbtright.setText("购买返程");
                break;
            case 70://退款成功,退款成功
                allToGray();
                // 顶部提示信息
                tv_tips_info.setText("您的退款已成功，会在1-10个工作日内原路退回。");

                // 删除订单
                ll_deleteOrder.setVisibility(View.VISIBLE);

                // 底部按钮
                ll_buttonbottom.setVisibility(View.GONE);
                lltraindetailsbtleft.setVisibility(View.GONE);
                bttraindeatilsbtright.setVisibility(View.GONE);
                break;

        }
    }

    public void deleteOrder() {

        if(mDialog == null){
            mDialog = new CommonDialog(TrainOrderDetailsActivity.this);
        }else{
            if(mDialog.isShow()){
                mDialog.dissmissDialog();
            }
        }
        mDialog.showDialog("删除提示", "确认删除订单？", new DialogCallBackListener() {
            @Override
            public void doPositiveClick(String string) {
            }

            @Override
            public void doPositiveClick() {
                deleteOrderToServer();
            }

            @Override
            public void doNegativeClick() {
                mDialog.dissmissDialog();
            }
        });
    }


    private TrainPriceModel getTrainprice() { // 计算弹出框内部数据需要显示的数据 TODO 暂时没有用到，后续再看。

        TrainPriceModel model = new TrainPriceModel();
        int fullnum = 0;
        int childnum = 0;
        int studentnum = 0;
        int adultinsurancenum = 0;
        int childinsurancenum = 0;
        int couponPrice = 0;

        // 等待支付，说明是一般流程的弹窗
        for (TrainOrderDetailsTicketModel ticket : mOrderResponseInfo.getTicketList()) {

            if (ticket.getTicketType() == 1) {
                // 1:成人票，2:儿童票，3:学生票，
                model.setFullprice(ticket.getTicketAmount() * 0.01 + "");
                fullnum++;
                model.setAdultinsuranceprice(ticket.getInsuranceAmount() * 0.01 + "");
                adultinsurancenum++;

            }

            if (ticket.getTicketType() == 2) {
                model.setChildprice(ticket.getTicketAmount() * 0.01 + "");
                childnum++;
                model.setChildinsuranceprice(ticket.getInsuranceAmount() * 0.01 + "");
                childinsurancenum++;
            }

            if (ticket.getTicketType() == 3) {
                model.setStudentprice(ticket.getTicketAmount() * 0.01 + "");
                studentnum++;
                model.setAdultinsuranceprice(ticket.getInsuranceAmount() * 0.01 + "");
                adultinsurancenum++;
            }
        }

        model.setFullnum(fullnum);
        model.setChildnum(childnum);
        model.setStudentnum(studentnum);
        model.setAdultinsurancenum(adultinsurancenum);
        model.setChildinsurancenum(childinsurancenum);
        model.setCouponprice(mOrderResponseInfo.getCouponAmount() * 0.01 + "");

        return model;
    }

    public void calculateTotal() {

        tv_amount.setText(mOrderResponseInfo.getAmount() * 0.01 + "");
        tv_count.setText("（共" + mOrderResponseInfo.getTicketList().size() + "人）");

    }

    public void allToGray() { //

        ((TextView) findViewById(R.id.tv_orderdetails_text1)).setTextColor(Color.parseColor("#999999"));
        ((TextView) findViewById(R.id.tv_orderdetails_text2)).setTextColor(Color.parseColor("#999999"));
        ((TextView) findViewById(R.id.tv_orderdetails_text3)).setTextColor(Color.parseColor("#999999"));
        ((TextView) findViewById(R.id.tv_orderdetails_text4)).setTextColor(Color.parseColor("#999999"));
        ((TextView) findViewById(R.id.tv_orderdetails_text5)).setTextColor(Color.parseColor("#999999"));
        findViewById(R.id.iv_orderdetails_arrow).setBackgroundResource(R.mipmap.icon_orderdetails_grayarrow);

        // 非改签票信息
        tv_ticket_num.setTextColor(Color.parseColor("#999999"));
        tv_fromdate.setTextColor(Color.parseColor("#999999"));
        tv_fromtime.setTextColor(Color.parseColor("#999999"));
        tv_fromstation.setTextColor(Color.parseColor("#999999"));
        tv_todate.setTextColor(Color.parseColor("#999999"));
        tv_totime.setTextColor(Color.parseColor("#999999"));
        tv_tostation.setTextColor(Color.parseColor("#999999"));
        tv_traincode.setTextColor(Color.parseColor("#999999"));
        tv_traintime.setTextColor(Color.parseColor("#999999"));

        // 联系手机/保险/退票等信息
        tv_phone.setTextColor(Color.parseColor("#999999"));
        tv_adultinsurance.setTextColor(Color.parseColor("#999999"));
        tv_childinsurance.setTextColor(Color.parseColor("#999999"));
        ll_refundlayout.setClickable(false);

    }


    public void getTrainOrderDetails() {
        if (null != timeCount) { // 刷新界面的时候获取，则cancel倒计时。
            timeCount.cancel();
        }
        HttpRequestManager.getInstance().getTrainOrderdetails(orderNo, new ApiSubscriber<JsonResult<TrainOrderdetailsResponseModel>>(TrainOrderDetailsActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<TrainOrderdetailsResponseModel> result) {
                if ("0000".equals(result.getResultCode()) && result.getObject() != null) {
                    mOrderResponseInfo = result.getObject();
                    initData();
                    times++;
                    //dismissLoading();
                    delayHandler.sendEmptyMessageDelayed(1, 16);
                } else {
                    showErrorOrNoData(getString(R.string.loaddata_fail), R.string.click_reload,R.mipmap.base_not_network);
                }
            }
        });


    }

    private void cancelEndorseToServer(){

        HttpRequestManager.getInstance().trainCancelEndorse(mOrderResponseInfo.getOriginalOrderId(), orderNo, new ApiSubscriber<JsonResult<EndorseFailureModel>>(TrainOrderDetailsActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<EndorseFailureModel> result) {
                //dismissLoading();
                if ("0000".equals(result.getResultCode())) {
                    // 取消改签成功
                   // showLoading();
                    getTrainOrderDetails();

                } else {
                    ToastManager.getInstance().showToast(TrainOrderDetailsActivity.this, result.getResultMsg());
                }
            }
        });
    }

    private void deleteOrderToServer(){
        HttpRequestManager.getInstance().trainDeleteOrder(mOrderResponseInfo.getOrderId(), new ApiSubscriber<JsonResult<TrainOrderdetailsResponseModel>>(TrainOrderDetailsActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<TrainOrderdetailsResponseModel> result) {
                if ("0000".equals(result.getResultCode())) {
                    ToastManager.getInstance().showToast(TrainOrderDetailsActivity.this, "删除成功！");
                   // RxBus.getInstance().post(new RefreshOrderListEvent(HCPDD));

                    finish();
                } else {
                    ToastManager.getInstance().showToast(TrainOrderDetailsActivity.this, result.getResultMsg());
                }
            }
        });
    }

    private void cancelOrderToServer(){

        HttpRequestManager.getInstance().trainCancelOrder(mOrderResponseInfo.getOrderId(), new ApiSubscriber<JsonResult<CancelOrderModel>>(TrainOrderDetailsActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<CancelOrderModel> result) {
                //dismissLoading();
                if ("0000".equals(result.getResultCode())) {
                    // 取消成功
                    //showLoading();
                    getTrainOrderDetails();
                } else {
                    ToastManager.getInstance().showToast(TrainOrderDetailsActivity.this, result.getResultMsg());
                }
            }
        });
    }

    @Subscribe
    public void refresh(CommonEvent event){
        if(event.getFlag() == CommonEvent.REFUND_ENDORSE_SUCCESS){
            //showLoading();
            getTrainOrderDetails();
        }
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            bttraindeatilsbtright.setEnabled(false);
            tv_tips_info.setText("抱歉，您的操作已超时，请重新购票。");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            long minute = millisUntilFinished / (1000 * 60);
            long second = (millisUntilFinished / 1000) % 60;
            if (mOrderType == 1) {
                tv_tips_info.setText("已为您抢票成功，请在" + minute + "分" + second + "秒内完成支付");
            } else {
                tv_tips_info.setText("改签锁票成功，请在" + minute + "分" + second + "秒之前确认改签出票");
            }

        }

    }

    @Override
    public void clickReload() {
        getTrainOrderDetails();
    }
}