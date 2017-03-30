package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.SeatinfoModel;

import java.util.List;

/**
 * desc:
 * Date: 2016-08-01 15:02
 *
 * @auther: jiangyan
 */
public class SeatInfoAdapter extends SimpleBaseAdapter {

    private Context mContext;
    private boolean mIsEndorse;
    private OnBuyTrainTicketClickListener mBuyListener;

    public SeatInfoAdapter(Context context, boolean isEndorse) {
        super(context, null);
        this.mContext = context;
        this.mIsEndorse = isEndorse;
    }

    public void refreshData(List<SeatinfoModel> seatList) {
        replaceAll(seatList);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_chooseseat_seatinfo;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        TextView tv_name = (TextView) holder.getView(R.id.tv_itemchoose_type);
        TextView tv_count = (TextView) holder.getView(R.id.tv_itemchoose_count);
        TextView tv_price = (TextView) holder.getView(R.id.tv_itemchoose_price);
        Button bt_buy = (Button) holder.getView(R.id.bt_itemchoose_buy);

        SeatinfoModel seatinfoModel = (SeatinfoModel) getItem(position);

        tv_name.setText(seatinfoModel.getName());

        tv_count.setText(seatinfoModel.getNum() + "张");

        tv_price.setText("¥" + seatinfoModel.getPrice() * 0.01);

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuyListener.buy(position);
            }
        });

        if (seatinfoModel.getNum() == 0) {
            bt_buy.setBackgroundResource(R.drawable.shape_999);
            bt_buy.setClickable(false);
            bt_buy.setSelected(true);
            bt_buy.setText("售完");
            tv_count.setText("无");

            tv_name.setTextColor(Color.parseColor("#999999"));
            tv_count.setTextColor(Color.parseColor("#999999"));
            tv_price.setTextColor(Color.parseColor("#999999"));

        } else {
            bt_buy.setBackgroundResource(R.drawable.selector_bt_endorse);
            bt_buy.setClickable(true);
            bt_buy.setSelected(false);
            if (mIsEndorse) {
                bt_buy.setText("改签");
            } else {
                bt_buy.setText("购买");
            }


            tv_name.setTextColor(Color.parseColor("#333333"));
            tv_count.setTextColor(Color.parseColor("#333333"));
            tv_price.setTextColor(Color.parseColor("#333333"));

            if (seatinfoModel.getNum() <= 10) {
                tv_count.setText("仅剩" + seatinfoModel.getNum() + "张");
                tv_count.setTextColor(Color.parseColor("#FF7272"));
            }
        }

        return convertView;
    }

    public interface OnBuyTrainTicketClickListener {
        void buy(int position);
    }

    public void setOnBuyTrainTicketClickListener(OnBuyTrainTicketClickListener listener) {
        this.mBuyListener = listener;
    }

}