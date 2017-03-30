package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.PopTimeSelectModel;

import java.util.List;

/**
 * desc: 火车类型
 * Date: 2016-07-22 17:06
 *
 * @auther: lixiangxiang
 */
public class TrainSelectTypeAdapter extends SimpleBaseAdapter {

    private List<PopTimeSelectModel> data;

    public TrainSelectTypeAdapter(Context context, List<PopTimeSelectModel> data) {
        super(context, data);
        this.data = data;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_grid_train_type;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        PopTimeSelectModel type = (PopTimeSelectModel) getItem(position);

        TextView tvType = (TextView) holder.getView(R.id.tv_type);

        tvType.setText(type.getName());


        tvType.setSelected(type.isSelect());

        return convertView;
    }
}
