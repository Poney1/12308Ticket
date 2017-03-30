package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.my.Entity.MessageListResponsePara;

import java.util.List;

/**
 * desc: 消息列表adapter
 * Date: 2016-11-07 10:38
 *
 * @author jiangyan
 */
public class MessageAdapter extends SimpleBaseAdapter {

    public MessageAdapter(Context context) {
        super(context, null);
    }

    public void refreshData(List<MessageListResponsePara> data) {
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_message;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView tvTitle = (TextView) holder.getView(R.id.tv_message_title);
        TextView tvDate = (TextView) holder.getView(R.id.tv_message_date);

        MessageListResponsePara para = (MessageListResponsePara) data.get(position);

        tvTitle.setText(para.getTitle());
        tvDate.setText(para.getCreateTime());

        return convertView;
    }
}