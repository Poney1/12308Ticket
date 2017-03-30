package com.wxws.myticket.train.entity;


import java.io.Serializable;
import java.util.List;

/**
 * desc: 车次列表
 * Date: 2016/8/4 17:23
 *
 * @auther: lixiangxiang
 */
public class TrainListModel implements Serializable {


    /**
     * saleDateTime : 09:00
     * canBuyNow : Y
     * startTime : 09:00
     * arriveDays : 0
     * arriveTime : 18:21
     * runTimeMinute : 628
     * trainCode : G72
     * trainNo : G72123456
     * trainType : G
     * fromStationName : 深圳北
     * fromStationCode : IOQ
     * toStationName : 北京西
     * toStationCode : BXP
     * seatList : [{"type":"gjrw","num":-1,"price":0},{"type":"qtxb","num":-1,"price":0},{"type":"rws","num":-1,"price":0},{"type":"rwx","num":-1,"price":0},{"type":"rz","num":-1,"price":0},{"type":"swz","num":23,"price":292350},{"type":"tdz","num":-1,"price":0},{"type":"wz","num":-1,"price":0},{"type":"yws","num":20,"price":10000},{"type":"ywz","num":0,"price":12000},{"type":"ywx","num":0,"price":14000},{"type":"yz","num":-1,"price":0},{"type":"edz","num":541,"price":93650},{"type":"ydz","num":125,"price":147950}]
     * distance : 1000
     * trainStartDate:
     */

    private String saleDateTime;
    private String canBuyNow;
    private String startTime;
    private String arriveDays;
    private String arriveTime;
    private String runTimeMinute;
    private String trainCode;
    private String trainNo;
    private String trainType;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private String distance;
    private String trainStartDate;
    private boolean isSelected = false;//是否选中

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTrainStartDate() {
        return trainStartDate;
    }

    public void setTrainStartDate(String trainStartDate) {
        this.trainStartDate = trainStartDate;
    }

    /**
     * type : gjrw
     * num : -1
     * price : 0
     */

    private List<SeatListEntity> seatList;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getArriveDays() {
        return arriveDays;
    }

    public void setArriveDays(String arriveDays) {
        this.arriveDays = arriveDays;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<SeatListEntity> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<SeatListEntity> seatList) {
        this.seatList = seatList;
    }


}
