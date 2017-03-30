/******************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.wxws.myticket.common.widgets.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import com.wxws.myticket.R;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.interfaces.DialogCallBack;

import java.io.File;
import java.text.NumberFormat;

/**
 * 下载对话框
 * @author lixiangxiang
 */
public class DialogDownProgress extends AlertDialog {

	private ProgressBar progressBar;
	private TextView textPercent, textNumber;
	private Button btnCancel;
	private Handler handler;

	private boolean isCancel;//能否取消
	private String message = "下载中ing";//显示文字
	private static final String numberFormat = "%1.2fM/%2.2fM";
	private int fileSize, currentPosition;
	private boolean isStart;
	private NumberFormat percentFormat;
	private DialogCallBack cancelCallBack;
	private String url;
	private Context mContext;
	private String mPath;
	private BaseDownloadTask mBaseDownloadTask;
	private int mLength;
	private boolean isContinue = false ;

	public DialogDownProgress(Context context, String url,int theme) {
		super(context,theme);
		this.mContext = context ;
		this.url = url;
		init();
	}

	public void startDownload(){
		mPath = ConfigConstants.FILE_ROOT_DIRECTORY + File.separator +"download" + File.separator+ "Ticket12308.apk";
		mBaseDownloadTask = FileDownloader.getImpl().create(url).setPath(mPath).
				setListener(new FileDownloadListener() {
			@Override
			protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
				setData(isCancel);
			}

			@Override
			protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
				fileSize = totalBytes ;
				currentPosition = 100 * soFarBytes / totalBytes ;
				setProgress(currentPosition);
			}

			@Override
			protected void completed(BaseDownloadTask task) {
				fileSize = (int)task.getLargeFileTotalBytes();
				currentPosition = 100;
				setProgress(currentPosition);
				try {
					File apkFile = new File(mPath);
					installApkVersion(apkFile);
					if (!isCancel){
						DialogDownProgress.this.dismiss();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}

			@Override
			protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

			}

			@Override
			protected void error(BaseDownloadTask task, Throwable e) {
				btnCancel.setText("下载失败,点击继续");
				btnCancel.setClickable(true);
				isContinue = true;
			}

			@Override
			protected void warn(BaseDownloadTask task) {
			}
		});
		mLength = mBaseDownloadTask.start();
	}
	/**
	 * 初始化
	 */
	private void init() {
		percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_download);
		progressBar = (ProgressBar) findViewById(R.id.progress);
		textPercent = (TextView) findViewById(R.id.text_percent);
		textNumber = (TextView) findViewById(R.id.text_number);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isContinue){
					startDownload();
				}else {
					isContinue = false;
					DialogDownProgress.this.dismiss();
					FileDownloader.getImpl().pause(mLength);
				}
			}
		});
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				int progress = progressBar.getProgress();
				double dMax = (double) fileSize / (double) (1024 * 1024);
				double dProgress = (double) progress / 100 * dMax;
				textNumber.setText(String.format(numberFormat, dProgress, dMax));// 格式化成m的格式
				if (percentFormat != null) {
					double percent = (double) progress / (double) progressBar.getMax();
					SpannableString tmp = new SpannableString(percentFormat.format(percent));
					tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textPercent.setText(tmp);
				} else {
					textPercent.setText("");
				}
				return false;
			}
		});
		onProgressChanged();
		if (currentPosition > 0) {
			setProgress(currentPosition);
		}
	}

	/**
	 * 进度变更
	 */
	private void onProgressChanged() {
		handler.sendEmptyMessage(0);
	}

	/**
	 * 设置进度条的最大值
	 * @param fileSize
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}


	/**
	 * 设置数据
	 * @param isCancel
	 */
	public void setData(boolean isCancel) {
		this.isCancel = isCancel;
		if (isCancel){
			this.message = "下载ing";
		}else {
			this.message= "取消下载";
		}
		btnCancel.setText(message);
		btnCancel.setClickable(!isCancel);
	}

	/**
	 * 设置进度条进度
	 * @param value
	 */
	public void setProgress(int value) {
		if (isStart) {
			progressBar.setProgress(value);
			onProgressChanged();
		} else {
			currentPosition = value;
		}
	}

	/**
	 * 设置取消回调
	 * @param callBack
	 */
	public void setCancel(DialogCallBack callBack) {
		cancelCallBack = callBack;
	}

	@Override
	protected void onStart() {
		super.onStart();
		isStart = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isStart = false;
	}

	/**
	 * 安装APK
	 * @param apkFile
	 */
	private void installApkVersion(File apkFile) {
		if(apkFile != null && apkFile.exists()) {
			Intent apkIntent = new Intent(Intent.ACTION_VIEW);
			apkIntent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
			mContext.startActivity(apkIntent);
		}
	}
}
