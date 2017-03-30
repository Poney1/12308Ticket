package com.wxws.myticket.train.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.common.widgets.view.JustifyTextView;
import com.wxws.myticket.train.activity.FillinOrderActivity;

/**
 * desc: 火车票抢票失败窗口
 * Date: 2016-07-28 14:38
 *
 * @auther: jiangyan
 */
public class BookFailureDialog extends Dialog {


    private Button btnRob;
    private TextView tvClose;
    private FillinOrderActivity activity;
    private JustifyTextView tvFail;
    private String info;

    public BookFailureDialog(FillinOrderActivity activity, String info) {
        super(activity, R.style.dialog);
        this.activity  = activity ;
        this.info  = info ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_book_failure);
        BookFailureDialog.this.setCanceledOnTouchOutside(false);
        BookFailureDialog.this.setCancelable(false);
        initView();
        bindClick();
        tvFail.setText(info);
    }

    private void bindClick() {
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookFailureDialog.this.dismiss();
            }
        });

        btnRob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookFailureDialog.this.dismiss();
                //重新抢票
                activity.robTicket();
            }
        });
    }

    private void initView() {
        tvClose = (TextView) findViewById(R.id.tv_close);
        tvFail = (JustifyTextView) findViewById(R.id.tv_fail);
        btnRob = (Button) findViewById(R.id.btn_rob);
    }

}