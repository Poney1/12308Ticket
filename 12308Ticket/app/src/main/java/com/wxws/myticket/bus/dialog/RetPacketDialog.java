/**
 * ***************************************************************************
 * Copyright (C) 2014 ShenZhen HeShiDai Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为合时代控股有限公司开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 * ***************************************************************************
 */
package com.wxws.myticket.bus.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wxws.myticket.R;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.RoundedImageView;
import com.wxws.myticket.my.activity.H5CommonActivity;


/**
 *  首页的广告弹窗
 * @author lixiangxiang
 *         on 2015/10/12
 */
public class RetPacketDialog extends Dialog {


    private Context context;
    private ImageView mImgUrlCancel;//html url
    private RoundedImageView mImgRetUrl;//红包url
    private String retUrl;
    private String htmlUrl;
    private TextView mTvKown;
    private TextView mTvNeverKown;

    public RetPacketDialog(Context context, int theme, String retUrl, String htmlUrl) {
        super(context, theme);
        this.context =context;
        this.retUrl = retUrl ;
        this.htmlUrl = htmlUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ret_packet);
        initView();
        initData();
    }

    private void initView() {
        mImgUrlCancel = (ImageView) findViewById(R.id.img_url_cancel);
        mImgRetUrl = (RoundedImageView) findViewById(R.id.img_ret_url);
        mTvKown =(TextView)findViewById(R.id.tv_know);
        mTvNeverKown =(TextView)findViewById(R.id.tv_never_know);
        mImgRetUrl.setCornerRadius(context.getResources().getDimension(R.dimen.btn_radius));

    }

    private void initData() {
        ImageLoader.getInstance().displayImage(retUrl,mImgRetUrl);
        mImgUrlCancel.setImageResource(R.mipmap.index_img_cancel);
        mImgUrlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetPacketDialog.this.dismiss();
            }
        });
        mImgRetUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, H5CommonActivity.class);
                intent.putExtra("URL", htmlUrl);
                context.startActivity(intent);
                // 活动的点击
                MobclickAgent.onEvent(context, "AlertPacket");
                RetPacketDialog.this.dismiss();
            }
        });

        mTvKown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetPacketDialog.this.dismiss();
            }
        });

        mTvNeverKown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtils.putDataBoolean(context,retUrl,false);
                RetPacketDialog.this.dismiss();
            }
        });
    }

}
