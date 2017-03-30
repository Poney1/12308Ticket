package com.wxws.myticket.order.utils;

/**
 * desc: 订单列表、除火车票订单详情外其他几种订单详情状态。
 * Date: 2016-11-11 09:58
 *
 * @author jiangyan
 */
public class OrderStatusUtils {

    public static String getOrderStatus(String status) {
        int mstatus = Integer.parseInt(status);
        return getOrderStatus(mstatus);
    }

    public static String getOrderStatus(int status) {
        switch (status) {
            case 1:
                return "索票中";
            case 4:
                return "订单关闭";
            case 9:
                return "订单关闭";
            case 10:
                return "等待支付";
            case 11:
                return "订单关闭";
            case 16:
                return "等待支付";
            case 19:
                return "等待支付";
            case 20:
                return "等待出票";
            case 31:
                return "等待出票";
            case 34:
                return "等待出票";
            case 35:
                return "等待出票";
            case 36:
                return "等待出票";
            case 39:
                return "出票失败";
            case 40:
                return "出票成功";
            case 48:
                return "已检票";
            case 51:
                return "退票中";
            case 59:
                return "退票失败";
            case 60:
                return "待退款";

        }
        return "状态异常";
    }

    public static String getTicketStatus(String ticketstatus) {

        int istatus = Integer.parseInt(ticketstatus);
        return getTicketStatus(istatus);
    }

    public static String getTicketStatus(int ticketStatus) {

        switch (ticketStatus) {
            case 20:
                return "等待出票";
            case 31:
                return "等待出票";
            case 34:
                return "等待出票";
            case 35:
                return "等待出票";
            case 36:
                return "等待出票";
            case 39:
                return "出票失败";
            case 40:
                return "改签成功";
            case 45:
                return "已取票";
            case 48:
                return "已检票";
            case 51:
                return "退票中";
            case 59:
                return "退票失败";
            case 60:
                return "待退款";
        }


        return "状态异常";
    }

    // 用户退款状态
    public static String getRefundStatus(String refundStatus) {
        switch (refundStatus) {
            case "61":
                return "等待退款";
            case "66":
                return "退款中";
            case "69":
                return "退款失败";
            case "70":
                return "退款成功";
        }


        return "状态异常";
    }


}