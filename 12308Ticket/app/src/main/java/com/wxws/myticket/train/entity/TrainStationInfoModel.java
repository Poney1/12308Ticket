package com.wxws.myticket.train.entity;


import java.io.Serializable;
import java.util.List;

/**
 * desc:  经停站点信息
 * Date: 2016/8/5 10:46
 *
 * @auther: lixiangxiang
 */
public class TrainStationInfoModel implements Serializable {


    /**
     * startStationName : 深圳北
     * endStationName : 北京西
     * trainType : 高速
     * trainCode : G72
     * data : [{"arriveTime":"----","stationName":"深圳北","arriveDays":0,"startTime":"07:53","stationNo":"1","stopoverTime":"----"},{"arriveTime":"08:22","stationName":"广州南","arriveDays":0,"startTime":"08:27","stationNo":"2","stopoverTime":"5分钟"},{"arriveTime":"18:21","stationName":"北京西","arriveDays":0,"startTime":"18:21","stationNo":"15","stopoverTime":"----"}]
     */

    private String startStationName;
    private String endStationName;
    private String trainType;
    private String trainCode;
    /**
     * arriveTime : ----
     * stationName : 深圳北
     * arriveDays : 0
     * startTime : 07:53
     * stationNo : 1
     * stopoverTime : ----
     */

    private List<DataBean> data;

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String arriveTime;
        private String stationName;
        private int arriveDays;
        private String startTime;
        private String stationNo;
        private String stopOverTime;

        public String getArriveTime() {
            return arriveTime;
        }

        public void setArriveTime(String arriveTime) {
            this.arriveTime = arriveTime;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getArriveDays() {
            return arriveDays;
        }

        public void setArriveDays(int arriveDays) {
            this.arriveDays = arriveDays;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStationNo() {
            return stationNo;
        }

        public void setStationNo(String stationNo) {
            this.stationNo = stationNo;
        }

        public String getStopOverTime() {
            return stopOverTime;
        }

        public void setStopOverTime(String stopOverTime) {
            this.stopOverTime = stopOverTime;
        }
    }
}
