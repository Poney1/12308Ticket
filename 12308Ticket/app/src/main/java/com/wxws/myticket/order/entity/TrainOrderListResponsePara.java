package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 10:17
 *
 * @author jiangyan
 */
public class TrainOrderListResponsePara {
    /**
     * userId : 123456
     * orderId : H2270999
     * orderNumber :
     * orderStatus : 1
     * fromTime : 2016-08-13 07:10
     * toTime : 2016-08-13 12:42
     * deadlineTime : 2016-08-13 06:40:00
     * trainCode : G106
     * fromStationName : 上海虹桥
     * fromStationCode : 123
     * toStationName : 北京南
     * toStationCode : 123456
     * runTimeMinute : 332
     * isChange : 0
     * isRefund : 0
     * isOwnAccount : 0
     * amount : 0
     * couponAmount : 0
     * insuranceAmount : 0
     * mobile : 123456
     * userCouponId : 123456
     * refundAmount : 0
     * returnMsg : 123456
     * originalOrderId : 123456
     * adultInsuranceNum : 0
     * adultInsuranceName : 123456
     * childInsuranceNum : 0
     * childInsuranceName : 123456
     */

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
    private List<TrainOrderListEndorseModel> changeOrderList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getIsOwnAccount() {
        return isOwnAccount;
    }

    public void setIsOwnAccount(int isOwnAccount) {
        this.isOwnAccount = isOwnAccount;
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

    public List<TrainTicketInfoModel> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TrainTicketInfoModel> ticketList) {
        this.ticketList = ticketList;
    }

    public List<TrainOrderListEndorseModel> getChangeOrderList() {
        return changeOrderList;
    }

    public void setChangeOrderList(List<TrainOrderListEndorseModel> changeOrderList) {
        this.changeOrderList = changeOrderList;
    }
}