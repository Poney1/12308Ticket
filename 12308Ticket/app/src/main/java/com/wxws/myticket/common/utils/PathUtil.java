package com.wxws.myticket.common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.wxws.myticket.common.constants.ConfigConstants;

import java.io.File;
import java.lang.reflect.Method;

public class PathUtil {

	/**
	 * 文件 目录
	 */
	public static String FILE_ROOT_DIRECTORY = Environment.getExternalStorageState();

	//SD卡默认50M
	public static final int SDCARD_MEMORY = 50;

	//  SD卡是否可以存储
	public static boolean SDCARD_CAN_SAVE = false;

	// 安装包目录
	public static String INSTALL_DIRECTORY;
	//上传目录
	public static String UPLOAD_DIRECTORY;

	/**
	 * 获取内置存储卡 或者 外置存储卡的位置，如果有多张存储卡，就获取外置存储卡的位置
	 * @return
	 */
	public static String getExternalStorageDirectory(Activity activity) {
		// 使用2.2 ，2.3的版本 用于获取内置存储卡路径
		String outStoragePath = null;
		
		try {
			// 使用4.0以后的版本 ，  反射获取多个存储卡的路径，返回外资存储卡的路径
			StorageManager storageManager = (StorageManager) activity.getSystemService(Context.STORAGE_SERVICE);
		    Class<?>[] paramClasses = {};
		    Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
		    getVolumePathsMethod.setAccessible(true);  
		    Object[] params = {};
		    Object invoke = getVolumePathsMethod.invoke(storageManager, params);
		    if(((String[])invoke).length > 1){
		    	for (int i = 0; i < ((String[])invoke).length; i++) {
			        outStoragePath = ((String[])invoke)[i];
			    }
		    } else {
		    	outStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		    }
		} catch (Exception e) {
			outStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return outStoragePath;
	}


	/**
	 * 创建文件目录
	 * @param context Context
	 */
	public static void createDirectory(Context context) {
		long space = checkFreeDiskSpace();
		// 判断SD卡空间若大于50Mb，则视为可以存储
		SDCARD_CAN_SAVE = space > SDCARD_MEMORY;
		if (SDCARD_CAN_SAVE) {
			// 配置文件目录放置在SD卡根目录
			FILE_ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
		} else {
			// 内存中创建目录
			FILE_ROOT_DIRECTORY = context.getFilesDir().getPath();
		}
		// 安装包
		INSTALL_DIRECTORY = FILE_ROOT_DIRECTORY + "/patch/";
		createDirectory(INSTALL_DIRECTORY);
		// 上传
		UPLOAD_DIRECTORY = FILE_ROOT_DIRECTORY + "/upload/";
		createDirectory(UPLOAD_DIRECTORY);
	}

	/**
	 * 计算SDCARD的剩余空间
	 * @return 返回空间大小
	 */
	@SuppressWarnings("deprecation")
	public static long checkFreeDiskSpace() {
		long freeSpace = 0l;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
				if(null != statFs) {
					long blockSize = statFs.getBlockSize();
					long availableBlocks = statFs.getAvailableBlocks();
					freeSpace = availableBlocks * blockSize / 1024 / 1024;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return freeSpace;
	}

	/**
	 * 创建文件夹目录
	 * @param directory 文件夹路径
	 */
	public static void createDirectory(String directory) {
		try {
			if(!TextUtils.isEmpty(directory)) {
				File file = new File(directory);
				if (!file.exists()) {
					file.mkdirs();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
