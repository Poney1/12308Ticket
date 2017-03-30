package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.StationDescEntity;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.List;

/**
 * desc: 地图描述
 * Date: 2016/11/9 11:31
 *
 * @auther: lixiangxiang
 */
public class StationDescAdapter extends SimpleBaseAdapter<StationDescEntity> {

    private List<StationDescEntity> data;

    public StationDescAdapter(Context context, List<StationDescEntity> data) {
        super(context, data);
        this.data = data;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_station_desc;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView tvStartStation = holder.getView(R.id.tv_start_station);
        TextView tvPlace = holder.getView(R.id.tv_place);
        TextView tvStationPhone = holder.getView(R.id.tv_station_phone);
        TextView tvDistance = holder.getView(R.id.tv_distance);

        StationDescEntity stationDescEntity = (StationDescEntity) getItem(position);

        tvStartStation.setText(stationDescEntity.getStationName());
        tvPlace.setText(stationDescEntity.getAddress());
        if (!StringUtils.isNullOrEmpty(stationDescEntity.getDistance())) {
            tvDistance.setVisibility(View.VISIBLE);
            tvDistance.setText(stationDescEntity.getDistance());
        } else {
            tvDistance.setVisibility(View.GONE);
        }

        if (!StringUtils.isNullOrEmpty(stationDescEntity.getContactMobile())) {
            tvStationPhone.setVisibility(View.VISIBLE);
            tvStationPhone.setText(stationDescEntity.getContactMobile());
        } else {
            tvStationPhone.setVisibility(View.GONE);
        }

        return convertView;
    }
}
