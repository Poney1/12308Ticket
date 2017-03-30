package com.wxws.myticket.order.utils;

/**
 * desc:
 * Date: 2016-11-02 14:06
 *
 * @author jiangyan
 */
public class SeatTypeUtils {

    //9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，4:软卧，3:硬卧，2:软座，1:硬座

    public static String getSeatName(String seatType){
        switch (seatType){
            case "9":
                return "商务座";
            case "P":
                return "特等座";
            case "M":
                return "一等座";
            case "O":
                return "二等座";
            case "6":
                return "高级软卧";
            case "4":
                return "软卧";
            case "3":
                return "硬卧";
            case "2":
                return "软座";
            case "1":
                return "硬座";
        }
        return "无座";
    }
}