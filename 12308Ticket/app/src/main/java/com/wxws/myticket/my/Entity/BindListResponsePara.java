package com.wxws.myticket.my.Entity;

/**
 * desc:
 * Date: 2016-11-09 17:14
 *
 * @author jiangyan
 */
public class BindListResponsePara {

    public BindListResponsePara() {
    }

    public BindListResponsePara(int type, String name, String openId, int bind) {
        this.type = type;
        this.name = name;
        this.openId = openId;
        this.bind = bind;
    }

    /**
     * type : 1
     * name : 微信
     * openId : null
     * bind : 0
     */


    private int type;
    private String name;
    private String openId;
    private int bind;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }
}