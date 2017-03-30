package com.wxws.myticket.bus.activity;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.POIEntity;
import com.wxws.myticket.bus.rxentity.GangAoCityEvent;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.ViewQueryLine;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshBase;

/**
 * desc: 港澳快线
 * Date: 2016/11/10 10:26
 *
 * @auther: lixiangxiang
 */
public class GangAoKXActivity extends BaseActivity {

    private ViewQueryLine vq;
    private TextView tvSelectStart;
    private TextView tvSelectEnd;
    private TextView tvDisplayTime;
    private TextView tvDefaultDay;
    private ImageButton btnChange;
    private Button btnQuery;

    private String year, month, day;
    private String startPlace, endPlace;//出发到达站点
    /**
     * 名称	类型	位置	必填	描述
     * fromCityId	string	param	是	出发城市Id
     * fromCityName	string	param	是	出发城市名
     * fromStationId	string	param	是	出发站点Id
     * fromStationName	string	param	是	出发站点名
     * toCityId	string	param	是	到达城市Id
     * toCityName	string	param	是	到达城市名
     * toStationId	string	param	是	到达站点Id
     * toStationName	string	param	是	到达站点名
     * departDate	string	param	是	出发日期
     * bizType	string	param	是	业务类型
     */
    private String fromCityId, fromCityName, fromStationName, fromStationId, toCityId, toCityName,
            toStationName, toStationId, departDate, bizType;
    private String cityTag;
    //城际 快线
    private double fromLongitude;
    private double fromLatitude;
    private double toLongitude;
    private double toLatitude;
    private LinearLayout llDefaultTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gangao);
        RxBus.getInstance().register(GangAoKXActivity.this);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void getPreData() {
        cityTag = getIntent().getStringExtra("cityTag");
    }

    private void bindClick() {
        btnChange.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        tvSelectStart.setOnClickListener(this);
        tvSelectEnd.setOnClickListener(this);
        llDefaultTime.setOnClickListener(this);
    }


    private void initData() {
        if (cityTag.equals(BusTicketActivity.CITYLINETAG)) {
            bizType = ModuleConstants.bizTypeCityLine;
            barTitle.setText(getString(R.string.bus_city_line));
        } else {
            bizType = ModuleConstants.bizTypeGangao;
            barTitle.setText(getString(R.string.bus_direct_gangao));
        }
        departDate = DateTimeUtil.getTomorrowDate();
        setDate(departDate);
    }

    private void initView() {
        bindTitleViews();
        //出发城市选择
        tvSelectStart = (TextView) findViewById(R.id.tv_select_start);
        //到达城市选择
        tvSelectEnd = (TextView) findViewById(R.id.tv_select_end);
        //日期
        tvDisplayTime = (TextView) findViewById(R.id.tv_display_time);
        //天
        tvDefaultDay = (TextView) findViewById(R.id.tv_default_day);
        //切换出发到达城市
        btnChange = (ImageButton) findViewById(R.id.btn_change);
        //时间选择
        llDefaultTime = (LinearLayout) findViewById(R.id.ll_default_time);
        // 查询
        btnQuery = (Button) findViewById(R.id.btn_query);
    }


    //设定时间
    public void setDate(String selectDate) {
        String[] splits = selectDate.split("-");
        year = splits[0];
        month = splits[1];
        day = splits[2];
        int[] compare = new int[3];
        compare[0] = Integer.parseInt(year);
        compare[1] = Integer.parseInt(month) - 1;
        compare[2] = Integer.parseInt(day);
        long betweenDays = DateTimeUtil.getDaysBetween(compare) + 1;
        String to = getString(R.string.date_today);
        String tomr = getString(R.string.date_tomorrow);
        String ttomr = getString(R.string.date_after_tomorrow);
        if (betweenDays == 0) {
            tvDefaultDay.setText(String.format("%s  %s", to, DateTimeUtil.getWeekStr(selectDate)));
        } else if (betweenDays == 1) {
            tvDefaultDay.setText(String.format("%s  %s", to, DateTimeUtil.getWeekStr(selectDate)));
        } else if (betweenDays == 2) {
            tvDefaultDay.setText(String.format("%s  %s", tomr, DateTimeUtil.getWeekStr(selectDate)));
        } else if (betweenDays == 3) {
            tvDefaultDay.setText(String.format("%s  %s", ttomr, DateTimeUtil.getWeekStr(selectDate)));
        } else {
            tvDefaultDay.setText(DateTimeUtil.getWeekStr(selectDate));
        }
        tvDisplayTime.setText(DateTimeUtil.getSpChineseMMDD(selectDate));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_query:
                if (judgeStartCity() && judgeEndCity()) {
                    intent = new Intent(GangAoKXActivity.this, BusTicketActivity.class);

                    if (cityTag.equals(BusTicketActivity.CITYLINETAG)) {

                        intent.putExtra("fromLongitude", fromLongitude);
                        intent.putExtra("fromLatitude", fromLatitude);
                        intent.putExtra("toLongitude", toLongitude);
                        intent.putExtra("toLatitude", toLatitude);

                    } else {
                        intent.putExtra("departDate", departDate);
                        intent.putExtra("startCity", fromCityName);
                        intent.putExtra("startId", fromCityId);
                        intent.putExtra("endCity", toCityName);
                        intent.putExtra("endId", toCityId);
                        intent.putExtra("fromStationName", fromStationName);
                        intent.putExtra("fromStationId", fromStationId);
                        intent.putExtra("toStationName", toStationName);
                        intent.putExtra("toStationId", toStationId);
                    }
                    intent.putExtra("bizType", bizType);
                    intent.putExtra("cityTag", cityTag);
                    intent.putExtra("departDate", departDate);
                    startActivityWithAnim(intent);
                }
                break;
            case R.id.btn_change:
                changeCity();
                break;
            case R.id.tv_select_start:
                if (cityTag.equals(BusTicketActivity.CITYLINETAG)) {
                    intent = new Intent(this, CityLineActivity.class);
                    intent.putExtra("inputName", getString(R.string.city_line_up));
                    intent.putExtra("cityId", Constants.LineStartCity);
                    startActivityWithAnim(intent);
                } else {
                    intent = new Intent(GangAoKXActivity.this, GangAoCityActivity.class);
                    intent.putExtra("origin", GangAoCityActivity.upTag);
                    startActivityWithAnim(intent);
                }

                break;
            case R.id.tv_select_end:
                if (judgeStartCity()) {
                    if (cityTag.equals(BusTicketActivity.CITYLINETAG)) {
                        intent = new Intent(this, CityLineActivity.class);
                        intent.putExtra("inputName", getString(R.string.city_line_down));
                        intent.putExtra("fromCityId", fromCityId);
                        intent.putExtra("fromCityName", fromCityName);
                        intent.putExtra("cityId", Constants.LineEndCity);
                        startActivityWithAnim(intent);
                    } else {
                        intent = new Intent(GangAoKXActivity.this, GangAoCityActivity.class);
                        intent.putExtra("origin", GangAoCityActivity.downTag);
                        intent.putExtra("fromCityId", fromCityId);
                        intent.putExtra("fromStationId", fromStationId);
                        startActivityWithAnim(intent);
                    }
                }

                break;
            case R.id.ll_default_time:
                intent = new Intent(GangAoKXActivity.this, SelectDateActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("tag", SelectDateActivity.BUSGANGAOTAG);
                startActivityWithAnim(intent);
                break;
        }
    }

    //出发城市
    private boolean judgeStartCity() {
        String startCity = tvSelectStart.getText().toString();
        if (StringUtils.isNullOrEmpty(startCity)) {
            ToastManager.getInstance().showToast(GangAoKXActivity.this, R.string.ll_view_please_select);
            return false;
        }
        return true;
    }


    //到达城市
    private boolean judgeEndCity() {
        String endCity = tvSelectEnd.getText().toString();
        if (StringUtils.isNullOrEmpty(endCity)) {
            ToastManager.getInstance().showToast(GangAoKXActivity.this, R.string.ll_view_please_select_end);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(GangAoKXActivity.this);
    }

    @Subscribe
    public void onReceivedDate(SelectDayEvent event) {
        if (SelectDateActivity.BUSGANGAOTAG.equals(event.getTag())) {
            StringBuilder startDate = new StringBuilder();
            startDate.append(event.getYear()).append("-").append(event.getMonth()).append("-").append(event.getDay());
            setDate(startDate.toString());
        }
    }

    @Subscribe
    public void onRecivedLineCity(POIEntity poiEntity) {
        if (poiEntity.getSelect() == Constants.LineStartCity) {
            fromLongitude = poiEntity.getLatLng().longitude;
            fromLatitude = poiEntity.getLatLng().latitude;
            fromCityId = poiEntity.getCity();
            fromCityName = poiEntity.getCity();
            tvSelectStart.setText(String.format("%s %s", poiEntity.getCity(), poiEntity.getName()));
            tvSelectEnd.setText("");
        } else if (poiEntity.getSelect() == Constants.LineEndCity){
            toLongitude = poiEntity.getLatLng().longitude;
            toLatitude = poiEntity.getLatLng().latitude;
            tvSelectEnd.setText(String.format("%s %s", poiEntity.getCity(), poiEntity.getName()));
        }
    }

    @Subscribe
    public void onReceivedCity(GangAoCityEvent event) {
        if (GangAoCityActivity.upTag.equals(event.getTag())) {
            fromCityId = event.getCityId();
            fromCityName = event.getCityName();
            fromStationName = event.getStationName();
            fromStationId = event.getStationId();

            tvSelectStart.setText(String.format("%s %s", fromCityName, fromStationName));
            tvSelectEnd.setText("");
        } else if (GangAoCityActivity.downTag.equals(event.getTag())) {
            toCityId = event.getCityId();
            toCityName = event.getCityName();
            toStationName = event.getStationName();
            toStationId = event.getStationId();

            tvSelectEnd.setText(String.format("%s %s", toCityName, toStationName));
        }
    }

    //切换城市
    private void changeCity() {
        if (judgeStartCity() && judgeEndCity()) {
            String tempFromCityId = fromCityId;
            String tempFromCityName = fromCityName;
            String tempFromStationName = fromStationName;
            String tempFromStationId = fromStationId;

            fromCityId = toCityId;
            fromCityName = toCityName;
            fromStationName = toStationName;
            fromStationId = toStationId;

            toCityId = tempFromCityId;
            toCityName = tempFromCityName;
            toStationName = tempFromStationName;
            toStationId = tempFromStationId;
        }
    }
}
