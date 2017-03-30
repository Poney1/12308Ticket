package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 机场快线城市 
 * Date: 2016/11/11 16:35
 *
 * @auther: lixiangxiang
 */
public class AircraftCityEntity implements Serializable{


    /**
     * id : 12235
     * name : 宝安机场
     * secondName : 广州
     * pinyin : baoanjichang
     * simplePinyin : ba
     * initialChar : b
     */

    private String id;
    private String name;
    private String secondName;
    private String pinyin;
    private String simplePinyin;
    private String firstChar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSimplePinyin() {
        return simplePinyin;
    }

    public void setSimplePinyin(String simplePinyin) {
        this.simplePinyin = simplePinyin;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}
