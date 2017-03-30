package com.wxws.myticket.bus.entity;

import java.util.List;

/**
 * desc: 港澳快线出发城市
 * Date: 2016/11/10 14:30
 *
 * @auther: lixiangxiang
 */
public class GangAoStartCityEntity {


    /**
     * id : 123
     * name : 香港
     * pinyin : xianggang
     * simplePinyin : xg
     * initialChar : x
     * isHot : 0
     * version : 1
     * status : 1
     * stationList : [{"stationId":"1111111","stationName":"尖沙咀"},{"stationId":"1111111","stationName":"海洋公园"}]
     */

    private String id;
    private String name;
    private String pinyin;
    private String simplePinyin;
    private String firstChar;
    private String isHot;
    private int version;
    private String status;
    private boolean isSelect;
    /**
     * stationId : 1111111
     * stationName : 尖沙咀
     */

    private List<StationListEntity> stationList;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSimplePinyin() {
        return simplePinyin;
    }

    public void setSimplePinyin(String simplePinyin) {
        this.simplePinyin = simplePinyin;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StationListEntity> getStationList() {
        return stationList;
    }

    public void setStationList(List<StationListEntity> stationList) {
        this.stationList = stationList;
    }


}
