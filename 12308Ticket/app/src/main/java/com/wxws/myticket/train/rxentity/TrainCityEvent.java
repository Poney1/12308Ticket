package com.wxws.myticket.train.rxentity;

/**
 * desc: 火车票选择出发城市 事件
 * Date:
 *
 * @auther: lixiangxiang
 */
public class TrainCityEvent {

    private String city;
    private String isSelect;
    private String code;

    public String getCode() {
        return code;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public String getCity() {
        return city;
    }

    public TrainCityEvent(String isSelect, String city, String code){
        this.city = city;
        this.isSelect = isSelect ;
        this.code = code ;
    }
}
