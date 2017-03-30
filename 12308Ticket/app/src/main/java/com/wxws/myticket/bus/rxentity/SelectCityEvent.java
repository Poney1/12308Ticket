package com.wxws.myticket.bus.rxentity;

import com.wxws.myticket.common.rx.entity.BaseEvent;

/**
 * desc: 火车票选择出发城市 事件
 * Date:
 *
 * @auther: lixiangxiang
 */
public class SelectCityEvent extends BaseEvent {

    private String cityName;
    private String origin;
    private String cityId;


    public SelectCityEvent(String cityName, String origin, String cityId) {
        super(null);
        this.cityName = cityName;
        this.origin = origin;
        this.cityId = cityId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
