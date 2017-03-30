package com.wxws.myticket.train.entity;

import java.util.Comparator;

/**
 * desc:
 * Date: 2016/8/5 16:09
 *
 * @auther: lixiangxiang
 */
public class TrainPriceSortModel implements Comparator<TrainListModel> {
    @Override
    public int compare(TrainListModel lhs, TrainListModel rhs) {

        int wz1 = 0;
        int wz2 = 0;
        for (SeatListEntity bean1 : lhs.getSeatList()){
            if ("wz".equals(bean1.getType()) && bean1.getNum()>=0){
                 wz1  = bean1.getPrice();
            }
        }

        for (SeatListEntity bean2 : rhs.getSeatList()){
            if ("wz".equals(bean2.getType()) && bean2.getNum()>=0){
                 wz2  = bean2.getPrice();
            }
        }

        int big = -1;
        try {
            if (wz1 > wz2) {
                big = 1;
            } else if (wz1 < wz2) {
                big = -1;
            } else {
                big = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return big;
    }

}
