package com.wxws.myticket.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * desc: Urlencode工具类
 * Date: 2016-11-09 14:41
 *
 * @author jiangyan
 */
public class UrlEncodeUtils {

    /**
     * 两次UrlEncode
     *
     * @param original
     * @return
     */
    public static String doubleUrlEncode(String original) {

        String result = null;

        try {
            result = URLEncoder.encode(original, "UTF-8");
            result = URLEncoder.encode(result, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogUtils.e("UrlEncode出错" + e.toString());
            return "";
        }

        return result;
    }
}