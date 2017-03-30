package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-15 16:14
 *
 * @author jiangyan
 */
public class BusRefundModel {

    public BusRefundModel(String ticketId, String name, String phone, String status, boolean choosed) {
        this.ticketId = ticketId;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.choosed = choosed;
    }

    private String ticketId;
    private String name;
    private String phone;
    private String status;
    private boolean choosed;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }
}