package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.AircraftCityAdapter;
import com.wxws.myticket.bus.entity.AircraftCityEntity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;

import java.util.List;


/**
 * desc: 机场快线城市
 * Date: 2016/11/10 17:57
 *
 * @auther: lixiangxiang
 */
public class AircraftCityActivity extends BaseActivity {

    private GridView mGridAircraft;
    private List<AircraftCityEntity> data;
    private AircraftCityAdapter aircraft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircraft_city);
        initView();

        initData();
    }

    private void initData() {

        barTitle.setText(getString(R.string.aircraft_select_city));
        aircraft = new AircraftCityAdapter(AircraftCityActivity.this,data);
        mGridAircraft.setAdapter(aircraft);

        getAircraftCity();
    }

    private void initView() {
        bindTitleViews();
        mGridAircraft = (GridView) findViewById(R.id.grid_aircraft);
    }

    private void  getAircraftCity(){
        HttpRequestManager.getInstance().aircraftCityList(0,new ApiSubscriber<JsonResult<List<AircraftCityEntity>>>(AircraftCityActivity.this) {
            @Override
            public void onNext(JsonResult<List<AircraftCityEntity>> listJsonResult) {
                if (listJsonResult!=null && listJsonResult.getObject()!=null && listJsonResult.getObject().size()>0){
                    data = listJsonResult.getObject();
                    aircraft.replaceAll(data);
                }
            }
        });
    }
}
