package com.wxws.myticket.my.Entity;

/**
 * desc:
 * Date: 2016-11-08 15:31
 *
 * @author jiangyan
 */
public class MessageListResponsePara {
    private String noticeId;
    private String title;
    private String createTime;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}