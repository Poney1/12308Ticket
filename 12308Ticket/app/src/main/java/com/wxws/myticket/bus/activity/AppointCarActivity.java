package com.wxws.myticket.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.OptionsPickerView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.dialog.CancelAndListViewDialog;
import com.wxws.myticket.bus.entity.AppointEntity;
import com.wxws.myticket.bus.entity.LowestPriceRentCarEntity;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.interfaces.IDialog;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.dialog.FragmentDialog;
import com.wxws.myticket.my.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 新约租车界面
 * Date: 2016-06-17 14:23
 *
 * @auther: lixiangxiang
 */
public class AppointCarActivity extends BaseActivity implements BDLocationListener {

    private LatLng point;
    private BaiduMap mBaiduMap;
    private LinearLayout mLlBegin;
    private LinearLayout mLlEnd;
    private LinearLayout mLayoutDeparture;
    private OptionsPickerView pvTime;//时间选择
    private TextView mTvTime;//时间设置
    private TextView mTvOverbooking;//马上下单
    private TextView etBecity;
    private TextView etEncity;
    private MapView mapView;
    private Button btnMyLocation;

    private double lat = 0, lng = 0;//坐标
    private String displayName;//显示的地址
    ArrayList<String> day = new ArrayList<>();//天
    ArrayList<String> dayCopy = new ArrayList<>();//copy天
    ArrayList<ArrayList<String>> hour = new ArrayList<>(); //时
    ArrayList<ArrayList<ArrayList<String>>> minute = new ArrayList<>(); //分
    private List<String> startCity = new ArrayList<>();//出发城市
    private String locationCity = "";//定位城市
    private boolean isHaveCity = false;//定位城市是否开通
    private String mMyLocation;
    private float zoom = 14;//默认地图缩放等级
    private boolean needLocation = false;//是否请求定位
    private boolean isChangeCity = true ;//是否重新变化城市

