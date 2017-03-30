package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 历史搜索
 * Date: 2016/10/24 11:18
 *
 * @auther: lixiangxiang
 */
public class BusHistory implements Serializable {

    private String startCityId;

    private String endCity;

    private String endCityId;



    private String startCity;//出发城市

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartCityId() {
        return startCityId;
    }

    public void setStartCityId(String startCityId) {
        this.startCityId = startCityId;
    }

    public String getEndCityId() {
        return endCityId;
    }

    public void setEndCityId(String endCityId) {
        this.endCityId = endCityId;
    }


    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

}
