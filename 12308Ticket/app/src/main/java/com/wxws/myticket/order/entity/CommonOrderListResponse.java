package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-12-19 16:21
 *
 * @author jiangyan
 */
public class CommonOrderListResponse {

    private int invalidTotal;
    private int validTotal;
    private int total;
    private boolean isLastPage;
    private int pageSize;
    private int pageNum;
    private List<OrderListResponsePara> rows;


    public List<OrderListResponsePara> getRows() {
        return rows;
    }

    public void setRows(List<OrderListResponsePara> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getInvalidTotal() {
        return invalidTotal;
    }

    public void setInvalidTotal(int invalidTotal) {
        this.invalidTotal = invalidTotal;
    }

    public int getValidTotal() {
        return validTotal;
    }

    public void setValidTotal(int validTotal) {
        this.validTotal = validTotal;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}