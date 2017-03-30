package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.SeatSelectModel;

import java.util.List;

/**
 * desc: 火车座位
 * Date: 2016-07-22 17:06
 *
 * @auther: lixiangxiang
 */
public class TrainSeatTypeAdapter extends SimpleBaseAdapter {

    private List<SeatSelectModel> data;

    public TrainSeatTypeAdapter(Context context, List<SeatSelectModel> data) {
        super(context, data);
        this.data = data;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_grid_train_type;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        SeatSelectModel type = (SeatSelectModel) getItem(position);

        TextView tvType = (TextView) holder.getView(R.id.tv_type);

        tvType.setText(type.getName());

        tvType.setSelected(type.getIsSelect());

        return convertView;
    }
}
