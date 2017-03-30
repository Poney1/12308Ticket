package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-14 18:31
 *
 * @author jiangyan
 */
public class BusMapResponsePara {

    private String curStationId;
    private List<StopoverStation> stopoverStationList;

    public String getCurStationId() {
        return curStationId;
    }

    public void setCurStationId(String curStationId) {
        this.curStationId = curStationId;
    }

    public List<StopoverStation> getStopoverStationList() {
        return stopoverStationList;
    }

    public void setStopoverStationList(List<StopoverStation> stopoverStationList) {
        this.stopoverStationList = stopoverStationList;
    }


}