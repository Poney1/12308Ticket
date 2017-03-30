package com.wxws.myticket.bus.entity;

import java.util.List;

/**
 * desc: 出发到达车站
 * Date: 2016/11/29 10:54
 *
 * @auther: lixiangxiang
 */
public class BusStationListEntity {


    /**
     * id : 123284136822
     * name : 深圳
     * pinyin : shenzhen
     * simplePinyin : sz
     * initialChar : s
     * stationList : [{"stationId":"11123611121","stationName":"深圳宝安汽车站"},{"stationId":"1175751117131","stationName":"深圳银湖汽车站"}]
     */

    private String id;
    private String name;
    private String pinyin;
    private String simplePinyin;
    private String firstChar;
    /**
     * stationId : 11123611121
     * stationName : 深圳宝安汽车站
     */

    private List<StationListBean> stationList;

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

    public List<StationListBean> getStationList() {
        return stationList;
    }

    public void setStationList(List<StationListBean> stationList) {
        this.stationList = stationList;
    }

    public static class StationListBean {
        private String stationId;
        private String stationName;

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
    }
}
