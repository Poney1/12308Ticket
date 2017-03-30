package com.wxws.myticket.common.constants;

/**
 * desc: 各种 模块标识
 * Date: 2016/10/24 17:00
 *
 * @auther: lixiangxiang
 */
public class ModuleConstants {

    /**
     * bizType
     * 0：汽车票
     * 1：旅游快线
     * 2：城际专线
     * 3：机场大巴
     * 4：港澳专线
     * 6：约租车
     * 7：船票
     * 8：飞机票
     * 9：包车
     * 10：火车票
     * 11:直通车
     */
    public final static  String bizTypeBus ="0";
    public final static  String bizTypeTravel ="1";
    public final static  String bizTypeCityLine ="2";
    public final static  String bizTypeAirCraftBus ="3";
    public final static  String bizTypeGangao ="4";
    public final static  String bizTypeAppoint ="6";
    public final static  String bizTypeBoat ="7";
    public final static  String bizTypeFly ="8";
    public final static  String bizTypeRentCar ="9";
    public final static  String bizTypeTrain ="10";
    public final static  String bizTypeDirect ="11";

    /**
     * 订单类型
     * 0：全部订单
     * 1：汽车票订单
     * 2：火车票订单
     * 3：直通车订单
     * 4：我要包车订单
     * 5：约租车订单
     */
    public final static int orderTypeAll = 0;
    public final static int orderTypeBus = 1;
    public final static int orderTypeTrain = 2;
    public final static int orderTypeDirect = 3;
    public final static int orderTypeBaoche = 4;
    public final static int orderTypeYuezu = 5;

    /**
     * 是否失效
     * 0 失效订单、
     * 1 有效订单、
     * 2最近订单、
     * 3历史订单
     */
    public final static int validTypeValid = 1;
    public final static int validTypeInvalid = 0;
    public final static int validTypeRecent = 2;
    public final static int validTypeHistory = 3;

}
