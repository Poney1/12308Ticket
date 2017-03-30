package com.wxws.myticket.common.widgets.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.wxws.myticket.R;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.interfaces.IDialog;

/**
 * desc:
 * Date: 2016/10/20 16:28
 *
 * @auther: lixiangxiang
 */
public class FragmentDialog implements IDialog{
    Context context;

    public FragmentDialog(Context context) {
        super();
        this.context = context;
    }

    /*
     * 只有一个OK按钮的对话框
     * @see com.hbobd.impl.IDialog#showDialog(android.content.Context, int)
     */
    @Override
    public void showDialog(int id) {
        DialogFrag.showDialog(context, id, R.layout.customdialog_ok, null);
    }

    /*
     * 需要处理确定或者取消（包括等待对话框）按钮事件的对话框
     * @see com.hbobd.impl.IDialog#showDialog(android.content.Context, int, int)
     */
    @Override
    public void showDialog(final int id, int resid) {
        DialogFrag.showDialog(context, id, resid, new DialogCallBackListener() {

            @Override
            public void doPositiveClick(String string) {}

            @Override
            public void doPositiveClick() {
                // switch (id) {
                // case R.string.tips_net_expired:
                // // 重启应用
                // context.startActivity(new Intent(context,
                // LoginActivity.class));
                // ((Activity) context).finish();
                // break;
                // default:
                // break;
                // }
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

    /*
     * 确认对话框
     * @see com.hbobd.impl.IDialog#showDialog(android.content.Context,
     * java.lang.String, com.hbobd.fragment.DialogFrag.DialogCallBackListerner)
     */
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
