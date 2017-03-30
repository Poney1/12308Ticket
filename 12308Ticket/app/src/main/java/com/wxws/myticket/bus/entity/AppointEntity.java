package com.wxws.myticket.bus.entity;


/**
 * desc:  约租车 城市选择
 * Date: 2016-06-22 15:21
 *
 * @auther: lixiangxiang
 */
public class AppointEntity {

    private int isStart;//出发城市还是目的城市

    private POIEntity mPOIEntity; //选中的城市对象


    public AppointEntity(int isStart, POIEntity mPOIEntity){
        this.isStart = isStart;
        this.mPOIEntity = mPOIEntity;
    }

    public int getIsStart() {
        return isStart;
    }

    public POIEntity getPOIEntity() {
        return mPOIEntity;
    }
}
