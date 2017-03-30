package com.wxws.myticket.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.activity.SelectDateActivity;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.interfaces.BusUpOrDownCallback;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshBase;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshListView;
import com.wxws.myticket.train.adapter.TrainListAdapter;
import com.wxws.myticket.train.adapter.TrainListDescriptionAdapter;
import com.wxws.myticket.train.entity.PopTimeSelectModel;
import com.wxws.myticket.train.entity.SeatListEntity;
import com.wxws.myticket.train.entity.SeatSelectModel;
import com.wxws.myticket.train.entity.SelectBusEntity;
import com.wxws.myticket.train.entity.TrainArriveTimeModel;
import com.wxws.myticket.train.entity.TrainListModel;
import com.wxws.myticket.train.entity.TrainPriceSortModel;
import com.wxws.myticket.train.entity.TrainShortTimeModel;
import com.wxws.myticket.train.entity.TrainStationInfoModel;
import com.wxws.myticket.train.entity.TrainStratTimeModel;
import com.wxws.myticket.train.fragment.TrainSearchFragment;
import com.wxws.myticket.train.popwindow.StationPopWindow;
import com.wxws.myticket.train.popwindow.TrainTypeWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * desc: 火车票 列表
 * Date: 2016/10/9 18:11
 *
 * @auther: lixiangxiang
 */
public class TrainListActivity extends BaseActivity {

    private ImageView imgChange;
    private ImageView imgLeft;
    private TextView tvStartStation;
    private TextView tvEnd;
    private PullToRefreshListView linePull;
    private LinearLayout layoutSort;
    private LinearLayout layoutSelect;
    private LinearLayout layoutOnlyTicket;
    private RelativeLayout layoutTicketPrice;
    private TrainTypeWindow mTypePopWindow;
    private StationPopWindow mPopWindow;
    private TextView mPrevDay;
    private TextView tvCurDay;
    private TextView tvNextDay;//日期控制
    private TextView tvHaveTicket;
    private TextView tvShuanxuan;
    private TextView tvPrice;
    public TextView[] textViews;//点击那个 底部按钮
    private PopupWindow[] popupWindow;
    private TextView tvSort;

    private List<PopTimeSelectModel> listType;//车型
    private List<SeatSelectModel> listSeat;//座位
    private List<PopTimeSelectModel> listStation;//上车点
    private List<PopTimeSelectModel> listDown;//下车点
    private List<PopTimeSelectModel> listStartTime;//出发时间
    private List<PopTimeSelectModel> listEndTime;//到达时间
    private TrainListAdapter tAdapter;//车次列表
    private String startDate;//出发时间

    //搜索线路数据
    private String startCity;
    private String endCity;
    private List<TrainListModel> list;//列表数据
    private List<TrainListModel> listShow;//显示的数据
    private int position = 0;//排序选择

    private List<TrainStationInfoModel.DataBean> dataBeanList;

    //第三个筛选条件
    private List<String> trainType1 = new ArrayList<>();
    private List<String> trainSeat1 = new ArrayList<>();
    private List<String> trainStart1 = new ArrayList<>();
    private List<String> trainEndStation1 = new ArrayList<>();
    private List<String> trainStartTime1 = new ArrayList<>();
    private List<String> trainArriveTime1 = new ArrayList<>();

    //默认显示余票
    private boolean isHaveTicket = true;
    private boolean isOnlyTicket = false;//默认看全部
    private String startCode;
    private String endCode;
    private boolean isOnlySpeed;
    private boolean isFirstEnter;//第一次进入

