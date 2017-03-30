package com.wxws.myticket.app.base.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wxws.myticket.bus.dao.BusEndCityDao;
import com.wxws.myticket.bus.dao.BusStartCityDao;
import com.wxws.myticket.bus.dao.DirectStartCityDao;
import com.wxws.myticket.bus.dao.MapCityDao;
import com.wxws.myticket.train.dao.CouponAreaDao;
import com.wxws.myticket.train.dao.TrainStartCityDao;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

/**
 * desc: 数据库
 * Date: 2016/10/20 11:06
 *
 * @auther: lixiangxiang
 */
public class DaoMaster  extends AbstractDaoMaster {

    private final static int SCHEMA_VERSION = 7;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        BusStartCityDao.createTable(db,ifNotExists);
        BusEndCityDao.createTable(db,ifNotExists);
        DirectStartCityDao.createTable(db,ifNotExists);
        CouponAreaDao.createTable(db,ifNotExists);
        TrainStartCityDao.createTable(db,ifNotExists);
        MapCityDao.createTable(db,ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        BusStartCityDao.dropTable(db,ifExists);
        BusEndCityDao.dropTable(db,ifExists);
        DirectStartCityDao.dropTable(db,ifExists);
        CouponAreaDao.dropTable(db,ifExists);
        TrainStartCityDao.dropTable(db,ifExists);
        MapCityDao.dropTable(db,ifExists);
    }

    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db,SCHEMA_VERSION);
        registerDaoClass(BusStartCityDao.class);
        registerDaoClass(BusEndCityDao.class);
        registerDaoClass(DirectStartCityDao.class);
        registerDaoClass(CouponAreaDao.class);
        registerDaoClass(TrainStartCityDao.class);
        registerDaoClass(MapCityDao.class);
    }

    public DaoMaster(SQLiteDatabase db, int schemaVersion) {
        super(db, schemaVersion);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType identityScopeType) {
        return new DaoSession(db, identityScopeType, daoConfigMap);
    }
}
