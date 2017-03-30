package com.wxws.myticket.train.rxentity;


/**
 * desc:
 * Date: 2016-08-18 14:17
 *
 * @author jiangyan
 */
public class TrainLoginEvent {

    public TrainLoginEvent(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    private boolean loginSuccess;
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}