package com.wxws.myticket.train.entity;

import java.io.Serializable;

/**
 * desc: 火车历史搜索线路
 * Date: 2016-08-01 11:05
 *
 * @auther: lixiangxiang
 */
public class TrainLineSaveEntity implements Serializable {

    private String startCity;

    private String endCity;

    private String startCode;

    private String endCode;

    public String getStartCode() {
        return startCode;
    }

    public void setStartCode(String startCode) {
        this.startCode = startCode;
    }

    public String getEndCode() {
        return endCode;
    }

    public void setEndCode(String endCode) {
        this.endCode = endCode;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }
}
