package com.wxws.myticket.train.entity;


import com.wxws.myticket.common.utils.StringUtils;

import java.io.Serializable;

/**
 * desc:  上下车
 * user: lixiangxiang
 * date: 2016-03-31
 * time: 09:08
 */
public class SelectBusEntity implements Serializable, Comparable {

    private String station;//上下车点
    private int isSelected; //是否选中 1 选中，0 未选中
    private String ticketType;//车票类型
    private String price;//价格排序
    private int selectCondition;//选择条件

    public final static  int TICKET_UP = 0;//上车点
    public final static  int TICKET_DOWN = 1;//下车点
    public final static  int TICKET_PRICE = 2;//价格
    public final static  int TICKET_TYPE = 3;//车票类型筛选


    public SelectBusEntity(String station, String price, String ticketType, int selectCondition, int isSelected) {
        this.station = station;
        this.isSelected = isSelected;
        this.ticketType = ticketType;
        this.price = price;
        this.selectCondition = selectCondition;
    }

    public int getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(int selectCondition) {
        this.selectCondition = selectCondition;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int hashCode() {
        if (!StringUtils.isNullOrEmpty(this.station)) {
            return station.hashCode();
        } else if (!StringUtils.isNullOrEmpty(this.ticketType)) {
            return ticketType.hashCode();
        }
        return 1000;
    }

    public int compareTo(Object another) {
        SelectBusEntity slt = (SelectBusEntity) another;
        if (!StringUtils.isNullOrEmpty(this.station)) {
            return this.station.compareTo(slt.station);
        } else if (!StringUtils.isNullOrEmpty(this.ticketType)) {
            return this.ticketType.compareTo(slt.ticketType);
        }
        return 0;
    }

    public boolean equals(Object obj) {
        SelectBusEntity s = (SelectBusEntity) obj;
        if (!StringUtils.isNullOrEmpty(this.station)) {
            return this.station.equals(s.station);
        } else if (!StringUtils.isNullOrEmpty(this.ticketType)) {
            return this.ticketType.equals(s.ticketType);
        }
        return false;
    }

}
