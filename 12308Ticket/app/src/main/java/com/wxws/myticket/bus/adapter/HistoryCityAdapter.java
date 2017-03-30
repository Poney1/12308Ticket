package com.wxws.myticket.bus.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.activity.CityActivity;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.bus.rxentity.CitySelectEvent;
import com.wxws.myticket.bus.rxentity.SelectCityEvent;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;

import java.util.List;



/**
 * desc: 历史记录
 * Date: 2016/8/23 18:20
 *
 * @auther: lixiangxiang
 */
public class HistoryCityAdapter extends SimpleBaseAdapter {

    private List<CityDisplayEntity> list;
    private Activity context;
    private String origin;
    protected HistoryCityAdapter(Activity context, List<CityDisplayEntity> data) {
        super(context, data);
        this.list = data;
        this.context = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_city_history;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        TextView tvHotCity = (TextView) holder.getView(R.id.tv_hot_city);
        tvHotCity.setText(list.get(position).getCity());
        tvHotCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (origin.equals(CityActivity.origin)){
                    RxBus.getInstance().post(new SelectCityEvent(list.get(position).getCity(), CityActivity.origin, list.get(position).getCityId()));
                    ListPreferencesSave.getInstance().saveSingleCityHistory(context, list.get(position),CityActivity.origin);
                    context.finish();
                }else {
                    RxBus.getInstance().post(new CitySelectEvent(list.get(position).getCity()));
                    ListPreferencesSave.getInstance().saveSingleCityHistory(context, list.get(position),origin);
                }

            }
        });
        return convertView;
    }

    public void setOrigin(String origin){
        this.origin = origin ;
    }
}
