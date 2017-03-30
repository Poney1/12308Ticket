package com.wxws.myticket.app.base.adapter;

/**
 * desc: 设置样式
 * Date: 2016/10/17 16:07
 *
 * @auther: lixiangxiang
 */
public interface MulItemTypeSupport<T> {

    int getLayoutId(int position,T t);

    int getViewTypeCount();

    int getItemViewType(int position,T t);
}
