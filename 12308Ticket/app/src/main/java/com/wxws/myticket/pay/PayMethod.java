package com.wxws.myticket.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tenpay.paybyqq.Tenpay;
import com.umeng.analytics.MobclickAgent;
import com.unionpay.UPPayAssistEx;
import com.wxws.myticket.R;
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.interfaces.IDialog;
import com.wxws.myticket.common.interfaces.PayCallBackListener;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.dialog.FragmentDialog;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * desc: 支付
 * Date: 2016/10/20 15:17
 *
 * @auther: lixiangxiang
 */
public class PayMethod {

    /**
     * 支付key
     */
    // 微信
    public static final String WECHAT_APPID = "wxc9847938780ba8d1";
    /**
     * 银联支付环境
     */
    public static final String serverMode = "00";//00正式环境，01测试环境

    /**
     * 微信支付失败（可能没有安装微信端）
     */
    public static final int WxinPayError = 4;
    /**
     * 微信支付
     */
    public static final String WXPAY = "WXPay";
    public static final String NEWWXPAY = "WECHAT_APP";
    /**
     * 支付宝支付
     */
    public static final String ALIPAY = "AliPay";
    public static final String NEWALIPAY = "ALIPAY_APP";
    /**
     * 银联支付
     */
    public static final String UPPAY = "UPPay";
    public static final String NEWUPPAY = "UNIONPAY_APP";
    /**
     * QQ钱包支付
     */
    public static final String QPAY = "QPay";
    public static final String NEWQPAY = "TENPAY_APP";

    private PayInnerHandler innerHandler;
    private PayCallBackListener mPayCallListener;

    private static PayMethod payMethod;

    private IDialog iDialog;
    private Context mContext;
    private PayMethod() {

    }

    public static PayMethod getDefault() {
        if (payMethod == null) {
            synchronized (PayMethod.class) {
                if (payMethod == null) {
                    payMethod = new PayMethod();
                }
            }
        }
        return payMethod;
    }

