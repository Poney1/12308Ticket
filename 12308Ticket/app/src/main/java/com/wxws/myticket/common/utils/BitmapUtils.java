package com.wxws.myticket.common.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {
	private Context context;
	public BitmapUtils(Context context) {
		this.context = context;
	}
	// 创建本地图
	public  void creatLocalFile(final Bitmap bm, final String iconPath) {

		        if(bm == null){
		        }
				FileOutputStream m_fileOutPutStream = null;
				File cache = new File(getSavePath());
				if (!cache.exists()) {
					cache.mkdirs();
				}
				try {
					m_fileOutPutStream = new FileOutputStream(iconPath);// 写入的文件路径
				} catch (FileNotFoundException e) {
				}
				Log.i("开始写图片", "----------->");
				bm.compress(CompressFormat.JPEG, 100, m_fileOutPutStream);
				Log.i("结束图片", "----------->");
				try {
					m_fileOutPutStream.flush();
					m_fileOutPutStream.close();
				} catch (IOException e) {
					
				}

	
	}

    //得到放缩后的图片
	public synchronized Bitmap getBitmap(String path, int newWidth,
										 int newHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true; // 只得到图片边框信息。
		BitmapFactory.decodeFile(path, opts);
		if (opts.outHeight > newHeight || opts.outWidth > newWidth) {
			int insamplesizeH = opts.inSampleSize = opts.outHeight / newHeight; // 压缩比的设置高压缩
			int insamplesizeW = opts.inSampleSize = opts.outWidth / newWidth; // 压缩比的设置宽压缩
			opts.inSampleSize = opts.outHeight / newHeight; // 压缩比的设置高压缩

			if (insamplesizeW > insamplesizeH) {
				opts.inSampleSize = opts.outWidth / newWidth; // 压缩比的设置宽压缩
			}
		} else {
			opts.inSampleSize = opts.outHeight / newHeight; // 压缩比的设置 高压缩 溢出问题
		}
		// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inJustDecodeBounds = false;
		try {
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			Bitmap bmp = BitmapFactory.decodeFile(path, opts);
			return bmp;
		} catch (OutOfMemoryError err) {

		}
		return null;
	}
	
	public Bitmap creatBitmap(Bitmap bitmapImage, int w, int h, Matrix mx) {

		double s = 0.999999; // 默认按图片正常大小显示
		w = bitmapImage.getWidth();
		h = bitmapImage.getHeight();
		double temp1 = 0;// 图片压缩比
		double temp2 = 0;// 图片压缩比
		if (w > 1280) {
			temp1 = 1280.0 / w;
		}
		if (h > 752) {
			temp2 = 752.0 / h;
		}
		if (temp1 > 0 && temp2 > 0) {
			if (temp1 > temp2) {
				s = temp2;
			} else {
				s = temp1;
			}
		} else if (temp1 > 0 && temp2 == 0) {
			s = temp1;
		} else if (temp2 > 0 && temp1 == 0) {
			s = temp2;
		}
		Log.v("ViewImage", "---------------->>>s = " + s);
		float sw = 1, sh = 1;
		sw = (float) (sw * s);
		sh = (float) (sh * s);
		mx = new Matrix();
		mx.postScale(sw, sh);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmapImage, 0, 0, w, h, mx, true);
		return resizeBmp;
	}
	
	
	String savePath;
	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	public String getSavePath() {
		if(isHasSdcard()){
			savePath = Environment.getExternalStorageDirectory().getPath()+"/ticketImg/";
        }else{
        	savePath = context.getFilesDir().toString()+"/ticketImg/";
        }
		return savePath;
	}
	/**
	 * 检测是否有SD卡
	 * 
	 * @return
	 */
	public boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}
	
	
	
}
