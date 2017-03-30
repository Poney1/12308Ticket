package com.wxws.myticket.bus.rxentity;

import com.wxws.myticket.common.rx.entity.BaseEvent;

/**
 * desc: 日期选择
 * Date: 2016/10/24 14:45
 *
 * @auther: lixiangxiang
 */
public class SelectDayEvent extends BaseEvent {

    private String year;
    private String month;
    private String day;
    private String tag;

    public SelectDayEvent(String year,String month,String day,String tag){
        super(null);
        this.year = year;
        this.month = month ;
        this.day = day ;
        this.tag = tag;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getTag() {
        return tag;
    }
}
