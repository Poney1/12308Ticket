package com.wxws.myticket.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法通用工具
 * @author lixiangxiang
 */
public class InputMethod {
	
	/**隐藏输入法
	 * @param activity Activity
	 */
	public static void closeInputMethod(Activity activity) {
		try {
			InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if(manager.isActive()) {
				manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**隐藏输入法
	 * @param activity Activity
     * @param myView 控件
	 */
	public static void closeInputMethod(Activity activity, View myView) {
		try {
			InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(myView.getWindowToken(), 0); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**显示输入法
	 * @param activity Activity
	 * @param myView 控件
	 */
	public static void showInputMethod(Activity activity, View myView) {
		try {
			InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.showSoftInput(myView, InputMethodManager.SHOW_FORCED);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
