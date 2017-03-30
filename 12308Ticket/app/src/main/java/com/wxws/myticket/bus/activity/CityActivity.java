package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.SearchCityAdapter;
import com.wxws.myticket.bus.adapter.StartCityAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.dao.BusEndCity;
import com.wxws.myticket.bus.dao.BusEndCityHelper;
import com.wxws.myticket.bus.dao.BusStartCity;
import com.wxws.myticket.bus.dao.BusStartCityHelper;
import com.wxws.myticket.bus.entity.BusStationListEntity;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.LogUtils;
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
public class CityActivity extends BaseActivity implements BDLocationListener, TextWatcher {

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
    //城市区别
    private int cityId;
    private InnerHandler innerHandler;
    /**
     * cityStart  cityEnd  cityTrain
     */
    public static String origin = Constants.CITYSTART;//选择来源

    private ArrayList<String> firstArray;
    private List<CityDisplayEntity> historyList;//历史城市
    private List<CityDisplayEntity> hotList;//热门城市
    private List<CityDisplayEntity> cityList;//城市列表
    private StartCityAdapter startCityAdapter;
    //出发到达城市
    private List<BusEndCity> listToNewCity;
    private String fromCityId;//出发城市id
    private List<BusStartCity> listNewCity;
    private String inputName;
    private SearchCityAdapter searchCity;//搜索 后显示的城市
    private String fromCityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initPreData();