    // 是否是改签订单，默认不是改签
    private boolean flag_isendorse = false;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            linePull.onRefreshComplete();
            return false;
        }
    });
    private List<TrainListModel> listShowShaixuan;
    private String orderid;
    private Bundle ticketlistBundle;
    private String id;
    private TextView tv_num;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_train_list);
        RxBus.getInstance().register(this);

        getIntentData();

        initView();

        bindData();

        bindClick();
    }

    //获取数据
    private void getIntentData() {
        list = new ArrayList<>();
        listShow = new ArrayList<>();
        startCity = getIntent().getStringExtra("startCity");
        endCity = getIntent().getStringExtra("endCity");
        startDate = getIntent().getStringExtra("startTime");
        startCode = getIntent().getStringExtra("startCode");
        endCode = getIntent().getStringExtra("endCode");
        isOnlySpeed = getIntent().getBooleanExtra("isOnlySpeed", false);
        id = getIntent().getStringExtra("id");

        flag_isendorse = getIntent().getBooleanExtra("isendorse", false);// 是否是改签票
        //改签的id
        orderid = getIntent().getStringExtra("orderid");
        //改签人信息list的bundle
        ticketlistBundle = getIntent().getBundleExtra("ticketbundle");
    }

    private void bindClick() {

        linePull.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                    refreshView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pullUpToRefresh));
                    refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.releaseToRefresh));
                    refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
                }
            }
        });
        linePull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        tAdapter.setOnShowStationInfo(new TrainListAdapter.OnShowStationInfo() {
            @Override
            public void ShowStationInfo(List<TrainStationInfoModel.DataBean> dataBeen2, TrainListDescriptionAdapter tDescription, String trainCode, String fromStationCode, String toStationCode, String trainNo, String startDate, int position) {
                loadStationData(dataBeen2, tDescription, trainCode, fromStationCode, toStationCode, trainNo,startDate, position);
            }
        });
        tAdapter.setDate(startDate);
        //排序
        layoutSort.setOnClickListener(this);
        //只看有票
        layoutOnlyTicket.setOnClickListener(this);
        //筛选
        layoutSelect.setOnClickListener(this);
        //余票
        layoutTicketPrice.setOnClickListener(this);

        tvNextDay.setOnClickListener(this);
        tvCurDay.setOnClickListener(this);
        mPrevDay.setOnClickListener(this);

        imgChange.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        HttpRequestManager.getInstance().trainTicketList(startDate, startCity, startCode, endCity,
                endCode, isOnlySpeed, new ApiSubscriber<JsonResult<List<TrainListModel>>>(TrainListActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<List<TrainListModel>> listJsonResult) {
                if ("0000".equals(listJsonResult.getResultCode())) {
                    if (listJsonResult.getObject() != null) {
                        list = listJsonResult.getObject();
                        listShow = list;
                        listShowShaixuan = list;
                        // TODO: 2016/8/8
                        List<TrainListModel> firstListShow = typeWindowShaixuan(listShow);
                        List<TrainListModel> onlyTicket;
                        if (!isFirstEnter) {
                            textViews[2].setSelected(isOnlyTicket);
                            textViews[2].setText(getString(R.string.train_list_all_ticket));
                            onlyTicket = firstListShow;
                            tAdapter.replaceAll(onlyTicket);
                            tAdapter.replaceCurrentData(onlyTicket);
                            isFirstEnter = true;
                        } else {
                            if (isOnlyTicket) {
                                onlyTicket = getOnlyTicketData(firstListShow);
                            } else {
                                onlyTicket = firstListShow;
                            }
                            tAdapter.replaceAll(onlyTicket);
                            tAdapter.replaceCurrentData(onlyTicket);
                        }
                        if (0 == onlyTicket.size()) {
                            ToastManager.getInstance().showToast(TrainListActivity.this, getString(R.string.query_train_fail));
                        }

                    } else {
                        showErrorOrNoData(getString(R.string.query_train_fail), R.string.click_return, R.mipmap.tip1);
                    }
                } else {
                    showErrorOrNoData(getString(R.string.query_train_fail), R.string.click_return, R.mipmap.tip1);
                }
                handler.sendEmptyMessage(0);
            }
        });

    }

    private void bindData() {
        dataBeanList = new ArrayList<>();
        initPopwindowData();
        List<SelectBusEntity> listSortFirst = initSortData();
        mPopWindow = new StationPopWindow(TrainListActivity.this, listSortFirst, new BusUpOrDownCallback() {
            @Override
            public void onScreenBus(List<SelectBusEntity> up, int position) {
                TrainListActivity.this.position = position;
                List<TrainListModel> listSort = typeWindowShaixuan(listShowShaixuan);
                tAdapter.replaceAll(listSort);
                tAdapter.replaceCurrentData(listSort);
                mPopWindow.dismiss();
            }
        });

        //统一处理popwindow
        popupWindow = new PopupWindow[2];
        popupWindow[0] = mPopWindow;
        popupWindow[1] = mTypePopWindow;
        tvStartStation.setText(startCity);
        tvEnd.setText(endCity);
        tvCurDay.setText(spiltTimeForDisplay(startDate));

        popupWindow[0].setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                textViews[0].setSelected(false);
            }
        });
        popupWindow[1].setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTypePopWindow.refreshWindow();
                if (isShaixuanPressed()){
                    textViews[1].setSelected(true);
                } else {
                    textViews[1].setSelected(false);
                }
            }
        });
        refreshData();
    }


    /**
     * 排序
     */
    private List<TrainListModel> initSort(List<TrainListModel> listModel) {
        List<TrainListModel> listSort  = new ArrayList<>() ;
        //排序
        switch (position) {
            case 0:
                listSort = sortStartCondition(listModel);
                break;
            case 1:
                listSort = sortArriveCondition(listModel);
                break;
            case 2:
                listSort = sortShortTimeCondition(listModel);
                break;
            case 3:
                listSort = sortPriceCondition(listModel);
                break;
        }

        return listSort;
    }

    //初始化排序数据
    private List<SelectBusEntity> initSortData() {
        List<SelectBusEntity> listSort = new ArrayList<>();
        SelectBusEntity selectBusEntity = new SelectBusEntity("", "出发从早到晚", "", 0, 1);
        SelectBusEntity selectBusEntity1 = new SelectBusEntity("", "到达从早到晚", "", 0, 0);
        SelectBusEntity selectBusEntity2 = new SelectBusEntity("", "历时最短", "", 0, 0);
        SelectBusEntity selectBusEntity3 = new SelectBusEntity("", "价格由低到高", "", 0, 0);
        listSort.add(selectBusEntity);
        listSort.add(selectBusEntity1);
        listSort.add(selectBusEntity2);
        listSort.add(selectBusEntity3);
        return listSort;
    }

    private void initView() {

        bindLoadingView();
        imgLeft = (ImageView) findViewById(R.id.imgLeft);
        imgChange = (ImageView) findViewById(R.id.img_change);
        tvStartStation = (TextView) findViewById(R.id.tv_start_station);
        tvEnd = (TextView) findViewById(R.id.tv_end);
        linePull = (PullToRefreshListView) findViewById(R.id.train_line_pull);
        linePull.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        mPrevDay = (TextView) findViewById(R.id.tvPrevDay);
        tvCurDay = (TextView) findViewById(R.id.tvCurDay);
        tvNextDay = (TextView) findViewById(R.id.tvNextDay);
        //底部的四个选择按钮
        tvSort = (TextView) findViewById(R.id.tv_sort);
        tvHaveTicket = (TextView) findViewById(R.id.tv_have_ticket);
        tvShuanxuan = (TextView) findViewById(R.id.tv_shuanxuan);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tv_num = (TextView) findViewById(R.id.tv_num);
        textViews = new TextView[4];
        textViews[0] = tvSort;
        textViews[1] = tvShuanxuan;
        textViews[2] = tvHaveTicket;
        textViews[3] = tvPrice;
        tv_num.setSelected(true);
        layoutSort = (LinearLayout) findViewById(R.id.layout_sort);
        layoutSelect = (LinearLayout) findViewById(R.id.layout_select);
        layoutOnlyTicket = (LinearLayout) findViewById(R.id.layout_only_ticket);
        layoutTicketPrice = (RelativeLayout) findViewById(R.id.layout_ticket_price);

        tAdapter = new TrainListAdapter(this);
        tAdapter.setBookType(flag_isendorse, orderid, ticketlistBundle, id);
        linePull.setAdapter(tAdapter);
    }

    //是否高亮
    private boolean isShaixuanPressed(){
        if (trainType1.size() >0 || trainSeat1.size()>0 || trainStart1.size() > 0 || trainEndStation1.size() >0 || trainStartTime1.size()> 0 || trainArriveTime1.size() > 0)
            return true;
        return false;
    }
    @Override
    public void onBackPressed() {
        if (mTypePopWindow.isShowing() || mPopWindow.isShowing()) {
            closePopWindow(mTypePopWindow);
            closePopWindow(mPopWindow);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    //初始化 筛选按钮
    private void initPopwindowData() {
        //初始化站点选择数据
        listType = new ArrayList<>();
        listSeat = new ArrayList<>();
        listStation = new ArrayList<>();//上车点
        listDown = new ArrayList<>();
        listStartTime = new ArrayList<>();
        listEndTime = new ArrayList<>();
        mTypePopWindow = new TrainTypeWindow(TrainListActivity.this);

        mTypePopWindow.setOnTypeParamListener(new TrainTypeWindow.OnTypeParameterSelect() {
            @Override
            public void setOnTypeParameterSelect(List<String> trainType, List<String> trainSeat, List<String> trainStart, List<String> trainEndStation, List<String> trainStartTime, List<String> trainArriveTime) {
                //第三个筛选条件
                trainType1 = trainType;
                trainSeat1 = trainSeat;
                trainStart1 = trainStart;
                trainEndStation1 = trainEndStation;
                trainStartTime1 = trainStartTime;
                trainArriveTime1 = trainArriveTime;
                listShow = typeWindowShaixuan(listShowShaixuan);
                if (listShow.size() == 0) {
                    ToastManager.getInstance().showToast(TrainListActivity.this, getString(R.string.train_list_no_data));
                }
                tAdapter.replaceAll(listShow);
                tAdapter.replaceCurrentData(listShow);
                if (isShaixuanPressed()){
                    textViews[1].setSelected(true);
                }else {
                    textViews[1].setSelected(false);
                }
            }
        });
    }

    //刷新 popwindow
    private void refreshPopupWindow() {

        listStartTime.clear();
        listEndTime.clear();
        listType.clear();

        listStartTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "00:00", "24:00"), false));
        listStartTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "00:00", "06:00"), false));
        listStartTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "06:00", "12:00"), false));
        listStartTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "12:00", "18:00"), false));
        listStartTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "18:00", "24:00"), false));

        listEndTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "00:00", "24:00"), false));
        listEndTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "00:00", "06:00"), false));
        listEndTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "06:00", "12:00"), false));
        listEndTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "12:00", "18:00"), false));
        listEndTime.add(new PopTimeSelectModel(String.format("%s ~  %s", "18:00", "24:00"), false));

        listType.add(new PopTimeSelectModel(getString(R.string.train_list_select_high), false));
        listType.add(new PopTimeSelectModel(getString(R.string.train_list_select_speed), false));
        listType.add(new PopTimeSelectModel(getString(R.string.train_list_select_common), false));
        listType.add(new PopTimeSelectModel(getString(R.string.train_list_select_else), false));

        Set<SeatSelectModel> setSeat = new HashSet<>();//筛选座次
        Set<String> seatUp = new HashSet<>();//筛选上车点
        Set<String> seatDown = new HashSet<>();//筛选下车点
        String hardName = getString(R.string.train_list_hard_type);
        int hardIndex = 0;
        String hardType = "";
        String softName = getString(R.string.train_list_type_soft);
        String softType = "";
        int softIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            seatUp.add(list.get(i).getFromStationName());
            seatDown.add(list.get(i).getToStationName());
            for (SeatListEntity seatType : list.get(i).getSeatList()) {
                if (StringUtils.isNullOrEmpty(seatType.getName())) {
                    continue;
                }
                if (seatType.getNum() >= 0) {
                    if (seatType.getName().contains(getString(R.string.train_list_hard_type))) {
                        hardIndex = seatType.getIndex();
                        hardType = seatType.getType();
                    } else if (seatType.getName().contains(getString(R.string.train_list_type_soft)) && !seatType.getName().equals(getString(R.string.train_list_high_type_soft))) {
                        softIndex = seatType.getIndex();
                        softType = seatType.getType();
                    } else {
                        SeatSelectModel seat = new SeatSelectModel(seatType.getIndex(), seatType.getName(), seatType.getType(), false);
                        setSeat.add(seat);
                    }
                }
            }
        }
        if (!StringUtils.isNullOrEmpty(hardType)) {
            SeatSelectModel seatHard = new SeatSelectModel(hardIndex, hardName, hardType, false);
            setSeat.add(seatHard);
        }
        if (!StringUtils.isNullOrEmpty(softType)) {
            SeatSelectModel seatSoft = new SeatSelectModel(softIndex, softName, softType, false);
            setSeat.add(seatSoft);
        }
        Iterator iterator = setSeat.iterator();
        while (iterator.hasNext()) {
            SeatSelectModel selectModel = (SeatSelectModel) iterator.next();
            listSeat.add(selectModel);
        }
        Collections.sort(listSeat);
        for (String upStr : seatUp) {
            listStation.add(new PopTimeSelectModel(upStr, false));
        }
        for (String downStr : seatDown) {
            listDown.add(new PopTimeSelectModel(downStr, false));
        }
        mTypePopWindow.setShaixuanData(listType, listSeat, listStation, listDown, listStartTime, listEndTime);
    }


    //关闭 window
    private void closePopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tvPrevDay:
                String date = DateTimeUtil.addDay(startDate, -1);
                if (DateTimeUtil.Lessthantoday(date)) {
                    ToastManager.getInstance().showToast(this, R.string.index_out_date);
                    return;
                }
                startDate = date;
                tvCurDay.setText(spiltTimeForDisplay(startDate));
                sendEvent(startDate);
                break;
            case R.id.tvCurDay:
                intent = new Intent(this, SelectDateActivity.class);
                String[] calendar = startDate.split("-");
                intent.putExtra("y", calendar[0]);
                intent.putExtra("m", String.valueOf(Integer.parseInt(calendar[1])));
                intent.putExtra("d", calendar[2]);
                intent.putExtra("Predate", 60);
                intent.putExtra("tag", TrainSearchFragment.TAG);
                startActivityWithAnim(intent);
                break;
            case R.id.tvNextDay:
                String nextDate = DateTimeUtil.addDay(startDate, 1);
                if (DateTimeUtil.Morethanpredate(nextDate, 60 - 1)) {
                    ToastManager.getInstance().showToast(this, R.string.index_out_date);
                    return;
                }
                startDate = nextDate;
                tvCurDay.setText(spiltTimeForDisplay(startDate));
                sendEvent(startDate);
                break;
            case R.id.img_change:
                if (flag_isendorse) {
                    ToastManager.getInstance().showToast(TrainListActivity.this, getString(R.string.train_list_change_order));
                    return;
                }
                String startCityCopy = endCity;
                String startCodeCopy = endCode;
                //更换数据
                endCity = startCity;
                startCity = startCityCopy;

                endCode = startCode;
                startCode = startCodeCopy;
                tvStartStation.setText(startCity);
                tvEnd.setText(endCity);
                initQuery();
                break;
            case R.id.imgLeft:
                finish();
                break;
            case R.id.layout_sort:
                showTextView(0);
                if (mPopWindow != null) {
                    if (mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                        textViews[0].setSelected(false);
                    } else {
                        closePopWindow(mTypePopWindow);
                        textViews[0].setSelected(true);
                        mPopWindow.showPop(findViewById(R.id.layout_bottom));
                    }
                }
                break;
            case R.id.layout_select:
                if (listSeat.size() > 0 && listStation.size() > 0 && listDown.size() > 0) {
                    mTypePopWindow.setShaixuanData(listType, listSeat, listStation, listDown, listStartTime, listEndTime);
                } else {
                    refreshPopupWindow();
                }
                if (mTypePopWindow != null) {
                    if (mTypePopWindow.isShowing() ) {
                        mTypePopWindow.dismiss();
                        if (!isShaixuanPressed()){
                            textViews[1].setSelected(false);
                        }
                    } else {
                        closePopWindow(mPopWindow);
                        textViews[1].setSelected(true);
                        mTypePopWindow.showPop(findViewById(R.id.layout_bottom));
                    }
                }
                break;
            case R.id.layout_only_ticket:
                showTextView(2);
                isOnlyTicket = !isOnlyTicket;
                setHaveTicketList();
                textViews[2].setSelected(isOnlyTicket);
                showWhatPopupwindow();
                break;
            case R.id.layout_ticket_price:
                showTextView(3);
                isHaveTicket = !isHaveTicket;
                // 大筛选
                tAdapter.setHaveTicket(isHaveTicket);
                textViews[3].setSelected(!isHaveTicket);
                tv_num.setSelected(isHaveTicket);
                showWhatPopupwindow();
                break;
        }
    }

    //加载数据
    private void loadStationData(List<TrainStationInfoModel.DataBean> dataBeen2, final TrainListDescriptionAdapter adapter, String trainCode, String fromStationCode, String toStationCode, String trainNo,String trainStartDate, final int position) {

        if (dataBeen2 != null && dataBeen2.size() > 0) {
            adapter.replaceAll(dataBeen2);
        } else {
           HttpRequestManager.getInstance().stopOverStation(startDate, startCity, fromStationCode, endCity,
                   toStationCode, trainCode, trainNo, new ApiSubscriber<JsonResult<TrainStationInfoModel>>(TrainListActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
               @Override
               public void onNext(JsonResult<TrainStationInfoModel> listJsonResult) {
                   if ("0000".equals(listJsonResult.getResultCode())) {
                       dataBeanList = listJsonResult.getObject().getData();
                       if (dataBeanList != null) {
                           tAdapter.refreshThis(adapter, dataBeanList, position);
                       }
                   } else {
                       ToastManager.getInstance().showToast(TrainListActivity.this, listJsonResult.getResultMsg());

                   }
               }
           });
        }
    }

    //查询结果
    private void initQuery() {
        refreshData();
    }

    /**
     * 有票
     */
    private void setHaveTicketList() {
        List<TrainListModel> onlyTicket;

        if (isOnlyTicket) {
            textViews[2].setText(getString(R.string.train_list_only_ticket));
            onlyTicket = typeWindowShaixuan(listShow);
        } else {
            textViews[2].setText(getString(R.string.train_list_all_ticket));
            onlyTicket = typeWindowShaixuan(listShowShaixuan);
        }
        tAdapter.setOnlyTicket(onlyTicket);
        closeWindow();
    }

    //获取有票的数据
    private List<TrainListModel> getOnlyTicketData(List<TrainListModel> showOnlyTicket) {
        List<TrainListModel> onlyTicket = new ArrayList<>();
        for (int i = 0; i < showOnlyTicket.size(); i++) {
            boolean isHaveTicket = false;
            for (SeatListEntity bean : showOnlyTicket.get(i).getSeatList()) {
                if (bean.getNum() > 0) {
                    isHaveTicket = true;
                }
            }
            if (isHaveTicket) {
                onlyTicket.add(showOnlyTicket.get(i));
            }
        }
        return onlyTicket;
    }

    /**
     * 关闭 显示的 popupwindow
     */
    private void showWhatPopupwindow() {
        for (int i = 0; i < 2; i++) {
            if (mPopWindow != null && mPopWindow.isShowing()) {
                mPopWindow.dismiss();
            }else if (mTypePopWindow !=null && mTypePopWindow.isShowing()){
                mTypePopWindow.dismiss();
            }
        }
    }

    /**
     * 显示选中的 textview
     *
     * @param j
     */
    private void showTextView(int j) {
        for (int i = 0; i < 1; i++) {
            if (j == i) {
                textViews[j].setSelected(true);
            } else {
                textViews[i].setSelected(false);
            }
        }
    }

    /**
     * 格式化显示时间
     *
     * @param startdate
     * @return
     */
    private String spiltTimeForDisplay(String startdate) {

        String[] split = startdate.split("-");
        if (split != null && split.length > 2) {
            String time = String.format("%s月%s日", split[1], split[2]);
            return time;
        } else {
            Date date = new Date(System.currentTimeMillis());
            return DateTimeUtil.getDate(date, "mm月dd日");
        }

    }

    //类型 筛选
    private List<TrainListModel> typeWindowShaixuan(List<TrainListModel> listModels) {
        List<TrainListModel> listModels1 = new ArrayList<>();
        List<TrainListModel> listModels2 = new ArrayList<>();
        List<TrainListModel> listModels2EndTime = new ArrayList<>();
        List<TrainListModel> listModels3 = new ArrayList<>();
        List<TrainListModel> listModels4 = new ArrayList<>();
        List<TrainListModel> listModels5 = new ArrayList<>();//第二个筛选过的数据
        List<TrainListModel> listModels6 = new ArrayList<>();//排序过的数据
        List<TrainListModel> onlyTicketSelect;//是否只看有票

        if (trainSeat1.size() > 0) {
            for (int j = 0; j < trainSeat1.size(); j++) {
                for (int i = 0; i < listModels.size(); i++) {
                    for (SeatListEntity seatListBean : listModels.get(i).getSeatList()) {
                        if (StringUtils.isNullOrEmpty(seatListBean.getName())) {
                            continue;
                        }
                        if (seatListBean.getNum() >= 0) {
                            if (seatListBean.getName().contains(trainSeat1.get(j)) && !seatListBean.getName().equals(getString(R.string.train_list_high_type_soft))) {
                                if (!listModels1.contains(listModels.get(i))) {
                                    listModels1.add(listModels.get(i));
                                }
                            } else if (seatListBean.getName().equals(getString(R.string.train_list_high_type_soft))) {
                                if (!listModels1.contains(listModels.get(i))) {
                                    listModels1.add(listModels.get(i));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            listModels1 = listModels;
        }
        if (trainStartTime1.size() > 0) {
            for (String startTime : trainStartTime1) {
                String[] startTimes = startTime.split("~");
                int startTimes0 = getTimeSecond(startTimes[0]);
                int startTimes1 = getTimeSecond(startTimes[1]);
                for (TrainListModel trainListModel : listModels1) {
                    int startTime2 = getTimeSecond(trainListModel.getStartTime());
                    if (startTime2 >= startTimes0 && startTime2 <= startTimes1) {
                        if (!listModels2.contains(trainListModel))
                            listModels2.add(trainListModel);
                    }
                }
            }
        } else {
            listModels2 = listModels1;
        }

        if (trainArriveTime1.size() > 0) {
            for (String endTime : trainArriveTime1) {
                String[] endTimes = endTime.split("~");
                int endTimes1 = getTimeSecond(endTimes[1]);
                int endTimes0 = getTimeSecond(endTimes[0]);
                for (TrainListModel trainListModel : listModels2) {
                    int entTimes2 = getTimeSecond(trainListModel.getStartTime());
                    if (entTimes2 >= endTimes0 && entTimes2 <= endTimes1) {
                        if (!listModels2EndTime.contains(trainListModel))
                            listModels2EndTime.add(trainListModel);
                    }
                }
            }
        } else {
            listModels2EndTime = listModels2;
        }

        if (trainStart1.size() > 0) {
            for (String trainStr : trainStart1) {
                for (TrainListModel trainListModel : listModels2EndTime) {
                    if (trainStr.equals(trainListModel.getFromStationName())) {
                        if (!listModels3.contains(trainListModel))
                            listModels3.add(trainListModel);
                    }
                }
            }
        } else {
            listModels3 = listModels2EndTime;
        }

        if (trainEndStation1.size() > 0) {

            for (String trainStr : trainEndStation1) {
                for (TrainListModel trainListModel : listModels3) {
                    if (trainStr.equals(trainListModel.getToStationName())) {
                        if (!listModels4.contains(trainListModel))
                            listModels4.add(trainListModel);
                    }
                }
            }
        } else {
            listModels4 = listModels3;
        }
        if (trainType1.size() > 0) {
            for (String trainStr : trainType1) {
                String type = "";
                if (trainStr.equals(getString(R.string.train_list_select_else))) {
                    type = "Y/P/L";
                }
                for (TrainListModel trainListModel : listModels4) {
                    if (trainStr.contains(trainListModel.getTrainType())) {
                        if (!listModels5.contains(trainListModel))
                            listModels5.add(trainListModel);
                    } else if (type.contains(trainListModel.getTrainType())) {
                        if (!listModels5.contains(trainListModel))
                            listModels5.add(trainListModel);
                    }
                }
            }
        } else {
            listModels5 = listModels4;
        }

        listModels6  = initSort(listModels5);

        //只看有票
        if (isOnlyTicket) {
            onlyTicketSelect = getOnlyTicketData(listModels6);
        } else {
            onlyTicketSelect = listModels6;
        }
        //余票 价格
        return onlyTicketSelect;
    }

    /**
     * hh:mm
     *
     * @param time
     * @return
     */
    private int getTimeSecond(String time) {

        if (StringUtils.isNullOrEmpty(time)) {
            return 1;
        }
        String[] times = time.split(":");

        int returnTime = Integer.parseInt(times[0].trim()) * 60 + Integer.parseInt(times[1].trim());

        return returnTime;
    }

    // 按发时 升序排序
    private List<TrainListModel> sortStartCondition(List<TrainListModel> listModels5) {
        Collections.sort(listModels5, new TrainStratTimeModel());
        return listModels5;
    }

    // 按到时 升序排序
    private List<TrainListModel> sortArriveCondition(List<TrainListModel> listModels5) {
        Collections.sort(listModels5, new TrainArriveTimeModel());
        return listModels5;
    }

    // 按价格 升序排序
    private List<TrainListModel> sortPriceCondition(List<TrainListModel> listModels5) {
        Collections.sort(listModels5, new TrainPriceSortModel());
        return listModels5;
    }

    // 按时间长度排序
    private List<TrainListModel> sortShortTimeCondition(List<TrainListModel> listModels5) {
        Collections.sort(listModels5, new TrainShortTimeModel());
        return listModels5;
    }

    private void sendEvent(String date) {
        String[] split = date.split("-");
        SelectDayEvent event = new SelectDayEvent(split[0], split[1], split[2], TrainSearchFragment.TAG);
        RxBus.getInstance().post(event);
    }

    @Override
    public void clickReload() {
        refreshData();
    }

    @Subscribe
    public void onDateEvent(SelectDayEvent event) {
        String y = event.getYear();
        String m = event.getMonth();
        String d = event.getDay();
        StringBuffer buffer = new StringBuffer();
        buffer.append(y + "-");
        if (m.length() == 1) {
            buffer.append("0" + m + "-");
        } else {
            buffer.append(m + "-");
        }
        if (d.length() == 1) {
            buffer.append("0" + d);
        } else {
            buffer.append(d);
        }
        startDate = buffer.toString();
        tAdapter.setDate(startDate);
        tvCurDay.setText(spiltTimeForDisplay(startDate));
        initQuery();
    }
}
