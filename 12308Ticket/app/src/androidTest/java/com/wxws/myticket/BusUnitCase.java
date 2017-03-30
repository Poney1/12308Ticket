package com.wxws.myticket;

import android.test.InstrumentationTestCase;


/**
 * desc: 汽车票模块单元测试
 * Date: 2016/10/19 17:18
 *
 * @auther: lixiangxiang
 */
public class BusUnitCase extends InstrumentationTestCase {

        public void testPay(){
            String order1 = " _input_charset=\"UTF-8\"&body=\"购买  深圳--广州东 火车票!\"&it_b_pay=\"28m\"&notify_url=\"http://114.55.18.8/payment/v1/pay/notify.html?channelType=ALIPAY_APP\"&out_trade_no=\"201610251021000010971214\"&partner=\"2088411293539364\"&pay_method=\"directPay\"&payment_type=\"1\"&seller_id=\"2088411293539364\"&service=\"mobile.securitypay.pay\"&subject=\"12308全国汽车票\"&total_fee=\"79.5\"&sign_type=\"RSA\"&sign=\"uaT7%2F9hGGnFgWqhzkaw%2BmuMdZCm5lpuV3lk4sAj2g6CWN7Su3R2LiQvrZRg5bszEJ9hMjrvQTFRTpr0bqrq13WGBCcTpXsdM8ztcJrGo4oK8OgymOOdLydQQqOWCFiWw1H4tG2Mz7%2BBuNkQSR3WkpSUvX%2FIwC8zA3DNeGzemD2s%3D\"";
        }
}
