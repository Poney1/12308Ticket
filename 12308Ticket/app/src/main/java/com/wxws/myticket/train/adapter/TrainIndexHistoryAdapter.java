package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.TrainLineSaveEntity;

import java.util.List;


/**
 * desc: 火车票 历史搜索线路
 * user: lixiangxiang
 * date: 2016-03-28
 * time: 17:42
 */
public class TrainIndexHistoryAdapter extends SimpleBaseAdapter {

    private TextView mTvStart;
    private TextView mTvEnd;

    public TrainIndexHistoryAdapter(Context context, List<TrainLineSaveEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_index_train_history;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TrainLineSaveEntity item = (TrainLineSaveEntity) getItem(position);
        mTvStart = (TextView) holder.getView(R.id.tv_item_start);
        mTvEnd = (TextView) holder.getView(R.id.tv_item_end);
        if (item.getStartCity() != null && item.getEndCity() != null) {
            mTvStart.setText(item.getStartCity());
            mTvEnd.setText(item.getEndCity());
        }
        return convertView;
    }
}
