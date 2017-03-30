package com.wxws.myticket.common.net.http;

import com.wxws.myticket.bus.dao.BusEndCity;
import com.wxws.myticket.bus.dao.BusStartCity;
import com.wxws.myticket.bus.entity.ActiveEntity;
import com.wxws.myticket.bus.entity.AircraftCityEntity;
import com.wxws.myticket.bus.entity.BaocheBeforetimePara;
import com.wxws.myticket.bus.entity.BaocheBusType;
import com.wxws.myticket.bus.entity.BaocheMapCity;
import com.wxws.myticket.bus.entity.BaocheNoticeEntity;
import com.wxws.myticket.bus.entity.BaochePriceResponse;
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
import com.wxws.myticket.my.Entity.BindListResponsePara;
import com.wxws.myticket.my.Entity.CouponResponsePara;
import com.wxws.myticket.my.Entity.LoginResponsePara;
import com.wxws.myticket.my.Entity.MessageListResponsePara;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.Entity.QuestionResponsePara;
import com.wxws.myticket.my.Entity.ShareInfo;
import com.wxws.myticket.my.Entity.TrainLoginResponseModel;
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
import com.wxws.myticket.train.entity.LockTicketResultEntity;
import com.wxws.myticket.train.entity.ProvinceModel;
import com.wxws.myticket.train.entity.SchoolModel;
import com.wxws.myticket.train.entity.TrainInsuranceModel;
import com.wxws.myticket.train.entity.TrainListModel;
import com.wxws.myticket.train.entity.TrainStartCityModel;
import com.wxws.myticket.train.entity.TrainStationInfoModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * desc: 所有的 请求
 * Date: 2016/10/8 14:51
 *
 * @auther: lixiangxiang
 */
public interface ApiService {

    /**
     * 获取城市版本号
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/other/v1/app/version.json")
    Observable<JsonResult<VersionInfo>> getVersion(@FieldMap Map<String, String> map);


    /**
     * @return
     */
    @POST("/home/v1/ad/activityAd.json")
    Observable<JsonResult<List<ActiveEntity>>> getActiveContent();

    /**
     * @return  http://192.168.1.120:8100/home/v1/ad/alertAd.json
     */
    @POST("/home/v1/ad/alertAd.json")
    Observable<JsonResult<ActiveEntity>> getAlertAd();

    /**
     * 支付参数获取接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/payment/v1/pay/topay.json")
    Observable<JsonResult<PayParamModel>> getPayParam(@FieldMap Map<String, String> map);

    /**
     * 汽车票出发城市
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/bus/v1/bus/fromCityList.json")
    Observable<JsonResult<List<BusStartCity>>> fromCityList(@FieldMap Map<String, String> map);

    /**
     * 汽车票到达城市
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/bus/v1/bus/toCityList.json")
    Observable<JsonResult<List<BusEndCity>>> toCityList(@FieldMap Map<String, String> map);

    /**
     * 汽车票两个汉字以上的搜索
     *
     * @param map  http://192.168.1.120:8100/bus/v1/bus/fromToStationList.json
     * @return
     */
    @FormUrlEncoded
    @POST("/bus/v1/bus/fromToStationList.json")
    Observable<JsonResult<BusStationListEntity>> fromToStationList(@FieldMap Map<String, String> map);


    /**
     * 汽车票搜索列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/bus/v1/line/list.json")
    Observable<JsonResult<TraditionTotalEntity>> busLineList(@FieldMap Map<String, String> map);

    /**
     * 汽车票搜索顶部推荐
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/bus/v1/top/recommend.json")
    Observable<JsonResult<TraditionBusListEntity>> recommend(@FieldMap Map<String, String> map);

    /**
     * 出发城市所有站点
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/bus/v1/bus/fromCityStation.json")
    Observable<JsonResult<List<StationDescEntity>>> fromCityStation(@FieldMap Map<String, String> map);

    /**
     * 港澳快线 出发城市
     *
     * @return
     */
    @POST("/bus/v1/gangao/fromList.json")
    Observable<JsonResult<List<GangAoStartCityEntity>>> fromGangAoList();

