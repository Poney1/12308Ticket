package com.wxws.myticket.bus.entity;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 车次详情
 * Date: 2016/11/14 13:45
 *
 * @auther: lixiangxiang
 */
public class LineDetailEntity  implements Serializable{


    /**
     * scheduleId : 123
     * scheduleName : 测试线路
     * fromCityId : 1111
     * fromCityName : 深圳
     * fromStationId : 22222
     * fromStationName : 宝安汽车站
     * startCityId : 11112222
     * startCityName : 深圳
     * startStationId : 112233
     * startStationName : 宝安汽车站
     * toCityId : 122222
     * toCityName : 广州
     * toStationId : 22222
     * toStationName : 广州汽车站
     * endCityId : 22333
     * endCityName : 广州
     * endStationId : 22222
     * endStationName : 广州
     * startDate : 2016-11-12
     * startTime : 09:15
     * distance : 50KM
     * scheduleType : 高级大巴
     * vehicleType : 45座车
     * seatType : 硬座
     * status : 1
     * totalSeats : 50
     * leaveSeats : 20
     * interfaceId : 123
     * serviceFee : 0
     * isSupportChildTicket : 1
     * isSupportHalfTicket : 1
     * isSupportRefundTikcet : 1
     * insuranceList : [{"insuranceId":"123","insuranceName":"合众火车意外险G款（成人）","money":300,"insuranceDesc":"交通工具意外伤害10万元，意外医疗1.5万元","insuranceTerms":"1.投保范围：18-80周岁身体健康者，均可以作为被保险人，由本人或对其具有保险","insuranceCompanyName":"航天华有","type":"1"}]
     * price : 30000
     * isBackCheci : 1
     * isCoupon : 1
     * isInsurance : 1
     * endTime : 21:15
     * runTime : 50000
     * discountType : 0
     * discountCount : 0
     * statementInfoList : []
     * helpInfoUrl : www.baidu.com
     * transportCompany : 广州深发客运有限公司
     * transportBGUrl : www.baidu.com
     * servicePhone : 0755-123
     * starLevel : 5
     * transportCompanyLogo : www.baidu.com
     * labelInfoList : []
     * scheduleCount : 5
     * stopoverStationList : [{"stationId":"123","stationName":"汽车站","nameAlias":"汽车站","type":"0","sortNum":1,"longitude":"11.1234","latitude":"11.1234","contactName":"张三","contactMobile":"155788869999"}]
     * isStartStation : 0
     * isRealName : 1
     * childrenTicketInfo : []
     * getTicketWay : 0
     */

    private String scheduleId;
    private String scheduleName;
    private String fromCityId;
    private String fromCityName;
    private String fromStationId;
    private String fromStationName;
    private String startCityId;
    private String startCityName;
    private String startStationId;
    private String startStationName;
    private String toCityId;
    private String toCityName;
    private String toStationId;
    private String toStationName;
    private String endCityId;
    private String endCityName;
    private String endStationId;
    private String endStationName;
    private String startDate;
    private String startTime;
    private String distance;
    private String scheduleType;
    private String vehicleType;
    private String seatType;
    private String status;
    private int totalSeats;
    private int leaveSeats;
    private String interfaceId;
    private int serviceFee;
    private String isSupportChildTicket;
    private String isSupportHalfTicket;
    private String isSupportRefundTikcet;
    private String isBackCheci;
    private String isCoupon;
    private String isInsurance;
    private String endTime;
    private String runTime;
    private String discountType;
    private String discountCount;
    private String helpInfoUrl;
    private String transportCompany;
    private String transportBGUrl;
    private String servicePhone;
    private String starLevel;
    private String transportCompanyLogo;
    private int scheduleCount;
    private String isStartStation;
    private String isRealName;
    private String getTicketWay;
    /**
     * insuranceId : 123
     * insuranceName : 合众火车意外险G款（成人）
     * money : 300
     * insuranceDesc : 交通工具意外伤害10万元，意外医疗1.5万元
     * insuranceTerms : 1.投保范围：18-80周岁身体健康者，均可以作为被保险人，由本人或对其具有保险
     * insuranceCompanyName : 航天华有
     * type : 1
     */

    private List<LineDetailInsuranceEntity> insuranceList;
    private List<String> statementInfoList;
    private List<String> labelInfoList;
    /**
     * stationId : 123
     * stationName : 汽车站
     * nameAlias : 汽车站
     * type : 0
     * sortNum : 1
     * longitude : 11.1234
     * latitude : 11.1234
     * contactName : 张三
     * contactMobile : 155788869999
     */

    private List<DirectStopoverStationList> stopoverStationList;
    private List<String> childrenTicketInfo;
    /**
     * bizType : 0
     * openFreeInsurance : 0
     * scheduleNature : 1
     */

    private String bizType;
    private String openFreeInsurance;
    private String scheduleNature;
    /**
     * price : []
     */

    private LineDetailAllTicketEntity price;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
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

