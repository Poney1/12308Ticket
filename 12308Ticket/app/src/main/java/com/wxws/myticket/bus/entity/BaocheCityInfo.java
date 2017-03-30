package com.wxws.myticket.bus.entity;

/**
 * desc:
 * Date: 2016-11-18 16:02
 *
 * @author jiangyan
 */
public class BaocheCityInfo {
    private boolean isGroup;
    private String groupName;
    private String cityName;
    private long cityCode;
    private int preDate;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getCityCode() {
        return cityCode;
    }

    public void setCityCode(long cityCode) {
        this.cityCode = cityCode;
    }

    public int getPreDate() {
        return preDate;
    }

    public void setPreDate(int preDate) {
        this.preDate = preDate;
    }
}