    /**
     * 港澳快线 到达城市
     *
     * @param map
     * @return http://192.168.1.120:8100/bus/v1/gangao/toList.json
     */
    @FormUrlEncoded
    @POST("/bus/v1/gangao/toList.json")
    Observable<JsonResult<List<GangAoStartCityEntity>>> toGangAoList(@FieldMap Map<String, String> map);

    /**
     * 直通车出发城市
     *
     * @return http://192.168.1.120:8100/bus/v1/direct/fromCityList.json
     */
    @FormUrlEncoded
    @POST("/bus/v1/direct/fromCityList.json")
    Observable<JsonResult<List<BusStartCity>>> cityLineFromCityList(@FieldMap Map<String, String> map);

    /**
     * 直通车到达城市
     *
     * @return  http://192.168.1.120:8100/bus/v1/direct/toCityList.json
     */
    @FormUrlEncoded
    @POST("/bus/v1/direct/toCityList.json")
    Observable<JsonResult<List<CityLineEndEntity>>> cityLineToCityList(@FieldMap Map<String, String> map);

    /**
     * 机场快线 城市
     *
     * @return http://192.168.1.120:8100/bus/v1/air/list.json
     */
    @FormUrlEncoded
    @POST("/bus/v1/air/list.json")
    Observable<JsonResult<List<AircraftCityEntity>>> airList(@FieldMap Map<String, String> map);


    /**
     * 火车票出发站点
     * http://192.168.1.120:8100/train/v1/line/stationlist.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/line/stationlist.json")
    Observable<JsonResult<List<TrainStartCityModel>>> trainStationList(@FieldMap Map<String, String> map);

    /**
     * 火车票经停站点
     * http://192.168.1.120:8100/train/v1/line/stopover.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/line/stopover.json")
    Observable<JsonResult<TrainStationInfoModel>> stopOverStation(@FieldMap Map<String, String> map);

    /**
     * 火车票余票查询
     * http://192.168.1.120:8100/train/v1/line/ticketlist.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/line/ticketlist.json")
    Observable<JsonResult<List<TrainListModel>>> trainTicketList(@FieldMap Map<String, String> map);

    /**
     * 车次余票查询
     * http://192.168.1.120:8100/train/v1/line/ticket.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/line/ticket.json")
    Observable<JsonResult<ChooseSeatModel>> trainLineTicket(@FieldMap Map<String, String> map);

    /**
     * 火车票dingdan提交
     * http://192.168.1.120:8100/train/v1/order/create.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/order/create.json")
    Observable<JsonResult<CommitOrderResponseModel>> trainOrderCreate(@FieldMap Map<String, String> map);

    /**
     * 火车票查询结果
     *  http://192.168.1.120:8100/train/v1/order/create/result.json?orderId=T20160723000001
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/order/create/result.json")
    Observable<JsonResult<LockTicketResultEntity>> trainCreateResult(@FieldMap Map<String, String> map);

    /**
     * 火车票改签申请
     *  http://192.168.1.120:8100/train/v1/order/requestchange.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/order/requestchange.json")
    Observable<JsonResult<CommitOrderResponseModel>> requestChange(@FieldMap Map<String, String> map);

    /**
     * 火车票改签结果
     *  http://192.168.1.120:8100/train/v1/order/requestchange/result.json
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/order/requestchange/result.json")
    Observable<JsonResult<ChangeTicketResultEntity>> requestChangeResult(@FieldMap Map<String, String> map);

    /**
     * 火车票保险
     * /train/v1/insurance/list.json
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/insurance/list.json")
    Observable<JsonResult<List<TrainInsuranceModel>>> trainInsuranceList(@FieldMap Map<String, String> map);

    /**
     * 汽车票保险
     *  http://192.168.1.120:8100/order/v1/insurance/list.json
     * @return
     */
    @FormUrlEncoded
    @POST("/order/v1/insurance/list.json")
    Observable<JsonResult<List<BusInsuranceEntity>>> busInsuranceList(@FieldMap Map<String, String> map);

