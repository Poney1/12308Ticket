package com.wxws.myticket.order.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.order.adapter.BusMapAdapter;
import com.wxws.myticket.order.entity.BusMapResponsePara;
import com.wxws.myticket.order.entity.StopoverStation;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 直通车查看位置
 * Date: 2016-11-14 18:15
 *
 * @author jiangyan
 */
public class BusMapActivity extends BaseActivity {

    private String mScheduleId;
    BusMapResponsePara mPara;
    private BaiduMap mBaiduMap;
    private MapView mMvBusMap;
    private RoutePlanSearch mSearch;
    private BusMapAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busmap);
        // TODO
        mScheduleId = getIntent().getStringExtra("scheduleId");
        initCommonView();
        initView();
        //getData();
    }

    private void initCommonView() {
        bindLoadingView();
        bindTitleViews();
        barTitle.setText("订单详情");
    }

    private void initView() {

        mMvBusMap = (MapView) findViewById(R.id.mv_busmap);
        mBaiduMap = mMvBusMap.getMap();

        ListView lvStation = (ListView) findViewById(R.id.lv_busmap_stationinfo);
        mAdapter = new BusMapAdapter(BusMapActivity.this);
        lvStation.setAdapter(mAdapter);

        mMvBusMap.showZoomControls(false);
        MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.setMapStatus(ms);

        initData();

    }

    private void getData() {
        showLoading();
        HttpRequestManager.getInstance().getBusMapInfo(mScheduleId, new ApiSubscriber<JsonResult<BusMapResponsePara>>(BusMapActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<BusMapResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    mPara = result.getObject();
                    initData();
                    dismissLoading();
                } else {

                    // TODO showErrorOrNoData();
                }
            }
        });

    }

    private void initData() {
        //List<StopoverStation> stationInfoList = mPara.getStopoverStationList();

        List<StopoverStation> stationInfoList = new ArrayList<>();
        //TODO 显示地址
        stationInfoList.add(new StopoverStation("深大", "0", "22.5438700000", "113.9503390000"));
        stationInfoList.add(new StopoverStation("桃园", "1", "22.5380890000", "113.9313220000"));

        Double lat = 0.00;
        Double lnt = 0.00;
        for (StopoverStation station : stationInfoList) {
            if (station.getLatitude() != null) {
                lat = Double.parseDouble(station.getLatitude());
            }
            if (station.getLongitude() != null) {
                lnt = Double.parseDouble(station.getLongitude());
            }

            loadMarker(new LatLng(lat, lnt), station);
        }

        mAdapter.refreshData(stationInfoList);


    }

    private void loadMarker(LatLng latLng, StopoverStation station) {

        Log.e("mudo", latLng.latitude + ":" + latLng);

        BitmapDescriptor bitmapDescriptor;
        if ("0".equals(station.getType())) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_ups);
        } else {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_downs);
        }

        OverlayOptions overlayoption = new MarkerOptions().position(latLng).icon(bitmapDescriptor).title(station.getStationName()).zIndex(10);

        mBaiduMap.addOverlay(overlayoption);
        // TODO 将地图移到到最后一个经纬度位置 看具体需求。
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMvBusMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMvBusMap.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMvBusMap.onDestroy();
    }

}