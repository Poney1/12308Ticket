package com.wxws.myticket.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.dao.DaoMaster;
import com.wxws.myticket.app.base.dao.DaoSession;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.utils.PathUtil;
import com.wxws.myticket.pay.PayMethod;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * desc: 应用配置
 * Date: 2016/10/9 10:26
 *
 * @auther: lixiangxiang
 */
public class AppApplication extends Application {

    private static AppApplication myApplication;
    private IWXAPI iwxapi;
    //数据库相关
    public DaoMaster.DevOpenHelper helper;
    public SQLiteDatabase db;
    public DaoMaster daoMaster;
    public DaoSession daoSession;

    public String appointEndCity ="城市";//约租车目的城市
    public boolean isShowUpdate = true;//是否弹出升级
    public  boolean isAlertAD = true;//是否弹出广告

    public void setAppointEndCity(String appointEndCity) {
        this.appointEndCity = appointEndCity;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        Fresco.initialize(this);

        PathUtil.createDirectory(this);//创建目录

        registerWeChat();

        initDownLoad();

        initImageLoader(this);

        setupDatabase();

        initCrash();//初始化异常俘获
    }

    private void initCrash() {
//        AppCrashHandler crashHandler = new AppCrashHandler();
//        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    //注册微信
    private void registerWeChat() {
        iwxapi = WXAPIFactory.createWXAPI(this, PayMethod.WECHAT_APPID, false);
        // // 将该app注册到微信
        iwxapi.registerApp(PayMethod.WECHAT_APPID);
    }

    //支持文件断点续传
    private void initDownLoad() {
        FileDownloader.init(getApplicationContext(),
                new FileDownloadHelper.OkHttpClientCustomMaker() {
                    @Override
                    public OkHttpClient customMake() {
                        // just for OkHttpClient customize.
                        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                        // you can set the connection timeout.
                        builder.connectTimeout(15_000, TimeUnit.MILLISECONDS);
                        // you can set the HTTP proxy.
                        builder.proxy(Proxy.NO_PROXY);
                        // etc.
                        return builder.build();
                    }
                });
    }

    // 图片加载类的初始化
    public static void initImageLoader(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(getOptions(R.mipmap.ic_default_baner, 0))
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache((int) (maxMemory / 8)))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(new BaseImageDownloader(context, 30 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取图片配置参数
     *
     * @param imageRes 默认图片
     * @return
     */
    public static DisplayImageOptions getOptions(int imageRes, int round) {
        BitmapFactory.Options resizeOptions = new BitmapFactory.Options();//图片压缩
        resizeOptions.inSampleSize = 5; // decrease size 5 times
        resizeOptions.inScaled = true;
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示 EXACTLY_STRETCHED:图片会缩放到目标大小完全
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .showImageOnLoading(imageRes) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(imageRes)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imageRes)//设置图片加载/解码过程中错误时候显示的图片
                .delayBeforeLoading(500)//延迟加载
                .build();
    }
    /**
     * 获取application 实例
     * @return
     */
    public static AppApplication getInstance(){

        return myApplication;
    }

    /**
     * 注册数据库
     */
    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new DaoMaster.DevOpenHelper(this, ConfigConstants.DATABASE_NAME, null);
        db = helper.getWritableDatabase();
        //当数据库表变更，更改DaoMaster.SCHEMA_VERSION可升级数据库
        //如果本地数据库小于新数据库版本，则更新数据库
       /* if (db.getVersion() < Constants.DATABASE_VERSION) {
            helper.onUpgrade(db, db.getVersion(), Constants.DATABASE_VERSION);
        }*/
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
