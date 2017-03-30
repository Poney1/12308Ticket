package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 汽车票保险
 * Date: 2016/11/23 16:53
 *
 * @auther: lixiangxiang
 */
public class BusInsuranceEntity  implements Serializable {

   private String insuranceId ;//	保险编号
   private String  insuranceName;	//	保险名称
   private int money	;//	保险金额
   private String insuranceDesc	;//	保险简单描述
   private String insuranceTerms	;//	保险责任条款
   private String insuranceCompanyName	;//	公司名称
   private String type	;//保险类型，0：成人，1儿童
   private String isRecommend	;//	是否推荐0：否1：是

   private String  isForce	;	//是	是否强制赠送保险 赠送保险为免费保险，价格为0的保险的免费保险

    private boolean isSelect; //是否选中
    public String getIsForce() {
        return isForce;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

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

    public String getInsuranceDesc() {
        return insuranceDesc;
    }

    public void setInsuranceDesc(String insuranceDesc) {
        this.insuranceDesc = insuranceDesc;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
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

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }
}
