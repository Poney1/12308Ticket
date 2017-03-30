package com.wxws.myticket.bus.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.CityLineSearchAdapter;
import com.wxws.myticket.bus.adapter.StartCityAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.dao.BusStartCity;
import com.wxws.myticket.bus.dao.DirectStartCityHelper;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.bus.entity.CityLineEndEntity;
import com.wxws.myticket.bus.entity.POIEntity;
import com.wxws.myticket.bus.rxentity.CitySelectEvent;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ThreadManager;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.MyLetterView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * desc: 城市选择 ，包括 传统汽车票 ，火车票
 * Date: 2016/10/24 15:06
 *
 * @auther: lixiangxiang
 */
public class CityLineActivity extends BaseActivity implements BDLocationListener, OnGetGeoCoderResultListener, TextWatcher {

    private LocationClient locationClient;
    private FrameLayout myFlLeft;
    private EditText etSearch;
    private ImageButton imgDel;
    private TextView tvNoKey;
    private LinearLayout llFirst;
    private ListView lsSearchCity;
    private TextView locationCityOne;
    private RecyclerView rvCity;
    private MyLetterView mlLetter;
    private TextView citySelect;
    private TextView tvCancel;
    //城市区别
    private int cityId;
    private InnerHandler innerHandler;
    /**
     * cityStart  cityEnd  cityTrain
     */
    public static int origin = Constants.LineStartCity;//选择来源
    public static String originData = Constants.CITYLINESTART;//选择来源

    private ArrayList<String> firstArray;
    private List<CityDisplayEntity> historyList;//历史城市
    private List<CityDisplayEntity> hotList;//热门城市
    private List<CityDisplayEntity> cityList;//城市列表
    private List<CityDisplayEntity> trainSearchCity;//搜索结果
    private StartCityAdapter startCityAdapter;
    //出发到达城市
    private List<CityLineEndEntity> listToNewCity;
    private String fromCityId;//出发城市id
    private List<BusStartCity> listNewCity;
    private String inputName;

    private SuggestionSearch mSuggestionSearch;//百度建议搜索
    private SuggestionSearchOption mOption;
    private GeoCoder mGeoCoder;

