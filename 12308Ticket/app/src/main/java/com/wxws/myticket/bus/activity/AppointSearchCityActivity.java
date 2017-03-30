package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
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
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.SearchAppointSlCityAdapter;
import com.wxws.myticket.bus.adapter.SearchAppointAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.POICityEntity;
import com.wxws.myticket.bus.entity.POIEntity;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 约租车城市搜索
 * Date: 2016-06-20 14:49
 *
 * @auther: lixiangxiang
 */
public class AppointSearchCityActivity extends BaseActivity {

    private EditText mEtSearch;
    private TextView mTvCity;
    private TextView mTvCancel;
    private TextView mTvLocation;
    private GridView mGridCity;
    private ListView mSearchResult;
    private RelativeLayout mRelativeLayout;//控制城市选择是否出现
    private TextView tvNokeyword;

    private SuggestionSearch mSuggestionSearch;//百度建议搜索
    private SuggestionSearchOption mOption;

    private List<POIEntity> listAddress = new ArrayList<>(); //每次搜索出现的地址
    private List<POIEntity> cityList = new ArrayList<>(); //搜索后展示的城市
    private List<POICityEntity> mPOICities = new ArrayList<>();//
    private String addCity = "";//筛选城市
    private SearchAppointAdapter mAdapter;
    private int mWhere;//来源
    private boolean isSelectCity = false;//默认不是选择城市
    private List<String> mListApp = new ArrayList<>();//城市 网格
    private String mAppCity; //显示的城市
    private SearchAppointSlCityAdapter mApp;//网格

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mRelativeLayout.setVisibility(View.GONE);
            mSearchResult.setVisibility(View.VISIBLE);
            switch (msg.what) {
                case 0:
                    mAdapter.replaceAll(listAddress);
                    break;
                case 1:
                    mAdapter.replaceAll(cityList);
                    break;
            }
            return false;
        }
    });
    private String mLocationCity;
    private String locationCity;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_appoint_search);

        bindLoadingView();

        initView();

        initData();

        bindClick();

    }

    private void bindClick() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethod.closeInputMethod(AppointSearchCityActivity.this);
                finish();
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 0) {
                    tvNokeyword.setVisibility(View.GONE);
                    if (!StringUtils.isNullOrEmpty(addCity)) {
                        mSearchResult.setVisibility(View.GONE);
                        mRelativeLayout.setVisibility(View.GONE);
                        mLlLayoutLoading.setVisibility(View.VISIBLE);
                        mSuggestionSearch.requestSuggestion(mOption.city(addCity).keyword(s.toString()));
                        mSuggestionSearch.setOnGetSuggestionResultListener(poiListener);
                    }
                } else {
                    mLlLayoutLoading.setVisibility(View.GONE);
                    mSearchResult.setVisibility(View.GONE);
                    if (mRelativeLayout.getVisibility() != View.VISIBLE) {
                        showKey();
                    }
                }
            }
        });
    }

    private void initData() {
        showKey();
        showGridViewCity();//加载数据
        mLlLayoutLoading.setVisibility(View.GONE);
        mWhere = getIntent().getIntExtra("where", 0);
        locationCity = getIntent().getStringExtra("locationCity");//定位城市
        if (!StringUtils.isNullOrEmpty(locationCity)) {
            mTvLocation.setText(String.format("当前城市：%s", locationCity));
        }
        mAdapter = new SearchAppointAdapter(AppointSearchCityActivity.this, cityList, mWhere);
        mSearchResult.setAdapter(mAdapter);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mOption = new SuggestionSearchOption();

        switch (mWhere) {
            case R.string.nac_from_where:
                mAppCity = locationCity;
                if (!StringUtils.isNullOrEmpty(locationCity)) {
                    mTvCity.setText(mAppCity);//设置城市
                    addCity = mAppCity;
                }
                getSelectCity();
                mEtSearch.setHint(getString(R.string.nac_from_where));
                break;
            case R.string.nac_to_where:
                boolean isChangeCity = getIntent().getBooleanExtra("isChangeCity",false);
                if (isChangeCity){
                    mAppCity  = "城市";
                }else {
                    mAppCity= AppApplication.getInstance().appointEndCity;
                }

                if (!StringUtils.isNullOrEmpty(mAppCity)) {
                    mTvCity.setText(mAppCity);//设置城市
                    addCity = mAppCity;
                }
                if (mAppCity.equals("城市")) {
                    getEndCity();
                } else {
                    addCity = mAppCity;
                    mTvCity.setText(mAppCity);
                }
                mEtSearch.setHint(getString(R.string.nac_to_where));
                break;
        }

        mTvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWhere == R.string.nac_to_where) {
                    if (mListApp.size() > 0) {
                        showTVCity();
                    } else {
                        getEndCity();
                    }
                } else {
                    showTVCity();
                }
            }
        });

        if ("城市".equals(mTvCity.getText().toString().trim())) {
            showCitySelect();
            isSelectCity = true;
        } else {
            closeCity();
        }
    }

    /**
     * 显示出发终点网格
     */
    private void showTVCity() {
        mEtSearch.setText("");
        tvNokeyword.setVisibility(View.GONE);
        if (!(mRelativeLayout.getVisibility()==View.VISIBLE)) {
            showCitySelect();
            isSelectCity = true;
        } else if (isSelectCity && !"城市".equals(mTvCity.getText().toString().trim())) {
            closeCity();
            isSelectCity = false;
        }
    }

    /**
     * 获取目的城市
     * */
    private void getEndCity() {
        mLlLayoutLoading.setVisibility(View.VISIBLE);
        String startCity = getIntent().getStringExtra("startCity");
       /* mWebAPI.getAppointArriveCity("1", startCity, netErrorLisenterTag,
                new ResponseFactory<List<AppointArriveEntity>>(new TypeReference<WebResult<List<AppointArriveEntity>>>() {
                }) {
                    @Override
                    public void parseResponse(WebResult<List<AppointArriveEntity>> object) {
                        if ("0000".equals(object.getResultCode())) {
                            List<AppointArriveEntity> listArrive = object.getObject();
                            if (listArrive != null && listArrive.size() > 0) {
                                layout_loading.setVisibility(View.GONE);
                                mListApp.clear();
                                for (int i = 0; i < listArrive.size(); i++) {
                                    mListApp.add(listArrive.get(i).getEndCityName());
                                }
                                showCitySelect();
                            } else {
                                showErrorOrNoData(R.string.query_station_fail, R.string.click_return, R.drawable.tip1);
                            }
                        } else {
                            showErrorOrNoData(R.string.query_station_fail, R.string.click_return, R.drawable.tip1);
                        }
                    }
                });*/
    }


    /**
     * 显示关键字
     */
    private void showKey() {
        tvNokeyword.setText("请在上面搜索框输入关键字");
        tvNokeyword.setVisibility(View.VISIBLE);
    }

    /**
     * 获取已开通出发城市
     */
    private void getSelectCity() {
        mListApp = (List<String>) PreferencesUtils.readDataObject(AppointSearchCityActivity.this, Constants.APPOINTSTARTCITY);
        if (mListApp != null && mListApp.size() > 0) {
            if (getIntent().getBooleanExtra("isHaveCity", false) || mAppCity != null) {
                closeCity();
            } else {
                showCitySelect();
            }
        } else {
            mLlLayoutLoading.setVisibility(View.VISIBLE);
            //获取出发城市数据
           /* mWebAPI.getAppointCitys("1", netErrorLisenterTag, new ResponseFactory<List<AppointEntity>>(new TypeReference<WebResult<List<AppointEntity>>>() {
            }) {
                @Override
                public void parseResponse(WebResult<List<AppointEntity>> object) {

                    if ("0000".equals(object.getResultCode())) {
                        List<AppointEntity> listStart = object.getObject();
                        if (listStart != null && listStart.size() > 0) {
                            layout_loading.setVisibility(View.GONE);
                            mListApp.clear();
                            for (int i = 0; i < listStart.size(); i++) {
                                mListApp.add(listStart.get(i).getStartCityName());
                            }
                            showCitySelect();
                            PreferencesUtils.saveObject(AppointSearchCityActivity.this, PreferencesUtils.PREFERENCE_NAME_DATA, Constants.APPOINTSTARTCITY, mListApp);
                        } else {
                            showErrorOrNoData(R.string.loaddata_fail, R.string.click_return, R.drawable.tip1);
                        }
                    } else {
                        showErrorOrNoData(R.string.loaddata_fail, R.string.click_return, R.drawable.tip1);
                    }
                }
            });*/
        }
    }

    private void initView() {
        mEtSearch = (EditText) findViewById(R.id.etSearch);
        mTvCity = (TextView) findViewById(R.id.city_select);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvLocation = (TextView) findViewById(R.id.location_city_one);
        mGridCity = (GridView) findViewById(R.id.gridCity);
        mSearchResult = (ListView) findViewById(R.id.search_result);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_grid_control);
        mRelativeLayout.setVisibility(View.GONE);
        tvNokeyword = (TextView) findViewById(R.id.tvNokeyword);
    }


    private void showGridViewCity() {
        mApp = new SearchAppointSlCityAdapter(AppointSearchCityActivity.this, mListApp,mAppCity);
        mGridCity.setAdapter(mApp);
    }

    /**
     * 刷新选中城市
     *
     * @param city
     */
    public void refreshCity(String city) {
        isSelectCity = false;
        mEtSearch.setText("");
        mTvCity.setText(city);
        addCity = city;
        closeCity();
        switch (mWhere) {
            case R.string.nac_from_where:
                mApp.setSelectCity(city);
                break;
            case R.string.nac_to_where:
                AppApplication.getInstance().appointEndCity = city;
                break;
        }

    }

    /**
     * 关闭城市选择
     */
    private void closeCity() {
        tvNokeyword.setVisibility(View.VISIBLE);
        mSearchResult.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.GONE);
    }

    /**
     * 展示需要选择的城市
     */
    private void showCitySelect() {
        if (mListApp != null) {
            mApp.setSelectCity(addCity);
            mApp.replaceAll(mListApp);
            tvNokeyword.setVisibility(View.GONE);
            mSearchResult.setVisibility(View.GONE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 未找到关键字 隐藏
     */
    private void goneView() {
        mLlLayoutLoading.setVisibility(View.GONE);
        mSearchResult.setVisibility(View.GONE);
        tvNokeyword.setVisibility(View.VISIBLE);
        tvNokeyword.setText(String.format("未找到%s,换个关键字试试", mEtSearch.getText()));
    }


    OnGetSuggestionResultListener poiListener = new OnGetSuggestionResultListener() {

        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            mLlLayoutLoading.setVisibility(View.GONE);
            if (suggestionResult != null) {
                listAddress.clear();
                if (suggestionResult.getAllSuggestions() != null && suggestionResult.getAllSuggestions().size() > 0) {
                    mSearchResult.setVisibility(View.VISIBLE);
                    tvNokeyword.setVisibility(View.GONE);
                    for (SuggestionResult.SuggestionInfo suggestionInfo : suggestionResult.getAllSuggestions()) {
                        if (suggestionInfo != null && suggestionInfo.pt != null && suggestionInfo.pt.latitude != 0 && suggestionInfo.pt.longitude != 0) {
                            if (addCity.equals(suggestionInfo.city) && suggestionInfo.district != null) {
                                listAddress.add(new POIEntity(suggestionInfo.key, suggestionInfo.district, suggestionInfo.city, suggestionInfo.district, suggestionInfo.pt));
                            } else if (suggestionInfo.city == null && suggestionInfo.district == null) {
                                listAddress.add(new POIEntity(suggestionInfo.key, suggestionInfo.district, suggestionInfo.city, suggestionInfo.district, suggestionInfo.pt));
                            } else if (suggestionInfo.city != null && suggestionInfo.district == null) {
                                listAddress.add(new POIEntity(suggestionInfo.key, suggestionInfo.district, suggestionInfo.city, suggestionInfo.district, suggestionInfo.pt));
                            }
                        }
                    }

                    if (listAddress.size() > 0 && listAddress.get(0).getAddress() != null && listAddress.get(0).getLatLng() != null) {
                        mHandler.sendEmptyMessage(0);
                    }
                    for (int i = 0; i < listAddress.size(); i++) {
                        GeoCoder mMGeoCoder = GeoCoder.newInstance();
                        mMGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                            @Override
                            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                            }

                            @Override
                            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                                String district = "";
                                if (reverseGeoCodeResult.getAddressDetail() != null)
                                    district = reverseGeoCodeResult.getAddressDetail().district;
                                POICityEntity poiEntity2 = new POICityEntity();
                                poiEntity2.district = district;
                                poiEntity2.name = reverseGeoCodeResult.getAddress();
                                poiEntity2.cityCopy = reverseGeoCodeResult.getAddressDetail().city;
                                mPOICities.add(poiEntity2);
                                if (mPOICities.size() == listAddress.size()) {
                                    cityList.clear();
                                    for (int i = 0; i < listAddress.size(); i++) {
                                        if (addCity.equals(mPOICities.get(i).cityCopy)) {
                                            listAddress.get(i).setAddress(mPOICities.get(i).district);
                                            listAddress.get(i).setCity(addCity);
                                            listAddress.get(i).setDistrict(mPOICities.get(i).district);
                                            cityList.add(listAddress.get(i));
                                            mHandler.sendEmptyMessage(1);
                                        }
                                    }
                                    listAddress.clear();
                                    listAddress.addAll(cityList);
                                    if (listAddress.size() == 0) {
                                        goneView();
                                    }
                                    mPOICities.clear();
                                }
                            }
                        });
                        if (listAddress.get(i).getCity() == null || listAddress.get(i).getAddress() == null) {
                            mMGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                                    .location(new LatLng(listAddress.get(i).getLatLng().latitude, listAddress.get(i).getLatLng().longitude)));
                        }
                    }
                    if (listAddress.size() == 0) {
                        goneView();
                    }
                } else {
                    goneView();
                }
            } else {
                goneView();
            }
        }
    };

}