package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-02 14:07
 *
 * @author jiangyan
 */
public class EndorseTicketModel {
    private String id;
    private String seatType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    @Override
    public String toString() {
        return "EndorseTicketModel{" +
                "id='" + id + '\'' +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}