package com.wxws.myticket.bus.entity;

/**
 * desc: 直通车到达城市
 * Date: 2016/11/15 17:34
 *
 * @auther: lixiangxiang
 */
public class CityLineEndEntity implements Comparable<CityLineEndEntity> {

    private String id;
    private String name;
    private String pinyin;
    private String isHot;
    private String simplePinyin;
    private String firstChar;

    public String getId() {
        return id;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
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

    @Override
    public int compareTo(CityLineEndEntity o) {
        return this.pinyin.compareTo(o.pinyin);
    }
}
