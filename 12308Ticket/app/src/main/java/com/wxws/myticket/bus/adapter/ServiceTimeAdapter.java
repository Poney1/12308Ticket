package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.LowestPriceRentCarEntity;

import java.util.List;

/**
 * desc: 约租车无服务时间
 * Date: 2016-06-29 10:25
 *
 * @auther: lixiangxiang
 */
public class ServiceTimeAdapter extends SimpleBaseAdapter {

    public ServiceTimeAdapter(Context context, List<LowestPriceRentCarEntity.ServiceTimeEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_service_time;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        LowestPriceRentCarEntity.ServiceTimeEntity  serviceTimeEntity = (LowestPriceRentCarEntity.ServiceTimeEntity) getItem(position);

        String serviceTime = String.format("%s ~ %s",serviceTimeEntity.getStartTime(),serviceTimeEntity.getEndTime());

        TextView tvStartTime = (TextView) holder.getView(R.id.start_time);

        tvStartTime.setText(serviceTime);

        return convertView;
    }
}
