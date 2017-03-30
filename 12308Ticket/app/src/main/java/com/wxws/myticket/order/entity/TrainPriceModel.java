package com.wxws.myticket.order.entity;

/**
 * desc:
 * Date: 2016-11-02 14:39
 *
 * @author jiangyan
 */
public class TrainPriceModel {
    private String fullprice;
    private int fullnum;

    private String childprice;
    private int childnum;

    private String studentprice;
    private int studentnum;

    private String adultinsuranceprice;
    private int adultinsurancenum;

    private String childinsuranceprice;
    private int childinsurancenum;

    private String couponprice;

    public String getFullprice() {
        return fullprice;
    }

    public void setFullprice(String fullprice) {
        this.fullprice = fullprice;
    }

    public int getFullnum() {
        return fullnum;
    }

    public void setFullnum(int fullnum) {
        this.fullnum = fullnum;
    }

    public String getChildprice() {
        return childprice;
    }

    public void setChildprice(String childprice) {
        this.childprice = childprice;
    }

    public int getChildnum() {
        return childnum;
    }

    public void setChildnum(int childnum) {
        this.childnum = childnum;
    }

    public String getStudentprice() {
        return studentprice;
    }

    public void setStudentprice(String studentprice) {
        this.studentprice = studentprice;
    }

    public int getStudentnum() {
        return studentnum;
    }

    public void setStudentnum(int studentnum) {
        this.studentnum = studentnum;
    }

    public String getAdultinsuranceprice() {
        return adultinsuranceprice;
    }

    public void setAdultinsuranceprice(String adultinsuranceprice) {
        this.adultinsuranceprice = adultinsuranceprice;
    }

    public int getAdultinsurancenum() {
        return adultinsurancenum;
    }

    public void setAdultinsurancenum(int adultinsurancenum) {
        this.adultinsurancenum = adultinsurancenum;
    }

    public String getChildinsuranceprice() {
        return childinsuranceprice;
    }

    public void setChildinsuranceprice(String childinsuranceprice) {
        this.childinsuranceprice = childinsuranceprice;
    }

    public int getChildinsurancenum() {
        return childinsurancenum;
    }

    public void setChildinsurancenum(int childinsurancenum) {
        this.childinsurancenum = childinsurancenum;
    }

    public String getCouponprice() {
        return couponprice;
    }

    public void setCouponprice(String couponprice) {
        this.couponprice = couponprice;
    }
}