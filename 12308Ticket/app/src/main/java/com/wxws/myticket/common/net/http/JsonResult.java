package com.wxws.myticket.common.net.http;

/**
 * desc: 公共解析对象
 * Date: 2016/10/8 14:55
 *
 * @auther: lixiangxiang
 */
public class JsonResult<T> {

    private String resultMsg;
    private String resultCode;
    private T object;
    private String authToken;
    private boolean isCompress;

    public boolean isCompress() {
        return isCompress;
    }

    public void setCompress(boolean compress) {
        isCompress = compress;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
