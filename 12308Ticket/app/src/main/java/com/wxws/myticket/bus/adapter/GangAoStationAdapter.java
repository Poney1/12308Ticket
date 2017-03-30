package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.activity.GangAoCityActivity;
import com.wxws.myticket.bus.entity.GangAoStartCityEntity;
import com.wxws.myticket.bus.entity.StationListEntity;

import java.util.List;

/**
 * desc: 港澳站点
 * Date: 2016/11/10 14:39
 *
 * @auther: lixiangxiang
 */
public class GangAoStationAdapter extends SimpleBaseAdapter<StationListEntity> {

    private Context mContext;
    public GangAoStationAdapter(Context context, List<StationListEntity> data) {
        super(context, data);
        this.mContext = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_gangao_station;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        StationListEntity  stationList = (StationListEntity) getItem(position);
        TextView tvStationName =  holder.getView(R.id.station_name);
        tvStationName.setText(stationList.getStationName());

        return convertView;
    }
}
