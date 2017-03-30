package com.wxws.myticket.bus.rxentity;

import com.wxws.myticket.common.rx.entity.BaseEvent;

/**
 * desc: 选择建议搜索城市
 * Date: 2016/11/15 16:43
 *
 * @auther: lixiangxiang
 */
public class CitySelectEvent extends BaseEvent {

    private String city;

    public CitySelectEvent(String city) {
        super(null);
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
