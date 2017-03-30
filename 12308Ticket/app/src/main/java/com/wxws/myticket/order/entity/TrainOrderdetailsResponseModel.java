package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 13:57
 *
 * @author jiangyan
 */
public class TrainOrderdetailsResponseModel {
    /**
     * orderId : H2270967
     * orderNumber : 342534645675
     * orderStatus : 1
     * fromTime : 2016-08-20 06:00
     * toTime : 2016-08-21 13:28
     * deadlineTime : 2016-08-20 10:10:10
     * trainCode : G72
     * fromStationName : 深圳北
     * toStationName : 九江
     * runTimeMinute : 180
     * isRefund : 1
     * isChange : 1
     * amount : 18000
     * couponAmount : 5000
     * insuranceAmount : 6000
     * adultInsurance : 火车票意外保险（30元）
     * adultInsuranceNum : 2
     * childInsurance : 火车票意外保险（30元）
     * childInsuranceNum : 2
     * mobile : 18988750209
     * originalOrderId: 来源 id
     */

    private String userId;
    private String orderId;
    private String orderNumber;
    private int orderStatus;
    private String fromTime;
    private String toTime;
    private String deadlineTime;
    private long deadlineTimeMillis;
    private String trainCode;
    private String fromStationCode;
    private String fromStationName;
    private String toStationCode;
    private String toStationName;
    private int runTimeMinute;
    private int isRefund;
    private int isChange;
    private int isOwnAccount;
    private int amount;
    private int couponAmount;
    private int insuranceAmount;
    private String mobile;
    private String userCouponId;
    private int refundAmount;
    private String returnMsg;
    private String originalOrderId;
    private String adultInsuranceName;
    private String adultInsuranceNum;
    private String childInsuranceName;
    private String childInsuranceNum;
    private List<TrainOrderDetailsTicketModel> ticketList;
    //private List<TrainOrderDetails> changeOrderList;

    public long getDeadlineTimeMillis() {
        return deadlineTimeMillis;
    }

    public void setDeadlineTimeMillis(long deadlineTimeMillis) {
        this.deadlineTimeMillis = deadlineTimeMillis;
    }

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

    public String getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(String originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public List<TrainOrderDetailsTicketModel> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TrainOrderDetailsTicketModel> ticketList) {
        this.ticketList = ticketList;
    }

//    public List<TrainOrderDetails> getChangeOrderList() {
//        return changeOrderList;
//    }
//
//    public void setChangeOrderList(List<TrainOrderDetails> changeOrderList) {
//        this.changeOrderList = changeOrderList;
//    }

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

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public int getRunTimeMinute() {
        return runTimeMinute;
    }

    public void setRunTimeMinute(int runTimeMinute) {
        this.runTimeMinute = runTimeMinute;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public int getIsChange() {
        return isChange;
    }

    public void setIsChange(int isChange) {
        this.isChange = isChange;
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

    public String getAdultInsuranceNum() {
        return adultInsuranceNum;
    }

    public void setAdultInsuranceNum(String adultInsuranceNum) {
        this.adultInsuranceNum = adultInsuranceNum;
    }

    public String getChildInsuranceNum() {
        return childInsuranceNum;
    }

    public void setChildInsuranceNum(String childInsuranceNum) {
        this.childInsuranceNum = childInsuranceNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getAdultInsuranceName() {
        return adultInsuranceName;
    }

    public void setAdultInsuranceName(String adultInsuranceName) {
        this.adultInsuranceName = adultInsuranceName;
    }

    public String getChildInsuranceName() {
        return childInsuranceName;
    }

    public void setChildInsuranceName(String childInsuranceName) {
        this.childInsuranceName = childInsuranceName;
    }
}