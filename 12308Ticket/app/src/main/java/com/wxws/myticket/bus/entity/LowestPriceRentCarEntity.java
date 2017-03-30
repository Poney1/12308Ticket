package com.wxws.myticket.bus.entity;


import java.io.Serializable;
import java.util.List;

/**
 * desc:
 * Date: 2016-06-28 18:11
 *
 * @auther: lixiangxiang
 */
public class LowestPriceRentCarEntity implements Serializable {


    /**
     * carPrice : [{"busId":13370,"busType":"豪华大巴","leaveSeat":1,"price":"120","totalSeat":1},{"busId":13371,"busType":"豪华大巴1","leaveSeat":80,"price":"33","totalSeat":80}]
     * company : 12308公司测试
     * endCity : 广州
     * endPlace : 天河区政府
     * endProvince : 广东
     * feeDescription : 假如你要退款，请直接参考退款规则
     * firstStartTime :
     * lastStartTime : 12:20
     * mobilePhone : 13755443322
     * otherDescription : 假如你要退款，请直接参考退款规则
     * price : 33
     * remark :
     * scheduleCode :
     * serviceStandard : 假如你要退款，请直接参考退款规则
     * startCity : 深圳
     * startDate : 2016-06-28
     * startPlace : 软件园二期
     * startProvince : 广东
     * startTime : 11:50
     * totalPrice : 130
     * zuCheType : 2
     * minRentBusId: int
     */

    private String company;
    private String endCity;
    private String endPlace;
    private String endProvince;
    private String feeDescription;
    private String firstStartTime;
    private String lastStartTime;
    private String mobilePhone;
    private String otherDescription;
    private String price;
    private String remark;
    private String scheduleCode;
    private String serviceStandard;
    private String startCity;
    private String startDate;
    private String startPlace;
    private String startProvince;
    private String startTime;
    private String totalPrice;
    private String zuCheType;

    public String getCoustomId() {
        return coustomId;
    }

    public void setCoustomId(String coustomId) {
        this.coustomId = coustomId;
    }

    private String coustomId;


    private String minRentBusId;//
    /**
     * busId : 13370
     * busType : 豪华大巴
     * leaveSeat : 1
     * price : 120
     * totalSeat : 1
     */

    private List<CarPriceEntityLow> carPrice;
    /**
     * startTime : 08:30
     * endTime : 09:30
     */

    private List<ServiceTimeEntity> serviceTime;


    public String getMinRentBusId() {
        return minRentBusId;
    }

    public void setMinRentBusId(String minRentBusId) {
        this.minRentBusId = minRentBusId;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getEndProvince() {
        return endProvince;
    }

    public void setEndProvince(String endProvince) {
        this.endProvince = endProvince;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(String firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public String getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(String lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOtherDescription() {
        return otherDescription;
    }

    public void setOtherDescription(String otherDescription) {
        this.otherDescription = otherDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getServiceStandard() {
        return serviceStandard;
    }

    public void setServiceStandard(String serviceStandard) {
        this.serviceStandard = serviceStandard;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getStartProvince() {
        return startProvince;
    }

    public void setStartProvince(String startProvince) {
        this.startProvince = startProvince;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getZuCheType() {
        return zuCheType;
    }

    public void setZuCheType(String zuCheType) {
        this.zuCheType = zuCheType;
    }

    public List<CarPriceEntityLow> getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(List<CarPriceEntityLow> carPrice) {
        this.carPrice = carPrice;
    }

    public List<ServiceTimeEntity> getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(List<ServiceTimeEntity> serviceTime) {
        this.serviceTime = serviceTime;
    }

    public static class ServiceTimeEntity implements Serializable {
        private String startTime;
        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }


    public static class CarPriceEntityLow implements Serializable {
        private String busId;
        private String busType;
        private String leaveSeat;
        private String price;
        private String totalSeat;

        public String getBusId() {
            return busId;
        }

        public void setBusId(String busId) {
            this.busId = busId;
        }

        public String getBusType() {
            return busType;
        }

        public void setBusType(String busType) {
            this.busType = busType;
        }

        public String getLeaveSeat() {
            return leaveSeat;
        }

        public void setLeaveSeat(String leaveSeat) {
            this.leaveSeat = leaveSeat;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotalSeat() {
            return totalSeat;
        }

        public void setTotalSeat(String totalSeat) {
            this.totalSeat = totalSeat;
        }
    }


}
