package com.wxws.myticket.bus.entity;

/**
 * desc: 保险选择事件
 * Date: 2016/12/15 17:03
 *
 * @auther: lixiangxiang
 */
public class InsuranceSelectEntity {

    public static int noSelect = 0;//不选择保险
    public static int select = 1;//选择保险

    private int flag;
    private BusInsuranceEntity entity;

    public InsuranceSelectEntity(int flag,BusInsuranceEntity entity) {
        this.flag = flag;
        this.entity = entity;
    }

    public int getFlag() {
        return flag;
    }

    public BusInsuranceEntity getEntity() {
        return entity;
    }
}
