package com.wxws.myticket.my.Entity;

/**
 * desc:
 * Date: 2016-11-24 16:00
 *
 * @author jiangyan
 */
public class WalletTypeEvent {

    public WalletTypeEvent(int type) {
        this.type = type;
    }

    private int type;//1 全部，2 收入，3 支出

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}