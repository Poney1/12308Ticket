package com.wxws.myticket.common.net.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.wxws.myticket.bus.dao.BusEndCity;
import com.wxws.myticket.bus.dao.BusStartCity;
import com.wxws.myticket.bus.entity.ActiveEntity;
import com.wxws.myticket.bus.entity.AircraftCityEntity;
import com.wxws.myticket.bus.entity.BaocheBeforetimePara;
import com.wxws.myticket.bus.entity.BaocheBusType;
import com.wxws.myticket.bus.entity.BaocheMapCity;
import com.wxws.myticket.bus.entity.BaocheNoticeEntity;
import com.wxws.myticket.bus.entity.BaochePriceResponse;
import com.wxws.myticket.bus.entity.BaocheSubmitRequest;
import com.wxws.myticket.bus.entity.BaocheSubmitResponse;
import com.wxws.myticket.bus.entity.BusInsuranceEntity;
import com.wxws.myticket.bus.entity.BusStationListEntity;
import com.wxws.myticket.bus.entity.CityLineEndEntity;
import com.wxws.myticket.bus.entity.GangAoStartCityEntity;
import com.wxws.myticket.bus.entity.LineDetailEntity;
import com.wxws.myticket.bus.entity.OrderIdEntity;
import com.wxws.myticket.bus.entity.StationDescEntity;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.bus.entity.TraditionTotalEntity;
import com.wxws.myticket.bus.entity.VersionInfo;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.my.Entity.BindListResponsePara;
import com.wxws.myticket.my.Entity.CouponResponsePara;
import com.wxws.myticket.my.Entity.LoginResponsePara;
import com.wxws.myticket.my.Entity.MessageListResponsePara;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.Entity.QuestionResponsePara;
import com.wxws.myticket.my.Entity.ShareInfo;
import com.wxws.myticket.my.Entity.TrainLoginResponseModel;
import com.wxws.myticket.order.entity.ApplyRefundRequestPara;
import com.wxws.myticket.order.entity.BusMapResponsePara;
import com.wxws.myticket.order.entity.CancelOrderModel;
import com.wxws.myticket.order.entity.EndorseFailureModel;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;
import com.wxws.myticket.order.entity.RefundConfirmModel;
import com.wxws.myticket.order.entity.RefundModel;
import com.wxws.myticket.order.entity.RefundPriceResponsePara;
import com.wxws.myticket.order.entity.CommonOrderListResponse;
import com.wxws.myticket.order.entity.TicketOperateResponse;
import com.wxws.myticket.order.entity.TrainOrderdetailsResponseModel;
import com.wxws.myticket.order.entity.TrainorderlistPara;
import com.wxws.myticket.pay.PayParamModel;
import com.wxws.myticket.pay.PaymentEntity;
import com.wxws.myticket.train.entity.ChangeTicketResultEntity;
import com.wxws.myticket.train.entity.ChooseSeatModel;
import com.wxws.myticket.train.entity.CommitOrderResponseModel;
import com.wxws.myticket.train.entity.CreateTrainOrderModel;
import com.wxws.myticket.train.entity.LockTicketResultEntity;
import com.wxws.myticket.train.entity.ProvinceModel;
import com.wxws.myticket.train.entity.SchoolModel;
import com.wxws.myticket.train.entity.TrainInsuranceModel;
import com.wxws.myticket.train.entity.TrainListModel;
import com.wxws.myticket.train.entity.TrainStartCityModel;
import com.wxws.myticket.train.entity.TrainStationInfoModel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * desc: 网络请求管理类
 * Date: 2016/10/8 15:06
 *
 * @auther: lixiangxiang
 */
public class HttpRequestManager extends HttpRequestManagerParent {

    private ApiService mApiService;//接口管理类

    private static HttpRequestManager httpRequestManager;

    private HttpRequestManager() {
    }

    /**
     * 静态内部类单例模式，解决资源消耗、多余同步、线程安全问题，同时处理DCL模式下的失效问题。
     */
    public static HttpRequestManager getInstance() {
        return HttpInstanceHolder.instance;
    }

    private static class HttpInstanceHolder {
        private static final HttpRequestManager instance = new HttpRequestManager();
    }

