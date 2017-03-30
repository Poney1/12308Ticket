package com.wxws.myticket.train.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.interfaces.IDialog;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ThreadManager;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.dialog.FragmentDialog;
import com.wxws.myticket.common.widgets.view.MyLetterView;
import com.wxws.myticket.train.adapter.TrainSearchResultAdapter;
import com.wxws.myticket.train.adapter.TrainStartCityAdapter;
import com.wxws.myticket.train.contants.TrainContants;
import com.wxws.myticket.train.dao.TrainStartCityDaoHelper;
import com.wxws.myticket.train.entity.TrainRealCityModel;
import com.wxws.myticket.train.entity.TrainStartCityModel;
import com.wxws.myticket.train.rxentity.TrainCityEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * desc: 火车票城市选择
 * Date: 2016-08-01 14:20
 *
 * @auther: lixiangxiang
 */
public class TrainCityActivity extends BaseActivity implements BDLocationListener {


    private FrameLayout layoutLeft;
    private TextView tvCancel;
    private LinearLayout layoutStatusbar;
    private TextView locationCityOne;
    private RecyclerView rvTrainCity;
    private MyLetterView mlLetter;
    private EditText etSearch;
    private ImageButton imgBtnDel;
    private ImageButton imgLeft;
    private ListView lsSearchCity;
    private LocationClient mLocationClient;
    private TextView tvNoKey;
    private LinearLayout llFirst;

