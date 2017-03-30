package com.wxws.myticket.bus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.dao.DaoSession;
import com.wxws.myticket.bus.entity.BaocheMapCity;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 操作mapcity表
 */
public class MapCityHelper {
    private MapCityDao mMapCityDao;
    private static class LazyHolder {
        private static final MapCityHelper instance = new MapCityHelper();
    }

    public static MapCityHelper getInstance() {
        return LazyHolder.instance;
    }

    private MapCityHelper() {
        // 数据库对象
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        if (daoSession != null) {
            mMapCityDao = daoSession.getMapCityDao();
        }
    }

    /**
     * 加载全部数据
     *
     * @return
     */
    public List<BaocheMapCity> getList() {
        try {
            return mMapCityDao.loadAll();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 插入全部数据
     *
     * @param list
     */
    public void addAll(List<BaocheMapCity> list) {
        if (list != null && list.size() > 0) {
            try {
                mMapCityDao.insertOrReplaceInTx(list);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 搜索城市
     *
     * @param search
     * @return
     */
    public List<BaocheMapCity> getListByStr(String search) {
        QueryBuilder<BaocheMapCity> qb = mMapCityDao.queryBuilder();
        qb.where(qb.or(MapCityDao.Properties.Name.like(search + "%"), MapCityDao.Properties.Full_pinyin.like(search.toLowerCase() + "%"), MapCityDao.Properties.Simple_pinyin.like(search.toLowerCase() + "%")));
        return qb.list();
    }


    /**
     * 获取城市首字母列表
     *
     * @return
     */
    public ArrayList<String> getShouPinList() {
        try {
            SQLiteDatabase db = AppApplication.getInstance().getDb();
            Cursor cursor = db.query(MapCityDao.TABLENAME, new String[]{MapCityDao.Properties.Initial.columnName}, null, null, MapCityDao.Properties.Initial.columnName,
                    null, MapCityDao.Properties.Initial.columnName);
            ArrayList<String> list = new ArrayList<String>();
            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndex(MapCityDao.Properties.Initial.columnName));
                if (!StringUtils.isNullOrEmpty(word)) {
                    list.add(word);
                }
            }
            cursor.close();
            return list;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 清空数据
     *
     * @param
     */
    public void clear() {
        if (mMapCityDao != null) {
            mMapCityDao.deleteAll();
        }
    }

    /**
     * 根据城市名称获取城市(模糊查询)
     *
     * @return
     */
    public BaocheMapCity getModelByCity(String city) {
        if (StringUtils.isNullOrEmpty(city)) {
            return null;
        }
        QueryBuilder<BaocheMapCity> qb = mMapCityDao.queryBuilder();
        qb.where(MapCityDao.Properties.Name.like(city + "%"));
        if (qb.list().size() > 0) {
            return qb.list().get(0);
        }
        return null;
    }

    /**
     * 根据城市ID获取城市
     *
     * @param cityid
     * @return
     */
    public BaocheMapCity getModelByCityId(long cityid) {
        QueryBuilder<BaocheMapCity> qb = mMapCityDao.queryBuilder();
        qb.where(MapCityDao.Properties.Id.eq(cityid));
        if (qb.list().size() > 0) {
            return qb.list().get(0);
        }
        return null;
    }

    /**
     * 根据城市名称获取城市(精准查询)
     *
     * @param name
     * @return
     */
    public BaocheMapCity getModelByCityName(String name) {
        QueryBuilder<BaocheMapCity> qb = mMapCityDao.queryBuilder();
        qb.where(MapCityDao.Properties.Name.eq(name));
        List<BaocheMapCity> list = qb.list();
        if (list != null && list.size() > 0) {
            return qb.list().get(0);
        }
        return null;
    }
}
