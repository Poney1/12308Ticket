package com.wxws.myticket.train.entity;


import java.io.Serializable;

/**
 * desc: 学校描述
 * Date: 2016/8/9 11:12
 *
 * @auther: lixiangxiang
 */
public class SchoolModel implements Serializable {
    /**
     * provinceId : 110000
     * provinceName : 北京
     * schoolCode : 10003
     * schoolName : 清华大学
     * schoolPY:
     * private String firstChar;
     * private String pinyin;
     */

    private String provinceId;
    private String provinceName;
    private String schoolCode;
    private String schoolName;
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

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
