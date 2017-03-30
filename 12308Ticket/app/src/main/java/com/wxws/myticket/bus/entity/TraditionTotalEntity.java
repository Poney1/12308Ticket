package com.wxws.myticket.bus.entity;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 车次列表对象
 * Date: 2016/12/7 15:44
 *
 * @auther: lixiangxiang
 */
public class TraditionTotalEntity implements Serializable {

    private int total;

    private List<TraditionBusListEntity> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TraditionBusListEntity> getRows() {
        return rows;
    }

    public void setRows(List<TraditionBusListEntity> rows) {
        this.rows = rows;
    }
}
