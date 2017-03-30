package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.BaocheBusType;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.List;

/**
 * desc:
 * Date: 2016-12-14 14:01
 *
 * @author jiangyan
 */
public class BaocheTypeAdapter extends SimpleBaseAdapter {

    private OnCountChangeListener mCountChangeListener;

    public BaocheTypeAdapter(Context context) {
        super(context, null);
    }

    public void refreshData(List<BaocheBusType> typeList) {
        replaceAll(typeList);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_baoche_choosecar;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        SimpleDraweeView carImg = (SimpleDraweeView) holder.getView(R.id.car_img);
        TextView rentcarContent = (TextView) holder.getView(R.id.car_content);
        TextView rentcarType = (TextView) holder.getView(R.id.car_type);
        TextView rentPrice = (TextView) holder.getView(R.id.rent_price);
        TextView rentReduce = (TextView) holder.getView(R.id.rent_reduce);
        final TextView rentAcount = (TextView) holder.getView(R.id.rent_acount);
        TextView rentAdd = (TextView) holder.getView(R.id.rent_add);

        final BaocheBusType para = (BaocheBusType) data.get(position);

        carImg.setImageURI(para.getImage());
        if (!StringUtils.isNullOrEmpty(para.getRemark())) {
            rentcarContent.setText(para.getRemark());
        }
        if (!StringUtils.isNullOrEmpty(para.getName())) {
            rentcarType.setText(para.getName());
        }

        rentPrice.setText(para.getPrice() + "");
        rentAcount.setText(para.getRecommendCount() + "");

        rentReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(rentAcount.getText().toString());
                if (num <= 0) {
                    return;
                }
                num--;
                rentAcount.setText(num + "");
                para.setRecommendCount(num);
                mCountChangeListener.countChange();
            }
        });
        rentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(rentAcount.getText().toString());
                num++;

                rentAcount.setText(num + "");
                para.setRecommendCount(num);
                mCountChangeListener.countChange();
            }
        });

        return convertView;
    }

    public interface OnCountChangeListener {
        void countChange();
    }

    public void setOnCountChangeListener(OnCountChangeListener countChangeListener) {
        this.mCountChangeListener = countChangeListener;
    }
}