    private List<POIEntity> listSearch;//搜索结果
    private CityLineSearchAdapter searchAdapter;
    private boolean isLocationSuccess = true;//定位成功
    private String fromCityName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_line);

        RxBus.getInstance().register(this);

        initPreData();

        initView();

        initData();

        initPoi();

        registerLocation();

        bindClick();
    }

    private void initPoi() {
        mSuggestionSearch = SuggestionSearch.newInstance();
        mOption = new SuggestionSearchOption();

        //第一次打开的根据定位获取周边POI
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);

    }

    private int jumpPosition;
    private void bindClick() {
        mlLetter.setOnTouchingLetterChangedListener(new MyLetterView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(final String s) {
                ToastManager.getInstance().showToast(CityLineActivity.this, s);
                if (getString(R.string.train_city_history).equals(s)) {
                    rvCity.scrollToPosition(0);
                } else if (getString(R.string.city_line_hot).equals(s)) {
                    rvCity.scrollToPosition(1);
                } else {
                    ThreadManager.getNormalPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            jumpPosition = alphaIndex(s) + 2;
                            innerHandler.sendEmptyMessage(4);
                        }
                    });
                }
            }
        });

        etSearch.addTextChangedListener(this);
        citySelect.setOnClickListener(this);

        lsSearchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listSearch.size() > 0 && position < listSearch.size()) {
                    listSearch.get(position).setSelect(origin);
                    RxBus.getInstance().post(listSearch.get(position));
                    finish();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData() {
        historyList = new ArrayList<>();
        hotList = new ArrayList<>();
        cityList = new ArrayList<>();
        listSearch = new ArrayList<>();
        etSearch.setHint(String.format(getString(R.string.city_search_hint), inputName));
        innerHandler = new InnerHandler(this);
        mLoadingPopWindow.showLoadingDialog();

        origin = cityId;
        switch (cityId) {
            case Constants.LineEndCity:
                originData = Constants.CITYLINEEND;
                break;
            case Constants.LineStartCity:
                originData = Constants.CITYLINESTART;
                break;
            case Constants.DirectStartCity:
                originData = Constants.DirectEnd;
                break;
            case Constants.DirectEndCity:
                originData = Constants.DirectStart;
                break;
            default:
                break;
        }

        historyList = (List<CityDisplayEntity>) PreferencesUtils.readDataObject(CityLineActivity.this,originData);
        startCityAdapter = new StartCityAdapter(CityLineActivity.this, historyList, hotList, cityList,originData,this.getClass().getSimpleName());
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setAdapter(startCityAdapter);
        citySelect.setText("城市");

        searchAdapter = new CityLineSearchAdapter(CityLineActivity.this, listSearch);
        lsSearchCity.setAdapter(searchAdapter);
        switch (cityId) {
            case Constants.LineEndCity:
            case Constants.DirectEndCity:
                getEndCity();
                break;
            case Constants.LineStartCity:
            case Constants.DirectStartCity:
                getDirectStartCity();
                break;
            default:
                break;
        }
    }

    /**
     * 需要跳转的条目
     *
     * @param s
     * @return
     */
    private int alphaIndex(String s) {
        int i = 0;
        while (i < cityList.size()) {
            if (cityList.get(i).isGroup() && cityList.get(i).getCity().equals(s)) {
                break;
            }
            ++i;
        }
        return i;
    }

    private void registerLocation() {
        locationClient = new LocationClient(CityLineActivity.this);
        locationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(locationClient);
        locationClient.start();
        locationClient.requestLocation();
    }

    private void initPreData() {
        cityId = getIntent().getIntExtra("cityId", 0);
        inputName = getIntent().getStringExtra("inputName");
        fromCityName = getIntent().getStringExtra("fromCityName");
        fromCityId = getIntent().getStringExtra("fromCityId");
    }

    private void initView() {
        bindLoadingView();
        myFlLeft = (FrameLayout) findViewById(R.id.fl_left);//返回
        etSearch = (EditText) findViewById(R.id.et_search);//搜索
        imgDel = (ImageButton) findViewById(R.id.img_del);//删除 内容

        tvNoKey = (TextView) findViewById(R.id.tv_no_key);//no key
        citySelect = (TextView) findViewById(R.id.city_select);//城市选择以及显示
        llFirst = (LinearLayout) findViewById(R.id.ll_first);
        lsSearchCity = (ListView) findViewById(R.id.ls_search_city);//搜索后的城市

        //取消
        tvCancel = (TextView) findViewById(R.id.tv_cancel);//定位
        locationCityOne = (TextView) findViewById(R.id.location_city_one);//定位
        rvCity = (RecyclerView) findViewById(R.id.rv_city);//正常的城市
        mlLetter = (MyLetterView) findViewById(R.id.ml_letter);//索引

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (!StringUtils.isNullOrEmpty(bdLocation.getAddrStr())) {
            locationCityOne.setText(String.format("当前定位：%s", bdLocation.getCity()));
            citySelect.setText(bdLocation.getCity());
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude())));
        } else {
            isLocationSuccess = false;
            showWhatView(llFirst);
        }
    }
    private boolean  isShowLlFirst  = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_select:
                if (isShowLlFirst && isLocationSuccess) {
                    setDrawableRight(R.mipmap.direct_up);
                    isShowLlFirst = true;
                } else {
                    isShowLlFirst =false;
                    setDrawableRight(R.mipmap.appoint_dropdown);
                    showWhatView(llFirst);
                }
                break;
        }
    }

    //设置左DrawableLeft
    private void setDrawableRight(@DrawableRes int id) {
        Drawable drawable = getResources().getDrawable(id);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        citySelect.setCompoundDrawables(null, null, drawable, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
        locationClient.unRegisterLocationListener(this);
        locationClient.stop();
    }

    /**
     * 拉取 汽车票出发城市
     */
    int version = 0;

    private void getDirectStartCity() {
        version = PreferencesUtils.getDataInt(CityLineActivity.this, Constants.CITYLINESTARTVERSION, 0);
        HttpRequestManager.getInstance().cityLineFromCityList(version, new ApiSubscriber<JsonResult<List<BusStartCity>>>
                (CityLineActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<BusStartCity>> listJsonResult) {

                if ("0000".equals(listJsonResult.getResultCode())) {

                    listNewCity = listJsonResult.getObject();
                    if (listNewCity != null && listNewCity.size() > 0) {
                        ThreadManager.getNormalPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                List<BusStartCity> listCity = DirectStartCityHelper.getInstance().getAllCity();
                                if (listCity.size() == 0) {
                                    DirectStartCityHelper.getInstance().addAllBusCity(listNewCity);
                                    version = 1;
                                    PreferencesUtils.putDataInt(CityLineActivity.this, Constants.CITYLINESTARTVERSION, version);
                                    getAllList(true);
                                } else {
                                    int newVersion = 0;
                                    int newVersion1 = version;
                                    for (BusStartCity startCity : listNewCity) {
                                        newVersion = startCity.getVersion();
                                        if (newVersion > newVersion1) {
                                            newVersion1 = newVersion;
                                        }
                                        DirectStartCityHelper.getInstance().addCity(startCity);
                                    }
                                    version = newVersion1;
                                    PreferencesUtils.putInt(CityLineActivity.this, Constants.CITYLINESTARTVERSION, version);
                                    getAllList(true);
                                }
                            }
                        });
                    } else {
                        ThreadManager.getNormalPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                getAllList(true);
                            }
                        });
                    }
                } else {
                    ThreadManager.getNormalPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            getAllList(true);
                        }
                    });
                }
            }
        });
    }

    /**
     * 到达城市
     */
    private void getEndCity() {

        HttpRequestManager.getInstance().cityLineToCityList(fromCityId, fromCityName, new ApiSubscriber<JsonResult<List<CityLineEndEntity>>>(CityLineActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<CityLineEndEntity>> listJsonResult) {

                if ("0000".equals(listJsonResult.getResultCode())) {
                    listToNewCity = listJsonResult.getObject();
                    if (listToNewCity != null && listToNewCity.size() > 0){
                        ThreadManager.getNormalPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                getAllEndCity();
                            }
                        });
                    }
                }else {
                   mLoadingPopWindow.dismissLoadingDialog();
                }
            }
        });

    }

    /**
     * 关闭 window
     */
    private int j = 0;

    private void closePopupWindow() {
        if (j == 1) {
            mLoadingPopWindow.dismissLoadingDialog();
        } else {
            j++;
        }
    }

    //获取所有到达站点
    private void getAllEndCity() {
        firstArray = new ArrayList<>();
        Collections.sort(listToNewCity);
        for (CityLineEndEntity fChar : listToNewCity) {
            String firstChar = fChar.getFirstChar().toUpperCase();
            if (!firstArray.contains(firstChar)) {
                firstArray.add(firstChar);
            }
        }

        Collections.sort(firstArray);

        for (int i = 0; i < firstArray.size(); i++) {
            Iterator<CityLineEndEntity> iterator = listToNewCity.iterator();
            if (!StringUtils.isNullOrEmpty(firstArray.get(i))) {
                CityDisplayEntity tHeader = new CityDisplayEntity();
                tHeader.setGroup(true);
                tHeader.setCity(firstArray.get(i).toUpperCase());
                cityList.add(tHeader);
                while (iterator.hasNext()) {
                    CityLineEndEntity trainStartCityModel = iterator.next();
                    if (firstArray.get(i).toLowerCase().equals(trainStartCityModel.getFirstChar())) {
                        CityDisplayEntity tCity = new CityDisplayEntity();
                        tCity.setGroup(false);
                        tCity.setCity(trainStartCityModel.getName());
                        tCity.setCityId(trainStartCityModel.getId() + "");
                        cityList.add(tCity);
                    }
                }
            }
        }

        for (CityLineEndEntity busEndCity : listToNewCity) {
            if ("1".equals(busEndCity.getIsHot())) {
                CityDisplayEntity hCity = new CityDisplayEntity();
                hCity.setCity(busEndCity.getName());
                hCity.setCityId(busEndCity.getId() + "");
                hotList.add(hCity);
            }
        }
        firstArray.add(0, getString(R.string.train_city_hot));
        firstArray.add(0, getString(R.string.train_city_history));
        innerHandler.sendEmptyMessage(5);
    }

    //获取所有的集合数据
    private void getAllList(boolean isLocal) {
        if (isLocal) {
            //拿到c出发城市
            listNewCity = DirectStartCityHelper.getInstance().getAllCity();
        }
        firstArray = new ArrayList<>();
        if (isLocal) {
            firstArray = DirectStartCityHelper.getInstance().getFirstChar();
        } else {
            for (BusStartCity fChar : listNewCity) {
                String firstChar = fChar.getFirstChar().toUpperCase();
                if (!firstArray.contains(firstChar)) {
                    firstArray.add(firstChar);
                }
            }
        }
        Collections.sort(firstArray);
        Collections.sort(listNewCity);
        for (int i = 0; i < firstArray.size(); i++) {
            Iterator<BusStartCity> iterator = listNewCity.iterator();
            if (!StringUtils.isNullOrEmpty(firstArray.get(i))) {
                CityDisplayEntity tHeader = new CityDisplayEntity();
                tHeader.setGroup(true);
                tHeader.setCity(firstArray.get(i).toUpperCase());
                cityList.add(tHeader);
                while (iterator.hasNext()) {
                    BusStartCity trainStartCityModel = iterator.next();
                    if (firstArray.get(i).toLowerCase().equals(trainStartCityModel.getFirstChar())) {
                        CityDisplayEntity tCity = new CityDisplayEntity();
                        tCity.setGroup(false);
                        tCity.setCity(trainStartCityModel.getName());
                        tCity.setCityId(trainStartCityModel.getId() + "");
                        cityList.add(tCity);
                    }
                }
            }
        }

        for (BusStartCity trainStartCityModel : listNewCity) {
            if ("1".equals(trainStartCityModel.getIsHot())) {
                CityDisplayEntity hCity = new CityDisplayEntity();
                hCity.setCity(trainStartCityModel.getName());
                hCity.setCityId(trainStartCityModel.getId() + "");
                hotList.add(hCity);
            }
        }
        firstArray.add(0, getString(R.string.train_city_hot));
        firstArray.add(0, getString(R.string.train_city_history));
        innerHandler.sendEmptyMessage(1);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult.getPoiList() != null && reverseGeoCodeResult.getPoiList().size() > 0) {
            showWhatView(lsSearchCity);
            listSearch.clear();
            for (PoiInfo poiInfo : reverseGeoCodeResult.getPoiList()) {
                if (poiInfo.location.latitude != 0 && poiInfo.location.longitude != 0) {
                    listSearch.add(new POIEntity(poiInfo.name, poiInfo.address, reverseGeoCodeResult.getAddressDetail().city, reverseGeoCodeResult.getAddressDetail().district, poiInfo.location));
                }
            }
            innerHandler.sendEmptyMessage(2);
        }
    }

    //展示哪个view
    private void showWhatView(View i) {
        switch (i.getId()) {
            case R.id.tv_no_key:
                tvNoKey.setVisibility(View.VISIBLE);
                lsSearchCity.setVisibility(View.GONE);
                llFirst.setVisibility(View.GONE);
                locationCityOne.setVisibility(View.GONE);
                break;
            case R.id.ll_first:
                tvNoKey.setVisibility(View.GONE);
                lsSearchCity.setVisibility(View.GONE);
                llFirst.setVisibility(View.VISIBLE);
                locationCityOne.setVisibility(View.VISIBLE);
                break;
            case R.id.ls_search_city:
                locationCityOne.setVisibility(View.GONE);
                lsSearchCity.setVisibility(View.VISIBLE);
                llFirst.setVisibility(View.GONE);
                locationCityOne.setVisibility(View.GONE);
                break;
            default:

                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tvNoKey.setText(String.format("未找到与%s相关的城市", s.toString()));
        if (!StringUtils.isNullOrEmpty(s.toString())) {
            if (!StringUtils.isNullOrEmpty(citySelect.getText().toString())) {
                mSuggestionSearch.requestSuggestion(mOption.city(citySelect.getText().toString()).keyword(s.toString()));
                mSuggestionSearch.setOnGetSuggestionResultListener(poiListener);
            }
        } else {
            showWhatView(tvNoKey);
        }
    }

    @Subscribe
    public void onRecievedCity(CitySelectEvent citySelectEvent) {
        if (!StringUtils.isNullOrEmpty(citySelectEvent.getCity())) {
            citySelect.setText(citySelectEvent.getCity());
        }
    }

    private static class InnerHandler extends Handler {
        WeakReference<CityLineActivity> activityWeakReference;

        public InnerHandler(CityLineActivity cityActivity) {
            this.activityWeakReference = new WeakReference<>(cityActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CityLineActivity cityActivity = activityWeakReference.get();
            if (cityActivity == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    cityActivity.startCityAdapter.notifyDataSetChanged();
                    //刷新 letter
                    MyLetterView.b = cityActivity.firstArray.toArray(new String[]{});
                    cityActivity.mlLetter.postInvalidate();
                    cityActivity.closePopupWindow();
                    break;
                case 2:
                    cityActivity.searchAdapter.replaceAll(cityActivity.listSearch);
                    cityActivity.closePopupWindow();
                    break;
                case 3:
                    cityActivity.showWhatView(cityActivity.tvNoKey);
                    break;
                case  4:
                    cityActivity.rvCity.getLayoutManager().scrollToPosition(cityActivity.jumpPosition);
                    break;
                case 5:
                    cityActivity.startCityAdapter.notifyDataSetChanged();
                    //刷新 letter
                    MyLetterView.b = cityActivity.firstArray.toArray(new String[]{});
                    cityActivity.mlLetter.postInvalidate();
                    cityActivity.mLoadingPopWindow.dismissLoadingDialog();
                    break;
                default:
                    break;
            }
        }
    }

    OnGetSuggestionResultListener poiListener = new OnGetSuggestionResultListener() {

        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult != null) {
                listSearch.clear();
                if (suggestionResult.getAllSuggestions() != null && suggestionResult.getAllSuggestions().size() > 0) {

                    for (SuggestionResult.SuggestionInfo suggestionInfo : suggestionResult.getAllSuggestions()) {
                        if (suggestionInfo != null && suggestionInfo.pt != null && suggestionInfo.pt.latitude != 0 && suggestionInfo.pt.longitude != 0) {
                            listSearch.add(new POIEntity(suggestionInfo.key, suggestionInfo.key, suggestionInfo.city, suggestionInfo.district, suggestionInfo.pt));
                        }
                    }
                    if (listSearch.size() > 0) {
                        showWhatView(lsSearchCity);
                        innerHandler.sendEmptyMessage(2);
                    } else {
                        showWhatView(tvNoKey);
                        innerHandler.sendEmptyMessage(3);
                    }
                }
            }
        }
    };

}
