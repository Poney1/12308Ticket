package com.wxws.myticket.bus.entity;

/**
 * desc: 包车列表
 * Date: 2016-11-18 16:46
 *
 * @author jiangyan
 */
public class BaocheBusType {


    private String busId;
    private String name;
    private int seat;
    private String remark;
    private int price;
    private String image;
    private String mileage;
    private int unitPrice;
    private int recommendCount;

    @Override
    public String toString() {
        return "BaocheBusType{" +
                "busId='" + busId + '\'' +
                ", name='" + name + '\'' +
                ", seat=" + seat +
                ", remark='" + remark + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", mileage='" + mileage + '\'' +
                ", unitPrice=" + unitPrice +
                ", recommendCount=" + recommendCount +
                '}';
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }
}