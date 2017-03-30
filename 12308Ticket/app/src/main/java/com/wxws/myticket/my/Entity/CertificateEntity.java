package com.wxws.myticket.my.Entity;

import java.io.Serializable;

/**
 * desc: 护照
 * Date: 2016/8/15 16:01
 *
 * @auther: lixiangxiang
 */
public class CertificateEntity implements Serializable {

    private String passengerType;

    private String passengerNo;

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getPassengerNo() {
        return passengerNo;
    }

    public void setPassengerNo(String passengerNo) {
        this.passengerNo = passengerNo;
    }
}
