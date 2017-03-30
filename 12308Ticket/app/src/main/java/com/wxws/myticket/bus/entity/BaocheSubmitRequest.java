package com.wxws.myticket.bus.entity;

/**
 * desc:
 * Date: 2016-11-29 13:50
 *
 * @author jiangyan
 */
public class BaocheSubmitRequest {


    private String busInfo;
    private String contactName;
    private String contactMobile;
    private String isGoBack;
    private String goBackTime;
    private String useTime;
    private String fromPlace;
    private String toPlace;
    private String fromLongitude;
    private String fromLatitude;
    private String toLongitude;
    private String toLatitude;
    private String personCount;
    private String isInvoice;
    private String invoiceTitle;
    private String receiver;
    private String receiverAddr;
    private String orderMoney;
    private String couponId;

    private String fromCityName;
    private String toCityName;

    public String getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(String busInfo) {
        this.busInfo = busInfo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getIsGoBack() {
        return isGoBack;
    }

    public void setIsGoBack(String isGoBack) {
        this.isGoBack = isGoBack;
    }

    public String getGoBackTime() {
        return goBackTime;
    }

    public void setGoBackTime(String goBackTime) {
        this.goBackTime = goBackTime;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getFromLongitude() {
        return fromLongitude;
    }

    public void setFromLongitude(String fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public String getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(String fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public String getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(String toLongitude) {
        this.toLongitude = toLongitude;
    }

    public String getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(String toLatitude) {
        this.toLatitude = toLatitude;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    @Override
    public String toString() {
        return "BaocheSubmitRequest{" +
                "busInfo='" + busInfo + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactMobile='" + contactMobile + '\'' +
                ", isGoBack='" + isGoBack + '\'' +
                ", goBackTime='" + goBackTime + '\'' +
                ", useTime='" + useTime + '\'' +
                ", fromPlace='" + fromPlace + '\'' +
                ", toPlace='" + toPlace + '\'' +
                ", fromLongitude='" + fromLongitude + '\'' +
                ", fromLatitude='" + fromLatitude + '\'' +
                ", toLongitude='" + toLongitude + '\'' +
                ", toLatitude='" + toLatitude + '\'' +
                ", personCount='" + personCount + '\'' +
                ", isInvoice='" + isInvoice + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiverAddr='" + receiverAddr + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", couponId='" + couponId + '\'' +
                '}';
    }
}