    /**
     * 调用入口
     *
     * @param context
     * @param payChannel
     * @param orderId
     * @param amount
     */
    public void getPayParam(Context context, String payChannel, String orderId, String amount,String bizType,PayCallBackListener mPayCallListener) {
        this.mContext  = context ;
        this.mPayCallListener = mPayCallListener;
        innerHandler = new PayInnerHandler((Activity) context,payMethod);
        if (iDialog == null) {
            iDialog = new FragmentDialog(context);
        }
        HttpRequestManager.getInstance().getPayParam(payChannel,orderId, amount, bizType,new ApiSubscriber<JsonResult<PayParamModel>>(context) {

            @Override
            public void onNext(JsonResult<PayParamModel> payParamModelJsonResult) {
                if ("0000".equals(payParamModelJsonResult.getResultCode())) {
                    PayParamModel.PayDataModel trainPayDataModel = payParamModelJsonResult.getObject().getData();
                    if (trainPayDataModel == null) {
                        return;
                    }
                    switch (payParamModelJsonResult.getObject().getPayChannel()) {
                        case NEWWXPAY:
                            IWXAPI msgApi = WXAPIFactory.createWXAPI(mContext, WECHAT_APPID);
                            msgApi.registerApp(WECHAT_APPID);
                            WXProductArgs wxProductArgs = new WXProductArgs();
                            wxProductArgs.setAppId(trainPayDataModel.getAppid());
                            wxProductArgs.setPackageValue(trainPayDataModel.getPackageValue());
                            wxProductArgs.setSign(trainPayDataModel.getSign());
                            wxProductArgs.setNoncestr(trainPayDataModel.getNoncestr());
                            wxProductArgs.setPartnerId(trainPayDataModel.getPartnerid());
                            wxProductArgs.setTimestamp(trainPayDataModel.getTimestamp());
                            wxProductArgs.setPrepayId(trainPayDataModel.getPrepayid());
                            sendPayReq(msgApi, wxProductArgs);
                            MobclickAgent.onEvent(mContext, "callWXPay");
                            break;
                        case NEWALIPAY:
                            final String orderInfo = trainPayDataModel.getAlikey();
                            if (StringUtils.isNullOrEmpty(orderInfo)) {
                                return;
                            }
                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    String result = "";
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask((Activity) mContext);
                                    if (alipay != null && !StringUtils.isNullOrEmpty(orderInfo)) {
                                        try {
                                            // 调用支付接口，获取支付结果
                                            result = alipay.pay(orderInfo, true);
                                        } catch (Exception ex) {
                                        }
                                    }
                                    Message msg = new Message();
                                    msg.what = AlipayUtil.RQF_PAY;
                                    msg.obj = result;
                                    innerHandler.sendMessage(msg);
                                }
                            };

                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();

                            // 调用支付宝计数事件
                            MobclickAgent.onEvent(mContext, "callAlixPay");
                            break;
                        case NEWUPPAY:
                            String tn = trainPayDataModel.getTn();
                            if (!StringUtils.isNullOrEmpty(tn)) {
                                try {
                                    UPPayAssistEx.startPayByJAR(mContext, com.unionpay.uppay.PayActivity.class, null, null, tn, serverMode);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                // 调用银联计数事件
                                MobclickAgent.onEvent(mContext, "callUPPay");
                            } else {
                                ToastManager.getInstance().showToast(AppApplication.getInstance().getApplicationContext(), "获取银联信息失败！");
                            }
                            break;
                        case NEWQPAY:
                            String mTokenId = trainPayDataModel.getToken_id();
                            // 先得到财付通订单号
                            if (mTokenId == null || mTokenId.length() < 32) {
                                ToastManager.getInstance().showToast(mContext, "tokenid不正确");
                                return;
                            }
                            // 检测手机QQ是否支持支付接入
                            if (Tenpay.checkMobileQQ(AppApplication.getInstance().getApplicationContext())) {
                                // 特例处理: 手机QQ为4.2版本，仅支持3.2以上Android版本，否则会crash
                                // 4.2以上版本会修复此crash问题，支持2.1以上Android版本。
                                String qqVersion = Tenpay.getMobileQQVersion(AppApplication.getInstance().getApplicationContext());
                                if (qqVersion != null && qqVersion.startsWith("4.2") && Build.VERSION.SDK_INT < 12) {
                                    ToastManager.getInstance().showToast(AppApplication.getInstance().getApplicationContext(), "不支持qq财付通支付");
                                    return;
                                }
                                // 开启qq界面
                                Tenpay.startQQPay(AppApplication.getInstance().getApplicationContext(), mTokenId);
                            } else {
                                ToastManager.getInstance().showToast(AppApplication.getInstance().getApplicationContext(), "不支持qq财付通支付");
                                return;
                            }
                            break;
                        default:
                            ToastManager.getInstance().showToast(AppApplication.getInstance().getApplicationContext(), "支付错误，请重试");
                            break;
                    }
                } else {
                    if (StringUtils.isNullOrEmpty(payParamModelJsonResult.getResultMsg())) {
                        ToastManager.getInstance().showToast(AppApplication.getInstance().getApplicationContext(), "获取支付信息失败！");
                        return;
                    }
                    ToastManager.getInstance().showToast(AppApplication.getInstance().getApplicationContext(), payParamModelJsonResult.getResultMsg());
                }
            }
        });
    }


    /**
     * 调用微信支付
     *
     * @param wxproductArgs
     */
    private void sendPayReq(final IWXAPI msgApi, WXProductArgs wxproductArgs) {
        final PayReq req = new PayReq();
        req.appId = wxproductArgs.getAppId();
        req.partnerId = wxproductArgs.getPartnerId();
        req.prepayId = wxproductArgs.getPrepayId();
        req.nonceStr = wxproductArgs.getNoncestr();
        req.timeStamp = wxproductArgs.getTimestamp();
        req.packageValue = wxproductArgs.getPackageValue();
        req.sign = wxproductArgs.getSign();

        LogUtils.d("PayActivity", wxproductArgs.toString());
        if (req.checkArgs()) {
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            new Thread() {

                @Override
                public void run() {
                    super.run();
                    msgApi.registerApp(WECHAT_APPID);
                    boolean bR = msgApi.sendReq(req);
                    LogUtils.d("PayActivity", bR + "");
                    if (!bR) {
                        innerHandler.sendEmptyMessage(WxinPayError);
                    }
                }
            }.start();
        }
    }


    /**
     * 支付失败
     */
    private void payError() {
        // 支付错误（签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。）
        iDialog.showDialog(mContext.getString(R.string.pay_fail), mContext.getString(R.string.pay_fail_desc), R.layout.customdialog_ok_cancel,
                new DialogCallBackListener() {

                    @Override
                    public void doPositiveClick() {
                        // 重新支付，什么也不做
                        iDialog.dissmissDialog();
                    }

                    @Override
                    public void doPositiveClick(String string) {
                    }

                    @Override
                    public void doNegativeClick() {
                        // 刷新订单列表
//                        EventBus.getDefault().post(new BaseEvent(BaseEvent.REFRSH_ORDER));
                        // 跳转订单详情页，获取支付状态是否已经完成，如果没有支付，什么也不做，如果已经支付，进入等待出票状态
                        mPayCallListener.payFail();
//                        getAppointCar();
                    }
                });
    }

    private  class PayInnerHandler extends Handler {
        WeakReference<Activity> weakReference;
        PayMethod payMethod ;
        public PayInnerHandler(Activity activity,PayMethod payMethod) {
            weakReference = new WeakReference<>(activity);
            this.payMethod = payMethod ;
        }

        @Override
        public void handleMessage(Message msg) {

            Activity activity = weakReference.get();

            if (activity == null)
                return;

            switch (msg.what) {
                    case AlipayUtil.RQF_PAY:
                         Result result = new Result((String) msg.obj);
                        if (result.parseResult().equals("9000")) {
                            // 支付宝支付成功
                            HashMap<String, String> payway1 = new HashMap<String, String>();
                            payway1.put("支付成功信息",
                                    "支付宝;");
                            MobclickAgent.onEvent(activity, "alixpaymsginfo", payway1);
                            // 刷新订单列表
//                        EventBus.getDefault().post(new BaseEvent(BaseEvent.REFRSH_ORDER));
//                            getPayResult();
                            mPayCallListener.paySuccess();
                            activity.finish();

                        } else if (result.parseResult().equals("6001")) {
                            // 用户取消
                            payMethod.iDialog.showDialog(R.string.pay_cancel);
                        } else {
                            payMethod.payError();
                        }
                        break;
                    case WxinPayError:
                       ToastManager.getInstance().showToast(activity, "亲,请检测您的手机是否支持微信支付哦");
                        break;
                    case 5://普通支付
//                        getPayResult();
                        mPayCallListener.paySuccess();
                        break;
                    case 6://改签支付
//                        getConfirmChangeOrder();
                        mPayCallListener.paySuccess();
                        break;
                    default:
                        break;
                }
            }
    }
}
