package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.TrainStationInfoModel;

import java.util.List;

/**
 * desc: 停留站点信息
 * Date: 2016-07-26 14:43
 *
 * @auther: lixiangxiang
 */
public class TrainListDescriptionAdapter extends SimpleBaseAdapter {

    protected TrainListDescriptionAdapter(Context context, List<TrainStationInfoModel.DataBean> data) {

        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_station_information;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TrainStationInfoModel.DataBean dataBean = (TrainStationInfoModel.DataBean) getItem(position);

        TextView tvOrder = (TextView) holder.getView(R.id.tv_order);
        TextView tvStationName = (TextView) holder.getView(R.id.tv_station_name);
        TextView tvArriveTime = (TextView) holder.getView(R.id.tv_arrive_time);
        TextView tvStartTime = (TextView) holder.getView(R.id.tv_start_time);
        TextView tvWaitTime = (TextView) holder.getView(R.id.tv_wait_time);

        tvOrder.setText(dataBean.getStationNo());
        tvStationName.setText(dataBean.getStationName());
        tvArriveTime.setText(dataBean.getArriveTime());
        tvStartTime.setText(dataBean.getStartTime());
        tvWaitTime.setText(dataBean.getStopOverTime());
        return convertView;
    }
}
