package com.wxws.myticket.bus.entity;

/**
 * desc:
 * Date: 2016/11/9 14:17
 *
 * @auther: lixiangxiang
 */
public class StationDescEntity  {

    /*stationId	string	是	站点编码
    stationName	string	是	站点名称
    sortNum	string	是	站点排序 一般按照添加顺序递增
    longitude	string	否	经度
    latitude	string	否	纬度
    contactMobile	string	否	站点联系电话
    address	string	是	站点地址
    distance	string	否	距离“我” 多远*/

    private String  stationName;
    private String  address;
    private String  contactMobile;
    private String  distance;


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
