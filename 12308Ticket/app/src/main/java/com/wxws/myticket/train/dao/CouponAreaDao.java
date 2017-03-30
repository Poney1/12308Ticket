package com.wxws.myticket.train.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.wxws.myticket.train.entity.CouponAreaModel;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * desc:
 * Date: 2016/8/16 17:00
 *
 * @auther: lixiangxiang
 */
public class CouponAreaDao  extends AbstractDao<CouponAreaModel, Long> {

    public static final String TABLENAME = "COUPONAREA";

    public CouponAreaDao(DaoConfig config) {
        super(config);
    }

    public CouponAreaDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected CouponAreaModel readEntity(Cursor cursor, int i) {

        CouponAreaModel coupon = new CouponAreaModel(
                cursor.isNull(i + 0) ? null : cursor.getLong(i + 0),
                cursor.isNull(i + 1) ? null : cursor.getString(i + 1),
                cursor.isNull(i + 2) ? null : cursor.getString(i + 2),
                cursor.isNull(i + 3) ? null : cursor.getString(i + 3),
                cursor.isNull(i + 4) ? null : cursor.getString(i + 4),
                cursor.isNull(i + 5) ? null : cursor.getString(i + 5),
                cursor.isNull(i + 6) ? null : cursor.getString(i + 6),
                cursor.isNull(i + 7) ? null : cursor.getInt(i + 7));

        return coupon;
    }

    @Override
    protected Long readKey(Cursor cursor, int i) {
        return null;
    }

    @Override
    protected void readEntity(Cursor cursor, CouponAreaModel couponAreaModel, int i) {
        couponAreaModel.setId(cursor.isNull(i+0) ? null :cursor.getLong(i + 0));
        couponAreaModel.setCode(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        couponAreaModel.setName(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        couponAreaModel.setSimplePinyin(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        couponAreaModel.setPinyin(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        couponAreaModel.setFirstChar(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        couponAreaModel.setVersion(cursor.isNull(i+6) ? null :cursor.getString(i + 6));
        couponAreaModel.setStatus(cursor.isNull(7) ? null : cursor.getInt(i+7));
    }

    @Override
    protected void bindValues(SQLiteStatement sqLiteStatement, CouponAreaModel couponAreaModel) {

        sqLiteStatement.clearBindings();

        Long id = couponAreaModel.getId();
        if (id != null) {
            sqLiteStatement.bindLong(1, id);
        }

        String code = couponAreaModel.getCode();
        if (code != null) {
            sqLiteStatement.bindString(2, code);
        }


        String name = couponAreaModel.getName();
        if (name != null) {
            sqLiteStatement.bindString(3, name);
        }

        String simplePinyin = couponAreaModel.getSimplePinyin();
        if (simplePinyin != null) {
            sqLiteStatement.bindString(4, simplePinyin);
        }

        String pinyin = couponAreaModel.getPinyin();
        if (pinyin != null) {
            sqLiteStatement.bindString(5, pinyin);
        }

        String firstChar = couponAreaModel.getFirstChar();
        if (firstChar != null) {
            sqLiteStatement.bindString(6, firstChar);
        }

        String version = couponAreaModel.getVersion();

        if (version != null) {
            sqLiteStatement.bindString(7, version);
        }

        int status = couponAreaModel.getStatus();
        if (status != 0) {
            sqLiteStatement.bindLong(8, status);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(CouponAreaModel couponAreaModel, long l) {

        couponAreaModel.setId(l);

        return couponAreaModel.getId();
    }

    @Override
    protected Long getKey(CouponAreaModel couponAreaModel) {
        if (couponAreaModel != null) {
            return couponAreaModel.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    public static class Properties{
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Code = new Property(1, String.class, "code", true, "CODE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property SimplePinYin = new Property(3, String.class, "simplepinyin", false, "SIMPLEPINYIN");
        public final static Property Pinyin = new Property(4, String.class, "pinyin", false, "PINYIN");
        public final static Property firstChar = new Property(5, String.class, "firstchar", false, "FIRSTCHAR");
        public final static Property Status = new Property(6, String.class, "status", false, "STATUS");
        public final static Property version = new Property(7, Integer.class, "version", false, "VERSION");
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {

        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'COUPONAREA' (" + //
                "'ID' INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + // 0: id
                "'CODE' TEXT," + // 1:  Code
                "'NAME' TEXT," + // 2: Name
                "'SIMPLEPINYIN' TEXT," + // 3: SimplePinyin
                "'PINYIN' TEXT," + // 4:  Pinyin
                "'FIRSTCHAR' TEXT," + // 5: FIRSTCHAR
                "'STATUS' TEXT," + // 6: Status
                "'VERSION' INTEGER);" );  // 7: version

        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_COUPONAREA_NAME_PINYIN_SIMPLEPINYIN ON COUPONAREA" +
                " (NAME,PINYIN,SIMPLEPINYIN);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'COUPONAREA'";
        db.execSQL(sql);
    }
}
