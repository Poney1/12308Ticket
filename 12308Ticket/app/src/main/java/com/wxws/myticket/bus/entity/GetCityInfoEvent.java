package com.wxws.myticket.bus.entity;

/**
 * desc:
 * Date: 2016-11-18 16:08
 *
 * @author jiangyan
 */
public class GetCityInfoEvent {
    private BaocheCityInfo cityInfo;
    private int titleId;
    private int schedule_type;

    public GetCityInfoEvent(BaocheCityInfo cityInfo, int titleId, int schedule_type) {
        this.cityInfo = cityInfo;
        this.titleId = titleId;
        this.schedule_type = schedule_type;
    }

    public BaocheCityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(BaocheCityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(int schedule_type) {
        this.schedule_type = schedule_type;
    }
}