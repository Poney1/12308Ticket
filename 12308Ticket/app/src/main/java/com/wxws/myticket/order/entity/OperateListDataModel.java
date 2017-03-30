package com.wxws.myticket.order.entity;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 15:31
 *
 * @author jiangyan
 */
public class OperateListDataModel {
    private String date;
    private List<OperateHistoryModel> historyList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OperateHistoryModel> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<OperateHistoryModel> historyList) {
        this.historyList = historyList;
    }
}