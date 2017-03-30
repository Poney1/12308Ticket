package com.wxws.myticket.bus.entity;

/**
 * User: 李俊峰
 * Date: 2015-12-17
 * Time: 13:50
 */
public class GetMapCityEvent {
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public GetMapCityEvent(String cityName) {
        this.cityName = cityName;
    }
}
