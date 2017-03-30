package com.wxws.myticket.train.entity;



/**
 * desc:
 * Date: 2016-08-12 15:21
 *
 * @auther: jiangyan
 */
public class NewPassengerInfoModel {

    @Override
    public String toString() {
        return "NewPassengerInfoModel{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", adultname='" + adultname + '\'' +
                ", certType=" + certType +
                ", certNo='" + certNo + '\'' +
                '}';
    }

    private String id;
    private String passengerId;
    private int type;//乘客类型
    private String adultname;
    private String certType;
    private String certNo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAdultname() {
        return adultname;
    }

    public void setAdultname(String adultname) {
        this.adultname = adultname;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
}