    private int newVersion = 1;
    private List<TrainRealCityModel> historyList;
    private List<TrainRealCityModel> hotList;
    private List<TrainRealCityModel> trainCityList;
    private List<TrainStartCityModel> trainSearchCity;//搜索结果
    public static String SELECTCITY = "start";
    private List<TrainStartCityModel> tempList;
    private InnerHandler handler;//handler 静态
    private TrainStartCityAdapter trainStartCityAdapter;
    private TrainSearchResultAdapter trainSearchResultAdapter;
    private int version;
    private ArrayList<String> firstArray;
    private String locationCity = "";//定位城市
    private String locationCode = "";//定位code
    private IDialog mDialog;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_train_city);

        initView();

        initData();

        bindData();

        bindClick();
    }

    private void initData() {
        mDialog = new FragmentDialog(this);
    }

    private void bindClick() {

        mlLetter.setOnTouchingLetterChangedListener(new MyLetterView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                ToastManager.getInstance().showToast(TrainCityActivity.this, s);
                if (getString(R.string.train_city_history).equals(s)) {
                    rvTrainCity.scrollToPosition(0);
                } else if (getString(R.string.train_city_hot).equals(s)) {
                    rvTrainCity.scrollToPosition(1);
                } else {
                    int position = alphaIndex(s) + 2;
                    rvTrainCity.getLayoutManager().scrollToPosition(position);
                }
            }
        });


        imgBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethod.closeInputMethod(TrainCityActivity.this);
                etSearch.setText("");
                shouldShowWhatView(0);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (View.VISIBLE == mLlLayoutLoading.getVisibility()) {
                    return;
                }
                if (!StringUtils.isNullOrEmpty(s.toString())) {
                    imgBtnDel.setVisibility(View.VISIBLE);
                    trainSearchCity = TrainStartCityDaoHelper.getInstance().getTrainListByStr(s.toString());
                    if (trainSearchCity != null && trainSearchCity.size() > 0) {
                        trainSearchResultAdapter.replaceAll(trainSearchCity);
                        shouldShowWhatView(1);
                    } else {
                        shouldShowWhatView(2);
                        tvNoKey.setText(String.format("未找到与%s相关的城市", etSearch.getText()));
                    }
                } else {
                    imgBtnDel.setVisibility(View.GONE);
                    shouldShowWhatView(2);
                    tvNoKey.setText("请输入关键字");
                }
            }
        });
        layoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lsSearchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (trainSearchCity != null && trainCityList.size() > 0) {
                    TrainRealCityModel trainRealCityModel = new TrainRealCityModel();
                    trainRealCityModel.setCity(trainSearchCity.get(position).getName());
                    trainRealCityModel.setCode(trainSearchCity.get(position).getCode());
                    ListPreferencesSave.getInstance().saveTrainCityHistory(TrainCityActivity.this, trainRealCityModel);
                    RxBus.getInstance().post(new TrainCityEvent(TrainCityActivity.SELECTCITY, trainSearchCity.get(position).getName(), trainSearchCity.get(position).getCode()));
                    InputMethod.closeInputMethod(TrainCityActivity.this);
                    TrainCityActivity.this.finish();
                }
            }
        });

        locationCityOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isNullOrEmpty(locationCity) && !StringUtils.isNullOrEmpty(locationCode)) {
                    locationCity = locationCity.substring(0, locationCity.length() - 1);
                    RxBus.getInstance().post(new TrainCityEvent(TrainCityActivity.SELECTCITY, locationCity, locationCode));
                    TrainRealCityModel trainRealCityModel = new TrainRealCityModel();
                    trainRealCityModel.setCity(locationCity);
                    trainRealCityModel.setCode(locationCode);
                    trainRealCityModel.setGroup(false);
                    ListPreferencesSave.getInstance().saveTrainCityHistory(TrainCityActivity.this, trainRealCityModel);
                    finish();
                }
            }
        });
    }

    private void bindData() {

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(mLocationClient);
        mLocationClient.start();

        handler = new InnerHandler(TrainCityActivity.this);
        SELECTCITY = getIntent().getStringExtra("city");

        historyList = (List<TrainRealCityModel>) PreferencesUtils.readDataObject(TrainCityActivity.this, TrainContants.TRAINCITYHISTORY);
        //初始化 集合
        if (historyList == null) {
            historyList = new ArrayList<>();
        }
        hotList = new ArrayList<>();
        trainCityList = new ArrayList<>();
        trainStartCityAdapter = new TrainStartCityAdapter(TrainCityActivity.this, historyList, hotList, trainCityList);
        rvTrainCity.setLayoutManager(new LinearLayoutManager(this));
        rvTrainCity.setAdapter(trainStartCityAdapter);

        getStationData();
        shouldShowWhatView(0);
        trainSearchCity = new ArrayList<>();//搜索结果
        trainSearchResultAdapter = new TrainSearchResultAdapter(TrainCityActivity.this, trainSearchCity);
        lsSearchCity.setAdapter(trainSearchResultAdapter);

        etSearch.setHint(getIntent().getStringExtra("hint"));
    }

    private void initView() {
        bindLoadingView();
        imgLeft = (ImageButton) findViewById(R.id.imgLeft);
        layoutLeft = (FrameLayout) findViewById(R.id.layout_left);
        etSearch = (EditText) findViewById(R.id.et_search);
        imgBtnDel = (ImageButton) findViewById(R.id.imgBtnDel);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        tvNoKey = (TextView) findViewById(R.id.tv_no_key);//no key
        llFirst = (LinearLayout) findViewById(R.id.ll_first);
        lsSearchCity = (ListView) findViewById(R.id.ls_search_city);

        layoutStatusbar = (LinearLayout) findViewById(R.id.layout_statusbar);
        locationCityOne = (TextView) findViewById(R.id.location_city_one);
        rvTrainCity = (RecyclerView) findViewById(R.id.rv_train_city);
        mlLetter = (MyLetterView) findViewById(R.id.ml_letter);

    }

    //获取城市数据
    private void getStationData() {
        version = PreferencesUtils.getInt(TrainCityActivity.this, PreferencesUtils.PREFERENCE_NAME_DATA, TrainContants.TRAINCITYVERSION);

        HttpRequestManager.getInstance().trainStationList(String.valueOf(version), new ApiSubscriber<JsonResult<List<TrainStartCityModel>>>(this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<TrainStartCityModel>> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())) {
                    tempList = listJsonResult.getObject();
                    if (tempList != null) {
                        if (tempList.size() > 0) {
                            ThreadManager.getNormalPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (tempList.size() > 2500) {
                                        TrainStartCityDaoHelper.getInstance().addCity(tempList);
                                        version = 1;
                                    } else {
                                        int newVersion = 0;
                                        for (TrainStartCityModel startCity : tempList) {
                                            newVersion = startCity.getVersion();
                                            if (newVersion > version) {
                                                version = newVersion;
                                            }
                                            TrainStartCityDaoHelper.getInstance().addSingleCity(startCity);
                                        }
                                    }
                                    getAllList(true);
                                    PreferencesUtils.putDataInt(TrainCityActivity.this, TrainContants.TRAINCITYVERSION, newVersion);
                                }
                            });
                        }
                    } else {
                        getAllList(true);
                    }
                } else {
                    getAllList(true);
                }
            }
        });
    }

    //获取所有的集合数据
    private void getAllList(boolean isLocal) {
        //拿到c出发城市
        tempList = TrainStartCityDaoHelper.getInstance().getStartCityObject();
        firstArray = new ArrayList<>();
        if (isLocal) {
            firstArray = TrainStartCityDaoHelper.getInstance().getFirstChar();
        } else {
            for (TrainStartCityModel fChar : tempList) {
                String firstChar = fChar.getFirstChar().toUpperCase();
                if (!firstArray.contains(firstChar)) {
                    firstArray.add(firstChar);
                }
            }
        }
        Collections.sort(firstArray);

        for (int i = 0; i < firstArray.size(); i++) {
            Iterator<TrainStartCityModel> iterator = tempList.iterator();
            if (!StringUtils.isNullOrEmpty(firstArray.get(i))) {
                TrainRealCityModel tHeader = new TrainRealCityModel();
                tHeader.setGroup(true);
                tHeader.setCity(firstArray.get(i).toUpperCase());
                trainCityList.add(tHeader);
                while (iterator.hasNext()) {
                    TrainStartCityModel trainStartCityModel = iterator.next();
                    if (firstArray.get(i).equals(trainStartCityModel.getFirstChar().toUpperCase())) {
                        TrainRealCityModel tCity = new TrainRealCityModel();
                        tCity.setGroup(false);
                        tCity.setCity(trainStartCityModel.getName());
                        tCity.setCode(trainStartCityModel.getCode());
                        trainCityList.add(tCity);
                    }
                }
            }
        }

        for (TrainStartCityModel trainStartCityModel : tempList) {
            if ("0".equals(trainStartCityModel.getIsHot())) {
                TrainRealCityModel hCity = new TrainRealCityModel();
                hCity.setCity(trainStartCityModel.getName());
                hCity.setCode(trainStartCityModel.getCode());
                hotList.add(hCity);
            }
        }
        firstArray.add(0, getString(R.string.train_city_hot));
        firstArray.add(0, getString(R.string.train_city_history));
        handler.sendEmptyMessage(0);
    }

    /**
     * 需要跳转的条目
     *
     * @param s
     * @return
     */
    private int alphaIndex(String s) {
        int i = 0;
        while (i < trainCityList.size()) {
            if (trainCityList.get(i).isGroup() && trainCityList.get(i).getCity().equals(s)) {
                break;
            }
            ++i;
        }
        return i;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        if (!StringUtils.isNullOrEmpty(bdLocation.getCity())) {
            locationCity = bdLocation.getCity();
            locationCode = TrainStartCityDaoHelper.getInstance().getCodeThroughCity(locationCity.substring(0, locationCity.length() - 1));
            locationCityOne.setText(String.format("当前定位: %s", locationCity));
        }
    }

    private static class InnerHandler extends Handler {

        WeakReference<TrainCityActivity> weakReference;

        public InnerHandler(TrainCityActivity trainCityActivity) {
            this.weakReference = new WeakReference<>(trainCityActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            TrainCityActivity trainCityActivity = weakReference.get();
            if (trainCityActivity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    trainCityActivity.trainStartCityAdapter.notifyDataSetChanged();
                    //刷新 letter
                    MyLetterView.b = trainCityActivity.firstArray.toArray(new String[]{});
                    trainCityActivity.mlLetter.postInvalidate();
                    if (trainCityActivity.mDialog != null) {
                        trainCityActivity.mDialog.dissmissDialog();
                    }
                    break;
            }
        }
    }

    /**
     * 应该展示那个view
     *
     * @param i 0 展示 默认所有数据， 1 展示搜索后的数据  2 没有搜索到结果的展示
     */
    private void shouldShowWhatView(int i) {
        switch (i) {
            case 0:
                llFirst.setVisibility(View.VISIBLE);
                locationCityOne.setVisibility(View.VISIBLE);
                lsSearchCity.setVisibility(View.GONE);
                tvNoKey.setVisibility(View.GONE);
                break;
            case 1:
                llFirst.setVisibility(View.GONE);
                locationCityOne.setVisibility(View.GONE);
                lsSearchCity.setVisibility(View.VISIBLE);
                tvNoKey.setVisibility(View.GONE);
                break;
            case 2:
                llFirst.setVisibility(View.GONE);
                locationCityOne.setVisibility(View.GONE);
                lsSearchCity.setVisibility(View.GONE);
                tvNoKey.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
