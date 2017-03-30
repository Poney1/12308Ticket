package com.wxws.myticket.order.widgets;

import android.content.Context;
import android.content.DialogInterface;

import com.wxws.myticket.R;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.interfaces.IDialog;
import com.wxws.myticket.common.widgets.dialog.DialogFrag;

/**
 * desc:
 * Date: 2016-11-02 14:27
 *
 * @author jiangyan
 */
public class CommonDialog implements IDialog {
    Context context;

    public CommonDialog(Context context) {
        super();
        this.context = context;
    }


    @Override
    public void showDialog(int id) {
        DialogFrag.showDialog(context, id, R.layout.customdialog_ok, null);
    }

    @Override
    public void showDialog(final int id, int resid) {
        DialogFrag.showDialog(context, id, resid, new DialogCallBackListener() {

            @Override
            public void doPositiveClick(String string) {}

            @Override
            public void doPositiveClick() {

            }

            @Override
            public void doNegativeClick() {}
        });
    }

    @Override
    public void dissmissDialog() {
        DialogFrag.removeDialog(context);
    }

    @Override
    public void showDialog(String msg) {
        DialogFrag.showDialog(context, msg, R.layout.customdialog_ok, null);

    }

    @Override
    public void showDialog(String msg, DialogCallBackListener callback) {
        DialogFrag.showDialog(context, msg, R.layout.customdialog_ok_cancel, callback);
    }

    @Override
    public void showDialog(String title, String msg, DialogCallBackListener callback) {
        DialogFrag.showDialog(context, title, msg, R.layout.customdialog_ok_cancel, callback);
    }

    @Override
    public void showDialog(String title, String msg, int layoutId, DialogCallBackListener callback) {
        DialogFrag.showDialog(context, title, msg, layoutId, callback);
    }

    @Override
    public void showDialog(String title, String msg, int layoutId,boolean dismiss, DialogCallBackListener callback, DialogInterface.OnDismissListener onDismiss) {
        DialogFrag.showDialog(context, title, msg, layoutId, dismiss,callback,onDismiss);
    }

    @Override
    public void showProgressDialog(int id) {
        showDialog(id, R.layout.customdialog_loading);
    }

    @Override
    public void showProgressDialog(String msg, DialogCallBackListener callback) {
        showDialog(null, msg, R.layout.customdialog_loading, callback);
    }

    @Override
    public void showRetryDialog(int id, DialogCallBackListener callback) {
        // DialogFrag.showDialog(context, id,
        // R.layout.customdialog_retry_cancel, callback);
    }

    @Override
    public boolean isShow() {
        return DialogFrag.show;
    }

}