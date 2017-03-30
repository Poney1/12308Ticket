package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 定位的 信息
 * Date: 2016/11/29 13:52
 *
 * @auther: lixiangxiang
 */
public class LocationEntity implements Serializable {

    private String city;

    private String addr;

    private double  lng;

    private double lat;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
