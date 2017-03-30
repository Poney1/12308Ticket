package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-03 18:16
 *
 * @author jiangyan
 */
// TODO 包车订单需要的entity，后续订单详情修改后，数据可能改变
public class RefundOrderEntity {
    /**
     * id : 93,
     * orderNo : "bus1606234731230",
     * refundDesc : "",
     * refundFee : 0,
     * refundRate : 10,
     * refundReason : "出行有变,更换其他车次",
     * refundTime : "2016-06-23 14:05:55",
     * refundType : "1",
     * userId : 15183058
     *
     */


    private int id;
    private String orderNo;
    private String refundDesc;
    private int refundFee;
    private int refundRate;
    private String refundReason;
    private String refundTime;
    private String refundType;
    private long userId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public int getRefundRate() {
        return refundRate;
    }

    public void setRefundRate(int refundRate) {
        this.refundRate = refundRate;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}