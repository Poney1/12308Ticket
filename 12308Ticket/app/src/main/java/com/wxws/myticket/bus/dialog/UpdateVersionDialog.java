package com.wxws.myticket.bus.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.interfaces.IDialog;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.widgets.dialog.DialogDownProgress;
import com.wxws.myticket.common.widgets.dialog.FragmentDialog;

/**
 * 加载进度对话框
 *
 * @author ljf
 * @version 2014年7月30日 上午11:04:52
 */
public class UpdateVersionDialog extends Dialog {
    private BaseActivity activity;
    private View view;
    private TextView mTvTitle;
    private TextView mDialogMessage;
    private Button mDialogBtnOk;
    private Button mDialogBtnCancel;
    private ImageView mImgLine;
    private String url;
    private boolean forceUpdate;
    private IDialog mDialog;//
    public boolean isCancel = false;//是否是后退键导致 popwindow 取消
    private int theme;


    public UpdateVersionDialog(BaseActivity activity,int theme) {
        super(activity,theme);
        this.activity = activity;
        this.theme = theme ;
        mDialog = new FragmentDialog(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog_update_version);
        bindViews();
    }

    private void bindViews() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mDialogMessage = (TextView)findViewById(R.id.dialog_message);
        mDialogBtnOk = (Button) findViewById(R.id.dialog_btn_ok);
        mImgLine = (ImageView) findViewById(R.id.imgLine);
        mDialogBtnCancel = (Button)findViewById(R.id.dialog_btn_cancel);
        mDialogBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateVersionDialog.this.dismiss();
                AppApplication.getInstance().isShowUpdate = false;// 一次之后不再弹出
                if (SystemUtils.wifiConnection(activity)) {
                    continueDownload();
                } else {
                    mDialog.showDialog(activity.getString(R.string.app_download_use_flow), new DialogCallBackListener() {
                        @Override
                        public void doPositiveClick(String string) {

                        }

                        @Override
                        public void doPositiveClick() {
                            continueDownload();
                        }

                        @Override
                        public void doNegativeClick() {
                            mDialog.dissmissDialog();
                        }
                    });
                }

            }
        });
        mDialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppApplication.getInstance().isShowUpdate = false;// 一次之后不再弹出
                UpdateVersionDialog.this.dismiss();
            }
        });
    }

    public void setData(String title, String content, boolean forceUpdate, String url) {
        this.forceUpdate = forceUpdate;
        this.url = url;
        mTvTitle.setText(title);
        mDialogMessage.setText(content);
        if (forceUpdate) {
            mImgLine.setVisibility(View.GONE);
            mDialogBtnCancel.setVisibility(View.GONE);
        }
    }

    //是否继续下载
    private void continueDownload() {
        DialogDownProgress downProgress = new DialogDownProgress(activity, url,theme);

        if (forceUpdate) {
            downProgress.setCancelable(false);
        } else {
            downProgress.setCanceledOnTouchOutside(false);
        }
        if (url.endsWith(".apk")) {
            downProgress.show();
            downProgress.setData(forceUpdate);
            downProgress.startDownload();
            isCancel = true;
        } else {
            ToastManager.getInstance().showToast(activity, activity.getString(R.string.app_download_error));
        }
    }
}
