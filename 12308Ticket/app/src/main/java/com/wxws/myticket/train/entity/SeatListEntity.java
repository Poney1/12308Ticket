package com.wxws.myticket.train.entity;

import java.io.Serializable;

/**
 * desc:
 * Date: 2016/11/17 14:39
 *
 * @auther: lixiangxiang
 */
public class SeatListEntity implements Serializable {
        private String name ;
        private int index ;
        private String type;
        private int num;
        private int price;
        private String seatType;

        public String getSeatType() {
            return seatType;
        }

        public void setSeatType(String seatType) {
            this.seatType = seatType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
