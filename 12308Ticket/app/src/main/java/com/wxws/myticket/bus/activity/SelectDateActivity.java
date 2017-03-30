package com.wxws.myticket.bus.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.interfaces.OnDaySelectListener;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.widgets.view.TwelveMonthCalendar;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.DateTimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.http.PUT;

/**
 * desc: 日期选择
 * Date: 2016/10/19 13:45
 *
 * @auther: lixiangxiang
 */
public class SelectDateActivity extends BaseActivity {

    private TextView curMonth;
    private TextView nextMonth;
    private TextView thirdMonth;
    private TwelveMonthCalendar currentCalendar ,nextCalender,thirdCalendar ;

    private String year ,month,day;
    private int predate;
    private String tag;
    public static int defaultDate = 60 ;//默认60 天 预售
    private Date date;
    private HashMap<String, String> hashMap;

    /**
     * 标记各个业务的日期选择
     */
    public static String BUSTRADITIONALTAG = "busTraditionalTag";//传统汽车票tag
    public static String BUSDIRECTTAG = "busDirectTag";//直通车tag
    public static String BUSGANGAOTAG = "busGangaoTag";//港澳tag
    public static String TrainTAG = "TrainTAG";//港澳tag

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        initView();

        initData();
    }

    private void initData() {

        barTitle.setText("选择日期");

        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        day = getIntent().getStringExtra("day");
        predate = getIntent().getIntExtra("Predate", defaultDate);
        tag = getIntent().getStringExtra("tag");

        //除去第一位的0
        month = DateTimeUtil.removeFirstZero(month);
        day = DateTimeUtil.removeFirstZero(day);

        date = new Date();
        hashMap = new HashMap<>();
        for (int i = 0; i < predate; i++) {
            getDateAfter(date, i);
        }
        currentCalendar.setDateData(date, hashMap);
        curMonth.setText(currentCalendar.getYear() + "年" + currentCalendar.getMonth() + "月");

        nextCalender.setDateData(nextMonthFirstDate(date), hashMap);
        nextMonth.setText(nextCalender.getYear() + "年" + nextCalender.getMonth() + "月");
        nextCalender.setOnDaySelectListener(new OnDaySelectListener() {

            @Override
            public void onDaySelectListener(String date) {
                String key = nextCalender.getYear() + nextCalender.getMonth() + date;
                if (hashMap.containsKey(key)) {
                    currentCalendar.setSelectItem("-1");
                    currentCalendar.gridViewAdapter.notifyDataSetChanged();
                    thirdCalendar.setSelectItem("-1");
                    thirdCalendar.gridViewAdapter.notifyDataSetChanged();

                    nextCalender.setSelectItem(date);
                    nextCalender.gridViewAdapter.notifyDataSetChanged();

                    RxBus.getInstance().post(new SelectDayEvent(nextCalender.getYear(),nextCalender.getMonth(),date,tag));

                    finish();
                }

            }
        });

        thirdCalendar.setDateData(thirdMonthFirstDate(date), hashMap);
        thirdMonth.setText(thirdCalendar.getYear() + "年" + thirdCalendar.getMonth() + "月");
        thirdCalendar.setOnDaySelectListener(new OnDaySelectListener() {

            @Override
            public void onDaySelectListener(String date) {
                String key = thirdCalendar.getYear() + thirdCalendar.getMonth() + date;
                if (hashMap.containsKey(key)) {
                    currentCalendar.setSelectItem("-1");
                    currentCalendar.gridViewAdapter.notifyDataSetChanged();

                    thirdCalendar.setSelectItem(date);
                    thirdCalendar.gridViewAdapter.notifyDataSetChanged();

                    RxBus.getInstance().post(new SelectDayEvent(thirdCalendar.getYear(),thirdCalendar.getMonth(),date,tag));

                    finish();
                }

            }
        });

        currentCalendar.setOnDaySelectListener(new OnDaySelectListener() {

            @Override
            public void onDaySelectListener(String date) {
                String key = currentCalendar.getYear() + currentCalendar.getMonth() + date;
                if (hashMap.containsKey(key)) {
                    nextCalender.setSelectItem("-1");
                    nextCalender.gridViewAdapter.notifyDataSetChanged();
                    thirdCalendar.setSelectItem("-1");
                    thirdCalendar.gridViewAdapter.notifyDataSetChanged();

                    currentCalendar.setSelectItem(date);
                    currentCalendar.gridViewAdapter.notifyDataSetChanged();

                    RxBus.getInstance().post(new SelectDayEvent(currentCalendar.getYear(),currentCalendar.getMonth(),date,tag));
                    finish();
                }
            }
        });

        if (year!=null&&year.equals(currentCalendar.getYear()) &&month!=null&& month.equals(currentCalendar.getMonth())) {
            currentCalendar.setSelectItem(day);
            currentCalendar.gridViewAdapter.notifyDataSetChanged();

            nextCalender.setSelectItem("-1");
            nextCalender.gridViewAdapter.notifyDataSetChanged();

            thirdCalendar.setSelectItem("-1");
            thirdCalendar.gridViewAdapter.notifyDataSetChanged();
        }
        if (year!=null&&year.equals(nextCalender.getYear()) && month!=null&&month.equals(nextCalender.getMonth())) {
            nextCalender.setSelectItem(day);
            nextCalender.gridViewAdapter.notifyDataSetChanged();

            currentCalendar.setSelectItem("-1");
            currentCalendar.gridViewAdapter.notifyDataSetChanged();

            thirdCalendar.setSelectItem("-1");
            thirdCalendar.gridViewAdapter.notifyDataSetChanged();
        }

        if (year!=null&&year.equals(thirdCalendar.getYear()) && month!=null&&month.equals(thirdCalendar.getMonth())) {
            thirdCalendar.setSelectItem(day);
            thirdCalendar.gridViewAdapter.notifyDataSetChanged();

            currentCalendar.setSelectItem("-1");
            currentCalendar.gridViewAdapter.notifyDataSetChanged();

            nextCalender.setSelectItem("-1");
            nextCalender.gridViewAdapter.notifyDataSetChanged();
        }

    }

    private void initView() {
        bindTitleViews();

        curMonth = (TextView) findViewById(R.id.cur_TV);
        nextMonth = (TextView) findViewById(R.id.next_TV);
        thirdMonth = (TextView) findViewById(R.id.third_TV);
        currentCalendar = (TwelveMonthCalendar) findViewById(R.id.cur_calendar);
        nextCalender = (TwelveMonthCalendar) findViewById(R.id.next_calendar);
        thirdCalendar = (TwelveMonthCalendar) findViewById(R.id.third_calendar);
    }

    public  Date thirdMonthFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 2);
        return calendar.getTime();
    }

    private  Date nextMonthFirstDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    private void getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        String v = now.get(Calendar.YEAR) + "" + (now.get(Calendar.MONTH) + 1) + "" + now.get(Calendar.DAY_OF_MONTH);
        if (day == 0) {
            hashMap.put(v, "true");
        } else {
            hashMap.put(v, "false");
        }
    }

}
