package com.wxws.myticket.train.entity;

import java.io.Serializable;

/**
 * desc: 筛选类型 的 window
 * Date: 2016/8/11 15:35
 *
 * @auther: lixiangxiang
 */
public class PopTimeSelectModel implements Serializable,Cloneable {

    private boolean  isSelect;

    private String name;

    @Override
    public PopTimeSelectModel clone() throws CloneNotSupportedException {
        return (PopTimeSelectModel) super.clone();
    }

    public PopTimeSelectModel(String name, boolean  isSelect){
        this.isSelect = isSelect ;
        this.name = name ;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getName() {
        return name;
    }
}
