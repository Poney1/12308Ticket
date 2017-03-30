package com.wxws.myticket.order.entity;

/**
 * desc: 直通车地图信息 list
 * Date: 2016-11-15 10:20
 *
 * @author jiangyan
 */
public class StopoverStation {

    public StopoverStation(String stationName, String type, String latitude, String longitude) {
        this.stationName = stationName;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private String stationId;
    private String stationName;
    private String nameAlias;
    private String startTime;
    private String type;
    private String sortNum;
    private String longitude;
    private String latitude;
    private String contactName;
    private String contactMobile;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }
}