package com.wxws.myticket.train.entity;


import java.io.Serializable;
import java.util.List;

/**
 * desc: 选择的车次等信息
 * Date: 2016-08-01 13:58
 *
 * @auther: jiangyan
 */
public class ChooseSeatModel implements Serializable {

    /**
     * saleDateTime : 09:00
     * canBuyNow : Y
     * arriveDays : 0
     * trainCode : G72
     * trainNo : 6i00000G720C
     * trainType : G
     * fromStationName : 深圳北
     * fromStationCode : IOQ
     * toStationName : 长沙南
     * toStationCode : CWQ
     * startTime : 07:53
     * arriveTime : 11:04
     * runTimeMinute : 191
     * distance : 0
     * seatList : 座次列表
     */

    private String saleDateTime;
    private String canBuyNow;
    private String arriveDays;
    private String trainCode;
    private String trainNo;
    private String trainType;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private String startTime;
    private String arriveTime;
    private String runTimeMinute;
    private String distance;
    private List<SeatinfoModel> seatList;

    public List<SeatinfoModel> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<SeatinfoModel> seatList) {
        this.seatList = seatList;
    }

    public String getSaleDateTime() {
        return saleDateTime;
    }

    public void setSaleDateTime(String saleDateTime) {
        this.saleDateTime = saleDateTime;
    }

    public String getCanBuyNow() {
        return canBuyNow;
    }

    public void setCanBuyNow(String canBuyNow) {
        this.canBuyNow = canBuyNow;
    }

    public String getArriveDays() {
        return arriveDays;
    }

    public void setArriveDays(String arriveDays) {
        this.arriveDays = arriveDays;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getRunTimeMinute() {
        return runTimeMinute;
    }

    public void setRunTimeMinute(String runTimeMinute) {
        this.runTimeMinute = runTimeMinute;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}