        registerLocation();

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {

        mlLetter.setOnTouchingLetterChangedListener(new MyLetterView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                ToastManager.getInstance().showToast(CityActivity.this, s);
                if (getString(R.string.train_city_history).equals(s)) {
                    rvCity.getLayoutManager().scrollToPosition(0);
                } else if (getString(R.string.train_city_hot).equals(s)) {
                    rvCity.getLayoutManager().scrollToPosition(1);
                } else {
                    int position = alphaIndex(s) + 2;
                    rvCity.getLayoutManager().scrollToPosition(position);
                }
            }
        });

        myFlLeft.setOnClickListener(this);
        etSearch.addTextChangedListener(this);
    }


    private void initData() {
        historyList = new ArrayList<>();
        hotList = new ArrayList<>();
        cityList = new ArrayList<>();

        etSearch.setHint(String.format(getString(R.string.city_search_hint), inputName));

        switch (cityId) {
            case Constants.BUSENDCITY:
                origin = Constants.CITYEND;
                break;
            case Constants.BUSSTARTCITY:
                origin = Constants.CITYSTART;
                break;
            case Constants.TRAINCITY:
                origin = Constants.CITYTRAIN;
                break;
            default:
                break;
        }

        searchCity = new SearchCityAdapter(CityActivity.this, listShow);
        searchCity.setOrigin(origin);
        lsSearchCity.setAdapter(searchCity);
        innerHandler = new InnerHandler(this);

        historyList = (List<CityDisplayEntity>) PreferencesUtils.readDataObject(CityActivity.this, origin);

        if (historyList == null){
            historyList = new ArrayList<>();
        }
        startCityAdapter = new StartCityAdapter(CityActivity.this, historyList, hotList, cityList,origin,this.getClass().getSimpleName());
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setAdapter(startCityAdapter);
        mLoadingPopWindow.showLoadingDialog();
        switch (cityId) {
            case Constants.BUSENDCITY:
                fromCityId = getIntent().getStringExtra("fromCityId");
                fromCityName = getIntent().getStringExtra("fromCityName");
                getEndCity();
                break;
            case Constants.BUSSTARTCITY:
                getBusStartCity();
                break;
            case Constants.TRAINCITY:
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
        locationClient = new LocationClient(CityActivity.this);
        locationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(locationClient);
        locationClient.start();
    }

    private void initPreData() {
        cityId = getIntent().getIntExtra("cityId", 0);
        inputName = getIntent().getStringExtra("inputName");
    }

    private void initView() {

        bindLoadingView();

        myFlLeft = (FrameLayout) findViewById(R.id.fl_left);//返回
        etSearch = (EditText) findViewById(R.id.et_search);//搜索
        imgDel = (ImageButton) findViewById(R.id.img_del);//删除 内容

        tvNoKey = (TextView) findViewById(R.id.tv_no_key);//no key
        llFirst = (LinearLayout) findViewById(R.id.ll_first);
        lsSearchCity = (ListView) findViewById(R.id.ls_search_city);//搜索后的城市

        locationCityOne = (TextView) findViewById(R.id.location_city_one);//定位
        rvCity = (RecyclerView) findViewById(R.id.rv_city);//正常的城市
        mlLetter = (MyLetterView) findViewById(R.id.ml_letter);//索引
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (!StringUtils.isNullOrEmpty(bdLocation.getAddrStr())) {
            locationCityOne.setText(String.format("当前定位：%s", bdLocation.getCity()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.unRegisterLocationListener(this);
        locationClient.stop();
    }

    /**
     * 拉取 汽车票出发城市
     */
    int version = 0;

    private void getBusStartCity() {

        version = PreferencesUtils.getDataInt(CityActivity.this, Constants.BUSSTARTVERSION, 0);

        HttpRequestManager.getInstance().fromCityList(version, new ApiSubscriber<JsonResult<List<BusStartCity>>>(CityActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<BusStartCity>> payParamModelJsonResult) {
                if ("0000".equals(payParamModelJsonResult.getResultCode())) {

                    listNewCity = payParamModelJsonResult.getObject();

                    if (listNewCity != null && listNewCity.size() > 0) {
                        if (version == 0) {
                            ThreadManager.getNormalPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    BusStartCityHelper.getInstance().addAllBusCity(listNewCity);
                                    version = 1;
                                    PreferencesUtils.putDataInt(CityActivity.this, Constants.BUSSTARTVERSION, version);
                                    getAllList(true);
                                }
                            });
                        } else {
                            ThreadManager.getNormalPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    int newVersion = 0;
                                    int newVersion1 = version;
                                    for (BusStartCity startCity : listNewCity) {
                                        newVersion = startCity.getVersion();
                                        if (newVersion > newVersion1) {
                                            newVersion1 = newVersion;
                                        }
                                        BusStartCityHelper.getInstance().addCity(startCity);
                                    }
                                    version = newVersion1;
                                    PreferencesUtils.putInt(CityActivity.this, Constants.BUSSTARTVERSION, version);
                                    getAllList(true);
                                }
                            });
                        }
                    } else {
                        ThreadManager.getNormalPool().execute(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        getAllList(true);
                                    }
                                });
                    }
                } else {
                    ThreadManager.getNormalPool().execute(
                            new Runnable() {
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
     * 获取 站点列表
     */
    private void getStationList(String keyWord){
        String type  = "1" ;
        String fromStationId = "";
        String fromStationName = "";
        HttpRequestManager.getInstance().fromToStationList(keyWord, type, fromCityId, fromCityName,
                fromStationId, fromStationName, new ApiSubscriber<JsonResult<BusStationListEntity>>(CityActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<BusStationListEntity> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())){
                    listShow.clear();
                    showCity(2);
                    BusStationListEntity  object = listJsonResult.getObject();
                    if (object!=null && object.getStationList()!=null){
                        for (BusStationListEntity.StationListBean entity:object.getStationList()){
                            CityDisplayEntity displayEntity = new CityDisplayEntity();
                            displayEntity.setCity(object.getName());
                            displayEntity.setCityId(object.getId());
                            displayEntity.setStationId(entity.getStationId());
                            displayEntity.setStationName(entity.getStationName());
                            listShow.add(displayEntity);
                        }
                        innerHandler.sendEmptyMessage(2);
                    }
                }else {
                    showCity(3);
                }
            }
        });
    }
    /**
     * 到达城市
     */
    int toVersion = 0;

    private void getEndCity() {
        toVersion = PreferencesUtils.getDataInt(CityActivity.this, Constants.BUSENDVERSION, 0);
        HttpRequestManager.getInstance().toCityList(toVersion, fromCityId, fromCityName, new ApiSubscriber<JsonResult<List<BusEndCity>>>(CityActivity.this, ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<BusEndCity>> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())) {
                    listToNewCity = listJsonResult.getObject();
                    if (listToNewCity != null && listToNewCity.size() > 0) {
                        ThreadManager.getNormalPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                List<BusEndCity> listCity = BusEndCityHelper.getInstance().getAllCity(fromCityId);
                                if (listCity.size() == 0) {
                                    BusEndCityHelper.getInstance().addAllBusCity(listToNewCity);
                                    version = 1;
                                    PreferencesUtils.putDataInt(CityActivity.this, Constants.BUSENDVERSION, version);
                                    getAllEndCity(true);
                                } else {
                                    int newVersion = 0;
                                    int newVersion1 = version;
                                    for (BusEndCity endCity : listToNewCity) {
                                        newVersion = endCity.getVersion();
                                        if (newVersion > newVersion1) {
                                            newVersion1 = newVersion;
                                        }
                                        BusEndCityHelper.getInstance().addCity(endCity);
                                    }
                                    version = newVersion1;
                                    PreferencesUtils.putDataInt(CityActivity.this, Constants.BUSENDVERSION, version);
                                    getAllEndCity(true);
                                }
                            }
                        });
                    } else {
                        getAllEndCity(true);
                    }
                } else {
                    getAllEndCity(true);
                }
            }
        });
    }

    //获取所有到达站点
    private void getAllEndCity(boolean isLocal) {
        listToNewCity = BusEndCityHelper.getInstance().getAllCity(fromCityId);
        firstArray = new ArrayList<>();

        for (BusEndCity fChar : listToNewCity) {
            String firstChar = fChar.getFirstChar().toUpperCase();
            if (!firstArray.contains(firstChar)) {
                firstArray.add(firstChar);
            }
        }

        Collections.sort(firstArray);

        Collections.sort(listToNewCity);

        for (int i = 0; i < firstArray.size(); i++) {
            Iterator<BusEndCity> iterator = listToNewCity.iterator();
            if (!StringUtils.isNullOrEmpty(firstArray.get(i))) {
                CityDisplayEntity tHeader = new CityDisplayEntity();
                tHeader.setGroup(true);
                tHeader.setCity(firstArray.get(i).toUpperCase());
                cityList.add(tHeader);
                while (iterator.hasNext()) {
                    BusEndCity trainStartCityModel = iterator.next();
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

        for (BusEndCity busEndCity : listToNewCity) {
            if ("1".equals(busEndCity.getIsHot())) {
                CityDisplayEntity hCity = new CityDisplayEntity();
                hCity.setCity(busEndCity.getName());
                hCity.setCityId(busEndCity.getId() + "");
                hotList.add(hCity);
            }
        }
        if (cityId != Constants.BUSENDCITY){
            firstArray.add(0, getString(R.string.train_city_hot));
        }
        firstArray.add(0, getString(R.string.train_city_history));
        innerHandler.sendEmptyMessage(1);
    }

    //获取所有的集合数据
    private void getAllList(boolean isLocal) {
        if (isLocal) {
            //拿到c出发城市
            listNewCity = BusStartCityHelper.getInstance().getAllCity();
        }

        firstArray = BusStartCityHelper.getInstance().getFirstChar();

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_left:
                finish();
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
        if (!StringUtils.isNullOrEmpty(s.toString()))
            if (StringUtils.getTextChineseCount(s.toString())>=2){
                getStationList(s.toString());
            }else {
                showSelectData(s.toString());
            }

        else
            showCity(1);
    }

    /**
     * 筛选数据
     *
     * @param s
     */
    private List<CityDisplayEntity> listShow = new ArrayList<>();

    private void showSelectData(String s) {
        showCity(2);
        listShow.clear();
        switch (cityId) {
            case Constants.BUSENDCITY:
                List<BusEndCity> listEnd = BusEndCityHelper.getInstance().getTrainListByStr(s.toString());
                if (listEnd != null && listEnd.size() > 0) {
                    for (BusEndCity bus : listEnd) {
                        CityDisplayEntity city = new CityDisplayEntity();
                        city.setCity(bus.getName());
                        city.setCityId(bus.getId() + "");
                        listShow.add(city);
                    }
                } else {
                    showCity(3);
                }
                break;
            case Constants.BUSSTARTCITY:
                List<BusStartCity> listStart = BusStartCityHelper.getInstance().getTrainListByStr(s.toString());
                if (listStart != null && listStart.size() > 0) {
                    for (BusStartCity bus : listStart) {
                        CityDisplayEntity city = new CityDisplayEntity();
                        city.setCity(bus.getName());
                        city.setCityId(bus.getId() + "");
                        listShow.add(city);
                    }
                } else {
                    showCity(3);
                }
                break;
            case Constants.TRAINCITY:
                break;
            default:
                break;
        }
        innerHandler.sendEmptyMessage(2);
    }

    /**
     * 切换界面
     *
     * @param i 1，2，3  1正常 ，2 搜索数据 3， nokey
     */
    private void showCity(int i) {
        switch (i) {
            case 1:
                tvNoKey.setVisibility(View.GONE);
                llFirst.setVisibility(View.VISIBLE);
                lsSearchCity.setVisibility(View.GONE);
                break;
            case 2:
                tvNoKey.setVisibility(View.GONE);
                llFirst.setVisibility(View.GONE);
                lsSearchCity.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvNoKey.setVisibility(View.VISIBLE);
                llFirst.setVisibility(View.GONE);
                lsSearchCity.setVisibility(View.GONE);
                break;
        }
    }

    private static class InnerHandler extends Handler {
        WeakReference<CityActivity> activityWeakReference;

        public InnerHandler(CityActivity cityActivity) {
            this.activityWeakReference = new WeakReference<>(cityActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CityActivity cityActivity = activityWeakReference.get();
            if (cityActivity == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    cityActivity.startCityAdapter.notifyDataSetChanged();
                    //刷新 letter
                    MyLetterView.b = cityActivity.firstArray.toArray(new String[]{});
                    cityActivity.mlLetter.postInvalidate();
                    cityActivity.mLoadingPopWindow.dismissLoadingDialog();
                    break;
                case 2:
                    cityActivity.searchCity.replaceAll(cityActivity.listShow);
                    break;
                case 3:

                    break;
                case 4:

                    break;
                default:
                    break;
            }
        }
    }
}
