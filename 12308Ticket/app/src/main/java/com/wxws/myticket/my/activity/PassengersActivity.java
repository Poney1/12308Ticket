package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.adapter.PassengerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 选择乘客
 * Date: 2016/11/7 15:09
 *
 * @auther: lixiangxiang
 */
public class PassengersActivity extends BaseActivity{

    //传统汽车票
    public static final int TRADITIONALPASSENGER = 1;
    //    火车票
    public static final int TRAINPASSENGER = 2;
    //    个人中心
    public static final int SELECT_PASSENGER = 3;

    //修改联系人
    public static final int MODIFY_PASSENGER = 4;

    public static final int SELECTSINGLE = 5;//单选
    public static final int SELECTMUL = 6;//多选

    /**
     *
     */
    private LinearLayout llAdd;
    private SwipeListView swipeListView;
    private TextView tvAddPassenger;
    private LinearLayout llPrompt;

    private PassengerListAdapter passengerListAdapter;//
    //intent 数据
    private int flag;//来源标记
    private boolean isTrain;
    private String isNeed;
    private ArrayList<PassengerInfoEntity> selectList;//选中的
    private List<PassengerInfoEntity>  passList;//所有的用户
    private boolean pickTicket;
    private String bizType;
    private int selectOrigin;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        RxBus.getInstance().register(this);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {
        llAdd.setOnClickListener(this);
    }

    private void initData() {

        switch (flag){
            case TRADITIONALPASSENGER:
                barTitle.setText(R.string.passenger_select);
                tvRight.setText(R.string.passenger_ok);
                flTitleBarRight.setVisibility(View.VISIBLE);
                break;
        }
        passList = new ArrayList<>();
        passengerListAdapter = new PassengerListAdapter(PassengersActivity.this,flag,selectList,swipeListView,passList);
        passengerListAdapter.setSelectFlag(selectOrigin);
        swipeListView.setAdapter(passengerListAdapter);
        getData(bizType);
    }

    private void initView() {
        bindTitleViews();
        bindLoadingView();
        llAdd = (LinearLayout) findViewById(R.id.layout_add);
        llPrompt = (LinearLayout) findViewById(R.id.ll_prompt);
        swipeListView = (SwipeListView) findViewById(R.id.swipe_list);
        tvAddPassenger = (TextView) findViewById(R.id.tv_add_passenger);
    }

    private void getPreData() {
        flag = getIntent().getIntExtra("from",0);
        bizType = getIntent().getStringExtra("bizType");
        selectList = (ArrayList<PassengerInfoEntity>) getIntent().getSerializableExtra("selectPassenger");
        selectOrigin = getIntent().getIntExtra("mulSelect",0);
        isTrain = getIntent().getBooleanExtra("fromTrain", false);
        isNeed = getIntent().getStringExtra("isNeed12306");
        pickTicket = getIntent().getBooleanExtra("pickTicket",false);
    }

    /**
     * 获取数据
     */
    private void getData(String bizType){
        HttpRequestManager.getInstance().getContactList(bizType, new ApiSubscriber<JsonResult<List<PassengerInfoEntity>>>(PassengersActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<PassengerInfoEntity>> passengerInfoEntityJsonResult) {
                if (passengerInfoEntityJsonResult == null)
                    return;
                if ("0000".equals(passengerInfoEntityJsonResult.getResultCode())){
                    passList = passengerInfoEntityJsonResult.getObject();
                    passengerListAdapter.replaceAll(passList);
                }else {
                    ToastManager.getInstance().showToast(PassengersActivity.this,passengerInfoEntityJsonResult.getResultMsg());
                }
            }
        });
    }

    //接收添加或者修改 联系人
    @Subscribe
    public void onRecievedPasseneger(PassengerInfoEntity passengerInfoEntity){
        if (passengerInfoEntity == null)
            return;
        int i = -1;
        for (int j=0 ;j<passList.size();j++){
            if (passList.get(j).getPassengerId().equals(passengerInfoEntity.getPassengerId())){
                i = j;
                break;
            }
        }
        if (i == -1){
            passList.add(0,passengerInfoEntity);
        }else {
            passList.set(i,passengerInfoEntity);
        }
        passengerListAdapter.replaceAll(passList);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent  intent  = null ;
        switch (v.getId()){
            case R.id.layout_add:
                if (flag == TRADITIONALPASSENGER){
                    intent  = new Intent(PassengersActivity.this,EditPassengersActivity.class);
                    startActivityWithAnim(intent);
                }else if (flag == TRAINPASSENGER){
                    intent  = new Intent(PassengersActivity.this,EditTrainPassengersActivity.class);
                    startActivityWithAnim(intent);
                }
                break;
            case R.id.tv_right:
                passengerListAdapter.postPassenger();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
}
