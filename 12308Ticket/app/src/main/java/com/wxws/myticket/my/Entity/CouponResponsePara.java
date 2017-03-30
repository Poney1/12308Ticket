package com.wxws.myticket.my.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * desc:
 * Date: 2016-11-04 16:43
 *
 * @author jiangyan
 */
public class CouponResponsePara implements Serializable{

    private String code;
    private String name;
    private String userId;
    private String userMobile;
    private String createTime;
    private String expireTime;
    private String type;
    private String coinsThresholdAmt;
    private String coinsAmt;
    private String disctVal;
    private String disctMaxAmt;
    private String giveMode;
    private List<PlatForm> platform;
    private String status;//0：已使用 1：可使用 2：已过期 3：不适用

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoinsThresholdAmt() {
        return coinsThresholdAmt;
    }

    public void setCoinsThresholdAmt(String coinsThresholdAmt) {
        this.coinsThresholdAmt = coinsThresholdAmt;
    }

    public String getCoinsAmt() {
        return coinsAmt;
    }

    public void setCoinsAmt(String coinsAmt) {
        this.coinsAmt = coinsAmt;
    }

    public String getDisctVal() {
        return disctVal;
    }

    public void setDisctVal(String disctVal) {
        this.disctVal = disctVal;
    }

    public String getDisctMaxAmt() {
        return disctMaxAmt;
    }

    public void setDisctMaxAmt(String disctMaxAmt) {
        this.disctMaxAmt = disctMaxAmt;
    }

    public String getGiveMode() {
        return giveMode;
    }

    public void setGiveMode(String giveMode) {
        this.giveMode = giveMode;
    }

    public List<PlatForm> getPlatform() {
        return platform;
    }

    public void setPlatform(List<PlatForm> platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class PlatForm implements Serializable{
        private String platform;
        private int bizType;

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public int getBizType() {
            return bizType;
        }

        public void setBizType(int bizType) {
            this.bizType = bizType;
        }
    }

}