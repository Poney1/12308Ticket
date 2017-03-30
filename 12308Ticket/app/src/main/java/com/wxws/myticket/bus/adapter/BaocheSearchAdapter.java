package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.entity.BaocheCityInfo;
import com.wxws.myticket.bus.entity.GetMapCityEvent;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.InputMethod;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-18 16:05
 *
 * @author jiangyan
 */
public class BaocheSearchAdapter extends BaseAdapter {
    private Context _context;
    private List<BaocheCityInfo> citys;
    private int titleId;
    private int schedule_type;
    private boolean isMapCity = false;

    public BaocheSearchAdapter(Context context, boolean isMapCity) {
        _context = context;
        this.isMapCity = isMapCity;
    }

    @Override
    public int getCount() {
        return citys == null ? 0 : citys.size();
    }

    public void reloadData(List<BaocheCityInfo> citys) {
        this.citys = citys;
        notifyDataSetChanged();
    }

    @Override
    public BaocheCityInfo getItem(int position) {
        return citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(_context).inflate(R.layout.item_searchcity_list, parent, false);
            holder = new ViewHolder();
            holder.tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BaocheCityInfo item = getItem(position);
        holder.tvCityName.setText(item.getCityName());

        holder.tvCityName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 不是点击组名则跳转返回
                if (!item.isGroup()) {
                    if (isMapCity) {
                        RxBus.getInstance().post(new GetMapCityEvent(item.getCityName()));
                    }
                    ((BaseActivity) _context).finish();
                    InputMethod.closeInputMethod((BaseActivity)_context);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tvCityName;
    }
}