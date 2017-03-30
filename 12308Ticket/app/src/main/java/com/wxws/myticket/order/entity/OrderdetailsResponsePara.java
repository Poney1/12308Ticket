package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc: 订单详情
 * Date: 2016-11-11 15:53
 *
 * @author jiangyan
 */
public class OrderdetailsResponsePara {

    private String orderId;
    private String scheduleId;
    private int orderMoney;
    private int ticketMoney;
    private int halfTicketMoney;
    private int couponMoney;
    private int insuranceMoney;
    private int refundMoeny;// 拼写错误，后台如此
    private int reserveMoney;
    private int finalMoney;
    private int thirdFeeMoney;
    private String createTime;
    private int paySurplusTime;
    private String payTime;
    private String fromCityName;
    private String fromCityId;
    private String toCityName;
    private String toCityId;
    private String fromStationName;
    private String fromStationId;
    private String toStationName;
    private String toStationId;
    private String departDate;
    private String departTime;
    private String toTime;
    private String goBackTime;
    private String isGoBack;
    private String isThisDayGo;
    private String busCompany;
    private String busNumber;
    private String busMobile;
    private String busLogoUrl;
    private String byCarAddr;
    private String name;
    private String mobile;
    private String certType;
    private String certNo;
    private String ticketNumber;
    private String ticketMsg;
    private int adultTicketCount;
    private int childtTicketCount;
    private int halfTicketCount;
    private int count;
    private String payChannel;
    private String orderStatus;
    private String payStatus;
    private String refundStatus;
    private String couponJson;
    private String isFinal;
    private String vehicleType;
    private String checi;
    private List<TicketInfosBean> ticketInfos;
    private String checkTicketUrl;
    private String returnTicketInfoUrl;
    private String lineRemark;
    private String checkTicketWindow;
    private String invoiceTitle;
    private String recipientsName;
    private String deliveryAddress;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getTicketMoney() {
        return ticketMoney;
    }

    public void setTicketMoney(int ticketMoney) {
        this.ticketMoney = ticketMoney;
    }

    public int getHalfTicketMoney() {
        return halfTicketMoney;
    }

    public void setHalfTicketMoney(int halfTicketMoney) {
        this.halfTicketMoney = halfTicketMoney;
    }

    public int getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(int couponMoney) {
        this.couponMoney = couponMoney;
    }

    public int getInsuranceMoney() {
        return insuranceMoney;
    }

    public void setInsuranceMoney(int insuranceMoney) {
        this.insuranceMoney = insuranceMoney;
    }

    public int getRefundMoeny() {
        return refundMoeny;
    }

    public void setRefundMoeny(int refundMoeny) {
        this.refundMoeny = refundMoeny;
    }

    public int getReserveMoney() {
        return reserveMoney;
    }

    public void setReserveMoney(int reserveMoney) {
        this.reserveMoney = reserveMoney;
    }

    public int getFinalMoney() {
        return finalMoney;
    }

    public void setFinalMoney(int finalMoney) {
        this.finalMoney = finalMoney;
    }

    public int getThirdFeeMoney() {
        return thirdFeeMoney;
    }

