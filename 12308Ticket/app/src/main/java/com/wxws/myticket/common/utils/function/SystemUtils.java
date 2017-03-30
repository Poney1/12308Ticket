package com.wxws.myticket.common.utils.function;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.common.utils.StringUtils;


/**
 * desc:  定位、wifi 等
 * Date: 2016-07-08 14:50
 *
 * @auther: lixiangxiang
 */
public class SystemUtils {

    private static  SystemUtils systemUtils = null;

    public static SystemUtils getInstance(){
        if (systemUtils == null){
            synchronized (SystemUtils.class){
                if (systemUtils == null){
                    systemUtils = new SystemUtils();
                }
            }
        }
        return systemUtils;
    }

    /**
     * 判断是否有wifi
     * @param context
     * @return
     */
    public static boolean wifiConnection(Context context){

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return wifi != null && wifi.isWifiEnabled() && wifi.reconnect();
    }

    /**
     * 地图初始化设置
     * @param mLocationClient
     */
    public  void  InitLocation(LocationClient mLocationClient) {
        LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Battery_Saving;
        String tempcoor = "bd09ll";
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);//
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 获取资源id
     *
     * @param context
     * @param name
     * @param type    资源类型
     * @return
     */
    public static int getResourceId(Context context, String name, String type) {
        Resources themeResources = null;
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            themeResources = pm.getResourcesForApplication(packageName);
            return themeResources.getIdentifier(name, type, packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //手机唯一标识(IMIE)
    public  String getDeviceId(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = TelephonyMgr.getDeviceId();
        return uuid;
    }

    //获取手机 mac 地址 /唯一标识符号
    public  String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String  mac = info.getMacAddress();
        if (StringUtils.isNullOrEmpty(mac)){
            mac =  getDeviceId(AppApplication.getInstance().getApplicationContext());
        }
        return mac;
    }
}
