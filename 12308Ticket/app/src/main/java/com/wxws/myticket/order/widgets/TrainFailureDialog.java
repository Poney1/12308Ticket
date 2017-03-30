package com.wxws.myticket.order.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxws.myticket.R;

/**
 * desc:
 * Date: 2016-11-02 17:18
 *
 * @author jiangyan
 */
public class TrainFailureDialog extends Dialog {
    private String mTitle;
    private String mTipsInfo;
    public TrainFailureDialog(Context context, String title, String tipsinfo) {
        super(context, R.style.dialog);
        mTipsInfo = tipsinfo;
        mTitle= title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_traindetails_failure);
        initView();
    }

    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_traindialog_title);
        TextView tv_tipsinfo = (TextView) findViewById(R.id.tv_traindialog_tipsinfo);

        Button bt_dismiss = (Button) findViewById(R.id.bt_traindialog_dismiss);

        tv_title.setText(mTitle);
        tv_tipsinfo.setText(mTipsInfo);

        bt_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainFailureDialog.this.dismiss();
            }
        });
    }
}