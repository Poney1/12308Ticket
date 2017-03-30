package com.wxws.myticket.bus.entity;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * desc: 直通车出发到达
 * Date: 2016/11/15 17:33
 *
 * @auther: lixiangxiang
 */
public class POIEntity  implements Serializable{
    private String name;
    private String address;
    private String city;
    private LatLng mLatLng;

    private String district;//区

    private int select;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public POIEntity(String name, String address, String city, String district, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.district = district;
        this.mLatLng = latLng;
    }
}
