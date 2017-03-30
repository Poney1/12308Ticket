package com.wxws.myticket.train.entity;


/**
 * desc:
 * Date: 2016-08-12 09:51
 *
 * @auther: jiangyan
 */
public class TicketListModel {
    private String id;// 成人乘客的联系人id
    private int ticketType; // 成人票，儿童票等，如果为儿童票，需要显示下面的childname及birthday
    private String childName;
    private String gender;
    private String birthday;
    private String seatType; // 一等座，二等座
    private String insuranceId; // 保险id

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}