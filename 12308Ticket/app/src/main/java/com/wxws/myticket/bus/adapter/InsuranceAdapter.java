package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.BusInsuranceEntity;
import com.wxws.myticket.common.utils.ArithUtils;
import com.wxws.myticket.common.utils.function.TextFunUtils;

import java.util.List;

/**
 * desc: 保险列表
 * Date: 2016/12/9 17:01
 *
 * @auther: lixiangxiang
 */
public class InsuranceAdapter extends SimpleBaseAdapter {

    private TextView tvInsurancePrice;
    private TextView tvInsuranceDesc;
    private ImageView imgSelect;
    private Context  mContext;

    public InsuranceAdapter(Context context, List<BusInsuranceEntity> data) {
        super(context, data);
        this.mContext = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_insurance;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        BusInsuranceEntity busInsuranceEntity = (BusInsuranceEntity) getItem(position);
        tvInsurancePrice = (TextView) holder.getView(R.id.tv_insurance_price);
        tvInsuranceDesc = (TextView) holder.getView(R.id.tv_insurance_desc);
        imgSelect = (ImageView) holder.getView(R.id.img_select);
        tvInsuranceDesc.setText(busInsuranceEntity.getInsuranceDesc());
        String name = String.format(mContext.getString(R.string.train_desc),busInsuranceEntity.getInsuranceName(),
                ArithUtils.div(busInsuranceEntity.getMoney(),100.00)+"");
        tvInsurancePrice.setText(TextFunUtils.spanColor(name,busInsuranceEntity.getInsuranceName(),mContext.getResources().getColor(R.color.app_style)));
        if (busInsuranceEntity.isSelect()){
            imgSelect.setImageResource(R.mipmap.bus_ticket_shaixuan_draw);
        }else {
            imgSelect.setImageResource(R.mipmap.bus_ticket_shaixuan_draw_normal);
        }
        return convertView;
    }

}
