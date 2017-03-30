package com.wxws.myticket.common.utils.function;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;

public class ScreenUtil {
	
	/**
	 * 获取屏幕可见范围的宽度 和高度，必须使用 XXView.post(new Runnable(){....}) 才可以获取到真实数据
	 * @param activity
	 * @return
	 */
	public static int[]  getScreenVisiable(Activity activity) {
		 // 获取状况栏高度
		Rect frame = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top; 
		System.out.println(" 状态栏的高度: " + statusBarHeight);
		
		// 获取标题栏高度
		// 获取到的view就是程序不包括标题栏的部分
		int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT) .getTop();
	    int titleBarHeight = contentViewTop - statusBarHeight;  
	    System.out.println(" 标题栏的高度: " + titleBarHeight);
 
	    // 获取屏幕 宽度 和 高度
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight  = dm.heightPixels - statusBarHeight - titleBarHeight;
        System.out.println(" 可用屏幕部分的高度： " + screenWidth + ", " + screenHeight);
        return  new int[]{screenWidth, screenHeight };
	}
	
	
	
	/**
	 * 获取屏幕的宽度 和高度，必须使用 XXView.post(new Runnable(){....}) 才可以获取到真实数据
	 * @param activity
	 * @return
	 */
	public static int[]  getScreenDisplay(Activity activity) {
	    // 获取屏幕 宽度 和 高度
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight  = dm.heightPixels;
        // System.out.println(" 屏幕的高度： " + screenWidth + ", " + screenHeight);
        return  new int[]{screenWidth, screenHeight };
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param context Context
	 * @param dpValue
	 * @return px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param context Context
	 * @param pxValue
	 * @return dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 扩大View的触摸和点击响应范围,最大不超过其父View范围
	 * @param view
	 */
	public static void expandViewTouch(final View view) {
		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);
				bounds.top -= 100;
				bounds.bottom += 100;
				bounds.left -= 100;
				bounds.right += 100;
				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}


}
