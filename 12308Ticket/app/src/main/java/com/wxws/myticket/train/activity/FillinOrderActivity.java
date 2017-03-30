package com.wxws.myticket.train.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.net.http.retrofit.HttpRequest;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.CommonUtil;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.CheckSwitchButton;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.activity.H5CommonActivity;
import com.wxws.myticket.my.activity.LoginActivity;
import com.wxws.myticket.my.activity.PassengersActivity;
import com.wxws.myticket.order.activity.TrainOrderDetailsActivity;
import com.wxws.myticket.train.adapter.FillinorderPassengerAdapter;
import com.wxws.myticket.train.adapter.FillinorderSeatAdapter;
import com.wxws.myticket.train.contants.TrainContants;
import com.wxws.myticket.train.dialog.BookFailureDialog;
import com.wxws.myticket.train.dialog.BookingDialog;
import com.wxws.myticket.train.entity.ChildInfoModel;
import com.wxws.myticket.train.entity.ChooseSeatModel;
import com.wxws.myticket.train.entity.CommitOrderResponseModel;
import com.wxws.myticket.train.entity.CreateTrainOrderModel;
import com.wxws.myticket.train.entity.LockTicketResultEntity;
import com.wxws.myticket.train.entity.NewPassengerInfoModel;
import com.wxws.myticket.train.entity.SeatinfoModel;
import com.wxws.myticket.train.entity.TicketListModel;
import com.wxws.myticket.train.entity.TrainInsuranceModel;
import com.wxws.myticket.train.rxentity.CheckPassengerEvent;
import com.wxws.myticket.train.rxentity.EditPassengerEvent;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * desc: 订单填写页面
 * Date: 2016-07-28 11:05
 *
 * @author jiangyan
 */
public class FillinOrderActivity extends BaseActivity implements View.OnClickListener, FillinorderPassengerAdapter.OnUpdateListener {


    private final static int REQUEST_PHONE = 0; //　联系人手机
    private final static int REQUEST_ADULT_INSURANCE = 1; // 成人保险
    private final static int REQUEST_CHILD_INSURANCE = 2; // 儿童保险
    private final static int REQUEST_ADDPASSENGER = 3; // 添加乘客信息
    private final static int REQUEST_LOGIN = 4; // 没有登录，跳转去登录

    private ListView seatListview;
    private ListView passengerListview;

    // top数据
    private TextView tv_fromdate;
    private TextView tv_fromtime;
    private TextView tv_fromstation;
    private TextView tv_todate;
    private TextView tv_totime;
    private TextView tv_tostation;
    private TextView tv_traincode;
    private TextView tv_runtime;
    private CheckSwitchButton cb_acceptnoseat;
    private ImageView iv_addpassenger;
    private EditText et_mobilephone;
    private ImageView iv_addphone;

    private TextView tv_seattype;
    private TextView tv_seatprice;
    private FillinorderSeatAdapter mSeatAdapter;
    private FillinorderPassengerAdapter mPassengerAdapter;
    private View footview;
    private ImageView iv_isunfold;
    private boolean is_unfold = true; // 座位表是否关闭
    private LinearLayout ll_adultinsurance;
    private TextView tv_adultinsurance_infodetails;
    private TextView tv_adultinsurance_price;
    private LinearLayout ll_childinsurance;
    private TextView tv_childinsurance_price;
    private LinearLayout ll_refundx;
    private Button bt_commitorder;
    private LinearLayout ll_addpassenger;

    private List<TrainInsuranceModel> mChildModel = new ArrayList<>(); // 网络获取的儿童保险信息
    private List<TrainInsuranceModel> mAdultModel = new ArrayList<>(); // 网络获取的成人保险信息

    private List<ChildInfoModel> mChildInfoList = new ArrayList<>(); // 儿童保险姓名、生日等数据

    private List<PassengerInfoEntity> mSelectedPassengerList = new ArrayList<>(); // 已经选中成人乘客信息
    private List<NewPassengerInfoModel> mChoosedPassengerList = new ArrayList<>();// 添加的全部乘客信息，包括成人和儿童

    private ChooseSeatModel mTicketLineInfo; // 从pre获取的车次信息
    private List<SeatinfoModel> mSeatList;// 从pre获取的座位信息
    private int mBuyPosition;//pre选中的座位类型，一等座二等座等，

    // 创建订单需要的数据
    private String mCreateStartData;// 乘车日期
    private String mCreateFromstationCode;//出发站简码
    private String mCreateTostationCode;//到达站简码
    private String mCreateTrainCode;//车次
    private String mCreateMobie;//联系手机
    private boolean mCreateAcceptstanding = false;//默认不接受无座
    private List<TicketListModel> mCreateTicketList = new ArrayList<>();//乘车人车票列表

