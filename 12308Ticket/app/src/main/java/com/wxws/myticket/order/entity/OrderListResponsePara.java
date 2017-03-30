package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-01 16:25
 *
 * @author jiangyan
 */
public class OrderListResponsePara {

    private String orderId;//订单id
    private String orderStatus;//订单状态
    private String fromCityId;//出发城市Id
    private String fromCityName;//出发城市名
    private String toCityId;//到达城市Id
    private String toCityName;//到达城市名
    private String fromStationId;//出发站点Id
    private String fromStationName;//出发站点名
    private String toStationId;//到达站点Id
    private String toStationName;//到达站点名
    private String isGoBack;//是否往返（包车）0：否 1：是
    private String vehicleType;//车辆类型（约租车）
    private String checi;//车次
    private List<TicketInfo> ticketInfos;//票信息
    private String orderMoney;//订单金额
    private String createTime;//订单生成时间
    private String payTime;//订单支付成功时间
    private String departDate;//出发日期
    private String departTime;//出发时间
    private String toTime;//到达时间
    private String bizType;//业务类型
    private String isStartStation;//是否始发站0：否 1：是

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFromCityId() {
        return fromCityId;
    }

    public void setFromCityId(String fromCityId) {
        this.fromCityId = fromCityId;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    public String getToCityId() {
        return toCityId;
    }

    public void setToCityId(String toCityId) {
        this.toCityId = toCityId;
    }

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getFromStationId() {
        return fromStationId;
    }

    public void setFromStationId(String fromStationId) {
        this.fromStationId = fromStationId;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getToStationId() {
        return toStationId;
    }

    public void setToStationId(String toStationId) {
        this.toStationId = toStationId;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getIsGoBack() {
        return isGoBack;
    }

    public void setIsGoBack(String isGoBack) {
        this.isGoBack = isGoBack;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCheci() {
        return checi;
    }

    public void setCheci(String checi) {
        this.checi = checi;
    }

    public List<TicketInfo> getTicketInfos() {
        return ticketInfos;
    }

    public void setTicketInfos(List<TicketInfo> ticketInfos) {
        this.ticketInfos = ticketInfos;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getIsStartStation() {
        return isStartStation;
    }

    public void setIsStartStation(String isStartStation) {
        this.isStartStation = isStartStation;
    }
}