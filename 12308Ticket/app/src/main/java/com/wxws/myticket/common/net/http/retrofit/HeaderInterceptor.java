package com.wxws.myticket.common.net.http.retrofit;


import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.utils.Md5Utils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.ManifestMetaDataUtil;
import com.wxws.myticket.common.utils.store.PreferencesUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc: 添加公共头
 * Date: 2016/10/8 14:16
 *
 * @auther: lixiangxiang
 */
public class HeaderInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        String secretKey = ConfigConstants.SECRITAPPKEY;
        //获取储存的用户信息
        //UserInfo userInfo = (UserInfo) PreferencesUtils.readDataObject(AppApplication.getInstance().getApplicationContext(), ConfigConstants.USERINFO);
        String time = System.currentTimeMillis() + "";

        Request.Builder builderHeader = chain.request().newBuilder();
        builderHeader.header("time", time)
                .header("source", "Android")
                .header("auth", Md5Utils.getMd5Str(secretKey + time))
                .header("channel", ManifestMetaDataUtil.getAppMetaData(AppApplication.getInstance().getApplicationContext(), "UMENG_CHANNEL") == null
                        ? "" : ManifestMetaDataUtil.getAppMetaData(AppApplication.getInstance().getApplicationContext(), "UMENG_CHANNEL"))
                .header("versionCode", ManifestMetaDataUtil.getVersionCode(AppApplication.getInstance().getApplicationContext()) + "");

        String auth = PreferencesUtils.getDataString(AppApplication.getInstance().getApplicationContext(), ConfigConstants.AUTHTOKEN);

        if (!StringUtils.isNullOrEmpty(auth)) {
            builderHeader.header("authToken", auth);
        }

        Request request = builderHeader.build();

        return chain.proceed(request);
    }
}