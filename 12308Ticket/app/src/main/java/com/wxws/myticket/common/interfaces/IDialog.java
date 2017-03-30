package com.wxws.myticket.common.interfaces;

import android.content.DialogInterface;

import com.wxws.myticket.common.widgets.dialog.DialogFrag;

/**
 * desc:
 * Date: 2016/10/20 16:29
 *
 * @auther: lixiangxiang
 */
public interface IDialog {

    void showDialog(final int id);

    void showDialog(final String msg);

    void showDialog(final int id, int resid);

    void showDialog(final String msg, DialogCallBackListener callback);

    void dissmissDialog();

    void showProgressDialog(final int id);
    void showProgressDialog(final String msg, DialogCallBackListener callback);

    void showRetryDialog(final int id, DialogCallBackListener callback);

    boolean isShow();

    void showDialog(String title, String msg, DialogCallBackListener callback);

    void  showDialog(String title, String msg, int layoutId, DialogCallBackListener callback);

    void  showDialog(String title, String msg, int layoutId, boolean dismiss,DialogCallBackListener callback, DialogInterface.OnDismissListener onDismiss);
}
