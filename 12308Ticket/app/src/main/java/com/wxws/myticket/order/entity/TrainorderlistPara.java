package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 11:19
 *
 * @author jiangyan
 */
public class TrainorderlistPara {
    private int pageNum;
    private int startRow;
    private int endRow;
    private int pages;
    private int total;
    private List<TrainOrderListResponsePara> rows;


    public void setRows(List<TrainOrderListResponsePara> rows) {
        this.rows = rows;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TrainOrderListResponsePara> getRows() {
        return rows;
    }
}