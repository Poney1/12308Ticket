package com.wxws.myticket.bus.entity;

/**
 * User: 李俊峰
 * Date: 2015-12-16
 * Time: 17:49
 */
public class GetPOIEvent {
    private int editId;
    private BaochePOIEntity mPoiInfo;

    public GetPOIEvent(int editId, BaochePOIEntity poiInfo) {
        this.editId = editId;
        mPoiInfo = poiInfo;
    }

    public int getEditId() {
        return editId;
    }

    public void setEditId(int editId) {
        this.editId = editId;
    }

    public BaochePOIEntity getPoiInfo() {
        return mPoiInfo;
    }

    public void setPoiInfo(BaochePOIEntity poiInfo) {
        mPoiInfo = poiInfo;
    }
}
