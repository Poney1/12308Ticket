package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.train.entity.NewPassengerInfoModel;

import java.util.List;

/**
 * desc:
 * Date: 2016-08-03 14:34
 *
 * @auther: jiangyan
 */
public class FillinorderPassengerAdapter extends SimpleBaseAdapter {

    private Context mContext;
    private OnUpdateListener mOnUpdateListener;

    public FillinorderPassengerAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<NewPassengerInfoModel> passengerList) {
        replaceAll(passengerList);
    }

    public List<NewPassengerInfoModel> getChoosedPassenger() {
        return data;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_fillinorder_passenger;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        ImageView iv_delete = (ImageView) holder.getView(R.id.iv_itempassenger_delete);
        TextView tv_name = (TextView) holder.getView(R.id.tv_itempassenger_name);
        TextView tv_tickettype = (TextView) holder.getView(R.id.tv_itempassenger_tickettype);
        TextView tv_identitype = (TextView) holder.getView(R.id.tv_itempassenger_identitype);
        TextView tv_identinum = (TextView) holder.getView(R.id.tv_itempassenger_identinum);
        TextView tv_addchildticket = (TextView) holder.getView(R.id.tv_itempassenger_addchildticket);
        NewPassengerInfoModel passengerInfo = (NewPassengerInfoModel) getItem(position);

        tv_name.setText(passengerInfo.getAdultname());

        switch (passengerInfo.getType()) {
            case 1:
                tv_tickettype.setText("成人票");
                tv_addchildticket.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_tickettype.setText("儿童票");
                tv_addchildticket.setVisibility(View.GONE);
                break;
            case 3:
                tv_tickettype.setText("学生票");
                tv_addchildticket.setVisibility(View.GONE);
                break;
        }

        switch (passengerInfo.getCertType()) {//01:二代身份证 02第一代身份证；03护照；04港澳通行证；05台湾通行证
            case "01":
            case "02":

                tv_identitype.setText("身份证");
                break;

            case "03":
                tv_identitype.setText("护照");
                break;
            case "04":
                tv_identitype.setText("港澳通行证");
                break;
            case "05":
                tv_identitype.setText("台湾通行证");
                break;
        }


        tv_identinum.setText(passengerInfo.getCertNo());

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnUpdateListener.remove(position);
            }
        });

        tv_addchildticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.size() < 5) {
                    mOnUpdateListener.addchild(position);
                } else {
                    ToastManager.getInstance().showToast(mContext, "亲，最多添加五位乘客哦");
                }

            }
        });

        return convertView;
    }


    public interface OnUpdateListener {
        void addchild(int position);

        void remove(int position);
    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        this.mOnUpdateListener = listener;
    }


}