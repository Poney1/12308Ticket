package com.wxws.myticket.my.Entity;

/**
 * desc:
 * Date: 2016-11-07 14:47
 *
 * @author jiangyan
 */
public class ShareInfo {

    /**
     * shareId : 11112222
     * type : 0
     * content : 买汽车票就上12308
     * title : 汽车票
     * url : www.baidu.com
     * photo : www.baidu.com
     * createTime : 2016-11-02 18:20:20
     */

    private String shareId;
    private String type;
    private String content;
    private String title;
    private String url;
    private String photo;
    private String createTime;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}