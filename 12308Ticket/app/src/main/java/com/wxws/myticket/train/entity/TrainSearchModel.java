package com.wxws.myticket.train.entity;

import java.io.Serializable;

/**
 * desc: 火车票出发城市保存
 * Date: 2016/9/2 18:19
 *
 * @auther: lixiangxiang
 */
public class TrainSearchModel implements Serializable {

    private String startCity;
    private String endCity;
    private String startCityCode;
    private String endCityCode;


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

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }
}
