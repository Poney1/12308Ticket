package com.wxws.myticket.bus.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.POICitySelectEntity;
import com.wxws.myticket.bus.entity.POIEntity;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.List;


/**
 * desc:
 * Date: 2016-06-20 15:57
 *
 * @auther: lixiangxiang
 */
public class SearchAppointAdapter extends SimpleBaseAdapter {

    private Activity mContext;
    private int mWhere ;

    public SearchAppointAdapter(Activity context, List<POIEntity> data, int mWhere) {
        super(context, data);
        this.mContext = context;
        this.mWhere = mWhere;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_poi_search;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        final POIEntity  poiEntity= (POIEntity) getItem(position);

        TextView tvTitle = (TextView) holder.getView(R.id.tv_title);
        TextView tvAddress = (TextView) holder.getView(R.id.tv_address);
        LinearLayout layoutContainer = (LinearLayout) holder.getView(R.id.layout_container);
        if(!StringUtils.isNullOrEmpty(poiEntity.getDistrict())){
            tvTitle.setText(poiEntity.getDistrict());
        }else {
            tvTitle.setText(poiEntity.getCity());
        }
        tvAddress.setText(poiEntity.getName());
        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POICitySelectEntity appointEvent = new POICitySelectEntity(mWhere, poiEntity);
                RxBus.getInstance().post(appointEvent);
                mContext.finish();
                InputMethod.closeInputMethod(mContext);
            }
        });
        return convertView;
    }
}
