package com.wxws.myticket.bus.entity;

/**
 * desc:
 * Date: 2016-11-28 11:53
 *
 * @author jiangyan
 */
public class BaochePriceRequestPara {

    private String busId;
    private int busCount;

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public int getBusCount() {
        return busCount;
    }

    public void setBusCount(int busCount) {
        this.busCount = busCount;
    }
}