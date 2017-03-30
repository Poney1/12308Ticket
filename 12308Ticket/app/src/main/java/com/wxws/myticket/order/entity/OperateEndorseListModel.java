package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 15:30
 *
 * @author jiangyan
 */
public class OperateEndorseListModel {
    /**
     * amount : 0
     * changeOrderList : null
     * fromStationName : 武穴
     * fromTime : 2016-09-21 12:05:00
     * insuranceAmount : 0
     * operationList : null
     * orderId : H2271063
     * orderNumber : E869823746
     * orderStatus : 36
     * refundAmount : 0
     * toStationName : 九江
     * toTime : 2016-09-21 13:28:00
     */

    private int amount;
    private Object changeOrderList;
    private String fromStationName;
    private String fromTime;
    private int insuranceAmount;
    private String orderId;
    private String orderNumber;
    private int orderStatus;
    private int refundAmount;
    private String toStationName;
    private String toTime;
    private List<OperatTicketListModel> ticketList;
    private List<OperateHistoryModel> operationList;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Object getChangeOrderList() {
        return changeOrderList;
    }

    public void setChangeOrderList(Object changeOrderList) {
        this.changeOrderList = changeOrderList;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public int getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(int insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public List<OperatTicketListModel> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<OperatTicketListModel> ticketList) {
        this.ticketList = ticketList;
    }

    public List<OperateHistoryModel> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<OperateHistoryModel> operationList) {
        this.operationList = operationList;
    }
}