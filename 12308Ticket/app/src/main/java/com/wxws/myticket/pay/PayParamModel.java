package com.wxws.myticket.pay;

import java.io.Serializable;

/**
 * desc: 支付参数model
 * Date: 2016/10/20 15:24
 *
 * @auther: lixiangxiang
 */
public class PayParamModel {

    /**
     * orderId : T201607230001
     * payChannel : ALIPAY_APP
     */
    private String orderId;
    private String payChannel;
    private PayDataModel data;

    public PayDataModel getData() {
        return data;
    }

    public void setData(PayDataModel data) {
        this.data = data;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public static class PayDataModel implements Serializable {
        /**
         * alikey : _input_charset="UTF-8"&body="购票总金额：0.01"?ify_url="http://99cd496e.ngrok.io/pay-center-demo/payment/notify?channelType=ALIPAY_APP"&out_trade_no="TP17091099326244"&partner="2088411293539364"&pay_method="directPay"&payment_type="1"&seller_id="2088411293539364"&service="mobile.securitypay.pay"&subject="12308全国汽车票"&total_fee="0.01"&sign_type="RSA"&sign="R9a31y945MHAyFLtWZssI3KQyQUbtVIMBWpX9fMWyGyWHs%2BnUFSAW7Wq9MHtjJnpztRxLTks%2Brwd88K%2Byogwr4f0KSytw6jrhWx6lpqrFvuZwc2SzrxXTiRwNoRWzPOXNFLQ6YlL00i2JYcpxq%2BN
         */

        private String alikey;
        /**
         * appid : 1234
         * sign : 9C46E071F4B71B556FA253C3764272F3
         * bargainor_id : 1233438601
         * token_id : 0V6ef2b465c62be43ac2d66ee76d1085
         * err_info : null
         */

        private String appid;
        private String sign;
        private String bargainor_id;
        private String token_id;
        private String err_info;




        /**
         * tn : 201608171417088630428
         */

        private String tn;
        /**
         * partnerid : wxc9847938780ba8d1
         * prepayid : wx20160822173126bda56ff6ce0830963158
         * timestamp : 1471858100
         * noncestr : aqhqTT4Y7j60ZfPO
         * packageValue : Sign=WXPay
         */

        private String partnerid;
        private String prepayid;
        private String timestamp;
        private String noncestr;
        private String packageValue;

        public String getErr_info() {
            return err_info;
        }

        public void setErr_info(String err_info) {
            this.err_info = err_info;
        }

        public String getAlikey() {
            return alikey;
        }

        public void setAlikey(String alikey) {
            this.alikey = alikey;
        }


        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getBargainor_id() {
            return bargainor_id;
        }

        public void setBargainor_id(String bargainor_id) {
            this.bargainor_id = bargainor_id;
        }

        public String getToken_id() {
            return token_id;
        }

        public void setToken_id(String token_id) {
            this.token_id = token_id;
        }

        public String getTn() {
            return tn;
        }

        public void setTn(String tn) {
            this.tn = tn;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }
    }
}
