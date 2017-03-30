package com.wxws.myticket.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Text;
import com.bigkoo.pickerview.OptionsPickerView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.FillPassengerAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.BusInsuranceEntity;
import com.wxws.myticket.bus.entity.FillOrderDescriptionEntity;
import com.wxws.myticket.bus.entity.LineDetailEntity;
import com.wxws.myticket.bus.entity.OrderIdEntity;
import com.wxws.myticket.bus.interfaces.OnAdultSelectListener;
import com.wxws.myticket.bus.popupwindow.BookFillOrderPopWindow;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.CheckSwitchButton;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.my.Entity.CouponResponsePara;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.activity.CouponActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 直通车填写订单
 * Date: 2016/11/4 15:37
 *
 * @auther: lixiangxiang
 */
public class DirectFillInOrderActivity extends BaseActivity {

    /**
     * 很长
     */
    private TextView tvArriveTime;
    private TextView tvDate;
    private TextView tvWeek;
    private TextView tvStartCity;
    private TextView tvStartStation;
    private TextView tvEndCity;
    private TextView tvEndStation;
    private TextView tvBusType;
    private TextView tvFillPrice;
    private TextView tvFee;
    private LinearLayout llNotNeedName;
    private RelativeLayout selectTicketType;
    private RelativeLayout rlHalf;
    private RelativeLayout rlXietong;
    private EditText asvFullPrice;
    private EditText asvHalfPrice;
    private EditText asvXietong;
    private EditText etPhone;
    private LinearLayout llAddPassenger;
    private LinearLayout llPhone;
    private ListViewForScrollView listFill;
    private RelativeLayout layoutPicker;
    private TextView tvTicketPeople;
    private TextView tvBackup;
    private RelativeLayout rlBackup;
    private CheckSwitchButton csbBackup;
    private TextView tvCoupon;
    private TextView tvInsurancePrice;
    private TextView tvInsuranceDesc;
    private RelativeLayout rlInsurance;
    private TextView tvTotalPrice;
    private TextView tvTicketCount;
    private TextView tvShowDesc;
    private TextView btnPay;
    private RelativeLayout rlShowDesc;
    private LinearLayout llNeedName;

