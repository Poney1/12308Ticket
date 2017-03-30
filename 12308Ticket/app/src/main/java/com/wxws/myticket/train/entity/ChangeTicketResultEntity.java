package com.wxws.myticket.train.entity;

import java.util.List;

/**
 * desc: 锁座结果
 * Date: 2016/11/17 18:41
 *
 * @auther: lixiangxiang
 */
public class ChangeTicketResultEntity {


    /**
     * amount : 0
     * message : 改签票款差价:0元
     * newOrderId : H2271063
     * orderId : H2271038
     * status : 1
     * ticketList : [{"newTicketId":"H2271063-1","status":1,"ticketId":"H2271038-1"}]
     */

    private int amount;
    private String message;
    private String newOrderId;
    private String orderId;
    private int status;
    /**
     * newTicketId : H2271063-1
     * status : 1
     * ticketId : H2271038-1
     */

    private List<TicketListBean> ticketList;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TicketListBean> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TicketListBean> ticketList) {
        this.ticketList = ticketList;
    }

    public static class TicketListBean {
        private String newTicketId;
        private int status;
        private String ticketId;

        public String getNewTicketId() {
            return newTicketId;
        }

        public void setNewTicketId(String newTicketId) {
            this.newTicketId = newTicketId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTicketId() {
            return ticketId;
        }

        public void setTicketId(String ticketId) {
            this.ticketId = ticketId;
        }
    }
}
