package com.wxws.myticket.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.activity.SelectDateActivity;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.order.activity.TrainOrderDetailsActivity;
import com.wxws.myticket.order.entity.EndorseTicketModel;
import com.wxws.myticket.train.adapter.SeatInfoAdapter;
import com.wxws.myticket.train.dialog.BookingDialog;
import com.wxws.myticket.train.dialog.ChangeFailureDialog;
import com.wxws.myticket.train.entity.ChangeTicketResultEntity;
import com.wxws.myticket.train.entity.ChooseSeatModel;
import com.wxws.myticket.train.entity.CommitOrderResponseModel;
import com.wxws.myticket.train.entity.ListSortComparator;
import com.wxws.myticket.train.entity.SeatinfoModel;
import com.wxws.myticket.train.entity.TrainListModel;
import com.wxws.myticket.train.fragment.TrainSearchFragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * desc: 火车票下单选座界面
 * Date: 2016-08-01 11:33
 *
 * @author jiangyan
 */
public class ChooseSeatTypeActivity extends BaseActivity implements View.OnClickListener, SeatInfoAdapter.OnBuyTrainTicketClickListener {

    private TextView tv_fromtime;
    private TextView tv_fromstation;
    private TextView tv_totime;
    private TextView tv_tostation;
    private TextView tv_preday;
    private LinearLayout ll_curday;
    private TextView tv_currentday;
    private TextView tv_nextday;
    private ListView lv_container;
    private TextView tv_traincode;
    private TextView tv_runtime;
    private SeatInfoAdapter mAdapter;
    private String startdatestr;
    private String fromstation;
    private String tostation;
    private String traincode;
    private String fromTime;
    private String toTime;
    private String runtime;
    private ChooseSeatModel mSeatInfo;
    private List<SeatinfoModel> mSeatList = new ArrayList<SeatinfoModel>();
    private String fromstationcode;
    private String tostationcode;
    private String trainNo;
    private boolean flag_endorse;
    private String mOrderId;
    private List<EndorseTicketModel> ticketList;
    private String newOrderId;
    private String id;//订单来源id
    private InnerHandler innerHandler;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chooseseat);

        getDataFromPre();

        initView();

        initData();

        getData();
    }

    public void getDataFromPre() {
        // 从前面的intent获取初始数据
        Bundle bundle = getIntent().getBundleExtra("trainItem");
        TrainListModel trainListModel = (TrainListModel) bundle.getSerializable("item");

        startdatestr = getIntent().getBundleExtra("trainItem").getString("startDate");
        id = getIntent().getStringExtra("id");
        fromTime = trainListModel.getStartTime();
        toTime = trainListModel.getArriveTime();
        runtime = trainListModel.getRunTimeMinute();

        fromstation = trainListModel.getFromStationName();
        fromstationcode = trainListModel.getFromStationCode();
        tostationcode = trainListModel.getToStationCode();
        tostation = trainListModel.getToStationName();
        traincode = trainListModel.getTrainCode();
        trainNo = trainListModel.getTrainNo();

        flag_endorse = getIntent().getBooleanExtra("isendorse", false);

        if (flag_endorse) {
            mOrderId = getIntent().getStringExtra("orderid");
            ticketList = (List<EndorseTicketModel>) getIntent().getBundleExtra("ticketbundle").getSerializable("ticketlist");
        }
    }

    public void initView() {
        bindTitleViews();

        bindLoadingView();

        tv_fromtime = (TextView) findViewById(R.id.tv_chooseseat_fromtime);
        tv_fromstation = (TextView) findViewById(R.id.tv_chooseseat_fromstation);
        tv_totime = (TextView) findViewById(R.id.tv_chooseseat_totime);
        tv_tostation = (TextView) findViewById(R.id.tv_chooseseat_tostation);
        tv_traincode = (TextView) findViewById(R.id.tv_chooseseat_traincode);
        tv_runtime = (TextView) findViewById(R.id.tv_chooseseat_runtime);

        tv_preday = (TextView) findViewById(R.id.tv_chooseseat_preday);
        ll_curday = (LinearLayout) findViewById(R.id.ll_chooseseat_currentday);
        tv_currentday = (TextView) findViewById(R.id.tv_chooseseat_curDay);
        tv_nextday = (TextView) findViewById(R.id.tv_chooseseat_nextday);

        tv_preday.setOnClickListener(this);
        tv_nextday.setOnClickListener(this);
        ll_curday.setOnClickListener(this);


        lv_container = (ListView) findViewById(R.id.lv_chooseseat_container);
        mAdapter = new SeatInfoAdapter(ChooseSeatTypeActivity.this, flag_endorse);
        mAdapter.setOnBuyTrainTicketClickListener(this);
        lv_container.setAdapter(mAdapter);

    }

    public void initData() {
        tv_fromtime.setText(fromTime);
        tv_fromstation.setText(fromstation);
        tv_totime.setText(toTime);
        tv_tostation.setText(tostation);
        tv_traincode.setText(traincode);
        tv_runtime.setText(DateTimeUtil.minToHour(runtime));
        tv_currentday.setText(spiltTimeForDisplay(startdatestr));

        mImgSpline.setVisibility(View.GONE);
        llTitleBar.setBackgroundResource(R.mipmap.chooseseat_titlebg);
        barTitle.setTextColor(getResources().getColor(R.color.white));
        barTitle.setText(getString(R.string.train_choose_seat));
        imgBarLeft.setImageResource(R.drawable.slt_return_white);
        innerHandler = new InnerHandler(ChooseSeatTypeActivity.this);
        dialog = new BookingDialog(ChooseSeatTypeActivity.this);
    }

    public void getData() {

        HttpRequestManager.getInstance().trainLineTicket(startdatestr, fromstation, fromstationcode,
                tostation, tostationcode, traincode, trainNo, new ApiSubscriber<JsonResult<ChooseSeatModel>>(ChooseSeatTypeActivity.this,ApiSubscriber.NETVIEWLOADING) {
                    @Override
                    public void onNext(JsonResult<ChooseSeatModel> changeTicketJsonResult) {
                        if ("0000".equals(changeTicketJsonResult.getResultCode()) && changeTicketJsonResult.getObject() != null) {
                            mSeatList.clear();
                            mSeatInfo = changeTicketJsonResult.getObject();
                            updateSeat();
                        } else {
                            showErrorOrNoData(getString(R.string.choose_seat_tips), R.string.click_reload, R.mipmap.tip1);
                        }
                    }
                });
    }

    public void clickReload() {
        // 点击重试
        getData();
    }

    public void updateSeat() {

        tv_fromtime.setText(mSeatInfo.getStartTime());
        tv_totime.setText(mSeatInfo.getArriveTime());
        tv_runtime.setText(DateTimeUtil.minToHour(mSeatInfo.getRunTimeMinute()));

        String rwreg = "^rw.*";// 软卧正则
        String ywreg = "^yw.*";// 硬卧正则

        int rwprice_max = 0;  // 软卧价格,以最高为准
        int ywprice_max = 0;  // 硬卧价格，以最高为准

        int rw_position = -1; // 软卧position
        int yw_position = -1; // 硬卧position

        for (int i = 0; i < mSeatInfo.getSeatList().size(); i++) {
            SeatinfoModel nowModel = mSeatInfo.getSeatList().get(i);
            boolean isRWorYW = false;

            if (nowModel.getNum() != -1) {

                if (nowModel.getType().matches(rwreg)) {
                    nowModel.setName("软卧");

                    if (rw_position == -1) {
                        rw_position = mSeatList.size();
                    } else {
                        isRWorYW = true;
                        // 价格
                        if (nowModel.getPrice() > rwprice_max) {
                            rwprice_max = nowModel.getPrice();
                        }
                        nowModel.setPrice(rwprice_max);

                        // 数量
                        int maxNum = mSeatList.get(rw_position).getNum() + nowModel.getNum();
                        nowModel.setNum(maxNum);

                        mSeatList.set(rw_position, nowModel);
                    }
                }

                if (nowModel.getType().matches(ywreg)) {
                    nowModel.setName("硬卧");

                    if (yw_position == -1) {
                        yw_position = mSeatList.size();
                    } else {
                        isRWorYW = true;
                        // 价格
                        if (nowModel.getPrice() > ywprice_max) {
                            ywprice_max = nowModel.getPrice();
                        }
                        nowModel.setPrice(ywprice_max);

                        // 数量
                        int maxNum = mSeatList.get(yw_position).getNum() + nowModel.getNum();
                        nowModel.setNum(maxNum);

                        mSeatList.set(yw_position, nowModel);
                    }
                }
                if (!isRWorYW) {
                    mSeatList.add(nowModel);
                }
            }
        }

        for (SeatinfoModel model : mSeatList) {
            if (model.getNum() == 0) {
                model.setLevel(1000000+model.getPrice());
            } else {
                model.setLevel(model.getPrice());
            }
            model.setLevel(model.getPrice());
            if (model.getType().equals("wz")) {
                model.setLevel(2000001);
            }
        }

        sortData(mSeatList);
        mAdapter.refreshData(mSeatList);

    }

    private void sortData(List<SeatinfoModel> seatinfoList) {
        // 对 mSeatList 进行排序
        ListSortComparator comp = new ListSortComparator();
        Collections.sort(seatinfoList, comp);
    }

    // 格式化显示时间
    private String spiltTimeForDisplay(String startdate) {

        String[] split = startdate.split("-");
        if (split != null && split.length > 2) {
            String time = String.format("%s月%s日", split[1], split[2]);
            return time;
        } else {
            java.util.Date date = new java.util.Date(System.currentTimeMillis());
            return DateTimeUtil.getDate(date, "mm月dd日");
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tv_chooseseat_preday:
                String date = DateTimeUtil.addDay(startdatestr, -1);
                if (DateTimeUtil.Lessthantoday(date)) {
                    ToastManager.getInstance().showToast(this, "亲,不能选择过去的时间哦~");
                    return;
                }
                startdatestr = date;
                tv_currentday.setText(spiltTimeForDisplay(startdatestr));

                getData();

                break;
            case R.id.tv_chooseseat_nextday:
                String nextdate = DateTimeUtil.addDay(startdatestr, 1);
                if (DateTimeUtil.Morethanpredate(nextdate, 60 - 1)) {
                    ToastManager.getInstance().showToast(this, R.string.index_out_date);
                    return;
                }
                startdatestr = nextdate;
                tv_currentday.setText(spiltTimeForDisplay(startdatestr));
                getData();
                break;

            case R.id.ll_chooseseat_currentday:
                Intent intent = new Intent(this, SelectDateActivity.class);
                String[] calendar = startdatestr.split("-");
                intent.putExtra("y", calendar[0]);
                intent.putExtra("m", String.valueOf(Integer.parseInt(calendar[1])));
                intent.putExtra("d", calendar[2]);
                intent.putExtra("Predate", 60);
                intent.putExtra("tag", TrainSearchFragment.TAG);
                startActivityWithAnim(intent);
                break;
        }
    }

    @Subscribe
    public void onRecivedEvent(SelectDayEvent event) {
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
        startdatestr = buffer.toString();
        tv_currentday.setText(spiltTimeForDisplay(startdatestr));

        getData();

    }

    private BookingDialog dialog;

    @Override
    public void buy(int position) {

        if (flag_endorse) {
            repeatChooseSeat(position);
        } else {
            //初始化选中状态
            for (SeatinfoModel seatinfoModel : mSeatList) {
                seatinfoModel.setChoosed(false);
            }
            mSeatList.get(position).setChoosed(true);
            Intent intent = new Intent(ChooseSeatTypeActivity.this, FillinOrderActivity.class);
            intent.putExtra("fromdate", startdatestr);
            intent.putExtra("buyposition", position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("seatinfo", mSeatInfo);
            bundle.putSerializable("seatlist", (Serializable) mSeatList);
            intent.putExtra("seatinfobundle", bundle);

            startActivityWithAnim(intent);
        }

    }

    //改签
    private void repeatChooseSeat(int position) {
        dialog.show();
        dialog.refreshDialog(getString(R.string.fill_in_order_background_change));
        loadTicketData(position);
    }


    private void loadTicketData(int position) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        String seatType = "";

        if ("无座".equals(mSeatList.get(position).getName())) {
            seatType = mSeatList.get(0).getSeatType();
        } else {
            seatType = mSeatList.get(position).getSeatType();
        }
        for (EndorseTicketModel endorseTicketModel : ticketList) {
            stringBuffer.append(String.format("{\"id\":\"%s\",\"seatType\":\"%s\"}", endorseTicketModel.getId(), seatType));
        }
        stringBuffer.append("]");

        HttpRequestManager.getInstance().requestChange(mOrderId, startdatestr, fromstationcode, tostationcode,
                traincode,  trainNo, seatType,stringBuffer.toString(), new ApiSubscriber<JsonResult<CommitOrderResponseModel>>(ChooseSeatTypeActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
                    @Override
                    public void onNext(JsonResult<CommitOrderResponseModel> changeOrderModelJsonResult) {
                        if ("0000".equals(changeOrderModelJsonResult.getResultCode())) {
                            newOrderId = changeOrderModelJsonResult.getObject().getNewOrderId();
                            lockTicket();
                        } else {
                            closeDialog();
                            ToastManager.getInstance().showToast(ChooseSeatTypeActivity.this, changeOrderModelJsonResult.getResultMsg());
                        }
                    }
                });
    }

    //锁座
    private void lockTicket() {
        HttpRequestManager.getInstance().requestChangeResult(newOrderId, new ApiSubscriber<JsonResult<ChangeTicketResultEntity>>(ChooseSeatTypeActivity.this,ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<ChangeTicketResultEntity> changeOrderModelJsonResult) {
                if ("0000".equals(changeOrderModelJsonResult.getResultCode())) {
                    switch (changeOrderModelJsonResult.getObject().getStatus()) {
                        case 0:
                            innerHandler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        case 1:
                            closeDialog();
                            Intent intent = new Intent(ChooseSeatTypeActivity.this, TrainOrderDetailsActivity.class);
                            intent.putExtra("isendorse", true);
                            intent.putExtra("orderid", changeOrderModelJsonResult.getObject().getOrderId());
                            intent.putExtra("isChange", true);
                            intent.putExtra("ordertype", 2);
                            startActivityWithAnim(intent);
                            break;
                        case 2:
                            closeDialog();
                            showChangeFailure(changeOrderModelJsonResult.getObject().getMessage());
                            break;
                    }
                } else {
                    closeDialog();
                    ToastManager.getInstance().showToast(ChooseSeatTypeActivity.this, changeOrderModelJsonResult.getResultMsg());
                }

            }
        });
    }

    /**
     * 改签失败
     */
    private void showChangeFailure(String content) {
        ChangeFailureDialog changeFailureDialog = new ChangeFailureDialog(ChooseSeatTypeActivity.this, content);
        changeFailureDialog.show();
    }

    //关闭dialog
    private void closeDialog() {
        if (dialog != null) {
            dialog.colseFlag();
            dialog.dismiss();
        }
    }

    private static class InnerHandler extends Handler {

        WeakReference<ChooseSeatTypeActivity> weakReference;

        public InnerHandler(ChooseSeatTypeActivity chooseSeatTypeActivity) {
            this.weakReference = new WeakReference<>(chooseSeatTypeActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChooseSeatTypeActivity chooseSeatTypeActivity = weakReference.get();
            if (chooseSeatTypeActivity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    chooseSeatTypeActivity.lockTicket();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        innerHandler.removeMessages(0);
    }
}