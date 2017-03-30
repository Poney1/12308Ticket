package com.wxws.myticket.bus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.dao.DaoSession;
import com.wxws.myticket.bus.dao.BusEndCityDao.Properties;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.train.dao.TrainStartCityDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * desc: 出发城市 操作类
 * Date: 2016/10/31 19:02
 *
 * @auther: lixiangxiang
 */
public class BusEndCityHelper {

    private BusEndCityDao busEndCityDao;


    private static class LazyHolder {
        private static final BusEndCityHelper instance = new BusEndCityHelper();
    }


    public static BusEndCityHelper getInstance() {

        return LazyHolder.instance;
    }

    private BusEndCityHelper() {
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        if (daoSession != null) {
            busEndCityDao = daoSession.getBusEndCityDao();
        }
    }

    /**
     * 插入全部数据
     *
     * @param lists
     */
    public void addAllBusCity(List<BusEndCity> lists) {
        if (lists != null && lists.size() > 0) {
            try {
                busEndCityDao.insertOrReplaceInTx(lists);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入 单个城市
     *
     * @param busEndCity
     */
    public void addCity(BusEndCity busEndCity) {

        if (busEndCity != null) {
            try {
                busEndCityDao.insertOrReplaceInTx(busEndCity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否有这个出发城市
     *
     * @param city
     * @return
     */
    public boolean isHaveCity(String city) {
        QueryBuilder<BusEndCity> qb = busEndCityDao.queryBuilder();

        qb.where(Properties.Name.eq(city));

        if (qb.list() != null && qb.list().size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取首字母
     *
     * @return
     */
    public ArrayList<String> getFirstChar() {
        SQLiteDatabase db = AppApplication.getInstance().getDb();
        Cursor cursor = db.query(BusStartCityDao.TABLENAME, new String[]{Properties.FirstChar.columnName}, null, null, null, null, null);
        Set<String> set = new HashSet<>();
        ArrayList<String> firstArray = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String first = cursor.getString(cursor.getColumnIndex(Properties.FirstChar.columnName));
                if (first != null) {
                    set.add(first);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            firstArray.add(iterator.next().toUpperCase());
        }

        Collections.sort(firstArray);

        return firstArray;
    }

    /**
     * 获取热门城市
     *
     * @return
     */
    public List<String> getHotCity() {

        SQLiteDatabase db = AppApplication.getInstance().getDb();

        Cursor cursor = db.query(BusStartCityDao.TABLENAME, new String[]{Properties.Name.columnName}, "ISHOT = " + "0", null, null, null, null);

        List<String> list = new ArrayList<>();

        while (cursor.moveToNext()) {

            String stationName = cursor.getString(cursor.getColumnIndex(Properties.Name.columnName));

            if (StringUtils.isNullOrEmpty(stationName)) {

                list.add(stationName);
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 获取所有 城市
     *
     * @return
     */
    public List<BusEndCity> getAllCity(@NonNull String fromCityId) {
        QueryBuilder<BusEndCity> qb = busEndCityDao.queryBuilder();
        qb.where(Properties.FromCityId.eq(fromCityId));
        qb.orderAsc(Properties.Pinyin);
        List<BusEndCity> list = qb.list();
        return list;

    }

    /**
     * 搜索 城市
     *
     * @param search
     * @return
     */
    public List<BusEndCity> getTrainListByStr(String search) {
        QueryBuilder<BusEndCity> qb = busEndCityDao.queryBuilder();
        qb.where(qb.or(Properties.Name.like(search + "%"), Properties.Pinyin.like(search.toLowerCase() + "%"), Properties.SimplePinyin.like(search.toLowerCase() + "%")));
        qb.orderAsc(Properties.Pinyin);
        return qb.list();
    }

    /**
     * 删除城市
     *
     * @param id
     */
    public void deleteCity(String id) {
        try {
            busEndCityDao.deleteByKey(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除 数据
     */
    public void clear() {
        busEndCityDao.deleteAll();
    }
}
