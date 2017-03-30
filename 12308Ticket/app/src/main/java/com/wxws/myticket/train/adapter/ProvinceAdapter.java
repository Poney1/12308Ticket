package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.TrainRealCityModel;

import java.util.List;

/**
 * desc: 省份adapter
 * Date: 2016/8/9 11:40
 *
 * @auther: lixiangxiang
 */
public class ProvinceAdapter extends SimpleBaseAdapter {

    public ProvinceAdapter(Context context, List<TrainRealCityModel> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_recycler_city;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TrainRealCityModel trainRealCityModel = (TrainRealCityModel) getItem(position);
        TextView tvShowFirst = (TextView) holder.getView(R.id.tv_show_first);
        TextView tvShowCity = (TextView) holder.getView(R.id.tv_show_city);
        tvShowFirst.setText(trainRealCityModel.getCity());
        tvShowCity.setText(trainRealCityModel.getCity());
        if (trainRealCityModel.isGroup()) {
            tvShowFirst.setVisibility(View.VISIBLE);
            tvShowCity.setVisibility(View.GONE);
        } else {
            tvShowFirst.setVisibility(View.GONE);
            tvShowCity.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
