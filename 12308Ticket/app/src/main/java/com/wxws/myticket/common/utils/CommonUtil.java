package com.wxws.myticket.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class CommonUtil {

    private Context context;
    private String oldString = null;
    public static final String URL = "url";
    public static final String PORT = "port";
    public static final String TIMEOUT = "timeout";
    public static final String STRING = "string";
    public static final String STRING_USERNAME = "username";
    public static final String STRING_PWD = "pwd";

    public CommonUtil(Context context) {
        this.context = context;
    }

    /**
     * 检查服务是否正在运行
     *
     * @param context
     * @param className
     * @return
     */
    @SuppressWarnings("static-access")
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 当前应用是否启动
     *
     * @param context
     * @return
     */
    public static boolean isAppRunning(Context context) {
        String MY_PKG_NAME = null;
        try {
            MY_PKG_NAME = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) && info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isRunning = true;
                // find it, break
                break;
            }
        }
        return isRunning;
    }

    /**
     * 检测sdcard是否存在
     *
     * @return
     */
    public static boolean checkSdCard() {
        boolean bR = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            bR = true;
        }
        return bR;
    }

    /**
     * 剩余sdcard空间大小(剩余空间需要大于5M)
     *
     * @return
     */
    public static boolean readSDCard() {
        boolean bR = true;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            if ((availCount * blockSize / 1024) < 1024 * 5) {
                bR = false;
            }
        }
        return bR;
    }

    /**
     * 剩余sdcard空间大小(剩余空间需要大于5M)
     *
     * @return
     */
    public static boolean readSDCard(long size) {
        boolean bR = true;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            if ((availCount * blockSize / 1024) < size / 1024) {
                bR = false;
            }
        } else {
            bR = false;
        }
        return bR;
    }

    /**
     * 检测输入的字符串
     *
     * @param s         输入的字符串
     * @param checkType 要检测的输入框类型
     * @return
     */
    private boolean inputCheck(String s, String checkType) {

        if (URL.equals(checkType)) {
            return s.matches("|(^[A-Za-z0-9_.]+$)");
        } else if (STRING.equals(checkType)) {
            return s.matches("|(^[A-Za-z0-9_]+$)");
        } else if (PORT.equals(checkType)) {
            return s.matches("|(^[1-9]\\d{0,3}$)|(^[1-5]\\d{4}$)|(^6[0-4]\\d{3}$)|(^65[0-4]\\d{2}$)|(^655[0-2]\\d$)|(^6553[0-5]$)");
        } else if (TIMEOUT.equals(checkType)) {
            return s.matches("|([1-3][0])|[1-2]?[1-9]");
        } else if (STRING_USERNAME.equals(checkType)) {
            return s.matches("[\\w]{4,20}");
        } else if (STRING_PWD.equals(checkType)) {
            return s.matches("[\\w]{6,20}");
        }

        return false;
    }

    /**
     * 获取当前使用的语言,如zh-CN，zh-TW，zh-HK，en-US等
     *
     * @return
     */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
    }

    public static boolean isChinese() {
        return Locale.getDefault().getLanguage().equals("zh");
    }

    /**
     * 根据方向获取图标名
     *
     * @param degree
     * @return
     */
    public static String getCarImgName(double degree) {

        if (degree >= 360) {
            degree = degree - 360;
        }
        int i = ((int) degree / 90) * 4;
        double s = degree % 90;
        if (s == 90) {
            i += 5;
        } else if (s == 0) {
            i += 1;
        } else if (s > 0 && s <= 22.5) {
            i += 2;
        } else if (s > 22.5 && s < 67.5) {
            i += 3;
        } else if (s >= 67.5 && s < 90) {
            i += 4;
        }

        return "C" + i;
    }

    public static int getDirection(double degree) {

        if (degree >= 360) {
            degree = degree - 360;
        }
        int i = ((int) degree / 90) * 4;
        double s = degree % 90;
        if (s == 90) {
            i += 5;
        } else if (s == 0) {
            i += 1;
        } else if (s > 0 && s <= 22.5) {
            i += 2;
        } else if (s > 22.5 && s < 67.5) {
            i += 3;
        } else if (s >= 67.5 && s < 90) {
            i += 4;
        }

        return i;
    }

    /**
     * dip转px
     *
     * @param context  上下文
     * @param dipValue dip值
     * @return 对应的px值
     */
    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return (int) dipValue;
        }
        return (int) (0.5F + dipValue * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int status_bar_height(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbar;
    }

    /**
     * 四舍五入获取小数点后一位的double
     *
     * @param doub 要四舍五入精简长度的double变量
     * @return double
     */
    public static double getDouble(int scale, double doub) {
        BigDecimal bd = new BigDecimal(doub);
        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Activity activity) {
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }

        return 480;
    }

    /**
     * 将文件大小由字节转化为M
     *
     * @param size
     * @return
     */
    public static String sizeToM(long size) {
        if (size / 1024 / 1024 >= 1) {
            return size / 1024 / 1024 + (size / 1024 % 1024 / 1024.0 + "").substring(1, 3) + "MB";
        } else {
            return size / 1024 + (size % 1024 / 1024.0 + "").substring(1, 3) + "KB";
        }
    }

    /**
     * 根据角度旋转图片
     *
     * @param paramBitmap
     * @param paramFloat
     * @return
     */
    public static Bitmap matrixBitmap(Bitmap paramBitmap, float paramFloat) {
        if ((paramFloat < 1.0F) || (paramFloat > 359.0F)) {
            return paramBitmap;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(paramFloat);

        return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
    }

    /**
     * 选择图片并放回Drawable对象
     *
     * @param paramBitmap
     * @param paramFloat
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable getDrawableByMatrix(Bitmap paramBitmap, float paramFloat) {
        return new BitmapDrawable(matrixBitmap(paramBitmap, paramFloat));
    }

    /**
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight += 10;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * bitmap转字节
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        try {
            imageZoom(bmp, 50);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(CompressFormat.PNG, 100, output);
            if (needRecycle) {
                bmp.recycle();
            }
            byte[] result = output.toByteArray();

            output.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 图片压缩方法：（使用compress的方法）
     *
     * @param bitmap  要压缩的图片
     * @param maxSize 压缩后的大小，单位kb
     * @explain 如果bitmap本身的大小小于maxSize，则不作处理
     */
    public static void imageZoom(Bitmap bitmap, double maxSize) {
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 格式、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 获取bitmap大小 是允许最大大小的多少倍
        double i = mid / maxSize;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (i > 1) {
            // 缩放图片 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （保持宽高不变，缩放后也达到了最大占用空间的大小）
            scale(bitmap, bitmap.getWidth() / Math.sqrt(i), bitmap.getHeight() / Math.sqrt(i));
        }
    }

    /***
     * 图片的缩放方法
     *
     * @param src       ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     */
    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        // 记录src的宽高
        float width = src.getWidth();
        float height = src.getHeight();
        // 创建一个matrix容器
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 开始缩放
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static void startBottomInAnimation(View view) {
        TranslateAnimation animation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
        animation.setDuration(600);
        view.setDrawingCacheEnabled(true);
        view.startAnimation(animation);
    }

    public static String meter2Kilometer(double meter) {
        String result = (int) meter + "m";
        if (meter / 1000 < 1) {
            return result;
        }
        if (meter % 1000 == 0) {
            int kilo = (int) meter / 1000;
            result = kilo + "Km";
        } else {
            double kilo = getDouble(2, meter / 1000);
            result = kilo + "Km";
        }
        return result;
    }

    public static String meterTransKilometer(double meter) {
        String result = (int) meter + "";
        if (meter / 1000 < 1) {
            return result;
        }
        if (meter % 1000 == 0) {
            int kilo = (int) meter / 1000;
            result = kilo + "";
        } else {
            double kilo = getDouble(2, meter / 1000);
            result = kilo + "";
        }
        return result;
    }

    /**
     * 根据资源ID获取Drawable对象
     *
     * @param id
     * @return
     */
    public static Drawable getResourceId(Drawable id) {
        id.setBounds(0, 0, id.getMinimumWidth(), id.getMinimumHeight());
        return id;
    }

    /**
     * 判断当前context是否存在
     *
     * @return
     */
    public static boolean isActivity(Activity activity) {
        return activity == null;
    }

    /**
     * 格式化文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
