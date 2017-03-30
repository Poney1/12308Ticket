package com.wxws.myticket.bus.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.BaocheTypeAdapter;
import com.wxws.myticket.bus.entity.BaocheBusType;
import com.wxws.myticket.bus.entity.BaocheNoticeEntity;
import com.wxws.myticket.bus.entity.BaochePriceRequestPara;
import com.wxws.myticket.bus.entity.BaochePriceResponse;
import com.wxws.myticket.bus.entity.BaocheSubmitRequest;
import com.wxws.myticket.bus.entity.BaocheSubmitResponse;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.my.Entity.CommonEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeRentCar;

/**
 * desc: 包车选择车次页面
 * Date: 2016-11-18 14:19
 *
 * @author jiangyan
 */
public class BaocheSelectCarActivity extends BaseActivity implements OnClickListener {

    private ListViewForScrollView listView;
    private BaocheTypeAdapter madapter;
    private RadioButton radio_yes;
    private RadioButton radio_no;
    private RadioButton radio_single;
    private RadioButton radio_round;
    private LinearLayout invoiceLayout;
    private TextView tvEndTime;
    private TextView tvdistribute;
    private TextView rent_amount;
    private TextView pay_btn;
    private final String SELECT_DAY_TAG = "RentCarFrag";
    private String userName, userPhone, startTime, endTime, startPlace, endPlace, startCity, endCity;
    private int userAcount;
    private int personAcount;
    private int roundState = 0;
    private int invoiceState = 0;
    TimePickerView pvTime;
    OptionsPickerView pvOptions;
    private EditText etinvoiceHead, etinvoiceConsignee, etinvoiceAddress;
    private String invoiceHead = null;
    private String invoiceConsignee = null;
    private String invoiceAddress = null;
    private String startLat;
    private String startLng;
    private String endLat;
    private String endLng;
    private AlertDialog rentBusDialog;
    private LinearLayout layoutQuan;
    private TextView tvQuan;

    private String totalFee;
    double price;
    String beforeHour;
    String orderMinutes;
    String phone;
    private String couponCode;
    private int seatCount;
    private int remainCount;
    private String bookTicketDisco;