    public String getStartCityId() {
        return startCityId;
    }

    public void setStartCityId(String startCityId) {
        this.startCityId = startCityId;
    }

    public String getStartCityName() {
        return startCityName;
    }

    public void setStartCityName(String startCityName) {
        this.startCityName = startCityName;
    }

    public String getStartStationId() {
        return startStationId;
    }

    public void setStartStationId(String startStationId) {
        this.startStationId = startStationId;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
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

    public String getEndCityId() {
        return endCityId;
    }

    public void setEndCityId(String endCityId) {
        this.endCityId = endCityId;
    }

    public String getEndCityName() {
        return endCityName;
    }

    public void setEndCityName(String endCityName) {
        this.endCityName = endCityName;
    }

    public String getEndStationId() {
        return endStationId;
    }

    public void setEndStationId(String endStationId) {
        this.endStationId = endStationId;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getLeaveSeats() {
        return leaveSeats;
    }

    public void setLeaveSeats(int leaveSeats) {
        this.leaveSeats = leaveSeats;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(int serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getIsSupportChildTicket() {
        return isSupportChildTicket;
    }

    public void setIsSupportChildTicket(String isSupportChildTicket) {
        this.isSupportChildTicket = isSupportChildTicket;
    }

    public String getIsSupportHalfTicket() {
        return isSupportHalfTicket;
    }

    public void setIsSupportHalfTicket(String isSupportHalfTicket) {
        this.isSupportHalfTicket = isSupportHalfTicket;
    }

    public String getIsSupportRefundTikcet() {
        return isSupportRefundTikcet;
    }

    public void setIsSupportRefundTikcet(String isSupportRefundTikcet) {
        this.isSupportRefundTikcet = isSupportRefundTikcet;
    }

    public String getIsBackCheci() {
        return isBackCheci;
    }

    public void setIsBackCheci(String isBackCheci) {
        this.isBackCheci = isBackCheci;
    }

    public String getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(String isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(String isInsurance) {
        this.isInsurance = isInsurance;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountCount() {
        return discountCount;
    }

    public void setDiscountCount(String discountCount) {
        this.discountCount = discountCount;
    }

    public String getHelpInfoUrl() {
        return helpInfoUrl;
    }

    public void setHelpInfoUrl(String helpInfoUrl) {
        this.helpInfoUrl = helpInfoUrl;
    }

    public String getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }

    public String getTransportBGUrl() {
        return transportBGUrl;
    }

    public void setTransportBGUrl(String transportBGUrl) {
        this.transportBGUrl = transportBGUrl;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getTransportCompanyLogo() {
        return transportCompanyLogo;
    }

    public void setTransportCompanyLogo(String transportCompanyLogo) {
        this.transportCompanyLogo = transportCompanyLogo;
    }

    public int getScheduleCount() {
        return scheduleCount;
    }

    public void setScheduleCount(int scheduleCount) {
        this.scheduleCount = scheduleCount;
    }

    public String getIsStartStation() {
        return isStartStation;
    }

    public void setIsStartStation(String isStartStation) {
        this.isStartStation = isStartStation;
    }

    public String getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(String isRealName) {
        this.isRealName = isRealName;
    }

    public String getGetTicketWay() {
        return getTicketWay;
    }

    public void setGetTicketWay(String getTicketWay) {
        this.getTicketWay = getTicketWay;
    }

    public List<LineDetailInsuranceEntity> getInsuranceList() {
        return insuranceList;
    }

    public void setInsuranceList(List<LineDetailInsuranceEntity> insuranceList) {
        this.insuranceList = insuranceList;
    }

    public List<String> getStatementInfoList() {
        return statementInfoList;
    }

    public void setStatementInfoList(List<String> statementInfoList) {
        this.statementInfoList = statementInfoList;
    }

    public List<String> getLabelInfoList() {
        return labelInfoList;
    }

    public void setLabelInfoList(List<String> labelInfoList) {
        this.labelInfoList = labelInfoList;
    }

    public List<DirectStopoverStationList> getStopoverStationList() {
        return stopoverStationList;
    }

    public void setStopoverStationList(List<DirectStopoverStationList> stopoverStationList) {
        this.stopoverStationList = stopoverStationList;
    }

    public List<String> getChildrenTicketInfo() {
        return childrenTicketInfo;
    }

    public void setChildrenTicketInfo(List<String> childrenTicketInfo) {
        this.childrenTicketInfo = childrenTicketInfo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOpenFreeInsurance() {
        return openFreeInsurance;
    }

    public void setOpenFreeInsurance(String openFreeInsurance) {
        this.openFreeInsurance = openFreeInsurance;
    }

    public String getScheduleNature() {
        return scheduleNature;
    }

    public void setScheduleNature(String scheduleNature) {
        this.scheduleNature = scheduleNature;
    }

    public LineDetailAllTicketEntity getPrice() {
        return price;
    }

    public void setPrice(LineDetailAllTicketEntity price) {
        this.price = price;
    }


}
