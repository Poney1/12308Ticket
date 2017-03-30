package com.wxws.myticket.train.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.wxws.myticket.train.entity.TrainStartCityModel;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * desc:
 * Date: 2016-08-01 14:43
 *
 * @auther: lixiangxiang
 */
public class TrainStartCityDao extends AbstractDao<TrainStartCityModel, Long> {

    public static final String TABLENAME = "TRAINCITY";

    public static class Properties {

        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Code = new Property(2, String.class, "code", false, "CODE");
        public final static Property Pinyin = new Property(3, String.class, "pinyin", false, "PINYIN");
        public final static Property SimplePinyin = new Property(4, String.class, "simplePinyin", false, "SIMPLEPINYIN");
        public final static Property IsHot = new Property(5, String.class, "isHot", false, "ISHOT");
        public final static Property Status = new Property(6, String.class, "status", false, "STATUS");
        public final static Property version = new Property(7, Integer.class, "version", false, "VERSION");
        public final static Property FirstChar = new Property(8, String.class, "firstchar", false, "FIRSTCHAR");
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {

        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'TRAINCITY' (" + //
                "'ID' INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "'NAME' TEXT," + // 1: Name
                "'CODE' TEXT," + // 2: Code
                "'PINYIN' TEXT," + // 3: Pinyin
                "'SIMPLEPINYIN' TEXT," + // 4: SimplePinyin
                "'ISHOT' TEXT," + // 5: IsHot
                "'STATUS' TEXT," + // 6: Status
                "'VERSION' INTEGER," +  // 7: version
                " 'FIRSTCHAR' TEXT);"); //8: FIRSTCHAR
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_TRAINCITY_NAME_PINYIN ON TRAINCITY" +
                " (NAME,PINYIN);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TRAINCITY'";
        db.execSQL(sql);
    }

    public TrainStartCityDao(DaoConfig config) {
        super(config);
    }

    public TrainStartCityDao(DaoConfig config, AbstractDaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected TrainStartCityModel readEntity(Cursor cursor, int i) {
        TrainStartCityModel train = new TrainStartCityModel(
                cursor.isNull(i + 0) ? null : cursor.getLong(i + 0),
                cursor.isNull(i + 1) ? null : cursor.getString(i + 1),
                cursor.isNull(i + 2) ? null : cursor.getString(i + 2),
                cursor.isNull(i + 3) ? null : cursor.getString(i + 3),
                cursor.isNull(i + 4) ? null : cursor.getString(i + 4),
                cursor.isNull(i + 5) ? null : cursor.getString(i + 5),
                cursor.isNull(i + 6) ? null : cursor.getString(i + 6),
                cursor.isNull(i + 7) ? null : cursor.getInt(i + 7),
                cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
        return train;
    }


    @Override
    protected Long readKey(Cursor cursor, int i) {
        return cursor.isNull(i + 0) ? null : cursor.getLong(i + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, TrainStartCityModel trainStartCityModel, int i) {
        trainStartCityModel.setId(cursor.isNull(i+0) ? null :cursor.getLong(i + 0));
        trainStartCityModel.setName(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        trainStartCityModel.setCode(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        trainStartCityModel.setPinyin(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        trainStartCityModel.setSimplePinyin(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        trainStartCityModel.setIsHot(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        trainStartCityModel.setStatus(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        trainStartCityModel.setVersion(cursor.isNull(i+7) ? null :cursor.getInt(i + 7));
        trainStartCityModel.setFirstChar(cursor.isNull(8) ? null : cursor.getString(i+8));
    }

    @Override
    protected void bindValues(SQLiteStatement sqLiteStatement, TrainStartCityModel trainStartCityModel) {
        sqLiteStatement.clearBindings();

        Long id = trainStartCityModel.getId();
        if (id != null) {
            sqLiteStatement.bindLong(1, id);
        }

        String name = trainStartCityModel.getName();
        if (name != null) {
            sqLiteStatement.bindString(2, name);
        }

        String code = trainStartCityModel.getCode();
        if (code != null) {
            sqLiteStatement.bindString(3, code);
        }

        String pinyin = trainStartCityModel.getPinyin();
        if (pinyin != null) {
            sqLiteStatement.bindString(4, pinyin);
        }

        String simplePinyin = trainStartCityModel.getSimplePinyin();
        if (simplePinyin != null) {
            sqLiteStatement.bindString(5, simplePinyin);
        }

        String isHot = trainStartCityModel.getIsHot();
        if (isHot != null) {
            sqLiteStatement.bindString(6, isHot);
        }

        String status = trainStartCityModel.getStatus();
        if (status != null) {
            sqLiteStatement.bindString(7, status);
        }

        int version = trainStartCityModel.getVersion();
        if (version != 0) {
            sqLiteStatement.bindLong(8, version);
        }

        String firstChar = trainStartCityModel.getFirstChar();
        if (firstChar != null) {
            sqLiteStatement.bindString(9, firstChar);
        }
    }

    @Override
    protected Long updateKeyAfterInsert(TrainStartCityModel trainStartCityModel, long l) {

        trainStartCityModel.setId(l);

        return trainStartCityModel.getId();
    }

    @Override
    protected Long getKey(TrainStartCityModel trainStartCityModel) {
        if (trainStartCityModel != null) {
            return trainStartCityModel.getId();
        } else {
            return null;
        }

    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