    //价格详情
    private BookFillOrderPopWindow bookPopwindow;
    private FillOrderDescriptionEntity fillDescEntity;
    private boolean showDesc = false;
    private LineDetailEntity mDetail;
    private TextView tvShowType;
    private LinearLayout llTicketType;
    private String bizType;
    private ArrayList<BusInsuranceEntity> insurance;
    private BusInsuranceEntity isRecommend;
    //联系人
    private ArrayList<PassengerInfoEntity> listPassenger;
    private ArrayList<PassengerInfoEntity> selectList;
    private FillPassengerAdapter fillPassengerAdapter;
    private OptionsPickerView halfTicket;
    private int selectPosition;
    private ArrayList<String> ticketList;
    private PassengerInfoEntity person;
    private LinearLayout llUseCoupon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_fill);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void getPreData() {
        mDetail = (LineDetailEntity) getIntent().getSerializableExtra("lineDetail");
        bizType = getIntent().getStringExtra("bizType");
    }

    private void bindClick() {

        btnPay.setOnClickListener(this);
        layoutPicker.setOnClickListener(this);
        tvShowDesc.setOnClickListener(this);
        selectTicketType.setOnClickListener(this);
        rlShowDesc.setOnClickListener(this);
        tvShowType.setOnClickListener(this);
        llAddPassenger.setOnClickListener(this);
        layoutPicker.setOnClickListener(this);
        tvCoupon.setOnClickListener(this);
    }

    private void initData() {
        barTitle.setText("填写订单");

        //价格详情
        bookPopwindow = new BookFillOrderPopWindow(DirectFillInOrderActivity.this);
        fillDescEntity = new FillOrderDescriptionEntity();
        bookPopwindow.setDataForPop(fillDescEntity);
        bookPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {


            @Override
            public void onDismiss() {
                showDesc = false;
                tvShowDesc.setSelected(showDesc);
            }

        });
        bindViewData();

        initPassenger();

        initPickView();

        getInsurance();
    }

    private void initPassenger() {
        fillPassengerAdapter = new FillPassengerAdapter(DirectFillInOrderActivity.this, listPassenger);
        listFill.setAdapter(fillPassengerAdapter);
        fillPassengerAdapter.setOnAdultSelectListener(new OnAdultSelectListener() {
            @Override
            public void onSelectEnd(int position, String adult) {
                selectPosition = position;
                halfTicket.show();
            }
        });
    }

    //成人票半价票选择
    private void initPickView() {
        ticketList = new ArrayList<>();
        halfTicket = new OptionsPickerView(DirectFillInOrderActivity.this);
        if ("1".equals(mDetail.getIsSupportHalfTicket()))
            ticketList.add(getString(R.string.bookTicket_half));
        halfTicket.setPicker(ticketList);
        halfTicket.setCyclic(false);
        halfTicket.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                if (getString(R.string.bookTicket_all).equals(ticketList.get(i))) {
                    listPassenger.get(selectPosition).setTicketType(Constants.busAllTicket);
                } else if (getString(R.string.bookTicket_half).equals(ticketList.get(i))) {
                    listPassenger.get(selectPosition).setTicketType(Constants.busHalfTicket);
                }
                fillPassengerAdapter.replaceAll(listPassenger);
            }
        });
    }

    /**
     * 绑定数据
     */
    private void bindViewData() {
        selectList = new ArrayList<>();

        showNeedName();
        tvDate.setText(String.format("%s %s", mDetail.getStartDate(), mDetail.getStartTime()));
        tvWeek.setText(DateTimeUtil.getWeekStr(mDetail.getStartDate()));
        tvStartCity.setText(mDetail.getStartCityName());
        tvStartStation.setText(mDetail.getStartStationName());
        tvEndCity.setText(mDetail.getEndCityName());
        tvEndStation.setText(mDetail.getEndStationName());
        tvBusType.setText(mDetail.getVehicleType());
        tvFillPrice.setText(String.valueOf(ArithUtils.div(mDetail.getPrice().getDiscountPrice(), 100.00)));
        if (-1 == mDetail.getServiceFee()) {
            tvFee.setVisibility(View.GONE);
        } else {
            tvFee.setText(String.format(getString(R.string.direct_fill_service), ArithUtils.div(mDetail.getServiceFee(), 100.00) + ""));
        }

        if ("1".equals(mDetail.getIsSupportHalfTicket())) {
            rlHalf.setVisibility(View.VISIBLE);
        } else {
            rlHalf.setVisibility(View.GONE);
        }
        if ("1".equals(mDetail.getIsSupportChildTicket())) {
            rlXietong.setVisibility(View.VISIBLE);
        } else {
            rlXietong.setVisibility(View.GONE);
        }
        tvArriveTime.setText(String.format(getString(R.string.bookTicket_show_time), getHeaderShowTime()));

        if ("1".equals(mDetail.getIsBackCheci())) {
            csbBackup.setChecked(false);
        } else {
            csbBackup.setChecked(true);
        }
        if ("1".equals(mDetail.getIsInsurance())) {
            rlInsurance.setVisibility(View.VISIBLE);
        } else {
            rlInsurance.setVisibility(View.GONE);
        }

        if ("1".equals(mDetail.getIsCoupon())){
            llUseCoupon.setVisibility(View.VISIBLE);
        }else {
            llUseCoupon.setVisibility(View.GONE);
        }
        calcPrice();
    }

    private void initView() {
        bindTitleViews();

        //剩余时间
        tvArriveTime = (TextView) findViewById(R.id.tv_arrive_time);
        //日期
        tvDate = (TextView) findViewById(R.id.tv_date);
        //周几
        tvWeek = (TextView) findViewById(R.id.tv_week);
        //出发城市
        tvStartCity = (TextView) findViewById(R.id.tv_start_city);
        //出发站点
        tvStartStation = (TextView) findViewById(R.id.tv_start_station);
        //到达城市
        tvEndCity = (TextView) findViewById(R.id.tv_end_city);
        //到达站点
        tvEndStation = (TextView) findViewById(R.id.tv_end_station);
        //车类型
        tvBusType = (TextView) findViewById(R.id.tv_bus_type);
        //现价
        tvFillPrice = (TextView) findViewById(R.id.tv_fill_price);
        //服务费
        tvFee = (TextView) findViewById(R.id.tv_fee);

        //正常购票
        //不需要实名
        llNotNeedName = (LinearLayout) findViewById(R.id.ll_not_need_name);
        //显示票
        llTicketType = (LinearLayout) findViewById(R.id.ll_ticket_type);
        //使用优惠券
        llUseCoupon = (LinearLayout) findViewById(R.id.ll_use_coupon);
        //选择票类型
        selectTicketType = (RelativeLayout) findViewById(R.id.layout_select_ticket_type);
        //展示半价票
        rlHalf = (RelativeLayout) findViewById(R.id.rl_half);
        //展示协同票
        rlXietong = (RelativeLayout) findViewById(R.id.rl_xietong);
        //全价票
        asvFullPrice = (EditText) findViewById(R.id.asv_full_price);
        //半价票
        asvHalfPrice = (EditText) findViewById(R.id.asv_half_price);
        //协同票
        asvXietong = (EditText) findViewById(R.id.asv_xietong);
        //取票手机号码
        etPhone = (EditText) findViewById(R.id.et_phone);

        //实名购票
        llNeedName = (LinearLayout) findViewById(R.id.ll_need_name);
        //添加乘客
        llAddPassenger = (LinearLayout) findViewById(R.id.ll_add_passenger);
        //是否显示 号码
        llPhone = (LinearLayout) findViewById(R.id.ll_phone);
        //乘客数量
        listFill = (ListViewForScrollView) findViewById(R.id.list_fill);
        //选择取票人
        layoutPicker = (RelativeLayout) findViewById(R.id.layout_picker);
        //取票人姓名
        tvTicketPeople = (TextView) findViewById(R.id.tv_ticket_people);
        //提示前后更换俩小时内的车次
        tvBackup = (TextView) findViewById(R.id.tv_backup);
        //是否显示备用车次
        rlBackup = (RelativeLayout) findViewById(R.id.rl_backup);
        //d打开开关
        csbBackup = (CheckSwitchButton) findViewById(R.id.csb_backup);
        //选择优惠券
        tvCoupon = (TextView) findViewById(R.id.tv_coupon);
        //保险价格
        tvInsurancePrice = (TextView) findViewById(R.id.tv_insurance_price);
        //保险描述
        tvInsuranceDesc = (TextView) findViewById(R.id.tv_insurance_desc);
        //是否展示保险
        rlInsurance = (RelativeLayout) findViewById(R.id.rl_insurance);
        //展示详情
        rlShowDesc = (RelativeLayout) findViewById(R.id.rl_show_desc);
        //车次类型
        tvShowType = (TextView) findViewById(R.id.tv_show_type);

        //支付总价
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        //人数
        tvTicketCount = (TextView) findViewById(R.id.tv_ticket_count);
        //展示 价格构成
        tvShowDesc = (TextView) findViewById(R.id.tv_show_desc);
        //支付
        btnPay = (TextView) findViewById(R.id.btn_pay);
    }

    //提交订单
    private void submitOrder() {
        String scheduleId = mDetail.getScheduleId();
        String fromCityId = mDetail.getFromStationId();
        String toCityId = mDetail.getToStationId();
        String isBack = mDetail.getIsBackCheci();
        String couponId;
        if (couponRecieve != null) {
            couponId = couponRecieve.getCode();
        } else {
            couponId = "";
        }
        String adultCount = "0";
        String halfCountS = "0";
        String childCountS = "0";
        String passenger = "[]";
        String contactName = "";
        String contactNo = "";
        String contactId = "";
        String contactMobile = "";

        if ("1".equals(mDetail.getIsRealName())) {
            adultCount = String.valueOf(fullCount);
            halfCountS = String.valueOf(halfCount);
            childCountS = asvXietong.getText().toString().trim();
            StringBuilder sb = new StringBuilder();
            if (listPassenger != null && listPassenger.size() > 0) {
                sb.append("[");
                int j = 0;
                for (PassengerInfoEntity info : listPassenger) {
                    j++;
                    if (j != listPassenger.size())
                        sb.append(String.format("{\"id\":%s,\"ticketType\":%s,\"insuranceId\":\"%s\"},", info.getPassengerId(), info.getTicketType(), isRecommend == null ? "" : isRecommend.getInsuranceId()));
                    else
                        sb.append(String.format("{\"id\":%s,\"ticketType\":%s,\"insuranceId\":\"%s\"}", info.getPassengerId(), info.getTicketType(), isRecommend == null ? "" : isRecommend.getInsuranceId()));
                }
                sb.append("]");
            } else {
                ToastManager.getInstance().showToast(DirectFillInOrderActivity.this, getString(R.string.bookTicket_add));
                return;
            }
            passenger = sb.toString();
            if (person != null) {
                contactName = person.getPassengerName();
                contactNo = person.getCertificateNo();
                contactId = person.getPassengerId();
                contactMobile = person.getMobilePhone();
            } else {
                ToastManager.getInstance().showToast(DirectFillInOrderActivity.this, getString(R.string.bookTicket_person));
                return;
            }
        } else {
            adultCount = asvFullPrice.getText().toString().trim();
            halfCountS = asvHalfPrice.getText().toString().trim();
            childCountS = asvXietong.getText().toString().trim();
            contactMobile = etPhone.getText().toString().trim();
            if ("0".equals(adultCount) && "0".equals(halfCountS) && "0".equals(childCountS)){
                ToastManager.getInstance().showToast(DirectFillInOrderActivity.this,"请至少购买一张票！");
                return;
            }
            if (StringUtils.isNullOrEmpty(contactMobile)){
                ToastManager.getInstance().showToast(DirectFillInOrderActivity.this,"联系电话不能为空！");
                return;
            }
        }

        String orderMoney = String.valueOf(ArithUtils.mul(Double.parseDouble(priceShow), 100));
        String openFreeInsurance = mDetail.getOpenFreeInsurance();
        String bizTypes = mDetail.getBizType();
        String isRealName = mDetail.getIsRealName();
        HttpRequestManager.getInstance().submitOrder(mDetail.getStartDate(), scheduleId, fromCityId, toCityId, isBack, couponId, adultCount,
                childCountS, halfCountS, passenger, contactName, contactNo, contactId, contactMobile, orderMoney, openFreeInsurance, isRealName,
                bizTypes, new ApiSubscriber<JsonResult<OrderIdEntity>>(DirectFillInOrderActivity.this) {
                    @Override
                    public void onNext(JsonResult<OrderIdEntity> stringJsonResult) {
                        if ("0000".equals(stringJsonResult.getResultCode())) {
                            if (!StringUtils.isNullOrEmpty(stringJsonResult.getObject().getOrderId())) {
                                //提交订单
                                Intent intent = new Intent(DirectFillInOrderActivity.this, PayActivity.class);
                                intent.putExtra("orderId", stringJsonResult.getObject().getOrderId());
                                intent.putExtra("bizType", mDetail.getBizType());
                                DirectFillInOrderActivity.this.startActivityWithAnim(intent);
                            } else {
                                ToastManager.getInstance().showToast(DirectFillInOrderActivity.this, stringJsonResult.getResultMsg());
                            }
                        } else {
                            ToastManager.getInstance().showToast(DirectFillInOrderActivity.this, stringJsonResult.getResultMsg());
                        }
                    }
                });
    }


    //显示保险
    private void getInsurance() {
        HttpRequestManager.getInstance().busInsuranceList(mDetail.getScheduleId(), mDetail.getBizType(),
                mDetail.getFromStationId(), mDetail.getToStationId(), mDetail.getStartDate(),
                new ApiSubscriber<JsonResult<List<BusInsuranceEntity>>>(DirectFillInOrderActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
                    @Override
                    public void onNext(JsonResult<List<BusInsuranceEntity>> listJsonResult) {
                        if ("0000".equals(listJsonResult.getResultCode())) {
                            insurance = (ArrayList<BusInsuranceEntity>) listJsonResult.getObject();
                            String isForce = "";//强制购买
                            if (insurance != null && insurance.size() > 0) {
                                for (BusInsuranceEntity entity : insurance) {
                                    isForce = entity.getIsForce();
                                    if ("1".equals(entity.getIsRecommend())) {
                                        isRecommend = entity;
                                        break;
                                    }
                                }

                                if ("1".equals(isForce)) {
                                    if (isRecommend != null) {
                                        showInsurance(String.format(getString(R.string.train_part), ArithUtils.div(isRecommend.getMoney(), 100.00) + "", isRecommend.getInsuranceName()),
                                                isRecommend.getInsuranceDesc());
                                        insuranceCalc = ArithUtils.div(isRecommend.getMoney(), 100.00);
                                    } else {
                                        isRecommend = insurance.get(0);
                                        showInsurance(String.format(getString(R.string.train_part), ArithUtils.div(insurance.get(0).getMoney(), 100.00) + "", insurance.get(0).getInsuranceName()),
                                                insurance.get(0).getInsuranceDesc());
                                        insuranceCalc = ArithUtils.div(insurance.get(0).getMoney(), 100.00);
                                    }
                                } else {
                                    if (isRecommend != null) {
                                        showInsurance(String.format(getString(R.string.train_part), ArithUtils.div(isRecommend.getMoney(), 100.00) + "", isRecommend.getInsuranceName()),
                                                isRecommend.getInsuranceDesc());
                                        insuranceCalc = ArithUtils.div(isRecommend.getMoney(), 100.00);
                                    } else {
                                        insuranceCalc = 0;
                                        showInsurance("", "暂不购买");
                                    }
                                }
                                if (isRecommend != null) {
                                    fillDescEntity.setInsurance(isRecommend.getMoney());
                                    bookPopwindow.setDataForPop(fillDescEntity);
                                }
                            } else {
                                insuranceCalc = 0;
                                rlInsurance.setVisibility(View.GONE);
                            }
                        }

                    }
                }
        );
    }

    //显示保险内容
    private void showInsurance(String explain, String desc) {
        rlInsurance.setVisibility(View.VISIBLE);
        tvInsurancePrice.setText(explain);
        tvInsuranceDesc.setText(desc);
    }

    //获取发车时间显示
    private String getHeaderShowTime() {

        String currentTime = DateTimeUtil.getSysTimeYMDHM();
        String busStartTime = String.format("%s %s", mDetail.getStartDate(), mDetail.getStartTime());

        long minute = DateTimeUtil.diffSecond(currentTime, busStartTime);
        String showTime = "";
        if (minute < 60 && minute > 0) {
            showTime = minute + " 分";
        } else if (minute < (24 * 60)) {
            showTime = String.format("%s小时%s分", minute / 60, minute % 60);
        } else if (minute < 0 || minute == 0) {
            showTime = 0 + "分";
        } else {
            showTime = String.format("%s天%s小时", minute / (24 * 60), (minute % ((24 * 60))) / 60);
        }
        return showTime;
    }

    /**
     * 是否实名
     */
    private void showNeedName() {
        if ("1".equals(mDetail.getIsRealName())) {
            llNeedName.setVisibility(View.VISIBLE);
            llNotNeedName.setVisibility(View.GONE);
        } else {
            llNeedName.setVisibility(View.GONE);
            llNotNeedName.setVisibility(View.VISIBLE);
        }
    }

    //默认展示全价票半价票
    private boolean isShowTicket = true;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent  intent  = null ;
        switch (v.getId()) {
            case R.id.btn_pay:
                submitOrder();
                break;
            case R.id.rl_show_desc:
                if (showDesc) {
                    if (bookPopwindow != null && bookPopwindow.isShowing()) {
                        bookPopwindow.dismiss();
                        showDesc = false;
                    }
                } else {
                    if (bookPopwindow != null && !bookPopwindow.isShowing()) {
                        bookPopwindow.showPop(findViewById(R.id.ll_bottom_direct));
                        showDesc = true;
                    }
                }
                tvShowDesc.setSelected(showDesc);
                break;
            case R.id.tv_show_type:
                if (isShowTicket) {
                    isShowTicket = false;
                    llTicketType.setVisibility(View.GONE);
                } else {
                    isShowTicket = true;
                    llTicketType.setVisibility(View.VISIBLE);
                }
                tvShowType.setSelected(isShowTicket);
                break;
            case R.id.ll_use_coupon:
                intent = new Intent(DirectFillInOrderActivity.this, CouponActivity.class);
                intent.putExtra("use",1);
                startActivityWithAnim(intent);
                break;
        }
    }

    /**
     * 接收 多选 乘客
     *
     * @param selectList
     */
    @Subscribe
    public void onRecievedMulPassenger(ArrayList<PassengerInfoEntity> selectList) {
        listPassenger = selectList;
        fillPassengerAdapter.replaceAll(listPassenger);
        calcPrice();
        if (listPassenger != null && listPassenger.size() > 0) {
            setTicketPerson(listPassenger.get(0));
        } else {
            setTicketPerson(null);
        }
    }

    /**
     * 计算价格
     * 需要计算 服务费，保险，全价票，半价票，优惠
     */
    private String priceShow;
    private double coupon;//优惠
    private double insuranceCalc;//保险 计算
    private double priceTicket;//票价

    private void calcPrice() {
        halfCount = 0;
        fullCount = 0;
        allCount = 0;
        int fullPrice = mDetail.getPrice().getDiscountPrice();
        int halfPrice = mDetail.getPrice().getHalfPrice();

        if ("1".equals(mDetail.getIsRealName())) {
            if (listPassenger != null && listPassenger.size() > 0) {
                allCount = listPassenger.size();
                for (PassengerInfoEntity infoEntity : listPassenger) {

                    switch (infoEntity.getTicketType()) {
                        case Constants.busAllTicket:
                            fullCount++;
                            break;
                        case Constants.busHalfTicket:
                            halfCount++;
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {

            fullCount = Integer.parseInt(asvFullPrice.getText().toString().trim());
            halfCount = Integer.parseInt(asvHalfPrice.getText().toString().trim());
        }
        refreshPriceDesc();

        priceTicket = ArithUtils.mul(fullCount, fullPrice) + ArithUtils.mul(halfCount, halfPrice);

        if (couponRecieve != null) {
            if ("1".equals(couponRecieve.getType())) {
                double dis = ArithUtils.div(Double.parseDouble(couponRecieve.getCoinsAmt()), 100.00);
                priceTicket = priceTicket - dis;
            } else if ("2".equals(couponRecieve.getType())) {
                double dis = ArithUtils.mul(priceTicket, Double.parseDouble(couponRecieve.getDisctVal()) / 100.00);
                double disMax = ArithUtils.div(Double.parseDouble(couponRecieve.getDisctMaxAmt()), 100.00);
                if (dis < disMax) {
                    priceTicket = priceTicket - dis;
                } else {
                    priceTicket = priceTicket - disMax;
                }
            }
        }

        priceShow = String.valueOf(priceTicket + ArithUtils.mul(serviceFee, allCount)
                + ArithUtils.mul(insuranceCalc, allCount));

        tvTotalPrice.setText(priceShow);
        tvTicketCount.setText(String.format(getString(R.string.bookTicket_count), allCount + ""));

        fillDescEntity.setFullCount(fullCount);
        fillDescEntity.setHalfCount(halfCount);

    }

    //全价票半价票数目
    private int halfCount = 0;
    private int fullCount = 0;
    private int allCount = 0;
    private int couponPrice = 0;
    private double fullPrice;
    private double halfPrice;
    private double serviceFee;

    private void refreshPriceDesc() {
        if (mDetail.getPrice() == null)
            return;
        fullPrice = ArithUtils.divDouble(mDetail.getPrice().getDiscountPrice(), 100);

        halfPrice = ArithUtils.divDouble(mDetail.getPrice().getHalfPrice(), 100);
        serviceFee = ArithUtils.div(mDetail.getServiceFee(), 100.00);
        fillDescEntity.setFullPrice(fullPrice + "");
        fillDescEntity.setFullCount(fullCount);
        fillDescEntity.setHalfCount(halfCount);
        fillDescEntity.setCoupon(couponPrice + "");
        fillDescEntity.setHalfPrice(halfPrice + "");
        fillDescEntity.setCount(allCount);
        fillDescEntity.setServiceFee(mDetail.getServiceFee());
        bookPopwindow.setDataForPop(fillDescEntity);
    }

    //设置取票人
    private void setTicketPerson(PassengerInfoEntity person) {
        this.person = person;
        if (person != null) {
            tvTicketPeople.setText(person.getPassengerName());
            etPhone.setText(person.getMobilePhone());
        }
    }

    /**
     * 接收单选 /修改一个联系人
     *
     * @param passengerInfoEntity
     */
    @Subscribe
    public void onRecievedSinglePassenger(PassengerInfoEntity passengerInfoEntity) {
        if (selectList != null && selectList.size() > 0) {
            for (int i = 0; i < selectList.size(); i++) {
                if (passengerInfoEntity.getPassengerId().equals(selectList.get(i).getPassengerId())) {
                    selectList.remove(i);
                    break;
                }
            }
            selectList.add(passengerInfoEntity);
        }
        fillPassengerAdapter.replaceAll(selectList);
        setTicketPerson(passengerInfoEntity);
    }

    /**
     * 优惠券 选择
     * 1面额；2折扣
     */
    private CouponResponsePara couponRecieve;

    @Subscribe
    public void onRecieveCoupon(CouponResponsePara couponRecieve) {
        this.couponRecieve = couponRecieve;
        calcPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (bookPopwindow != null && bookPopwindow.isShowing()) {
            bookPopwindow.dismiss();
            showDesc = false;
            tvShowDesc.setSelected(showDesc);
        } else {
            super.onBackPressed();
        }
    }
}
