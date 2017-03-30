package com.wxws.myticket.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * desc: toast 管理
 * Date: 2016/10/10 14:10
 *
 * @auther: lixiangxiang
 */
public class ToastManager {

    private Toast mToast;

    private static ToastManager toastManager;
    private ToastManager(){
    }

    public static ToastManager getInstance(){
        if (toastManager == null){
            toastManager = new ToastManager();
        }
        return  toastManager;
    }
    /**
     * 提示消息位置统一管理
     * @param context
     * @param resid
     */
    public  void showToast(Context context, int resid) {
        if (context == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 200);
        }
        mToast.setText(resid);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public  void showToast(Context context, String msg) {
        if (context == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0, 0);

        }
        mToast.setText(msg);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public  void showToast(Context context, String msg, int showtime) {
        if (context == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, showtime);
            mToast.setGravity(Gravity.CENTER,0, 200);
        }
        mToast.setText(msg);
        mToast.setDuration(showtime);
        mToast.show();
    }

    public  void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast=null;
        }
    }
}
