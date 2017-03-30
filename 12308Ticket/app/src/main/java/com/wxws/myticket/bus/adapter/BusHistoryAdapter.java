package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.BusHistory;

import java.util.List;

/**
 * desc: 出发到达
 * Date: 2016/10/24 11:20
 *
 * @auther: lixiangxiang
 */
public class BusHistoryAdapter extends SimpleBaseAdapter {


    public BusHistoryAdapter(Context context, List<BusHistory> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_bus_history;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        BusHistory item =(BusHistory) getItem(position);
        TextView mTvStart = (TextView) holder.getView(R.id.tv_item_start);
        RelativeLayout rlHistory =  (RelativeLayout)holder.getView(R.id.rl_history);
        TextView mTvEnd = (TextView) holder.getView(R.id.tv_item_end);
        if (item.getStartCity()!=null && item.getEndCity()!=null){
            rlHistory.setVisibility(View.VISIBLE);
            mTvStart.setText(item.getStartCity());
            mTvEnd.setText(item.getEndCity());
        }else {
            rlHistory.setVisibility(View.GONE);
        }
        return convertView;
    }
}
