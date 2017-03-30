package com.wxws.myticket.train.entity;

import java.util.Comparator;

/**
 * desc: 按到时升序
 * Date: 2016/8/5 16:09
 *
 * @auther: lixiangxiang
 */
public class TrainArriveTimeModel implements Comparator<TrainListModel> {
    @Override
    public int compare(TrainListModel lhs, TrainListModel rhs) {



        int arriveTime1 = getArriveTime(lhs.getArriveTime(), lhs.getArriveDays());
        int arriveTime2 = getArriveTime(rhs.getArriveTime(), rhs.getArriveDays());

        int big = -1;
        try {
            if (arriveTime1 < arriveTime2) {
                big = -1;
            } else if (arriveTime1 > arriveTime2) {
                big = 1;
            } else {
                big = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return big;
    }

    private int getArriveTime(String arriveTime1, String arriveDay) {

        String[] time = arriveTime1.split(":");

        int i1 = Integer.valueOf(time[0]);
        int i2 = Integer.valueOf(time[1]);
        int i3 = Integer.valueOf(arriveDay);

        int iTime = i1 * 60 + i3 *24 * 60 + i2;

        return iTime;
    }
}
