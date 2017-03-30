package com.wxws.myticket.train.entity;


import java.io.Serializable;

/**
 * desc:
 * Date: 2016-08-04 10:41
 *
 * @auther: jiangyan
 */
public class TrainInsuranceModel implements Serializable {

    /**
    {
        "insuranceCompanyName": "航天华有",
            "insuranceDesc": "交通工具意外伤害80万元，意外医疗0.1万元",
            "insuranceId": "1804019800540198",
            "insuranceName": "火车意外险C款",
            "insuranceTerms": "1.保险责任金额：0-10周岁交通工具意外伤害20万，10-17周岁交通工具意外伤害50万，成人交通工具意外伤害80万元，意外医疗0.1万元   \r\n2.投保范围：30天-80周岁身体健康者，均可以作为被保险人，由本人或对其具有保险\r\n3.利益的其他人作为投保人向本公司投保本保险。\r\n4.证件类型：身份证、护照等有效证件\r\n5.受益人：法定\r\n6.保险承保范围：全国，当次列车\r\n7.保险生效时间：以投保选定的时间开始生效\r\n8.退保规则:保险责任正式开始之前可全额退保\r\n9.其他保险说明：如果保险产品内容或购买后有疑问，可拨打合众人寿客服热线95515，将有专人为您解答。\r\n10.撤单条件：您可在本保险生效日之前申请撤单，撤单成功后保险费将划转到原交费帐户；\r\n保险咨询、理赔报案：\r\n合众人寿保险公司客服电话95515或登录http://www.unionlife.com.cn/",
            "isFree": "0",
            "isRecommend": "1",
            "money": 1000,
            "type": "0"
    }
*/
    private String insuranceId;
    private String insuranceName;
    private String insuranceDesc;
    private String insuranceTerms;
    private String insuranceCompanyName;
    private String insuranceCompanyId;
    private int money;
    private String type;
    private String status;
    private int validityTime;
    private boolean flag_checkbox = false;
    /**
     * isFree : 0
     * isRecommend : 1
     */

    private String isFree;
    private String isRecommend;

    public boolean isFlag_checkbox() {
        return flag_checkbox;
    }

    public void setFlag_checkbox(boolean flag_checkbox) {
        this.flag_checkbox = flag_checkbox;
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

    public String getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(String insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(int validityTime) {
        this.validityTime = validityTime;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }
}