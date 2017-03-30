package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.train.entity.SeatinfoModel;

import java.util.List;

/**
 * desc:
 * Date: 2016-08-03 14:32
 *
 * @auther: jiangyan
 */
public class FillinorderSeatAdapter extends SimpleBaseAdapter {

    private Context mContext;
    private OnChoosedSeatListener mOnChoosedSeatListener;

    public FillinorderSeatAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<SeatinfoModel> seatList) {
        replaceAll(seatList);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_fillinorder_seatsinfo;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        TextView tv_name = (TextView) holder.getView(R.id.tv_itemfillinorder_seatname);
        TextView tv_num = (TextView) holder.getView(R.id.tv_itemfillinorder_seatnum);
        TextView tv_price = (TextView) holder.getView(R.id.tv_itemfillinorder_seatprice);
        CheckBox cb_ischoosed = (CheckBox) holder.getView(R.id.cb_itemfillinorder_ischoosed);

        SeatinfoModel seatInfo = (SeatinfoModel) getItem(position);

        tv_name.setText(seatInfo.getName());
        tv_num.setText(seatInfo.getNum() + "张");
        tv_price.setText("¥" + seatInfo.getPrice() * 0.01);
        if (seatInfo.isChoosed()) {
            cb_ischoosed.setChecked(true);
        } else {
            cb_ischoosed.setChecked(false);
        }
        cb_ischoosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    mOnChoosedSeatListener.choosed(position);
                }
                ((CheckBox) v).setChecked(true);
            }
        });

        return convertView;
    }


    public interface OnChoosedSeatListener {
        void choosed(int position);
    }

    public void setOnChoosedSeatListener(OnChoosedSeatListener listener) {
        this.mOnChoosedSeatListener = listener;
    }


}