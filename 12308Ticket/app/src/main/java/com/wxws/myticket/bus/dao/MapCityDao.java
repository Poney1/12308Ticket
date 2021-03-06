package com.wxws.myticket.bus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wxws.myticket.app.base.dao.DaoSession;
import com.wxws.myticket.bus.entity.BaocheMapCity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table MAP_CITY.
*/
public class MapCityDao extends AbstractDao<BaocheMapCity, String> {

    public static final String TABLENAME = "MAP_CITY";

    /**
     * Properties of entity MapCity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Full_pinyin = new Property(2, String.class, "full_pinyin", false, "FULL_PINYIN");
        public final static Property Simple_pinyin = new Property(3, String.class, "simple_pinyin", false, "SIMPLE_PINYIN");
        public final static Property Initial = new Property(4, String.class, "initial", false, "INITIAL");
    }


    public MapCityDao(DaoConfig config) {
        super(config);
    }
    
    public MapCityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MAP_CITY' (" + //
                "'ID' TEXT ," + // 0: id
                "'NAME' TEXT UNIQUE," + // 1: name
                "'FULL_PINYIN' TEXT," + // 2: full_pinyin
                "'SIMPLE_PINYIN' TEXT," + // 3: simple_pinyin
                "'INITIAL' TEXT);"); // 4: initial
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_MAP_CITY_NAME ON MAP_CITY" +
                " (NAME);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MAP_CITY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BaocheMapCity entity) {
        stmt.clearBindings();

        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String full_pinyin = entity.getFull_pinyin();
        if (full_pinyin != null) {
            stmt.bindString(3, full_pinyin);
        }
 
        String simple_pinyin = entity.getSimple_pinyin();
        if (simple_pinyin != null) {
            stmt.bindString(4, simple_pinyin);
        }
 
        String initial = entity.getInitial();
        if (initial != null) {
            stmt.bindString(5, initial);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BaocheMapCity readEntity(Cursor cursor, int offset) {
        BaocheMapCity entity = new BaocheMapCity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // full_pinyin
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // simple_pinyin
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // initial
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BaocheMapCity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFull_pinyin(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSimple_pinyin(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setInitial(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(BaocheMapCity entity, long rowId) {
        entity.setId(rowId +"");
        return entity.getId()  ;
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(BaocheMapCity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
