package com.wxws.myticket.train.entity;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * desc:
 * Date: 2016-08-01 14:44
 *
 * @auther: lixiangxiang
 */
public class TrainStartCityModel implements Parcelable {

    /**
     * id : 1
     * name : 北京北
     * code : VAP
     * pinyin : beijingbei
     * simplePinyin : bjb
     * isHot : 0
     * status : 1
     * version : 1
     * firstChar
     */

    private long id;
    private String name;
    private String code;
    private String pinyin;
    private String simplePinyin;
    private String isHot;
    private String status;
    private int version;

    //Todo 修改数据库
    private int isGroup;//是否大写字母
    private String firstChar;//首字母 大写

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public static Creator<TrainStartCityModel> getCREATOR() {
        return CREATOR;
    }

    public TrainStartCityModel() {
    }
    public TrainStartCityModel(Long id) {
        this.id = id;
    }

    public TrainStartCityModel(Long id, String name, String code, String pinyin, String simplePinyin, String isHot, String status, Integer version, String firstChar) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.pinyin = pinyin;
        this.simplePinyin = simplePinyin;
        this.isHot = isHot;
        this.status = status;
        this.version = version;
        this.firstChar = firstChar;
    }

    protected TrainStartCityModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        code = in.readString();
        pinyin = in.readString();
        simplePinyin = in.readString();
        isHot = in.readString();
        status = in.readString();
        version = in.readInt();
        firstChar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(pinyin);
        dest.writeString(simplePinyin);
        dest.writeString(isHot);
        dest.writeString(status);
        dest.writeInt(version);
        dest.writeString(firstChar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainStartCityModel> CREATOR = new Creator<TrainStartCityModel>() {
        @Override
        public TrainStartCityModel createFromParcel(Parcel in) {
            return new TrainStartCityModel(in);
        }

        @Override
        public TrainStartCityModel[] newArray(int size) {
            return new TrainStartCityModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
