package com.wxws.myticket.bus.entity;

/**
 * desc: 包车提交订单弹窗数据
 * Date: 2016-11-21 15:40
 *
 * @author jiangyan
 */
public class BaocheNoticeEntity {

    private String beforeHour;
    private String orderMinutes;
    private String phone;

    public String getBeforeHour() {
        return beforeHour;
    }

    public void setBeforeHour(String beforeHour) {
        this.beforeHour = beforeHour;
    }

    public String getOrderMinutes() {
        return orderMinutes;
    }

    public void setOrderMinutes(String orderMinutes) {
        this.orderMinutes = orderMinutes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}