package com.wxws.myticket.bus.entity;

/**
 * desc:  约租车 事件
 * Date: 2016-06-22 15:21
 *
 * @auther: lixiangxiang
 */
public class POICitySelectEntity {

    private int isStart;//出发城市还是目的城市

    private POIEntity mPOIEntity; //选中的城市对象


    public POICitySelectEntity(int isStart, POIEntity mPOIEntity){
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
