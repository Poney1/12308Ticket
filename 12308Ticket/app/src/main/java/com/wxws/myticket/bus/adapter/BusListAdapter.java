package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.serializer.IntegerCodec;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.MulItemAdapter;
import com.wxws.myticket.app.base.adapter.MulItemTypeSupport;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.StringUtils;

import java.util.List;

/**
 * desc: 汽车票列表
 * Date: 2016/11/1 16:59
 *
 * @auther: lixiangxiang
 */
public class BusListAdapter extends MulItemAdapter {

    private List<TraditionBusListEntity> data;
    private Context context;
    private TextView tvStartTime;
    private TextView tvBusTypePrice;
    private TextView tvStartStation;
    private TextView tvEndStation;

    public BusListAdapter(Context context, List<TraditionBusListEntity> data) {
        super(context, data, new MulItemTypeSupport<TraditionBusListEntity>() {

            @Override
            public int getLayoutId(int position, TraditionBusListEntity traditionBusListEntity) {
                if (traditionBusListEntity.isIntroduce()) {
                    return R.layout.item_bus_introduce;
                } else {
                    return R.layout.item_bus_traditional;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, TraditionBusListEntity traditionBusListEntity) {
                if (traditionBusListEntity.isIntroduce()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        this.data = data;
        this.context = context;
    }

    @Override
    public View getItemView(MulViewHolder holder, int position, View view) {
        TraditionBusListEntity traditionBusListEntity = (TraditionBusListEntity) getItem(position);
        if (traditionBusListEntity.isIntroduce()) {
            TextView tvBusType = (TextView) holder.getView(R.id.tv_bus_type);//城际约租车
            TextView tvBusTypeDesc = (TextView) holder.getView(R.id.tv_bus_type_desc);//描述详情
            TextView tvBusTypePriceIn = (TextView) holder.getView(R.id.tv_bus_type_price);//价格
            ImageView imgBusType = (ImageView) holder.getView(R.id.img_bus_type);//图标详情
            tvBusTypeDesc.setText(traditionBusListEntity.getDesc());
            tvBusTypePriceIn.setText(String.valueOf(ArithUtils.divDouble(traditionBusListEntity.getDiscountPrice(), 100)));
            tvBusType.setText(traditionBusListEntity.getTitle());
        } else {
            //出发时间
            tvStartTime = (TextView) holder.getView(R.id.tv_start_time);
            //出发站点
            tvStartStation = (TextView) holder.getView(R.id.tv_start_station);
            //到达站点
            tvEndStation = (TextView) holder.getView(R.id.tv_end_station);
            TextView tvBusModel = (TextView) holder.getView(R.id.tv_bus_model);//车型tv_start_station
            TextView tvTicketOrigin = (TextView) holder.getView(R.id.tv_ticket_price_origin);//原价
            //折后价
            tvBusTypePrice = (TextView) holder.getView(R.id.tv_bus_type_price);
            TextView tvHaveTicket = (TextView) holder.getView(R.id.tv_have_ticket);//有票
            TextView tvRunTime = (TextView) holder.getView(R.id.tv_run_time);//运行时间

            tvStartTime.setText(traditionBusListEntity.getStartTime());
            tvStartStation.setText(traditionBusListEntity.getStartStationName());
            tvEndStation.setText(traditionBusListEntity.getEndStationName());
            tvBusModel.setText(traditionBusListEntity.getSeatType());
            tvTicketOrigin.setText(String.valueOf(ArithUtils.divDouble(traditionBusListEntity.getPrice().getAllPrice(), 100)));
            tvBusTypePrice.setText(String.valueOf(ArithUtils.divDouble(traditionBusListEntity.getDiscountPrice(), 100)));
            if (!StringUtils.isNullOrEmpty(traditionBusListEntity.getRunTime()))
                tvRunTime.setText(String.format(context.getString(R.string.bus_list_run_time), aboutHour(Integer.parseInt(traditionBusListEntity.getRunTime()))));
            switch (traditionBusListEntity.getStatus()) {
                case Constants.busInternetStop:
                    setGray();
                    tvHaveTicket.setText(context.getString(R.string.bus_list_internet_stop));
                    break;
                case Constants.busHaveTicket:
                    tvHaveTicket.setText(context.getString(R.string.bus_list_have_ticket));
                    break;
                case Constants.busSaleAll:
                    setGray();
                    tvHaveTicket.setText(context.getString(R.string.bus_list_sale_all));
                    break;
                case Constants.busStop:
                    setGray();
                    tvHaveTicket.setText(context.getString(R.string.bus_list_stop));
                    break;
            }
        }

        return view;
    }

    //设置左DrawableLeft
    private void setDrawableLeft(TextView textView) {
        Drawable drawable = context.getResources().getDrawable(R.mipmap.bus_item_gray);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 小时
     *
     * @param runTime 分钟
     * @return
     */
    private String aboutHour(int runTime) {

        float hour = ((float) runTime) / 60;
        return StringUtils.decimals(String.valueOf(hour), 1);
    }


    /**
     * 置灰
     */
    private void setGray() {
        setDrawableLeft(tvStartStation);
        setDrawableLeft(tvEndStation);
        tvStartTime.setTextColor(context.getResources().getColor(R.color.c_999999));
        tvBusTypePrice.setTextColor(context.getResources().getColor(R.color.c_999999));
    }
}
