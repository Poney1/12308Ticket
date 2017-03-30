package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.interfaces.BusUpOrDownCallback;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.train.entity.SelectBusEntity;

import java.util.List;

/**
 * desc: 选择上车点下车点
 * user: lixiangxiang
 * date: 2016-03-31
 * time: 09:00
 */
public class SelectBusDownAdapter extends SimpleBaseAdapter {

    private List<SelectBusEntity> data2;

    private BusUpOrDownCallback onBsd;

    public SelectBusDownAdapter(Context context, List<SelectBusEntity> data, BusUpOrDownCallback onBsd) {
        super(context, data);
        this.onBsd = onBsd;
        this.data2 = data;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_trandition_pop;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        TextView tvContent = (TextView) holder.getView(R.id.tv_content);

        ImageView checkBox = (ImageView) holder.getView(R.id.cb_cancel);

        SelectBusEntity slt = (SelectBusEntity) getItem(position);

        if (!StringUtils.isNullOrEmpty(slt.getStation())) {
            tvContent.setText(slt.getStation());
        } else if (!StringUtils.isNullOrEmpty(slt.getPrice())) {
            tvContent.setText(slt.getPrice());
        } else if (!StringUtils.isNullOrEmpty(slt.getTicketType())) {
            switch (slt.getTicketType()) {
                case "-1":
                    tvContent.setText("不限");
                    break;
                case "0":
                    tvContent.setText("汽车票");
                    break;
                case "1":
                    tvContent.setText("旅游快线");
                    break;
                case "2":
                    tvContent.setText("城际快线");
                    break;
                case "3":
                    tvContent.setText("机场快线");
                    break;
                case "4":
                    tvContent.setText("港澳快线");
                    break;
            }
        }
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSelectBus(position);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSelectBus(position);
            }
        });


        if (slt.getIsSelected() == 1) {
            checkBox.setBackgroundResource(R.mipmap.bus_ticket_shaixuan_draw);
        } else {
            checkBox.setBackgroundResource(R.mipmap.bus_ticket_shaixuan_draw_normal);
        }


        return convertView;
    }

    private void doSelectBus(int position) {

        for (int i = 0; i < data2.size(); i++) {
            if (position == i) {
                data2.get(position).setIsSelected(1);
            } else {
                data2.get(i).setIsSelected(0);
            }
        }
        onBsd.onScreenBus(data2, position);//回调
    }

    //改变源数据
    public void changeList(List<SelectBusEntity> data2) {
        this.data2 = data2;
    }
}
