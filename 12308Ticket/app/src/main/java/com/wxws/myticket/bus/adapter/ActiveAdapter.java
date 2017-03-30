package com.wxws.myticket.bus.adapter;

import android.content.Context;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.bus.entity.ActiveEntity;
import com.wxws.myticket.common.widgets.view.RoundedImageView;

import java.util.List;

/**
 * desc:  活动 广告
 * Date: 2016/12/5 11:09
 *
 * @auther: lixiangxiang
 */
public class ActiveAdapter  extends SimpleBaseAdapter<ActiveEntity>{


    public ActiveAdapter(Context context, List<ActiveEntity> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_active;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        RoundedImageView imageView = holder.getView(R.id.img_active);
        imageView.setCornerRadius(20);
        ActiveEntity url = (ActiveEntity) getItem(position);
        ImageLoader.getInstance().displayImage(url.getImageUrl(), imageView);
        return convertView;
    }
}
