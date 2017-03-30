package com.wxws.myticket.train.entity;


import java.io.Serializable;

/**
 * desc: 改签
 * Date: 2016/8/19 17:21
 *
 * @auther: lixiangxiang
 */
public class ChangeOrderModel implements Serializable {


    /**
     * orderId : T20160723000001
     * newOrderId : T1111111111111
     */

    private String orderId;
    private String newOrderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }
}
