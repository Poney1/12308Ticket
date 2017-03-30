package com.wxws.myticket.common.widgets.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.wxws.myticket.R;


/**
 * desc: 加载ing
 * Date: 2016/10/10 10:08
 *
 * @auther: lixiangxiang
 */
public class LoadingProgressDialog extends ProgressDialog {

    private boolean  isShowDialog;//是否显示dialog
    private Context context ;
    private TextView tvMessage;

    public LoadingProgressDialog(Context context) {
        this(context,true);
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingProgressDialog(Context context,boolean isShowDialog){
        super(context,R.style.loadingDialog);
        this.isShowDialog  = isShowDialog ;
        this.context = context ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_loading);
        initView();
        LoadingProgressDialog.this.setCanceledOnTouchOutside(false);
        initData();
    }

    private void initData() {
        tvMessage.setText(R.string.base_activity_loading);
    }
    private void initView() {
        tvMessage = (TextView) findViewById(R.id.dialog_message);
    }

    public void showLoadingDialog(){
        if (context != null &&  !LoadingProgressDialog.this.isShowing()){
            LoadingProgressDialog.this.show();
        }
    }

    public void dismissLoadingDialog(){
        if (context !=null && LoadingProgressDialog.this.isShowing())
        LoadingProgressDialog.this.dismiss();
    }
}
