package com.wxws.myticket.my.Entity;

import java.io.Serializable;

/**
 * desc:
 * Date: 2016-11-01 13:46
 *
 * @author jiangyan
 */
public class LoginResponsePara implements Serializable{

    /**
     * mobilePhone : mobilePhone
     * userName : userName
     * realName : realName
     * certType : certType
     * certNo : certNo
     * nickName : nickName
     * photo : photo
     * password;
     * authToken;//验证
     * type;// type  0 表示12308   1表示微信  2 表示QQ  3表示微博
     */

    private String mobilePhone;
    private String userName;
    private String realName;
    private String certType;
    private String certNo;
    private String nickName;
    private String photo;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "LoginResponsePara{" +
                "mobilePhone='" + mobilePhone + '\'' +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", certType='" + certType + '\'' +
                ", certNo='" + certNo + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}