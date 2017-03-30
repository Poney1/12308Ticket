package com.wxws.myticket.order.entity;

/**
 * desc: 订单 列表
 * Date: 2016-10-31 16:41
 *
 * @author jiangyan
 */
public class OrderListEvent {

    private int type;
    private boolean is_recent;
    private boolean is_recent_changed;// 是否改变 有效、失效/最近、历史

    public OrderListEvent(int type) {
        this.type = type;
        this.is_recent_changed = false;
    }

    public OrderListEvent(int type, boolean is_recent) {
        this.type = type;
        this.is_recent = is_recent;
        this.is_recent_changed = true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean is_recent() {
        return is_recent;
    }

    public void setIs_recent(boolean is_recent) {
        this.is_recent = is_recent;
    }

    public boolean is_recent_changed() {
        return is_recent_changed;
    }

    public void setIs_recent_changed(boolean is_recent_changed) {
        this.is_recent_changed = is_recent_changed;
    }
}