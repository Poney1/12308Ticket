package com.wxws.myticket.bus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.DirectLineDescAdapter;
import com.wxws.myticket.bus.adapter.DirectStationSelectAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.DirectStopoverStationList;
import com.wxws.myticket.bus.entity.LineDetailEntity;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.my.activity.H5CommonActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc: 直通车选择上车点
 * Date: 2016/11/4 15:47
 *
 * @auther: lixiangxiang
 */
public class DirectSelectStationActivity extends BaseActivity {

    /**
     * Ffind
     */
    private ImageView businessLogo;
    private TextView businessName;
    private TextView tvScore;
    private TextView tvStartDate;
    private TextView tvContainNoon;
    private TextView tvContainTicket;
    private TextView tvStartStation;
    private TextView tvEndStation;
    private TextView tvIsStartStation;
    private TextView tvCityLinePrice;
    private TextView tvIsHave;
    private TextView tvGuide;
    private TextView tvLineGuide;
    private LinearLayout llQueryMap;
    private ListViewForScrollView lvSelectUp;
    private ListViewForScrollView lvSelectDown;
    private RatingBar rbStars;
    private Button btnOrder;
    private ListViewForScrollView lvDesc;


    private String dapartDate;
    private LineDetailEntity mLineDetail;
    private DirectStationSelectAdapter directStationSelectAdapter;
    private DirectLineDescAdapter descAdapter;
    private List<DirectStopoverStationList> stopList;
    private List<DirectStopoverStationList> stopListUp;
    private List<DirectStopoverStationList> stopListDown;
    private List<String> stopListAll;
    private DirectStationSelectAdapter directDownAdapter;
    private String bizType;
    private TraditionBusListEntity singleBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_direct_station);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void getPreData() {
        dapartDate = getIntent().getStringExtra("departDate");
        singleBus = (TraditionBusListEntity)getIntent().getSerializableExtra("singleBus");
        bizType = getIntent().getStringExtra("bizType");
    }

    private void bindClick() {
        btnOrder.setOnClickListener(this);
        llQueryMap.setOnClickListener(this);
        //乘车退票指南
        tvGuide.setOnClickListener(this);
        //打电话
        tvLineGuide.setOnClickListener(this);
        lvSelectUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stopListUp != null && stopListUp.size() > 0) {
                    for (int i = 0; i < stopListUp.size(); i++) {
                        if (i == position) {
                            stopListUp.get(position).setSelect(true);
                        } else {
                            stopListUp.get(position).setSelect(false);
                        }
                    }
                }
            }
        });

        lvSelectDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stopListDown != null && stopListDown.size() > 0) {
                    for (int i = 0; i < stopListDown.size(); i++) {
                        if (i == position) {
                            stopListDown.get(position).setSelect(true);
                        } else {
                            stopListDown.get(position).setSelect(false);
                        }
                    }
                }
            }
        });
    }

    private void initData() {
        stopListUp = new ArrayList<>();
        stopListDown = new ArrayList<>();
        stopListAll = new ArrayList<>();
        directStationSelectAdapter = new DirectStationSelectAdapter(DirectSelectStationActivity.this, stopListUp);
        directStationSelectAdapter.isUp(true);
        lvSelectUp.setAdapter(directStationSelectAdapter);
        directDownAdapter = new DirectStationSelectAdapter(DirectSelectStationActivity.this, stopListDown);
        directDownAdapter.isUp(false);
        lvSelectDown.setAdapter(directDownAdapter);

        descAdapter = new DirectLineDescAdapter(DirectSelectStationActivity.this, stopListAll);
        lvDesc.setAdapter(descAdapter);

        getBusDesc();
    }

    private void initView() {
        bindTitleViews();

        bindLoadingView();

        businessLogo = (ImageView) findViewById(R.id.business_logo);
        businessName = (TextView) findViewById(R.id.business_name);
        //得分
        tvScore = (TextView) findViewById(R.id.tv_score);
        //出发日期
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        //报午餐
        tvContainNoon = (TextView) findViewById(R.id.tv_contain_noon);
        //包票
        tvContainTicket = (TextView) findViewById(R.id.tv_contain_ticket);
        //出发站点
        tvStartStation = (TextView) findViewById(R.id.tv_start_station);
        //到达站点
        tvEndStation = (TextView) findViewById(R.id.tv_end_station);
        //是否途径
        tvIsStartStation = (TextView) findViewById(R.id.tv_is_start_station);
        //车票价格
        tvCityLinePrice = (TextView) findViewById(R.id.tv_city_line_price);
        //是否有票
        tvIsHave = (TextView) findViewById(R.id.tv_is_have);
        //乘车退票指南
        tvGuide = (TextView) findViewById(R.id.tv_guide);
        //打电话
        tvLineGuide = (TextView) findViewById(R.id.tv_line_guide);
        //路线图
        llQueryMap = (LinearLayout) findViewById(R.id.ll_query_map);
        //上车点集合
        lvSelectUp = (ListViewForScrollView) findViewById(R.id.lv_select_up);
        //下车点集合 也许用不到
        lvSelectDown = (ListViewForScrollView) findViewById(R.id.lv_select_down);
        //说明
        lvDesc = (ListViewForScrollView) findViewById(R.id.lv_desc);
        //星星
        rbStars = (RatingBar) findViewById(R.id.rb_stars);
        //
        btnOrder = (Button) findViewById(R.id.btn_order);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_order:
                if (mLineDetail !=null){
                    intent = new Intent(DirectSelectStationActivity.this, DirectFillInOrderActivity.class);
                    intent.putExtra("lineDetail",mLineDetail);
                    startActivityWithAnim(intent);
                }
                break;
            case R.id.ll_query_map:
                if (mLineDetail !=null && mLineDetail.getStopoverStationList() != null && mLineDetail.getStopoverStationList().size() > 0) {
                    intent = new Intent(DirectSelectStationActivity.this, MapActivity.class);
                    intent.putExtra("stopOverList", (Serializable) mLineDetail.getStopoverStationList());
                    startActivityWithAnim(intent);
                }
                break;
            case R.id.tv_guide:
                intent = new Intent(DirectSelectStationActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", mLineDetail.getHelpInfoUrl());
                startActivityWithAnim(intent);
                break;
            case R.id.tv_line_guide:
                String phone = mLineDetail.getServicePhone();
                if (!StringUtils.isNullOrEmpty(phone)) {
                    try {
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception ex) {
                        ToastManager.getInstance().showToast(DirectSelectStationActivity.this, "呼叫失败");
                    }
                } else {
                    ToastManager.getInstance().showToast(DirectSelectStationActivity.this, "暂无联系方式");
                }
                break;
        }
    }

    //获取班次详情
    private void getBusDesc() {
        HttpRequestManager.getInstance().getLineDetail(bizType,dapartDate, singleBus.getScheduleId(),
                new ApiSubscriber<JsonResult<LineDetailEntity>>(DirectSelectStationActivity.this, ApiSubscriber.NETVIEWLOADING) {
                    @Override
                    public void onNext(JsonResult<LineDetailEntity> lineDetailJsonResult) {
                        if ("0000".equals(lineDetailJsonResult.getResultCode())) {
                            mLineDetail = lineDetailJsonResult.getObject();
                            refreshView();
                        }
                    }
                });
    }

    private void refreshView() {
        barTitle.setText(String.format("%s-%s", mLineDetail.getStartCityName(), mLineDetail.getEndCityName()));

        tvScore.setText(String.format(getString(R.string.direct_business_score), mLineDetail.getStarLevel()));
//        rbStars.setRating(Float.parseFloat(mLineDetail.getStarLevel()));
        businessName.setText(mLineDetail.getTransportCompany());
        tvStartDate.setText(mLineDetail.getStartDate());

        if (mLineDetail.getLabelInfoList() != null && mLineDetail.getLabelInfoList().size() > 0) {
            tvContainNoon.setText(mLineDetail.getLabelInfoList().get(0));
            if (mLineDetail.getLabelInfoList().size() > 1)
                tvContainTicket.setText(mLineDetail.getLabelInfoList().get(1));
        }

        tvStartStation.setText(mLineDetail.getStartStationName());
        //到达站点
        tvEndStation.setText(mLineDetail.getEndStationName());
        //是否途径
        if ("1".equals(mLineDetail.getIsStartStation())) {
            tvIsStartStation.setText("途径");
        } else {
            tvIsStartStation.setText("始发");
        }
        //车票价格
        tvCityLinePrice.setText(String.valueOf(ArithUtils.div(mLineDetail.getPrice().getDiscountPrice(), 100.00)));
        //是否有票
        switch (mLineDetail.getStatus()) {
            case Constants.busInternetStop:
                tvIsHave.setText(getString(R.string.bus_list_internet_stop));
                break;
            case Constants.busHaveTicket:
                tvIsHave.setText(getString(R.string.bus_list_have_ticket));
                break;
            case Constants.busSaleAll:
                tvIsHave.setText(getString(R.string.bus_list_sale_all));
                break;
            case Constants.busStop:
                tvIsHave.setText(getString(R.string.bus_list_stop));
                break;
        }

        stopList = mLineDetail.getStopoverStationList();

        if (stopList != null && stopList.size() > 0) {
            for (DirectStopoverStationList direct : stopList) {
                if ("0".equals(direct.getType())) {
                    stopListUp.add(direct);
                } else if ("1".equals(direct.getType())) {
                    stopListDown.add(direct);
                }else {
                    stopListUp.add(direct);
                }
            }
            //预置 始发站 以及终点站
            if (stopListUp != null && stopListUp.size() > 0)
            stopListUp.get(0).setSelect(true);

            directStationSelectAdapter.replaceAll(stopListUp);
            if (stopListDown != null && stopListDown.size() > 0)
                stopListDown.get(stopListDown.size() - 1).setSelect(true);

            directDownAdapter.replaceAll(stopListDown);
        }

        if (mLineDetail.getStatementInfoList()!=null){
            stopListAll = mLineDetail.getStatementInfoList();
            descAdapter.replaceAll(stopListAll);
        }

    }
}
