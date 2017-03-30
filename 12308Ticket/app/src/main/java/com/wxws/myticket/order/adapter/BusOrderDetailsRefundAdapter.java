package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.order.entity.OrderdetailsResponsePara;
import com.wxws.myticket.order.utils.OrderStatusUtils;

import java.util.List;

/**
 * desc: 直通车、汽车票订单详情退款信息adapter
 * Date: 2016-11-03 15:28
 *
 * @author jiangyan
 */
public class BusOrderDetailsRefundAdapter extends SimpleBaseAdapter {

    private Context mContext;

    public BusOrderDetailsRefundAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<OrderdetailsResponsePara.TicketInfosBean> data) {
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_busorderdetails_refundinfo;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView tvTotalMoney = (TextView) holder.getView(R.id.tv_itemrefund_totalmoney);
        TextView tvTime = (TextView) holder.getView(R.id.tv_itemrefund_time);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_itemrefund_status);
        TextView tvRefundId = (TextView) holder.getView(R.id.tv_itemrefund_refundid);
        TextView tvTicketMoney = (TextView) holder.getView(R.id.tv_itemrefund_ticketmoney);
        TextView tvTicketName = (TextView) holder.getView(R.id.tv_itemrefund_ticketname);
        TextView tvInsuranceId = (TextView) holder.getView(R.id.tv_itemrefund_insuranceid);
        TextView tvInsuranceName = (TextView) holder.getView(R.id.tv_itemrefund_insurancename);
        TextView tvInsuranceMoney = (TextView) holder.getView(R.id.tv_itemrefund_insurancemoney);

        OrderdetailsResponsePara.TicketInfosBean para = (OrderdetailsResponsePara.TicketInfosBean) data.get(position);

        tvTotalMoney.setText(ArithUtils.mul(para.getRefundMoney(),0.01)+"");
        tvTime.setText(para.getRefundUpdateTime());
        tvStatus.setText(OrderStatusUtils.getRefundStatus(para.getRefundStatus()));
        tvRefundId.setText(para.getRefundId());
        tvTicketMoney.setText("¥"+ArithUtils.mul(para.getTicketRefundMoney(),0.01));
        tvTicketName.setText(para.getName());
        tvInsuranceId.setText(para.getInsuranceId());
        tvInsuranceName.setText(para.getName());//TODO 被保人name
        tvInsuranceMoney.setText("¥"+ArithUtils.mul(para.getInsuranceRefundMoney(),0.01));

        return convertView;
    }
}