    public void setThirdFeeMoney(int thirdFeeMoney) {
        this.thirdFeeMoney = thirdFeeMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPaySurplusTime() {
        return paySurplusTime;
    }

    public void setPaySurplusTime(int paySurplusTime) {
        this.paySurplusTime = paySurplusTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    public String getFromCityId() {
        return fromCityId;
    }

    public void setFromCityId(String fromCityId) {
        this.fromCityId = fromCityId;
    }

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getToCityId() {
        return toCityId;
    }

    public void setToCityId(String toCityId) {
        this.toCityId = toCityId;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getFromStationId() {
        return fromStationId;
    }

    public void setFromStationId(String fromStationId) {
        this.fromStationId = fromStationId;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getToStationId() {
        return toStationId;
    }

    public void setToStationId(String toStationId) {
        this.toStationId = toStationId;
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

    public String getGoBackTime() {
        return goBackTime;
    }

    public void setGoBackTime(String goBackTime) {
        this.goBackTime = goBackTime;
    }

    public String getIsGoBack() {
        return isGoBack;
    }

    public void setIsGoBack(String isGoBack) {
        this.isGoBack = isGoBack;
    }

    public String getIsThisDayGo() {
        return isThisDayGo;
    }

    public void setIsThisDayGo(String isThisDayGo) {
        this.isThisDayGo = isThisDayGo;
    }

    public String getBusCompany() {
        return busCompany;
    }

    public void setBusCompany(String busCompany) {
        this.busCompany = busCompany;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusMobile() {
        return busMobile;
    }

    public void setBusMobile(String busMobile) {
        this.busMobile = busMobile;
    }

    public String getBusLogoUrl() {
        return busLogoUrl;
    }

    public void setBusLogoUrl(String busLogoUrl) {
        this.busLogoUrl = busLogoUrl;
    }

    public String getByCarAddr() {
        return byCarAddr;
    }

    public void setByCarAddr(String byCarAddr) {
        this.byCarAddr = byCarAddr;
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

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketMsg() {
        return ticketMsg;
    }

    public void setTicketMsg(String ticketMsg) {
        this.ticketMsg = ticketMsg;
    }

    public int getAdultTicketCount() {
        return adultTicketCount;
    }

    public void setAdultTicketCount(int adultTicketCount) {
        this.adultTicketCount = adultTicketCount;
    }

    public int getChildtTicketCount() {
        return childtTicketCount;
    }

    public void setChildtTicketCount(int childtTicketCount) {
        this.childtTicketCount = childtTicketCount;
    }

    public int getHalfTicketCount() {
        return halfTicketCount;
    }

    public void setHalfTicketCount(int halfTicketCount) {
        this.halfTicketCount = halfTicketCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getCouponJson() {
        return couponJson;
    }

    public void setCouponJson(String couponJson) {
        this.couponJson = couponJson;
    }

    public String getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(String isFinal) {
        this.isFinal = isFinal;
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

    public List<TicketInfosBean> getTicketInfos() {
        return ticketInfos;
    }

    public void setTicketInfos(List<TicketInfosBean> ticketInfos) {
        this.ticketInfos = ticketInfos;
    }

    public String getCheckTicketUrl() {
        return checkTicketUrl;
    }

    public void setCheckTicketUrl(String checkTicketUrl) {
        this.checkTicketUrl = checkTicketUrl;
    }

    public String getReturnTicketInfoUrl() {
        return returnTicketInfoUrl;
    }

    public void setReturnTicketInfoUrl(String returnTicketInfoUrl) {
        this.returnTicketInfoUrl = returnTicketInfoUrl;
    }

    public String getLineRemark() {
        return lineRemark;
    }

    public void setLineRemark(String lineRemark) {
        this.lineRemark = lineRemark;
    }

    public String getCheckTicketWindow() {
        return checkTicketWindow;
    }

    public void setCheckTicketWindow(String checkTicketWindow) {
        this.checkTicketWindow = checkTicketWindow;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getRecipientsName() {
        return recipientsName;
    }

    public void setRecipientsName(String recipientsName) {
        this.recipientsName = recipientsName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public static class TicketInfosBean {

        private String ticketId;
        private String name;
        private String mobile;
        private String ticketType;//10全价，15 半价，17 儿童价，40 优惠价
        private String certType;
        private String certNo;
        private String seatInfo;
        private String insuranceId;
        private String insuranceStatus;
        private int ticketMoney;
        private int couponMoney;
        private int insuranceMoney;
        private int thirdFeeMoney;
        private String ticketStatus;
        private String updateTime;
        private String qrCode;
        private String barCode;
        private String isHasChildren;
        private String refundUpdateTime;
        private int feeMoney;
        private String refundStatus;
        private String refundId;
        private int refundMoney;
        private int ticketRefundMoney;
        private String refundRatio;
        private int insuranceRefundMoney;

        public String getTicketId() {
            return ticketId;
        }

        public void setTicketId(String ticketId) {
            this.ticketId = ticketId;
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

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getCertType() {
            return certType;
        }

        public void setCertType(String certType) {
            this.certType = certType;
        }

        public String getCertNo() {
            return certNo;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public String getSeatInfo() {
            return seatInfo;
        }

        public void setSeatInfo(String seatInfo) {
            this.seatInfo = seatInfo;
        }

        public String getInsuranceId() {
            return insuranceId;
        }

        public void setInsuranceId(String insuranceId) {
            this.insuranceId = insuranceId;
        }

        public String getInsuranceStatus() {
            return insuranceStatus;
        }

        public void setInsuranceStatus(String insuranceStatus) {
            this.insuranceStatus = insuranceStatus;
        }

        public int getTicketMoney() {
            return ticketMoney;
        }

        public void setTicketMoney(int ticketMoney) {
            this.ticketMoney = ticketMoney;
        }

        public int getCouponMoney() {
            return couponMoney;
        }

        public void setCouponMoney(int couponMoney) {
            this.couponMoney = couponMoney;
        }

        public int getInsuranceMoney() {
            return insuranceMoney;
        }

        public void setInsuranceMoney(int insuranceMoney) {
            this.insuranceMoney = insuranceMoney;
        }

        public int getThirdFeeMoney() {
            return thirdFeeMoney;
        }

        public void setThirdFeeMoney(int thirdFeeMoney) {
            this.thirdFeeMoney = thirdFeeMoney;
        }

        public String getTicketStatus() {
            return ticketStatus;
        }

        public void setTicketStatus(String ticketStatus) {
            this.ticketStatus = ticketStatus;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getIsHasChildren() {
            return isHasChildren;
        }

        public void setIsHasChildren(String isHasChildren) {
            this.isHasChildren = isHasChildren;
        }

        public String getRefundUpdateTime() {
            return refundUpdateTime;
        }

        public void setRefundUpdateTime(String refundUpdateTime) {
            this.refundUpdateTime = refundUpdateTime;
        }

        public int getFeeMoney() {
            return feeMoney;
        }

        public void setFeeMoney(int feeMoney) {
            this.feeMoney = feeMoney;
        }

        public String getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
        }

        public String getRefundId() {
            return refundId;
        }

        public void setRefundId(String refundId) {
            this.refundId = refundId;
        }

        public int getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(int refundMoney) {
            this.refundMoney = refundMoney;
        }

        public int getTicketRefundMoney() {
            return ticketRefundMoney;
        }

        public void setTicketRefundMoney(int ticketRefundMoney) {
            this.ticketRefundMoney = ticketRefundMoney;
        }

        public String getRefundRatio() {
            return refundRatio;
        }

        public void setRefundRatio(String refundRatio) {
            this.refundRatio = refundRatio;
        }

        public int getInsuranceRefundMoney() {
            return insuranceRefundMoney;
        }

        public void setInsuranceRefundMoney(int insuranceRefundMoney) {
            this.insuranceRefundMoney = insuranceRefundMoney;
        }
    }
}