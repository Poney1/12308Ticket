package com.wxws.myticket.my.Entity;

import java.io.Serializable;

/**
 * desc:
 * Date: 2016-11-01 10:37
 *
 * @author jiangyan
 */
public class TrainAccountInfo implements Serializable{

    private String trainId;
    private String account;
    private String password;
    private boolean hasbind;
    private boolean pass = true;

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public boolean isHasbind() {
        return hasbind;
    }

    public void setHasbind(boolean hasbind) {
        this.hasbind = hasbind;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TrainAccountInfo{" +
                "trainId='" + trainId + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", hasbind=" + hasbind +
                '}';
    }
}