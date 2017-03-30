package com.wxws.myticket.train.entity;

import java.util.Comparator;

/**
 * desc: 按到时升序
 * Date: 2016/8/5 16:09
 *
 * @auther: lixiangxiang
 */
public class TrainStratTimeModel implements Comparator<TrainListModel> {
    @Override
    public int compare(TrainListModel lhs, TrainListModel rhs) {

        int startTime1 = getArriveTime(lhs.getStartTime());
        int startTime2 = getArriveTime(rhs.getStartTime());

        int big = -1;
        try {
            if (startTime1 > startTime2) {
                big = 1;
            } else if (startTime1 < startTime2) {
                big = -1;
            } else {
                big = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return big;
    }

    private int getArriveTime(String arriveTime1) {

        String[] time = arriveTime1.split(":");

        int i1 = Integer.valueOf(time[0]);
        int i2 = Integer.valueOf(time[1]);

        int iTime = i1 * 60 + i2  ;

        return iTime;
    }
}
