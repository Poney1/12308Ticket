package com.wxws.myticket.common.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * desc: 字符串处理
 * Date: 2016/10/10 17:36
 *
 * @auther: lixiangxiang
 */
public class StringUtils {


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNullOrEmpty(EditText str) {
        return str.getText().toString() == null || str.getText().toString().trim().length() == 0;
    }

    /**
     * 电话号码长度为11位的数字
     *
     * @param s
     * @return
     */
    public static boolean isPhone(String s) {
        if (isNullOrEmpty(s)) {
            return false;
        }
        if (s.length() != 11) {
            return false;
        }

        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(s.trim());
        return m.matches();

    }

    /**
     * 是否包含特殊字符
     *
     * @param special
     * @return
     */
    public static boolean isHaveSpecial(String special) {

        //中文数字英文
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9]+$");
        Matcher m = p.matcher(special);

        //纯中文
        Pattern p1 = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
        Matcher m1 = p1.matcher(special);

        //纯数字
        Pattern p2 = Pattern.compile("^[0-9]*$");
        Matcher m2 = p2.matcher(special);

        //纯字母
        Pattern p3 = Pattern.compile("[a-zA-Z]");
        Matcher m3 = p3.matcher(special);

        return m2.matches() || m.matches() || m1.matches() || m3.matches();
    }

    /**
     * 小数点后有大于0的值则保留，否则不保留
     *
     * @param num
     * @return
     */
    public static String doubleTrans(String num) {
        try {
            if (!isNullOrEmpty(num)) {
                return doubleTrans(Double.parseDouble(num) + "");
            }
        } catch (Exception ex) {
            return num;
        }
        return num;
    }

    /**
     * 保留 scale 位小数
     * @param price
     * @param scale
     * @return
     */
    public static String decimals(String price,int scale){
        String zero = price;
        try {
            if(TextUtils.isEmpty(price)){
                return  "";
            }
            //保留两位小数
            BigDecimal b   =   new   BigDecimal(Double.valueOf(price));
            double   f1   =   b.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
            price = String.valueOf(f1).trim();
            zero =   removeZero(String.valueOf(price));
        }catch (Exception e){
            return "";
        }
        return  zero;
    }

    /**
     * 去掉小数点后的0
     * @param zero
     * @return
     */
    public static String removeZero(String zero){
        if (isNullOrEmpty(zero))
            return "";
        if(zero.indexOf(".") > 0){
            //正则表达
            zero = zero.replaceAll("0+?$", "");//去掉后面无用的零
            zero = zero.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return  zero;
    }
    /**
     * 保留 2 位 小数
     * @param price
     * @return
     */
    public static String decimals(String price){
        try {
            if(TextUtils.isEmpty(price)){
                return  "";
            }
            //保留两位小数
            BigDecimal b   =   new   BigDecimal(Double.valueOf(price));
            double   f1   =   b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            price = String.valueOf(f1).trim();
            doubleTrans(String.valueOf(price));
        }catch (Exception e){
            return "";
        }
        //保留两位小数字
        return  price;
    }

    /**
     * 是否符合密码规则(6-20位数字或字母)
     *
     * @return
     */
    // TODO 忘记密码是否是这个规则？
    public static boolean isPsw(String psw) {
        return psw.length() > 5 && psw.length() < 21;
    }

    /**
     * 字符串的最小最大长度
     *
     * @param str
     * @return
     */
    public static boolean requireLength(String str, int minLenth, int maxLength) {
        return !(str.length() < minLenth || str.length() > maxLength);
    }

    /**
     * * 限制中文 并限制长度
     *
     * @param special
     * @param length
     * @return
     */

    public static boolean isHaveChinaLength(String special, int length) {

        if (special.length() > length) {
            return false;
        }
        // 中文
        Pattern p1 = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
        Matcher m1 = p1.matcher(special);
        return m1.matches();
    }

    /**
     * 数字并限制长度
     *
     * @param special
     * @param length
     * @return
     */
    public static boolean isNumberLength(String special, int length) {

        if (special.length() > length) {
            return false;
        }
        //纯数字
        Pattern p2 = Pattern.compile("^[0-9]*$");
        Matcher m2 = p2.matcher(special);
        return m2.matches();
    }
    /**
     * 只包含中文和英文
     * @param special
     * @return
     */
    public static boolean isHaveChinaEnglish(String special){
        //除空格
        Pattern p0 = Pattern.compile("\\s+");
        Matcher m0 = p0.matcher(special);
        special  = m0.replaceAll("");
        // 英文 + 中文
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z]+$");
        Matcher m = p.matcher(special);
        // 中文
        Pattern p1 = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
        Matcher m1 = p1.matcher(special);
        //英文
        Pattern p3 = Pattern.compile("[a-zA-Z]");
        Matcher m3 = p3.matcher(special);

        return m.matches() || m1.matches() || m3.matches();
    }

    /**
     * 英文  和 数字
     * @param special
     * @return
     */
    public static boolean isEnglishAndNum(String special){

        //中文数字英文
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m = p.matcher(special);


        //纯数字
        Pattern p2 = Pattern.compile("^[0-9]*$");
        Matcher m2 = p2.matcher(special);

        //纯字母
        Pattern p3 = Pattern.compile("[a-zA-Z]");
        Matcher m3 = p3.matcher(special);

        return m2.matches() || m.matches() || m3.matches();
    }

    /**
     * 判断 一个字符串 含有多少个文字
     * @param text
     * @return
     */
    public  static  int getTextChineseCount(String text){
        int count = 0 ;

        String regex = "[\u4e00-\u9fff]";

        count = text.split(regex).length -1;

        return  count;
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return doubleTrans(b1.multiply(b2).doubleValue()+"");
    }
}