    /**
     * 获取版本号
     *
     * @param subscriber
     * @param versionCode
     */
    public void getVersion(int versionCode, ApiSubscriber<JsonResult<VersionInfo>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("version", versionCode + "");

        ApiService mApiService = sendApiServiceRequest(params);

        Observable observable = mApiService.getVersion(params)
                .map(new HttpResultFunc<VersionInfo>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取 活动广告
     *
     * @param subscriber
     */
    public void getActiveContent(ApiSubscriber<JsonResult<List<ActiveEntity>>> subscriber) {
        ApiService mApiService = sendApiServiceRequest(null);
        Observable observable = mApiService.getActiveContent()
                .map(new HttpResultFunc<List<ActiveEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取 弹窗广告
     *
     * @param subscriber
     */
    public void getAlertAd(ApiSubscriber<JsonResult<ActiveEntity>> subscriber) {
        ApiService mApiService = sendApiServiceRequest(null);
        Observable observable = mApiService.getAlertAd()
                .map(new HttpResultFunc<ActiveEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取支付条件
     *
     * @param subscriber
     * @param payChannel
     * @param orderId
     * @param amount
     */
    public void getPayParam(String payChannel, String orderId, String amount, String bizType, ApiSubscriber<JsonResult<PayParamModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("amount", String.valueOf(amount));
        params.put("payChannel", String.valueOf(payChannel));
        params.put("bizType", bizType);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.getPayParam(params).map(new HttpResultFunc<PayParamModel>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取汽车票出发城市
     *
     * @param subscriber
     * @param version
     */
    public void fromCityList(int version, ApiSubscriber<JsonResult<List<BusStartCity>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("version", String.valueOf(version));

        mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.fromCityList(params).map(new HttpResultFunc<List<BusStartCity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 汽车票到达城市
     *
     * @param subscriber
     * @param version
     * @param fromCityId
     */
    public void toCityList(int version, @NonNull String fromCityId, String fromCityName, ApiSubscriber<JsonResult<List<BusEndCity>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("version", String.valueOf(version));
        params.put("fromCityId", fromCityId);
        params.put("fromCityName", fromCityName);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.toCityList(params).map(new HttpResultFunc<List<BusEndCity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 汽车票出发到达站点
     *
     * @param keyWord         是	用户输入的搜索关键字
     * @param type            是	搜索类型  0：出发 1：到达
     * @param fromCityId      否	出发城市id type=1时必传
     * @param fromCityName    否	到达站点id type=1时必传
     * @param fromStationId   否	到达站点名 type=1时必传
     * @param fromStationName 否	到达站点名 type=1时必传
     * @param subscriber
     */
    public void fromToStationList(String keyWord, String type, String fromCityId, String fromCityName, String fromStationId, String fromStationName, ApiSubscriber<JsonResult<BusStationListEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("keyWord", keyWord);
        params.put("type", type);
        params.put("fromStationId", fromStationId);
        params.put("fromCityId", fromCityId);
        params.put("fromCityName", fromCityName);
        params.put("fromStationName", fromStationName);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.fromToStationList(params).map(new HttpResultFunc<BusStationListEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 港澳快线出发城市
     *
     * @param subscriber
     */
    public void fromGangAoList(ApiSubscriber<JsonResult<List<GangAoStartCityEntity>>> subscriber) {
        ApiService mApiService = sendApiServiceRequest(null);
        Observable observable = mApiService.fromGangAoList().map(new HttpResultFunc<List<GangAoStartCityEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 港澳快线到达城市
     *
     * @param subscriber
     */
    public void toGangAoList(String fromCityId, String fromStationId, ApiSubscriber<JsonResult<List<GangAoStartCityEntity>>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("fromStationId", String.valueOf(fromStationId));
        params.put("fromCityId", fromCityId);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.toGangAoList(params).map(new HttpResultFunc<List<GangAoStartCityEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 汽车票列表
     *
     * @param subscriber
     * @param fromCityId
     * @param fromCityName
     * @param toCityId
     * @param toCityName
     * @param departDate
     * @param bizType
     * @param pageNum
     * @param pageCount
     */
    public void busLineList(String fromCityId, String fromCityName, String toCityId,
                            String toCityName, String departDate, String bizType, int pageNum,
                            int pageCount, ApiSubscriber<JsonResult<TraditionTotalEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("pageCount", String.valueOf(pageCount));
        params.put("pageNum", String.valueOf(pageNum));
        params.put("fromCityName", fromCityName);
        params.put("toCityId", toCityId);
        params.put("toCityName", toCityName);
        params.put("fromCityId", fromCityId);
        params.put("departDate", departDate);
        params.put("bizType", bizType);

        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.busLineList(params).map(new HttpResultFunc<TraditionTotalEntity>());
        toSubscribe(observable, subscriber);

    }

    /**
     * 获取汽车票列表 保险
     *
     * @param busId
     * @param bizType
     * @param subscriber
     */
    public void busInsuranceList(String busId, String bizType, String fromStationCode, String toStationCode,
                                 String departDate, ApiSubscriber<JsonResult<List<BusInsuranceEntity>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("checiId", busId);
        params.put("bizType", bizType);
        params.put("fromStationCode", fromStationCode);
        params.put("departDate", departDate);
        params.put("toStationCode", toStationCode);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.busInsuranceList(params).map(new HttpResultFunc<List<BusInsuranceEntity>>());
        toSubscribe(observable, subscriber);
    }
    /**
     * departDate	string	param	是	出发日期
     scheduleId	string	param	是	班次号
     fromStationId	string	param	否	出发站点Id
     toStationId	string	param	否	到达站点Id
     isBack	string	param	是	是否开启备选车次按钮
     0：否
     1：是
     couponId	string	param	否	优惠券id
     adultCount	int	param	否	成人数量（正常购票）
     halfCount	int	param	否	半价票数量（正常购票）
     childCount	int	param	否	儿童数量（正常购票）
     passengerList	list	param	否	成人乘客列表（实名购票）
     id 乘客id
     ticketType 票类型-班次详情中的价格中的价格类型
     insuranceId 保险id
     contactName	string	param	否	取票人姓名（实名购票）
     contactCertNo	string	param	否	取票证件号（实名购票）
     contactId	string	param	否	取票人常用联系人id（实名购票）
     contactMobile	string	param	是	取票人手机号
     orderMoney	int	param	是	订单金额（支付金额）
     openFreeInsurance	string	param	是	是否开启获取免费保险
     0：否
     1：是
     isRealName	string	param	是	是否实名购票
     0：否
     1：是
     bizType	string	param	是	业务类型departDate	string	param	是	出发日期
     */
    /**
     * 获取汽车票列表 保险
     *
     * @param subscriber
     */
    public void submitOrder(String departDate, String scheduleId, String fromStationId, String toStationId,
                            String isBack, String couponId, String adultCount, String childCount, String halfCount,
                            String passengerList, String contactName, String contactCertNo, String contactId, String contactMobile, String orderMoney,
                            String openFreeInsurance, String isRealName, String bizType, ApiSubscriber<JsonResult<OrderIdEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("departDate", departDate);
        params.put("scheduleId", scheduleId);
        params.put("fromStationId", fromStationId);
        params.put("toStationId", toStationId);
        params.put("couponId", couponId);
        params.put("adultCount", adultCount);
        params.put("childCount", childCount);
        params.put("halfCount", halfCount);
        params.put("passengerList", passengerList);
        params.put("contactName", contactName);
        params.put("contactCertNo", contactCertNo);
        params.put("contactId", contactId);
        params.put("contactMobile", contactMobile);
        params.put("orderMoney", orderMoney);
        params.put("openFreeInsurance", openFreeInsurance);
        params.put("isRealName", isRealName);
        params.put("bizType", bizType);
        params.put("isBack", isBack);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.submitOrder(params).map(new HttpResultFunc<OrderIdEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 港澳快线
     *
     * @param fromCityId      param	是	出发城市Id
     * @param fromCityName    string	param	是	出发城市名
     * @param fromStationId   string	param	是	出发站点Id
     * @param fromStationName string	param	是	出发站点名
     * @param toCityId        string	param	是	到达城市Id
     * @param toCityName      string	param	是	到达城市名
     * @param toStationId     string	param	是	到达站点Id
     * @param toStationName   string	param	是	到达站点名
     * @param departDate      string	param	是	出发日期
     * @param bizType         string	param	是	业务类型
     * @param pageNum         int	param	是	当前页码
     * @param pageCount       int 	param	是	每页显示条数
     * @param subscriber
     */
    public void busLineGangaoList(String fromCityId, String fromCityName, String fromStationId, String fromStationName,
                                  String toCityId, String toCityName, String toStationId, String toStationName, String departDate,
                                  String bizType, String pageNum, String pageCount, ApiSubscriber<JsonResult<TraditionTotalEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("pageCount", String.valueOf(pageCount));
        params.put("pageNum", String.valueOf(pageNum));
        params.put("fromCityName", fromCityName);
        params.put("toCityId", toCityId);
        params.put("toCityName", toCityName);
        params.put("fromCityId", fromCityId);
        params.put("departDate", departDate);
        params.put("fromStationId", fromStationId);
        params.put("bizType", bizType);
        params.put("fromStationName", fromStationName);
        params.put("toStationId", toStationId);
        params.put("toStationName", toStationName);

        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.busLineList(params).map(new HttpResultFunc<TraditionTotalEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 直通车、城际快线 请求参数
     *
     * @param fromLongitude string	param	是	地理经度
     * @param fromLatitude  string	param	是	地理纬度
     * @param toLongitude   string	param	是	地理纬度
     * @param toLatitude    string	param	是	地理纬度
     * @param departDate    string	param	是	出发日期
     * @param bizType       string	param	是	业务类型
     * @param pageNum       int	param	是	当前页码
     * @param pageCount     int	param	是	每页显示条数
     * @param subscriber
     */
    public void busCityLineList(String fromLongitude, String fromLatitude, String toLongitude, String toLatitude, String departDate, String bizType, String pageNum,
                                String pageCount, ApiSubscriber<JsonResult<TraditionTotalEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("pageCount", String.valueOf(pageCount));
        params.put("pageNum", String.valueOf(pageNum));
        params.put("departDate", departDate);
        params.put("fromLongitude", fromLongitude);
        params.put("bizType", bizType);
        params.put("fromLatitude", fromLatitude);
        params.put("toLongitude", toLongitude);
        params.put("toLatitude", toLatitude);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.busLineList(params).map(new HttpResultFunc<TraditionTotalEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 机场快线
     *
     * @param airId      string	param	是	机场Id
     * @param airName    string	param	是	机场名
     * @param departDate string	param	是	出发日期
     * @param bizType    string	param	是	业务类型
     * @param pageNum    int	param	是	当前页码
     * @param pageCount  int	param	是	每页显示条数
     * @param subscriber
     */
    public void busAircraftList(String airId, String airName, String departDate, String bizType, String pageNum,
                                String pageCount, ApiSubscriber<JsonResult<TraditionTotalEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("pageCount", String.valueOf(pageCount));
        params.put("pageNum", String.valueOf(pageNum));
        params.put("departDate", departDate);
        params.put("airId", airId);
        params.put("bizType", bizType);
        params.put("airName", airName);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.busLineList(params).map(new HttpResultFunc<TraditionTotalEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 机场快线城市
     *
     * @param subscriber
     */
    public void aircraftCityList(int version, ApiSubscriber<JsonResult<List<AircraftCityEntity>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("version", String.valueOf(version));
        ApiService mApiService = sendApiServiceRequest(null);
        Observable observable = mApiService.airList(params).map(new HttpResultFunc<List<AircraftCityEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 城际快线，直通车出发城市
     *
     * @param version
     * @param subscriber
     */
    public void cityLineFromCityList(int version, ApiSubscriber<JsonResult<List<BusStartCity>>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("version", String.valueOf(version));
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.cityLineFromCityList(params).map(new HttpResultFunc<List<BusStartCity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 城际快线，直通车到达城市
     *
     * @param fromCityId
     * @param fromCityName
     * @param subscriber
     */
    public void cityLineToCityList(String fromCityId, String fromCityName, ApiSubscriber<JsonResult<List<CityLineEndEntity>>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("fromCityId", fromCityId);
        params.put("fromCityName", fromCityName);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.cityLineToCityList(params).map(new HttpResultFunc<List<CityLineEndEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 车次详情
     *
     * @param bizType
     * @param departDate string	param	是	出发日期
     * @param scheduleId string	param	是	班次号
     * @param subscriber
     */
    public void getLineDetail(String bizType, String departDate, String scheduleId, ApiSubscriber<JsonResult<LineDetailEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("departDate", departDate);
        params.put("scheduleId", scheduleId);
        params.put("bizType", bizType);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.getLineDetail(params).map(new HttpResultFunc<LineDetailEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 汽车票顶部推荐
     * fromCityId	string	param	是	出发城市Id
     * fromCityName	string	param	是	出发城市名
     * toCityId	string	param	是	到达城市Id
     * toCityName	string	param	是	到达城市名
     * departDate	string	param	是	出发日期
     *
     * @param bizType
     * @param fromCityId
     * @param fromCityName
     * @param toCityId
     * @param toCityName
     * @param departDate
     * @param subscriber
     */
    public void recommend(String bizType, String fromCityId, String fromCityName, String toCityId, String toCityName, String departDate, ApiSubscriber<JsonResult<TraditionBusListEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("bizType", bizType);
        params.put("fromCityId", StringUtils.isNullOrEmpty(fromCityId) ? "" : fromCityId);
        params.put("fromCityName", fromCityName);
        params.put("toCityId", StringUtils.isNullOrEmpty(toCityId) ? "" : toCityId);
        params.put("toCityName", toCityName);
        params.put("departDate", departDate);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.recommend(params).map(new HttpResultFunc<TraditionBusListEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 汽车票根据出发城市获取所有的出发站点
     *
     * @param longitude
     * @param latitude
     * @param fromCityName
     * @param fromCityId
     * @param subscriber
     */
    public void fromCityStation(String longitude, String latitude, String fromCityName, String fromCityId,
                                ApiSubscriber<JsonResult<List<StationDescEntity>>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("fromCityName", fromCityName);
        params.put("fromCityId", fromCityId);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.fromCityStation(params).map(new HttpResultFunc<List<StationDescEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 火车票所有出发站点
     *
     * @param version
     * @param subscriber
     */
    public void trainStationList(String version, ApiSubscriber<JsonResult<List<TrainStartCityModel>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("version", version);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.trainStationList(params).map(new HttpResultFunc<List<TrainStartCityModel>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * * 火车票余票查询
     * 名称	类型	位置	必填	描述
     * startDate	string	param	是	乘车日期（yyyy-MM-dd）
     * fromStationName	string	param	是	出发站名称
     * fromStationCode	string	param	是	出发站编码
     * toStationName	string	param	是	到达站名称
     * toStationCode	string	param	是	到达站编码
     * onlyHighSpeed	boolean	param	否	只看高铁动车：true/false
     *
     * @param startDate
     * @param fromStationName
     * @param fromStationCode
     * @param toStationName
     * @param toStationCode
     * @param onlyHighSpeed
     * @param subscriber
     */
    public void trainTicketList(String startDate, String fromStationName, String fromStationCode, String toStationName, String toStationCode, boolean onlyHighSpeed, ApiSubscriber<JsonResult<List<TrainListModel>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("fromStationName", fromStationName);
        params.put("fromStationCode", fromStationCode);
        params.put("toStationName", toStationName);
        params.put("toStationCode", toStationCode);
        params.put("onlyHighSpeed", String.valueOf(onlyHighSpeed));
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.trainTicketList(params).map(new HttpResultFunc<List<TrainListModel>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * *    * 火车票经停站点
     * 名称	类型	位置	必填	描述
     * startDate	string	param	是	列车始发日期，使用余票接口中的trainStartDate（yyyy-MM-dd）
     * fromStationName	string	param	是	出发站名称
     * fromStationCode	string	param	是	出发站编码
     * toStationName	string	param	是	到达站名称
     * toStationCode	string	param	是	到达站编码
     * trainCode	string	param	是	车次
     * trainNo	string	param	是	车次内部编码，如：G72123456
     *
     * @param startDate
     * @param fromStationName
     * @param fromStationCode
     * @param toStationName
     * @param toStationCode
     * @param trainCode
     * @param trainNo
     * @param subscriber
     */
    public void stopOverStation(String startDate, String fromStationName, String fromStationCode,
                                String toStationName, String toStationCode, String trainCode, String trainNo, ApiSubscriber<JsonResult<TrainStationInfoModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("fromStationName", fromStationName);
        params.put("fromStationCode", fromStationCode);
        params.put("toStationName", toStationName);
        params.put("toStationCode", toStationCode);
        params.put("trainCode", trainCode);
        params.put("trainNo", trainNo);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.stopOverStation(params).map(new HttpResultFunc<TrainStationInfoModel>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 创建火车票订单
     *
     * @param commitPara
     * @param subscriber
     */
    public void trainOrderCreate(CreateTrainOrderModel commitPara, ApiSubscriber<JsonResult<CommitOrderResponseModel>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("startDate", commitPara.getStartDate());
        params.put("fromStationCode", commitPara.getFromStationCode());
        params.put("toStationCode", commitPara.getToStationCode());
        params.put("trainCode", commitPara.getTrainCode());
        params.put("mobile", commitPara.getMobile());
        params.put("trainNo", commitPara.getTrainNo());
        params.put("acceptStanding", String.valueOf(commitPara.isAcceptStanding()));
        try {
            params.put("ticketList", JSON.toJSONString(commitPara.getTicketList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.trainOrderCreate(params).map(new HttpResultFunc<CommitOrderResponseModel>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 占座查询
     *
     * @param orderId
     * @param subscriber
     */
    public void trainCreateResult(String orderId, ApiSubscriber<JsonResult<LockTicketResultEntity>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.trainCreateResult(params).map(new HttpResultFunc<LockTicketResultEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 改签申请
     *
     * @param orderId
     * @param startDate
     * @param fromStationCode
     * @param toStationCode
     * @param trainCode
     * @param trainNo
     * @param seatType
     * @param ticketList
     * @param subscriber
     */
    public void requestChange(String orderId, String startDate, String fromStationCode, String toStationCode, String trainCode,
                              String trainNo, String seatType, String ticketList, ApiSubscriber<JsonResult<CommitOrderResponseModel>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("startDate", startDate);
        params.put("fromStationCode", fromStationCode);
        params.put("toStationCode", toStationCode);
        params.put("trainCode", trainCode);
        params.put("trainNo", trainNo);
        params.put("seatType", seatType);
        params.put("ticketList", ticketList);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.requestChange(params).map(new HttpResultFunc<CommitOrderResponseModel>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 改签占座结果
     *
     * @param orderId
     * @param subscriber
     */
    public void requestChangeResult(String orderId, ApiSubscriber<JsonResult<ChangeTicketResultEntity>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.requestChangeResult(params).map(new HttpResultFunc<ChangeTicketResultEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 车次查询
     * startDate	string	param	是	乘车日期（yyyy-MM-dd）
     * fromStationName	string	param	是	出发站名称
     * fromStationCode	string	param	是	出发站编码
     * toStationName	string	param	是	到达站名称
     * toStationCode	string	param	是	到达站编码
     * trainCode	string	param	是	车次
     * trainNo	string	param	是	车次编码
     *
     * @param startDate
     * @param fromStationName
     * @param fromStationCode
     * @param toStationName
     * @param toStationCode
     * @param trainCode
     * @param trainNo
     * @param subscriber
     */
    public void trainLineTicket(String startDate, String fromStationName, String fromStationCode,
                                String toStationName, String toStationCode, String trainCode, String trainNo, ApiSubscriber<JsonResult<ChooseSeatModel>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("fromStationName", fromStationName);
        params.put("fromStationCode", fromStationCode);
        params.put("toStationName", toStationName);
        params.put("toStationCode", toStationCode);
        params.put("trainCode", trainCode);
        params.put("trainNo", trainNo);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.trainLineTicket(params).map(new HttpResultFunc<ChooseSeatModel>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取火车票保险详情
     *
     * @param subscriber
     */
    public void trainInsuranceList(String checiId, String bizType, String fromStationCode, String fromStationName, String toStationCode, String toStationName, ApiSubscriber<JsonResult<List<TrainInsuranceModel>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("checiId", checiId);
        params.put("bizType", bizType);
        params.put("fromStationCode", fromStationCode);
        params.put("fromStationName", fromStationName);
        params.put("toStationCode", toStationCode);
        params.put("toStationName", toStationName);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.trainInsuranceList(params).map(new HttpResultFunc<List<TrainInsuranceModel>>());
        toSubscribe(observable, subscriber);
    }


    /**
     * 支付方式获取
     *
     * @param subscriber
     */
    public void clientPayType(ApiSubscriber<JsonResult<List<PaymentEntity>>> subscriber) {
        ApiService mApiService = sendApiServiceRequest(null);
        Observable observable = mApiService.clientPayType().map(new HttpResultFunc<List<PaymentEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取验证码
     *
     * @param type
     * @param recipient
     * @param subscriber
     */
    public void getCode(String type, String recipient, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("recipient", recipient);
        toSubscribe(sendApiServiceRequest(params).getCode(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 用户名密码登录
     */

    public void login(String password, String mobilePhone, ApiSubscriber<JsonResult<LoginResponsePara>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("mobilePhone", mobilePhone);

        toSubscribe(sendApiServiceRequest(params).login(params).map(new HttpResultFunc<LoginResponsePara>()), subscriber);
    }

    /**
     * 验证码登录
     *
     * @param mobilePhone
     * @param captcha
     * @param subscriber
     */
    public void smsLogin(String mobilePhone, String captcha, String nonce, ApiSubscriber<JsonResult<LoginResponsePara>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("mobilePhone", mobilePhone);
        params.put("captcha", captcha);
        params.put("nonce", nonce);

        toSubscribe(sendApiServiceRequest(params).smslogin(params).map(new HttpResultFunc<LoginResponsePara>()), subscriber);
    }

    /**
     * 第三方登录
     *
     * @param openType
     * @param openId
     * @param nickName
     * @param photo
     * @param subscriber
     */
    public void thridLogin(String openType, String openId, String nickName, String photo, ApiSubscriber<JsonResult<LoginResponsePara>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("openType", openType);
        params.put("openId", openId);
        params.put("nickName", nickName);
        params.put("photo", photo);

        toSubscribe(sendApiServiceRequest(params).thridlogin(params).map(new HttpResultFunc<LoginResponsePara>()), subscriber);
    }

    /**
     * 重置密码
     *
     * @param captcha
     * @param mobilePhone
     * @param password
     * @param subscriber
     */
    public void resetPsw(String captcha, String mobilePhone, String nonce, String password, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("captcha", captcha);
        params.put("mobilePhone", mobilePhone);
        params.put("nonce", nonce);
        params.put("password", password);

        toSubscribe(sendApiServiceRequest(params).resetPsw(params).map(new HttpResultFunc<String>()), subscriber);

    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param subscriber
     */
    public void modifyPsw(String oldPassword, String newPassword, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);

        toSubscribe(sendApiServiceRequest(params).modifyPsw(params).map(new HttpResultFunc<String>()), subscriber);
    }


    /**
     * 获取联系人列表
     *
     * @param bizType    火车票 10  其它不需要传
     * @param subscriber
     */
    public void getContactList(String bizType, ApiSubscriber<JsonResult<List<PassengerInfoEntity>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("bizType", bizType);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.getContactList(params).map(new HttpResultFunc<List<PassengerInfoEntity>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 删除联系人列表
     *
     * @param passengerId
     * @param subscriber
     */
    public void delContact(String passengerId, ApiSubscriber<JsonResult<Void>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("passengerId", passengerId);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.delContact(params).map(new HttpResultFunc<Void>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取 省份列表
     *
     * @param subscriber
     */
    public void getProvinceList(ApiSubscriber<JsonResult<List<ProvinceModel>>> subscriber) {
        ApiService mApiService = sendApiServiceRequest(null);
        Observable observable = mApiService.getProvinceList().map(new HttpResultFunc<List<ProvinceModel>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取 省份学校
     *
     * @param subscriber
     */
    public void getSchoolList(String provinceId, String version, ApiSubscriber<JsonResult<List<SchoolModel>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("provinceId", provinceId);
        params.put("version", version);
        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.getSchoolList(params).map(new HttpResultFunc<List<SchoolModel>>());
        toSubscribe(observable, subscriber);
    }

    /**
     * @param name                      必传
     * @param mobile                    必传
     * @param certNo                    必传
     * @param bizType                   0：汽车票 10:火车票
     * @param gender
     * @param certType
     * @param personType
     * @param provinceCode
     * @param provinceName
     * @param schoolName
     * @param schoolCode                string	param	否	学校编码
     * @param studentNo
     * @param schoolSystem
     * @param enterYear
     * @param preferenceCardNo
     * @param preferenceFromStationName
     * @param preferenceFromStationCode
     * @param preferenceToStationName
     * @param preferenceToStationCode
     * @param subscriber
     */

    public void addContactList(String name, String mobile, String certNo, String bizType, String gender, String certType, String personType, String provinceCode, String provinceName, String schoolName, String schoolCode,
                               String studentNo, String schoolSystem, String enterYear, String preferenceCardNo, String preferenceFromStationName, String preferenceFromStationCode, String preferenceToStationName, String preferenceToStationCode, ApiSubscriber<JsonResult<PassengerInfoEntity>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("mobile", mobile);
        params.put("certNo", certNo);
        params.put("bizType", bizType);
        params.put("gender", StringUtils.isNullOrEmpty(gender) ? "" : gender);
        params.put("certType", StringUtils.isNullOrEmpty(certType) ? "" : certType);
        params.put("personType", StringUtils.isNullOrEmpty(personType) ? "" : personType);
        params.put("provinceCode", StringUtils.isNullOrEmpty(provinceCode) ? "" : provinceCode);
        params.put("provinceName", StringUtils.isNullOrEmpty(provinceName) ? "" : provinceName);
        params.put("schoolName", StringUtils.isNullOrEmpty(schoolName) ? "" : schoolName);
        params.put("studentNo", StringUtils.isNullOrEmpty(studentNo) ? "" : studentNo);
        params.put("schoolSystem", StringUtils.isNullOrEmpty(schoolSystem) ? "" : schoolSystem);
        params.put("enterYear", StringUtils.isNullOrEmpty(enterYear) ? "" : enterYear);
        params.put("preferenceCardNo", StringUtils.isNullOrEmpty(preferenceCardNo) ? "" : preferenceCardNo);
        params.put("preferenceFromStationName", StringUtils.isNullOrEmpty(preferenceFromStationName) ? "" : preferenceFromStationName);
        params.put("preferenceToStationCode", StringUtils.isNullOrEmpty(preferenceToStationCode) ? "" : preferenceToStationCode);
        params.put("preferenceFromStationCode", StringUtils.isNullOrEmpty(preferenceFromStationCode) ? "" : preferenceFromStationCode);
        params.put("preferenceToStationName", StringUtils.isNullOrEmpty(preferenceToStationName) ? "" : preferenceToStationName);
        params.put("schoolCode", StringUtils.isNullOrEmpty(schoolCode) ? "" : schoolCode);

        ApiService mApiService = sendApiServiceRequest(params);
        Observable observable = mApiService.addContact(params).map(new HttpResultFunc<PassengerInfoEntity>());
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取订单列表
     *
     * @param status
     * @param orderType
     * @param pageNum
     * @param pageCount
     */
    public void getOrderList(String status, String orderType, String pageNum, String pageCount, ApiSubscriber<JsonResult<CommonOrderListResponse>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        params.put("orderType", orderType);
        params.put("pageNum", pageNum);
        params.put("pageCount", pageCount);

        toSubscribe(sendApiServiceRequest(params).getOrderList(params).map(new HttpResultFunc<CommonOrderListResponse>()), subscriber);
    }

    /**
     * 火车票订单列表
     *
     * @param pageSize
     * @param currentPage
     * @param type
     * @param subscriber
     */
    public void getTrainOrderList(int pageSize, int currentPage, int type, ApiSubscriber<JsonResult<TrainorderlistPara>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", pageSize + "");
        params.put("pageNum", currentPage + "");
        params.put("type", type + "");
        toSubscribe(sendNewApiServiceRequest(params).getTrainOrderList(params).map(new HttpResultFunc<TrainorderlistPara>()), subscriber);
    }

    /**
     * 火车票订单详情
     *
     * @param orderNo
     * @param subscribe
     */
    public void getTrainOrderdetails(String orderNo, ApiSubscriber<JsonResult<TrainOrderdetailsResponseModel>> subscribe) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderNo);
        toSubscribe(sendNewApiServiceRequest(params).getTrainOrderDetails(params).map(new HttpResultFunc<TrainOrderdetailsResponseModel>()), subscribe);
    }

    /**
     * 火车票操作详情
     *
     * @param orderId
     * @param subscriber
     */
    public void getTrainOperateDetails(String orderId, ApiSubscriber<JsonResult<TicketOperateResponse>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        toSubscribe(sendNewApiServiceRequest(params).getTrainOperateDetails(params).map(new HttpResultFunc<TicketOperateResponse>()), subscriber);
    }

    /**
     * 火车票取消改签
     *
     * @param originId
     * @param endorseId
     * @param subscriber
     */
    public void trainCancelEndorse(String originId, String endorseId, ApiSubscriber<JsonResult<EndorseFailureModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", originId);
        params.put("newOrderId", endorseId);
        toSubscribe(sendNewApiServiceRequest(params).trainCancelEndorse(params).map(new HttpResultFunc<EndorseFailureModel>()), subscriber);
    }

    /**
     * 火车票删除订单
     *
     * @param orderId
     * @param subscriber
     */
    public void trainDeleteOrder(String orderId, ApiSubscriber<JsonResult<TrainOrderdetailsResponseModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        toSubscribe(sendNewApiServiceRequest(params).trainDeleteOrder(params).map(new HttpResultFunc<TrainOrderdetailsResponseModel>()), subscriber);
    }

    /**
     * 火车票取消订单
     *
     * @param orderId
     * @param subscriber
     */
    public void trainCancelOrder(String orderId, ApiSubscriber<JsonResult<CancelOrderModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        toSubscribe(sendNewApiServiceRequest(params).trainCancelOrder(params).map(new HttpResultFunc<CancelOrderModel>()), subscriber);
    }

    /**
     * 火车票申请退票
     *
     * @param orderId
     * @param ticketid
     * @param subscriber
     */
    public void trainApplyRefund(String orderId, String ticketid, ApiSubscriber<JsonResult<RefundModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("ticketList", ticketid);
        toSubscribe(sendNewApiServiceRequest(params).trainApplyRefund(params).map(new HttpResultFunc<RefundModel>()), subscriber);
    }

    /**
     * 火车票确认退票
     *
     * @param orderId
     * @param ticketList
     * @param subscriber
     */
    public void trainConfirmRefund(String orderId, String ticketList, ApiSubscriber<JsonResult<RefundConfirmModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("ticketList", ticketList);
        toSubscribe(sendNewApiServiceRequest(params).trainConfirmRefund(params).map(new HttpResultFunc<RefundConfirmModel>()), subscriber);
    }

    /**
     * 个人中心-获取优惠券
     *
     * @param mobile
     * @param isExpire
     * @param subscriber
     */
    public void getCoupon(String mobile, String isExpire, ApiSubscriber<JsonResult<List<CouponResponsePara>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("isExpire", isExpire);
        params.put("mobile", mobile);
        toSubscribe(sendApiServiceRequest(params).getCoupon(params).map(new HttpResultFunc<List<CouponResponsePara>>()), subscriber);
    }

    /**
     * 个人中心-优惠券兑换
     *
     * @param code
     * @param subscriber
     */
    public void isCoupon(String code, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        toSubscribe(sendApiServiceRequest(params).isCoupon(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 个人中心-意见反馈
     *
     * @param feedbackType
     * @param content
     * @param subscriber
     */
    public void feedback(String feedbackType, String content, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("feedbackType", feedbackType);
        params.put("content", content);
        toSubscribe(sendApiServiceRequest(params).feedback(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 获取分享内容
     *
     * @param type
     * @param subscriber
     */
    public void getShare(String type, ApiSubscriber<JsonResult<ShareInfo>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        toSubscribe(sendApiServiceRequest(params).getShare(params).map(new HttpResultFunc<ShareInfo>()), subscriber);
    }

    /**
     * 火车票账号绑定
     *
     * @param account
     * @param psw
     * @param subscriber
     */
    public void bindTrain(String account, String psw, ApiSubscriber<JsonResult<TrainLoginResponseModel>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("trainAccount", account);
        params.put("pass", psw);
        toSubscribe(sendApiServiceRequest(params).bindTrain(params).map(new HttpResultFunc<TrainLoginResponseModel>()), subscriber);
    }

    /**
     * 火车票解除绑定
     *
     * @param subscriber
     */
    public void unbindTrain(String trainId, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("trainAccountId", trainId);
        toSubscribe(sendApiServiceRequest(params).unbindTrain(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 火车票验证账号是否通过
     *
     * @param subscriber
     */
    public void trainAccount(ApiSubscriber<JsonResult<TrainLoginResponseModel>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).trainAccountList().map(new HttpResultFunc<TrainLoginResponseModel>()), subscriber);
    }

    /**
     * 个人中心-获取用户信息
     *
     * @param subscriber
     */
    public void getUserInfo(ApiSubscriber<JsonResult<LoginResponsePara>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).getUserInfo().map(new HttpResultFunc<LoginResponsePara>()), subscriber);
    }

    /**
     * 个人中心-修改用户昵称
     *
     * @param nickName
     * @param subscriber
     */
    public void updateName(String nickName, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        toSubscribe(sendApiServiceRequest(params).updateName(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 个人中心-修改用户身份证号码
     *
     * @param certType
     * @param certNo
     * @param subscriber
     */
    public void updateIDCard(String certType, String certNo, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("certType", certType);
        params.put("certNo", certNo);
        toSubscribe(sendApiServiceRequest(params).updateIDCard(params).map(new HttpResultFunc<String>()), subscriber);
    }


    /**
     * 个人中心-修改头像
     *
     * @param iconPath
     * @param subscriber
     */
    public void upLoadIcon(String iconPath, ApiSubscriber<JsonResult<String>> subscriber) {

        Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] photo = baos.toByteArray();

        toSubscribe(sendApiServiceRequest(null).uploadicon(RequestBody.create(MediaType.parse("multipart/form-data"), photo)).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 个人中心-消息列表
     *
     * @param subscriber
     */
    public void getMessageList(ApiSubscriber<JsonResult<List<MessageListResponsePara>>> subscriber) {

        toSubscribe(sendApiServiceRequest(null).getMessgeList().map(new HttpResultFunc<List<MessageListResponsePara>>()), subscriber);
    }

    /**
     * 个人中心-提问列表
     *
     * @param subscriber
     */
    public void getQuestionList(ApiSubscriber<JsonResult<List<QuestionResponsePara>>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).getQuestionList().map(new HttpResultFunc<List<QuestionResponsePara>>()), subscriber);
    }

    /**
     * 个人中心-我要提问
     *
     * @param question
     * @param subscriber
     */
    public void toAsk(String question, String detail, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("question", question);
        params.put("detail", detail);
        toSubscribe(sendApiServiceRequest(params).toAsk(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 个人中心-注册
     *
     * @param captcha
     * @param mobilePhone
     * @param password
     * @param nonce
     * @param subscriber
     */
    public void registe(String captcha, String mobilePhone, String password, String nonce, ApiSubscriber<JsonResult<String>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("captcha", captcha);
        params.put("mobilePhone", mobilePhone);
        params.put("password", password);
        params.put("nonce", nonce);
        toSubscribe(sendApiServiceRequest(params).registe(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 个人中心-获取第三方绑定列表
     *
     * @param subscriber
     */
    public void getBindList(ApiSubscriber<JsonResult<List<BindListResponsePara>>> subscriber) {

        toSubscribe(sendApiServiceRequest(null).getbindlist().map(new HttpResultFunc<List<BindListResponsePara>>()), subscriber);
    }

    /**
     * 个人中心-绑定第三方账号
     *
     * @param openType
     * @param openId
     * @param nickName
     * @param photo
     * @param subscriber
     */
    public void bindThrid(String openType, String openId, String nickName, String photo, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("openType", openType);
        params.put("openId", openId);
        params.put("nickName", nickName);
        params.put("nonce", photo);
        toSubscribe(sendApiServiceRequest(params).bindThrid(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 解绑第三方账号
     *
     * @param openType
     * @param openId
     * @param subscriber
     */
    public void unbindThrid(String openType, String openId, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("openType", openType);
        params.put("openId", openId);
        toSubscribe(sendApiServiceRequest(params).unbindThrid(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 个人中心-修改手机号码
     *
     * @param password
     * @param mobilePhone
     * @param nonce
     * @param captcha
     * @param subscriber
     */
    public void updatePhone(String password, String mobilePhone, String nonce, String captcha, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("mobilePhone", mobilePhone);
        params.put("nonce", nonce);
        params.put("captcha", captcha);
        toSubscribe(sendApiServiceRequest(params).updatePhone(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 订单-订单详情
     *
     * @param orderId
     * @param bizType
     * @param subscriber
     */
    public void getOrderdetails(String orderId, String bizType, ApiSubscriber<JsonResult<OrderdetailsResponsePara>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("bizType", bizType);
        toSubscribe(sendApiServiceRequest(params).getOrderdetails(params).map(new HttpResultFunc<OrderdetailsResponsePara>()), subscriber);
    }

    /**
     * 订单-直通车查看位置
     *
     * @param scheduleId
     * @param subscriber
     */
    public void getBusMapInfo(String scheduleId, ApiSubscriber<JsonResult<BusMapResponsePara>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("scheduleId", scheduleId);

        toSubscribe(sendApiServiceRequest(params).getBusMaoInfo(params).map(new HttpResultFunc<BusMapResponsePara>()), subscriber);
    }

    /**
     * 订单 退款计算退款金额
     *
     * @param orderId
     * @param ticketIdList
     * @param subscriber
     */
    public void caculateRefundPrice(String orderId, String ticketIdList, String returnTicketWay, String ticketCount, ApiSubscriber<JsonResult<RefundPriceResponsePara>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("ticketIdList", ticketIdList);
        params.put("returnTicketWay", returnTicketWay);
        params.put("ticketCount", ticketCount);

        toSubscribe(sendApiServiceRequest(params).caculateRefund(params).map(new HttpResultFunc<RefundPriceResponsePara>()), subscriber);

    }

    /**
     * 订单 申请退票
     *
     * @param para
     * @param subscriber
     */
    public void applyRefund(ApplyRefundRequestPara para, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", para.getOrderId());
        params.put("ticketIdList", para.getTicketIdList());
        params.put("returnTicketWay", para.getReturnTicketWay());
        params.put("refundType", para.getRefundType());
        params.put("refundReason", para.getRefundReason());
        params.put("refundMoney", para.getRefundMoney());
        params.put("ticketCount", para.getTicketCount());

        toSubscribe(sendApiServiceRequest(params).applyRefund(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 订单 取消订单
     *
     * @param orderId
     * @param subscriber
     */
    public void cancelOrder(String orderId, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        toSubscribe(sendApiServiceRequest(params).cancelOrder(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 订单 删除订单
     *
     * @param orderId
     * @param subscriber
     */
    public void deleteOrder(String orderId, ApiSubscriber<JsonResult<String>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        toSubscribe(sendApiServiceRequest(params).deleteOrder(params).map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 包车 获取城市列表
     *
     * @param subscriber
     */
    public void getBaocheList(ApiSubscriber<JsonResult<List<BaocheMapCity>>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).getBaocheCityList().map(new HttpResultFunc<List<BaocheMapCity>>()), subscriber);
    }

    /**
     * 包车 提前多少时间下单
     *
     * @param subscriber
     */
    public void bcPreTime(ApiSubscriber<JsonResult<BaocheBeforetimePara>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).baochepretime().map(new HttpResultFunc<BaocheBeforetimePara>()), subscriber);
    }

    /**
     * 包车 获取车辆类型List
     *
     * @param subscriber
     */
    public void getBaocheType(String count, ApiSubscriber<JsonResult<List<BaocheBusType>>> subscriber) {
        Map<String, String> params = new HashMap<>();
        params.put("count", count);

        toSubscribe(sendApiServiceRequest(params).getBaocheType(params).map(new HttpResultFunc<List<BaocheBusType>>()), subscriber);
    }

    /**
     * 包车协议
     *
     * @param subscriber
     */
    public void getBaocheProtocol(ApiSubscriber<JsonResult<String>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).getBaocheProtocol().map(new HttpResultFunc<String>()), subscriber);
    }

    /**
     * 包车 提交订单后弹出数据
     *
     * @param subscriber
     */
    public void getBaocheNotice(ApiSubscriber<JsonResult<BaocheNoticeEntity>> subscriber) {
        toSubscribe(sendApiServiceRequest(null).getBaocheNotice().map(new HttpResultFunc<BaocheNoticeEntity>()), subscriber);
    }

    /**
     * 包车，计算价格
     *
     * @param subscriber
     */
    public void getBaochePrice(BaocheSubmitRequest para, ApiSubscriber<JsonResult<BaochePriceResponse>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("busInfo", para.getBusInfo());
        params.put("isGoBack", para.getIsGoBack());
        params.put("goBackTime", para.getGoBackTime());
        params.put("useTime", para.getUseTime());
        params.put("fromPlace", para.getFromPlace());
        params.put("toPlace", para.getToPlace());
        params.put("fromLongitude", para.getFromLongitude());
        params.put("fromLatitude", para.getFromLatitude());
        params.put("toLongitude", para.getToLongitude());
        params.put("toLatitude", para.getToLatitude());
        params.put("personCount", para.getPersonCount());
        params.put("couponId", para.getCouponId());
        toSubscribe(sendApiServiceRequest(params).getBaochePrice(params).map(new HttpResultFunc<BaochePriceResponse>()), subscriber);
    }

    /**
     * 包车 下单
     *
     * @param para
     * @param subscriber
     */
    public void BaocheSubmitOrder(BaocheSubmitRequest para, ApiSubscriber<JsonResult<BaocheSubmitResponse>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("busInfo", para.getBusInfo());
        params.put("contactName", para.getContactName());
        params.put("contactMobile", para.getContactMobile());
        params.put("isGoBack", para.getIsGoBack());
        params.put("goBackTime", para.getGoBackTime());
        params.put("useTime", para.getUseTime());
        params.put("fromPlace", para.getFromPlace());
        params.put("toPlace", para.getToPlace());
        params.put("personCount", para.getPersonCount());
        params.put("isInvoice", para.getIsInvoice());
        params.put("invoiceTitle", para.getInvoiceTitle());
        params.put("receiver", para.getReceiver());
        params.put("receiverAddr", para.getReceiverAddr());
        params.put("orderMoney", para.getOrderMoney());
        params.put("fromPlace", para.getFromPlace());
        params.put("couponId", para.getCouponId());
        params.put("fromLongitude", para.getFromLongitude());
        params.put("fromLatitude", para.getFromLatitude());
        params.put("toLongitude", para.getToLongitude());
        params.put("toLatitude", para.getToLatitude());
        params.put("fromCityName", para.getFromCityName());
        params.put("toCityName", para.getToCityName());

        toSubscribe(sendApiServiceRequest(params).BaocheSubmitOrder(params).map(new HttpResultFunc<BaocheSubmitResponse>()), subscriber);
    }

    /**
     * 获取下单中可用的优惠券
     *
     * @param amount
     * @param bizType
     * @param subscriber
     */
    public void getUsableCoupon(String amount, String bizType, ApiSubscriber<JsonResult<List<CouponResponsePara>>> subscriber) {

        Map<String, String> params = new HashMap<>();
        params.put("amount", amount);
        params.put("bizType", bizType);
        toSubscribe(sendApiServiceRequest(params).getUsableCoupon(params).map(new HttpResultFunc<List<CouponResponsePara>>()), subscriber);

    }

}
