package com.wxws.myticket.order.utils;

/**
 * desc: 火车票订单详情需要显示的状态对应工具类
 * Date: 2016-08-16 13:56
 *
 * @author jiangyan
 */
public class TrainOrderStatusUtils {

    // 火车票一般订单状态
    public static String getCommonStatus(int status) {

        switch (status) {
            case 1:
                return "抢票中";
            case 4:
                return "抢票失败";
            case 9:
                return "抢票失败";
            case 10:
                return "等待支付";
            case 11:
                return "订单关闭";
            case 16:
                return "等待支付";
            case 17:
                return "订单关闭";
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
            case 38:
                return "改签失败";
            case 39:
                return "出票失败";
            case 40:
                return "出票成功";
            case 41:
                return "已改签";
            case 45:
                return "已取票";
            case 50:
                return "退款中";
            case 51:
                return "退票中";
            case 59:
                return "退票失败";
            case 60:
                return "待退款";
            case 61:
                return "退款中";
            case 69:
                return "退款失败";
            case 70:
                return "退款成功";
        }

        return "状态异常";
    }

    // 火车票改签订单状态
    public static String getEndorseStatus(int status) {

        switch (status) {
            case 1:
                return "改签申请中";
            case 4:
                return "改签失败";
            case 9:
                return "改签失败";
            case 10:
                return "改签待支付";
            case 11:
                return "改签取消";
            case 16:
                return "改签待支付";
            case 17:
                return "改签取消";
            case 19:
                return "改签待支付";
            case 20:
                return "改签待出票";
            case 31:
                return "待出票";
            case 34:
                return "待出票";
            case 35:
                return "待出票";
            case 36:
                return "改签待出票";
            case 38:
                return "改签失败";
            case 39:
                return "改签失败";
            case 40:
                return "改签成功";
            case 41:
                return "已改签";
            case 45:
                return "已取票";
            case 50:
                return "退款中";
            case 51:
                return "退票中";
            case 59:
                return "退票失败";
            case 60:
                return "待退款";
            case 61:
                return "退款中";
            case 69:
                return "退款失败";
            case 70:
                return "退款成功";

        }
        return "状态异常";
    }

    // 一般票状态
    public static String getTicketStatus(int status) {
        switch (status) {
            case 1:
                return "抢票中";
            case 4:
                return "抢票失败";
            case 9:
                return "抢票失败";
            case 10:
                return "待支付";
            case 11:
                return "订单关闭";
            case 16:
                return "待支付";
            case 17:
                return "订单关闭";
            case 19:
                return "待支付";
            case 20:
                return "待出票";
            case 31:
                return "待出票";
            case 34:
                return "待出票";
            case 35:
                return "待出票";
            case 36:
                return "待出票";
            case 38:
                return "改签失败";
            case 39:
                return "出票失败";
            case 40:
                return "已出票";
            case 41:
                return "已改签";
            case 45:
                return "已取票";
            case 50:
                return "退款中";
            case 51:
                return "退票中";
            case 59:
                return "退票失败";
            case 60:
                return "待退款";
            case 61:
                return "退款中";
            case 69:
                return "退款失败";
            case 70:
                return "退款成功";
        }

        return "状态异常";
    }

    // 改签票状态
    public static String getEndorseTicketStatus(int status) {
        switch (status) {
            case 1:
                return "改签申请中";
            case 4:
                return "改签失败";
            case 9:
                return "改签失败";
            case 10:
                return "改签待支付";
            case 11:
                return "改签取消";
            case 16:
                return "改签待支付";
            case 17:
                return "改签取消";
            case 19:
                return "改签待支付";
            case 20:
                return "改签待出票";
            case 31:
                return "待出票";
            case 34:
                return "待出票";
            case 35:
                return "待出票";
            case 36:
                return "改签待出票";
            case 38:
                return "改签失败";
            case 39:
                return "改签失败";
            case 40:
                return "已改签";
            case 41:
                return "已改签";
            case 45:
                return "已取票";
            case 50:
                return "退款中";
            case 51:
                return "退票中";
            case 59:
                return "退票失败";
            case 60:
                return "待退款";
            case 61:
                return "退款中";
            case 69:
                return "退款失败";
            case 70:
                return "退款成功";
        }
        return "状态异常";
    }
}