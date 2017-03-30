package com.wxws.myticket.bus.entity;

/**
 * desc: 获取的包车城市列表。
 * Date: 2016-11-18 16:17
 *
 * @author jiangyan
 */
public class BaocheMapCity {

    private String id;
    private String name;
    private String full_pinyin;
    private String simple_pinyin;
    private String initial;
    private String parentId;

    public BaocheMapCity() {
    }

    public BaocheMapCity(String id) {
        this.id = id;
    }

    public BaocheMapCity(String id, String name, String full_pinyin, String simple_pinyin, String initial) {
        this.id = id;
        this.name = name;
        this.full_pinyin = full_pinyin;
        this.simple_pinyin = simple_pinyin;
        this.initial = initial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_pinyin() {
        return full_pinyin;
    }

    public void setFull_pinyin(String full_pinyin) {
        this.full_pinyin = full_pinyin;
    }

    public String getSimple_pinyin() {
        return simple_pinyin;
    }

    public void setSimple_pinyin(String simple_pinyin) {
        this.simple_pinyin = simple_pinyin;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}