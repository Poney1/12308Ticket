package com.wxws.myticket.train.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.bus.activity.CityActivity;
import com.wxws.myticket.bus.activity.SelectDateActivity;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.CheckSwitchButton;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.my.Entity.TrainAccountInfo;
import com.wxws.myticket.my.activity.AccountActivity;
import com.wxws.myticket.my.activity.TrainLoginActivity;
import com.wxws.myticket.train.activity.TrainCityActivity;
import com.wxws.myticket.train.activity.TrainListActivity;
import com.wxws.myticket.train.adapter.TrainIndexHistoryAdapter;
import com.wxws.myticket.train.contants.TrainContants;
import com.wxws.myticket.train.entity.TrainLineSaveEntity;
import com.wxws.myticket.train.entity.TrainSearchEntity;
import com.wxws.myticket.train.entity.TrainSearchModel;
import com.wxws.myticket.train.rxentity.TrainCityEvent;
import com.wxws.myticket.train.rxentity.TrainLoginEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * desc: 火车票首页搜索
 * Date: 2016-07-26 10:06
 *
 * @auther: lixiangxiang
 */
public class TrainSearchFragment extends BaseFragment implements View.OnClickListener {

    public static String TAG = SelectDateActivity.TrainTAG;
    private TextView mTvTrainStart;
    private TextView mTvTrainEnd;
    private TextView tvShowTime;
    private TextView tvTomorrow;
    private LinearLayout llTimeClick;
    private CheckSwitchButton ckSelectSpeed;
    private Button btnQuery;
    private ListViewForScrollView lsHistory;
    private ImageButton btnChange;

