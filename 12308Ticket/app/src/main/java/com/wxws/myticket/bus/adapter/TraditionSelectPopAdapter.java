package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.BusTraditionPopEntity;

import java.util.List;

/**
 * desc:
 * Date: 2016/10/25 19:25
 *
 * @auther: lixiangxiang
 */
public class TraditionSelectPopAdapter extends SimpleBaseAdapter<BusTraditionPopEntity>{


    public TraditionSelectPopAdapter(Context context, List<BusTraditionPopEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_trandition_pop;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        BusTraditionPopEntity busTraditionPopEntity = (BusTraditionPopEntity) getItem(position);

        TextView  tvContent = holder.getView(R.id.tv_content);//内容
        ImageView tvCancel = holder.getView(R.id.cb_cancel);//勾选

        if (busTraditionPopEntity.isSelect()){
            tvCancel.setImageResource(R.mipmap.bus_ticket_shaixuan_draw);
        }else {
            tvCancel.setImageResource(R.mipmap.bus_ticket_shaixuan_draw_normal);
        }

        tvContent.setText(busTraditionPopEntity.getName());

        return convertView;
    }
}
