package com.wxws.myticket.train.entity;

import java.io.Serializable;


/**
 * desc: 座次排序
 * Date: 2016/8/11 14:45
 *
 * @auther: lixiangxiang
 */
public class SeatSelectModel implements Serializable, Comparable<SeatSelectModel>,Cloneable {

    private int index;

    private String name;

    private String type;

    private boolean isSelect;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public SeatSelectModel(int index, String name, String type, boolean isSelect) {
        super();
        this.name = name;
        this.index = index;
        this.type = type;
        this.isSelect = isSelect ;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        return this.index == ((SeatSelectModel) o).index;
    }

    @Override
    public int compareTo(SeatSelectModel another) {
        if (this.getIndex() < another.getIndex()) {
            return -1;
        } else if (this.getIndex() > another.getIndex()) {
            return 1;
        } else {
            return 0;
        }
    }
    @Override
    public SeatSelectModel clone() throws CloneNotSupportedException {
        return (SeatSelectModel)super.clone();
    }
}
