package com.wxws.myticket.common.interfaces;

/**
 * desc: 支付成功失败接口
 * Date: 2016/10/20 17:38
 *
 * @auther: lixiangxiang
 */
public interface PayCallBackListener {

    void paySuccess();

    void payFail();
}
