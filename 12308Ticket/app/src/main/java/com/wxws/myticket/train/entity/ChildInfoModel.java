package com.wxws.myticket.train.entity;


import java.io.Serializable;

/**
 * desc:
 * Date: 2016-08-05 10:03
 *
 * @auther: jiangyan
 */
public class ChildInfoModel implements Serializable {
    private String name;
    private String birthday;
    private int gender;// 1为男孩，2为女孩
    private boolean flag_editbirth;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isFlag_editbirth() {
        return flag_editbirth;
    }

    public void setFlag_editbirth(boolean flag_editbirth) {
        this.flag_editbirth = flag_editbirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}