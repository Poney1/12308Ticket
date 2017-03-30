package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.order.entity.StopoverStation;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-15 14:50
 *
 * @author jiangyan
 */
public class BusMapAdapter extends SimpleBaseAdapter {

    public BusMapAdapter(Context context) {
        super(context, null);
    }

    public void refreshData(List<StopoverStation> staionList) {
        replaceAll(staionList);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_busmap;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        ImageView ivIcon = (ImageView) holder.getView(R.id.iv_busmap_icon);
        TextView tvName = (TextView) holder.getView(R.id.tv_busmap_staionname);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_busmap_staionstatus);

        StopoverStation station = (StopoverStation) data.get(position);
        tvName.setText(station.getStationName());
        tvStatus.setText("尚未发车");

        return convertView;
    }
}