package com.wxws.myticket.common.interfaces;


import com.wxws.myticket.train.entity.SelectBusEntity;

import java.util.List;

/**
 * desc: 上车点或者下车点的筛选
 * user: lixiangxiang
 * date: 2016-03-30
 * time: 19:45
 */
public interface BusUpOrDownCallback {

    void onScreenBus(List<SelectBusEntity> up, int position); //下车点的回调

}