    private List<BaocheBusType> list = new ArrayList<>();
    private List<BaochePriceRequestPara> mPriceList = new ArrayList<>();
    private int mTotalPrice;
    private BaocheSubmitRequest mRequestPara;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_rent_car);
        RxBus.getInstance().register(this);

        getUserInfo();
        getDataFromPre();

        bindTitleViews();
        bindLoadingView();
        showLoading();

        initView();
        getBusType();
        getRentInfo();
        timePicker();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void getDataFromPre() {
        userName = getIntent().getStringExtra("userName");
        userPhone = getIntent().getStringExtra("userPhone");
        startTime = getIntent().getStringExtra("startTime");
        startPlace = getIntent().getStringExtra("startPlace");
        endPlace = getIntent().getStringExtra("endPlace");
        startCity = getIntent().getStringExtra("startCity");
        endCity = getIntent().getStringExtra("endCity");
        startLat = getIntent().getStringExtra("startLat");
        startLng = getIntent().getStringExtra("startLng");
        endLat = getIntent().getStringExtra("endLat");
        endLng = getIntent().getStringExtra("endLng");
        userAcount = getIntent().getIntExtra("userAcount", 0);
    }

    private void initView() {

        personAcount = userAcount;
        barTitle.setText("包车详情");

        listView = (ListViewForScrollView) findViewById(R.id.list_income);
        invoiceLayout = (LinearLayout) findViewById(R.id.invoice_layout);
        layoutQuan = (LinearLayout) findViewById(R.id.layout_coupon);
        tvQuan = (TextView) findViewById(R.id.tv_coupon);
        etinvoiceHead = (EditText) findViewById(R.id.invoice_head);
        etinvoiceConsignee = (EditText) findViewById(R.id.invoice_consignee);
        etinvoiceAddress = (EditText) findViewById(R.id.invoice_address);
        radio_yes = (RadioButton) findViewById(R.id.radio_yes);
        radio_no = (RadioButton) findViewById(R.id.radio_no);
        radio_round = (RadioButton) findViewById(R.id.radio_round);
        radio_single = (RadioButton) findViewById(R.id.radio_single);
        tvdistribute = (TextView) findViewById(R.id.rent_info);
        rent_amount = (TextView) findViewById(R.id.rent_amount);
        pay_btn = (TextView) findViewById(R.id.pay_btn);
        tvEndTime = (TextView) findViewById(R.id.user_time);
        tvEndTime.setText(DateTimeUtil.getDate(DateTimeUtil.addDay(DateTimeUtil.StringToDate(startTime, "yyyy-MM-dd HH:mm"), 1), "yyyy-MM-dd HH:mm"));

        madapter = new BaocheTypeAdapter(BaocheSelectCarActivity.this);
        listView.setAdapter(madapter);

        madapter.setOnCountChangeListener(new BaocheTypeAdapter.OnCountChangeListener() {

            @Override
            public void countChange() {
                calculateBusRentPrice();
            }
        });
        pay_btn.setOnClickListener(this);
        radio_no.setOnClickListener(this);
        radio_single.setOnClickListener(this);
        layoutQuan.setOnClickListener(this);
        radio_no.setChecked(true);
        radio_single.setChecked(true);


    }

    private void getBusType() {
        HttpRequestManager.getInstance().getBaocheType(userAcount + "", new ApiSubscriber<JsonResult<List<BaocheBusType>>>(BaocheSelectCarActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<List<BaocheBusType>> result) {
                if ("0000".equals(result.getResultCode())) {

                    if (result.getObject().size() > 0 && result.getObject() != null) {
                        dismissLoading();
                        list.clear();
                        list.addAll(result.getObject());

                        initData();
                    } else {
                        showErrorOrNoData("", R.mipmap.base_message_order_null);
                    }

                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network, true);
                }
            }
        });
    }

    private void initData() {

        ComUp cu = new ComUp();
        Collections.sort(list, cu);//list按车型从小到大排序
        if (list != null && list.size() > 0) {
            for (int i = list.size() - 1; i >= 0; i--) {
                BaocheBusType bt = list.get(i);
                seatCount += bt.getSeat() * bt.getRecommendCount();
            }
            remainCount = seatCount - userAcount;
            if (seatCount >= userAcount) {
                tvdistribute.setText("已分配" + userAcount + "人,多余" + (seatCount - userAcount) + "个座位");
            } else {
                tvdistribute.setText("已分配" + seatCount + "人,待分配" + (userAcount - seatCount) + "人");
            }
            madapter.refreshData(list);

        } else {
            tvdistribute.setVisibility(View.GONE);
        }

        //是否需要发票
        radio_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    invoiceLayout.setVisibility(View.VISIBLE);
                    invoiceState = 1;

                } else {
                    invoiceLayout.setVisibility(View.GONE);
                    invoiceState = 0;
                }
            }
        });
        //是否往返
        radio_round.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tvEndTime.setVisibility(View.VISIBLE);
                    roundState = 1;
                    endTime = tvEndTime.getText().toString() + ":00";
                    calculateBusRentPrice();
                } else {
                    tvEndTime.setVisibility(View.GONE);
                    roundState = 0;
                    endTime = null;
                    calculateBusRentPrice();
                }
            }
        });
        calculateBusRentPrice();
    }

    /**
     * 包车价格计算
     */

    private void calculateBusRentPrice() {

        int person = 0;
        mPriceList.clear();
        Iterator<BaocheBusType> it = list.iterator();
        while (it.hasNext()) {
            BaocheBusType bt = it.next();
            if (bt.getRecommendCount() != 0) {
                BaochePriceRequestPara para = new BaochePriceRequestPara();
                para.setBusId(bt.getBusId());
                para.setBusCount(bt.getRecommendCount());
                mPriceList.add(para);
            }
            person += bt.getRecommendCount() * bt.getSeat();
        }
        remainCount = person - personAcount;
        if (person < personAcount) {
            tvdistribute.setText("已分配" + person + "人,待分配" + (personAcount - person) + "人");
        } else {
            tvdistribute.setText("已分配" + personAcount + "人,多余" + (person - personAcount) + "个座位");
        }

        if (mPriceList.size() == 0) {
            ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "车辆信息不能为空");
            return;
        }

        String busInfoList = JSON.toJSONString(mPriceList);
        if (couponCode == null) {
            couponCode = "";
        }
        if (endTime == null) {
            endTime = "";
        }

        initPara();
        HttpRequestManager.getInstance().getBaochePrice(mRequestPara, new ApiSubscriber<JsonResult<BaochePriceResponse>>(BaocheSelectCarActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<BaochePriceResponse> result) {
                if ("0000".equals(result.getResultCode())) {

                    mTotalPrice = result.getObject().getMoney();
                    totalFee = result.getObject().getMoney() + "";
                    rent_amount.setText(totalFee);

                } else {
                    rent_amount.setText("0");
                }
            }
        });
    }

    //弹窗
    private void showDialog() {
        rentBusDialog = new AlertDialog.Builder(this).create();
        rentBusDialog.show();
        rentBusDialog.getWindow().setContentView(R.layout.dialog_rent_info);
        TextView tip1 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_1);
        TextView tip2 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_2);
        TextView tip3 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_3);
        TextView tip4 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_4);
        TextView tip5 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_5);
        TextView tip6 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_6);
        TextView tip7 = (TextView) rentBusDialog.getWindow().findViewById(R.id.tip_7);
        tip1.setText("包车需求需要距发车时间提前至少" + beforeHour + "小时完成支付，节假日顺延；");
        tip3.setText("订单支付时间为" + orderMinutes + "分钟, " + orderMinutes + "分钟未支付即自动取消；");
        tip7.setText("客服电话：" + phone);
        tip7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        tip2.setText("包车订单一旦支付完成，原则上将不接受退款，具体请咨询客服；");
        tip4.setText("包车协议由用户与巴士服务商自行签署；");
        tip5.setText("支付价格包含全部用车费用（车辆使用费、油费、路桥费、司机工酬等）；");
        tip6.setText("开具发票事宜请咨询客服；");
        rentBusDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        rentBusDialog.getWindow().findViewById(R.id.confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isFastDoubleClick()) {
                            confirmRentBusOrder();
                        }
                        rentBusDialog.dismiss();
                    }
                });
        rentBusDialog.getWindow().findViewById(R.id.rent_close)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rentBusDialog.dismiss();
                    }
                });
        rentBusDialog.getWindow().findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rentBusDialog.dismiss();
                    }
                });

    }

    //需要发票验证
    private boolean getVerify() {
        invoiceHead = etinvoiceHead.getText().toString();
        invoiceConsignee = etinvoiceConsignee.getText().toString();
        invoiceAddress = etinvoiceAddress.getText().toString();
        if (invoiceHead.equals("")) {
            ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "发票抬头不能为空!");
            return false;
        }
        if (invoiceConsignee.equals("")) {
            ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "收件人不能为空!");
            return false;
        }
        if (invoiceAddress.equals("")) {
            ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "收件地址不能为空!");
            return false;
        }

        return true;
    }

    private boolean getroundverify() {
        if (tvEndTime.getText().toString().equals("")) {
            ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "往返时间不能为空!");
            return false;
        }
        return true;
    }


    //获取服务说明接口
    private void getRentInfo() {
        HttpRequestManager.getInstance().getBaocheNotice(new ApiSubscriber<JsonResult<BaocheNoticeEntity>>(BaocheSelectCarActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<BaocheNoticeEntity> result) {
                if (!result.getObject().equals("")) {
                    beforeHour = result.getObject().getBeforeHour() + "";
                    orderMinutes = result.getObject().getOrderMinutes() + "";
                    phone = result.getObject().getPhone();
                }
            }
        });
    }

    //时间选择控件
    private void timePicker() {
        //时间选择器
        pvTime = new TimePickerView(BaocheSelectCarActivity.this, TimePickerView.Type.ALL);
        pvTime.setRange(2016, 2020);
        pvTime.setCyclic(true);
        pvTime.setTime(DateTimeUtil.addDay(DateTimeUtil.StringToDate(startTime, "yyyy-MM-dd HH:mm"), 1));
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (DateTimeUtil.compareTo(DateTimeUtil.getDate(date, "yyyy-MM-dd HH:mm"), startTime)) {
                    tvEndTime.setText(DateTimeUtil.getDate(date, "yyyy-MM-dd HH:mm"));
                    endTime = tvEndTime.getText().toString() + ":00";
                    calculateBusRentPrice();
                } else {
                    ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "往返时间需大于用车时间");
                }
            }
        });
        //弹出时间选择器
        tvEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime.show();
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tvEndTime.getWindowToken(), 0);
            }
        });

        //选项选择器
        pvOptions = new OptionsPickerView(this);
    }

    private void initPara(){
        mRequestPara = new BaocheSubmitRequest();
        mRequestPara.setBusInfo(JSON.toJSONString(mPriceList));
        mRequestPara.setContactName(userName);
        mRequestPara.setContactMobile(userPhone);
        mRequestPara.setIsGoBack(roundState + "");
        mRequestPara.setGoBackTime(endTime == null ? "" : endTime);
        mRequestPara.setUseTime(startTime+":00");//yyyy-MM-DD HH:mm:ss格式
        mRequestPara.setFromPlace(startPlace);
        mRequestPara.setToPlace(endPlace == null ? "" : endPlace);
        mRequestPara.setPersonCount(personAcount + "");
        mRequestPara.setIsInvoice(invoiceState + "");
        mRequestPara.setInvoiceTitle(invoiceHead == null ? "" : invoiceHead);
        mRequestPara.setReceiver(invoiceConsignee == null ? "" : invoiceConsignee);
        mRequestPara.setReceiverAddr(invoiceAddress == null ? "" : invoiceAddress);
        mRequestPara.setOrderMoney(mTotalPrice + "");
        mRequestPara.setCouponId(couponCode == null ? "" : couponCode);
        mRequestPara.setFromLatitude(startLat);
        mRequestPara.setFromLongitude(startLng);
        mRequestPara.setToLatitude(endLat);
        mRequestPara.setToLongitude(endLng);
        mRequestPara.setFromCityName(startCity);
        mRequestPara.setToCityName(endCity);
    }

    /**
     * 包车下单
     */
    private void confirmRentBusOrder() {

        initPara();
        HttpRequestManager.getInstance().BaocheSubmitOrder(mRequestPara, new ApiSubscriber<JsonResult<BaocheSubmitResponse>>(BaocheSelectCarActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<BaocheSubmitResponse> result) {
                if ("0000".equals(result.getResultCode())) {

                    if (result.getObject().getOrderId() != null) {
                        Log.e("mudo", "what?" + result.getObject().getOrderId());
                        Intent intent = new Intent(BaocheSelectCarActivity.this, PayActivity.class);
                        intent.putExtra("orderId", result.getObject().getOrderId());
                        intent.putExtra("bizType", bizTypeRentCar);
                        RxBus.getInstance().post(new CommonEvent(CommonEvent.CREATE_WYBC_ORDER));
                        //TODO 提示用户有未支付订单。
//                      userInfo.setRentState(true);
//                      new BaseConfig<UserInfo>(RentSelectCarActivity.this, BaseConfig.USERINFO).updateObject(userInfo);
                        startActivityWithAnim(intent);
                        finish();
                    }
                }

                ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, result.getResultMsg());

            }
        });
    }

    /**
     * TODO 获取优惠券
     */
    private void loadCoupon() {

//        new WebAPI().getNewUsableCoupons(userInfo.getUserId(), userInfo.getMobilePhone(), "APP", String.valueOf(GlobarVar.COUPONTYPE_WYBC),startCity
//                , "","", rent_amount.getText().toString(),dialogTag, netErrorLisenterTag,
//                new ResponseFactory<ArrayList<CouponEntity>>(new TypeReference<WebResult<ArrayList<CouponEntity>>>() {
//                }) {
//                    @Override
//                    public void parseResponse(WebResult<ArrayList<CouponEntity>> response) {
//                        if ("0000".equals(response.getResultCode())) {
//                            mCouponEntityArrayList = response.getObject();
//                            if (mCouponEntityArrayList != null && mCouponEntityArrayList.size() > 0) {
//                                Intent intent = new Intent(RentSelectCarActivity.this, CouponActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putBoolean("isUser", true);//是否使用优惠券
//                                bundle.putSerializable("Coupons", mCouponEntityArrayList);
//                                intent.putExtras(bundle);
//                                startActivityWithAnim(intent);
//                            } else {
//                                CommonUtil.showToast(RentSelectCarActivity.this, "没有可使用的优惠券");
//                            }
//                        }
//                    }
//                });
    }

    // TODO 接收到优惠券选择事件，后续可能修改。
