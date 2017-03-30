package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.TrainStartCityModel;

import java.util.List;

/**
 * desc:
 * Date: 2016/8/9 16:48
 *
 * @auther: lixiangxiang
 */
public class TrainSearchResultAdapter extends SimpleBaseAdapter {

    public TrainSearchResultAdapter(Context context, List<TrainStartCityModel> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_city_list;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TrainStartCityModel trainStartCityModel = (TrainStartCityModel) getItem(position);
        TextView tvGroupTitle = (TextView) holder.getView(R.id.tvGroupTitle);
        TextView tvCityName = (TextView) holder.getView(R.id.tvCityName);

        tvGroupTitle.setVisibility(View.GONE);
        tvCityName.setText(trainStartCityModel.getName());
        return convertView;
    }
}
