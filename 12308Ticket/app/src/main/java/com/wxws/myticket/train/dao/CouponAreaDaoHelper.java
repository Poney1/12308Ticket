package com.wxws.myticket.train.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.dao.DaoSession;
import com.wxws.myticket.train.dao.CouponAreaDao.Properties;
import com.wxws.myticket.train.entity.CouponAreaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * desc:  操作 优惠区间
 * Date: 2016/8/16 17:51
 *
 * @auther: lixiangxiang
 */
public class CouponAreaDaoHelper {

    private CouponAreaDao couponAreaDao;

    private static CouponAreaDaoHelper couponAreaDaoHelper;

    private CouponAreaDaoHelper() {
        // 数据库对象
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        if (daoSession != null) {
            couponAreaDao = daoSession.getCouponAreaDao();
        }
    }

    public static CouponAreaDaoHelper getInstance() {

        if (couponAreaDaoHelper == null) {
            synchronized (CouponAreaDaoHelper.class) {
                if (couponAreaDaoHelper == null) {
                    couponAreaDaoHelper = new CouponAreaDaoHelper();
                }
            }
        }
        return couponAreaDaoHelper;
    }

    /**
     * 插入 N 个数据
     *
     * @param list
     */
    public void addCity(List<CouponAreaModel> list) {

        if (list != null && list.size() > 0) {
            try {
                couponAreaDao.insertOrReplaceInTx(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加单个数据
     *
     * @param coupon
     */
    public void addSingleCity(CouponAreaModel coupon) {
        try {
            couponAreaDao.insertOrReplaceInTx(coupon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取首字母
     *
     * @return
     */
    public ArrayList<String> getFirstChar() {
        SQLiteDatabase db = AppApplication.getInstance().getDb();
        Cursor cursor = db.query(CouponAreaDao.TABLENAME, new String[]{Properties.firstChar.columnName}, null, null, null, null, null);
        Set<String> set = new HashSet<>();
        ArrayList<String> firstArray = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String first = cursor.getString(cursor.getColumnIndex(Properties.firstChar.columnName));
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
     * 获取 所有的优惠区域
     *
     * @return
     */
    public List<CouponAreaModel> getCouponCity() {

        SQLiteDatabase db = AppApplication.getInstance().getDb();

        Cursor cursor = db.query(CouponAreaDao.TABLENAME, new String[]{Properties.Id.columnName, Properties.Code.columnName, Properties.Name.columnName
                , Properties.SimplePinYin.columnName, Properties.Pinyin.columnName, Properties.firstChar.columnName, Properties.Status.columnName, Properties.version.columnName}, null, null, null, null, null);

        List<CouponAreaModel> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            CouponAreaModel couponAreaModel = new CouponAreaModel();

            long id = cursor.getLong(cursor.getColumnIndex(Properties.Id.columnName));
            String code = cursor.getString(cursor.getColumnIndex(Properties.Code.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Code.columnName));
            String name = cursor.getString(cursor.getColumnIndex(Properties.Name.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Name.columnName));
            String simplePinYin = cursor.getString(cursor.getColumnIndex(Properties.SimplePinYin.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.SimplePinYin.columnName));
            String pinYin = cursor.getString(cursor.getColumnIndex(Properties.Pinyin.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Pinyin.columnName));
            String firstChar = cursor.getString(cursor.getColumnIndex(Properties.firstChar.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.firstChar.columnName));
            int status = cursor.getInt(cursor.getColumnIndex(Properties.Status.columnName));
            String version = cursor.getString(cursor.getColumnIndex(Properties.version.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.version.columnName));

            couponAreaModel.setId(id);
            couponAreaModel.setCode(code);
            couponAreaModel.setFirstChar(firstChar);
            couponAreaModel.setPinyin(pinYin);
            couponAreaModel.setSimplePinyin(simplePinYin);
            couponAreaModel.setVersion(version);
            couponAreaModel.setStatus(status);
            couponAreaModel.setName(name);

            list.add(couponAreaModel);
        }
        return list;
    }


    /**
     * 优惠区间搜索
     *
     * @param search
     * @return
     */
    public List<CouponAreaModel> getCouponListByStr(String search) {
        QueryBuilder<CouponAreaModel> qb = couponAreaDao.queryBuilder();
        qb.where(qb.or(Properties.Name.like(search + "%"), Properties.Pinyin.like(search.toLowerCase() + "%"), Properties.SimplePinYin.like(search.toLowerCase() + "%")));
        qb.orderAsc(Properties.Pinyin);
        return qb.list();
    }

    /**
     * 删除指定 优惠城市
     *
     * @param couponAreaModel
     */
    public void deleteCity(CouponAreaModel couponAreaModel) {

        couponAreaDao.delete(couponAreaModel);
    }

    /**
     * 查询表的数据量
     *
     * @return
     */
    public int getCountTabName() {
        int count = (int) couponAreaDao.count();
        return count;
    }

}
