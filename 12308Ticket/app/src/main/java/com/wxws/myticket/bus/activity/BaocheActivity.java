package com.wxws.myticket.bus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.entity.BaocheBeforetimePara;
import com.wxws.myticket.bus.entity.BaochePOIEntity;
import com.wxws.myticket.bus.entity.GetPOIEvent;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.activity.LoginActivity;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * desc: 包车填写页面
 * Date: 2016-11-18 14:19
 *
 * @author jiangyan
 */
public class BaocheActivity extends BaseActivity {


    private TextView mTvUseTime;
    private CheckBox mCbAgree;
    private EditText mCetName, mCetPhone, mEtNum;
    private TextView mTvStartPlace, mTvEndPlace;
    private TextView mTvProtocol;
    private int userAcount;
    private Button mBtChooseCar;

    private String userName;
    private String userPhone;
    private String startTime;
    private String startCity;
    private String startPlace;
    private String endCity;
    private String endPlace;
    private String startLat;
    private String startLng;
    private String endLat;
    private String endLng;
    private String kilometre;

    private int lineType = 0;
    BaochePOIEntity startPoi, endPoi;
    TimePickerView pvTime;
    private int beforeTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baoche);
        RxBus.getInstance().register(this);

        iniTitle();

        getUserInfo();
        initView();
        initData();
        timePicker();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void iniTitle() {
        bindTitleViews();
        barTitle.setText("我要包车");
    }

    private void initView() {

        mCetName = (EditText) findViewById(R.id.cet_baoche_name);
        mCetPhone = (EditText) findViewById(R.id.cet_baoche_phone);
        mTvStartPlace = (TextView) findViewById(R.id.tv_baoche_startplace);
        mTvEndPlace = (TextView) findViewById(R.id.tv_baoche_endplace);
        mTvUseTime = (TextView) findViewById(R.id.tv_baoche_usetime);

        mEtNum = (EditText) findViewById(R.id.person_acount);
        mCbAgree = (CheckBox) findViewById(R.id.cb_baoche_agree);
        mTvProtocol = (TextView) findViewById(R.id.tv_baoche_protocol);
        mBtChooseCar = (Button) findViewById(R.id.bt_baoche_choosecar);

        mTvStartPlace.setOnClickListener(this);
        mTvEndPlace.setOnClickListener(this);
        mTvUseTime.setOnClickListener(this);
        mTvProtocol.setOnClickListener(this);
        mBtChooseCar.setOnClickListener(this);

        mCbAgree.setChecked(true);
        mCbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mBtChooseCar.setEnabled(true);
                } else {
                    mBtChooseCar.setEnabled(false);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_baoche_choosecar:

                if (getVerify()) {
                    if (userInfo == null) {
                        ToastManager.getInstance().showToast(BaocheActivity.this, "您还未登录,请先登录!");
                        startActivityWithAnim(LoginActivity.class);
                    } else {
                        startCity = startPoi.getCity();
                        endCity = endPoi.getCity();
                        startPlace = startPoi.getKey();
                        endPlace = endPoi.getKey();

                        Intent intent = new Intent(BaocheActivity.this, BaocheSelectCarActivity.class);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userPhone", userPhone);
                        intent.putExtra("startTime", mTvUseTime.getText().toString());
                        intent.putExtra("startCity", startCity);
                        intent.putExtra("startPlace", startPlace);
                        intent.putExtra("endCity", endCity);
                        intent.putExtra("endPlace", endPlace);
                        intent.putExtra("userAcount", userAcount); // 乘客人数

                        if (startLat != null && startLng != null && endLat != null && endLng != null) {
                            intent.putExtra("startLat", startLat);
                            intent.putExtra("startLng", startLng);
                            intent.putExtra("endLat", endLat);
                            intent.putExtra("endLng", endLng);
                        }
                        startActivityWithAnim(intent);

                    }
                }
                break;

            case R.id.tv_baoche_protocol:
                Intent intent1 = new Intent(BaocheActivity.this, BaocheProtocolActivity.class);
                startActivityWithAnim(intent1);
                break;
            case R.id.tv_baoche_usetime:
                pvTime.show();
                //强制隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.tv_baoche_startplace:
            case R.id.tv_baoche_endplace:
                Intent intent2 = new Intent(BaocheActivity.this, BaochePOIActivity.class);
                intent2.putExtra("editId", v.getId());
                startActivityWithAnim(intent2);
                break;
        }
    }

    private void initData() {

        HttpRequestManager.getInstance().bcPreTime(new ApiSubscriber<JsonResult<BaocheBeforetimePara>>(BaocheActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<BaocheBeforetimePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    if (result.getObject() != null) {
                        beforeTime = result.getObject().getBeforeTime();
                    }
                }
            }
        });
    }

    public boolean getVerify() {
        userName = mCetName.getText().toString();
        userPhone = mCetPhone.getText().toString();
        startTime = mTvUseTime.getText().toString() + ":00";
        startPlace = mTvStartPlace.getText().toString();
        endPlace = mTvEndPlace.getText().toString();

        if (userName.equals("")) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "请输入联系人姓名");
            return false;
        }
        if (!StringUtils.isHaveChinaEnglish(userName)) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "联系人只能是中文或者英文");
            return false;
        }
        if (userPhone.equals("")) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "请输入联系方式");
            return false;
        }
        if (!StringUtils.isPhone(userPhone)) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "您输入的手机号码格式不正确!");
            return false;
        }
        if (startTime.equals(":00")) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "请选择用车时间");
            return false;
        }
        if (startPlace.equals("")) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "请选择出发点");
            return false;
        }
        if (endPlace.equals("")) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "请选择到达点");
            return false;
        }
        if (!mEtNum.getText().toString().equals("") && Integer.parseInt(mEtNum.getText().toString()) > 0 && Integer.parseInt(mEtNum.getText().toString()) <= 99999) {
            userAcount = Integer.parseInt(mEtNum.getText().toString());
        } else if (!mEtNum.getText().toString().equals("") && Integer.parseInt(mEtNum.getText().toString()) >= 99999) {
            ToastManager.getInstance().showToast(BaocheActivity.this, "人数须小于100000");
            return false;
        } else {
            ToastManager.getInstance().showToast(BaocheActivity.this, "人数必须大于0");
            return false;
        }
        if (startPoi.getLatLng() != null) {
            startLat = new DecimalFormat("0.000000").format(Double.parseDouble(startPoi.getLatLng().latitude + ""));
            startLng = new DecimalFormat("0.000000").format(Double.parseDouble(startPoi.getLatLng().longitude + ""));
        }
        if (endPoi.getLatLng() != null) {
            endLat = new DecimalFormat("0.000000").format(Double.parseDouble(endPoi.getLatLng().latitude + ""));
            endLng = new DecimalFormat("0.000000").format(Double.parseDouble(endPoi.getLatLng().longitude + ""));
        }

        Log.e("mudo", "lllll:" + startLng + ":" + startLat);
        if (startLat != null && startLng != null && endLat != null && endLng != null) {
            if ((startLat + startLng).equals(endLat + endLng)) {
                ToastManager.getInstance().showToast(BaocheActivity.this, "出发点和到达点不能相同");
                return false;
            }
        }

        return true;

    }

    private void timePicker() {
        //时间选择器
        pvTime = new TimePickerView(BaocheActivity.this, TimePickerView.Type.ALL);
        pvTime.setRange(2016, 2020);
        pvTime.setCyclic(true);
        pvTime.setTime(DateTimeUtil.addMinute(DateTimeUtil.addDay(new Date(), 2), 10));
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Date sysDate = DateTimeUtil.StringToDate(DateTimeUtil.getSysTimeYMDHM(), "yyyy-MM-dd HH:mm");
                Date d1 = DateTimeUtil.addHour(sysDate, beforeTime);
                if (DateTimeUtil.compareTo(DateTimeUtil.getDate(date, "yyyy-MM-dd HH:mm"), DateTimeUtil.getDate(d1, "yyyy-MM-dd HH:mm"))) {
                    mTvUseTime.setText(DateTimeUtil.getDate(date, "yyyy-MM-dd HH:mm"));
                } else {
                    ToastManager.getInstance().showToast(BaocheActivity.this, "您须提前" + beforeTime + "小时下单");
                }
            }
        });
    }

    // TODO 回调

    @Subscribe
    public void loginRefresh(CommonEvent event) {
        switch (event.getFlag()) {
            case CommonEvent.LOGIN_SUCCESS:
                getUserInfo();
                break;
            case CommonEvent.CREATE_WYBC_ORDER:
                finish();
                break;
        }
    }

    @Subscribe
    public void placeUpdate(GetPOIEvent event) {
        if (event != null && event.getPoiInfo() != null) {
            if (event.getEditId() == R.id.tv_baoche_startplace) {
                mTvStartPlace.setText(event.getPoiInfo().getKey());
                startPoi = event.getPoiInfo();
                startCity = startPoi.getCity();
            } else {
                endPoi = event.getPoiInfo();
                mTvEndPlace.setText(event.getPoiInfo().getKey());
                endCity = endPoi.getCity();
            }
        }
    }
}