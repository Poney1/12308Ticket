package com.wxws.myticket.train.entity;

import java.util.Comparator;

/**
 * desc:
 * Date: 2016/8/5 16:49
 *
 * @auther: lixiangxiang
 */
public class TrainShortTimeModel implements Comparator<TrainListModel> {
    @Override
    public int compare(TrainListModel lhs, TrainListModel rhs) {
        int big = -1;

        if (Integer.valueOf(lhs.getRunTimeMinute()) < Integer.valueOf(rhs.getRunTimeMinute())){
            big = -1;
        }else if (Integer.valueOf(lhs.getRunTimeMinute()) > Integer.valueOf(rhs.getRunTimeMinute())){
            big = 1;
        }else {
            big = 0 ;
        }
        return big;
    }
}
