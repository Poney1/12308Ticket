package com.wxws.myticket.bus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wxws.myticket.common.utils.StringUtils;

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
public class BusEndCityDao extends AbstractDao<BusEndCity,String> {

    public static final String TABLENAME ="BUSENDCITY";

    public static  class Properties{
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Pinyin = new Property(2, String.class, "pinyin", false, "PINYIN");
        public final static Property SimplePinyin = new Property(3, String.class, "simplePinyin", false, "SIMPLEPINYIN");
        public final static Property IsHot = new Property(4, String.class, "isHot", false, "ISHOT");
        public final static Property Status = new Property(5, String.class, "status", false, "STATUS");
        public final static Property Version = new Property(6, Integer.class, "version", false, "VERSION");
        public final static Property FirstChar = new Property(7, String.class, "firstchar", false, "FIRSTCHAR");
        public final static Property FromCityId = new Property(8, String.class, "fromCityId", false, "FROMCITYID");
    }

    public BusEndCityDao(DaoConfig config) {
        super(config);
    }

    public BusEndCityDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {

        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'BUSENDCITY' (" + //
                "'ID' TEXT ," + // 0: id
                "'NAME' TEXT UNIQUE," + // 1: Name
                "'PINYIN' TEXT," + // 2: Pinyin
                "'SIMPLEPINYIN' TEXT," + // 3: SimplePinyin
                "'ISHOT' TEXT," + // 4: IsHot
                "'STATUS' TEXT," + // 5: Status
                "'VERSION' INTEGER," +  // 6: version
                " 'FIRSTCHAR' TEXT,"+//7: FIRSTCHAR
        "'FROMCITYID' TEXT);");//8 FROMCITYID
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BUSENDCITY_NAME_PINYIN ON BUSENDCITY" +
                " (NAME,PINYIN);");
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BUSENDCITY'";
        db.execSQL(sql);
    }

    @Override
    protected BusEndCity readEntity(Cursor cursor, int i) {

        BusEndCity bus = new BusEndCity(
                cursor.isNull(i + 0) ? null : cursor.getString(i + 0),
                cursor.isNull(i + 1) ? null : cursor.getString(i + 1),
                cursor.isNull(i + 2) ? null : cursor.getString(i + 2),
                cursor.isNull(i + 3) ? null : cursor.getString(i + 3),
                cursor.isNull(i + 4) ? null : cursor.getString(i + 4),
                cursor.isNull(i + 5) ? null : cursor.getString(i + 5),
                cursor.isNull(i + 6) ? null : cursor.getInt(i + 6),
                cursor.isNull(i + 7) ? null : cursor.getString(i + 7),
                cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
        return bus;
    }

    @Override
    protected String readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : cursor.getString(i + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, BusEndCity busEndCity, int i) {
        busEndCity.setId(cursor.isNull(i+0) ? null :cursor.getString(i + 0));
        busEndCity.setName(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        busEndCity.setPinyin(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        busEndCity.setSimplePinyin(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        busEndCity.setIsHot(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        busEndCity.setStatus(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        busEndCity.setVersion(cursor.isNull(i+6) ? null :cursor.getInt(i + 6));
        busEndCity.setFirstChar(cursor.isNull(7) ? null : cursor.getString(i+7));
        busEndCity.setFromCityId(cursor.isNull(8) ? null : cursor.getString(i+8));
    }

    @Override
    protected void bindValues(SQLiteStatement sqLiteStatement, BusEndCity busEndCity) {
        sqLiteStatement.clearBindings();

        String id = busEndCity.getId();
        if (id != null) {
            sqLiteStatement.bindString(1, id);
        }

        String name = busEndCity.getName();
        if (!StringUtils.isNullOrEmpty(name)) {
            sqLiteStatement.bindString(2, name);
        }


        String pinyin = busEndCity.getPinyin();
        if (!StringUtils.isNullOrEmpty(pinyin)) {
            sqLiteStatement.bindString(3, pinyin);
        }

        String simplePinyin = busEndCity.getSimplePinyin();
        if (simplePinyin != null) {
            sqLiteStatement.bindString(4, simplePinyin);
        }

        String isHot = busEndCity.getIsHot();
        if (isHot != null) {
            sqLiteStatement.bindString(5, isHot);
        }

        String status = busEndCity.getStatus();
        if (status != null) {
            sqLiteStatement.bindString(6, status);
        }

        int version = busEndCity.getVersion();
        if (version != 0) {
            sqLiteStatement.bindLong(7, version);
        }

        String firstChar = busEndCity.getFirstChar();
        if (firstChar != null) {
            sqLiteStatement.bindString(8, firstChar);
        }

        String fromCityId = busEndCity.getFromCityId();
        if (fromCityId != null) {
            sqLiteStatement.bindString(9, fromCityId);
        }
    }

    @Override
    protected String updateKeyAfterInsert(BusEndCity busStartCity, long l) {
        busStartCity.setId(l+"");

        return busStartCity.getId();
    }

    @Override
    protected String getKey(BusEndCity busStartCity) {
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
