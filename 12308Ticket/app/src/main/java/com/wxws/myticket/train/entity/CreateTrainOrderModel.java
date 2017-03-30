package com.wxws.myticket.train.entity;


import java.util.List;

/**
 * desc:
 * Date: 2016-08-01 14:47
 *
 * @auther: jiangyan
 */
public class CreateTrainOrderModel {

    /**
     * startDate,乘车日期
     * fromStationCode，出发站简码
     * toStationCode ，到达站简码
     * trainCode ，车次
     * mobile，联系手机
     * couponId，优惠券编号
     * acceptStanding，是否接受无座
     * ticketList，乘客信息list
     * trainNo,
     */
    private String startDate;
    private String fromStationCode;
    private String toStationCode;
    private String trainCode;
    private String mobile;
    private String couponId;
    private boolean acceptStanding;
    private String trainNo;
    private List<TicketListModel> ticketList;


    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFromStationCode() {
        return fromStationCode;
    }

    public void setFromStationCode(String fromStationCode) {
        this.fromStationCode = fromStationCode;
    }

    public String getToStationCode() {
        return toStationCode;
    }

    public void setToStationCode(String toStationCode) {
        this.toStationCode = toStationCode;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public boolean isAcceptStanding() {
        return acceptStanding;
    }

    public void setAcceptStanding(boolean acceptStanding) {
        this.acceptStanding = acceptStanding;
    }

    public List<TicketListModel> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TicketListModel> ticketList) {
        this.ticketList = ticketList;
    }
}
