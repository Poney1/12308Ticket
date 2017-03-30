package com.wxws.myticket.train.entity;

/**
 * desc: 正常支付锁票结果
 * Date: 2016/11/17 16:45
 *
 * @auther: lixiangxiang
 */
public class LockTicketResultEntity {

   private String  orderId	;
   private int status;
   private String  message;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
