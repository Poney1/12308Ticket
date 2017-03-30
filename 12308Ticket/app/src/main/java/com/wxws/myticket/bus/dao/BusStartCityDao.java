package com.wxws.myticket.bus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * desc: 汽车票出发城市
 * Date: 2016/10/31 18:25
 *
 * @auther: lixiangxiang
 */
public class BusStartCityDao  extends AbstractDao<BusStartCity,String> {

    public static final String TABLENAME ="BUSSTARTCITY";

    public static  class Properties{
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Pinyin = new Property(2, String.class, "pinyin", false, "PINYIN");
        public final static Property SimplePinyin = new Property(3, String.class, "simplePinyin", false, "SIMPLEPINYIN");
        public final static Property IsHot = new Property(4, String.class, "isHot", false, "ISHOT");
        public final static Property Status = new Property(5, String.class, "status", false, "STATUS");
        public final static Property Version = new Property(6, Integer.class, "version", false, "VERSION");
        public final static Property FirstChar = new Property(7, String.class, "firstchar", false, "FIRSTCHAR");
    }

    public BusStartCityDao(DaoConfig config) {
        super(config);
    }

    public BusStartCityDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {

        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'BUSSTARTCITY' (" + //
                "'ID' TEXT ," + // 0: id
                "'NAME' TEXT UNIQUE," + // 1: Name
                "'PINYIN' TEXT," + // 2: Pinyin
                "'SIMPLEPINYIN' TEXT," + // 3: SimplePinyin
                "'ISHOT' TEXT," + // 4: IsHot
                "'STATUS' TEXT," + // 5: Status
                "'VERSION' INTEGER," +  // 6: version
                " 'FIRSTCHAR' TEXT);"); //7: FIRSTCHAR
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BUSSTARTCITY_NAME_PINYIN ON BUSSTARTCITY" +
                " (NAME,PINYIN);");
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BUSSTARTCITY'";
        db.execSQL(sql);
    }

    @Override
    protected BusStartCity readEntity(Cursor cursor, int i) {

        BusStartCity bus = new BusStartCity(
                cursor.isNull(i + 0) ? null : cursor.getString(i + 0),
                cursor.isNull(i + 1) ? null : cursor.getString(i + 1),
                cursor.isNull(i + 2) ? null : cursor.getString(i + 2),
                cursor.isNull(i + 3) ? null : cursor.getString(i + 3),
                cursor.isNull(i + 4) ? null : cursor.getString(i + 4),
                cursor.isNull(i + 5) ? null : cursor.getString(i + 5),
                cursor.isNull(i + 6) ? 0 : cursor.getInt(i + 6),
                cursor.isNull(i + 7) ? null : cursor.getString(i + 7));
        return bus;
    }

    @Override
    protected String readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : cursor.getString(i + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, BusStartCity busStartCity, int i) {
        busStartCity.setId(cursor.isNull(i+0) ? null :cursor.getString(i + 0));
        busStartCity.setName(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        busStartCity.setPinyin(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        busStartCity.setSimplePinyin(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        busStartCity.setIsHot(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        busStartCity.setStatus(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        busStartCity.setVersion(cursor.isNull(i+7) ? null :cursor.getInt(i + 7));
        busStartCity.setFirstChar(cursor.isNull(8) ? null : cursor.getString(i+8));
    }

    @Override
    protected void bindValues(SQLiteStatement sqLiteStatement, BusStartCity busStartCity) {
        sqLiteStatement.clearBindings();

        String id = busStartCity.getId();
        if (id != null) {
            sqLiteStatement.bindString(1, id);
        }

        String name = busStartCity.getName();
        if (name != null) {
            sqLiteStatement.bindString(2, name);
        }


        String pinyin = busStartCity.getPinyin();
        if (pinyin != null) {
            sqLiteStatement.bindString(3, pinyin);
        }

        String simplePinyin = busStartCity.getSimplePinyin();
        if (simplePinyin != null) {
            sqLiteStatement.bindString(4, simplePinyin);
        }

        String isHot = busStartCity.getIsHot();
        if (isHot != null) {
            sqLiteStatement.bindString(5, isHot);
        }

        String status = busStartCity.getStatus();
        if (status != null) {
            sqLiteStatement.bindString(6, status);
        }

        int version = busStartCity.getVersion();
        if (version != 0) {
            sqLiteStatement.bindLong(7, version);
        }

        String firstChar = busStartCity.getFirstChar();
        if (firstChar != null) {
            sqLiteStatement.bindString(8, firstChar);
        }
    }

    @Override
    protected String updateKeyAfterInsert(BusStartCity busStartCity, long l) {
        busStartCity.setId(l+"");

        return busStartCity.getId();

    }

   /* @Override
    protected String updateKeyAfterInsert(BusStartCity busStartCity, String l) {
        busStartCity.setId(l);

        return busStartCity.getId();
    }*/

    @Override
    protected String getKey(BusStartCity busStartCity) {
        if (busStartCity != null) {
            return busStartCity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
