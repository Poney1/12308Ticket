package com.wxws.myticket.train.entity;

import java.util.Comparator;

/**
 * desc: 火车票下单选座,数据排序
 * Date: 2016-08-01 17:02
 *
 * @auther: jiangyan
 */
public class ListSortComparator implements Comparator {

    @Override
    public int compare(Object lhs, Object rhs) {

        SeatinfoModel a = (SeatinfoModel) lhs;
        SeatinfoModel b = (SeatinfoModel) rhs;

        return (a.getLevel() - b.getLevel());

    }

}