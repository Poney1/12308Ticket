package com.wxws.myticket.common.net.fastJsonFactory;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.activity.LoginActivity;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.zip.GZIPInputStream;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * desc: fastJson 解析响应的 json
 * Date: 2016/10/8 16:55
 *
 * @auther: lixiangxiang
 */
final class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;
    private int BUFFERSIZE = 1024;

    FastJsonResponseBodyConverter(Type type, ParserConfig config, int featureValues,
                                  Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String jsonAll = value.string();
            JSONObject jsonObject = new JSONObject(jsonAll);
            boolean isCompress = jsonObject.optBoolean("isCompress");
            String object = jsonObject.optString("object");
            String isLogin = jsonObject.optString("resultCode");
            String authToken = jsonObject.optString("authToken");

            //处理续租的authToken
            if (!StringUtils.isNullOrEmpty(authToken)) {
                PreferencesUtils.putDataString(AppApplication.getInstance().getApplicationContext(), ConfigConstants.AUTHTOKEN, authToken);
            }
            //token 失效 跳转 到登录界面
            if ("-100000004".equals(isLogin) || "-100000002".equals(isLogin) || "-100000003".equals(isLogin)) {
                AppApplication.getInstance().getApplicationContext().startActivity(new Intent(AppApplication.getInstance().getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return  JSON.parseObject(jsonAll, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);
            } else {
                if (isCompress) {
                    String gzip = unGZip(object);
                    String resultMsg = jsonObject.optString("resultMsg");
                    String resultCode = jsonObject.optString("resultCode");
                    String authTokenCompress = jsonObject.optString("authToken");
                    StringBuilder json = new StringBuilder();
                    json.append("{");
                    if (!StringUtils.isNullOrEmpty(resultCode)) {
                        String strCode = String.format("\"%s\":\"%s\",", "resultCode", resultCode);
                        json.append(strCode);
                    }
                    if (!StringUtils.isNullOrEmpty(resultMsg)) {
                        String strMsg = String.format("\"%s\":\"%s\",", "resultMsg", resultMsg);
                        json.append(strMsg);
                    }

                    if (!StringUtils.isNullOrEmpty(authTokenCompress) && !authTokenCompress.equals("null")) {
                        String strCode = String.format("\"%s\":\"%s\",", "authToken", authTokenCompress);
                        json.append(strCode);
                    }
                    if (!StringUtils.isNullOrEmpty(gzip)) {
                        String strCode = String.format("\"%s\":%s", "object", gzip);
                        json.append(strCode);
                    }
                    json.append("}");
                    jsonAll = json.toString();
                }
                Log.e("mudo", "解压后数据：" + jsonAll);
                return JSON.parseObject(jsonAll, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            value.close();
        }
        return null;
    }

    private String unGZip(String unGzip1) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(unGzip1
                    .getBytes("ISO-8859-1"));
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[BUFFERSIZE];
            int n;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            out.close();
            in.close();
            return out.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
