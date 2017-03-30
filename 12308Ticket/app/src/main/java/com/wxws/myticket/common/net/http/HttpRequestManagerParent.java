package com.wxws.myticket.common.net.http;


import com.wxws.myticket.common.net.http.retrofit.HttpRequest;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * desc: 将通用方法和API方法父子类分离。
 * Date: 2016-11-02 16:24
 *
 * @author jiangyan
 */
public class HttpRequestManagerParent {

    public static final String BASE_URL_OLD = /*"http://webapi.12308.com*/"http://192.168.1.120:8100";
    public static final String BASE_URL_NEW = "http://newapi.12308.com";

    protected  <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    //发送请求
    protected ApiService sendApiServiceRequest(Map<String,String> params) {
        return HttpRequest.getInstance().configBaseUrl(BASE_URL_OLD,params);
    }

    //新域名
    protected ApiService sendNewApiServiceRequest(Map<String,String> params){
        return HttpRequest.getInstance().configBaseUrl(BASE_URL_NEW,params);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1<JsonResult<T>, JsonResult<T>> {

        @Override
        public JsonResult<T> call(JsonResult<T> httpResult) {
            return httpResult;
        }
    }
}