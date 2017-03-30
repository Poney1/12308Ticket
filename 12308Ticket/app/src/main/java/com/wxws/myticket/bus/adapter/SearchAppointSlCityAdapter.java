package com.wxws.myticket.bus.adapter;

import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.activity.AppointSearchCityActivity;
import com.wxws.myticket.common.utils.InputMethod;

import java.util.List;

/**
 * desc: 约租车城市选择
 * user: lixiangxiang
 * date: 2016-06-22
 * time: 16:04
 */
public class SearchAppointSlCityAdapter extends SimpleBaseAdapter {

    private AppointSearchCityActivity mContext;
    private String select="";

    public SearchAppointSlCityAdapter(AppointSearchCityActivity context, List<String> data, String select) {
        super(context, data);
        this.mContext = context;
        this.select = select;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_appoint_dredge_city;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        final String item =(String) getItem(position);
        TextView mTvHotCity = (TextView) holder.getView(R.id.tv_appoint_car);
        if (item.equals(select)){
            mTvHotCity.setSelected(true);
        }else {
            mTvHotCity.setSelected(false);
        }
        mTvHotCity.setText(item);
        mTvHotCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethod.closeInputMethod(mContext);
                mContext.refreshCity(item);
            }
        });
        return convertView;
    }

    public void setSelectCity(String city){
        this.select = city;
    }
}
