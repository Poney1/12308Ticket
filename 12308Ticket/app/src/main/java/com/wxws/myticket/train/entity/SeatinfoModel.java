package com.wxws.myticket.train.entity;


import java.io.Serializable;

/**
 * desc: 选中的车次座位信息
 * Date: 2016-08-01 14:47
 *
 * @auther: jiangyan
 */
public class SeatinfoModel implements Serializable {

    @Override
    public String toString() {
        return "SeatinfoModel{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", price=" + price +
                ", level=" + level +
                ", choosed=" + choosed +
                '}';
    }

    /**
     * type:
     * name: 座位名称
     * num:余票张数
     * price:价格
     * level: 排序标识位,票价从低到高排序,如果余票为0,则放后面,但是无票总是放最底下
     * choosed: 后续填写资料使用,
     */


    private String type;
    private String name;
    private int num;
    private int price;
    private int level;
    private String seatType;
    private boolean choosed = false;

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}