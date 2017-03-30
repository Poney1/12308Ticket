package com.wxws.myticket.bus.entity;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 列表 所有 的 车次对象
 * Date: 2016/11/1 16:33
 *
 * @auther: lixiangxiang
 */
public class TraditionBusListEntity implements Serializable, Comparable<TraditionBusListEntity> {

    /**
     * scheduleId : 123666664444
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
     * scheduleType : 2
     * scheduleNature : 2
     * seatType : 硬座
     * status : 1
     * totalSeats : 50
     * leaveSeats : 20
     * price : 30000
     * discountPrice : 20000
     * endTime : 21:15
     * runTime : 50000
     * discountType : 0
     * discountCount : 0
     * labelInfoList : []
     * scheduleCount : 5
     * stopoverStationList : [{"stationId":"123111","stationName":"汽车站","nameAlias":"汽车站","type":"0","sortNum":1,"longitude":"11.1234","latitude":"11.1234"}]
     * isStartStation : 0
     * isRealName : 1
     * bizType : 1
     * distanceMe : 10KM
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
    private String scheduleNature;
    private String seatType;
    private String status;
    private int totalSeats;
    private int leaveSeats;
    private PriceInfo price;
    private int discountPrice;
    private String endTime;
    private String runTime;
    private String discountType;
    private String discountCount;
    private int scheduleCount;
    private String isStartStation;
    private String isRealName;
    private String distanceMe;
    private List<String> labelInfoList;
    /**
     * stationId : 123111
     * stationName : 汽车站
     * nameAlias : 汽车站
     * type : 0
     * sortNum : 1
     * longitude : 11.1234
     * latitude : 11.1234
     */

    private List<StopoverStationListEntity> stopoverStationList;

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

    public String getScheduleNature() {
        return scheduleNature;
    }

    public void setScheduleNature(String scheduleNature) {
        this.scheduleNature = scheduleNature;
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

    public PriceInfo getPrice() {
        return price;
    }

    public void setPrice(PriceInfo price) {
        this.price = price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getDistanceMe() {
        return distanceMe;
    }

    public void setDistanceMe(String distanceMe) {
        this.distanceMe = distanceMe;
    }

    public List<String> getLabelInfoList() {
        return labelInfoList;
    }

    public void setLabelInfoList(List<String> labelInfoList) {
        this.labelInfoList = labelInfoList;
    }

    public List<StopoverStationListEntity> getStopoverStationList() {
        return stopoverStationList;
    }

    public void setStopoverStationList(List<StopoverStationListEntity> stopoverStationList) {
        this.stopoverStationList = stopoverStationList;
    }

    public static class  PriceInfo  implements  Serializable{
        private int allPrice;

        public int getAllPrice() {
            return allPrice;
        }

        public void setAllPrice(int allPrice) {
            this.allPrice = allPrice;
        }
    }
    /**
     * logoUrl	string	是	logo地址
     * title	string	是	标题
     * desc	string	是	描述
     * price	int	是	价格
     * type	string	是	跳转目标
     * 0：H5 url
     * 1：车次详情页
     * 2：车次列表页
     * targetUrl	string	是	目标url
     * adId	string	是	广告位编号
     * bizType	string	是	业务类型
     * url	string	否	跳转url H5使用
     */
    private String logoUrl;
    private String title;
    private String desc;
    private String type;
    private String targetUrl;
    private String adId;
    private String bizType;
    private String url;
    private int lowestPrice;
    private boolean isIntroduce = false;

    public boolean isIntroduce() {
        return isIntroduce;
    }

    public void setIntroduce(boolean introduce) {
        isIntroduce = introduce;
    }

    public int getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(int lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(TraditionBusListEntity o) {
        return this.getDiscountPrice() - o.getDiscountPrice();
    }
}
