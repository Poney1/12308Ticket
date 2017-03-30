package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.AircraftCityEntity;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.List;

/**
 * desc: 机场快线 城市
 * Date: 2016/11/10 19:20
 *
 * @auther: lixiangxiang
 */
public class AircraftCityAdapter extends SimpleBaseAdapter<AircraftCityEntity> {

    private TextView mTvCity;
    private TextView mTvAircraft;

    public AircraftCityAdapter(Context context, List<AircraftCityEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_aircraft_city;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        AircraftCityEntity aircraftCityEntity = (AircraftCityEntity) getItem(position);
        mTvCity =  holder.getView(R.id.tv_city);
        mTvAircraft =  holder.getView(R.id.tv_aircraft);

        mTvCity.setText(StringUtils.isNullOrEmpty(aircraftCityEntity.getName()) ? "" :aircraftCityEntity.getName());
        mTvAircraft.setText(StringUtils.isNullOrEmpty(aircraftCityEntity.getSecondName()) ? "" :aircraftCityEntity.getSecondName());
        return convertView;
    }





}
