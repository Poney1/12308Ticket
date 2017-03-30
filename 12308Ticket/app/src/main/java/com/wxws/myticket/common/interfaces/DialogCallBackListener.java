package com.wxws.myticket.common.interfaces;

/**
 * desc:
 * Date: 2016/10/20 16:44
 *
 * @auther: lixiangxiang
 */
public interface DialogCallBackListener {
    /**
     * 点击确认，请在此方法内实现点击dialog的确认按钮后的操作
     *
     * @param string
     */
    void doPositiveClick(String string);

    void doPositiveClick();

    /**
     * 点击取消，请在此方法内实现点击dialog的取消按钮后的操作
     */
    void doNegativeClick();
}
