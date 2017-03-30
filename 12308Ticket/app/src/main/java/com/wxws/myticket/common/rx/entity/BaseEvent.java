package com.wxws.myticket.common.rx.entity;

/**
 * desc: rx 基类
 * Date: 2016/10/24 14:50
 *
 * @auther: lixiangxiang
 */
public class BaseEvent {

    public String eventTag;

    public BaseEvent(String eventTag) {
        this.eventTag = eventTag;
    }

    public String getEventTag() {
        return eventTag;
    }
}
