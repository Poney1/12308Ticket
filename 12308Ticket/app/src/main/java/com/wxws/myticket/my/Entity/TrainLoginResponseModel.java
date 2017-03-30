package com.wxws.myticket.my.Entity;

/**
 * desc:
 * Date: 2016-11-07 16:56
 *
 * @author jiangyan
 */
public class TrainLoginResponseModel {
    private String id;
    private String trainAccount;
    private int isPass; // 1为通过，2为不通过


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainAccount() {
        return trainAccount;
    }

    public void setTrainAccount(String trainAccount) {
        this.trainAccount = trainAccount;
    }

    public int getIsPass() {
        return isPass;
    }

    public void setIsPass(int isPass) {
        this.isPass = isPass;
    }
}