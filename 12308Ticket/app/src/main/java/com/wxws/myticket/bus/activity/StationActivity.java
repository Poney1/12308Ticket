package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.StationDescAdapter;
import com.wxws.myticket.bus.entity.StationDescEntity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.SystemUtils;

import java.util.List;

/**
 * desc: 汽车站点
 * Date: 2016/11/9 11:24
 *
 * @auther: lixiangxiang
 */
public class StationActivity extends BaseActivity implements BDLocationListener{


    private ListView stationMap;
    private StationDescAdapter stationDescAdapter;

    private String  longitude;
    private String  latitude;
    private String  fromCityName;
    private String  fromCityId;
    private LocationClient mLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

        getPreData();

        initView();

        initData();

        initRegisterMap();
    }

    private void initRegisterMap() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(mLocationClient);
        mLocationClient.start();
    }


    private void getPreData() {
        fromCityId = getIntent().getStringExtra("fromCityId");
        fromCityName = getIntent().getStringExtra("fromCityName");
    }

    private void initView() {
        stationMap = (ListView) findViewById(R.id.lv_insurance);
        bindTitleViews();
        bindLoadingView();
    }

    private void  initData(){

        stationDescAdapter = new StationDescAdapter(StationActivity.this,null);
        stationMap.setAdapter(stationDescAdapter);
        barTitle.setText(getString(R.string.title_list_station));
    }


    private void getStationData(){
        if (StringUtils.isNullOrEmpty(longitude) || StringUtils.isNullOrEmpty(latitude)){
            longitude ="";
            latitude = "";
        }
        HttpRequestManager.getInstance().fromCityStation(longitude, latitude, fromCityName, fromCityId,
                new ApiSubscriber<JsonResult<List<StationDescEntity>>>(StationActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<StationDescEntity>> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())){
                    if (listJsonResult.getObject()!=null&&listJsonResult.getObject().size()>0){
                        stationDescAdapter.replaceAll(listJsonResult.getObject());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(this);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        longitude = bdLocation.getLongitude()+"";
        latitude = bdLocation.getLatitude()+"";
        getStationData();
    }
}