    private String year, month, day;//设置年月日
    private boolean highSpeed = false;//默认关闭只看高铁
    private boolean isHighSpeed = true;
    private List<TrainLineSaveEntity> trainHistoryList;//火车票历史记录
    private TrainIndexHistoryAdapter mTrainHistory;
    private String clickStart = "start";//选择出发
    private String clickEnd = "end";
    private String startTime;//出发时间
    private String startCode;//
    private String endCode;//
    private TextView tvBinding12306;
    private LinearLayout llBinding12306;
    private TrainAccountInfo mTrainAccount;
    private TextView tv_binding_account;
    private TextView tv_binding_error;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_search, null);

        bindView();

        bindData();

        bindClick();

        if(checkLogin()){
            checkTrainLogin();
        }
        return view;
    }

    private void checkTrainLogin() {
        updateTrainView();
    }

    private void updateTrainView() {
        mTrainAccount = (TrainAccountInfo) PreferencesUtils.readDataObject(getActivity(), ConfigConstants.TRAIN_LOGININFO);

        if (null == mTrainAccount) {
            tvBinding12306.setText(getActivity().getString(R.string.train_binding_12308));
            tvBinding12306.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv_binding_account.setVisibility(View.GONE);
        } else {
            tvBinding12306.setText(getActivity().getString(R.string.train_binding_tips));
            tvBinding12306.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tv_binding_account.setVisibility(View.VISIBLE);
            if (!mTrainAccount.isPass()) { // 账号错误，只显示账号信息，不显示前置tag
                tv_binding_account.setText(mTrainAccount.getAccount());
                tv_binding_error.setVisibility(View.VISIBLE);
            } else {
                tv_binding_account.setText(String.format(getActivity().getString(R.string.train_binding_account), mTrainAccount.getAccount()));
                tv_binding_error.setVisibility(View.GONE);
            }
        }

    }

    private void bindClick() {

        llTimeClick.setOnClickListener(this);
        ckSelectSpeed.setOnClickListener(this);
        mTvTrainStart.setOnClickListener(this);
        mTvTrainEnd.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        llBinding12306.setOnClickListener(this);
        lsHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (trainHistoryList != null && trainHistoryList.size() > 0) {
                    mTvTrainStart.setText(trainHistoryList.get(position).getStartCity());
                    mTvTrainEnd.setText(trainHistoryList.get(position).getEndCity());
                    startCode = trainHistoryList.get(position).getStartCode();
                    endCode = trainHistoryList.get(position).getEndCode();
                    doQuery();
                }
            }
        });
    }

    private void bindData() {

        ckSelectSpeed.setChecked(isHighSpeed);

        trainHistoryList = (List<TrainLineSaveEntity>) PreferencesUtils.readDataObject(getActivity(),  TrainContants.TRAINSEARCHHISTORY);

        TrainSearchEntity trainSearchModel = (TrainSearchEntity) PreferencesUtils.readDataObject(getActivity(), TrainContants.TRAINSEARCH);

        if (trainSearchModel != null) {
            startCode = trainSearchModel.getStartCityCode();
            endCode = trainSearchModel.getEndCityCode();
            mTvTrainStart.setText(trainSearchModel.getStartCity());
            mTvTrainEnd.setText(trainSearchModel.getEndCity());
        }
        if (trainHistoryList == null) {
            trainHistoryList = new ArrayList<>();
        }
        mTrainHistory = new TrainIndexHistoryAdapter(getActivity(), trainHistoryList);
        lsHistory.setAdapter(mTrainHistory);
        setDate();
    }

    private void bindView() {
        mTvTrainStart = (TextView) view.findViewById(R.id.tv_train_start);//出发城市
        mTvTrainEnd = (TextView) view.findViewById(R.id.tv_train_end);//到达城市
        tvShowTime = (TextView) view.findViewById(R.id.tv_display_time);//显示星期几
        tvTomorrow = (TextView) view.findViewById(R.id.tv_default_day);//默认明天
        llTimeClick = (LinearLayout) view.findViewById(R.id.ll_default_time);//设置时间
        llBinding12306 = (LinearLayout) view.findViewById(R.id.ll_binding_12306);//绑定12306
        ckSelectSpeed = (CheckSwitchButton) view.findViewById(R.id.ck_select_speed);//是否选择只看高铁
        btnQuery = (Button) view.findViewById(R.id.btn_query);//查询
        btnChange = (ImageButton) view.findViewById(R.id.btn_change);//切换城市
        lsHistory = (ListViewForScrollView) view.findViewById(R.id.ls_history);//查询历史
        tvBinding12306 = (TextView) view.findViewById(R.id.tv_binding_12306);
        tv_binding_account = (TextView) view.findViewById(R.id.tv_binding_account);
        tv_binding_error = (TextView) view.findViewById(R.id.tv_binding_error);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_train_start:
                intent = new Intent(getActivity(), TrainCityActivity.class);
                intent.putExtra("city", clickStart);
                intent.putExtra("hint", getString(R.string.train_start_hint));
                startActivity(intent);
                break;
            case R.id.ll_default_time:
                clickSelectDate();
                break;
            case R.id.btn_change:
                if (StringUtils.isNullOrEmpty(mTvTrainStart.getText().toString())) {
                    ToastManager.getInstance().showToast(getActivity(), getActivity().getString(R.string.train_start_city_null));
                    return;
                }
                if (StringUtils.isNullOrEmpty(mTvTrainEnd.getText().toString())) {
                    ToastManager.getInstance().showToast(getActivity(), getActivity().getString(R.string.train_end_city_null));
                    return;
                }
                if (validate()) {
                    String change = endCode;
                    endCode = startCode;
                    startCode = change;
                    String startCity = mTvTrainEnd.getText().toString().trim();
                    String endCity = mTvTrainStart.getText().toString().trim();
                    mTvTrainStart.setText(startCity);
                    mTvTrainEnd.setText(endCity);
                }
                break;
            case R.id.btn_query:
                //状态选择
                highSpeed = !ckSelectSpeed.isChecked();
                if (validate()) {
                    doQuery();
                }
                break;
            case R.id.tv_train_end:
                if (validateStartCity()) {
                    intent = new Intent(getActivity(), TrainCityActivity.class);
                    intent.putExtra("city", clickEnd);
                    intent.putExtra("hint", getString(R.string.train_end_hint));
                    startActivity(intent);
                }
                break;
            case R.id.ll_binding_12306:
                startActivity(new Intent(getActivity(), TrainLoginActivity.class));
                break;
        }
    }

    /**
     * 查询车次
     */
    private void doQuery() {
        setCheckLine(mTrainHistory);
    }

    /**
     * 验证是否能通过请求
     */
    private boolean validate() {

        validateStartCity();


        validateEndCity();


        return true;
    }

    /**
     * 验证出发城市
     *
     * @return
     */
    private boolean validateStartCity() {

        if (StringUtils.isNullOrEmpty(mTvTrainStart.getText().toString().trim())) {
            ToastManager.getInstance().showToast(getActivity(), getActivity().getString(R.string.train_start_city_null));
            return false;
        }
        return true;
    }

    /**
     * 验证到达城市
     *
     * @return
     */
    private boolean validateEndCity() {

        if (StringUtils.isNullOrEmpty(mTvTrainEnd.getText().toString().trim())) {
            ToastManager.getInstance().showToast(getActivity(), getActivity().getString(R.string.train_end_city_null));
            return false;
        }
        return true;
    }

    /**
     * 存储 查询历史
     */
    private void setCheckLine(TrainIndexHistoryAdapter trainAdapter) {

        String startCity = mTvTrainStart.getText().toString().trim();
        String endCity = mTvTrainEnd.getText().toString().trim();

        if (!StringUtils.isNullOrEmpty(startCity) && !StringUtils.isNullOrEmpty(endCity)) {
            //添加历史记录
            TrainLineSaveEntity trainHistory = new TrainLineSaveEntity();
            List<TrainLineSaveEntity> listOther = new ArrayList<>();
            trainHistory.setStartCity(startCity);
            trainHistory.setEndCity(endCity);
            trainHistory.setStartCode(startCode);
            trainHistory.setEndCode(endCode);

            List<TrainLineSaveEntity> mListHistory = (List<TrainLineSaveEntity>) PreferencesUtils.readDataObject(getActivity(), TrainContants.TRAINSEARCHHISTORY);

            if (mListHistory == null) {
                mListHistory = new ArrayList<>();
            }

            //加一个去除重复的操作
            for (int i = 0; i < mListHistory.size(); i++) {
                if (trainHistory.getStartCity().equals(mListHistory.get(i).getStartCity()) && trainHistory.getEndCity().equals(mListHistory.get(i).getEndCity())) {
                    mListHistory.remove(i);
                    break;
                }
            }

            if (mListHistory != null && mListHistory.size() > 1) {
                listOther.add(mListHistory.get(0));
                listOther.add(mListHistory.get(1));
                listOther.add(0, trainHistory);
                mListHistory.clear();
                mListHistory.addAll(listOther);
            } else if (mListHistory != null && mListHistory.size() == 1) {
                mListHistory.add(0, trainHistory);
            } else {
                mListHistory.add(trainHistory);
            }
            //刷新数据
            if (mListHistory != null && mListHistory.size() > 0) {
                PreferencesUtils.saveDataObject(getActivity(), TrainContants.TRAINSEARCHHISTORY, mListHistory);
            }
            trainHistoryList.clear();
            trainHistoryList = mListHistory;
            trainAdapter.replaceAll(trainHistoryList);

            Intent intent = new Intent(getActivity(), TrainListActivity.class);
            intent.putExtra("startCity", startCity);
            intent.putExtra("endCity", endCity);
            intent.putExtra("startTime", startTime);
            intent.putExtra("isOnlySpeed", highSpeed);
            intent.putExtra("endCode", endCode);
            intent.putExtra("startCode", startCode);

            TrainSearchEntity trainSearchModel = new TrainSearchEntity();

            trainSearchModel.setStartCity(startCity);
            trainSearchModel.setEndCity(endCity);
            trainSearchModel.setStartCityCode(startCode);
            trainSearchModel.setEndCityCode(endCode);

            PreferencesUtils.saveDataObject(getActivity(),TrainContants.TRAINSEARCH, trainSearchModel);
            startActivity(intent);
        }
    }

    /**
     * 选择时间
     */
    private void clickSelectDate() {
        Intent intent;
        intent = new Intent(mActivity, SelectDateActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("tag", TAG);
        mActivity.startActivityWithAnim(intent);
    }

    /**
     * 设置时间
     */
    private void setDate() {
        Time time = new Time();
        time.setToNow();
        boolean b = time.hour >= 0;
        long millsOfOneDay = 1000 * 60 * 60 * 24;
        long nowMills = time.toMillis(false);
        if (b) {
            time.set(nowMills + millsOfOneDay);
        }
        year = time.year + "";
        month = (time.month + 1) + "";
        day = time.monthDay + "";
        StringBuffer buffer = new StringBuffer();
        buffer.append(year + "-");
        if (month.length() == 1) {
            buffer.append("0" + month + "-");
        } else {
            buffer.append(month + "-");
        }
        if (day.length() == 1) {
            buffer.append("0" + day);
        } else {
            buffer.append(day);
        }
        startTime = buffer.toString();
        tvShowTime.setText(DateTimeUtil.getSpChineseMMDD(startTime));
        tvTomorrow.setText(String.format("%s  %s", "明天", DateTimeUtil.getWeekStr(startTime)));
    }

    @Subscribe
    public void onSelectEvent(SelectDayEvent event) {
        if (TAG.equals(event.getTag())) {
            year = event.getYear();
            month = event.getMonth();
            day = event.getDay();

            StringBuffer buffer = new StringBuffer();
            buffer.append(year + "-");
            if (month.length() == 1) {
                buffer.append("0" + month + "-");
            } else {
                buffer.append(month + "-");
            }
            if (day.length() == 1) {
                buffer.append("0" + day);
            } else {
                buffer.append(day);
            }
            startTime = buffer.toString();
            Time t = new Time();
            t.setToNow();
            int[] compare = new int[3];
            compare[0] = Integer.parseInt(year);
            compare[1] = Integer.parseInt(month) - 1;
            compare[2] = Integer.parseInt(day);
            long betweenDays = DateTimeUtil.getDaysBetween(compare) + 1;
            String to = mActivity.getResources().getString(R.string.date_today);
            String tomr = mActivity.getResources().getString(R.string.date_tomorrow);
            String ttomr = mActivity.getResources().getString(R.string.date_after_tomorrow);
            if (betweenDays == 0) {
                tvTomorrow.setText(String.format("%s  %s", to, DateTimeUtil.getWeekStr(DateTimeUtil.getSysDateYMD(betweenDays))));
            } else if (betweenDays == 1) {
                tvTomorrow.setText(String.format("%s  %s", to, DateTimeUtil.getWeekStr(DateTimeUtil.getSysDateYMD(betweenDays))));
            } else if (betweenDays == 2) {
                tvTomorrow.setText(String.format("%s  %s", tomr, DateTimeUtil.getWeekStr(DateTimeUtil.getSysDateYMD(betweenDays))));
            } else if (betweenDays == 3) {
                tvTomorrow.setText(String.format("%s  %s", ttomr, DateTimeUtil.getWeekStr(DateTimeUtil.getSysDateYMD(betweenDays))));
            } else {
                tvTomorrow.setText(DateTimeUtil.getWeekStr(startTime));
            }
            tvShowTime.setText(DateTimeUtil.getSpChineseMMDD(startTime));
        }
    }

    @Subscribe
    public void onTrainCitySelectEvent(TrainCityEvent event) {

        if (clickStart.equals(event.getIsSelect())) {
            mTvTrainStart.setText(event.getCity());
            startCode = event.getCode();
            if (!mTvTrainEnd.getText().toString().trim().equals(getString(R.string.train_index_end))) {
                mTvTrainEnd.setText("");
            }
        } else if (clickEnd.equals(event.getIsSelect())) {
            mTvTrainEnd.setText(event.getCity());
            endCode = event.getCode();
        }
    }

    @Subscribe
    public void onRefreshLoginEvent(TrainLoginEvent event) {
        if(checkLogin()){
            checkTrainLogin();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
}
