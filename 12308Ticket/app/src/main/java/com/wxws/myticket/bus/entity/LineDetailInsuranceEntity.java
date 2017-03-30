package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 线路保险  列表
 * Date: 2016/12/8 15:29
 *
 * @auther: lixiangxiang
 */
public class LineDetailInsuranceEntity  implements Serializable{

    private String insuranceId;
    private String insuranceName;
    private int money;
    private String insuranceDesc;
    private String insuranceTerms;
    private String insuranceCompanyName;
    private String type;

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getInsuranceDesc() {
        return insuranceDesc;
    }

    public void setInsuranceDesc(String insuranceDesc) {
        this.insuranceDesc = insuranceDesc;
    }

    public String getInsuranceTerms() {
        return insuranceTerms;
    }

    public void setInsuranceTerms(String insuranceTerms) {
        this.insuranceTerms = insuranceTerms;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
