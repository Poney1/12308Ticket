package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-16 10:49
 *
 * @author jiangyan
 */
public class ApplyRefundRequestPara {

    private String orderId;
    private String ticketIdList;
    private String returnTicketWay;
    private String refundType;
    private String refundReason;
    private String refundMoney;
    private String ticketCount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTicketIdList() {
        return ticketIdList;
    }

    public void setTicketIdList(String ticketIdList) {
        this.ticketIdList = ticketIdList;
    }

    public String getReturnTicketWay() {
        return returnTicketWay;
    }

    public void setReturnTicketWay(String returnTicketWay) {
        this.returnTicketWay = returnTicketWay;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(String refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(String ticketCount) {
        this.ticketCount = ticketCount;
    }
}