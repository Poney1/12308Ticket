package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.order.entity.TicketInfo;
import com.wxws.myticket.order.utils.OrderStatusUtils;

import java.util.List;

/**
 * desc: 订单列表，火车票票信息
 * Date: 2016-11-02 10:21
 *
 * @author jiangyan
 */
public class TrainTicketGridViewAdapter extends SimpleBaseAdapter {
    private Context mContext;

    public TrainTicketGridViewAdapter(Context context, List<TicketInfo> ticketinfo) {
        super(context, ticketinfo);
        this.mContext = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_neworderlist_namestatus;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView tv_name = (TextView) holder.getView(R.id.tv_gridview_name);
        TextView tv_status = (TextView) holder.getView(R.id.tv_gridview_ticketstatus);

        TicketInfo ticketInfo = (TicketInfo) getItem(position);
        tv_name.setText(ticketInfo.getName());
        tv_status.setText(OrderStatusUtils.getTicketStatus(Integer.parseInt(ticketInfo.getTicketStatus())));


        return convertView;
    }
}