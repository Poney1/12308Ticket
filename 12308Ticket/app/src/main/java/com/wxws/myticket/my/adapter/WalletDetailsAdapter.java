package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-24 14:07
 *
 * @author jiangyan
 */
public class WalletDetailsAdapter extends SimpleBaseAdapter {

    public WalletDetailsAdapter(Context context) {
        super(context, null);
    }

    public void refreshData(List<String> data){
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_walletdetails;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView tvInfo = (TextView) holder.getView(R.id.tv_walletdetails_item_info);
        TextView tvTime = (TextView) holder.getView(R.id.tv_walletdetails_item_time);
        TextView tvMoney = (TextView) holder.getView(R.id.tv_walletdetails_item_money);
        TextView tvAllMoney = (TextView) holder.getView(R.id.tv_walletdetails_item_allmoney);

        return convertView;
    }
}