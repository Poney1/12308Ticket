package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.my.Entity.CouponResponsePara;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-04 16:05
 *
 * @author jiangyan
 */
public class CouponAdapter extends SimpleBaseAdapter {

    private Context mContext;

    public CouponAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<CouponResponsePara> data) {
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_coupon;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        //TODO 数据？
        LinearLayout layoutCoupon = (LinearLayout) holder.getView(R.id.layout_coupon);
        TextView tvPrice = (TextView) holder.getView(R.id.tv_coupon_price);
        TextView tvYuan = (TextView) holder.getView(R.id.tv_coupon_yuan);
        TextView tvLimit = (TextView) holder.getView(R.id.tv_coupon_limit);
        TextView tvDetails = (TextView) holder.getView(R.id.tv_coupon_detail);
        TextView tvBetime = (TextView) holder.getView(R.id.tv_coupon_betime);
        TextView tvEntime = (TextView) holder.getView(R.id.tv_coupon_entime);
        TextView tvPlatform = (TextView) holder.getView(R.id.tv_coupon_platform);
        TextView tvUsed = (TextView) holder.getView(R.id.tv_coupon_used);

        CouponResponsePara para = (CouponResponsePara) data.get(position);

        if (para.getType().equals("1")) {//1 为面额，2为折扣
            tvPrice.setText(para.getCoinsAmt());
            tvYuan.setText("元");
            tvLimit.setText(String.format("满%s可用", para.getCoinsThresholdAmt()));
        } else {
            tvPrice.setText(StringUtils.mul(para.getDisctVal(), "0.1"));
            tvYuan.setText("折");
            tvLimit.setText(String.format("最高优惠%s元", para.getDisctMaxAmt()));
        }
        tvDetails.setText(para.getName());
        tvBetime.setText(DateTimeUtil.getDateyyyyMMdd(para.getCreateTime()));
        tvEntime.setText("至" + DateTimeUtil.getDateyyyyMMdd(para.getExpireTime()));

        switch (para.getStatus()) {
            case "0":// 已使用
                tvUsed.setText("已使用");
                layoutCoupon.setBackgroundResource(R.drawable.coupon_bg_unable);
                tvPrice.setTextColor(Color.rgb(153, 153, 153));
                tvYuan.setTextColor(Color.rgb(153, 153, 153));
                tvLimit.setTextColor(Color.rgb(153, 153, 153));

                break;
            case "1":// 可使用
                tvUsed.setText("可使用");
                layoutCoupon.setBackgroundResource(R.drawable.coupon_bg);
                tvPrice.setTextColor(Color.rgb(240, 63, 63));
                tvYuan.setTextColor(Color.rgb(240, 63, 63));
                tvLimit.setTextColor(Color.rgb(240, 63, 63));
                break;
            case "2":// 已过期
                tvUsed.setText("已过期");

                layoutCoupon.setBackgroundResource(R.drawable.coupon_bg_unable);
                tvPrice.setTextColor(Color.rgb(153, 153, 153));
                tvYuan.setTextColor(Color.rgb(153, 153, 153));
                tvLimit.setTextColor(Color.rgb(153, 153, 153));
                break;
            case "3":// 不适用
                tvUsed.setText("不适用");
                layoutCoupon.setBackgroundResource(R.drawable.coupon_bg_unable);
                tvPrice.setTextColor(Color.rgb(153, 153, 153));
                tvYuan.setTextColor(Color.rgb(153, 153, 153));
                tvLimit.setTextColor(Color.rgb(153, 153, 153));

                break;
            default:
        }

        String platStr = "";
        for (int i = 0; i < para.getPlatform().size(); i++) {

            if (!platStr.contains(para.getPlatform().get(i).getPlatform())) {

                platStr += para.getPlatform().get(i).getPlatform();
                if (i != para.getPlatform().size() - 1) {
                    platStr += "、";
                }
            }

        }
        if (para.getPlatform().size() == 1) {
            tvPlatform.setText(platStr + "专用");
        } else if (para.getPlatform().size() == 3) {
            tvPlatform.setText("不限");
        } else {
            tvPlatform.setText(platStr);
        }

        return convertView;
    }
}