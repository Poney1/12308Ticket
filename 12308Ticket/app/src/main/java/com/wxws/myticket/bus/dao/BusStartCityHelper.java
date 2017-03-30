package com.wxws.myticket.bus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.base.dao.DaoSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.dao.query.QueryBuilder;
import com.wxws.myticket.bus.dao.BusStartCityDao.Properties;
import com.wxws.myticket.common.utils.StringUtils;

/**
 * desc: 出发城市 操作类
 * Date: 2016/10/31 19:02
 *
 * @auther: lixiangxiang
 */
public class BusStartCityHelper  {

    private  BusStartCityDao busStartCityDao;


    private static class  LazyHolder{
        private static final BusStartCityHelper  instance = new BusStartCityHelper();
    }


    public static BusStartCityHelper  getInstance(){

        return  LazyHolder.instance;
    }

    private BusStartCityHelper(){
        DaoSession  daoSession = AppApplication.getInstance().getDaoSession();
        if (daoSession!=null){
            busStartCityDao = daoSession.getBusStartCityDao();
        }
    }

    /**
     * 插入全部数据
     * @param lists
     */
    public void addAllBusCity(List<BusStartCity> lists){
      if (lists!=null && lists.size() >0){
          try {
              busStartCityDao.insertOrReplaceInTx(lists);
          }catch (Exception e){
             e.printStackTrace();
          }
      }
    }

    /**
     * 插入 单个城市
     *
     * @param busStartCity
     */
    public void addCity(BusStartCity busStartCity) {
        if (busStartCity != null) {
            try {
                busStartCityDao.insertOrReplaceInTx(busStartCity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否有这个出发城市
     * @param city
     * @return
     */
    public boolean  isHaveCity(String city){
        QueryBuilder<BusStartCity> qb = busStartCityDao.queryBuilder();

        qb.where(Properties.Name.eq(city));

        if (qb.list()!=null && qb.list().size()>0){
            return  true;
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

            String stationName = cursor.getString(cursor.getColumnIndex(BusStartCityDao.Properties.Name.columnName));

            if (StringUtils.isNullOrEmpty(stationName)) {

                list.add(stationName);
            }
        }
        cursor.close();
        return list;
    }

    /**
     *  获取所有 城市
     * @return
     */
    public  List<BusStartCity> getAllCity(){
        ArrayList<BusStartCity> list = new ArrayList<>();
        try {
            SQLiteDatabase db = AppApplication.getInstance().getDb();
            Cursor cursor = db.query(BusStartCityDao.TABLENAME, new String[]{Properties.Id.columnName, Properties.Name.columnName, Properties.Pinyin.columnName, Properties.SimplePinyin.columnName,
                            Properties.IsHot.columnName,Properties.Status.columnName,Properties.Version.columnName,Properties.FirstChar.columnName,}, null, null,null ,
                    null, null);

            while (cursor.moveToNext()) {
                BusStartCity c = new BusStartCity(cursor.getString(cursor.getColumnIndex(Properties.Id.columnName)),cursor.getString(cursor.getColumnIndex(Properties.Name.columnName)),
                        cursor.getString(cursor.getColumnIndex(Properties.Pinyin.columnName)),cursor.getString(cursor.getColumnIndex(Properties.SimplePinyin.columnName)),cursor.getString(cursor.getColumnIndex(Properties.IsHot.columnName)),
                        cursor.getString(cursor.getColumnIndex(Properties.Status.columnName)),cursor.getInt(cursor.getColumnIndex(Properties.Version.columnName)),cursor.getString(cursor.getColumnIndex(Properties.FirstChar.columnName)));
                list.add(c);
            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 搜索 城市
     *
     * @param search
     * @return
     */
    public List<BusStartCity> getTrainListByStr(String search) {
        QueryBuilder<BusStartCity> qb = busStartCityDao.queryBuilder();
        qb.where(qb.or(Properties.Name.like(search + "%"), Properties.Pinyin.like(search.toLowerCase() + "%"), Properties.SimplePinyin.like(search.toLowerCase() + "%")));
        qb.orderAsc(Properties.Pinyin);
        return qb.list();
    }

    /**
     * 删除城市
     * @param id
     */
    public void deleteCity(String id){
        try {
            busStartCityDao.deleteByKey(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 清除 数据
     */
    public void clear() {
        busStartCityDao.deleteAll();
    }
}
