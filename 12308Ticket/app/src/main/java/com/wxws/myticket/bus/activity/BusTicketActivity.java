package com.wxws.myticket.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.AircraftListAdapter;
import com.wxws.myticket.bus.adapter.BusListAdapter;
import com.wxws.myticket.bus.adapter.CityLineListAdapter;
import com.wxws.myticket.bus.adapter.GangAoListAdapter;
import com.wxws.myticket.bus.entity.BusTraditionPopEntity;
import com.wxws.myticket.bus.entity.PriceSortEntity;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.bus.entity.TraditionTotalEntity;
import com.wxws.myticket.bus.popupwindow.TraditionBusScreenPop;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.EventThread;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.DateChooseView;
import com.wxws.myticket.common.widgets.view.MenuButton;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshBase;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * desc: 汽车票 车次列表
 * Date: 2016/10/24 17:29
 *
 * @auther: lixiangxiang
 */
public class BusTicketActivity extends BaseActivity {


    private PullToRefreshListView pullToRefreshListView;
    private LinearLayout llBottom;
    private TextView tvBusLine;
    private TextView tvBusLineCount;
    private ImageView imgLeft;
    private DateChooseView dateChooseView;
    private MenuButton menuButton[];//底部四个按钮
    private boolean menuCheck[];//底部 按钮点击
    private TextView tvMap;//地图

    private boolean isLoadIntroduce = false;//推荐位是否加载

    private List<TraditionBusListEntity> busList;//数据源
    private List<TraditionBusListEntity> sortList;//排序后的list,筛选后的数据

    private BusListAdapter busListAdapter;//传统汽车票adapter
    private CityLineListAdapter cityLineListAdapter;//城际快线adapter
    private AircraftListAdapter aircraft; //机场快线 adapter
    private GangAoListAdapter gangAoListAdapter;//港澳快线

