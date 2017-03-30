package com.wxws.myticket.bus.configs;

/**
 * desc: 汽车票 常量
 * Date: 2016/10/24 11:17
 *
 * @auther: lixiangxiang
 */
public class Constants {

    /**
     * 数据缓存
     */
    public final static String HISTORYCITY = "historycity"; //ticket 搜索记录
    public final static String APPOINTSTARTCITY = "appointstartCity";//约租车出发城市

    /**
     * 城市界面数据来源区分
     */
    public final  static int BUSSTARTCITY = 1; //汽车票出发城市
    public final  static int BUSENDCITY = 2;//汽车票到达城市
    public final  static int TRAINCITY = 3;//火车票城市
    public final  static int LineStartCity = 4;//城际快线出发城市
    public final  static int LineEndCity = 5;//城际快线到达城市
    public final  static int DirectStartCity = 6;//直通车出发城市
    public final  static int DirectEndCity = 7;//直通车到达城市


    /**
     * 城市增量
     */
    public final static String BUSSTARTVERSION = "busStartVersion";
    public final static String BUSENDVERSION = "busEndVersion";
    public final static String TRAINVERSION = "trainVersion";
    public final static String CITYLINESTARTVERSION = "cityLineStartVersion";

    /**
     * 出发城市 历史 缓存
     */
    public final static String CITYSTART  = "cityStart";
    public final static String CITYEND  = "cityEnd";
    public final static String CityLineHistory  = "CityLineHistory";
    public final static String CITYTRAIN  = "cityTrain";
    public final static String CITYLINESTART  = "cityLineStart";
    public final static String CITYLINEEND  = "cityLineEnd";
    public final static String DirectEnd  = "DirectEnd";
    public final static String DirectStart  = "cityLineStart";
    public final static String DirectStartRecord  = "DirectStartRecord";//直通车出发城市 记录
    public final static String DirectEndRecord  = "DirectEndRecord";//直通车到达城市 记录

    //定位信息
    public final static String LocationInfo= "LocationInfo";

    /**
     * 车次列表 班次状态
     *  0：暂停
     *  1：有票
     *  3: 售罄
     *  4：网上停售
     */
    public final  static String  busStop ="0";
    public final  static String  busHaveTicket ="1";
    public final  static String  busSaleAll ="3";
    public final  static String  busInternetStop ="5";

    /**
     * 票价类型 对应值
     */
    public final  static String  busAllTicket ="10";//全价票
    public final  static String  busHalfTicket ="15";//半价票
    public final  static String  busChildTicket ="17";//儿童票
    public final  static String  busCouponTicket ="40";//优惠票
}
