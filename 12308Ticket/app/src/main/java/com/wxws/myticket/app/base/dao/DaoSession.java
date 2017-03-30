package com.wxws.myticket.app.base.dao;

import android.database.sqlite.SQLiteDatabase;

import com.wxws.myticket.bus.dao.BusEndCity;
import com.wxws.myticket.bus.dao.BusEndCityDao;
import com.wxws.myticket.bus.dao.BusStartCity;
import com.wxws.myticket.bus.dao.BusStartCityDao;
import com.wxws.myticket.bus.dao.DirectStartCityDao;
import com.wxws.myticket.bus.dao.MapCityDao;
import com.wxws.myticket.bus.entity.BaocheMapCity;
import com.wxws.myticket.train.dao.CouponAreaDao;
import com.wxws.myticket.train.dao.TrainStartCityDao;
import com.wxws.myticket.train.entity.CouponAreaModel;
import com.wxws.myticket.train.entity.TrainStartCityModel;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * desc: 数据库管理
 * Date: 2016/10/20 11:10
 *
 * @auther: lixiangxiang
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig busStartCityDaoConfig;
    private final BusStartCityDao busStartCityDao;

    private final DaoConfig busEndCityDaoConfig;
    private final BusEndCityDao busEndCityDao;

    private final DaoConfig directCityDaoConfig;
    private final DirectStartCityDao directCityDao;


    private final TrainStartCityDao mTrainStartCityDao;
    private final DaoConfig trainStartCityDaoConfig;

    private final DaoConfig couponAreaDaoConfig;
    private final CouponAreaDao couponAreaDao;

    private final DaoConfig mapCityDaoConfig;
    private final MapCityDao mapCityDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);
        busStartCityDaoConfig = daoConfigMap.get(BusStartCityDao.class).clone();
        busStartCityDaoConfig.initIdentityScope(type);
        busStartCityDao = new BusStartCityDao(busStartCityDaoConfig,this);
        registerDao(BusStartCity.class, busStartCityDao);

        busEndCityDaoConfig = daoConfigMap.get(BusEndCityDao.class).clone();
        busEndCityDaoConfig.initIdentityScope(type);
        busEndCityDao = new BusEndCityDao(busEndCityDaoConfig,this);
        registerDao(BusEndCity.class,busEndCityDao);

        directCityDaoConfig = daoConfigMap.get(BusStartCityDao.class).clone();
        directCityDaoConfig.initIdentityScope(type);
        directCityDao = new DirectStartCityDao(directCityDaoConfig,this);
        registerDao(BusStartCity.class,directCityDao);

        trainStartCityDaoConfig = daoConfigMap.get(TrainStartCityDao.class).clone();
        trainStartCityDaoConfig.initIdentityScope(type);
        mTrainStartCityDao = new TrainStartCityDao(trainStartCityDaoConfig,this);
        registerDao(TrainStartCityModel.class, mTrainStartCityDao);

        couponAreaDaoConfig = daoConfigMap.get(CouponAreaDao.class).clone();
        couponAreaDaoConfig.initIdentityScope(type);
        couponAreaDao = new CouponAreaDao(couponAreaDaoConfig,this);
        registerDao(CouponAreaModel.class,couponAreaDao);


        mapCityDaoConfig = daoConfigMap.get(MapCityDao.class).clone();
        mapCityDaoConfig.initIdentityScope(type);
        mapCityDao = new MapCityDao(mapCityDaoConfig,this);
        registerDao(BaocheMapCity.class, mapCityDao);

    }

    public void clear(){
        busStartCityDaoConfig.getIdentityScope().clear();
        busEndCityDaoConfig.getIdentityScope().clear();
        trainStartCityDaoConfig.getIdentityScope().clear();
        couponAreaDaoConfig.getIdentityScope().clear();
        mapCityDaoConfig.getIdentityScope().clear();
    }


    public BusStartCityDao getBusStartCityDao() {
        return busStartCityDao;
    }


    public BusEndCityDao getBusEndCityDao() {
        return busEndCityDao;
    }


    public DirectStartCityDao getDirectCityDao() {
        return directCityDao;
    }


    public TrainStartCityDao getTrainStartCityDao(){
        return mTrainStartCityDao;
    }

    public CouponAreaDao getCouponAreaDao(){
        return couponAreaDao;
    }

    public MapCityDao getMapCityDao() {
        return mapCityDao;
    }
}
