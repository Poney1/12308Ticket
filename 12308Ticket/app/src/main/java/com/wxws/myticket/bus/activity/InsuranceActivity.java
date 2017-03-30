package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.InsuranceAdapter;
import com.wxws.myticket.bus.entity.BusInsuranceEntity;
import com.wxws.myticket.bus.entity.InsuranceSelectEntity;
import com.wxws.myticket.common.rx.bus.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 保险列表
 * Date: 2016/10/27 14:10
 *
 * @auther: lixiangxiang
 */
public class InsuranceActivity extends BaseActivity {

    private ListView lvInsurance;
    private ArrayList<BusInsuranceEntity> insurance;
    private BusInsuranceEntity isRecommend;
    private TextView tvNoPay;// 不支付 目标
    private InsuranceAdapter insuranceAdapter;
    private int title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

        RxBus.getInstance().register(this);

        getPreData();

        initView();

        initData();
    }

    private void getPreData() {
        title = getIntent().getIntExtra("titleId",0);
        switch (title){
            case  R.string.title_list_insurance:
                insurance = (ArrayList<BusInsuranceEntity>) getIntent().getSerializableExtra("insurance");
                isRecommend =(BusInsuranceEntity) getIntent().getSerializableExtra("isRecommend");
                if (insurance!=null && isRecommend != null){
                    for (BusInsuranceEntity bus : insurance){
                        if (isRecommend.getInsuranceId().equals(bus.getInsuranceId())){
                            bus.setSelect(true);
                            break;
                        }
                    }
                }
                break;
            case R.string.title_list_station:

                break;
        }

    }

    private void initData() {
        switch (title){
            case  R.string.title_list_insurance:
                barTitle.setText(getString(R.string.title_list_insurance));
                initInsurance();
                bindClick();
                break;
            case R.string.title_list_station:
                barTitle.setText(getString(R.string.title_list_station));
                break;
            default:
                break;
        }
    }

    private void bindClick() {
        tvNoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post(new InsuranceSelectEntity(InsuranceSelectEntity.noSelect,null));
                finish();
            }
        });
        lvInsurance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < insurance.size()){
                    insurance.get(position).setSelect(true);
                    insuranceAdapter.replaceAll(insurance);
                    RxBus.getInstance().post(new InsuranceSelectEntity(InsuranceSelectEntity.noSelect,insurance.get(position)));
                    finish();
                }
            }
        });

    }

    private void initInsurance(){
        insuranceAdapter = new InsuranceAdapter(InsuranceActivity.this,insurance);
        lvInsurance.setAdapter(insuranceAdapter);
    }
    private void initView() {
        lvInsurance = (ListView) findViewById(R.id.lv_insurance);
        tvNoPay = (TextView) findViewById(R.id.tv_no_pay);
        bindTitleViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
}
