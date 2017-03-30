package com.wxws.myticket.pay;

/**
 * desc: 支付方式对象
 * Date: 2016/11/3 10:59
 *
 * @auther: lixiangxiang
 */
public class PaymentEntity  implements  Comparable<PaymentEntity>{


    /**
     * aid : 10
     * channelType : WECHAT_APP
     * channelName : 微信支付
     * source : Android
     * orders : 1
     * status : 1
     * description : sdsd
     * createTime : 1471504437000
     * updateTime : 1471504437000
     */

    private String aid;
    private String channelType;
    private String channelName;
    private String source;
    private int orders;
    private int status;
    private String description;
    private long createTime;
    private long updateTime;

    @Override
    public int compareTo(PaymentEntity o) {
        return this.getOrders()-o.getOrders();
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
