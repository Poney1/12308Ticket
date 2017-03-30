package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;

import java.util.List;

/**
 * desc: 直通车、汽车票订单详情，乘客信息adapter
 * Date: 2016-11-03 15:28
 *
 * @author jiangyan
 */
public class BusOrderDetailsPassengerAdapter extends SimpleBaseAdapter {

    private Context mContext;

    public BusOrderDetailsPassengerAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<OrderdetailsResponsePara.TicketInfosBean> data) {
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_busorderdetails_passenger;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView tvName = (TextView) holder.getView(R.id.tv_passenger_name);
        TextView tvCertNo = (TextView) holder.getView(R.id.tv_passenger_certno);
        TextView tvMobie = (TextView) holder.getView(R.id.tv_passenger_mobie);
        TextView tvIsFull = (TextView) holder.getView(R.id.tv_passenger_isfull);
        TextView tvInsurance = (TextView) holder.getView(R.id.tv_passenger_insurancenum);

        OrderdetailsResponsePara.TicketInfosBean para = (OrderdetailsResponsePara.TicketInfosBean) data.get(position);

        tvName.setText(para.getName());
        tvCertNo.setText(para.getCertNo());
        tvMobie.setText(para.getMobile());

        switch (para.getTicketType()) {
            case "10":
                tvIsFull.setText("全价票");
                break;
            case "15":
                tvIsFull.setText("半价票");
                break;
            case "17":
                tvIsFull.setText("儿童票");
                break;
            case "40":
                tvIsFull.setText("优惠票");
                break;
        }

        // 如果有保险id，则表示有保险，即有一份意外保险，后台反馈。
        if (para.getInsuranceId() == null || para.getInsuranceId().equals("")) {
            tvInsurance.setText("");
        } else {
            tvInsurance.setText("意外保险1份");
        }

        return convertView;
    }
}