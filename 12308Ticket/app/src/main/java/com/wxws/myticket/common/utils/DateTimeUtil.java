package com.wxws.myticket.common.utils;

import android.text.format.Time;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * desc: 日期 处理
 * Date: 2016/10/19 09:44
 *
 * @auther: lixiangxiang
 */
public class DateTimeUtil {

    /**
     * 获取系统当前日期（格式2012-12-28）
     *
     * @return
     */
    public static String getSysDateYMD() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
    }
    /**
     * 获取系统当前日期（格式2012-12-28）
     *
     * @return
     */
    public static String getSysDateYMDTom() {
        return getSysDateYMD(24 * 60 * 60 * 1000);
    }

    /**
     * 获取系统当前日期（格式2012-12）
     *
     * @return
     */
    public static String getSysDateYM() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM");
    }
    /**
     * 获取系统当前日期 年份（格式2012）
     *
     * @return
     */
    public static String getSysDateY() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy");
    }

    /**
     * 获取系统当前日期前多少天
     * time 时间
     *
     * @return
     */
    public static String getSysDateYMD(long time) {
        return getDate(new Date(System.currentTimeMillis() + time), "yyyy-MM-dd");
    }

    /**
     * 获取系统当前时间（格式13:58:00）
     *
     * @return
     */
    public static String getSysTimeHMS() {
        return getDate(new Date(System.currentTimeMillis()), "HH:mm:ss");
    }

    /**
     * 获取系统当前时间（格式2012-12-28 13:58:00）
     *
     * @return
     */
    public static String getSysTimeYMDHMS() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getSysTimeYMDHM() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm");
    }

    public static  String getSystTimeMDHM(){
        return getDate(new Date(System.currentTimeMillis()), "MM月dd日-HH-mm");
    }
    /**
     * 日期转获取字符串
     *
     * @param date   日期
     * @param format 格式
     *               ("yyyy-MM-dd hh:mm:ss")
     * @return
     */
    public static String getDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式时间，如果不是两位数的前面补0
     *
     * @param x
     * @return
     */
    public static String format(int x) {
        String s = "" + x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * 字符串转日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获取上个月 2015-05
     *
     * @param dateStr 2015-06
     * @return
     */
    public static String preMonth(String dateStr) {
        Date date = StringToDate(dateStr, "yyyy-MM");
        Date pre = addMonth(date, -1);
        return getDate(pre, "yyyy-MM");
    }

    /**
     * 获取下个月 2015-05
     *
     * @param dateStr 2015-04
     * @return
     */
    public static String nextMonth(String dateStr) {
        Date date = StringToDate(dateStr, "yyyy-MM");
        Date pre = addMonth(date, 1);
        return getDate(pre, "yyyy-MM");
    }

    public static String TimestampToString(Timestamp dateStr) {
        if (dateStr != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateStr);
        } else {
            return null;
        }
    }

    public static Timestamp StringToTimestamp(String dateStr) {
        return Timestamp.valueOf(dateStr);
    }

    public static String TimestampToString(Timestamp dateStr, String format) {
        return new SimpleDateFormat(format).format(dateStr);
    }

    public static Timestamp DoubleToTimespan(double time) {
        return longToTimespan((long) time);
    }

    public static Timestamp longToTimespan(long time) {
        return new Timestamp(time);
    }

    public static double TimespanToDouble(Timestamp time) {
        return time.getTime();
    }

    /**
     * 日期增加N天
     *
     * @param day  当前日期
     * @param days 增加天数
     * @return
     */
    public static Date addDay(Date day, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(day); // 设置当前日期
        c.add(Calendar.DATE, days); // 日期加7
        return c.getTime();
    }

    /**
     * 增加多少天
     * @param strDate
     * @param days
     * @return
     */
    public static String addDay(String strDate, int days) {
        Date date = StringToDate(strDate, "yyyy-MM-dd");
        Date newdate = addDay(date, days);
        return getDate(newdate, "yyyy-MM-dd");
    }

    /**
     * 日期增加N月
     *
     * @param day 当前日期
     * @return
     */
    public static Date addMonth(Date day, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(day); // 设置当前日期
        c.add(Calendar.MONTH, month); // 日期加7
        return c.getTime();
    }

    /**
     * 日期增加N年
     *
     * @param day   当前日期
     * @param years 增加天数
     * @return
     */
    public static Date addYear(Date day, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(day); // 设置当前日期
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    public static Date addHour(Date day, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(day); // 设置当前日期
        c.add(Calendar.HOUR, hour); // 时间增加
        return c.getTime();
    }

    public static Date addMinute(Date day, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(day); // 设置当前日期
        c.add(Calendar.MINUTE, minute); // 时间增加
        return c.getTime();
    }

    public static Date addSecond(Date day, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(day); // 设置当前日期
        c.add(Calendar.SECOND, second); // 时间增加
        return c.getTime();
    }

    public static String formatSeconds(long second) {
        String h = "0";
        String d = "0";
        String s = "0";
        long temp = second % 3600;
        if (second > 3600) {
            h = String.valueOf(second / 3600);
            if (Integer.parseInt(h) < 10) {
                h = "0" + h;
            }
            if (temp != 0) {
                if (temp > 60) {
                    d = String.valueOf(temp / 60);
                    if (Integer.parseInt(d) < 10) {
                        d = "0" + d;
                    }
                    if (temp % 60 != 0) {
                        s = String.valueOf(temp % 60);
                        if (Integer.parseInt(s) < 10) {
                            s = "0" + s;
                        }
                    }
                } else {
                    s = String.valueOf(temp);
                    d = "00";
                    if (Integer.parseInt(s) < 10) {
                        s = "0" + s;
                    }
                }
            } else {
                d = "00";
                s = "00";
            }
        } else {
            h = "00";
            d = String.valueOf(second / 60);

            if (Integer.parseInt(d) > 0 && Integer.parseInt(d) < 10) {
                d = "0" + d;
            }

            if (Integer.parseInt(d) % 60 == 0) {

                h = String.valueOf((Integer.parseInt(d) / 60));
                if (Integer.parseInt(h) > 0 && Integer.parseInt(h) < 10) {
                    h = "0" + h;
                }
                d = "00";
            }
            s = "00";
            if (second % 60 != 0) {
                s = String.valueOf(second % 60);
                if (Integer.parseInt(s) > 0 && Integer.parseInt(s) < 10) {
                    s = "0" + s;
                }
            }
        }

        return h + ":" + d + ":" + s;
    }

    /**
     * 时间差，返回秒
     *
     * @param start
     * @param end
     * @return
     */
    public static long diffTime(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt_start = formatter.parse(start);
            Date dt_end = formatter.parse(end);
            return (dt_end.getTime() - dt_start.getTime()) / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 时间差，返回分钟
     *
     * @param start
     * @param end
     * @return
     */
    public static long diffSecond(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt_start = formatter.parse(start);
            Date dt_end = formatter.parse(end);
            return (dt_end.getTime() - dt_start.getTime()) / (60*1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 日期差
     *
     * @param start
     * @param end
     * @return
     */
    public static long diffDate(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt_start = formatter.parse(start);
            Date dt_end = formatter.parse(end);

            return (dt_end.getTime() / 1000 - dt_start.getTime() / 1000) / (60 * 60 * 24);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 是否小于今天
     *
     * @param date
     * @return
     */
    public static boolean Lessthantoday(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt_start = formatter.parse(getSysDateYMD());
            Date dt_end = formatter.parse(date);

            if ((dt_end.getTime() / 1000 - dt_start.getTime() / 1000) / (60 * 60 * 24) < 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否超过预售期
     *
     * @param date
     * @return
     */
    public static boolean Morethanpredate(String date, int predate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt_start = formatter.parse(getSysDateYMD());
            Date dt_end = formatter.parse(date);

            if ((dt_end.getTime() / 1000 - dt_start.getTime() / 1000) / (60 * 60 * 24) > predate) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long stringToLong(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dtdate = formatter.parse(date);
            return dtdate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 日期大小比较
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean after(String beginDate, String endDate) {
        Date b = DateTimeUtil.StringToDate(beginDate, "yyyy-MM-dd");
        Date e = DateTimeUtil.StringToDate(endDate, "yyyy-MM-dd");
        return b.after(e);
    }

    /**
     * 日期时间比较
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static boolean compareTo(String beginDate, String endDate) {
        try {
            Date b = DateTimeUtil.StringToDate(beginDate, "yyyy-MM-dd HH:mm");
            Date e = DateTimeUtil.StringToDate(endDate, "yyyy-MM-dd HH:mm");
            return b.after(e);
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean equal(String beginDate, String endDate) {
        Date b = DateTimeUtil.StringToDate(beginDate, "yyyy-MM-dd");
        Date e = DateTimeUtil.StringToDate(endDate, "yyyy-MM-dd");
        return b.equals(e);
    }

    public static String getToday() {
        StringBuffer day = new StringBuffer();
        Time time = new Time();
        time.setToNow();
        return day.append(time.year).append((time.month + 1)).append(time.monthDay).toString();
    }

    /**
     * 获取上个月的今天
     *
     * @return
     */
    public static String getLastMonthToday() {
        StringBuffer stringBuffer = new StringBuffer();
        Time time = new Time();
        time.setToNow();
        String month = String.valueOf(time.month);
        String day = String.valueOf(time.monthDay);
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (day.length() < 2) {
            day = "0" + day;
        }

        return stringBuffer.append(time.year).append("-").append(month).append("-").append(day).toString();
    }


    public static String getTomorrow() {
        StringBuffer stringBuffer = new StringBuffer();
        Time time = new Time();
        time.setToNow();
        String month = String.valueOf(time.month + 1);
        String day = String.valueOf(time.monthDay + 1);
        return stringBuffer.append(time.year).append(month).append(day).toString();
    }

    //获取明天的格式
    public static String getTomorrowDate() {
        StringBuffer stringBuffer = new StringBuffer();
        Time time = new Time();
        time.setToNow();
        String month = String.valueOf(time.month + 1);
        String day = String.valueOf(time.monthDay + 1);
        return stringBuffer.append(time.year).append("-").append(month).append("-").append(day).toString();
    }

    /**
     * 后天日期
     *
     * @return
     */
    public static String getTheDayData() {
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};
        List<String> list_big = Arrays.asList(months_big);
        List<String> list_little = Arrays.asList(months_little);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (day == 30) {//30号
            if (list_big.contains(String.valueOf(month))) {
                day = 1;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
            if (list_little.contains(String.valueOf(month))) {
                day = 2;
                if (month == 12) {
                    year++;
                    month = 1;
                } else {
                    month++;
                }
            }
        } else if (day == 31) {//31号
            day = 2;
            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }
        } else if (day == 27 && month == 2) {//2月特殊处理
            day = 1;
            month++;
        } else if (day == 28 && month == 2) {//2月特殊处理
            day = 2;
            month++;
        } else {
            day = day + 2;
        }
        String data = year + "" + month + day;
        return data;
    }

    /**
     * + : 正确的时间 - : 过去的时间,错误的
     * hw 直接获取的10天以内的时间无需判断
     *
     * @param compare
     * @return
     */
    public static long getDaysBetween(int[] compare) {

        Time t = new Time();
        t.set(compare[2], compare[1], compare[0]);
        long t1m = t.toMillis(false);

        Time now = new Time();
        now.setToNow();
        int y = now.year;
        int m = now.month;
        int d = now.monthDay;
        now.set(d, m, y);
        long tnm = now.toMillis(false);

        long days = (t1m - tnm) / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 获取星期
     *
     * @param strDate
     * @return
     */
    public static String getWeekStr(String strDate) {
        if (StringUtils.isNullOrEmpty(strDate)) {
            return "";
        }
        Calendar c = Calendar.getInstance();
        Date date = StringToDate(strDate, "yyyy-MM-dd");
        c.setTime(date);
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

    public static String getShortWeekStr(String strDate) {
        if (StringUtils.isNullOrEmpty(strDate)) {
            return "";
        }
        Calendar c = Calendar.getInstance();
        Date date = StringToDate(strDate, "yyyy-MM-dd");
        c.setTime(date);
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "周" + mWay;
    }

    /**
     * 某月第一天
     *
     * @return
     */
    public static String getFirstDay(String dateStr) {
        if (StringUtils.isNullOrEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = StringToDate(dateStr, "yyyy-MM");
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return df.format(gcLast.getTime());
    }

    /**
     * 某月最后一天
     *
     * @return
     */
    public static String getLastDay(String dateStr) {
        if (StringUtils.isNullOrEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = StringToDate(dateStr, "yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1); // 加一个月
        calendar.set(Calendar.DATE, 1); // 设置为该月第一天
        calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        return df.format(calendar.getTime());
    }

    public static String[] format_time(String time) {
        if (StringUtils.isNullOrEmpty(time)) {
            return null;
        }
        String[] t = time.split(":");

        String[] times = new String[4];
        if (t[0].length() == 1) {
            times[0] = "0";
            times[1] = t[0];
        } else {
            times[0] = t[0].substring(0, 1);
            times[1] = t[0].substring(1, 2);
        }

        if (t[1].length() == 1) {
            times[2] = "0";
            times[3] = t[1];
        } else {
            times[2] = t[1].substring(0, 1);
            times[3] = t[1].substring(1, 2);
        }
        return times;
    }

    /**
     * @param date
     * @return 2月14日(周二)
     */
    public static String stringToYY_MM_week(String date) {
        if (date == null || date.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(getDate(new Date(stringToLong(date)), "MM月dd日"))
                    .append("(")
                    .append(getShortWeekStr(date))
                    .append(")");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     *  分钟数转换成时钟数输出
     * @param minutes
     * @return
     */
    public static String minToHour(String minutes){

        int minute = Integer.parseInt(minutes);
        int hour = minute / 60;
        minute = minute % 60;
        String hourstr = new DecimalFormat("00").format(hour);
        String minstr = new DecimalFormat("00").format(minute);

        return hourstr+":"+minstr;
    }

    /**
     * 根据完整时间获得MM-dd格式数据
     * @param date
     * @return
     */
    public static String getDateMMDD(String date){

        Date nowdate = StringToDate(date,"yyyy-MM-dd HH:mm");

        return getDate(nowdate, "MM-dd");

    }

    /**
     * 根据完整时间获得HH:mm格式数据
     * @param date
     * @return
     */
    public static String getDateHHMM(String date){
        Date nowdate = StringToDate(date,"yyyy-MM-dd HH:mm");
        return getDate(nowdate,"HH:mm");
    }

    /**
     * 根据日期获取实际MM月dd日日期
     * @param date
     * @return
     */
    public static String getChineseMMDD(String date){
        Date nowdate = StringToDate(date,"yyyy-MM-dd HH:mm");
        return getDate(nowdate,"MM月dd日");
    }

    public static String getSpChineseMMDD(String date){
        Date nowdate  = null;
        try{
            nowdate = StringToDate(date,"yyyy-MM-dd");
        }catch (Exception e){
            e.printStackTrace();
        }
        return getDate(nowdate,"MM月dd日");
    }

    /**
     * 根据完整时间戳获得年月日时间
     * @param date
     * @return
     */
    public static String getDateyyyyMMdd(String date){
        Date nowdate = StringToDate(date,"yyyy-MM-dd HH:mm");
        return getDate(nowdate,"yyyy-MM-dd");
    }

    /**
     * 根据当前日期获取三个月之前的日期
     * @return
     */
    public static String getThreeMonthAgoDate(){

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -3); // 减去3个月
        calendar.add(Calendar.DATE, 1);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 除去日期中的第一个0
     * @param zero
     * @return
     */
    public static String removeFirstZero(String zero){

        String date ;
        String first = zero.substring(0,1);

        if ("0".equals(first)){
            date = zero.substring(1,2);
        }else {
            date = zero ;
        }

        return date;
    }

}
