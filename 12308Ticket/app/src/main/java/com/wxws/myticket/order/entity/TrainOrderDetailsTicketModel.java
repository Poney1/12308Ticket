package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-02 13:58
 *
 * @author jiangyan
 */
public class TrainOrderDetailsTicketModel {

    /**
     * orderDetailId : H2270999-1
     * name : 哎哟
     * mobile : 123456
     * ticketType : 1
     * certType : 01
     * certNo : 430621199203285022
     * studentTicketInfo : 123456
     * childName : 123456
     * birthday : 123456
     * seatType : M
     * seatInfo :
     * amount : 0
     * orderStatus : 1
     * ticketStatus : 2
     * ticketAmount : 0
     * couponAmount : 0
     * insuranceAmount : 2
     * insuranceId : 123456
     * insuranceName : 123456
     * ticketRefundAmount : 0
     * refundAmount : 0
     * originalOrderDetailId : 123456
     * changeOrderDetailId : 123456
     * returnMsg : 123456
     *
     allowChange
     允许改签 ,1是0否
     allowRefund
     允许退票，1是0否

     */

    private String orderDetailId;
    private String name;
    private String mobile;
    private int ticketType;
    private String certType;
    private String certNo;
    private String studentTicketInfo;
    private String childName;
    private String birthday;
    private String seatType;
    private String seatInfo;
    private int amount;
    private int orderStatus;
    private int ticketStatus;
    private int ticketAmount;
    private int couponAmount;
    private int insuranceAmount;
    private String insuranceId;
    private String insuranceName;
    private int ticketRefundAmount;
    private int refundAmount;
    private String originalOrderDetailId;
    private String changeOrderDetailId;
    private String returnMsg;
    private int allowChange;
    private int allowRefund;

    public int getAllowChange() {
        return allowChange;
    }

    public void setAllowChange(int allowChange) {
        this.allowChange = allowChange;
    }

    public int getAllowRefund() {
        return allowRefund;
    }

    public void setAllowRefund(int allowRefund) {
        this.allowRefund = allowRefund;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getStudentTicketInfo() {
        return studentTicketInfo;
    }

    public void setStudentTicketInfo(String studentTicketInfo) {
        this.studentTicketInfo = studentTicketInfo;
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


    public String getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(String seatInfo) {
        this.seatInfo = seatInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public int getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(int ticketAmount) {
        this.ticketAmount = ticketAmount;
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

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public int getTicketRefundAmount() {
        return ticketRefundAmount;
    }

    public void setTicketRefundAmount(int ticketRefundAmount) {
        this.ticketRefundAmount = ticketRefundAmount;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOriginalOrderDetailId() {
        return originalOrderDetailId;
    }

    public void setOriginalOrderDetailId(String originalOrderDetailId) {
        this.originalOrderDetailId = originalOrderDetailId;
    }

    public String getChangeOrderDetailId() {
        return changeOrderDetailId;
    }

    public void setChangeOrderDetailId(String changeOrderDetailId) {
        this.changeOrderDetailId = changeOrderDetailId;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }


    @Override
    public String toString() {
        return "TrainOrderDetailsTicketModel{" +
                "orderDetailId='" + orderDetailId + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ticketType=" + ticketType +
                ", certType='" + certType + '\'' +
                ", certNo='" + certNo + '\'' +
                ", studentTicketInfo='" + studentTicketInfo + '\'' +
                ", childName='" + childName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", seatType='" + seatType + '\'' +
                ", seatInfo='" + seatInfo + '\'' +
                ", amount=" + amount +
                ", orderStatus=" + orderStatus +
                ", ticketStatus=" + ticketStatus +
                ", ticketAmount=" + ticketAmount +
                ", couponAmount=" + couponAmount +
                ", insuranceAmount=" + insuranceAmount +
                ", insuranceId='" + insuranceId + '\'' +
                ", insuranceName='" + insuranceName + '\'' +
                ", ticketRefundAmount=" + ticketRefundAmount +
                ", refundAmount=" + refundAmount +
                ", originalOrderDetailId='" + originalOrderDetailId + '\'' +
                ", changeOrderDetailId='" + changeOrderDetailId + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                '}';
    }
}