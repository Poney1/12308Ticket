package com.wxws.myticket.my.Entity;

import android.provider.ContactsContract;

import com.wxws.myticket.bus.configs.Constants;

import java.io.Serializable;

/**
 * desc: 乘客信息
 * Date: 2016/11/2 17:34
 *
 * @auther: lixiangxiang
 */
public class PassengerInfoEntity implements Serializable {


    /**
     * passengerId : 1148908
     * userId : 9475754
     * passengerName : 学生
     * gender : 1
     * countryCode : CN
     * certificateType : 01
     * certificateNo : 430621199203285022
     * personType : 3
     * checkStatus : 1
     * source : 12306
     * mobilePhone : 18207491936
     * student : {"psgId":1148908,"provinceCode":"430000","schoolName":"湖南大学","schoolCode":"10532","studentNo":"20140906","schoolSystem":3,"enterYear":"2016","preferenceCardNo":"204679561","preferenceFromStationName":"深圳","preferenceFromStationCode":"1505","preferenceToStationName":"长沙","preferenceToStationCode":"1407"}
     */

    private String passengerId;
    private String userId;
    private String passengerName;
    private int gender;
    private String countryCode;
    private String certificateType;
    private String certificateNo;
    private int personType;
    private int checkStatus;
    private String source;
    private String mobilePhone;
    private boolean isSelect = false;
    private String ticketType = Constants.busAllTicket;//默认全价票
    /**
     * psgId : 1148908
     * provinceCode : 430000
     * schoolName : 湖南大学
     * schoolCode : 10532
     * studentNo : 20140906
     * schoolSystem : 3
     * enterYear : 2016
     * preferenceCardNo : 204679561
     * preferenceFromStationName : 深圳
     * preferenceFromStationCode : 1505
     * preferenceToStationName : 长沙
     * preferenceToStationCode : 1407
     */

    private StudentBean student;

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public StudentBean getStudent() {
        return student;
    }

    public void setStudent(StudentBean student) {
        this.student = student;
    }

    public static class StudentBean implements Serializable{
        private String psgId;
        private String provinceCode;
        private String schoolName;
        private String schoolCode;
        private String studentNo;
        private int schoolSystem;
        private String enterYear;
        private String preferenceCardNo;
        private String preferenceFromStationName;
        private String preferenceFromStationCode;
        private String preferenceToStationName;
        private String preferenceToStationCode;

        public String getPsgId() {
            return psgId;
        }

        public void setPsgId(String psgId) {
            this.psgId = psgId;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolCode() {
            return schoolCode;
        }

        public void setSchoolCode(String schoolCode) {
            this.schoolCode = schoolCode;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public int getSchoolSystem() {
            return schoolSystem;
        }

        public void setSchoolSystem(int schoolSystem) {
            this.schoolSystem = schoolSystem;
        }

        public String getEnterYear() {
            return enterYear;
        }

        public void setEnterYear(String enterYear) {
            this.enterYear = enterYear;
        }

        public String getPreferenceCardNo() {
            return preferenceCardNo;
        }

        public void setPreferenceCardNo(String preferenceCardNo) {
            this.preferenceCardNo = preferenceCardNo;
        }

        public String getPreferenceFromStationName() {
            return preferenceFromStationName;
        }

        public void setPreferenceFromStationName(String preferenceFromStationName) {
            this.preferenceFromStationName = preferenceFromStationName;
        }

        public String getPreferenceFromStationCode() {
            return preferenceFromStationCode;
        }

        public void setPreferenceFromStationCode(String preferenceFromStationCode) {
            this.preferenceFromStationCode = preferenceFromStationCode;
        }

        public String getPreferenceToStationName() {
            return preferenceToStationName;
        }

        public void setPreferenceToStationName(String preferenceToStationName) {
            this.preferenceToStationName = preferenceToStationName;
        }

        public String getPreferenceToStationCode() {
            return preferenceToStationCode;
        }

        public void setPreferenceToStationCode(String preferenceToStationCode) {
            this.preferenceToStationCode = preferenceToStationCode;
        }
    }
}
