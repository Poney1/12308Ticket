package com.wxws.myticket.bus.rxentity;

import com.wxws.myticket.common.rx.entity.BaseEvent;

/**
 * desc: 港澳快线城市 选择
 * Date: 2016/11/10 15:01
 *
 * @auther: lixiangxiang
 */
public class GangAoCityEvent extends BaseEvent {

    private String cityName;
    private String cityId;
    private String stationName;
    private String stationId;
    private String tag;


    public GangAoCityEvent(String tag,String cityName,String cityId, String stationName,  String stationId) {
        super(null);
        this.cityId = cityId;
        this.stationName = stationName;
        this.cityName = cityName;
        this.stationId = stationId;
        this.tag = tag;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getTag() {
        return tag;
    }
}
