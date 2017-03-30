package com.wxws.myticket.common.constants;


import android.os.Environment;

/**
 * desc: app 配置
 * Date: 2016/10/8 18:20
 *
 * @auther: lixiangxiang
 */
public class ConfigConstants {

    public static final String APPLOG = "12308Ticket";//日志打印
    /**
     * //文件根目录
     */
    public  static  String FILE_ROOT_DIRECTORY = Environment.getExternalStorageState();
    /**
     * 密钥
     */
    public static final  String APPKEY = "12308.Com_Password";
    public static final  String SECRITAPPKEY = "488f5eb10bf240a8";

    /**
     * 用户信息
     */
    public final static String USERINFO = "USERINFO";
    /**
     * authToken
     */
    public final static String AUTHTOKEN = "authToken";

    /**
     * 用户登录信息
     */
    public final static String LOGININFO = "LOGININFO";

    /**
     * 版本信息
     */
    public final static String VERSIONINFO = "VERSIONINFO";
    /**
     * 模块信息
     */
    public final static String MODULES = "MODULES";

    /**
     * 数据库名字
     */
    public static final String DATABASE_NAME = "Ticket12308.db";

    /**
     * 火车票账号信息
     */
    public static final String TRAIN_LOGININFO = "TRAIN_LOGININFO";

    /**
     * 包车需要使用
     */
    public static final String LOCATIONINFO = "LOCATIONINFO";
}
