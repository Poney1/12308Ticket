package com.wxws.myticket.train.entity;

import java.util.Comparator;

/**
 * desc: 筛选 坐席
 * Date: 2016/8/10 18:49
 *
 * @auther: lixiangxiang
 */
public class SeatSortModel implements Comparator<SeatListEntity> {
    @Override
    public int compare(SeatListEntity lhs, SeatListEntity rhs) {
        int big = 0;

        if (lhs.getIndex() < rhs.getIndex()) {
            big = -1;
        } else if (lhs.getIndex() > rhs.getIndex()) {
            big = 1;
        } else {
            big = 0;
        }
        return big;
    }
}
