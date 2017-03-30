package com.wxws.myticket.bus.entity;

/**
 * desc: 活动
 * Date: 2016/12/5 11:12
 *
 * @auther: lixiangxiang
 */
public class ActiveEntity {
    /**
     * name	string	是	广告名称
     imageUrl	string	是	图片路径
     clickUrl	string	是	图片点击链接
     remark	string	是	广告描述
     createTime	string	是	活动创建时间
     failureTime	string	是	活动失效时间
     ownCityName	string	是	广告所属城市
     sort	string	是	排序
     width	int	是	宽
     height	int	是	高
     autoClose 自动关闭时间
     */

    private String name;
    private String imageUrl;
    private String clickUrl;
    private String remark;
    private String createTime;
    private String failureTime;
    private String ownCityName;
    private String sort;
    private int width;
    private int height;
    private int autoClose;


    public int getAutoClose() {
        return autoClose;
    }

    public void setAutoClose(int autoClose) {
        this.autoClose = autoClose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(String failureTime) {
        this.failureTime = failureTime;
    }

    public String getOwnCityName() {
        return ownCityName;
    }

    public void setOwnCityName(String ownCityName) {
        this.ownCityName = ownCityName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
