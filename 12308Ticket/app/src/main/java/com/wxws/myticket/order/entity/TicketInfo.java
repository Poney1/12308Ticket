package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-01 16:28
 *
 * @author jiangyan
 */
public class TicketInfo {

    public TicketInfo() {
    }

    public TicketInfo(String name, String ticketStatus, String refundStatus) {
        this.name = name;
        this.ticketStatus = ticketStatus;
        this.refundStatus = refundStatus;
    }

    private String name;
    private String ticketStatus;
    private String refundStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
}