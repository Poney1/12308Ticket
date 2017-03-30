package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 显示订单详情
 * Date: 2016-07-07 15:15
 *
 * @auther: lixiangxiang
 */
public class FillOrderDescriptionEntity implements Serializable {

    private String fullPrice;

    private int  fullCount;

    private String halfPrice;

    private int halfCount ;

    private String coupon;//优惠

    private int insurance = -1;//保险

    private int  count ;//总 人数

    private int serviceFee;//服务费

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(String fullPrice) {
        this.fullPrice = fullPrice;
    }

    public int getFullCount() {
        return fullCount;
    }

    public void setFullCount(int fullCount) {
        this.fullCount = fullCount;
    }

    public String getHalfPrice() {
        return halfPrice;
    }

    public void setHalfPrice(String halfPrice) {
        this.halfPrice = halfPrice;
    }

    public int getHalfCount() {
        return halfCount;
    }

    public void setHalfCount(int halfCount) {
        this.halfCount = halfCount;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}
