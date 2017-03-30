package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.order.entity.OrderListResponsePara;
import com.wxws.myticket.order.utils.OrderStatusUtils;
import com.wxws.myticket.order.widgets.CommonGridView;

import java.util.List;

import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeAirCraftBus;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeAppoint;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeBus;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeCityLine;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeGangao;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeRentCar;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeTrain;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeTravel;

/**
 * desc:
 * Date: 2016-11-01 16:16
 *
 * @author jiangyan
 */
public class CommonOrderAdapter extends SimpleBaseAdapter { // 测试用数据，后续继承simpleBaseAdapter

    onButtonClickListener mListener;
    boolean flag_gray = false;

    public CommonOrderAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<OrderListResponsePara> orderList, boolean isgray) {

        flag_gray = isgray; // 失效订单直接置灰不用判断
        replaceAll(orderList);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_commonlist;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        // TODO 初始化界面并处理
        TextView tvType = (TextView) holder.getView(R.id.tv_orderlist_type);
        TextView tvStatus = (TextView) holder.getView(R.id.tv_orderlist_status);
        TextView tvOrderNum = (TextView) holder.getView(R.id.tv_orderlist_ordernum);
        TextView tvOtherInfo = (TextView) holder.getView(R.id.tv_orderlist_otherinfo);
        TextView tvStartInfo = (TextView) holder.getView(R.id.tv_orderlist_startinfo);
        TextView tvEndInfo = (TextView) holder.getView(R.id.tv_orderlist_endinfo);
        TextView tvRMB = (TextView) holder.getView(R.id.tv_orderlist_rmb);
        TextView tvPrice = (TextView) holder.getView(R.id.tv_orderlist_price);
        TextView tvStartTime = (TextView) holder.getView(R.id.tv_orderlist_starttime);
        TextView tvCheci = (TextView) holder.getView(R.id.tv_orderlist_checi);
        CommonGridView cgvTicketList = (CommonGridView) holder.getView(R.id.gv_orderlist_endorseinfo);
        Button btLeft = (Button) holder.getView(R.id.bt_orderlist_delete);
        Button btRight = (Button) holder.getView(R.id.bt_orderlist_topay);

        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 取消或删除订单

                String orderId = ((OrderListResponsePara) data.get(position)).getOrderId();
                Log.e("mudo", position + ":" + orderId);
                if (((Button) v).getText().equals("删除订单")) {
                    mListener.deleteOrder(orderId);
                } else { // 取消订单
                    mListener.cancelOrder(orderId);
                }

            }
        });

        btRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 跳转去支付
                String orderId = ((OrderListResponsePara) data.get(position)).getOrderId();
                String bizType = ((OrderListResponsePara) data.get(position)).getBizType();

                mListener.payOrder(orderId, bizType);

            }
        });

        OrderListResponsePara para = (OrderListResponsePara) data.get(position);

        switch (para.getBizType()) {
            case bizTypeBus:
                tvType.setText("汽车票");
                break;
            case bizTypeTravel:
                tvType.setText("旅游快线");// 直通车
                break;
            case bizTypeCityLine:
                tvType.setText("城际专线");// 直通车
                break;
            case bizTypeAirCraftBus:
                tvType.setText("机场大巴");// 直通车
                break;
            case bizTypeGangao:
                tvType.setText("港澳专线");// 直通车
                break;
            case bizTypeAppoint:
                tvType.setText("约租车");
                break;
            case bizTypeRentCar:
                tvType.setText("我要包车");
                break;
            case bizTypeTrain:
                tvType.setText("火车票");
                break;
        }

        tvStatus.setText(OrderStatusUtils.getOrderStatus(para.getOrderStatus()));
        tvOrderNum.setText(para.getOrderId());
        tvPrice.setText(ArithUtils.mul(Float.parseFloat(para.getOrderMoney()), 0.01) + "");

        // 右侧其他内容判断，只有包车和约租车需要
        if (para.getBizType().equals(bizTypeRentCar)) { // 包车

            tvOtherInfo.setVisibility(View.VISIBLE);
            tvOtherInfo.setText(para.getIsGoBack().equals("1") ? "往返" : "单程");

        } else if (para.getBizType().equals(bizTypeAppoint)) { // 约租车

            tvOtherInfo.setVisibility(View.VISIBLE);
            tvOtherInfo.setText(para.getVehicleType());

        } else {
            tvOtherInfo.setVisibility(View.GONE);
        }

        // 火车票处理，车次信息，车票信息，其他等修改
        if (para.getBizType().equals(bizTypeTrain)) {
//                tvStartInfo.setText(DateTimeUtil.getDateHHMM(para.getDepartTime()) + "  " + para.getFromCityName());// 出发时间加城市
//                tvEndInfo.setText(DateTimeUtil.getDateHHMM(para.getToTime()) + "  " + para.getToCityName());// 到达时间加城市
//                tvStartTime.setText(DateTimeUtil.getDateyyyyMMdd(para.getDepartTime()) + "  出发");
//
//                tvCheci.setVisibility(View.VISIBLE);
//                tvCheci.setText(para.getCheci() == null ? "" : para.getCheci());
//
//                if (para.getTicketInfos() != null && para.getTicketInfos().size() > 0) {
//                    cgvTicketList.setVisibility(View.VISIBLE);
//                    cgvTicketList.setAdapter(new TrainTicketGridViewAdapter(mContext, para.getTicketInfos()));
//                } else {
//                    cgvTicketList.setVisibility(View.GONE);
//                }

        } else if (para.getBizType().equals(bizTypeAppoint) || para.getBizType().equals(bizTypeRentCar)) {
            tvStartInfo.setText(para.getFromStationName());
            tvEndInfo.setText(para.getToStationName());
            tvStartTime.setText(para.getDepartDate() + " " + para.getDepartTime());

            tvCheci.setVisibility(View.GONE);
            cgvTicketList.setVisibility(View.GONE);
        } else {
            SpannableStringBuilder ssb = new SpannableStringBuilder(para.getFromStationName() + "-" + (para.getIsStartStation().equals("0") ? "途经" : "起始"));
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), para.getFromStationName().length(), para.getFromStationName().length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new AbsoluteSizeSpan(36), para.getFromStationName().length(), para.getFromStationName().length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvStartInfo.setText(ssb);

            tvEndInfo.setText(para.getToStationName());
            tvStartTime.setText(para.getDepartDate() + " " + para.getDepartTime());

            tvCheci.setVisibility(View.GONE);
            cgvTicketList.setVisibility(View.GONE);
        }

        //  按钮，删除订单、取消订单、去支付
        // 等待支付10、16、19有支付和取消按钮

        if (para.getOrderStatus().equals("10") || para.getOrderStatus().equals("16") || para.getOrderStatus().equals("19")) {
            btLeft.setText("取消订单");
            btRight.setText("去支付");
            btLeft.setVisibility(View.VISIBLE);
            btRight.setVisibility(View.VISIBLE);

        } else {// 订单关闭4 9 11 有删除按钮

            if (para.getOrderStatus().equals("4") || para.getOrderStatus().equals("9") || para.getOrderStatus().equals("11")) {
                btLeft.setText("删除订单");
                btLeft.setVisibility(View.VISIBLE);
            } else {
                btLeft.setVisibility(View.GONE);
            }
            btRight.setVisibility(View.GONE);
        }

        // 根据状态判断是否置灰。
        if (para.getOrderStatus().equals("4") || para.getOrderStatus().equals("9") || para.getOrderStatus().equals("11")) {
            flag_gray = true;
        } else {
            flag_gray = false;
        }

        if (flag_gray) {
            tvType.setTextColor(Color.parseColor("#999999"));
            tvStatus.setTextColor(Color.parseColor("#999999"));
            tvOrderNum.setTextColor(Color.parseColor("#999999"));
            tvStartInfo.setTextColor(Color.parseColor("#999999"));
            tvEndInfo.setTextColor(Color.parseColor("#999999"));
            tvRMB.setTextColor(Color.parseColor("#999999"));
            tvPrice.setTextColor(Color.parseColor("#999999"));
            tvStartTime.setTextColor(Color.parseColor("#999999"));
            tvCheci.setTextColor(Color.parseColor("#999999"));

        } else {

            tvType.setTextColor(Color.parseColor("#333333"));
            tvStatus.setTextColor(Color.parseColor("#F03F3F"));
            tvOrderNum.setTextColor(Color.parseColor("#666666"));
            tvStartInfo.setTextColor(Color.parseColor("#333333"));
            tvEndInfo.setTextColor(Color.parseColor("#333333"));
            tvRMB.setTextColor(Color.parseColor("#F03F3F"));
            tvPrice.setTextColor(Color.parseColor("#F03F3F"));
            tvStartTime.setTextColor(Color.parseColor("#333333"));
            tvCheci.setTextColor(Color.parseColor("#333333"));
        }


        return convertView;
    }

    public interface onButtonClickListener {
        void deleteOrder(String orderId);

        void cancelOrder(String orderId);

        void payOrder(String orderId, String bizType);
    }

    public void setOnButtonClickListener(onButtonClickListener listener) {
        mListener = listener;
    }

}