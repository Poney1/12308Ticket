package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.MulItemAdapter;
import com.wxws.myticket.app.base.adapter.MulItemTypeSupport;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.activity.GangAoCityActivity;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.TextFunUtils;

import java.util.List;

/**
 * desc: 港澳快线
 * Date: 2016/11/14 09:55
 *
 * @auther: lixiangxiang
 */
public class GangAoListAdapter extends MulItemAdapter<TraditionBusListEntity> {

    private Context context;
    private TextView tvStartStation;
    private TextView tvEndStation;
    private TextView tvCityLinePrice;

    public GangAoListAdapter(Context context, List<TraditionBusListEntity> data) {
        super(context, data, new MulItemTypeSupport<TraditionBusListEntity>() {
            @Override
            public int getLayoutId(int position, TraditionBusListEntity traditionEntity) {
                if (traditionEntity.isIntroduce()) {
                    return R.layout.item_bus_introduce;
                } else {
                    return R.layout.item_gangao_list;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, TraditionBusListEntity o) {
                return position % 2;
            }
        });
        this.context = context;
    }


    @Override
    public View getItemView(MulViewHolder holder, int position, View view) {

        TraditionBusListEntity tradition = (TraditionBusListEntity) getItem(position);
        if (tradition.isIntroduce()) {
            TextView tvBusType = holder.getView(R.id.tv_bus_type);//城际约租车
            TextView tvBusTypeDesc = holder.getView(R.id.tv_bus_type_desc);//描述详情
            TextView tvBusTypePrice = holder.getView(R.id.tv_bus_type_price);//价格
            ImageView imgBusType = holder.getView(R.id.img_bus_type);//图标详情
            tvBusTypeDesc.setText(tradition.getDesc());
            tvBusTypePrice.setText(String.valueOf(ArithUtils.divDouble(tradition.getDiscountPrice(), 100)));
            tvBusType.setText(tradition.getTitle());
        } else {
            TextView tvMoningNoon = holder.getView(R.id.tv_morning_noon);//首班末班
            TextView tvStartTime = holder.getView(R.id.tv_start_time);//字体加粗
            TextView tvBizType = holder.getView(R.id.tv_biztype);//字体加粗
            //出发站点
            tvStartStation = holder.getView(R.id.tv_start_station);
            //到达站点
            tvEndStation = holder.getView(R.id.tv_end_station);
            TextView tvIsStartStation = holder.getView(R.id.tv_is_start_station);//是否途径站点
            TextView tvOrigin = holder.getView(R.id.tv_origin);//原价
            //现价
            tvCityLinePrice = holder.getView(R.id.tv_city_line_price);
            TextView tvIsHave = holder.getView(R.id.tv_is_have);//S是否有票
            TextView tvTimeAndDistance = holder.getView(R.id.tv_time_and_distance);//耗时与距离
            TextView tvContainTicket = holder.getView(R.id.tv_contain_ticket);//包门票
            TextView tvContainNoon = holder.getView(R.id.tv_contain_noon);//包午餐

            if (tradition.getLabelInfoList() == null ){
                tvContainTicket.setVisibility(View.GONE);
                tvContainNoon.setVisibility(View.GONE);
            }else if (tradition.getLabelInfoList().size() == 0){
                tvContainTicket.setVisibility(View.GONE);
                tvContainNoon.setVisibility(View.GONE);
            } else if (tradition.getLabelInfoList().size()==1){
                tvContainTicket.setVisibility(View.GONE);
                tvContainNoon.setVisibility(View.VISIBLE);
                tvContainNoon.setText(tradition.getLabelInfoList().get(0));
            }else {
                tvContainTicket.setVisibility(View.VISIBLE);
                tvContainNoon.setVisibility(View.VISIBLE);
                tvContainNoon.setText(tradition.getLabelInfoList().get(0));
                tvContainTicket.setText(tradition.getLabelInfoList().get(1));
            }
            if ("1".equals(tradition.getScheduleNature())) {
                tvBizType.setText(context.getString(R.string.bus_city_line));
                tvStartTime.setVisibility(View.VISIBLE);
                tvMoningNoon.setVisibility(View.GONE);
                tvStartTime.setText(tradition.getStartTime());
            } else {
                tvBizType.setText(context.getString(R.string.bus_city_water));
                tvStartTime.setVisibility(View.GONE);
                tvMoningNoon.setVisibility(View.VISIBLE);
                tvMoningNoon.setText(String.format("首班：%s 末班：%s", tradition.getStartTime(), tradition.getEndTime()));
            }
            tvStartStation.setText(tradition.getStartStationName());
            tvEndStation.setText(tradition.getEndStationName());
            if ("1".equals(tradition.getIsStartStation())) {
                tvIsStartStation.setText("途径");
            } else {
                tvIsStartStation.setText("始发");
            }
            tvOrigin.setText(TextFunUtils.setDeleteText(ArithUtils.divDouble(tradition.getPrice().getAllPrice(), 100) + ""));
            tvCityLinePrice.setText(ArithUtils.divDouble(tradition.getDiscountPrice(), 100) + "");
            tvTimeAndDistance.setText(String.format("%s公里，约%s分钟", tradition.getDistance(), tradition.getRunTime()));
            switch (tradition.getStatus()) {
                case Constants.busInternetStop:
                    setGray();
                    tvIsHave.setText(context.getString(R.string.bus_list_internet_stop));
                    break;
                case Constants.busHaveTicket:
                    tvIsHave.setText(context.getString(R.string.bus_list_have_ticket));
                    break;
                case Constants.busSaleAll:
                    setGray();
                    tvIsHave.setText(context.getString(R.string.bus_list_sale_all));
                    break;
                case Constants.busStop:
                    setGray();
                    tvIsHave.setText(context.getString(R.string.bus_list_stop));
                    break;
            }
        }

        return view;
    }

    /**
     * 置灰
     */
    private void setGray() {
        setDrawableLeft(tvStartStation);
        setDrawableLeft(tvEndStation);
        tvCityLinePrice.setTextColor(context.getResources().getColor(R.color.c_999999));
    }


    //设置左DrawableLeft
    private void setDrawableLeft(TextView textView) {
        Drawable drawable = context.getResources().getDrawable(R.mipmap.bus_item_gray);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }


}