    /**
     * 汽车票保险
     *   http://192.168.1.120:8100/order/v1/bus/submitOrder.json
     * @return
     */
    @FormUrlEncoded
    @POST("/order/v1/bus/submitOrder.json")
    Observable<JsonResult<OrderIdEntity>> submitOrder(@FieldMap Map<String, String> map);

    /**
     * 车次详情
     *
     * @return http://192.168.1.120:8100/bus/v1/line/detail.json
     */
    @FormUrlEncoded
    @POST("/bus/v1/line/detail.json")
    Observable<JsonResult<LineDetailEntity>> getLineDetail(@FieldMap Map<String, String> map);


    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("/account/v1/user/captcha.json")
    Observable<JsonResult<String>> getCode(@FieldMap Map<String, String> requestPara);

    /**
     * 用户名密码登录
     */
    @FormUrlEncoded
    @POST("/account/v1/user/normallogin.json")
    Observable<JsonResult<LoginResponsePara>> login(@FieldMap Map<String, String> requestPara);

    /**
     * 验证码登录
     */
    @FormUrlEncoded
    @POST("/account/v1/user/captchalogin.json")
    Observable<JsonResult<LoginResponsePara>> smslogin(@FieldMap Map<String, String> requestPara);

    /**
     * 第三方登录
     */
    @FormUrlEncoded
    @POST("/account/v1/user/openidlogin.json")
    Observable<JsonResult<LoginResponsePara>> thridlogin(@FieldMap Map<String, String> requestPara);

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST("/account/v1/user/pwdreset.json")
    Observable<JsonResult<String>> resetPsw(@FieldMap Map<String, String> requestPara);

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("/account/v1/user/pwdchange.json")
    Observable<JsonResult<String>> modifyPsw(@FieldMap Map<String, String> requestPara);

    /**
     * 获取联系人列表
     */
    @FormUrlEncoded
    @POST("/account/v1/contact/contactlist.json")
    Observable<JsonResult<List<PassengerInfoEntity>>> getContactList(@FieldMap Map<String, String> map);

    /**
     * 添加联系人
     */
    @FormUrlEncoded
    @POST("/account/v1/contact/addcontact.json")
    Observable<JsonResult<PassengerInfoEntity>> addContact(@FieldMap Map<String, String> map);

    /**
     * 添加联系人
     */
    @FormUrlEncoded
    @POST("/account/v1/contact/delcontact.json")
    Observable<JsonResult<Void>> delContact(@FieldMap Map<String, String> map);

    /**
     * 修改联系人
     */
    @FormUrlEncoded
    @POST("/account/v1/contact/updatecontact.json")
    Observable<JsonResult<Void>> updateContact(@FieldMap Map<String, String> map);

    /**
     * 获取省份接口 http://192.168.1.120:8100/train/v1/data/provincelist.json
     */
    @POST("/train/v1/data/provincelist.json")
    Observable<JsonResult<List<ProvinceModel>>> getProvinceList();

