package com.wxws.myticket.my.Entity;

import java.io.Serializable;

/**
 * desc: 登录账号密码信息
 * Date: 2016-11-05 15:04
 *
 * @author jiangyan
 */
public class LoginInfo implements Serializable{

    private String username;
    private String password;
    private boolean rempsw;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRempsw() {
        return rempsw;
    }

    public void setRempsw(boolean rempsw) {
        this.rempsw = rempsw;
    }
}