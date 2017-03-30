package com.wxws.myticket.bus.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.GangAoCityProvinceAdapter;
import com.wxws.myticket.bus.adapter.GangAoStationAdapter;
import com.wxws.myticket.bus.entity.GangAoStartCityEntity;
import com.wxws.myticket.bus.entity.StationListEntity;
import com.wxws.myticket.bus.rxentity.GangAoCityEvent;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 港澳 快线城市选择
 * Date: 2016/11/10 14:11
 *
 * @auther: lixiangxiang
 */
public class GangAoCityActivity extends BaseActivity {

    private ListView listStation;
    private ListView listCity;

    private GangAoCityProvinceAdapter gangAoCityProvinceAdapter;
    private GangAoStationAdapter stationAdapter;

    private List<GangAoStartCityEntity> list;//港澳出发城市
    private String fromCityId,fromStationId;
    private String origin;
    private int cityPosition = 0;

    //出发到达区分
    public static final String upTag = "up";
    public static final String downTag = "down";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_gangao);

        getPreData();

        initView();

        initData();

        bindClick();

    }

    private void getPreData() {
        origin = getIntent().getStringExtra("origin");
        fromCityId = getIntent().getStringExtra("fromCityId");
        fromStationId = getIntent().getStringExtra("fromStationId");
    }

    private void bindClick() {

        listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshGangAoCity(position);
            }
        });

        listStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshStation(position);
            }
        });

    }

    private void initData() {
        list = new ArrayList<>();
        gangAoCityProvinceAdapter = new GangAoCityProvinceAdapter(GangAoCityActivity.this,list);
        stationAdapter = new GangAoStationAdapter(GangAoCityActivity.this,null);
        listCity.setAdapter(gangAoCityProvinceAdapter);
        listStation.setAdapter(stationAdapter);
        switch (origin){
            case upTag:
                getStartData();
                break;
            case downTag:
                getEndData();
                break;
        }
    }


    private void initView() {
        bindTitleViews();
        bindLoadingView();
        llTitleBar.setBackgroundColor(Color.RED);
        barTitle.setTextColor(Color.WHITE);
        imgBarLeft.setImageResource(R.drawable.slt_return_white);
        barTitle.setText(getString(R.string.gangao_select_city));
        listStation = (ListView) findViewById(R.id.list_station);
        listCity = (ListView) findViewById(R.id.list_city);

    }

    /**
     * 刷新 城市
     * @param position
     */
    private void refreshGangAoCity(int position){
        if (list == null){
            finish();
            return;
        }
        if (list.size() == 0){
            finish();
            return;
        }
        if (list.size()<position){
            finish();
            return;
        }
        for (int i= 0 ;i< list.size();i++){
            if (i == position){
                list.get(position).setSelect(true);
            }else {
                list.get(i).setSelect(false);
            }
        }
        cityPosition = position;
        gangAoCityProvinceAdapter.replaceAll(list);
        stationAdapter.replaceAll(list.get(position).getStationList());
    }

    /**
     * 刷新站点
     * @param position
     */
    private void   refreshStation(int position){
        if (list.get(cityPosition).getStationList().size()<position)
            return;
        GangAoStartCityEntity gangAo = list.get(cityPosition);
        StationListEntity station = gangAo.getStationList().get(position);
        GangAoCityEvent gangAoCityEvent =new GangAoCityEvent(origin,gangAo.getName(),gangAo.getId(),station.getStationName(),station.getStationId());
        RxBus.getInstance().post(gangAoCityEvent);
        finish();
    }
    /**
     * 出发城市
     */
    private void getStartData(){
        HttpRequestManager.getInstance().fromGangAoList(new ApiSubscriber<JsonResult<List<GangAoStartCityEntity>>>(GangAoCityActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<GangAoStartCityEntity>> listJsonResult) {
                if (listJsonResult!=null && listJsonResult.getObject().size()>0){
                    list = listJsonResult.getObject();
                    list.get(0).setSelect(true);
                    gangAoCityProvinceAdapter.replaceAll(list);
                    stationAdapter.replaceAll(list.get(0).getStationList());
                }
            }
        });
    }

    /**
     * 到达 城市
     */
    private void getEndData(){
        HttpRequestManager.getInstance().toGangAoList(fromCityId,fromStationId,new ApiSubscriber<JsonResult<List<GangAoStartCityEntity>>>(GangAoCityActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<GangAoStartCityEntity>> listJsonResult) {
                if (listJsonResult.getObject()!=null && listJsonResult.getObject().size()>0){
                    list = listJsonResult.getObject();
                    list.get(0).setSelect(true);
                    gangAoCityProvinceAdapter.replaceAll(list);
                    stationAdapter.replaceAll(list.get(0).getStationList());
                }
            }
        });
    }
}
