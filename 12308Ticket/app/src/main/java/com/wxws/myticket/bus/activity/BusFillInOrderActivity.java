package com.wxws.myticket.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.FillPassengerAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.BusInsuranceEntity;
import com.wxws.myticket.bus.entity.FillOrderDescriptionEntity;
import com.wxws.myticket.bus.entity.InsuranceSelectEntity;
import com.wxws.myticket.bus.entity.LineDetailEntity;
import com.wxws.myticket.bus.entity.OrderIdEntity;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.bus.interfaces.OnAdultSelectListener;
import com.wxws.myticket.bus.popupwindow.BookFillOrderPopWindow;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.TextFunUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.AddAndSubView;
import com.wxws.myticket.common.widgets.view.CheckSwitchButton;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.CouponResponsePara;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.activity.PassengersActivity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * desc: 传统汽车票 填写订单
 * Date: 2016/10/26 09:57
 *
 * @auther: lixiangxiang
 */
public class BusFillInOrderActivity extends BaseActivity {

    /**
     * 一大堆控件 findViewById
     */
    private TraditionBusListEntity traditionEntity;
    private TextView tvArriveTime;
    private TextView tvCarType;
    private TextView tvPrice;
    private TextView tvServiceFee;
    private TextView tvBegin;
    private TextView tvTime;
    private TextView tvEndCity;
    private TextView tvBeginStation;
    private TextView tvEndStation;
    private LinearLayout addPassenger;
    private ListViewForScrollView listFill;
    private RelativeLayout needChild;
    private CheckSwitchButton csbChild;
    private RelativeLayout rlChildDesc;
    private RelativeLayout rlTicketInfo;
    private TextView tvChildDesc;
    private TextView tvInfo;
    private TextView tvName;
    private TextView tvCer;
    private TextView tvPhone;
    private ImageView imgDelInfo;
    private RelativeLayout rlBackup;
    private RelativeLayout rlInsurance;
    private CheckSwitchButton csbBackup;
    private TextView tvCoupon;
    private TextView tvInsurancePrice;
    private TextView tvInsuranceDesc;
    private TextView tvAdvance45;
    private OptionsPickerView halfTicket;
    private RelativeLayout rlShowDesc;
    private TextView tvTotalPrice;
    private TextView tvTicketCount;
    private TextView tvShowDesc;
    private Button btnPay;
    private TextView tvBackup;
    private TextView tvWeek;
    private AddAndSubView childCount;

