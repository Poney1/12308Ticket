package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.activity.GangAoCityActivity;
import com.wxws.myticket.bus.entity.GangAoStartCityEntity;

import java.util.List;

/**
 * desc: 城市
 * Date: 2016/11/10 14:29
 *
 * @auther: lixiangxiang
 */
public class GangAoCityProvinceAdapter extends SimpleBaseAdapter<GangAoStartCityEntity> {

    private Context mContext;
    public GangAoCityProvinceAdapter(Context context, List<GangAoStartCityEntity> data) {
        super(context, data);
        this.mContext = context ;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_gangao_province;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        GangAoStartCityEntity gaStartCity = (GangAoStartCityEntity) getItem(position);
        TextView tvProvince = holder.getView(R.id.tv_province);

        tvProvince.setText(gaStartCity.getName());
        if (gaStartCity.isSelect()) {
            tvProvince.setTextColor(Color.rgb(0, 176, 237));
            tvProvince.setBackgroundResource(R.drawable.gangao_bj_city);
        } else {
            tvProvince.setTextColor(Color.rgb(51, 51, 51));
            tvProvince.setBackgroundResource(R.drawable.gangao_bj_city_normal);
        }

        return convertView;
    }
}
