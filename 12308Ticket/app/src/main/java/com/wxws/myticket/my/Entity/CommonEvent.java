package com.wxws.myticket.my.Entity;

/**
 * desc: 普通事件，无需携带数据
 * Date: 2016-11-09 09:55
 *
 * @author jiangyan
 */
public class CommonEvent {

    public static final int LOGIN_SUCCESS = 0;// 用户登录成功发出事件
    public static final int LOGIN_BACK = 10;// 用户进入登录页面未登录 按下返回键的事件
    public static final int TRAIN_LOGINSUCCESS = 1;// 火车票绑定成功事件
    public static final int TRAIN_LOGINFAILURE = 2;// 火车票绑定失败事件
    public static final int LOGIN_LOGOUT = 3;// 用户退出登录
    public static final int COUPON_REFRESH = 4;// 兑换了新的coupon，要进行刷新
    public static final int QUESTION_REFRESH = 5;// 新提了问题，问题列表页面刷新
    public static final int THRIDBIND_REFRESH = 6;// 第三方账号绑定改变，账号列表需要刷新
    public static final int REFUND_ENDORSE_SUCCESS = 7;//退款或者改签成功，界面需要刷新
    public static final int UPDATE_PSW = 8;//修改密码成功，需要跳转到登录页面
    public static final int UPDATE_USERINFO = 9;//修改个人资料成功发出事件
    public static final int CREATE_WYBC_ORDER = 10;//创建成功包车订单

    private int flag;

    public CommonEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}