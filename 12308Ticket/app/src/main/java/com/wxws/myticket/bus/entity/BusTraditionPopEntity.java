package com.wxws.myticket.bus.entity;

import com.wxws.myticket.common.utils.StringUtils;

/**
 * desc: 传统汽车票筛选 弹窗对象
 * Date: 2016/10/25 19:18
 *
 * @auther: lixiangxiang
 */
public class BusTraditionPopEntity implements Comparable<BusTraditionPopEntity>,Cloneable {

    private int  busType;//类型. 1 上车点  2 下车点
    public final static  int upBusType = 1;
    public final static  int downBusType = 2;
    private boolean isSelect;//是否选中
    private String  name="";//显示文字
    private String  startTime;
    private String  endTime;//时间筛选
    private int sortPrice;//价格排序  1，降序，2 升序

    public BusTraditionPopEntity(int  busType, boolean isSelect, String name, String startTime, String endTime, int sortPrice) {
        this.busType = busType;
        this.isSelect = isSelect;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sortPrice = sortPrice;
    }

    public int getType() {
        return busType;
    }

    public void setType(int type) {
        this.busType = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getSortPrice() {
        return sortPrice;
    }

    public void setSortPrice(int sortPrice) {
        this.sortPrice = sortPrice;
    }

    @Override
    public BusTraditionPopEntity clone() throws CloneNotSupportedException {
        return (BusTraditionPopEntity)super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (StringUtils.isNullOrEmpty(this.name))
            return false;
        return this.name.equals(((BusTraditionPopEntity)obj).name);
    }

    @Override
    public int hashCode() {
        if (!StringUtils.isNullOrEmpty(this.name))
        return this.name.hashCode();
        else
            return 0;
    }

    @Override
    public int compareTo(BusTraditionPopEntity o) {
        if (StringUtils.isNullOrEmpty(this.name))
            return 1;
        return this.name.compareTo(o.name);
    }
}
