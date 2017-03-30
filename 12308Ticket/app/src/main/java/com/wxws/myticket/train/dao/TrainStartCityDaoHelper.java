package com.wxws.myticket.train.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.dao.DaoSession;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.train.entity.TrainStartCityModel;
import com.wxws.myticket.train.dao.TrainStartCityDao.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * desc: 火车票出发城市cuad
 * Date: 2016-08-01 15:58
 *
 * @auther: lixiangxiang
 */
public class TrainStartCityDaoHelper {

    private TrainStartCityDao mTrainDao;

    private static TrainStartCityDaoHelper mTrainHelper;

    private TrainStartCityDaoHelper() {
        // 数据库对象
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        if (daoSession != null) {
            mTrainDao = daoSession.getTrainStartCityDao();
        }
    }

    public static TrainStartCityDaoHelper getInstance() {

        if (mTrainHelper == null) {
            synchronized (TrainStartCityDaoHelper.class) {
                if (mTrainHelper == null) {
                    mTrainHelper = new TrainStartCityDaoHelper();
                }
            }
        }
        return mTrainHelper;
    }

    /**
     * 获取首字母
     *
     * @return
     */
    public ArrayList<String> getFirstChar() {
        SQLiteDatabase db = AppApplication.getInstance().getDb();
        Cursor cursor = db.query(TrainStartCityDao.TABLENAME, new String[]{Properties.FirstChar.columnName}, null, null, null, null, null);
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
     * 查询 站点名字
     *
     * @return
     */
    public List<TrainStartCityModel> getStartCityObject() {

        SQLiteDatabase db = AppApplication.getInstance().getDb();

        Cursor cursor = db.query(TrainStartCityDao.TABLENAME, new String[]{Properties.Name.columnName, Properties.IsHot.columnName, Properties.Id.columnName
                , Properties.Code.columnName, Properties.Pinyin.columnName, Properties.SimplePinyin.columnName, Properties.Status.columnName, Properties.version.columnName,Properties.FirstChar.columnName}, null, null, null, null, null);

        List<TrainStartCityModel> list = new ArrayList<>();

        while (cursor.moveToNext()) {

            TrainStartCityModel train = new TrainStartCityModel();
            String stationName = cursor.getString(cursor.getColumnIndex(Properties.Name.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Name.columnName));
            String isHot = cursor.getString(cursor.getColumnIndex(Properties.IsHot.columnName)) == null ? "1" : cursor.getString(cursor.getColumnIndex(Properties.IsHot.columnName));
            long id = cursor.getLong(cursor.getColumnIndex(Properties.Id.columnName));
            String code = cursor.getString(cursor.getColumnIndex(Properties.Code.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Code.columnName));
            String pinyin = cursor.getString(cursor.getColumnIndex(Properties.Pinyin.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Pinyin.columnName));
            String simplePinyin = cursor.getString(cursor.getColumnIndex(Properties.SimplePinyin.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.SimplePinyin.columnName));
            String status = cursor.getString(cursor.getColumnIndex(Properties.Status.columnName)) == null ? "" : cursor.getString(cursor.getColumnIndex(Properties.Status.columnName));
            int version = cursor.getInt(cursor.getColumnIndex(Properties.version.columnName));
            String firstChar = cursor.getString(cursor.getColumnIndex(Properties.FirstChar.columnName))==null?"": cursor.getString(cursor.getColumnIndex(Properties.FirstChar.columnName));

            train.setId(id);
            train.setVersion(version);
            train.setStatus(status);
            train.setIsHot(isHot);
            train.setCode(code);
            train.setName(stationName);
            train.setPinyin(pinyin);
            train.setSimplePinyin(simplePinyin);
            train.setFirstChar(firstChar);

            list.add(train);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取热门城市
     *
     * @return
     */
    public List<String> getHotCity() {

        SQLiteDatabase db = AppApplication.getInstance().getDb();

        Cursor cursor = db.query(TrainStartCityDao.TABLENAME, new String[]{TrainStartCityDao.Properties.Name.columnName}, "ISHOT = " + "0", null, null, null, null);

        List<String> list = new ArrayList<>();

        while (cursor.moveToNext()) {

            String stationName = cursor.getString(cursor.getColumnIndex(TrainStartCityDao.Properties.Name.columnName));

            if (StringUtils.isNullOrEmpty(stationName)) {

                list.add(stationName);
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 插入 数据
     *
     * @param list
     */
    public void addCity(List<TrainStartCityModel> list) {

        if (list != null && list.size() > 0) {
            try {
                mTrainDao.insertOrReplaceInTx(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 插入单个城市
     * @param trainStartCityModel
     */
    public void  addSingleCity(TrainStartCityModel trainStartCityModel){
        if (trainStartCityModel!=null){
            try {
                mTrainDao.insertOrReplaceInTx(trainStartCityModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 搜索 火车 票城市
     *
     * @param search
     * @return
     */
    public List<TrainStartCityModel> getTrainListByStr(String search) {
        QueryBuilder<TrainStartCityModel> qb = mTrainDao.queryBuilder();
        qb.where(qb.or(Properties.Name.like(search + "%"), Properties.Pinyin.like(search.toLowerCase() + "%"), Properties.SimplePinyin.like(search.toLowerCase() + "%")));
        qb.orderAsc(Properties.Pinyin);
        return qb.list();
    }

    /**
     * 通过城市获取code
     * @param city
     * @return
     */
    public String getCodeThroughCity(String city){
        SQLiteDatabase db = AppApplication.getInstance().getDb();
        String code ="" ;
       try {
           String.format("NAME = %s",city);
           Cursor cursor = db.query(TrainStartCityDao.TABLENAME, new String[]{Properties.Code.columnName},"NAME = '" +city+"'", null, null, null, null);
           while (cursor.moveToNext()){
               code = cursor.getString(cursor.getColumnIndex(Properties.Code.columnName));
           }
           cursor.close();
       }catch (Exception e){
           e.printStackTrace();
       }
        return code ;
    }
    /**
     * 清除 数据
     */
    public void clear() {
        mTrainDao.deleteAll();
    }
}