    private String mChoosedSeatType; // 选中的座位类型
    private String mChoosedInsuranceId = ""; // 选中的成人保险类型id
    private String mChoosedChildInsuranceId = "";// 选中的儿童保险类型id
    private int mChoosedInsurancePrice = -1;//选中的成人保险价格
    private int mChoosedChildInsurancePrice = -1;//选中的儿童保险价格
    private BookingDialog dialog;
    private CreateTrainOrderModel createPara;
    private int mAdultNum = 0;
    private int mChildNum = 0;
    private int mInsuranceChildNum = 0;

    private InnerHandler innerHandler;//
    private String orderId;
    private CheckBox cb_agree;
    private BookFailureDialog bookFailureDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_fillinorder);
        RxBus.getInstance().register(this);

        initView();

        initTitleData();

        getDataFromPre();

        if (!checkLogin()) {
            //跳转登录页面
            Intent loginIntent = new Intent(FillinOrderActivity.this, LoginActivity.class);
            loginIntent.putExtra("type", 0);
            startActivityForResultWithAnim(loginIntent, REQUEST_LOGIN);
        } else {
            initData();
            getInsuranceInfo();
        }
    }

    private void initTitleData() {
        mImgSpline.setVisibility(View.GONE);
        barTitle.setTextColor(getResources().getColor(R.color.white));
        tvRight.setTextColor(getResources().getColor(R.color.white));
        barTitle.setText(getString(R.string.train_choose_fillin));
        imgBarLeft.setImageResource(R.drawable.slt_return_white);
        llTitleBar.setBackgroundResource(R.mipmap.fillinorder_titlebg);
        flTitleBarRight.setVisibility(View.VISIBLE);
        barTitle.setText("填写订单");
        flTitleBarRight.setVisibility(View.VISIBLE);
        tvRight.setText("购票须知");
        flTitleBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //  购票须知
                Intent intent = new Intent(FillinOrderActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", TrainContants.HCPGPXZ);
                startActivityWithAnim(intent);
            }
        });
    }

    private void initView() {
        bindLoadingView();

        bindTitleViews();
        // top 基础车次信息
        tv_fromdate = (TextView) findViewById(R.id.tv_fillinorder_fromdate);
        tv_fromtime = (TextView) findViewById(R.id.tv_fillinorder_fromtime);
        tv_fromstation = (TextView) findViewById(R.id.tv_fillinorder_fromstation);
        tv_todate = (TextView) findViewById(R.id.tv_fillinorder_todate);
        tv_totime = (TextView) findViewById(R.id.tv_fillinorder_totime);
        tv_tostation = (TextView) findViewById(R.id.tv_fillinorder_tostation);
        tv_traincode = (TextView) findViewById(R.id.tv_fillinorder_traincode);
        tv_runtime = (TextView) findViewById(R.id.tv_fillinorder_runtime);

        //座位信息相关
        tv_seattype = (TextView) findViewById(R.id.tv_fillinorder_seattype);
        iv_isunfold = (ImageView) findViewById(R.id.iv_fillinorder_isunfold);
        tv_seatprice = (TextView) findViewById(R.id.tv_fillinorder_seatprice);
        seatListview = (ListView) findViewById(R.id.lv_fillorder_seat);

        // 是否接受无座
        cb_acceptnoseat = (CheckSwitchButton) findViewById(R.id.cb_fillinorder_acceptnoseat);

        // 乘客信息相关
        ll_addpassenger = (LinearLayout) findViewById(R.id.ll_fillinorder_addpassenger);
        iv_addpassenger = (ImageView) findViewById(R.id.iv_fillinorder_addpassenger);
        passengerListview = (ListView) findViewById(R.id.lv_fillorder_passenger);
        footview = View.inflate(FillinOrderActivity.this, R.layout.item_fillinorder_passengerfooter, null);

        // 联系手机
        et_mobilephone = (EditText) findViewById(R.id.et_fillinorder_mobiephone);
        iv_addphone = (ImageView) findViewById(R.id.iv_fillinorder_addphone);

        // 成人保险
        ll_adultinsurance = (LinearLayout) findViewById(R.id.ll_fillinorder_adultinsurance);
        tv_adultinsurance_infodetails = (TextView) findViewById(R.id.tv_fillinorder_adultinsurance_infodetails);
        tv_adultinsurance_price = (TextView) findViewById(R.id.tv_fillinorder_adultinsurance_price);

        // 儿童保险
        ll_childinsurance = (LinearLayout) findViewById(R.id.ll_fillinorder_childinsurance);
        tv_childinsurance_price = (TextView) findViewById(R.id.tv_fillinorder_childinsurance_price);

        // 退票、改签须知
        ll_refundx = (LinearLayout) findViewById(R.id.ll_fillinorder_refundx);
        cb_agree = (CheckBox) findViewById(R.id.cb_fillinorder_agree);
        TextView tv_needknow = (TextView) findViewById(R.id.tv_fillinorder_needknow);
        TextView tv_protocal = (TextView) findViewById(R.id.tv_fillinorder_protocal);

        // 底部两个按钮的位置
        bt_commitorder = (Button) findViewById(R.id.bt_fillinorder_commitorder);

        // 其他基础设置
        ll_adultinsurance.setOnClickListener(this);
        ll_childinsurance.setOnClickListener(this);
        bt_commitorder.setOnClickListener(this);
        iv_addphone.setOnClickListener(this);
        iv_addpassenger.setOnClickListener(this);

        mSeatAdapter = new FillinorderSeatAdapter(this);
        seatListview.setAdapter(mSeatAdapter);

        mPassengerAdapter = new FillinorderPassengerAdapter(this);
        passengerListview.setAdapter(mPassengerAdapter);
        mPassengerAdapter.setOnUpdateListener(this);
        iv_isunfold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_unfold) {
                    is_unfold = false;
                    seatListview.setVisibility(View.VISIBLE);
                    iv_isunfold.setBackgroundResource(R.mipmap.icon_fillinorder_packup);
                } else {
                    is_unfold = true;
                    seatListview.setVisibility(View.GONE);
                    iv_isunfold.setBackgroundResource(R.mipmap.icon_fillinorder_unfold);
                }

            }
        });

        tv_needknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 购票须知
                Intent intent = new Intent(FillinOrderActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", TrainContants.HCPGPXZ);
                startActivityWithAnim(intent);
            }
        });

        tv_protocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 12308购票协议
                Intent intent = new Intent(FillinOrderActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", TrainContants.HCPYDXY);
                startActivityWithAnim(intent);

            }
        });

        ll_refundx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 12308购票协议
                Intent intent = new Intent(FillinOrderActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", TrainContants.HCPTGQXZ);
                startActivityWithAnim(intent);
            }
        });

        passengerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == mChoosedPassengerList.size()) { // 在最后的footerview点击事件

                    Intent passengerIntent = new Intent(FillinOrderActivity.this, PassengersActivity.class);
                    Bundle passengerbundle = new Bundle();
                    passengerbundle.putInt("FROM_BOOK", PassengersActivity.SELECT_PASSENGER);
                    passengerbundle.putBoolean("FROM_TRAIN", true);
                    passengerbundle.putString("isNeed12306", ModuleConstants.bizTypeTrain);
                    passengerbundle.putSerializable("SELECTPASSENGER", (Serializable) mSelectedPassengerList);
                    passengerIntent.putExtras(passengerbundle);
                    startActivityWithAnim(passengerIntent);
                }

            }
        });

        mSeatAdapter.setOnChoosedSeatListener(new FillinorderSeatAdapter.OnChoosedSeatListener() {
            @Override
            public void choosed(int position) {

                for (int i = 0; i < mSeatList.size(); i++) {
                    if (position == i) {
                        mSeatList.get(i).setChoosed(true);
                    } else {
                        mSeatList.get(i).setChoosed(false);
                    }
                }

                tv_seattype.setText(mSeatList.get(position).getName());
                tv_seatprice.setText("¥" + mSeatList.get(position).getPrice() * 0.01);
                mBuyPosition = position;
                mSeatAdapter.refreshData(mSeatList);
                mChoosedSeatType = mSeatList.get(position).getSeatType();
                //处理选择无座的类型
                if ("无座".equals(mSeatList.get(position).getName())) {
                    mChoosedSeatType = mSeatList.get(0).getSeatType();
                    mCreateAcceptstanding = true;
                    cb_acceptnoseat.setChecked(false);
                }
                calculateTotal();
            }
        });
    }

    private void initData() {
        //初始化旋转dialog
        dialog = new BookingDialog(FillinOrderActivity.this);
        //处理选择无座的类型
        if ("无座".equals(mSeatList.get(mBuyPosition).getName())) {
            mChoosedSeatType = mSeatList.get(0).getSeatType();
            mCreateAcceptstanding = true;
            cb_acceptnoseat.setChecked(false);
        } else {
            cb_acceptnoseat.setChecked(true); // 这里为true的时候，表示不接受无座，自定义checkbox的问题。
        }
        Date fromdate = DateTimeUtil.StringToDate(mCreateStartData, "yyyy-MM-dd");
        String fromdatestr = DateTimeUtil.getDate(fromdate, "MM-dd");

        Date todate = DateTimeUtil.addDay(fromdate, Integer.parseInt(mTicketLineInfo.getArriveDays()));
        String todatestr = DateTimeUtil.getDate(todate, "MM-dd");

        tv_fromdate.setText(fromdatestr);
        tv_fromtime.setText(mTicketLineInfo.getStartTime());
        tv_fromstation.setText(mTicketLineInfo.getFromStationName());

        tv_todate.setText(todatestr);
        tv_totime.setText(mTicketLineInfo.getArriveTime());
        tv_tostation.setText(mTicketLineInfo.getToStationName());
        tv_traincode.setText(mTicketLineInfo.getTrainCode());
        tv_runtime.setText(DateTimeUtil.minToHour(mTicketLineInfo.getRunTimeMinute()));

        tv_seattype.setText(mSeatList.get(mBuyPosition).getName());
        tv_seatprice.setText("¥" + mSeatList.get(mBuyPosition).getPrice() * 0.01);

        et_mobilephone.setText(userInfo.getMobilePhone());
        mCreateMobie = userInfo.getMobilePhone();

        cb_acceptnoseat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCreateAcceptstanding = !isChecked;// 同上面注释，为true的时候，实际应该是false
            }
        });

        mSeatAdapter.refreshData(mSeatList);
        setListViewHeightBasedOnChildren(passengerListview);
        setListViewHeightBasedOnChildren(seatListview);
    }

    private void getDataFromPre() {

        innerHandler = new InnerHandler(FillinOrderActivity.this);
        mCreateStartData = getIntent().getStringExtra("fromdate");

        mTicketLineInfo = (ChooseSeatModel) getIntent().getBundleExtra("seatinfobundle").getSerializable("seatinfo");
        mSeatList = (List<SeatinfoModel>) getIntent().getBundleExtra("seatinfobundle").getSerializable("seatlist");
        mBuyPosition = getIntent().getIntExtra("buyposition", -1);


        mCreateFromstationCode = mTicketLineInfo.getFromStationCode();
        mCreateTostationCode = mTicketLineInfo.getToStationCode();
        mCreateTrainCode = mTicketLineInfo.getTrainCode();

        mChoosedSeatType = mSeatList.get(mBuyPosition).getSeatType();

    }

    private void getInsuranceInfo() {
        HttpRequestManager.getInstance().trainInsuranceList(mTicketLineInfo.getTrainNo(),ModuleConstants.bizTypeTrain,mTicketLineInfo.getFromStationCode(),
                mTicketLineInfo.getFromStationName(),mTicketLineInfo.getToStationCode(),mTicketLineInfo.getToStationName(),
                new ApiSubscriber<JsonResult<List<TrainInsuranceModel>>>(FillinOrderActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<TrainInsuranceModel>> trainInsuranceJsonResult) {
                if ("0000".equals(trainInsuranceJsonResult.getResultCode())) {
                    if (trainInsuranceJsonResult.getObject() != null && trainInsuranceJsonResult.getObject().size() > 0) {
                        for (TrainInsuranceModel model : trainInsuranceJsonResult.getObject()) {
                                if (model.getType().equals("0") && "1".equals(model.getIsRecommend())) {
                                    mAdultModel.add(model);
                                } else {
                                    mChildModel.add(model);
                                }
                            }
                            if (mAdultModel.size()>=1){
                                updateInsuranceInfo(0);
                            }
                    }
                } else {
                    ToastManager.getInstance().showToast(FillinOrderActivity.this, trainInsuranceJsonResult.getResultMsg());
                }
                calculateTotal();
            }
        });
    }

    //更新保险信息
    private void updateInsuranceInfo(int position) {
        mAdultModel.get(position).setFlag_checkbox(true);
        tv_adultinsurance_price.setText("¥" + mAdultModel.get(position).getMoney() + "x" + mAdultNum);
        tv_adultinsurance_infodetails.setText(mAdultModel.get(position).getInsuranceDesc());

        mChoosedInsuranceId = mAdultModel.get(position).getInsuranceId();
        mChoosedInsurancePrice = mAdultModel.get(position).getMoney();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.iv_fillinorder_addphone: // 跳转到手机联系人选择号码
                Uri uri = Uri.parse("content://com.android.contacts/contacts");
                Intent phoneIntent = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(phoneIntent, REQUEST_PHONE);
                break;
            case R.id.bt_fillinorder_commitorder:
                commitOrder();
                break;
            case R.id.ll_fillinorder_adultinsurance: // 成人保险
                Intent adultIntent = new Intent(FillinOrderActivity.this, TrainInsuranceActivity.class);

                Bundle adultBundle = new Bundle();
                adultBundle.putSerializable("adultlist", (Serializable) mAdultModel);
                adultIntent.putExtra("adultinfo", adultBundle);

                adultIntent.putExtra("num", mAdultNum);
                adultIntent.putExtra("type", 1); //成人保险类型
                startActivityForResultWithAnim(adultIntent, REQUEST_ADULT_INSURANCE);
                break;
            case R.id.ll_fillinorder_childinsurance: // 儿童保险
                Intent childIntent = new Intent(FillinOrderActivity.this, TrainInsuranceActivity.class);
                Bundle childbundle = new Bundle();
                childbundle.putSerializable("childlist", (Serializable) mChildModel);
                childbundle.putSerializable("childinfolist", (Serializable) mChildInfoList);
                childIntent.putExtra("childinfo", childbundle);
                childIntent.putExtra("num", mChildNum);
                childIntent.putExtra("childinfosize", mInsuranceChildNum);
                childIntent.putExtra("type", 0); // 儿童保险类型
                startActivityForResultWithAnim(childIntent, REQUEST_CHILD_INSURANCE);
                break;
            case R.id.iv_fillinorder_addpassenger: // 初始添加乘客

                Intent passengerIntent = new Intent(FillinOrderActivity.this, PassengersActivity.class);
                Bundle passengerbundle = new Bundle();
                passengerbundle.putInt("FROM_BOOK", PassengersActivity.SELECT_PASSENGER);
                passengerbundle.putBoolean("FROM_TRAIN", true);
                passengerbundle.putString("isNeed12306",ModuleConstants.bizTypeTrain);
                passengerIntent.putExtras(passengerbundle);
                startActivityWithAnim(passengerIntent);

                break;

            case R.id.title_bar_right:


                break;
        }
    }

    private void initTicketList() {

        // 对儿童和成人乘客信息list进行遍历处理
        mCreateTicketList.clear();

        mChoosedPassengerList.clear();
        mChoosedPassengerList.addAll(mPassengerAdapter.getChoosedPassenger());

        int i = 0;
        for (NewPassengerInfoModel ticket : mChoosedPassengerList) {
            TicketListModel model = new TicketListModel();
            model.setId(ticket.getId());
            model.setTicketType(ticket.getType());
            model.setSeatType(mChoosedSeatType);
            switch (ticket.getType()) {
                case 1:
                case 3:
                    model.setInsuranceId(mChoosedInsuranceId);
                    break;
                case 2:
                    model.setInsuranceId(mChoosedChildInsuranceId);
                    if (mChildInfoList != null && mChildInfoList.size() > 0) {
                        model.setBirthday(mChildInfoList.get(i).getBirthday());
                        model.setGender(mChildInfoList.get(i).getGender() + "");
                        model.setChildName(mChildInfoList.get(i++).getName());

                    }
                    break;
            }
            mCreateTicketList.add(model);
        }

    }

    private void commitOrder() {
        initTicketList();
        if (checkData()) {
            dialog.show();
            dialog.setInitView();
            dialog.refreshDialog(getString(R.string.fill_in_order_background));
            createPara = new CreateTrainOrderModel();
            createPara.setAcceptStanding(mCreateAcceptstanding);
            createPara.setMobile(mCreateMobie);
            createPara.setCouponId(""); // 暂不支持优惠券
            createPara.setFromStationCode(mCreateFromstationCode);
            createPara.setToStationCode(mCreateTostationCode);
            createPara.setTrainCode(mCreateTrainCode);
            createPara.setStartDate(mCreateStartData);
            createPara.setTicketList(mCreateTicketList);
            createPara.setTrainNo(mTicketLineInfo.getTrainNo());
            loadTicketData();
        }
    }

    private boolean checkData() {// 检查填写的数据是否符合要求

        if (mCreateTicketList.size() == 0) {
            ToastManager.getInstance().showToast(FillinOrderActivity.this, "亲，你还未选择乘客哦");
            return false;
        }
        if (StringUtils.isNullOrEmpty(et_mobilephone)) {
            ToastManager.getInstance().showToast(FillinOrderActivity.this, "请填写联系手机");
            return false;
        } else {
            String reg = "^[1][0-9]{10}$";
            if (!et_mobilephone.getText().toString().matches(reg)) {
                ToastManager.getInstance().showToast(FillinOrderActivity.this, "联系手机格式错误");
                return false;
            } else {
                mCreateMobie = et_mobilephone.getText().toString();
            }
        }

        if (mAdultNum == 0 && mChildNum != 0) {
            ToastManager.getInstance().showToast(FillinOrderActivity.this, "最少添加一位成人乘客");
            return false;
        }
        if (mInsuranceChildNum != 0 && mInsuranceChildNum != mChildNum) {
            ToastManager.getInstance().showToast(FillinOrderActivity.this, "请添加儿童保险信息");
            return false;
        }

        if (!cb_agree.isChecked()) {
            ToastManager.getInstance().showToast(FillinOrderActivity.this, "请阅读并同意《购票须知》与《12308火车票预订协议》");
            return false;
        }

        return true;
    }

    private void loadTicketData() {
        HttpRequestManager.getInstance().trainOrderCreate(createPara, new ApiSubscriber<JsonResult<CommitOrderResponseModel>>(FillinOrderActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<CommitOrderResponseModel> stringJsonResult) {
                if ("0000".equals(stringJsonResult.getResultCode())) {
                    orderId = stringJsonResult.getObject().getOrderId();
                    lockTicket();
                } else {
                    closeDialog();
                    ToastManager.getInstance().showToast(FillinOrderActivity.this, stringJsonResult.getResultMsg());
                }
            }
        });
    }

    //锁座
    private void lockTicket() {
        HttpRequestManager.getInstance().trainCreateResult(orderId, new ApiSubscriber<JsonResult<LockTicketResultEntity>>(FillinOrderActivity.this,ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<LockTicketResultEntity> lockTicketJsonResult) {
                if ("0000".equals(lockTicketJsonResult.getResultCode())) {
                    switch (lockTicketJsonResult.getObject().getStatus()) {
                        case 0:
                            innerHandler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        case 1:
                            closeDialog();
                            Intent intent = new Intent(FillinOrderActivity.this, TrainOrderDetailsActivity.class);
                            intent.putExtra("orderid", lockTicketJsonResult.getObject().getOrderId());
                            startActivityWithAnim(intent);
                            break;
                        case 2:
                            closeDialog();
                            bookFailureDialog = new BookFailureDialog(FillinOrderActivity.this,lockTicketJsonResult.getObject().getMessage());
                            bookFailureDialog.show();
                            break;
                    }
                } else {
                    closeDialog();
                    ToastManager.getInstance().showToast(FillinOrderActivity.this, lockTicketJsonResult.getResultMsg());
                }
            }
        });
    }

    //重新抢票
    public void robTicket() {
        dialog.show();
        dialog.refreshDialog(getString(R.string.fill_in_order_background));
        loadTicketData();
    }

    //关闭dialog
    private void closeDialog() {
        if (dialog != null) {
            dialog.colseFlag();
            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputMethod.closeInputMethod(FillinOrderActivity.this);
        switch (requestCode) {
            case REQUEST_PHONE: // 获取手机号码
                if (resultCode == RESULT_OK && (data != null)) {
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(data.getData(), null, null, null, null);
                    while (cursor.moveToNext()) {
                        //取得电话号码
                        String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

                        if (phone != null) {
                            phone.moveToFirst();
                            String PhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //格式化手机号
                            PhoneNumber = PhoneNumber.replace("-", "");
                            PhoneNumber = PhoneNumber.replace(" ", "");
                            et_mobilephone.setText(PhoneNumber);
                            phone.close();
                        }
                    }
                }

                break;

            case REQUEST_ADULT_INSURANCE: // 成人保险

                if (resultCode == 100) {
                    for (TrainInsuranceModel adult : mAdultModel) {
                        adult.setFlag_checkbox(false);
                    }

                    tv_adultinsurance_price.setText("暂不购买");
                    tv_adultinsurance_infodetails.setText("为了您和家人出行安全，建议您购买保险");
                    mChoosedInsuranceId = "";
                    mChoosedInsurancePrice = -1;

                } else if (resultCode == 101) {

                    int position = data.getIntExtra("adultposition", -1);

                    if (mAdultModel != null) {
                        for (TrainInsuranceModel adult : mAdultModel) {
                            adult.setFlag_checkbox(false);
                        }

                        if (position != -1) {
                            updateInsuranceInfo(position);
                        }
                    }
                }
                break;

            case REQUEST_CHILD_INSURANCE:// 儿童保险
                if (resultCode == 200) {
                    for (TrainInsuranceModel child : mChildModel) {
                        child.setFlag_checkbox(false);
                    }
                    tv_childinsurance_price.setText("暂不购买");
                    mChoosedChildInsuranceId = "";
                    mChildInfoList.clear();
                } else if (resultCode == 201) {
                    int position = data.getIntExtra("childposition", -1);

                    if (mChildModel != null) {
                        for (TrainInsuranceModel child : mChildModel) {
                            child.setFlag_checkbox(false);
                        }
                        if (position != -1) {
                            mChildModel.get(position).setFlag_checkbox(true);
                            tv_childinsurance_price.setText("¥" + (mChildModel.get(position).getMoney() * 0.01) + "x" + mInsuranceChildNum);
                            mChoosedChildInsuranceId = mChildModel.get(position).getInsuranceId();
                            mChoosedChildInsurancePrice = mChildModel.get(position).getMoney();
                        }
                        mChildInfoList.clear();
                        mChildInfoList.addAll((List<ChildInfoModel>) data.getBundleExtra("childlistbundle").getSerializable("childlist"));

                    }
                }
                break;
            case REQUEST_LOGIN:
                if (resultCode == 400) {
                    getUserInfo();
                    initData();
                    getInsuranceInfo();
                } else {
                    // 没有登录成功则dismiss
                    finish();
                }

                break;
        }
        calculateTotal();
    }

    public void onEvent(CheckPassengerEvent event) {

        passengerListview.setVisibility(View.VISIBLE);
        mSelectedPassengerList = event.getList();
        mChoosedPassengerList.clear();
        for (int i = 0; i < mSelectedPassengerList.size(); i++) {
            NewPassengerInfoModel model = new NewPassengerInfoModel();
            model.setType(mSelectedPassengerList.get(i).getPersonType());
            model.setAdultname(mSelectedPassengerList.get(i).getPassengerName());
            model.setCertNo(mSelectedPassengerList.get(i).getCertificateNo());
            model.setCertType(mSelectedPassengerList.get(i).getCertificateType());
            model.setId(mSelectedPassengerList.get(i).getPassengerId());
            mChoosedPassengerList.add(model);
        }
        mChildNum = 0;
        mInsuranceChildNum = 0;
        if (null != mChildInfoList || 0 != mChildInfoList.size()) {
            mChildInfoList.clear();
        }

        mChoosedChildInsuranceId = "";
        for (TrainInsuranceModel child : mChildModel) {
            child.setFlag_checkbox(false);
        }

        mPassengerAdapter.refreshData(mChoosedPassengerList);

        if (passengerListview.getFooterViewsCount() == 0) {
            passengerListview.addFooterView(footview);
        }

        setListViewHeightBasedOnChildren(passengerListview);

        ll_addpassenger.setVisibility(View.GONE);

        calculateTotal();

    }

    // scrollview嵌套listview,重新绘制
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    // 重新计算订单总价，包括总金额和总人数,以及保险等信息
    private void calculateTotal() {

        mAdultNum = 0;
        mChildNum = 0;
        mInsuranceChildNum = mChildInfoList.size();

        for (int i = 0; i < mChoosedPassengerList.size(); i++) {

            if (mChoosedPassengerList.get(i).getType() == 1 || mChoosedPassengerList.get(i).getType() == 3) { // 成人票
                mAdultNum++;
            } else { // 儿童票
                mChildNum++;
            }
        }

        if (mChildNum != 0) {
            ll_childinsurance.setVisibility(View.VISIBLE);
        } else {
            ll_childinsurance.setVisibility(View.GONE);
        }

        if (mChoosedInsurancePrice != -1) {
            tv_adultinsurance_price.setText("¥" + (mChoosedInsurancePrice * 0.01) + "x" + mAdultNum);
        }

        if (mChoosedChildInsurancePrice != -1) {
            tv_childinsurance_price.setText("¥" + (mChoosedChildInsurancePrice * 0.01) + "x" + mInsuranceChildNum);
        }

        if (mChildInfoList.size() == 0) {
            tv_childinsurance_price.setText("暂不购买");
        }

        int insuranceAmount = 0;
        if (mChoosedInsurancePrice != -1) {
            insuranceAmount = insuranceAmount + mChoosedInsurancePrice * mAdultNum;
        }

        if (mChoosedChildInsurancePrice != -1) {
            insuranceAmount = insuranceAmount + mChoosedChildInsurancePrice * mInsuranceChildNum;
        }

        float priceAmount = (float) (mSeatList.get(mBuyPosition).getPrice() * (mAdultNum + mChildNum) * 0.01);

    }

    @Override
    public void addchild(int position) {
        NewPassengerInfoModel info = new NewPassengerInfoModel();
        info.setType(2);
        info.setAdultname(mChoosedPassengerList.get(position).getAdultname());
        info.setId(mChoosedPassengerList.get(position).getId());
        info.setCertType(mChoosedPassengerList.get(position).getCertType());
        info.setCertNo(mChoosedPassengerList.get(position).getCertNo());

        mChildInfoList.clear(); // 填写的儿童保险信息需要清除
        mInsuranceChildNum = 0;
        mChoosedChildInsuranceId = "";
        tv_childinsurance_price.setText("暂不购买");
        for (TrainInsuranceModel child : mChildModel) {
            child.setFlag_checkbox(false);
        }

        mChoosedPassengerList.add(position + 1, info);
        mPassengerAdapter.refreshData(mChoosedPassengerList);
        setListViewHeightBasedOnChildren(passengerListview);
        calculateTotal();
    }

    @Override
    public void remove(int position) {

        String positionId = mChoosedPassengerList.get(position).getId();

        if (mChoosedPassengerList.get(position).getType() == 1 || mChoosedPassengerList.get(position).getType() == 3) { // 如果是成人

            for (int i = mChoosedPassengerList.size() - 1; i >= 0; i--) {

                if (mChoosedPassengerList.get(i).getId().equals(positionId)) {
                    for (int j = 0; j < mSelectedPassengerList.size(); j++) {
                        if (mSelectedPassengerList.get(j).getPassengerId().equals(mChoosedPassengerList.get(i).getId())) {
                            mSelectedPassengerList.remove(j);
                        }
                    }
                    mChoosedPassengerList.remove(i);
                }

            }
        } else { // 如果是儿童
            for (int i = mChoosedPassengerList.size() - 1; i >= 0; i--) {
                if (mChoosedPassengerList.get(i).getId().equals(positionId)) {
                    for (int j = 0; j < mSelectedPassengerList.size(); j++) {
                        if (mSelectedPassengerList.get(j).getPassengerId().equals(mChoosedPassengerList.get(i).getId())) {
                            mSelectedPassengerList.remove(j);
                        }
                    }
                }
            }
            mChoosedPassengerList.remove(position);
            mChildInfoList.clear(); // 填写的保险信息需要清除
            mInsuranceChildNum = 0;
            mChoosedChildInsuranceId = "";
            tv_childinsurance_price.setText("暂不购买");
            for (TrainInsuranceModel child : mChildModel) {
                child.setFlag_checkbox(false);
            }
        }

        if (mChoosedPassengerList.size() == 0) {
            ll_addpassenger.setVisibility(View.VISIBLE);
            passengerListview.removeFooterView(footview);
        }

        mPassengerAdapter.refreshData(mChoosedPassengerList);
        setListViewHeightBasedOnChildren(passengerListview);
        calculateTotal();
    }

    private static class InnerHandler extends Handler {

        WeakReference<FillinOrderActivity> weakReference;

        public InnerHandler(FillinOrderActivity fillinOrderActivity) {
            this.weakReference = new WeakReference<>(fillinOrderActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            FillinOrderActivity fillinOrderActivity = weakReference.get();
            if (fillinOrderActivity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    fillinOrderActivity.lockTicket();
                    break;
            }
        }
    }

    @Subscribe //刷新页面
    public void onEditPassengerEvent(EditPassengerEvent passengerEvent) {

        if ("newPerson".equals(passengerEvent.getNewPerson())) {
            for (int i = 0; i < mChoosedPassengerList.size(); i++) {
                if (passengerEvent.getPassenger().getPassengerId().equals(mChoosedPassengerList.get(i).getId())) {
                    NewPassengerInfoModel newPassengerInfoModel = new NewPassengerInfoModel();
                    newPassengerInfoModel.setCertNo(passengerEvent.getPassenger().getCertificateNo());
                    newPassengerInfoModel.setCertType(passengerEvent.getPassenger().getCertificateType());
                    newPassengerInfoModel.setId(passengerEvent.getPassenger().getPassengerId());
                    newPassengerInfoModel.setType(passengerEvent.getPassenger().getPersonType());
                    newPassengerInfoModel.setAdultname(passengerEvent.getPassenger().getPassengerName());
                    mChoosedPassengerList.set(i, newPassengerInfoModel);
                }
            }
            if (mChoosedPassengerList.size() == 1 && 2 == mChoosedPassengerList.get(0).getType()) {
                mChoosedPassengerList.clear();
                ll_addpassenger.setVisibility(View.VISIBLE);

                passengerListview.removeFooterView(footview);

                passengerListview.setVisibility(View.GONE);
                setListViewHeightBasedOnChildren(passengerListview);

            }
            mPassengerAdapter.refreshData(mChoosedPassengerList);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
        if (dialog != null) {
            dialog.colseFlag();
            dialog.cancel();
        }
        innerHandler.removeMessages(0);
    }
}