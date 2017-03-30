package com.wxws.myticket.train.popwindow;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.common.interfaces.BusUpOrDownCallback;
import com.wxws.myticket.common.interfaces.NotifyAllBus;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.train.adapter.SelectBusDownAdapter;
import com.wxws.myticket.train.entity.SelectBusEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 车站
 *
 * @author ljf
 * @version 2014年7月30日 上午11:04:52
 */
public class StationPopWindow extends PopupWindow {
    FragmentActivity activity;
    protected View view;
    private ListView list;
    private SelectBusDownAdapter mAdapter;
    private List<SelectBusEntity> titles = new ArrayList<>();
    private BusUpOrDownCallback onBusUpOrDownCallback;
    private TextView tvRidingStart;
    private NotifyAllBus notifyAllBus;
    private RelativeLayout mLlDismiss;
    private TextView tvUpStation;
    private TextView tvDownStation;
    private LinearLayout llStation;
    private ImageView imgSelect;
    private ImageView imgSelect1;

    public final  static  int  TICKET_ALL = 0;//所有数据
    public final  static  int  TICKET_UP = 1;//上车点
    public final  static  int  TICKET_DOWN = 2;//下车点
    private LinearLayout llHeader;


    public StationPopWindow(FragmentActivity activity, List<SelectBusEntity> titles, BusUpOrDownCallback onBusUpOrDownCallback) {
        super(activity);
        this.activity = activity;
        this.titles = titles;
        this.onBusUpOrDownCallback = onBusUpOrDownCallback;
        bindViews();
        initPopMenu();
    }

    public void setOnNotifyData(NotifyAllBus notifyAllBus) {
        this.notifyAllBus = notifyAllBus;
    }


    private void bindViews() {
        view = activity.getLayoutInflater().inflate(R.layout.pop_ridingtime, null);
        list = (ListView) view.findViewById(R.id.list);
        tvUpStation = (TextView) view.findViewById(R.id.tv_up_station);//上车点
        tvDownStation = (TextView) view.findViewById(R.id.tv_down_station);//下车点
        llStation = (LinearLayout) view.findViewById(R.id.ll_station);//下车点
        llHeader = (LinearLayout) view.findViewById(R.id.ll_header);//控制显示全部
        imgSelect = (ImageView) view.findViewById(R.id.img_select);//上车点选择
        imgSelect1 = (ImageView) view.findViewById(R.id.img_select_2);//下车点选择

        if(titles!=null && titles.size()>0 && !StringUtils.isNullOrEmpty(titles.get(0).getStation())){
            llStation.setVisibility(View.VISIBLE);
        }
        tvUpStation.setSelected(true);
        mAdapter = new SelectBusDownAdapter(activity, titles, onBusUpOrDownCallback);
        list.setAdapter(mAdapter);
        mLlDismiss = (RelativeLayout) view.findViewById(R.id.ll_dismiss);
        mLlDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StationPopWindow.this.dismiss();
            }
        });
        tvRidingStart = (TextView) view.findViewById(R.id.tv_riding_start);
        tvRidingStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAllBus.notifyData(TICKET_ALL);
                StationPopWindow.this.dismiss();
            }
        });
        tvDownStation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAllBus.notifyData(TICKET_DOWN);
                showStation(false);
            }
        });
        tvUpStation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAllBus.notifyData(TICKET_UP);
                showStation(true);
            }
        });
        showStation(true);
    }

    //展示 上下车点
    private void showStation(boolean isShow){
        if (isShow){
            imgSelect.setVisibility(View.VISIBLE);
            imgSelect1.setVisibility(View.GONE);
        }else {
            imgSelect.setVisibility(View.GONE);
            imgSelect1.setVisibility(View.VISIBLE);
        }
        tvUpStation.setSelected(isShow);
        tvDownStation.setSelected(!isShow);
    }
    //刷新数据
    public void reFreshListView(List<SelectBusEntity> titles) {
        this.titles = titles;
        mAdapter.changeList(titles);
        mAdapter.replaceAll(titles);
    }

    /**
     * 初始化对话框
     */
    @SuppressLint("InlinedApi")
    private void initPopMenu() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(false);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.argb(39, 0, 0, 0));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 打开或者关闭弹出框
     *
     * @param v
     */
    public void showPop(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        int height = v.getMeasuredHeight();

        Rect outRect = new Rect();
        if (activity != null && !activity.isFinishing()) {
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

            this.setHeight(outRect.height() - height);

            showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, height);
        }
    }

}
