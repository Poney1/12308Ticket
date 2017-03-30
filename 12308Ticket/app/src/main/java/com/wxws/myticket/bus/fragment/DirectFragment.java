package com.wxws.myticket.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.bus.activity.AircraftCityActivity;
import com.wxws.myticket.bus.activity.AppointCarActivity;
import com.wxws.myticket.bus.activity.BaocheActivity;
import com.wxws.myticket.bus.activity.BusTicketActivity;
import com.wxws.myticket.bus.activity.CityLineActivity;
import com.wxws.myticket.bus.activity.GangAoKXActivity;
import com.wxws.myticket.bus.activity.SelectDateActivity;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.AircraftCityEntity;
import com.wxws.myticket.bus.entity.LocationEntity;
import com.wxws.myticket.bus.entity.POIEntity;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.EventThread;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.ViewQueryLine;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 直通车
 * Date: 2016/10/21 10:23
 *
 * @auther: lixiangxiang
 */
public class DirectFragment extends BaseFragment {

    private ViewQueryLine vq;
    private String startDate;

    private String fromCityId;
    private String fromCityName;
    private double fromLongitude;
    private double fromLatitude;
    private double toLongitude;
    private double toLatitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_direct, null);
        RxBus.getInstance().register(this);
        initView();

        initData();

        return view;
    }

    private void initData() {

        POIEntity startPoi = (POIEntity) PreferencesUtils.readDataObject(getActivity(), Constants.DirectStartRecord);
        POIEntity endPoi = (POIEntity) PreferencesUtils.readDataObject(getActivity(), Constants.DirectEndRecord);
        if (startPoi != null && endPoi != null) {
            vq.setStartCity(startPoi.getName());
            vq.setEndCity(endPoi.getName());
            fromLongitude = startPoi.getLatLng().latitude;
            fromLatitude = startPoi.getLatLng().longitude;
            toLatitude = endPoi.getLatLng().latitude;
            toLongitude = endPoi.getLatLng().longitude;
        }
        startDate = vq.getStartDate();

        vq.setOnCityTimeSelectListener(new ViewQueryLine.OnCityTimeSelectListener() {
            private Intent intent;

            @Override
            public void startCitySelect() {
                intent = new Intent(getActivity(), CityLineActivity.class);
                intent.putExtra("inputName", getString(R.string.city_line_up));
                intent.putExtra("cityId", Constants.DirectStartCity);
                startActivityWithAnim(intent);
            }

            @Override
            public void endCitySelect() {
                intent = new Intent(getActivity(), CityLineActivity.class);
                intent.putExtra("inputName", getString(R.string.city_line_down));
                intent.putExtra("fromCityId", fromCityId);
                intent.putExtra("fromCityName", fromCityName);
                intent.putExtra("cityId", Constants.DirectEndCity);
                startActivityWithAnim(intent);
            }

            @Override
            public void changeData() {
                double fromLongitude1 = fromLongitude;
                double fromLatitude1 = fromLongitude;
                fromLongitude = toLongitude;
                fromLatitude = toLatitude;
                toLongitude = fromLongitude1;
                toLongitude = fromLatitude1;
            }

            @Override
            public void timeSelect(String year, String month, String day) {
                Intent intent = new Intent(getActivity(), SelectDateActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("tag", SelectDateActivity.BUSDIRECTTAG);
                getActivity().startActivity(intent);
            }

            @Override
            public void queryLine(String startCity, String endCity, String date) {
                startDate = date;
                Intent intent = new Intent(getActivity(), BusTicketActivity.class);
                String departDate = vq.getStartDate();
                intent.putExtra("cityTag", BusTicketActivity.CITYLINETAG);
                intent.putExtra("fromLongitude", ArithUtils.setMaximumFractionDigits(fromLongitude, 6));
                intent.putExtra("fromLatitude", ArithUtils.setMaximumFractionDigits(fromLatitude, 6));
                intent.putExtra("toLongitude", ArithUtils.setMaximumFractionDigits(toLongitude, 6));
                intent.putExtra("toLatitude", ArithUtils.setMaximumFractionDigits(toLatitude, 6));
                intent.putExtra("departDate", departDate);
                intent.putExtra("bizType", ModuleConstants.bizTypeCityLine);
                getActivity().startActivity(intent);

            }
        });
    }

    private void initView() {
        vq = (ViewQueryLine) view.findViewById(R.id.vq_bus_direct);
        bindFiveLineView();
    }

    /**
     * 5 条线路的搜索
     */
    private void bindFiveLineView() {
        TextView tv_cjkx = (TextView) view.findViewById(R.id.tv_ticket_cjkx);
        TextView tv_kcyc = (TextView) view.findViewById(R.id.tv_ticket_kcyc);
        TextView tv_jckx = (TextView) view.findViewById(R.id.tv_ticket_jckx);
        TextView tv_wybc = (TextView) view.findViewById(R.id.tv_ticket_wybc);
        TextView tv_gakx = (TextView) view.findViewById(R.id.tv_ticket_gakx);

        tv_cjkx.setOnClickListener(this);
        tv_kcyc.setOnClickListener(this);
        tv_jckx.setOnClickListener(this);
        tv_wybc.setOnClickListener(this);
        tv_gakx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_ticket_cjkx:
                intent = new Intent(getActivity(), GangAoKXActivity.class);
                intent.putExtra("cityTag", BusTicketActivity.CITYLINETAG);
                startActivityWithAnim(intent);
                break;
            case R.id.tv_ticket_kcyc:
                intent = new Intent(getActivity(), AppointCarActivity.class);
                startActivityWithAnim(intent);
                break;
            case R.id.tv_ticket_jckx:
                getAircraftCity();
                break;
            case R.id.tv_ticket_wybc:
                intent = new Intent(getActivity(), BaocheActivity.class);
                startActivityWithAnim(intent);
                break;
            case R.id.tv_ticket_gakx:
                intent = new Intent(getActivity(), GangAoKXActivity.class);
                intent.putExtra("cityTag", BusTicketActivity.CITYGANGAOTAG);
                startActivityWithAnim(intent);
                break;
        }
    }

    //机场城市判断
    private void getAircraftCity() {
        final LocationEntity locationEntity = (LocationEntity) PreferencesUtils.readDataObject(getActivity(), Constants.LocationInfo);
        HttpRequestManager.getInstance().aircraftCityList(0, new ApiSubscriber<JsonResult<List<AircraftCityEntity>>>(getActivity()) {
            @Override
            public void onNext(JsonResult<List<AircraftCityEntity>> listJsonResult) {
                Intent intent = null;
                boolean isHaveCity = false;
                if (listJsonResult != null && listJsonResult.getObject() != null && listJsonResult.getObject().size() > 0) {
                    List<AircraftCityEntity> data = listJsonResult.getObject();
                    if (locationEntity != null) {
                        for (AircraftCityEntity aircraft : data) {
                            if (locationEntity.getCity().equals(aircraft.getSecondName())) {
                                isHaveCity = true;
                                break;
                            }
                        }
                        if (isHaveCity) {
                            intent = new Intent(getActivity(), BusTicketActivity.class);
                            intent.putExtra("cityTag", BusTicketActivity.CITYAIRCRAFTTAG);
                            intent.putExtra("cityName", locationEntity.getCity());
                            intent.putExtra("bizType", ModuleConstants.bizTypeAirCraftBus);
                            intent.putExtra("departDate", startDate);
                            startActivityWithAnim(intent);
                        } else {
                            intent = new Intent(getActivity(), AircraftCityActivity.class);
                            intent.putExtra("airCraft", (Serializable) data);
                            startActivityWithAnim(intent);
                        }
                    } else {
                        intent = new Intent(getActivity(), AircraftCityActivity.class);
                        intent.putExtra("airCraft", (Serializable) data);
                        startActivityWithAnim(intent);
                    }
                }
            }
        });
    }

    @Subscribe
    public void onRecivedLineCity(POIEntity poiEntity) {
        if (Constants.DirectStartCity == poiEntity.getSelect()) {
            fromCityId = poiEntity.getCity();
            fromCityName = poiEntity.getCity();
            fromLongitude = poiEntity.getLatLng().longitude;
            fromLatitude = poiEntity.getLatLng().latitude;
            vq.setStartCity(String.format("%s %s", poiEntity.getCity(), poiEntity.getName()));
            PreferencesUtils.saveDataObject(getActivity(), Constants.DirectStartRecord, poiEntity);
            PreferencesUtils.saveDataObject(getActivity(), Constants.DirectEndRecord, null);
        } else if (Constants.DirectEndCity == poiEntity.getSelect()) {
            toLongitude = poiEntity.getLatLng().longitude;
            toLatitude = poiEntity.getLatLng().latitude;
            vq.setEndCity(String.format("%s %s", poiEntity.getCity(), poiEntity.getName()));
            PreferencesUtils.saveDataObject(getActivity(), Constants.DirectEndRecord, poiEntity);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onRecievedDate(SelectDayEvent event) {
        if (SelectDateActivity.BUSDIRECTTAG.equals(event.getTag())) {
            StringBuilder startDate = new StringBuilder();
            startDate.append(event.getYear()).append("-").append(event.getMonth()).append("-").append(event.getDay());
            vq.setDate(startDate.toString());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
}
