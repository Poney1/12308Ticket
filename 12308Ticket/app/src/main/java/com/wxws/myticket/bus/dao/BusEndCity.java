package com.wxws.myticket.bus.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * desc: 汽车票到达城市
 * Date: 2016/10/31 18:20
 *
 * @auther: lixiangxiang
 */
public class BusEndCity implements Parcelable ,Comparable<BusEndCity>{

    /**
     * id : 123
     * initialChar : s
     * isHot : 0
     * name : 深圳
     * pinyin : shenzhen
     * simplePinyin : sz
     * status : 1
     * version : 1
     * "fromCityId": "1223"
     */

    private String id;
    private String name;
    private String pinyin;
    private String simplePinyin;
    private String isHot;
    private String status;
    private Integer version;
    private String firstChar;
    private String fromCityId;

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getFromCityId() {
        return fromCityId;
    }

    public void setFromCityId(String fromCityId) {
        this.fromCityId = fromCityId;
    }

    public static Creator<BusEndCity> getCREATOR() {
        return CREATOR;
    }

    public BusEndCity(){
    }

    public BusEndCity(String id, String name, String pinyin, String simplePinyin, String isHot, String status, Integer version, String firstChar, String fromCityId) {
        this.id = id;
        this.name = name;
        this.pinyin = pinyin;
        this.simplePinyin = simplePinyin;
        this.isHot = isHot;
        this.status = status;
        this.version = version;
        this.firstChar = firstChar;
        this.fromCityId = fromCityId;
    }

    protected BusEndCity(Parcel in) {
        id = in.readString();
        firstChar = in.readString();
        isHot = in.readString();
        name = in.readString();
        pinyin = in.readString();
        simplePinyin = in.readString();
        status = in.readString();
        version = in.readInt();
    }

    public static final Creator<BusEndCity> CREATOR = new Creator<BusEndCity>() {
        @Override
        public BusEndCity createFromParcel(Parcel in) {
            return new BusEndCity(in);
        }

        @Override
        public BusEndCity[] newArray(int size) {
            return new BusEndCity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstChar);
        dest.writeString(isHot);
        dest.writeString(name);
        dest.writeString(pinyin);
        dest.writeString(simplePinyin);
        dest.writeString(status);
        dest.writeValue(version);
        dest.writeString(fromCityId);
    }

    @Override
    public int compareTo(BusEndCity o) {
        return this.pinyin.compareTo(o.pinyin);
    }
}
