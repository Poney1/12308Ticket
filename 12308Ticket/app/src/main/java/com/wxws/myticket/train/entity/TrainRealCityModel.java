package com.wxws.myticket.train.entity;

import java.io.Serializable;

/**
 * desc: 出发的实际城市 ，非历史 ，非热门 ，省份，学校 均用到这个对象
 * Date: 2016/8/4 10:59
 *
 * @auther: lixiangxiang
 */
public class TrainRealCityModel implements Serializable {

    private boolean isGroup ;

    private String city;

    private String code;//编码

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
