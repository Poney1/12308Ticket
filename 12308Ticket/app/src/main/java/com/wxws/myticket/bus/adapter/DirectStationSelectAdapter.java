package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.DirectStopoverStationList;

import java.util.List;

/**
 * desc: 站点
 * Date: 2016/11/4 17:45
 *
 * @auther: lixiangxiang
 */
public class DirectStationSelectAdapter extends SimpleBaseAdapter<DirectStopoverStationList> {


    private  List<DirectStopoverStationList> data;
    private  boolean  is;//

    public DirectStationSelectAdapter(Context context, List<DirectStopoverStationList> data) {
        super(context, data);
        this.data = data ;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_direct_desc_up_down;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        DirectStopoverStationList direct = (DirectStopoverStationList) getItem(position);
        ImageView imageView = holder.getView(R.id.img_type);
        ImageView imgTop = holder.getView(R.id.img_top);
        ImageView imgBottom = holder.getView(R.id.img_bottom);
        ImageView imgSelect = holder.getView(R.id.img_select);
        TextView tvStation = holder.getView(R.id.tv_station);

        if (is &&position == 0) {
            imgTop.setVisibility(View.GONE);
            imageView.setImageResource(R.mipmap.direct_start);
        }else if (!is && position == data.size() -1 ){
            imgBottom.setVisibility(View.GONE);
            imageView.setImageResource(R.mipmap.direct_end);
        }else if ("0".equals(data.get(position).getType())){
            //上车点
        }else if ("1".equals(data.get(position).getType())){
            //下车点
        }
        if (data !=null && data.size() >1 && position == data.size()-1){
            imgBottom.setVisibility(View.GONE);
        }
        if (direct.isSelect()){
            imgSelect.setImageResource(R.mipmap.bus_ticket_shaixuan_draw);
        }else {
            imgSelect.setImageResource(R.mipmap.bus_ticket_shaixuan_draw_normal);
        }

        tvStation.setText(direct.getStationName());
        return convertView;
    }

    /**
     * 是否上车 列表
     * @param is
     */
    public void isUp(boolean is){
        this.is = is ;
    }
}