    /**
     * 下单的参数
     */
    private String startCityName = "";
    private String startDistrict = "";
    private String startPlace = "";
    private String endCityName = "";
    private String endDistrict = "";
    private String endPlace = "";
    private String startTime = "";
    private String startDate = "";
    private Marker mMarker;
    private View mView;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //// TODO: 2016/7/5  提示用户打开定位权限
            dialog.showDialog("请授权应用定位权限！", new DialogCallBackListener() {
                @Override
                public void doPositiveClick(String string) {

                }

                @Override
                public void doPositiveClick() {
                    needLocation = true;
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityWithAnim(intent);
                    dialog.dissmissDialog();
                }

                @Override
                public void doNegativeClick() {

                }
            });
            return false;
        }
    });//
    private LocationClient mLocationClient;
    private IDialog dialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_appoint);
        RxBus.getInstance().register(this);
        initMap();

        initView();

        initData();

        bindClick();
    }

    private void initData() {
        dialog = new FragmentDialog(this);
        barTitle.setText(getString(R.string.nac_list_title));
        getStartCity();
    }

    /**
     * 获取出发城市
     */
    private void getStartCity() {

       /* //获取出发城市数据
        mWebAPI.getAppointCitys("1", netErrorLisenterTag, new ResponseFactory<List<AppointEntity>>(new TypeReference<WebResult<List<AppointEntity>>>() {
        }) {
            @Override
            public void parseResponse(WebResult<List<AppointEntity>> object) {

                if ("0000".equals(object.getResultCode())) {
                    List<AppointEntity> listStart = object.getObject();
                    if (listStart != null && listStart.size() > 0) {

                        for (int i = 0; i < listStart.size(); i++) {
                            startCity.add(listStart.get(i).getStartCityName());
                            if (displayName != null && locationCity.equals(listStart.get(i).getStartCityName())) {
                                etBecity.setText(displayName);
                                isHaveCity = true;
                            }
                        }
                        PreferencesUtils.saveObject(NewAppointCarActivity.this, PreferencesUtils.PREFERENCE_NAME_DATA, Constants.APPOINTSTARTCITY, startCity);
                    }
                }
            }
        });*/
    }

    private void bindClick() {

        mLlBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointCarActivity.this, AppointSearchCityActivity.class);
                intent.putExtra("where", R.string.nac_from_where);
                intent.putExtra("isHaveCity", isHaveCity);
                intent.putExtra("locationCity", mMyLocation);
                AppointCarActivity.this.startActivityWithAnim(intent);
            }
        });

        mLlEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectStart()) {
                    Intent intent = new Intent(AppointCarActivity.this, AppointSearchCityActivity.class);
                    intent.putExtra("where", R.string.nac_to_where);
                    intent.putExtra("startCity", locationCity);
                    intent.putExtra("locationCity", mMyLocation);
                    intent.putExtra("isChangeCity",isChangeCity);
                   AppointCarActivity.this.startActivityWithAnim(intent);
                }
            }
        });

        mLayoutDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        mTvOverbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkLogin()) {
                    startActivity(new Intent(AppointCarActivity.this, LoginActivity.class));
                    return;
                }
                //请求接口 是否有 车，没有则弹出 浮窗展示  服务时间
                if (!searchCondition()) {
                    return;
                }

               /* mWebAPI.getLowestPrice(startCityName, startDistrict, endCityName, endDistrict, startPlace, endPlace, startDate,
                        startTime, new ResponseFactory<LowestPriceRentCarEntity>(new TypeReference<WebResult<LowestPriceRentCarEntity>>() {
                        }) {
                            @Override
                            public void parseResponse(WebResult<LowestPriceRentCarEntity> object) {
                                mDialog.dissmissDialog();
                                if (object.getObject() == null) {
                                    CommonUtil.showToast(NewAppointCarActivity.this, object.getResultMsg());
                                    return;
                                }
                                if ("0000".equals(object.getResultCode())) {
                                    //传数据过去
                                    LowestPriceRentCarEntity low = object.getObject();
                                    Intent intent = new Intent(NewAppointCarActivity.this, AppointOverbookingActivity.class);
                                    intent.putExtra("overBooking", low);
                                    NewAppointCarActivity.this.startActivityWithAnim(intent);
                                } else if ("1010".equals(object.getResultCode())) {
                                    LowestPriceRentCarEntity lowest = object.getObject();
                                    if (lowest.getServiceTime() != null && lowest.getServiceTime().size() > 0) {
                                        loadTime(lowest.getServiceTime());
                                    } else {
                                        CommonUtil.showToast(NewAppointCarActivity.this, getString(R.string.nac_no_service));
                                    }
                                } else {
                                    CommonUtil.showToast(NewAppointCarActivity.this, object.getResultMsg());
                                }
                            }
                        });*/
            }
        });
    }

    private void initMap() {

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(mLocationClient);
        mLocationClient.start();

        //地图
        mapView = (MapView) findViewById(R.id.bmapView);
        //我的位置
        btnMyLocation = (Button) findViewById(R.id.btn_my_location);
        btnMyLocation.setOnClickListener(this);
        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(false);//隐藏缩放按钮
        MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(zoom);
        mBaiduMap.setMapStatus(ms);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.getUiSettings().setAllGesturesEnabled(true);
        mLocationClient.requestLocation();
    }

    private void initView() {
        bindTitleViews();
        mLlBegin = (LinearLayout) findViewById(R.id.layout_becity);
        mLlEnd = (LinearLayout) findViewById(R.id.layout_encity);
        mLayoutDeparture = (LinearLayout) findViewById(R.id.layout_departure_time);
        mTvTime = (TextView) findViewById(R.id.etDeparturetime);
        mTvOverbooking = (TextView) findViewById(R.id.tv_overbooking);
        etBecity = (TextView) findViewById(R.id.etBecity);//出发城市
        etEncity = (TextView) findViewById(R.id.etEncity);//终点城市
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_my_location://我的位置
                if (mLocationClient != null && lat != 0 && lng != 0) {
                    LatLng ll = new LatLng(lat, lng);
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(mapStatusUpdate);
                }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (needLocation) {
            needLocation = false;
            mLocationClient.requestLocation();
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    public void onReceiveLocation(BDLocation location) {
        if (location.getAddrStr() != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            if (location.getAddress() != null && location.getAddress().city != null && location.getAddress().city.length() > 1) {
                locationCity = location.getAddress().city;
                displayName = String.format("%s %s%s%s", location.getCity(), location.getDistrict(), location.getStreet(), location.getStreetNumber());
                etBecity.setText(displayName);
            }
            mMyLocation = location.getCity();
            loadMarker();
            startCityName = location.getAddress().city;
            startDistrict = location.getDistrict();
            startPlace = location.getAddrStr();
            List<String> readStartCity = (List<String>) PreferencesUtils.readDataObject(AppointCarActivity.this, Constants.APPOINTSTARTCITY);

            if (readStartCity != null && readStartCity.size() > 0) {

                for (String startCity : readStartCity) {
                    if (mMyLocation.equals(startCity)) {
                        isHaveCity = true;
                    }
                }
                if (!isHaveCity) {
                    etBecity.setText("");
                    ToastManager.getInstance().showToast(AppointCarActivity.this, getString(R.string.nac_no_go));
                }
            }

        } else {
                //弹出 提示信息
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    }

    //加载图层
    private void loadMarker() throws NullPointerException {
        // 定义Maker坐标点
        point = new LatLng(lat - 0.004, lng);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.appoint_gcoding);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions overlayoption = new MarkerOptions().position(point).icon(bitmap);

        // 在地图上添加Marker，并显示
        if (mBaiduMap == null) {
            return;
        }
        mMarker = (Marker) mBaiduMap.addOverlay(overlayoption);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
        mBaiduMap.animateMapStatus(u);
        mMarker.setAnchor(0.5f, 1f);
        showMaker(displayName);
    }

    private boolean selectStart() {
        if (StringUtils.isNullOrEmpty(etBecity.getText().toString().trim())) {
            ToastManager.getInstance().showToast(AppointCarActivity.this, getString(R.string.nac_select_start));
            return false;
        }
        return true;
    }


    private boolean searchCondition() {
        LogUtils.d("startCityName=" + startCityName + "startDistrict=" + startDistrict + "endCityName=" + endCityName + "endDistrict=" + endDistrict
                        + "startPlace=" + startPlace + "endPlace=" + endPlace + "startTime=" + startTime + "startDate=" + startDate
        );

        if (StringUtils.isNullOrEmpty(etBecity.getText().toString().trim())) {
            ToastManager.getInstance().showToast(AppointCarActivity.this, getString(R.string.nac_search_condition_start));
            return false;
        }
        if (StringUtils.isNullOrEmpty(etEncity.getText().toString().trim())) {

            ToastManager.getInstance().showToast(AppointCarActivity.this, getString(R.string.nac_search_condition_end));
            return false;
        }
        if (StringUtils.isNullOrEmpty(mTvTime.getText().toString().trim())) {

            ToastManager.getInstance().showToast(AppointCarActivity.this, getString(R.string.nac_no_start_time));
            return false;
        }
        return true;
    }

    /**
     * load服务时间
     */
    private void loadTime(List<LowestPriceRentCarEntity.ServiceTimeEntity> lowest) {
        CancelAndListViewDialog retPacketDialog = new CancelAndListViewDialog(AppointCarActivity.this, R.style.loadingDialog, lowest);
        retPacketDialog.setCancelable(true);
        retPacketDialog.setCanceledOnTouchOutside(true);
        Window win = retPacketDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        retPacketDialog.show();
    }

    /**
     * 显示图层
     */
    private void showMaker(String displayName1) {
        mView = View.inflate(AppointCarActivity.this, R.layout.map_marker_textview, null);
        TextView tvLocation = (TextView) mView.findViewById(R.id.tv_location);
        tvLocation.setText(displayName1);
        scrollToWhere();
    }

    /**
     * 动态漂移
     *
     * @param
     */
    private void scrollToWhere() {
        LatLng ll = mMarker.getPosition();
        LatLng llNew = new LatLng(ll.latitude, ll.longitude);
        InfoWindow mInfoWindow1 = new InfoWindow(BitmapDescriptorFactory.fromView(mView), llNew, -47, null);
        mBaiduMap.showInfoWindow(mInfoWindow1);
    }

    /**
     * 初始化时间选择器
     */
    private void timePicker() {
        setHourAndSecond();
        //时间选择器
        pvTime = new OptionsPickerView(this);
        //1.必须在可选时间范围内
        pvTime.setPicker(day, hour, minute, true);
        pvTime.setCyclic(false);

        //时间选择后回调
        pvTime.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int i, int i1, int i2) {
                try {
                    String d = dayCopy.get(i);
                    String h = hour.get(i).get(i1);
                    String m = minute.get(i).get(i1).get(i2);
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    sb.append(d).append(" ").append(h.substring(0,h.length()-1)).append(":").append(m.substring(0,m.length()-1));

                    startDate = d;
                    startTime = sb1.append(h.substring(0,h.length()-1)).append(":").append(m.substring(0,m.length()-1)).toString();
                    mTvTime.setText(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pvTime.show();
    }

    /**
     * 设置天 小时 秒
     */
    private void setHourAndSecond() {
        day.clear();
        hour.clear();
        minute.clear();
        String time = DateTimeUtil.getSystTimeMDHM();
        String[] split = time.split("-");
        String sysDateYMD = DateTimeUtil.getSysDateYMD();
        DateTimeUtil.getWeekStr(sysDateYMD);
        for (int i = 0; i < 30; i++) {
            dayCopy.add(sysDateYMD);
            String[] splitDay =  sysDateYMD.split("-");
            day.add(String.format("%s月%s日",splitDay[1],splitDay[2]));
            sysDateYMD = DateTimeUtil.addDay(sysDateYMD, 1);
            if (i == 0) {
                ArrayList<String> days = new ArrayList<>();
                ArrayList<ArrayList<String>> minites = new ArrayList<>();
                for (int j = (((Integer.parseInt(split[2]) / 10) == 5) ? Integer.parseInt(split[1]) + 1 : Integer.parseInt(split[1])); j < 24; j++) {
                    if (j < 10) {
                        days.add("0" + j+"时");
                    } else {
                        days.add(j + "时");
                    }
                    ArrayList<String> hours = new ArrayList<>();
                    if (Integer.parseInt(split[1]) == j) {
                        for (int k = ((Integer.parseInt(split[2]) / 10) + 1); k < 6; k++) {
                            hours.add(k * 10 + "分");
                        }
                        minites.add(hours);
                    } else {
                        for (int k = 0; k < 6; k++) {
                            if (k == 0) {
                                hours.add("00分");
                            } else {
                                hours.add(k * 10 + "分");
                            }
                        }
                        minites.add(hours);
                    }
                }
                hour.add(days);
                minute.add(minites);
            } else {
                ArrayList<String> days = new ArrayList<>();
                ArrayList<ArrayList<String>> minites = new ArrayList<>();
                for (int g = 0; g < 24; g++) {
                    if (g < 10) {
                        days.add("0" + g+"时");
                    } else {
                        days.add(g +"时");
                    }
                    ArrayList<String> hours = new ArrayList<>();
                    for (int m = 0; m < 6; m++) {
                        if (m == 0) {
                            hours.add("00分");
                        } else {
                            hours.add(m * 10 + "分");
                        }
                    }
                    minites.add(hours);
                }
                hour.add(days);
                minute.add(minites);
            }
        }
    }

    @Subscribe
    public void onRecivedCity(AppointEntity event){
        switch (event.getIsStart()) {
            case R.string.nac_from_where:

                if (!startCityName.equals(event.getPOIEntity().getCity())) {
                    AppApplication.getInstance().appointEndCity = "城市";
                }
                isChangeCity = false ;
                startCityName = event.getPOIEntity().getCity();
                startDistrict = event.getPOIEntity().getDistrict();
                startPlace = event.getPOIEntity().getName();//

                mBaiduMap.clear();
                displayName = String.format("%s %s", event.getPOIEntity().getCity(), event.getPOIEntity().getName());
                etBecity.setText(displayName);
                lng = event.getPOIEntity().getLatLng().longitude;
                lat = event.getPOIEntity().getLatLng().latitude;
                locationCity = event.getPOIEntity().getCity();
                etEncity.setText("");
                mTvTime.setText("");
                mTvTime.setHint(getString(R.string.nac_select_time));
                loadMarker();
                break;
            case R.string.nac_to_where:

                endCityName = event.getPOIEntity().getCity();
                endDistrict = event.getPOIEntity().getDistrict();
                endPlace = event.getPOIEntity().getName();
                String endStr = String.format("%s %s", event.getPOIEntity().getCity(), event.getPOIEntity().getName());
                etEncity.setText(endStr);
                break;
        }
    }


    @Override
    public void onBackPressed() {

        if (pvTime != null && pvTime.isShowing()) {
            pvTime.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().register(this);
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(this);
        mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
        mapView.onDestroy();
        mapView =null;
        super.onDestroy();
    }
}
