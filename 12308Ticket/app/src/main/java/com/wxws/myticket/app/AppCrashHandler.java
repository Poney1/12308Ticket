package com.wxws.myticket.app;

import com.wxws.myticket.bus.dao.BusEndCityHelper;
import com.wxws.myticket.bus.dao.BusStartCityHelper;
import com.wxws.myticket.bus.dao.DirectStartCityHelper;
import com.wxws.myticket.bus.dao.MapCityHelper;
import com.wxws.myticket.common.utils.ThreadManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;

/**
 * desc: 异常俘获 ，抛异常的时候清理数据，
 * Date: 2016/12/5 17:38
 *
 * @auther: lixiangxiang
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ThreadManager.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                PreferencesUtils.clearAll(AppApplication.getInstance());
                BusStartCityHelper.getInstance().clear();
                BusEndCityHelper.getInstance().clear();
                DirectStartCityHelper.getInstance().clear();
                MapCityHelper.getInstance().clear();
            }
        });
    }
}
