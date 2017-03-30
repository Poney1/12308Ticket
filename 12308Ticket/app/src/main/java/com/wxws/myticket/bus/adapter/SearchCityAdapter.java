package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.activity.CityActivity;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.bus.rxentity.SelectCityEvent;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;

import java.util.List;

/**
 * desc: 搜索后城市
 * Date: 2016/11/24 10:37
 *
 * @auther: lixiangxiang
 */
public class SearchCityAdapter  extends SimpleBaseAdapter<CityDisplayEntity> {

    private Context context;
    private String origin;
    public SearchCityAdapter(Context context, List<CityDisplayEntity> data) {
        super(context, data);
        this.context = context ;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_recycler_city;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

       final CityDisplayEntity cityDisplayEntity = (CityDisplayEntity) getItem(position);
        TextView  tvShowFirst = holder.getView(R.id.tv_show_first);
        TextView  tvShowCity = holder.getView(R.id.tv_show_city);

        if (cityDisplayEntity.isGroup()){
            tvShowFirst.setVisibility(View.VISIBLE);
            tvShowCity.setVisibility(View.GONE);
            tvShowFirst.setText(cityDisplayEntity.getCity());
        }else {
            tvShowFirst.setVisibility(View.GONE);
            tvShowCity.setVisibility(View.VISIBLE);
            tvShowCity.setText(cityDisplayEntity.getCity());
        }

        tvShowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListPreferencesSave.getInstance().saveSingleCityHistory(context, cityDisplayEntity, CityActivity.origin);
                RxBus.getInstance().post(new SelectCityEvent(cityDisplayEntity.getCity(),CityActivity.origin, cityDisplayEntity.getCityId()));
                ((BaseActivity)context).finish();
            }
        });
        return convertView;
    }
    //设置来源
    public void setOrigin(String origin){
        this.origin = origin;
    }
}
