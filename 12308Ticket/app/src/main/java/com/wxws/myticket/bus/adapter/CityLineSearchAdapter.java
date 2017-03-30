package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.POIEntity;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.List;

/**
 * desc: 城际快线搜索后显示
 * Date: 2016/11/15 10:30
 *
 * @auther: lixiangxiang
 */
public class CityLineSearchAdapter extends SimpleBaseAdapter<POIEntity> {

    public CityLineSearchAdapter(Context context, List<POIEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_poi_search;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        POIEntity  poiCityEntity = (POIEntity) getItem(position);
        TextView  tvTitle = holder.getView(R.id.tv_title);
        TextView  tvAddress = holder.getView(R.id.tv_address);
        tvTitle.setText(poiCityEntity.getName());
        tvAddress.setText(String.format("地址：%s%s",poiCityEntity.getCity(), StringUtils.isNullOrEmpty(poiCityEntity.getDistrict())? "" :poiCityEntity.getDistrict()));
        return convertView;
    }
}
