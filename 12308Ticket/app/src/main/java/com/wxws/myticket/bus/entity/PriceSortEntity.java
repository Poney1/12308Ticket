package com.wxws.myticket.bus.entity;

import java.util.Comparator;

/**
 * desc: 降序 排列
 * Date: 2016/11/18 13:50
 *
 * @auther: lixiangxiang
 */
public class PriceSortEntity implements Comparator<TraditionBusListEntity>{

    @Override
    public int compare(TraditionBusListEntity o1, TraditionBusListEntity o2) {
        return o2.getDiscountPrice() - o1.getDiscountPrice();
    }
}
