package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.my.Entity.CertificateEntity;

import java.util.List;


/**
 * desc: 编辑乘客 页面 类型选择
 * Date: 2016/8/8 15:03
 *
 * @auther: lixiangxiang
 */
public class EditPassengerTypeAdapter extends SimpleBaseAdapter {


    public EditPassengerTypeAdapter(Context context, List<CertificateEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_passenger_type;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        CertificateEntity type = (CertificateEntity) getItem(position);
        TextView tvEdit = (TextView) holder.getView(R.id.tv_type);
        tvEdit.setText(type.getPassengerType());
        return convertView;
    }
}