    /**
     * 获取省份接口 http://192.168.1.120:8100/train/v1/data/schoollist.json
     */
    @FormUrlEncoded
    @POST("/train/v1/data/schoollist.json")
    Observable<JsonResult<List<SchoolModel>>> getSchoolList(@FieldMap Map<String,String> map);


    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("/order/v1/order/list.json")
    Observable<JsonResult<CommonOrderListResponse>> getOrderList(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票订单列表
     */
    @FormUrlEncoded
    @POST("/train/v1/order/list.json")
    Observable<JsonResult<TrainorderlistPara>> getTrainOrderList(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票订单详情
     */
    @FormUrlEncoded
    @POST("/train/v1/order/detail.json")
    Observable<JsonResult<TrainOrderdetailsResponseModel>> getTrainOrderDetails(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票操作详情
     */
    @FormUrlEncoded
    @POST("/train/v1/order/operationlist.json")
    Observable<JsonResult<TicketOperateResponse>> getTrainOperateDetails(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票取消改签
     */
    @FormUrlEncoded
    @POST("/train/v1/order/cancelchange.json")
    Observable<JsonResult<EndorseFailureModel>> trainCancelEndorse(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票删除订单
     *
     * @param requestPara
     * @return
     */
    @FormUrlEncoded
    @POST("/train/v1/order/delete.json")
    Observable<JsonResult<TrainOrderdetailsResponseModel>> trainDeleteOrder(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票取消订单
     */
    @FormUrlEncoded
    @POST("/train/v1/order/cancel.json")
    Observable<JsonResult<CancelOrderModel>> trainCancelOrder(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票申请退票
     */
    @FormUrlEncoded
    @POST("/train/v1/order/requestrefund.json")
    Observable<JsonResult<RefundModel>> trainApplyRefund(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票确认退票
     */
    @FormUrlEncoded
    @POST("/train/v1/order/confirmrefund.json")
    Observable<JsonResult<RefundConfirmModel>> trainConfirmRefund(@FieldMap Map<String, String> requestPara);


    /**
     * 支付方式
     */
    @POST("/payment/v1/pay/channellist.json")
    Observable<JsonResult<List<PaymentEntity>>> clientPayType();

    /**
     * 获取优惠券
     */
    @FormUrlEncoded
    @POST("/order/v1/coupon/allList.json")
    Observable<JsonResult<List<CouponResponsePara>>> getCoupon(@FieldMap Map<String, String> requestPara);

    /**
     * 优惠券兑换
     */
    @FormUrlEncoded
    @POST("/order/v1/coupon/add.json")
    Observable<JsonResult<String>> isCoupon(@FieldMap Map<String, String> requestPara);

    /**
     * 意见反馈
     */
    @FormUrlEncoded
    @POST("/other/v1/feedback/add.json")
    Observable<JsonResult<String>> feedback(@FieldMap Map<String, String> requestPara);

    /**
     * 获取分享内容
     */
    @FormUrlEncoded
    @POST("/other/v1/share/detail.json")
    Observable<JsonResult<ShareInfo>> getShare(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票账号绑定
     */
    @FormUrlEncoded
    @POST("/train/v1/account/bind.json")
    Observable<JsonResult<TrainLoginResponseModel>> bindTrain(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票解除绑定
     */
    @FormUrlEncoded
    @POST("/train/v1/account/unbind.json")
    Observable<JsonResult<String>> unbindTrain(@FieldMap Map<String, String> requestPara);

    /**
     * 火车票验证是否通过
     */
    @POST("/train/v1/account/validate.json")
    Observable<JsonResult<TrainLoginResponseModel>> trainAccountList();

    /**
     * 获取用户信息
     */
    @POST("/account/v1/user/info.json")
    Observable<JsonResult<LoginResponsePara>> getUserInfo();

    /**
     * 修改用户昵称
     */
    @FormUrlEncoded
    @POST("/account/v1/user/nicknameupdate.json")
    Observable<JsonResult<String>> updateName(@FieldMap Map<String, String> requestPara);

    /**
     * 修改用户身份证号
     */
    @FormUrlEncoded
    @POST("/account/v1/user/certupdate.json")
    Observable<JsonResult<String>> updateIDCard(@FieldMap Map<String, String> requestPara);


    /**
     * 修改用户头像
     *
     * @return
     */
    @Multipart
    @POST("/account/v1/user/photoupdate.json")
    Observable<JsonResult<String>> uploadicon(@Part("photo\"; filename=\"photo.jpg\"") RequestBody requestPara);

    /**
     * 获取消息列表
     */
    @POST("/other/v1/notice/list.json")
    Observable<JsonResult<List<MessageListResponsePara>>> getMessgeList();

    /**
     * 我的提问
     */
    @POST("/other/v1/question/list.json")
    Observable<JsonResult<List<QuestionResponsePara>>> getQuestionList();

    /**
     * 我要提问
     */
    @FormUrlEncoded
    @POST("/other/v1/question/add.json")
    Observable<JsonResult<String>> toAsk(@FieldMap Map<String, String> requestPara);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/account/v1/user/register.json")
    Observable<JsonResult<String>> registe(@FieldMap Map<String, String> requestPara);

    /**
     * 第三方绑定列表
     */
    @POST("/account/v1/user/openapilist.json")
    Observable<JsonResult<List<BindListResponsePara>>> getbindlist();

    /**
     * 第三方账号绑定
     */
    @FormUrlEncoded
    @POST("/account/v1/user/openidbind.json")
    Observable<JsonResult<String>> bindThrid(@FieldMap Map<String, String> requestPara);

    /**
     * 第三方解绑
     */
    @FormUrlEncoded
    @POST("/account/v1/user/openidunbind.json")
    Observable<JsonResult<String>> unbindThrid(@FieldMap Map<String, String> requestPara);

    /**
     * 手机号码变更
     */
    @FormUrlEncoded
    @POST("/account/v1/user/mobilechange.json")
    Observable<JsonResult<String>> updatePhone(@FieldMap Map<String, String> requestPara);

    /**
     * 一般订单 订单详情
     */
    @FormUrlEncoded
    @POST("/order/v1/order/detail.json")
    Observable<JsonResult<OrderdetailsResponsePara>> getOrderdetails(@FieldMap Map<String, String> requestPara);

    /**
     * 获取地图位置信息
     */
    @FormUrlEncoded
    @POST("/order/v1/bus/position.json")
    Observable<JsonResult<BusMapResponsePara>> getBusMaoInfo(@FieldMap Map<String, String> requestPara);

    /**
     * 计算退票金额
     */
    @FormUrlEncoded
    @POST("/order/v1/ticket/queryRefundMoney.json")
    Observable<JsonResult<RefundPriceResponsePara>> caculateRefund(@FieldMap Map<String, String> requestPara);

    /**
     * 申请退票
     */
    @FormUrlEncoded
    @POST("/order/v1/ticket/return.json")
    Observable<JsonResult<String>> applyRefund(@FieldMap Map<String, String> requestPara);

    /**
     * 订单 取消订单
     */
    @FormUrlEncoded
    @POST("/order/v1/order/cancel.json")
    Observable<JsonResult<String>> cancelOrder(@FieldMap Map<String,String> requestPara);

    /**
     * 订单 删除订单
     */
    @FormUrlEncoded
    @POST("/order/v1/order/del.json")
    Observable<JsonResult<String>> deleteOrder(@FieldMap Map<String,String> requestPara);

    /**
     * 包车 城市列表
     */
    @POST("/bus/v1/bus/allCityList.json")
    Observable<JsonResult<List<BaocheMapCity>>> getBaocheCityList();

    /**
     * 包车，提前多少时间预约
     */
    @POST("/bus/v1/baoche/beforeTime.json")
    Observable<JsonResult<BaocheBeforetimePara>> baochepretime();

    /**
     * 包车 获取车辆类型列表
     */
    @FormUrlEncoded
    @POST("/bus/v1/baoche/typeList.json")
    Observable<JsonResult<List<BaocheBusType>>> getBaocheType(@FieldMap Map<String,String> requestPara);

    /**
     * 包车 包车协议
     */
    @POST("/bus/v1/baoche/serviceNotice.json")
    Observable<JsonResult<String>> getBaocheProtocol();

    /**
     * 包车 填写订单后弹出数据
     */
    @POST("/bus/v1/baoche/orderNotice.json")
    Observable<JsonResult<BaocheNoticeEntity>> getBaocheNotice();

    /**
     * 包车 计算价格
     */
    @FormUrlEncoded
    @POST("/order/v1/baoche/priceCount.json")
    Observable<JsonResult<BaochePriceResponse>> getBaochePrice(@FieldMap Map<String,String> requestPara);

    /**
     * 包车 下单
     */
    @FormUrlEncoded
    @POST("/order/v1/baoche/submitOrder.json")
    Observable<JsonResult<BaocheSubmitResponse>> BaocheSubmitOrder(@FieldMap Map<String,String> requestPara);

    /**
     * 下单中选择优惠券
     */
    @FormUrlEncoded
    @POST("/order/v1/coupon/usablelist.json")
    Observable<JsonResult<List<CouponResponsePara>>> getUsableCoupon(@FieldMap Map<String,String> requestPara);

}
