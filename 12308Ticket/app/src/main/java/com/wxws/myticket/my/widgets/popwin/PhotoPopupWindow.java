package com.wxws.myticket.my.widgets.popwin;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.common.utils.BitmapUtils;
import com.wxws.myticket.common.utils.ToastManager;

import java.io.File;

public class PhotoPopupWindow {
	private PopupWindow popupWindow;
	View view;
	View parent;
	Activity context;
	TextView paizhao, quxiao, xiangce;
	BitmapUtils bitmapUtil;

	public PhotoPopupWindow(Context context, View parent) {
		this.context = (Activity) context;
		this.parent = parent;
		init();
		bitmapUtil = new BitmapUtils(context);
	}

	private void init() {
		// 加载popupWindow的布局文件
		view = LayoutInflater.from(context).inflate(R.layout.popupwindow_photo, null);
		paizhao = (TextView) view.findViewById(R.id.paizhao);
		quxiao = (TextView) view.findViewById(R.id.quxiao);
		xiangce = (TextView) view.findViewById(R.id.xiangce);
		paizhao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
					doTakePhoto();
				}
				else {

				}
			}
		});
		xiangce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				doPickPhotoFromGallery();
			}
		});
		quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		// 声明一个弹出框
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		popupWindow.setFocusable(true);
		// 这个是为了点击“返回Back”也能使其消失
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(Color.argb(90, 0, 0, 0));
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}

	public void show() {
		try {
			popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			popupWindow.setFocusable(true);
		}
		catch (Exception e) {}
	}

	public void dismiss() {
		try {
			popupWindow.setFocusable(false);
			popupWindow.dismiss();
		}
		catch (Exception e) {}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String fileName;
	private static int RESULT_LOAD_PICTURE = 4;
	private static int RESULT_LOAD_IMAGE = 2;

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			File file = new File(bitmapUtil.getSavePath());
			if (!file.exists()) {
				boolean iscreat = file.mkdirs();// 创建照片的存储目录
			}
			fileName = System.currentTimeMillis() + String.valueOf((int) ((Math.random() * 9 + 1) * 1000)) + ".png";
			file = new File(file, fileName);
			final Intent intent = getTakePickIntent(file, fileName);

			context.startActivityForResult(intent, RESULT_LOAD_PICTURE);
		}
		catch (ActivityNotFoundException e) {
			ToastManager.getInstance().showToast(context,"拍照失败");
		}
	}

	public static Intent getTakePickIntent(File f, String fileName) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

		return intent;
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			final Intent intent = getPhotoPickIntent();
			context.startActivityForResult(intent, RESULT_LOAD_IMAGE);
		}
		catch (ActivityNotFoundException e) {

		}
	}

	// 封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent=new Intent(Intent.ACTION_PICK);
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		return intent;
	}
}
