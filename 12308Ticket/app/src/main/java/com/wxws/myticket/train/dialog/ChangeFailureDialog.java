package com.wxws.myticket.train.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxws.myticket.R;

/**
 * desc: 改签失败
 * Date: 2016-07-28 14:38
 *
 * @auther: jiangyan
 */
public class ChangeFailureDialog extends Dialog {


    private Button btnChange;
    private String failure;
    private Button btnSelect;
    private Activity activity;
    private TextView tvChangeInfo;

    public ChangeFailureDialog(Activity activity, String failure) {
        super(activity, R.style.dialog);
        this.failure = failure ;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_failure);
        ChangeFailureDialog.this.setCanceledOnTouchOutside(false);

        initView();

        initData();
    }

    private void initData() {

        tvChangeInfo.setText(failure);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFailureDialog.this.dismiss();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    private void initView() {
        btnChange = (Button) findViewById(R.id.btn_change);
        btnSelect = (Button) findViewById(R.id.btn_select);
        tvChangeInfo = (TextView) findViewById(R.id.tv_change_info);
    }
}