package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.MulItemAdapter;
import com.wxws.myticket.app.base.adapter.MulItemTypeSupport;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.TextFunUtils;

import java.util.List;

/**
 * desc: 城际快线 list
 * Date: 2016/11/4 11:07
 *
 * @auther: lixiangxiang
 */
public class CityLineListAdapter extends MulItemAdapter<TraditionBusListEntity> {

    private Context context;
    private TextView tvCityLinePrice;
    private TextView tvStartStation;
    private TextView tvEndStation;

    public CityLineListAdapter(Context context, List<TraditionBusListEntity> data) {
        super(context, data, new MulItemTypeSupport<TraditionBusListEntity>() {
            @Override
            public int getLayoutId(int position, TraditionBusListEntity traditionBusListEntity) {
                if (traditionBusListEntity.isIntroduce()){
                    return R.layout.item_bus_introduce;
                }else {
                    return  R.layout.item_city_line_list;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, TraditionBusListEntity traditionBusListEntity) {
                return position%2;
            }

        });
        this.context = context;
    }

    @Override
    public View getItemView(MulViewHolder holder, int position, View view) {
        TraditionBusListEntity traditionBusListEntity  = (TraditionBusListEntity) getItem(position);
        if (traditionBusListEntity.isIntroduce() ){
            TextView tvBusType =  holder.getView(R.id.tv_bus_type);//城际约租车
            TextView tvBusTypeDesc =  holder.getView(R.id.tv_bus_type_desc);//描述详情
            TextView tvBusTypePrice =  holder.getView(R.id.tv_bus_type_price);//价格
            ImageView imgBusType =  holder.getView(R.id.img_bus_type);//图标详情

            tvBusTypeDesc.setText(traditionBusListEntity.getDesc());
            tvBusTypePrice.setText(String.valueOf(ArithUtils.divDouble(traditionBusListEntity.getDiscountPrice(), 100)));
            tvBusType.setText(traditionBusListEntity.getTitle());

        }else{
            TextView tvStartTime =  holder.getView(R.id.tv_start_time);//大写的加粗时间
            TextView tvMoningNoon =  holder.getView(R.id.tv_morning_noon);//首班末班
            TextView tvDistance =  holder.getView(R.id.tv_distance);//当前 距离 多少
            //出发站点
            tvStartStation = holder.getView(R.id.tv_start_station);
            //到达站点
            tvEndStation = holder.getView(R.id.tv_end_station);
            TextView tvIsStartStation =  holder.getView(R.id.tv_is_start_station);//是否途径站点
            TextView tvOrigin =  holder.getView(R.id.tv_origin);//原价
            TextView tvReduce =  holder.getView(R.id.tv_reduce);//减免的价格
            //现价
            tvCityLinePrice = holder.getView(R.id.tv_city_line_price);
            TextView tvIsHave =  holder.getView(R.id.tv_is_have);//S是否有票
            TextView tvTimeAndDistance =  holder.getView(R.id.tv_time_and_distance);//耗时与距离
            TextView tvContainTicket =  holder.getView(R.id.tv_contain_ticket);//包门票
            TextView tvContainNoon =  holder.getView(R.id.tv_contain_noon);//包午餐

            if (!StringUtils.isNullOrEmpty(traditionBusListEntity.getDistanceMe() )){
                tvDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(String.format("距离当前位置%s",traditionBusListEntity.getDistanceMe()));
            }else {
                tvDistance.setVisibility(View.GONE);
            }

            if (!StringUtils.isNullOrEmpty(traditionBusListEntity.getDiscountCount())){
                tvReduce.setVisibility(View.VISIBLE);
                tvReduce.setText(traditionBusListEntity.getDiscountCount());
            }else {
                tvReduce.setVisibility(View.GONE);
            }
            tvStartTime.setText(traditionBusListEntity.getStartTime());
            tvStartStation.setText(traditionBusListEntity.getStartStationName());
            tvEndStation.setText(traditionBusListEntity.getEndStationName());

            if (traditionBusListEntity.getLabelInfoList() == null){
                tvContainTicket.setVisibility(View.GONE);
                tvContainNoon.setVisibility(View.GONE);
            }else if (traditionBusListEntity.getLabelInfoList().size() == 0){
                tvContainTicket.setVisibility(View.GONE);
                tvContainNoon.setVisibility(View.GONE);
            } else if (traditionBusListEntity.getLabelInfoList().size()==1){
                tvContainTicket.setVisibility(View.GONE);
                tvContainNoon.setVisibility(View.VISIBLE);
                tvContainNoon.setText(traditionBusListEntity.getLabelInfoList().get(0));
            }else {
                tvContainTicket.setVisibility(View.VISIBLE);
                tvContainNoon.setVisibility(View.VISIBLE);
                tvContainNoon.setText(traditionBusListEntity.getLabelInfoList().get(0));
                tvContainTicket.setText(traditionBusListEntity.getLabelInfoList().get(1));
            }

            if ("1".equals(traditionBusListEntity.getScheduleNature())) {
                tvStartTime.setVisibility(View.VISIBLE);
                tvMoningNoon.setVisibility(View.GONE);
                tvStartTime.setText(traditionBusListEntity.getStartTime());
            } else {
                tvStartTime.setVisibility(View.GONE);
                tvMoningNoon.setVisibility(View.VISIBLE);
                tvMoningNoon.setText(String.format("首班：%s 末班：%s", traditionBusListEntity.getStartTime(), traditionBusListEntity.getEndTime()));
            }

            if ("1".equals(traditionBusListEntity.getIsStartStation())){
                tvIsStartStation.setText("途径");
            }else {
                tvIsStartStation.setText("始发");
            }
            tvOrigin.setText(TextFunUtils.setDeleteText(ArithUtils.divDouble(traditionBusListEntity.getPrice().getAllPrice(),100)+""));
            tvCityLinePrice.setText(ArithUtils.divDouble(traditionBusListEntity.getDiscountPrice(),100)+"");
            tvTimeAndDistance.setText(String.format("%s公里，约%s分钟",traditionBusListEntity.getDistance(),traditionBusListEntity.getRunTime()));
            switch (traditionBusListEntity.getStatus()) {
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
