package com.wxws.myticket.train.entity;


/**
 * desc: 订单id
 * Date: 2016-08-01 14:47
 *
 * @auther: jiangyan
 */
public class CommitOrderResponseModel {
    private String orderId;
    private String newOrderId;

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
