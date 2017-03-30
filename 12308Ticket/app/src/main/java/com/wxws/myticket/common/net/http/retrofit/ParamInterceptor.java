package com.wxws.myticket.common.net.http.retrofit;


import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.common.utils.Md5Utils;
import com.wxws.myticket.common.utils.function.ManifestMetaDataUtil;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc: 添加公共参数
 * Date: 2016/10/8 18:17
 *
 * @auther: lixiangxiang
 */
public class ParamInterceptor implements Interceptor {

    private Map<String, String> params;

    public ParamInterceptor(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();
        String time = System.currentTimeMillis() + "";
        String secretKey = ConfigConstants.SECRITAPPKEY;
        String publicKey = ConfigConstants.APPKEY;

        if (params == null) {
            params = new HashMap<>();
        }

        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());

        authorizedUrlBuilder.setQueryParameter("time", time)
                .setQueryParameter("platform", "Android")
                .setQueryParameter("auth", Md5Utils.getMd5Str(secretKey + time))
                .setQueryParameter("channel", ManifestMetaDataUtil.getAppMetaData(AppApplication.getInstance().getApplicationContext(), "UMENG_CHANNEL"))
                .setQueryParameter("versionCode", ManifestMetaDataUtil.getVersionCode(AppApplication.getInstance().getApplicationContext()) + "")
                .setQueryParameter("deviceid", SystemUtils.getInstance().getDeviceId(AppApplication.getInstance().getApplicationContext()))
                .setQueryParameter("macAddress", SystemUtils.getInstance().getLocalMacAddress(AppApplication.getInstance().getApplicationContext()))
                .setQueryParameter("clientVersion", ManifestMetaDataUtil.getVersionName(AppApplication.getInstance().getApplicationContext()));
        String authToken = PreferencesUtils.getDataString(AppApplication.getInstance().getApplicationContext(), ConfigConstants.AUTHTOKEN);
        if (null != authToken) {
            authorizedUrlBuilder.setQueryParameter("authToken", authToken);
            params.put("authToken", authToken);
        }
        params.put("time", time);
        params.put("platform", "Android");
        params.put("auth", Md5Utils.getMd5Str(secretKey + time));
        params.put("channel", ManifestMetaDataUtil.getAppMetaData(AppApplication.getInstance().getApplicationContext(), "UMENG_CHANNEL"));
        params.put("versionCode", ManifestMetaDataUtil.getVersionCode(AppApplication.getInstance().getApplicationContext()) + "");
        params.put("deviceid", SystemUtils.getInstance().getDeviceId(AppApplication.getInstance().getApplicationContext()));
        params.put("macAddress", SystemUtils.getInstance().getLocalMacAddress(AppApplication.getInstance().getApplicationContext()));
        params.put("clientVersion", ManifestMetaDataUtil.getVersionName(AppApplication.getInstance().getApplicationContext()));

        authorizedUrlBuilder.setQueryParameter("sign", getSignature(params, publicKey));

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        LogUtils.d("域名" + oldRequest.url() + "time===" + time + "params===" + params.toString());

        if (params != null && params.size() > 0) {
            LogUtils.d("NetHelper-----Body------");
            Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                LogUtils.i(entry.getKey() + ":" + entry.getValue());
            }
            LogUtils.d("NetHelper", "-----Body------");
        }
        return chain.proceed(newRequest);
    }


    private String getSignature(Map<String, String> reqMap, String publicPassword) {
        String init = "";
        // 通过ArrayList构造函数把map.entrySet()转换成list
        List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(reqMap.entrySet());
        // 通过比较器实现比较排序
        Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
                return mapping1.getKey().compareTo(mapping2.getKey());
            }
        });

        for (Map.Entry<String, String> mapping : mappingList) {
            init += mapping.getKey() + "=" + mapping.getValue() + "&";
        }
        init += publicPassword;
        String retSignature = Md5Utils.getMd5Str(init).toUpperCase();
        return retSignature;
    }
}
