package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 出发的实际城市 ，非历史 ，非热门 ，省份，学校 均用到这个对象
 * Date: 2016/8/4 10:59
 *
 * @auther: lixiangxiang
 */
public class CityDisplayEntity implements Serializable {

    private boolean isGroup ;

    private String city;

    private String cityId;//编码

    private String stationName;//站点名

    private String stationId;//站点id


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
