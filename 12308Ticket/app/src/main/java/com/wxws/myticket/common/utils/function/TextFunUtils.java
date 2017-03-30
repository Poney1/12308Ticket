package com.wxws.myticket.common.utils.function;

import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

/**
 * desc: 文本处理
 * Date: 2016/11/23 16:11
 *
 * @auther: lixiangxiang
 */
public class TextFunUtils {

    /**
     * 字体和颜色
     * @param text
     * @param color
     * @return
     */
    public static SpannableString spanColor(String text, String first,String inputText, int color){
        SpannableString spanString = new SpannableString(text);
        ForegroundColorSpan span2 = new ForegroundColorSpan(color);
        spanString.setSpan(span2,first.length(),first.length()+inputText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     *
     * @param text 总长度
     * @param first 不需要修改颜色的 字符串
     * @param color 需要 修改的 颜色
     * @return
     */
    public static SpannableString spanColor(@NonNull String text, @NonNull String first, int color){
        SpannableString spanString = new SpannableString(text);
        ForegroundColorSpan span2 = new ForegroundColorSpan(color);
        spanString.setSpan(span2,first.length(),text.length()-first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 给TextView添加删除线
     *
     * @param content 内容
     * @return
     */
    public static SpannableString setDeleteText(String content) {
        SpannableString sp = null;
        if (content != null && !android.text.TextUtils.isEmpty(content)) {
            sp = new SpannableString(content);
            sp.setSpan(new StrikethroughSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    /**
     * 字体和颜色
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static SpannableString spanColorAndSize(String text,int size,int color){
        SpannableString spanString = new SpannableString(text);
        ForegroundColorSpan span2 = new ForegroundColorSpan(color);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size);
        spanString.setSpan(span, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(span2,0,text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 字体加粗
     *
     * @param str
     * @return
     */
    public static SpannableString spanStringBold(int start, int end, String str) {
        if (str == null) {
            str = "";
        }
        SpannableString spannableString = new SpannableString(str);
        StyleSpan span = new StyleSpan(Typeface.BOLD);//加粗
        spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 对指定文本进行大小的改变处理
     *
     * @param first      第一段文本
     * @param second     第二段文本
     * @param firstSize  第一段文本的大小 单位px
     * @param secondSize 第二段文本的大小 单位px
     * @return 处理过的String
     */
    public static SpannableString spanStringSize(String first, String second, int firstSize, int secondSize, int start, int end, boolean isBold) {
        SpannableString spannableString = null;
        if (!TextUtils.isEmpty(first)) {
            spannableString = new SpannableString(first + second);
        } else {
            spannableString = new SpannableString(second);
        }
        if (firstSize != -1 && !TextUtils.isEmpty(first)) {
            AbsoluteSizeSpan mFirstSize = new AbsoluteSizeSpan(firstSize);
            spannableString.setSpan(mFirstSize, 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (secondSize != -1 && !TextUtils.isEmpty(second)) {
            AbsoluteSizeSpan mSecondSize = new AbsoluteSizeSpan(secondSize);
            spannableString.setSpan(mSecondSize, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isBold) {
            StyleSpan span = new StyleSpan(Typeface.BOLD);//加粗
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
