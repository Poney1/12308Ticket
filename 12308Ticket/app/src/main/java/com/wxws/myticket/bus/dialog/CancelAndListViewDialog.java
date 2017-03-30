package com.wxws.myticket.bus.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.bus.adapter.ServiceTimeAdapter;
import com.wxws.myticket.bus.entity.LowestPriceRentCarEntity;

import java.util.List;


/**
 *  约租车弹出服务时间
 *
 * @author lixiangxiang
 *         on 2015/10/12
 */
public class CancelAndListViewDialog extends Dialog {


    private Context context;
    private ImageView mImgUrlCancel;//html url
    private ListView mLsTime;
    private TextView mTvDes;
    private TextView mTvDesTime;
    private List<LowestPriceRentCarEntity.ServiceTimeEntity> lowest;


    public CancelAndListViewDialog(Context context, int theme, List<LowestPriceRentCarEntity.ServiceTimeEntity> lowest) {
        super(context, theme);
        this.context =context;
        this.lowest = lowest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cancel_listview);
        initView();
        initData();
    }

    private void initView() {
        this.setCanceledOnTouchOutside(true);
        mImgUrlCancel = (ImageView) findViewById(R.id.img_url_cancel);
        mTvDes = (TextView) findViewById(R.id.tv_des);
        mLsTime = (ListView) findViewById(R.id.ls_time);
        mTvDesTime = (TextView) findViewById(R.id.tv_des_time);
    }

    private void initData() {
        ServiceTimeAdapter  serviceTimeAdapter = new ServiceTimeAdapter(context,lowest);
        mLsTime.setAdapter(serviceTimeAdapter);
        mImgUrlCancel.setImageResource(R.mipmap.bus_img_cancel);
        mImgUrlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelAndListViewDialog.this.dismiss();
            }
        });

    }

}
