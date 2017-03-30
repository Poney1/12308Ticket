package com.wxws.myticket.train.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;
import com.wxws.myticket.train.activity.TrainCityActivity;
import com.wxws.myticket.train.entity.TrainRealCityModel;
import com.wxws.myticket.train.rxentity.TrainCityEvent;

import java.util.List;


/**
 * desc: 历史记录
 * Date: 2016/8/23 18:20
 *
 * @auther: lixiangxiang
 */
public class TrainHistoryAdapter extends SimpleBaseAdapter {

    private List<TrainRealCityModel> list;
    private Activity context;

    protected TrainHistoryAdapter(Activity context, List<TrainRealCityModel> data) {
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
                RxBus.getInstance().post(new TrainCityEvent(TrainCityActivity.SELECTCITY, list.get(position).getCity(), list.get(position).getCode()));
                ListPreferencesSave.getInstance().saveTrainCityHistory(context, list.get(position));
                context.finish();
            }
        });
        return convertView;
    }
}
