package com.wxws.myticket.bus.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.wxws.myticket.R;

/**
 * desc: 半价票描述
 * Date: 2016/11/12 15:19
 *
 * @auther: lixiangxiang
 */
public class HalfPriceDialog  extends Dialog{
    public HalfPriceDialog(Context context) {
        super(context);
    }

    public HalfPriceDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HalfPriceDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_half_price);

        initView();

        initData();
    }

    private void initData() {

    }

    private void initView() {

    }
}
