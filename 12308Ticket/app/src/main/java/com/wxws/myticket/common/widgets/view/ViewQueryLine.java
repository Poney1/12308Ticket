package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.bus.dao.BusStartCityHelper;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;

/**
 * desc: 出发到达城市
 * Date: 2016/10/19 09:29
 *
 * @auther: lixiangxiang
 */
public class ViewQueryLine extends LinearLayout {

    private Context context;
    private String startCity;
    private String endCity;
    private String startDate;

    private TextView tvSelectStart;
    private TextView tvSelectEnd;
    private TextView tvDisplayTime;
    private TextView tvDefaultDay;
    private ImageButton btnChange;
    private Button button;
    private LinearLayout llBusStart;
    private LinearLayout llBusEnd;
    private LinearLayout llDefaultTime;

    private String year, month, day;

    private OnCityTimeSelectListener onCityTimeSelectListener;
    private LinearLayout llVq;

    public ViewQueryLine(Context context) {
        super(context);
        onCreateViewQueryLine(context);
    }


    public ViewQueryLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreateViewQueryLine(context);
    }

    public ViewQueryLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateViewQueryLine(context);
    }

    private void onCreateViewQueryLine(Context context) {
        this.context = context;

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {
        llBusStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityTimeSelectListener == null)
                    return;
                onCityTimeSelectListener.startCitySelect();

            }
        });

        llBusEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityTimeSelectListener == null)
                    return;
                if (judgeStartCity())
                    onCityTimeSelectListener.endCitySelect();
            }
        });

        btnChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCity();
            }
        });

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityTimeSelectListener == null || !judgeStartCity()|| !judgeEndCity() )
                    return;
                onCityTimeSelectListener.queryLine(startCity, endCity, startDate);
            }
        });

        llDefaultTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityTimeSelectListener == null)
                    return;
                onCityTimeSelectListener.timeSelect(year, month, day);
            }
        });
    }

    private void initData() {
        startDate = DateTimeUtil.getTomorrowDate();
        setDate(startDate);
    }

    private void initView() {

        View inflateView = View.inflate(context, R.layout.view_start_arrive, this);
        //出发城市选择
        tvSelectStart = (TextView) inflateView.findViewById(R.id.tv_select_start);
        //到达城市选择
        tvSelectEnd = (TextView) inflateView.findViewById(R.id.tv_select_end);
        //日期
        tvDisplayTime = (TextView) inflateView.findViewById(R.id.tv_display_time);
        //天
        tvDefaultDay = (TextView) inflateView.findViewById(R.id.tv_default_day);
        //切换出发到达城市
        btnChange = (ImageButton) inflateView.findViewById(R.id.btn_change);
        // 查询
        button = (Button) inflateView.findViewById(R.id.btn_query);
        //出发城市点击事件
        llBusStart = (LinearLayout) inflateView.findViewById(R.id.ll_bus_start);
        //出发城市点击事件
        llBusEnd = (LinearLayout) inflateView.findViewById(R.id.ll_bus_end);
        //布局id
        llVq = (LinearLayout) inflateView.findViewById(R.id.ll_vq);
        llDefaultTime = (LinearLayout) inflateView.findViewById(R.id.ll_default_time);

    }

    //设定时间
    public void setDate(String selectDate) {
        String[] splits = selectDate.split("-");
        year = splits[0];
        month = splits[1];
        day = splits[2];
        int[] compare = new int[3];
        compare[0] = Integer.parseInt(year);
        compare[1] = Integer.parseInt(month) - 1;
        compare[2] = Integer.parseInt(day);
        long betweenDays = DateTimeUtil.getDaysBetween(compare) + 1;
        String to = context.getString(R.string.date_today);
        String tomr = context.getString(R.string.date_tomorrow);
        String ttomr = context.getString(R.string.date_after_tomorrow);
        if (betweenDays == 0) {
            tvDefaultDay.setText(String.format("%s  %s", to, DateTimeUtil.getWeekStr(selectDate)));
        } else if (betweenDays == 1) {
            tvDefaultDay.setText(String.format("%s  %s", to, DateTimeUtil.getWeekStr(selectDate)));
        } else if (betweenDays == 2) {
            tvDefaultDay.setText(String.format("%s  %s", tomr, DateTimeUtil.getWeekStr(selectDate)));
        } else if (betweenDays == 3) {
            tvDefaultDay.setText(String.format("%s  %s", ttomr, DateTimeUtil.getWeekStr(selectDate)));
        } else {
            tvDefaultDay.setText(DateTimeUtil.getWeekStr(selectDate));
        }
        tvDisplayTime.setText(DateTimeUtil.getSpChineseMMDD(selectDate));
    }

    //切换城市
    private void changeCity() {
        if (judgeStartCity() && judgeEndCity()) {
            String temp = startCity;
            setStartCity(endCity);
            setEndCity(temp);
            onCityTimeSelectListener.changeData();
        }
    }

    //出发城市
    private boolean judgeStartCity() {

        if (StringUtils.isNullOrEmpty(startCity)) {
            ToastManager.getInstance().showToast(context, R.string.ll_view_please_select);
            return false;
        }
        return true;
    }

    //到达城市
    private boolean judgeEndCity() {
        if (StringUtils.isNullOrEmpty(endCity)) {
            ToastManager.getInstance().showToast(context, R.string.ll_view_please_select_end);
            return false;
        }
        return true;
    }

    public void setOnCityTimeSelectListener(OnCityTimeSelectListener onCityTimeSelectListener) {
        this.onCityTimeSelectListener = onCityTimeSelectListener;
    }

    /**
     * 设置出发城市
     *
     * @param startCity
     */
    public void setStartCity(String startCity) {
        this.startCity = startCity;
        tvSelectStart.setText(startCity);
        if (!StringUtils.isNullOrEmpty(endCity)){
            tvSelectEnd.setHint(context.getString(R.string.ll_view_please_select_end));
            tvSelectEnd.setText("");
        }

    }

    /**
     * 设置到达城市
     *
     * @param endCity
     */
    public void setEndCity(String endCity) {
        this.endCity = endCity;
        tvSelectEnd.setText(endCity);
    }

    //获取出发时间
    public String getStartDate(){
        return startDate;
    }

    public String getStartCity(){
        return tvSelectStart.getText().toString();
    }

    public String getEndCity(){
        return  tvSelectEnd.getText().toString();
    }
    /**
     * 设置 button 的文字
     * @param text
     */
    public void setButtonText(String text){
        button.setText(text);
    }

    /**
     * 设置背景
     * @param resId
     */
    public void setLinearlayoutBackground(int resId){
        llVq.setBackgroundResource(resId);
    }
    /**
     * 点击事件
     */
    public interface OnCityTimeSelectListener {

        void startCitySelect();

        void endCitySelect();

        void changeData();

        void timeSelect(String year, String month, String day);

        void queryLine(String startCity, String endCity, String date);
    }
}
