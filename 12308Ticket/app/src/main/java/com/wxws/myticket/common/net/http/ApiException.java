package com.wxws.myticket.common.net.http;

/**
 * desc: 异常
 * Date: 2016/10/8 14:51
 *
 * @auther: lixiangxiang
 */
public class ApiException extends IllegalArgumentException {

    private int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
