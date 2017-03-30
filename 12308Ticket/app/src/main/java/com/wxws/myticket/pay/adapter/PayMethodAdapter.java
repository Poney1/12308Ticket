package com.wxws.myticket.pay.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.pay.PaymentEntity;

import java.util.List;

/**
 * desc: 支付 方式选择 的 adapter
 * Date: 2016/11/3 10:57
 *
 * @auther: lixiangxiang
 */
public class PayMethodAdapter extends SimpleBaseAdapter<PaymentEntity> {

    private Context context;
    private boolean[] status;
    private int selectPosition = 0;

    public PayMethodAdapter(Context context, List<PaymentEntity> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_payment_list;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        PaymentEntity paymentEntity = (PaymentEntity) getItem(position);

        ImageView imgPayment = holder.getView(R.id.img_payment);
        TextView tvTitle = holder.getView(R.id.tv_title);
        TextView tvSubtitle = holder.getView(R.id.tv_subtitle);
        TextView rbPayment = holder.getView(R.id.tv_payment);
        LinearLayout layoutItem = holder.getView(R.id.layout_item);


        imgPayment.setImageResource(SystemUtils.getResourceId(context, paymentEntity.getChannelType().toLowerCase(), "mipmap"));
        tvTitle.setText(paymentEntity.getChannelName());
        tvSubtitle.setText(paymentEntity.getDescription());

        if (1 == paymentEntity.getStatus()) {
            tvSubtitle.setTextColor(context.getResources().getColor(R.color.app_style));
        } else {
            tvSubtitle.setTextColor(context.getResources().getColor(R.color.c_666666));
        }
        if (status[position]) {
            rbPayment.setSelected(true);
        } else {
            rbPayment.setSelected(false);
        }

        layoutItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < status.length; ++i) {
                    if (position == i) {
                        status[i] = true;
                        selectPosition = position;
                    } else {
                        status[i] = false;
                    }
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    /**
     * 刷新页面
     *
     * @param data1
     */
    public void refreshData(List<PaymentEntity> data1) {
        status = new boolean[data1.size()];
        status[0] = true;
        replaceAll(data1);
    }

    /**
     * 选择支付方式
     *
     * @return
     */
    public int getSelectPay() {
        return selectPosition;
    }
}
