package com.wxws.myticket.bus.entity;

import java.io.Serializable;

/**
 * desc: 价格 全价票 ，半价票
 * Date: 2016/12/8 15:27
 *
 * @auther: lixiangxiang
 */
public class LineDetailAllTicketEntity  implements Serializable{


    /**
     * allPrice : 10
     * discountPrice : 10
     * childPrice : 10
     * halfPrice : 10
     */

    private int allPrice;
    private int discountPrice;
    private int childPrice;
    private int halfPrice;

    public int getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(int allPrice) {
        this.allPrice = allPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(int childPrice) {
        this.childPrice = childPrice;
    }

    public int getHalfPrice() {
        return halfPrice;
    }

    public void setHalfPrice(int halfPrice) {
        this.halfPrice = halfPrice;
    }
}
