package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-02 15:29
 *
 * @author jiangyan
 */
public class OperatTicketListModel {
    /**
     * name : 黄双梅
     * orderDetailId : H2271038-1
     * orderStatus : 48
     * ticketStatus : 40
     */

    private String name;
    private String orderDetailId;
    private int orderStatus;
    private int ticketStatus;

    private String fromtime;
    private String fromstation;
    private String totime;
    private String tostation;
    private int type;// 0位一般票，1位改签票

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFromstation() {
        return fromstation;
    }

    public void setFromstation(String fromstation) {
        this.fromstation = fromstation;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getTostation() {
        return tostation;
    }

    public void setTostation(String tostation) {
        this.tostation = tostation;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}