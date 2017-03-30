package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.interfaces.OnAdultSelectListener;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;

import java.util.List;

/**
 * desc:  填写订单 之乘客 信息
 * Date: 2016/11/2 17:26
 *
 * @auther: lixiangxiang
 */
public class FillPassengerAdapter extends SimpleBaseAdapter<PassengerInfoEntity> {

    private  List<PassengerInfoEntity> data;
    private Context context;
    private OnAdultSelectListener  onAdultSelectListener;

    public FillPassengerAdapter(Context context, List<PassengerInfoEntity> data) {
        super(context, data);
        this.data = data ;
        this.context = context ;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_fill_passenger;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        PassengerInfoEntity passengerInfoEntity = (PassengerInfoEntity) getItem(position);

        ImageView itemDelete = holder.getView(R.id.item_delete);
        TextView itemTvName = holder.getView(R.id.item_tv_name);
        TextView itemTvCer = holder.getView(R.id.item_tv_cer);
        TextView itemTvAdult = holder.getView(R.id.item_tv_adult);

        itemTvName.setText(passengerInfoEntity.getPassengerName());
        itemTvCer.setText(passengerInfoEntity.getCertificateNo());

        itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        switch (passengerInfoEntity.getTicketType()){
            case Constants.busAllTicket:
                itemTvAdult.setText(context.getString(R.string.bookTicket_all));
                break;
            case Constants.busChildTicket:
                itemTvAdult.setText(context.getString(R.string.bookTicket_child));
                break;
            case Constants.busCouponTicket:
                itemTvAdult.setText(context.getString(R.string.bookTicket_coupon));
                break;
            case Constants.busHalfTicket:
                itemTvAdult.setText(context.getString(R.string.bookTicket_half));
                break;
        }

        itemTvAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdultSelectListener.onSelectEnd(position,"成人票");
            }
        });

        return convertView;
    }

    public void setOnAdultSelectListener(OnAdultSelectListener  onAdultSelectListener){

        this.onAdultSelectListener = onAdultSelectListener;
    }
}
