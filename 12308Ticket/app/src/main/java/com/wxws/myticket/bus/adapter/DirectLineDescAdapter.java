package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.SystemUtils;

import java.util.List;

/**
 * desc: 直通车描述说明
 * Date: 2016/11/5 09:49
 *
 * @auther: lixiangxiang
 */
public class DirectLineDescAdapter extends SimpleBaseAdapter<String> {

    private Context context ;

    public DirectLineDescAdapter(Context context, List<String> data) {
        super(context, data);
        this.context  = context;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_direct_line_desc;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        String descStr  = (String) getItem(position);
        TextView imgCount = holder.getView(R.id.img_count);
        TextView tvContent = holder.getView(R.id.tv_content);
        imgCount.setText(position+1+"");
        tvContent.setText(descStr);
        return convertView;
    }
}