    private TraditionBusListEntity traditionBusListEntity;//推荐位

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    pullToRefreshListView.onRefreshComplete();
                    break;
            }
            return false;
        }
    });
    private TraditionBusScreenPop traditionBusScreenPop;
    //popwindow  展示数据 ，包括价格 ，时间段，上下车点
    private List<BusTraditionPopEntity> sortPrice;
    private List<BusTraditionPopEntity> timeSection;
    private List<BusTraditionPopEntity> upStation;
    private List<BusTraditionPopEntity> downStation;

    private String upDot, downDot, timeSectionStart, timeSectionEnd;
    private boolean isAll = true;//是否选择全部车票
    private int sortCondition;//筛选条件

    /**
     * 不同业务列表
     */
    public static String CITYTRADITIONALTAG = "busTraditionalTag";//传统
    public static String CITYLINETAG = "busDirectTag";//城际快线
    public static String CITYGANGAOTAG = "CityGangAoTag";//港澳
    public static String CITYAIRCRAFTTAG = "CityAircraftTag";//机场

    private RelativeLayout rlBus;
    private String tag;//用来区分 来自于那个搜索
    //港澳专线专属
    private String fromStationName, fromStationId, toStationName, toStationId;
    //城际快线专属
    private String fromLongitude, fromLatitude, toLongitude, toLatitude;
    //机场快线专属
    private String airId, airName;
    //传统汽车票专属
    private String departDate, fromCityName, fromCityId, toCityName, toCityId, bizType = "";
    //页码以及count
    private int pageNum = 1;
    private int pageCount = 100;
    private boolean isClearData = false;//是否清除数据
    private String cityName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_ticket);

        RxBus.getInstance().register(BusTicketActivity.this);

        getPreData();

        initView();

        initData();
        //获取推荐广告位
        getIntroduce();

        initBizTypeData(ApiSubscriber.NETVIEWLOADING);

        bindClick();
    }

    private void initBizTypeData(int state) {

        if (CITYTRADITIONALTAG.equals(tag)) {
            getBusList(state);
        } else if (CITYLINETAG.equals(tag)) {
            getCityLineList(state);
        } else if (CITYGANGAOTAG.equals(tag)) {
            getGangaoList(state);
        } else if (CITYAIRCRAFTTAG.equals(tag)) {
            getAirCraft(state);
        }
    }

    private void bindClick() {

        pullToRefreshListView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                    refreshView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pullUpToRefresh));
                    refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.releaseToRefresh));
                    refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.pullLoading));
                }
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isClearData = true;
                initBizTypeData(ApiSubscriber.NETPROGRESSLOADING);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (!(sortList != null && sortList.size() >= 0))
                    return;

                if (sortList.get(position - 1).isIntroduce()) {

                } else {
                    if (ModuleConstants.bizTypeBus.equals(bizType)) {
                        intent = new Intent(BusTicketActivity.this, BusFillInOrderActivity.class);
                        intent.putExtra("singleBus", sortList.get(position - 1));
                        intent.putExtra("bizType", ModuleConstants.bizTypeBus);
                        intent.putExtra("departDate",departDate);
                        BusTicketActivity.this.startActivityWithAnim(intent);
                    } else {
                        intent = new Intent(BusTicketActivity.this, DirectSelectStationActivity.class);
                        intent.putExtra("singleBus", sortList.get(position - 1));
                        intent.putExtra("departDate", departDate);
                        intent.putExtra("bizType", bizType);
                        BusTicketActivity.this.startActivityWithAnim(intent);
                    }
                }
            }
        });

        //设置底部按钮的点击事件
        for (int i = 0; i < 4; i++) {
            menuButton[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.menu_shaixuan:
                setMenuCheck(R.id.menu_shaixuan);
                showPopupWindow(0);
                traditionBusScreenPop.setUpAndDown(upStation, downStation);
                break;
            case R.id.menu_time:
                setMenuCheck(R.id.menu_time);
                showPopupWindow(1);
                traditionBusScreenPop.setTimeSection(timeSection);
                break;
            case R.id.menu_price:
                setMenuCheck(R.id.menu_price);
                showPopupWindow(2);
                traditionBusScreenPop.setSortPrice(sortPrice);
                break;
            case R.id.menu_all:
                setMenuCheck(R.id.menu_all);
                if (traditionBusScreenPop != null && traditionBusScreenPop.isShowing()) {
                    traditionBusScreenPop.dismiss();
                }
                menuButton[3].setSelected(menuCheck[3]);
                isAll = !isAll;
                break;
            case R.id.tv_map:
                Intent intent = new Intent(BusTicketActivity.this, StationActivity.class);
                intent.putExtra("fromCityId", fromCityId);
                intent.putExtra("fromCityName", fromCityName);
                startActivityWithAnim(intent);
                break;
            case R.id.img_left:
                finish();
                break;
        }
    }

    private void initData() {

        upStation = new ArrayList<>();
        downStation = new ArrayList<>();
        sortPrice = initSortPrice();
        timeSection = initTimeSection();
        initAdapter();
        //显示框 初始化
        traditionBusScreenPop = new TraditionBusScreenPop(BusTicketActivity.this, new TraditionBusScreenPop.OnFiltersDataListener() {
            @Override
            public void setOnFiltersDataListener(String upStation, String downStation, String timeSectionStart, String timeSectionEnd, int sortPrice) {
                upDot = upStation;
                downDot = downStation;
                BusTicketActivity.this.timeSectionStart = timeSectionStart;
                BusTicketActivity.this.timeSectionEnd = timeSectionEnd;
                sortCondition = sortPrice;
                getFiltersData(upDot, downDot, timeSectionStart, timeSectionEnd, sortCondition, isAll);
            }
        });

        traditionBusScreenPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //// TODO: 2016/11/9
                for (int i = 0; i < 3; i++) {
                    menuCheck[i] = false;
                    menuButton[i].setSelected(menuCheck[i]);
                }
            }
        });
        dateChooseView.setMiddleDate(departDate);
        dateChooseView.setDateChooseViewClickListener(new DateChooseView.DateChooseViewClickListener() {
            @Override
            public void preClick(String date) {
                departDate = date;
                isClearData = true;
                initBizTypeData(ApiSubscriber.NETPROGRESSLOADING);
            }

            @Override
            public void dateClick(String year, String month, String day) {
                isClearData = true;
                Intent intent = new Intent(BusTicketActivity.this, SelectDateActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("tag", tag);
                BusTicketActivity.this.startActivity(intent);
            }

            @Override
            public void nextClick(String date) {
                isClearData = true;
                departDate = date;
                initBizTypeData(ApiSubscriber.NETPROGRESSLOADING);

            }
        });

    }

    private void initBusTitleBar() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        tvBusLine = (TextView) findViewById(R.id.tv_bus_line);
        tvBusLineCount = (TextView) findViewById(R.id.tv_bus_line_count);
        rlBus = (RelativeLayout) findViewById(R.id.rl_bus);
        tvMap = (TextView) findViewById(R.id.tv_map);

        tvBusLine.setText(String.format(getString(R.string.bus_start_end),fromCityName,toCityName));
        tvMap.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
    }

    private void initView() {

        if (CITYTRADITIONALTAG.equals(tag)) {
            initBusTitleBar();
            rlBus.setVisibility(View.VISIBLE);
        } else if (CITYLINETAG.equals(tag)) {
            bindTitleViews();
            llTitleBar.setVisibility(View.VISIBLE);
            barTitle.setText(getString(R.string.bus_check_result));
        } else if (CITYAIRCRAFTTAG.equals(tag)) {
            bindTitleViews();
            llTitleBar.setVisibility(View.VISIBLE);
            barTitle.setText(getString(R.string.bus_direct_aircraft));
        }else if (CITYGANGAOTAG.equals(tag)){
            bindTitleViews();
            llTitleBar.setVisibility(View.VISIBLE);
            barTitle.setText(getString(R.string.bus_direct_gangao));
        }
        bindLoadingView();
        dateChooseView = (DateChooseView) findViewById(R.id.view_date_bus);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_ticket);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);


        MenuButton menuAll = (MenuButton) findViewById(R.id.menu_all);
        MenuButton menuPrice = (MenuButton) findViewById(R.id.menu_price);
        MenuButton menuTime = (MenuButton) findViewById(R.id.menu_time);
        MenuButton menuShaixuan = (MenuButton) findViewById(R.id.menu_shaixuan);
        menuButton = new MenuButton[4];
        menuCheck = new boolean[4];
        menuButton[0] = menuShaixuan;
        menuButton[1] = menuTime;
        menuButton[2] = menuPrice;
        menuButton[3] = menuAll;
    }

    private void getPreData() {

        tag = getIntent().getStringExtra("cityTag");
        if (CITYTRADITIONALTAG.equals(tag)) {
            getTraditionalData();
        } else if (CITYLINETAG.equals(tag)) {
            getCityData();
        } else if (CITYGANGAOTAG.equals(tag)) {
            getGangAoData();
        } else if (CITYAIRCRAFTTAG.equals(tag)) {
            getAirCraftData();
        }
    }

    private void getAirCraftData() {
        airId = getIntent().getStringExtra("airId");
        airName = getIntent().getStringExtra("airName");
        cityName =  getIntent().getStringExtra("cityName");

        departDate = getIntent().getStringExtra("departDate");
        bizType = getIntent().getStringExtra("bizType");
    }

    private void getGangAoData() {

        fromStationName = getIntent().getStringExtra("fromStationName");
        fromStationId = getIntent().getStringExtra("fromStationId");
        toStationName = getIntent().getStringExtra("toStationName");
        toStationId = getIntent().getStringExtra("toStationId");

        fromCityName = getIntent().getStringExtra("startCity");
        fromCityId = getIntent().getStringExtra("startId");
        toCityId = getIntent().getStringExtra("endId");

        bizType = getIntent().getStringExtra("bizType");
        departDate = getIntent().getStringExtra("departDate");
    }

    //获取传统
    private void getTraditionalData() {

        fromCityName = getIntent().getStringExtra("startCity");
        fromCityId = getIntent().getStringExtra("startId");
        toCityName = getIntent().getStringExtra("endCity");
        toCityId = getIntent().getStringExtra("endId");

        bizType = getIntent().getStringExtra("bizType");
        departDate = getIntent().getStringExtra("departDate");
    }

    //获取城际
    private void getCityData() {
        departDate = getIntent().getStringExtra("departDate");
        bizType = getIntent().getStringExtra("bizType");

        fromLongitude = getIntent().getDoubleExtra("fromLongitude",0)+"";
        fromLatitude = getIntent().getDoubleExtra("fromLatitude",0)+"";
        toLongitude = getIntent().getDoubleExtra("toLongitude",0)+"";
        toLatitude = getIntent().getDoubleExtra("toLatitude",0)+"";

    }


    private void initAdapter() {

        busList = new ArrayList<>();
        sortList = new ArrayList<>();

        if (tag.equals(CITYTRADITIONALTAG)) {
            busListAdapter = new BusListAdapter(BusTicketActivity.this, sortList);
            pullToRefreshListView.setAdapter(busListAdapter);
        } else if (tag.equals(CITYLINETAG)) {
            cityLineListAdapter = new CityLineListAdapter(BusTicketActivity.this, sortList);
            pullToRefreshListView.setAdapter(cityLineListAdapter);
        } else if (tag.equals(CITYAIRCRAFTTAG)) {
            aircraft = new AircraftListAdapter(BusTicketActivity.this, sortList);
            pullToRefreshListView.setAdapter(aircraft);
        } else if (tag.equals(CITYGANGAOTAG)) {
            gangAoListAdapter = new GangAoListAdapter(BusTicketActivity.this, sortList);
            pullToRefreshListView.setAdapter(gangAoListAdapter);
        }

    }

    /**
     * 推荐位
     */
    private void getIntroduce() {
        HttpRequestManager.getInstance().recommend(bizType,fromCityId,fromCityName,toCityId,toCityName,departDate, new ApiSubscriber<JsonResult<TraditionBusListEntity>>(BusTicketActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<TraditionBusListEntity> traditionJsonResult) {
                if ("0000".equals(traditionJsonResult.getResultCode()) && traditionJsonResult.getObject() != null) {
                    traditionBusListEntity = traditionJsonResult.getObject();
                    traditionBusListEntity.setIntroduce(true);
                    sortList.add(0, traditionBusListEntity);
                    isLoadIntroduce = true;
                    cityLineListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取  传统汽车票列表
     */
    private void getBusList(int state) {

        HttpRequestManager.getInstance().busLineList(fromCityId, fromCityName, toCityId, toCityName, departDate, bizType, pageNum, pageCount,
                new ApiSubscriber<JsonResult<TraditionTotalEntity>>(BusTicketActivity.this, state) {
                    @Override
                    public void onNext(JsonResult<TraditionTotalEntity> listJsonResult) {
                        handler.sendEmptyMessage(0);
                        if ("0000".equals(listJsonResult.getResultCode()) && listJsonResult.getObject() != null && listJsonResult.getObject().getRows()!=null) {
                            dealData(listJsonResult);
                            tvBusLineCount.setVisibility(View.VISIBLE);
                            tvBusLineCount.setText(String.format(getString(R.string.bus_title_time),listJsonResult.getObject().getTotal()+""));
                            busListAdapter.notifyDataSetChanged();
                            initUpAndDown();
                            //清洗数据
                            if (pageNum * pageCount < listJsonResult.getObject().getTotal()) {
                                getBusList(ApiSubscriber.NETNULL);
                            }
                        } else {
                            ToastManager.getInstance().showToast(BusTicketActivity.this, "无数据");
                        }
                    }
                });

    }

    //城际快线
    private void getCityLineList(int state) {

        HttpRequestManager.getInstance().busCityLineList(fromLongitude, fromLatitude, toLongitude, toLatitude, departDate, bizType, pageNum + "",
                pageCount + "", new ApiSubscriber<JsonResult<TraditionTotalEntity>>(BusTicketActivity.this, state) {
                    @Override
                    public void onNext(JsonResult<TraditionTotalEntity> listJsonResult) {
                        handler.sendEmptyMessage(0);
                        if ("0000".equals(listJsonResult.getResultCode()) && listJsonResult.getObject() != null) {
                            dealData(listJsonResult);
                            cityLineListAdapter.notifyDataSetChanged();
                            initUpAndDown();
                            //清洗数据
                            if (pageNum * pageCount < listJsonResult.getObject().getTotal()) {
                                getCityLineList(ApiSubscriber.NETNULL);
                            }
                        } else {
                            ToastManager.getInstance().showToast(BusTicketActivity.this, "无数据");
                        }
                    }
                });
    }

    //港澳快线
    private void getGangaoList(int state) {
        HttpRequestManager.getInstance().busLineGangaoList(fromCityId, fromCityName, fromStationId, fromStationName, toCityId,
                toCityName, toStationId, toStationName, departDate, bizType, pageNum + "", pageCount + "", new ApiSubscriber<JsonResult<TraditionTotalEntity>>(BusTicketActivity.this, state) {
                    @Override
                    public void onNext(JsonResult<TraditionTotalEntity> listJsonResult) {
                        handler.sendEmptyMessage(0);
                        if ("0000".equals(listJsonResult.getResultCode()) && listJsonResult.getObject() != null) {
                            dealData(listJsonResult);
                            gangAoListAdapter.notifyDataSetChanged();
                            initUpAndDown();
                            //清洗数据
                            if (pageNum * pageCount < listJsonResult.getObject().getTotal()) {
                                getGangaoList(ApiSubscriber.NETNULL);
                            }
                        } else {
                            ToastManager.getInstance().showToast(BusTicketActivity.this, "无数据");
                        }
                    }
                });
    }

    //机场快线
    private void getAirCraft(int state) {
        HttpRequestManager.getInstance().busAircraftList(airId, airName, departDate, bizType, pageNum + "", pageCount + "", new ApiSubscriber<JsonResult<TraditionTotalEntity>>(BusTicketActivity.this, state) {
            @Override
            public void onNext(JsonResult<TraditionTotalEntity> listJsonResult) {
                handler.sendEmptyMessage(0);
                if ("0000".equals(listJsonResult.getResultCode()) && listJsonResult.getObject() != null) {
                    dealData(listJsonResult);
                    aircraft.notifyDataSetChanged();
                    initUpAndDown();
                    //清洗数据
                    if (pageNum * pageCount < listJsonResult.getObject().getTotal()) {
                        getAirCraft(ApiSubscriber.NETNULL);
                    }
                } else {
                    ToastManager.getInstance().showToast(BusTicketActivity.this, "无数据");
                }
            }
        });
    }

    //处理数据
    private void dealData(JsonResult<TraditionTotalEntity> object) {
        if (isClearData) {
            busList.clear();
            sortList.clear();
            if (isLoadIntroduce)
                sortList.add(traditionBusListEntity);
        }
        isClearData = false;
        busList.addAll(object.getObject().getRows());
        sortList.addAll(object.getObject().getRows());
    }

    /**
     * @param upStation        上车点
     * @param downStation      下车点
     * @param timeSectionStart 初始时间
     * @param timeSectionEnd   时间末
     * @param sortPrice        价格排序
     * @param isAll            是否全部车票
     * @return List<TraditionBusListEntity>
     */
    public List<TraditionBusListEntity> getFiltersData(String upStation, String downStation, String timeSectionStart, String timeSectionEnd, int sortPrice, boolean isAll) {

        sortList.clear();
        if (busList.size() == 0) {
            return null;
        }
        List<TraditionBusListEntity> listUp = new ArrayList<>();
        if (getString(R.string.bus_list_pop_unlimited).equals(upStation)) {
            listUp = busList;
        } else {
            for (TraditionBusListEntity tradition : busList) {
                if (tradition.getFromStationName().equals(upDot)) {
                    listUp.add(tradition);
                }
            }
        }
        if (listUp.size() == 0) {
            return null;
        }
        List<TraditionBusListEntity> listDown = new ArrayList<>();
        if (getString(R.string.bus_list_pop_unlimited).equals(downStation)) {
            listDown = listUp;
        } else {
            for (TraditionBusListEntity tradition : listUp) {
                if (tradition.getFromStationName().equals(upDot)) {
                    listDown.add(tradition);
                }
            }
        }
        if (listDown.size() == 0) {
            return null;
        }
        int timeStart = timeCalc(timeSectionStart);
        int timeEnd = timeCalc(timeSectionEnd);


        List<TraditionBusListEntity> listTime = new ArrayList<>();
        if (timeStart == 0 && timeEnd == (23 * 60 + 59)) {
            listTime = listDown;
        } else {
            for (TraditionBusListEntity tradition : listDown) {
                int busStart = timeCalc(tradition.getStartTime());
                if (busStart >= timeStart && busStart <= timeEnd) {
                    listTime.add(tradition);
                }
            }
        }
        if (listTime.size() == 0) {
            return null;
        }
        List<TraditionBusListEntity> listAll = new ArrayList<>();
        if (isAll) {
            listAll = listTime;
        } else {
            for (TraditionBusListEntity tradition : listTime) {
                if ("1".equals(tradition.getStatus())) {
                    listAll.add(tradition);
                }
            }
        }
        if (listAll.size() == 0) {
            return null;
        }

        if (sortPrice == 1) {
            Collections.sort(listAll, new PriceSortEntity());
        } else if (sortPrice == 2) {
            Collections.sort(listAll);
        }

        sortList.add(0, traditionBusListEntity);
        sortList.addAll(listAll);

        return sortList;
    }

    /**
     * 时间计算
     *
     * @param time
     * @return
     */
    private int timeCalc(@NonNull String time) {
        String[] timeStart = time.split(":");
        int startTime = Integer.parseInt(timeStart[0]) * 60 + Integer.parseInt(timeStart[1]);

        return startTime;
    }

    /**
     * 设置menu 状态
     *
     * @param id
     */
    private void setMenuCheck(int id) {
        for (int i = 0; i < 4; i++) {
            if (menuButton[i].getId() == id) {
                if (menuCheck[i]) {
                    menuCheck[i] = false;
                } else {
                    menuCheck[i] = true;
                }
            } else {
                menuCheck[i] = false;
            }
        }
    }


    /**
     * 控制展示 window
     *
     * @param i
     */
    private void showPopupWindow(int i) {

        for (int j = 0; j < 4; j++) {
            menuButton[j].setSelected(menuCheck[j]);
        }

        if (menuCheck[i] && traditionBusScreenPop != null) {
            traditionBusScreenPop.showPop(findViewById(R.id.ll_bottom));
        } else {
            if (traditionBusScreenPop != null && traditionBusScreenPop.isShowing()) {
                traditionBusScreenPop.dismiss();
            }
        }
    }


    /**
     * 初始化价格排序数据
     *
     * @return
     */
    private List<BusTraditionPopEntity> initSortPrice() {

        BusTraditionPopEntity busTraditionPopEntity1 = new BusTraditionPopEntity(0, true, getString(R.string.bus_list_pop_unlimited), "", "", 0);
        BusTraditionPopEntity busTraditionPopEntity2 = new BusTraditionPopEntity(0, false, getString(R.string.bus_list_pop_low), "", "", 1);
        BusTraditionPopEntity busTraditionPopEntity3 = new BusTraditionPopEntity(0, false, getString(R.string.bus_list_pop_improve), "", "", 2);

        List<BusTraditionPopEntity> sortPrice = new ArrayList<>();
        sortPrice.add(busTraditionPopEntity1);
        sortPrice.add(busTraditionPopEntity2);
        sortPrice.add(busTraditionPopEntity3);

        return sortPrice;
    }

    /**
     * 时间段
     *
     * @return
     */
    private List<BusTraditionPopEntity> initTimeSection() {
        BusTraditionPopEntity busTraditionPopEntity1 = new BusTraditionPopEntity(0, true, getString(R.string.bus_list_pop_unlimited), "00:00", "23:59", 0);
        BusTraditionPopEntity busTraditionPopEntity2 = new BusTraditionPopEntity(0, false, getString(R.string.bus_list_pop_forenoon), "06:00", "11:59", 0);
        BusTraditionPopEntity busTraditionPopEntity3 = new BusTraditionPopEntity(0, false, getString(R.string.bus_list_pop_afternoon), "12:00", "17:59", 0);
        BusTraditionPopEntity busTraditionPopEntity4 = new BusTraditionPopEntity(0, false, getString(R.string.bus_list_pop_night), "18:00", "23:59", 0);
        BusTraditionPopEntity busTraditionPopEntity5 = new BusTraditionPopEntity(0, false, getString(R.string.bus_list_pop_noon), "00:00", "09:59", 0);

        List<BusTraditionPopEntity> timeSection = new ArrayList<>();

        timeSection.add(busTraditionPopEntity1);
        timeSection.add(busTraditionPopEntity2);
        timeSection.add(busTraditionPopEntity3);
        timeSection.add(busTraditionPopEntity4);
        timeSection.add(busTraditionPopEntity5);

        return timeSection;
    }

    /**
     * 筛选上下车点
     */
    private void initUpAndDown() {
        upStation.clear();
        downStation.clear();
        Set<BusTraditionPopEntity> upSet = new HashSet<>();
        Set<BusTraditionPopEntity> downSet = new HashSet<>();
        for (int i = 0; i < sortList.size(); i++) {
            TraditionBusListEntity tl = sortList.get(i);
            if (StringUtils.isNullOrEmpty(tl.getFromStationName()))
                continue;
            BusTraditionPopEntity up = new BusTraditionPopEntity(BusTraditionPopEntity.upBusType, false, tl.getFromStationName(), "", "", 0);
            BusTraditionPopEntity down = new BusTraditionPopEntity(BusTraditionPopEntity.downBusType, false, tl.getToStationName(), "", "", 0);
            upSet.add(up);
            downSet.add(down);
        }

        Iterator<BusTraditionPopEntity> iteratorUp = upSet.iterator();
        while (iteratorUp.hasNext()) {
            upStation.add(iteratorUp.next());
        }
        BusTraditionPopEntity upFirst = new BusTraditionPopEntity(BusTraditionPopEntity.upBusType, true, getString(R.string.bus_list_pop_unlimited), "", "", 0);
        upStation.add(0, upFirst);

        Iterator<BusTraditionPopEntity> iteratorDown = downSet.iterator();
        while (iteratorDown.hasNext()) {
            downStation.add(iteratorDown.next());
        }
        BusTraditionPopEntity downFirst = new BusTraditionPopEntity(BusTraditionPopEntity.upBusType, true, getString(R.string.bus_list_pop_unlimited), "", "", 0);
        downStation.add(0, downFirst);
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onRecievedDate(SelectDayEvent event) {
        if (SelectDateActivity.BUSTRADITIONALTAG.equals(event.getTag())) {
            StringBuilder startDate = new StringBuilder();
            startDate.append(event.getYear()).append("-").append(event.getMonth()).append("-").append(event.getDay());
            dateChooseView.setMiddleDate(startDate.toString());
            //// TODO: 2016/11/9
            departDate = startDate.toString();
            initBizTypeData(ApiSubscriber.NETPROGRESSLOADING);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(BusTicketActivity.this);
    }
}
