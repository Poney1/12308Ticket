package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-02 10:17
 *
 * @author jiangyan
 */
public class TrainTicketInfoModel {
    private String name;
    private int ticketStatus;
    private int refundStatus;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }
}