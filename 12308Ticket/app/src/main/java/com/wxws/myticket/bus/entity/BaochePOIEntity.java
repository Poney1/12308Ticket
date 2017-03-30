package com.wxws.myticket.bus.entity;

import com.baidu.mapapi.model.LatLng;

/**
 * desc: 我要包车POI
 * Date: 2016-11-18 15:04
 *
 * @author jiangyan
 */
public class BaochePOIEntity {

    private String key;
    private String address;
    private String city;
    private LatLng mLatLng;

    private String district;//区

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public BaochePOIEntity(String key, String address, String city, String district,LatLng latLng) {
        this.key = key;
        this.address = address;
        this.city = city;
        this.district = district;
        mLatLng = latLng;
    }
}