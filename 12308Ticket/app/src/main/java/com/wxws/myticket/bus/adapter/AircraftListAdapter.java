package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.TraditionBusListEntity;

import java.util.List;

/**
 * desc: 机场快线列表
 * Date: 2016/11/10 19:24
 *
 * @auther: lixiangxiang
 */
public class AircraftListAdapter extends SimpleBaseAdapter<TraditionBusListEntity> {

    private RelativeLayout mRlTime;
    private TextView mTvMorningNoon;
    private TextView mTvStartTime;
    private TextView mTvBiztype;
    private LinearLayout mLlCoupon;
    private TextView mTvExplain1;
    private TextView mTvExplain2;
    private RelativeLayout mRlImg;
    private ImageView mImgUp;
    private ImageView mImgVertical;
    private TextView mTvStartStation;
    private TextView mTvIsStartStation;
    private TextView mTvEndStation;
    private TextView mTvOrigin;
    private TextView mTvYuan;
    private TextView mTvCityLinePrice;
    private View mViewLine;
    private TextView mTvTimeAndDistance;

    public AircraftListAdapter(Context context, List<TraditionBusListEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_aircraft_list;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        mRlTime =  holder.getView(R.id.rl_time);
        mTvMorningNoon =  holder.getView(R.id.tv_morning_noon);
        mTvStartTime =  holder.getView(R.id.tv_start_time);
        mTvBiztype =  holder.getView(R.id.tv_biztype);
        mLlCoupon =  holder.getView(R.id.ll_coupon);
        mTvExplain1 =  holder.getView(R.id.tv_explain1);
        mTvExplain2 =  holder.getView(R.id.tv_explain2);
        mRlImg =  holder.getView(R.id.rl_img);
        mImgUp =  holder.getView(R.id.img_up);
        mImgVertical = holder.getView(R.id.img_vertical);
        mTvStartStation = holder.getView(R.id.tv_start_station);
        mTvIsStartStation =  holder.getView(R.id.tv_is_start_station);
        mTvEndStation =  holder.getView(R.id.tv_end_station);
        mTvOrigin =  holder.getView(R.id.tv_origin);
        mTvYuan =  holder.getView(R.id.tv_yuan);
        mTvCityLinePrice =  holder.getView(R.id.tv_city_line_price);
        mViewLine = holder.getView(R.id.view_line);
        mTvTimeAndDistance = holder.getView(R.id.tv_time_and_distance);

        return convertView;
    }



}