    //数据
    private List<PassengerInfoEntity> listPassenger;
    private BookFillOrderPopWindow bookPopwindow;
    private FillOrderDescriptionEntity fillDescEntity;
    private ArrayList<PassengerInfoEntity> selectList;//选中的
    private FillPassengerAdapter fillPassengerAdapter;
    private TextView tvChildCheck;
    private LinearLayout llChild;
    private LineDetailEntity mLineDetail;
    private String bizType;
    private ArrayList<String> ticketList;
    private String departDate;
    private ArrayList<BusInsuranceEntity> insurance;
    private LinearLayout llCoupon;
    private PassengerInfoEntity person;
    private int selectPosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_fill_order);

        RxBus.getInstance().register(this);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {

        childCount.setOnAddNumChangeListener(new AddAndSubView.OnAddChangeListener() {
            @Override
            public void onAddNumChange(int num) {
                ToastManager.getInstance().showToast(BusFillInOrderActivity.this, String.valueOf(num));
            }

            @Override
            public void onSubNumChange(int num) {
                ToastManager.getInstance().showToast(BusFillInOrderActivity.this, String.valueOf(num));
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });

        bookPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showDesc = false;
                tvShowDesc.setSelected(showDesc);
            }
        });
        addPassenger.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        tvCoupon.setOnClickListener(this);
        rlInsurance.setOnClickListener(this);
        tvShowDesc.setOnClickListener(this);
        rlShowDesc.setOnClickListener(this);
        imgDelInfo.setOnClickListener(this);
        tvChildCheck.setOnClickListener(this);
        csbChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                closeOrOpenChild(!isChecked);
            }
        });
    }

    private void initData() {
        getBusDesc();

        ticketList = new ArrayList<>();
        selectList = new ArrayList<>();
        listPassenger = new ArrayList<>();
        //价格详情
        bookPopwindow = new BookFillOrderPopWindow(BusFillInOrderActivity.this);
        fillDescEntity = new FillOrderDescriptionEntity();

        rlTicketInfo.setVisibility(View.GONE);

        fillPassengerAdapter = new FillPassengerAdapter(BusFillInOrderActivity.this, listPassenger);
        listFill.setAdapter(fillPassengerAdapter);
        fillPassengerAdapter.setOnAdultSelectListener(new OnAdultSelectListener() {
            @Override
            public void onSelectEnd(int position, String adult) {
                selectPosition = position;
                halfTicket.show();
            }
        });

        barTitle.setText(getString(R.string.bookTicket_title));
        tvArriveTime.setText(String.format(getString(R.string.bookTicket_show_time), getHeaderShowTime()));

        tvBegin.setText(traditionEntity.getStartCityName());
        tvTime.setText(traditionEntity.getStartTime());
        tvEndCity.setText(traditionEntity.getEndCityName());
        tvBeginStation.setText(traditionEntity.getStartStationName());
        tvEndStation.setText(traditionEntity.getEndStationName());
        tvCarType.setText(traditionEntity.getType());
        csbBackup.setChecked(true);//默认关闭备选车次
        String[] backup = getBackupTime(traditionEntity.getStartTime());
        tvBackup.setText(String.format(getString(R.string.bookTicket_backup), backup[0], backup[2], backup[1], backup[2]));
        tvWeek.setText(String.format("%s %s", traditionEntity.getStartDate(), DateTimeUtil.getWeekStr(traditionEntity.getStartDate())));
        childCount.setNum(0);
        //携童说明
        tvChildDesc.setText(getString(R.string.bookTicket_child_content));
        tvChildCheck.setSelected(showChildDesc);
        tvChildDesc.setVisibility(View.GONE);
        csbChild.setChecked(true);
        closeOrOpenChild(false);
        tvAdvance45.setText(TextFunUtils.spanColor(String.format(getString(R.string.bookTicket_advance45), "45"), "12308提醒你提前", "45分钟", getResources().getColor(R.color.app_style)));

        getBusInsurance();
    }

    //提交订单
    private void submitOrder() {
        String scheduleId = traditionEntity.getScheduleId();
        String fromCityId = traditionEntity.getFromStationId();
        String toCityId = traditionEntity.getToStationId();
        String isBack = mLineDetail.getIsBackCheci();
        String couponId;
        if(couponRecieve!=null){
            couponId = couponRecieve.getCode();
        }else {
            couponId = "";
        }
        String adultCount = String.valueOf(fullCount);
        String halfCountS = String.valueOf(halfCount);
        String childCountS = String.valueOf(childCount.getNum());
        StringBuilder sb = new StringBuilder();
        if (listPassenger != null && listPassenger.size() > 0) {
            sb.append("[");
            int j = 0;
            for (PassengerInfoEntity info : listPassenger) {
                j++;
                if (j != listPassenger.size())
                    sb.append(String.format("{\"id\":%s,\"ticketType\":%s,\"insuranceId\":\"%s\"},", info.getPassengerId(), info.getTicketType(), isRecommend== null?"" :isRecommend.getInsuranceId()));
                else
                    sb.append(String.format("{\"id\":%s,\"ticketType\":%s,\"insuranceId\":\"%s\"}", info.getPassengerId(), info.getTicketType(),isRecommend== null?"" :isRecommend.getInsuranceId()));
            }
            sb.append("]");
        } else {
            ToastManager.getInstance().showToast(BusFillInOrderActivity.this, getString(R.string.bookTicket_add));
            return;
        }
        String passenger = sb.toString();
        String contactName;
        String contactNo;
        String contactId;
        String contactMobile;
        if (person != null) {
            contactName = person.getPassengerName();
            contactNo = person.getCertificateNo();
            contactId = person.getPassengerId();
            contactMobile = person.getMobilePhone();
        } else {
            ToastManager.getInstance().showToast(BusFillInOrderActivity.this, getString(R.string.bookTicket_person));
            return;
        }
        String orderMoney = String.valueOf(ArithUtils.mul(Double.parseDouble(priceShow),100));
        String openFreeInsurance = mLineDetail.getOpenFreeInsurance();
        String bizTypes = bizType;
        String isRealName = "1";
        HttpRequestManager.getInstance().submitOrder(departDate, scheduleId, fromCityId, toCityId, isBack, couponId, adultCount,
                childCountS, halfCountS, passenger, contactName, contactNo, contactId, contactMobile, orderMoney, openFreeInsurance, isRealName,
                bizTypes, new ApiSubscriber<JsonResult<OrderIdEntity>>(BusFillInOrderActivity.this) {
                    @Override
                    public void onNext(JsonResult<OrderIdEntity> stringJsonResult) {
                        if ("0000".equals(stringJsonResult.getResultCode())) {
                            if (!StringUtils.isNullOrEmpty(stringJsonResult.getObject().getOrderId())) {
                                //提交订单
                                Intent intent = new Intent(BusFillInOrderActivity.this, PayActivity.class);
                                intent.putExtra("orderId", stringJsonResult.getObject().getOrderId());
                                intent.putExtra("bizType",bizType);
                                BusFillInOrderActivity.this.startActivityWithAnim(intent);
                            } else {
                                ToastManager.getInstance().showToast(BusFillInOrderActivity.this, stringJsonResult.getResultMsg());
                            }
                        } else {
                            ToastManager.getInstance().showToast(BusFillInOrderActivity.this, stringJsonResult.getResultMsg());
                        }
                    }
                });
    }

    /**
     * 关闭 或者 打开 携童票
     *
     * @param close
     */
    private void closeOrOpenChild(boolean close) {
        if (close) {
            llChild.setVisibility(View.VISIBLE);
        } else {
            llChild.setVisibility(View.GONE);
        }
    }

    /**
     * 备选时间
     *
     * @param time
     * @return
     */
    private String[] getBackupTime(String time) {

        String[] backupTime = new String[3];
        if (StringUtils.isNullOrEmpty(time)) {
            return backupTime;
        }
        String[] split = time.split(":");
        int time1 = Integer.parseInt(split[0]);

        if (time1 == 0) {
            backupTime[0] = String.format("%s", 23);
            backupTime[1] = String.format("0%s", 1);
        } else if (time1 < 9 && time1 >= 1) {
            backupTime[0] = String.format("0%s", time1 - 1);
            backupTime[1] = String.format("0%s", time1 + 1);
        } else if (time1 == 9) {
            backupTime[0] = String.format("0%s", 8);
            backupTime[1] = String.format("%s", 10);
        } else if (time1 == 10) {
            backupTime[0] = String.format("0%s", 9);
            backupTime[1] = String.format("%s", 11);
        } else if (time1 > 10 && time1 < 23) {
            backupTime[0] = String.format("%s", time1 - 1);
            backupTime[1] = String.format("%s", time1 + 1);
        } else {
            backupTime[0] = String.format("%s", time1 - 1);
            backupTime[1] = String.format("%s", "00");
        }
        backupTime[2] = split[1];
        return backupTime;
    }

    //获取发车时间显示
    private String getHeaderShowTime() {

        String currentTime = DateTimeUtil.getSysTimeYMDHM();
        String busStartTime = String.format("%s %s", traditionEntity.getStartDate(), traditionEntity.getStartTime());

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

    //成人票半价票选择
    private void initPickView() {
        halfTicket = new OptionsPickerView(BusFillInOrderActivity.this);
        if ("1".equals(mLineDetail.getIsSupportHalfTicket()))
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

    private void getPreData() {
        traditionEntity = (TraditionBusListEntity) getIntent().getSerializableExtra("singleBus");
        bizType = getIntent().getStringExtra("bizType");
        departDate = getIntent().getStringExtra("departDate");
    }

    private void initView() {
        bindTitleViews();
        //距离到达时间
        tvArriveTime = (TextView) findViewById(R.id.tv_arrive_time);
        //备选车次说明
        tvBackup = (TextView) findViewById(R.id.tv_backup);
        //周末日期
        tvWeek = (TextView) findViewById(R.id.tv_week);
        //车型
        tvCarType = (TextView) findViewById(R.id.tv_car_type);
        //单价
        tvPrice = (TextView) findViewById(R.id.tv_price);
        //服务费
        tvServiceFee = (TextView) findViewById(R.id.tv_service_fee);
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
        //添加乘客
        addPassenger = (LinearLayout) findViewById(R.id.ll_add_passenger);
        //乘客数量
        listFill = (ListViewForScrollView) findViewById(R.id.list_fill);
        //是否需要协同票
        needChild = (RelativeLayout) findViewById(R.id.rl_need_child);
        //关闭 打开 携 童  //不响应onclick  事件
        csbChild = (CheckSwitchButton) findViewById(R.id.csb_child);
        // 携童
        rlChildDesc = (RelativeLayout) findViewById(R.id.rl_child_desc);
        // 取票人详细信息
        rlTicketInfo = (RelativeLayout) findViewById(R.id.rl_ticket_info);
        // 携童数量
        childCount = (AddAndSubView) findViewById(R.id.child_count);
        // 携童 de 规则
        tvChildDesc = (TextView) findViewById(R.id.tv_child_desc);
        // 选择取票人
        tvInfo = (TextView) findViewById(R.id.tv_info);
        // 取票人姓名
        tvName = (TextView) findViewById(R.id.tv_name);
        // 取票人身份证
        tvCer = (TextView) findViewById(R.id.tv_cer);
        // 取票人手机号码
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        //删除取票人
        imgDelInfo = (ImageView) findViewById(R.id.img_del_info);
        // 开启备用车次 csb_backup
        rlBackup = (RelativeLayout) findViewById(R.id.rl_backup);
        // 是否卖保险
        rlInsurance = (RelativeLayout) findViewById(R.id.rl_insurance);
        //开启备用车次
        csbBackup = (CheckSwitchButton) findViewById(R.id.csb_backup);
        //使用优惠券
        tvCoupon = (TextView) findViewById(R.id.tv_coupon);
        //保险价格
        tvInsurancePrice = (TextView) findViewById(R.id.tv_insurance_price);
        //保险描述
        tvInsuranceDesc = (TextView) findViewById(R.id.tv_insurance_desc);
        //提前45 分钟取票
        tvAdvance45 = (TextView) findViewById(R.id.tv_advance_45);
        //展示详情
        tvChildCheck = (TextView) findViewById(R.id.tv_child_check);
        //控制显示携童票
        llChild = (LinearLayout) findViewById(R.id.ll_child);
        //控制显示优惠券
        llCoupon = (LinearLayout) findViewById(R.id.ll_coupon);

        //底部按钮
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvTicketCount = (TextView) findViewById(R.id.tv_ticket_count);
        rlShowDesc = (RelativeLayout) findViewById(R.id.rl_show_desc);
        tvShowDesc = (TextView) findViewById(R.id.tv_show_desc);
        btnPay = (Button) findViewById(R.id.btn_pay);

    }

    private boolean showDesc = false;
    private boolean showChildDesc = false;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
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
            case R.id.ll_add_passenger:
                intent = new Intent(BusFillInOrderActivity.this, PassengersActivity.class);
                intent.putExtra("from", PassengersActivity.TRADITIONALPASSENGER);
                intent.putExtra("selectPassenger", selectList);
                intent.putExtra("bizType", ModuleConstants.bizTypeBus);
                intent.putExtra("mulSelect", PassengersActivity.SELECTMUL);
                startActivityWithAnim(intent);
                break;
            case R.id.tv_info://
                intent = new Intent(BusFillInOrderActivity.this, PassengersActivity.class);
                intent.putExtra("from", PassengersActivity.TRADITIONALPASSENGER);
                intent.putExtra("mulSelect", PassengersActivity.SELECTSINGLE);
                intent.putExtra("pickTicket", true);
                intent.putExtra("bizType", ModuleConstants.bizTypeBus);
                startActivityWithAnim(intent);
                break;
            case R.id.rl_insurance://保险
                intent = new Intent(BusFillInOrderActivity.this, InsuranceActivity.class);
                intent.putExtra("insurance", insurance);
                intent.putExtra("isRecommend", isRecommend);
                startActivityWithAnim(intent);
                break;
            case R.id.tv_coupon://优惠券

                break;
            case R.id.img_del_info://删除联系人
                rlTicketInfo.setVisibility(View.GONE);
                break;
            case R.id.tv_child_check:
                if (showChildDesc) {
                    showChildDesc = false;
                    tvChildCheck.setSelected(showChildDesc);
                    tvChildDesc.setVisibility(View.GONE);
                } else {
                    showChildDesc = true;
                    tvChildCheck.setSelected(showChildDesc);
                    tvChildDesc.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //显示保险内容
    private void showInsurance(String explain, String desc) {
        rlInsurance.setVisibility(View.VISIBLE);
        tvInsurancePrice.setText(explain);
        tvInsuranceDesc.setText(desc);
    }

    //拉取保险
    private BusInsuranceEntity isRecommend = null;//推荐

    private void getBusInsurance() {
        HttpRequestManager.getInstance().busInsuranceList(traditionEntity.getScheduleId(), traditionEntity.getBizType(),
                traditionEntity.getFromStationId(), traditionEntity.getToStationId(),departDate,
                new ApiSubscriber<JsonResult<List<BusInsuranceEntity>>>(BusFillInOrderActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
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
                                if (isRecommend!=null){
                                    fillDescEntity.setInsurance(isRecommend.getMoney());
                                    bookPopwindow.setDataForPop(fillDescEntity);
                                }
                            } else {
                                insuranceCalc = 0;
                                rlInsurance.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    //获取班次详情
    private void getBusDesc() {
        HttpRequestManager.getInstance().getLineDetail(ModuleConstants.bizTypeBus, traditionEntity.getStartDate(), traditionEntity.getScheduleId(),
                new ApiSubscriber<JsonResult<LineDetailEntity>>(BusFillInOrderActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
                    @Override
                    public void onNext(JsonResult<LineDetailEntity> lineDetailJsonResult) {
                        if ("0000".equals(lineDetailJsonResult.getResultCode())) {
                            mLineDetail = lineDetailJsonResult.getObject();
                            refreshView();
                            initPickView();
                        }
                    }
                });
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
        if (mLineDetail.getPrice() == null)
            return;
        fullPrice = ArithUtils.divDouble(mLineDetail.getPrice().getDiscountPrice(), 100);

        halfPrice = ArithUtils.divDouble(mLineDetail.getPrice().getHalfPrice(), 100);
        serviceFee = ArithUtils.div(mLineDetail.getServiceFee(), 100.00);
        fillDescEntity.setFullPrice(fullPrice + "");
        fillDescEntity.setFullCount(fullCount);
        fillDescEntity.setHalfCount(halfCount);
        fillDescEntity.setCoupon(couponPrice + "");
        fillDescEntity.setHalfPrice(halfPrice + "");
        fillDescEntity.setCount(allCount);
        fillDescEntity.setServiceFee(mLineDetail.getServiceFee());
        bookPopwindow.setDataForPop(fillDescEntity);
    }

    //刷新view
    private void refreshView() {

        refreshPriceDesc();

        tvPrice.setText(String.valueOf(ArithUtils.div(mLineDetail.getPrice().getDiscountPrice(), 100.00)));
        tvServiceFee.setText(String.format(getString(R.string.bookTicket_service_fee_unit), serviceFee + ""));//服务费

        switch (mLineDetail.getIsSupportChildTicket()) {
            case "0"://不支持携童票
                llChild.setVisibility(View.GONE);
                break;
            case "1":
                llChild.setVisibility(View.VISIBLE);
                break;
            default:
                ToastManager.getInstance().showToast(BusFillInOrderActivity.this, "携童票数据不正常");
                break;
        }

        switch (mLineDetail.getIsSupportHalfTicket()) {
            case "0"://不支持半价票
                ticketList.clear();
                ticketList.add(getString(R.string.bookTicket_all));
                break;
            case "1":
                ticketList.clear();
                ticketList.add(getString(R.string.bookTicket_all));
                ticketList.add(getString(R.string.bookTicket_half));
                break;
            default:
                ToastManager.getInstance().showToast(BusFillInOrderActivity.this, "全价票数据不正常");
                break;
        }

        if ("1".equals(mLineDetail.getIsBackCheci())) {
            csbBackup.setChecked(false);
        } else {
            csbBackup.setChecked(true);
        }

        if ("1".equals(mLineDetail.getIsCoupon())) {
            llCoupon.setVisibility(View.VISIBLE);
        } else {
            llCoupon.setVisibility(View.GONE);
        }
        //计算一次价格
        calcPrice();
    }

    @Override
    public void onBackPressed() {
        if (halfTicket != null && halfTicket.isShowing()) {
            halfTicket.dismiss();
        } else if (bookPopwindow != null && bookPopwindow.isShowing()) {
            bookPopwindow.dismiss();
            showDesc = false;
            tvShowDesc.setSelected(showDesc);
        } else {
            super.onBackPressed();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    //设置取票人
    private void setTicketPerson(PassengerInfoEntity person) {
        this.person = person;
        if (person != null) {
            rlTicketInfo.setVisibility(View.VISIBLE);
            tvName.setText(person.getPassengerName());
            tvCer.setText(person.getCertificateNo());
            tvPhone.setText(person.getMobilePhone());
        } else {
            rlTicketInfo.setVisibility(View.GONE);
        }

    }

    //保险
    @Subscribe
    public  void  onRecievedInsurance(InsuranceSelectEntity entity){
        if (InsuranceSelectEntity.noSelect == entity.getFlag()){
            isRecommend = null;
        }else {
            isRecommend = entity.getEntity();
        }
        calcPrice();
    }
    //登录事件
    @Subscribe
    public void onRecievedLogin(CommonEvent event) {
        if (event.getFlag() == 0) {
            getBusDesc();
            getBusInsurance();
        } else if (event.getFlag() == 10) {
            finish();
        }
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

}
