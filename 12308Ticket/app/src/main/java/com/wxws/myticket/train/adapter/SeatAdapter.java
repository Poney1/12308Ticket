package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.train.entity.SeatListEntity;

import java.util.List;

/**
 * desc: 座位排序
 * Date: 2016/8/11 11:42
 *
 * @auther: lixiangxiang
 */
public class SeatAdapter extends SimpleBaseAdapter {

    private boolean isHaveTicket;
    private Context context;

    public SeatAdapter(Context context, List<SeatListEntity> data, boolean isHaveTicket) {
        super(context, data);
        this.isHaveTicket = isHaveTicket;
        this.context = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_seat;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        SeatListEntity seatListBean = (SeatListEntity) getItem(position);
        TextView tvSoft = (TextView) holder.getView(R.id.tv_soft);
        TextView tvSoftPrice = (TextView) holder.getView(R.id.tv_soft_price);

        if (isHaveTicket) {
            if (seatListBean.getNum() > 0) {
                tvSoft.setText(String.format("%s:", seatListBean.getName()));
                tvSoftPrice.setText(String.format("%s张", seatListBean.getNum()));
                tvSoftPrice.setTextColor(context.getResources().getColor(R.color.train_list_item_unit));
                tvSoft.setTextColor(context.getResources().getColor(R.color.c_666666));
            } else if (seatListBean.getNum() == 0) {
                tvSoft.setText(String.format("%s:", seatListBean.getName()));
                tvSoftPrice.setText("无");
                tvSoftPrice.setTextColor(context.getResources().getColor(R.color.c_999999));
                tvSoft.setTextColor(context.getResources().getColor(R.color.c_999999));
            } else {
                tvSoft.setVisibility(View.GONE);
                tvSoftPrice.setVisibility(View.GONE);
            }
        } else {
            if (seatListBean.getNum() > 0) {
                tvSoft.setText(String.format("%s:", seatListBean.getName()));
                tvSoftPrice.setText(String.format("￥%s",ArithUtils.div(seatListBean.getPrice(),100.00)+""));
                tvSoftPrice.setTextColor(context.getResources().getColor(R.color.train_list_item_price));
                tvSoft.setTextColor(context.getResources().getColor(R.color.c_666666));
            } else if (seatListBean.getNum() == 0) {
                tvSoft.setText(String.format("%s:", seatListBean.getName()));
                tvSoftPrice.setText(String.format("￥%s", ArithUtils.div(seatListBean.getPrice(),100.00)+""));
                tvSoftPrice.setTextColor(context.getResources().getColor(R.color.c_999999));
                tvSoft.setTextColor(context.getResources().getColor(R.color.c_999999));
            } else {
                tvSoft.setVisibility(View.GONE);
                tvSoftPrice.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
}
