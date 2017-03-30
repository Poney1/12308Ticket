package com.wxws.myticket.common.net.http.retrofit;


import com.wxws.myticket.common.net.fastJsonFactory.FastJsonConverterFactory;
import com.wxws.myticket.common.net.http.ApiService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * desc: retrofit 设置
 * Date: 2016/10/8 14:58
 *
 * @auther: lixiangxiang
 */
public class HttpRequest  {

    private static final int DEFAULT_TIMEOUT = 30;

    private Retrofit retrofit;

    private OkHttpClient  client;
    private OkHttpClient.Builder  clientBuilder;

    private static  HttpRequest INSTANCE;

    private HttpRequest(){
        clientBuilder = new OkHttpClient.Builder();
        client = clientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor("12308Ticket", true))
                .build();
    }
    //创建单例
    public static HttpRequest getInstance(){
        if (INSTANCE == null){
            synchronized (HttpRequest.class){
                if (INSTANCE == null){
                    INSTANCE = new HttpRequest();
                }
            }
        }
        return INSTANCE;
    }

    //设置一个url 以及接口类
    public ApiService configBaseUrl(String baseUrl,Map<String,String> params){
        configRetrofit(baseUrl,params);
        ApiService mApiService =  retrofit.create(ApiService.class);
        return  mApiService;
    }

    //设置 retrofit
    private Retrofit configRetrofit(String baseUrl,Map<String,String>  params){

        client = clientBuilder.addInterceptor(new HeaderInterceptor())
                .addInterceptor(new ParamInterceptor(params))
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        return retrofit;
    }

}
