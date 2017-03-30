package com.wxws.myticket.my.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.widgets.view.ClearEditText;


/**
 * desc: 修改联系人
 * Date: 2016/11/7 16:51
 *
 * @auther: lixiangxiang
 */
public class EditPassengersActivity extends BaseActivity implements TextWatcher{

    private ClearEditText clName;
    private ClearEditText clNo;
    private ClearEditText clPhone;
    private Button btnPassenger;

    private PassengerInfoEntity passengerInfoEntity;
    private String name;
    private String number;
    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_passenger);
        RxBus.getInstance().register(this);
        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {
        clName.addTextChangedListener(this);
        clNo.addTextChangedListener(this);
        clPhone.addTextChangedListener(this);
        btnPassenger.setOnClickListener(this);
    }

    private void initData() {

        barTitle.setText(getString(R.string.passenger_add));
        if (passengerInfoEntity!=null){
            barTitle.setText(getString(R.string.passenger_modify));
            clName.setText(passengerInfoEntity.getPassengerName());
            clNo.setText(passengerInfoEntity.getCertificateNo());
            clPhone.setText(passengerInfoEntity.getMobilePhone());
        }
    }

    private void initView() {
        bindTitleViews();
        clName = (ClearEditText) findViewById(R.id.et_passenger_name);
        clNo = (ClearEditText) findViewById(R.id.et_passenger_no);
        clPhone = (ClearEditText) findViewById(R.id.et_passenger_phone);
        btnPassenger = (Button) findViewById(R.id.btn_passenger_save);
        getInputData();
        unEnableAdd();
    }

    private void getPreData() {
        passengerInfoEntity = (PassengerInfoEntity) getIntent().getSerializableExtra("passenger");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        getInputData();
        unEnableAdd();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_passenger_save:
                addPassenger();
                break;
        }
    }

    /**
     * 添加
     */
    private void unEnableAdd(){
        if (!StringUtils.isNullOrEmpty(name) && !StringUtils.isNullOrEmpty(number) && !StringUtils.isNullOrEmpty(phone) ){
            btnPassenger.setEnabled(true);
        }else {
            btnPassenger.setEnabled(false);
        }
    }

    /**
     * 获取输入的信息
     */
    private void getInputData(){
        name = clName.getText().toString().trim();
        number = clNo.getText().toString().trim();
        phone = clPhone.getText().toString().trim();
    }

    //添加乘客
    private void  addPassenger(){
        HttpRequestManager.getInstance().addContactList(name,phone,number,"0",null,null,null,null,null,null,null,null,null,null,null,
                null,null,null,null, new ApiSubscriber<JsonResult<PassengerInfoEntity>>(EditPassengersActivity.this) {
                    @Override
                    public void onNext(JsonResult<PassengerInfoEntity> stringJsonResult) {
                            if ("0000".equals(stringJsonResult.getResultCode())){
                                RxBus.getInstance().post(stringJsonResult.getObject());
                                finish();
                            }else {
                                ToastManager.getInstance().showToast(EditPassengersActivity.this,stringJsonResult.getResultMsg());
                            }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
}