//    public void onEvent(SelectCouponEvent event) {
//        mCouponEntity = event.getCouponEntity();
//        //计算总价格
//        setTotalStyle();
//    }
//
//    private void setTotalStyle() {
//
//        String allPrice = price+"";
//        if (mCouponEntity != null) {
//            if ("2".equals(mCouponEntity.getType()) && !StringUtils.isNullOrEmpty(mCouponEntity.getCouponDisctVal())) {
//                String disco = StringUtils.sub("100", mCouponEntity.getCouponDisctVal());
//                String realDisco = StringUtils.mul(disco, "0.01");
//                String remain = StringUtils.mul(realDisco, allPrice);
//                if (Double.parseDouble(StringUtils.sub(remain, mCouponEntity.getDisctMaxAmt())) > 0) {
//                    totalFee = StringUtils.sub(allPrice, mCouponEntity.getDisctMaxAmt());
//                    tvQuan.setText(String.format("%s元", mCouponEntity.getDisctMaxAmt()));
//                    bookTicketDisco = mCouponEntity.getDisctMaxAmt();
//                } else {
//                    totalFee = StringUtils.sub(allPrice, remain);
//                    bookTicketDisco = remain;
//                    tvQuan.setText(String.format("%s折", StringUtils.mul(mCouponEntity.getCouponDisctVal(), "0.1")));
//                }
//                if (Double.parseDouble(totalFee) > 0) {
//                    rent_amount.setText(totalFee);
//                } else {
//                    rent_amount.setText(0 + "");
//                    bookTicketDisco = allPrice;
//                }
//
//            } else if (!StringUtils.isNullOrEmpty(mCouponEntity.getPrice())
//                    && Double.parseDouble(StringUtils.sub(allPrice, mCouponEntity.getLimitUsePrice())) >= 0) {
//                totalFee = StringUtils.doubleTrans((Double.parseDouble(allPrice) - Double.parseDouble(mCouponEntity.getPrice())));
//                tvQuan.setText(mCouponEntity.getPrice() + "元");
//                if (Double.parseDouble(totalFee) > 0) {
//                    bookTicketDisco = mCouponEntity.getPrice();
//                    rent_amount.setText(totalFee);
//                } else {
//                    rent_amount.setText(0 + "");
//                    bookTicketDisco = allPrice;
//                }
//            } else {
//                bookTicketDisco = "0";
//                totalFee = allPrice;
//                tvQuan.setText("不使用优惠券");
//                rent_amount.setText(StringUtils.doubleTrans(totalFee));
//            }
//        } else {
//            bookTicketDisco = "0";
//            totalFee = allPrice;
//            tvQuan.setText("不使用优惠券");
//            rent_amount.setText(StringUtils.doubleTrans(totalFee));
//        }
//    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.radio_yes:
                if (radio_yes.isChecked()) {
                    invoiceLayout.setVisibility(View.VISIBLE);
                    invoiceState = 1;
                } else {
                    invoiceLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.radio_no:
                invoiceState = 0;
                break;
            case R.id.radio_single:
                roundState = 0;
                break;
            case R.id.layout_coupon:
//                if (mCouponEntityArrayList == null || mCouponEntityArrayList.size() == 0) {
//                    loadCoupon();
//                } else {
//                    Intent intent = new Intent(this, CouponActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("isUser", true);//是否使用优惠券
//                    bundle.putSerializable("Coupons", mCouponEntityArrayList);
//                    intent.putExtras(bundle);
//                    startActivityWithAnim(intent);
//                }

                Intent intent = new Intent(BaocheSelectCarActivity.this, CouponSelectActivity.class);
                intent.putExtra("orderPrice", rent_amount.getText().toString());
                intent.putExtra("bizType", bizTypeRentCar+"");
                startActivityWithAnim(intent);
                Log.e("mudo", "跳转");
                break;

            case R.id.pay_btn:
                if (remainCount >= 0) {
                    if (radio_yes.isChecked()) {
                        if (getVerify()) {
                            showDialog();
                        } else {
                            return;
                        }
                    } else if (radio_round.isChecked()) {
                        if (getroundverify()) {
                            showDialog();
                        } else {
                            return;
                        }
                    } else if (!radio_round.isChecked() && !radio_yes.isChecked()) {
                        showDialog();
                    } else {
                        rentBusDialog.dismiss();
                    }
                } else {
                    ToastManager.getInstance().showToast(BaocheSelectCarActivity.this, "您还有人数未分配完");
                }

                break;

        }

    }

    public boolean isFastDoubleClick() {
        long lastClickTime = 0;
        long time = System.currentTimeMillis();
        if (time < lastClickTime) {// 防止调手机时间到过去导至点击失效
            lastClickTime = 0;
        }
        if (time - lastClickTime < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 排序类
     */
    class ComUp implements Comparator<BaocheBusType> {
        public int compare(BaocheBusType i1, BaocheBusType i2) {//实现compare方法
            if (i1.getSeat() > i2.getSeat()) return 1;//控制顺序排列
            else if (i1.getSeat() < i2.getSeat()) return -1;
            else return 0;
        }
    }

}
