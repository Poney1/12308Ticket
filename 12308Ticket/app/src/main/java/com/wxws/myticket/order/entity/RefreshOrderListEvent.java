package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-10 14:15
 *
 * @author jiangyan
 */
public class RefreshOrderListEvent {

    public RefreshOrderListEvent(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}