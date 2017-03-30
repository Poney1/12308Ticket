package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 10:18
 *
 * @author jiangyan
 */
public class TrainOrderListEndorseModel {
    private String userId;
    private String orderId;
    private String orderNumber;
    private int orderStatus;
    private String fromTime;
    private String toTime;
    private String deadlineTime;
    private String trainCode;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private int runTimeMinute;
    private int isChange;
    private int isRefund;
    private int isOwnAccount;
    private int amount;
    private int couponAmount;
    private int insuranceAmount;
    private String mobile;
    private String userCouponId;
    private int refundAmount;
    private String returnMsg;
    private String originalOrderId;
    private int adultInsuranceNum;
    private String adultInsuranceName;
    private int childInsuranceNum;
    private String childInsuranceName;
    private List<TrainTicketInfoModel> ticketList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsOwnAccount() {
        return isOwnAccount;
    }

    public void setIsOwnAccount(int isOwnAccount) {
        this.isOwnAccount = isOwnAccount;
    }

    public String getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(String originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public int getAdultInsuranceNum() {
        return adultInsuranceNum;
    }

    public void setAdultInsuranceNum(int adultInsuranceNum) {
        this.adultInsuranceNum = adultInsuranceNum;
    }

    public String getAdultInsuranceName() {
        return adultInsuranceName;
    }

    public void setAdultInsuranceName(String adultInsuranceName) {
        this.adultInsuranceName = adultInsuranceName;
    }

    public int getChildInsuranceNum() {
        return childInsuranceNum;
    }

    public void setChildInsuranceNum(int childInsuranceNum) {
        this.childInsuranceNum = childInsuranceNum;
    }

    public String getChildInsuranceName() {
        return childInsuranceName;
    }

    public void setChildInsuranceName(String childInsuranceName) {
        this.childInsuranceName = childInsuranceName;
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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getFromStationCode() {
        return fromStationCode;
    }

    public void setFromStationCode(String fromStationCode) {
        this.fromStationCode = fromStationCode;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getToStationCode() {
        return toStationCode;
    }

    public void setToStationCode(String toStationCode) {
        this.toStationCode = toStationCode;
    }

    public int getRunTimeMinute() {
        return runTimeMinute;
    }

    public void setRunTimeMinute(int runTimeMinute) {
        this.runTimeMinute = runTimeMinute;
    }

    public int getIsChange() {
        return isChange;
    }

    public void setIsChange(int isChange) {
        this.isChange = isChange;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(int insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public List<TrainTicketInfoModel> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TrainTicketInfoModel> ticketList) {
        this.ticketList = ticketList;
    }
}