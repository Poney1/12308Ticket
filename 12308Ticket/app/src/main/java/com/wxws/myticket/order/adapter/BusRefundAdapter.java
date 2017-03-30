package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.order.entity.BusRefundModel;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 退款乘客信息adapter
 * Date: 2016-11-04 11:37
 *
 * @author jiangyan
 */
public class BusRefundAdapter extends SimpleBaseAdapter {


    private List<BusRefundModel> mList = new ArrayList<>();
    private OnChoosedRefundListener mListener;

    public BusRefundAdapter(Context context) {
        super(context, null);

    }

    public void refreshData(List<BusRefundModel> data) {
        mList.clear();
        mList.addAll(data);

        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_busrefund;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        final TextView tvName = (TextView) holder.getView(R.id.tv_busrefund_name);
        TextView tvPhone = (TextView) holder.getView(R.id.tv_busrefund_phone);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_busrefund_ticketstatus);
        CheckBox cbCheck = (CheckBox) holder.getView(R.id.cb_busrefund_check);

        BusRefundModel para = (BusRefundModel) data.get(position);

        tvName.setText(para.getName());
        tvPhone.setText(para.getPhone());
        tvStatus.setText(para.getStatus());
        cbCheck.setChecked(para.isChoosed());

        cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    ((BusRefundModel) data.get(position)).setChoosed(true);

                } else {

                    ((BusRefundModel) data.get(position)).setChoosed(false);
                }

                mListener.choosedRefund(getTicketIdList());

            }
        });

        return convertView;
    }

    private List<String> getTicketIdList() {
        List<String> ticketList = new ArrayList<>();

        for (BusRefundModel model : mList) {
            if (model.isChoosed()) {
                ticketList.add(model.getTicketId());
            }
        }

        return ticketList;
    }

    public interface OnChoosedRefundListener {
        void choosedRefund(List<String> ticketIdList);
    }

    public void setOnChoosedRefundListener(OnChoosedRefundListener listener) {
        this.mListener = listener;
    }
}