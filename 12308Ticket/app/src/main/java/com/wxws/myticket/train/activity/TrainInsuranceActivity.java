package com.wxws.myticket.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.train.adapter.TrainInsuranceAdapter;
import com.wxws.myticket.train.entity.ChildInfoModel;
import com.wxws.myticket.train.entity.TrainInsuranceModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * Date: 2016/11/17 18:11
 *
 * @auther: lixiangxiang
 */
public class TrainInsuranceActivity extends BaseActivity {

    private RecyclerView rvl_insuranceList;
    private Button btn_insurance;
    private Button btn_noinsurance;
    private int mType;
    private int mCount;
    private int mChildInsuranceNum;
    private List<TrainInsuranceModel> mChildModel;
    private List<TrainInsuranceModel> mAdultModel;
    private TrainInsuranceAdapter mInsuranceAdapter;
    private List<ChildInfoModel> childList = new ArrayList<ChildInfoModel>(); // 儿童保险，姓名生日等数据

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_train_insurance);

        bindTitleViews();

        getDateFromPre();

        initTitleView();   // 顺序不可修改，根据pre获得类型再去初始化title

        initView();

        initData();
    }

    public void getDateFromPre() {
        // 0为儿童，1为成人
        mType = getIntent().getIntExtra("type", -1);
        // 保险份数
        mCount = getIntent().getIntExtra("num", 0);
        // 添加了保险信息的儿童数量
        mChildInsuranceNum = getIntent().getIntExtra("childinfosize", 0);

        // 保险信息
        if (mType == 0) { // 儿童类型

            mChildModel = (List<TrainInsuranceModel>) getIntent().getBundleExtra("childinfo").getSerializable("childlist");
            childList = (List<ChildInfoModel>) getIntent().getBundleExtra("childinfo").getSerializable("childinfolist");

        } else if (mType == 1) { // 成人类型
            mAdultModel = (List<TrainInsuranceModel>) getIntent().getBundleExtra("adultinfo").getSerializable("adultlist");
        }

    }

    public void initTitleView() {
        switch (mType) {
            case 0:
                barTitle.setText("填写儿童保险信息");
                break;
            case 1:
                barTitle.setText("选择成人保险信息");
                break;
            default:
                finish(); // 传过来的数据类型有问题，直接finish
                break;
        }
    }

    public void initView() {
        // 初始化需要用到的控件
        rvl_insuranceList = (RecyclerView) findViewById(R.id.rlv_insurance_container);

        btn_insurance = (Button) findViewById(R.id.btn_insurance_confirm);
        btn_noinsurance = (Button) findViewById(R.id.btn_insurance_noconfirm);
        btn_insurance.setOnClickListener(this);
        btn_noinsurance.setOnClickListener(this);

        if (mType == 1) {
            btn_insurance.setVisibility(View.GONE);
        } else {
            btn_insurance.setVisibility(View.VISIBLE);
        }

        mInsuranceAdapter = new TrainInsuranceAdapter(TrainInsuranceActivity.this, mType, mCount, mChildInsuranceNum);
        mInsuranceAdapter.setOnCheckboxChoosedListener(new TrainInsuranceAdapter.OnCheckboxChoosedListener() {

            @Override
            public void choosed(int position, int type) {
                if (type == 0) { // 儿童保险

                    for (int i = 0; i < mChildModel.size(); i++) {
                        if (i == position) {
                            mChildModel.get(i).setFlag_checkbox(true);
                        } else {
                            mChildModel.get(i).setFlag_checkbox(false);
                        }
                    }
                    mInsuranceAdapter.refreshData(mChildModel, childList);

                } else if (type == 1) { // 成人保险

                    for (int i = 0; i < mAdultModel.size(); i++) {
                        if (i == position) {
                            mAdultModel.get(i).setFlag_checkbox(true);
                        } else {
                            mAdultModel.get(i).setFlag_checkbox(false);
                        }
                    }

                    Intent intent = new Intent();
                    intent.putExtra("adultposition", position);
                    setResult(101, intent);
                    finish();
                }

            }

        });
        rvl_insuranceList.setLayoutManager(new LinearLayoutManager(this));
        rvl_insuranceList.setAdapter(mInsuranceAdapter);

    }

    public void initData() {
        // 填充数据
        if (mType == 0) {
            mInsuranceAdapter.refreshData(mChildModel, childList);
        } else {
            mInsuranceAdapter.refreshData(mAdultModel, childList);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_insurance_confirm: // 购买保险
                boolean has_choosed = false;
                if (mType == 0) { // 儿童保险，需要传儿童信息过去
                    for (int i = 0; i < mChildModel.size(); i++) {
                        if (mChildModel.get(i).isFlag_checkbox()) {
                            has_choosed = true;
                            if (mInsuranceAdapter.checkDataComplete()) {
                                childList = mInsuranceAdapter.getChildInfoList();
                            } else {
                                return;

                            }

                            if (null != childList) {
                                Intent childIntent = new Intent();
                                childIntent.putExtra("childposition", i);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("childlist", (Serializable) childList);
                                childIntent.putExtra("childlistbundle", bundle);
                                setResult(201, childIntent);
                            }
                        }
                    }

                    if (!has_choosed) {
                        ToastManager.getInstance().showToast(this, "请先选择一个保险");
                        return;
                    }

                }
                break;
            case R.id.btn_insurance_noconfirm: // 不买保险
                if (mType == 0) { // 儿童保险
                    setResult(200);
                } else { // 成人保险
                    setResult(100);
                }
                break;
        }

        finish();
    }
}
