package com.wxws.myticket.train.entity;


import java.io.Serializable;

/**
 * desc: 省对象
 * Date: 2016/8/9 11:11
 *
 * @auther: lixiangxiang
 */
public class ProvinceModel implements Serializable {


    /**
     * id : 110000
     * name : 北京市
     * shortName : 北京
     * pinyin	string	是	省份拼音
     * firstChar	string	是	首字母
     */

    private String id;
    private String name;
    private String shortName;
    private String firstChar;
    private String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
