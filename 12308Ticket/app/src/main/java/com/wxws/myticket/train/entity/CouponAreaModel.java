package com.wxws.myticket.train.entity;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * desc: 优惠区间
 * Date: 2016/8/16 16:37
 *
 * @auther: lixiangxiang
 */
public class CouponAreaModel implements Parcelable,Comparable<CouponAreaModel> {


    /**
     * code : 110000
     * name : 北京市
     * pinyin : beijing
     * simplePinyin : bj
     * firstChar : b
     * version : 1
     * status : 1
     */

    private String code;
    private String name;
    private String pinyin;
    private String simplePinyin;
    private String firstChar;
    private String version;
    private int status;
    private long id;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CouponAreaModel> CREATOR = new Creator<CouponAreaModel>() {
        @Override
        public CouponAreaModel createFromParcel(Parcel in) {
            return new CouponAreaModel(in);
        }

        @Override
        public CouponAreaModel[] newArray(int size) {
            return new CouponAreaModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CouponAreaModel(long id , String code, String name, String pinyin, String simplePinyin, String firstChar, String version, int status) {
        this.code = code;
        this.name = name;
        this.pinyin = pinyin;
        this.simplePinyin = simplePinyin;
        this.firstChar = firstChar;
        this.version = version;
        this.status = status;
        this.id = id;
    }

    public CouponAreaModel(){

    }

    public CouponAreaModel(Long id) {
        this.id = id;
    }
    protected CouponAreaModel(Parcel in) {
        code = in.readString();
        name = in.readString();
        pinyin = in.readString();
        simplePinyin = in.readString();
        firstChar = in.readString();
        version = in.readString();
        status = in.readInt();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(pinyin);
        dest.writeString(simplePinyin);
        dest.writeString(firstChar);
        dest.writeString(version);
        dest.writeInt(status);
        dest.writeLong(id);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int compareTo(CouponAreaModel another) {
        int index = this.getPinyin().compareTo(another.getPinyin());
        return  index;
    }
}
