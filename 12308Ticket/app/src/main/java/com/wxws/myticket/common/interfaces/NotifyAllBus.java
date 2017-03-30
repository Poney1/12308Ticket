package com.wxws.myticket.common.interfaces;

/**
 * desc:  刷新车次
 * user: lixiangxiang
 * date: 2016-04-12
 * time: 11:14
 */
public interface NotifyAllBus {

    /**
     * 各数据刷新的区别
     * @param where
     */
    void notifyData(int where);

}
