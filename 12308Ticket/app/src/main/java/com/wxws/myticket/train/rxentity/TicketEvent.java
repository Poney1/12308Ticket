package com.wxws.myticket.train.rxentity;

/**
 * desc:
 * Date: 2016-09-08 16:19
 *
 * @author jiangyan
 */
public class TicketEvent {

    private int type;

    public TicketEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}