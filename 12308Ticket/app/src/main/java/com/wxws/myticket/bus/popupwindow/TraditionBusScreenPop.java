package com.wxws.myticket.bus.popupwindow;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.bus.adapter.TraditionSelectPopAdapter;
import com.wxws.myticket.bus.entity.BusTraditionPopEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:  传统汽车票筛选弹窗
 * Date: 2016/10/25 16:50
 *
 * @auther: lixiangxiang
 */
public class TraditionBusScreenPop extends PopupWindow {

    private Activity activity;
    private ListView list;
    private TextView tvUpStation;
    private TextView tvDownStation;
    private LinearLayout llStation;
    private RelativeLayout llHeader;
    private ImageView imgSelect;
    private ImageView imgSelect1;
    private TextView tvRidingCancel;
    private TextView tvRidingSure;
    private View view;
    private RelativeLayout mLlDismiss;

    private List<BusTraditionPopEntity> popList;
    private TraditionSelectPopAdapter tSelectPop;

    //列表数据
    private List<BusTraditionPopEntity> popUp ;
    private List<BusTraditionPopEntity> popDown ;
    private List<BusTraditionPopEntity> popTimeSection ;
    private List<BusTraditionPopEntity> popSortPrice;
    private List<BusTraditionPopEntity> popUpCopy ;
    private List<BusTraditionPopEntity> popDownCopy;
    private List<BusTraditionPopEntity> popTimeSectionCopy ;
    private List<BusTraditionPopEntity> popSortPriceCopy ;


    private int type = 1;//1，上车点，2，下车点，3，时间，4,价格排序
    private int upAndDownType = 1;//1，上车点，2，下车点，

    private String upStation, downStation, timeSectionStart, timeSectionEnd;
    private int sortPrice;//筛选条件


    private OnFiltersDataListener onFiltersDataListener;

    public TraditionBusScreenPop(Activity activity, OnFiltersDataListener onFiltersDataListener) {
        this.activity = activity;
        this.onFiltersDataListener = onFiltersDataListener;
        initArray();
        initPopWindow();
        initPopMenu();
        initData();
        bindClick();
    }

    private void initArray() {
        popList = new ArrayList<>();
        upStation = activity.getString(R.string.bus_list_pop_unlimited);
        downStation = activity.getString(R.string.bus_list_pop_unlimited);
        timeSectionEnd = "23:59";
        timeSectionStart = "00:00";

    }

    private void bindClick() {
        mLlDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TraditionBusScreenPop.this.dismiss();
            }
        });

        tvRidingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TraditionBusScreenPop.this.dismiss();
            }
        });

        tvRidingSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TraditionBusScreenPop.this.dismiss();

                if (popSortPriceCopy == null){
                    popSortPriceCopy = new ArrayList<>();
                }else {
                    popSortPriceCopy.clear();
                }
                popSortPriceCopy.addAll(popSortPrice);

                if (popTimeSectionCopy == null){
                    popTimeSectionCopy = new ArrayList<>();
                }else {
                    popTimeSectionCopy.clear();
                }
                popTimeSectionCopy.addAll(popTimeSection);
                if (popUpCopy == null){
                    popUpCopy = new ArrayList<>();
                }else {
                    popUpCopy.clear();
                }

                popUpCopy.addAll(popUp);
                if (popDownCopy == null){
                    popDownCopy = new ArrayList<>();
                }else {
                    popDownCopy.clear();
                }

                popDownCopy.addAll(popDown);

                onFiltersDataListener.setOnFiltersDataListener(upStation, downStation, timeSectionStart, timeSectionEnd, sortPrice);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case 1:
                        upStation = popUp.get(position).getName();
                        setSelectPosition(popUp,position);
                        break;
                    case 2:
                        downStation = popDown.get(position).getName();
                        setSelectPosition(popDown,position);
                        break;
                    case 3:
                        timeSectionEnd = popTimeSection.get(position).getEndTime();
                        timeSectionStart = popTimeSection.get(position).getStartTime();
                        setSelectPosition(popTimeSection,position);
                        break;
                    case 4:
                        sortPrice = popSortPrice.get(position).getSortPrice();
                        setSelectPosition(popSortPrice,position);
                        break;
                }
            }
        });

        tvUpStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpStation(popUp);
            }
        });
        tvDownStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDownStation(popDown);
            }
        });//下车点
    }

    /**
     * 设置某个选中的item
     * @param list
     * @param position
     */
    private List<BusTraditionPopEntity>  setSelectPosition(@NonNull List<BusTraditionPopEntity> list , int position){
        for (int i =0;i<list.size();i++){
            if (i==position){
                list.get(position).setSelect(true);
            }else {
                list.get(i).setSelect(false);
            }
        }
        tSelectPop.replaceAll(list);
        return list;
    }

    private void initData() {
        tSelectPop = new TraditionSelectPopAdapter(activity, popList);
        list.setAdapter(tSelectPop);
    }

    private void initPopWindow() {
        view = activity.getLayoutInflater().inflate(R.layout.pop_bus_trandition, null);
        list = (ListView) view.findViewById(R.id.list);
        tvUpStation = (TextView) view.findViewById(R.id.tv_up_station);//上车点
        tvDownStation = (TextView) view.findViewById(R.id.tv_down_station);//下车点
        llStation = (LinearLayout) view.findViewById(R.id.ll_station);//上下车点控制是否显示
        llHeader = (RelativeLayout) view.findViewById(R.id.ll_header);//控制显示全部
        imgSelect = (ImageView) view.findViewById(R.id.img_select);//上车点选择
        imgSelect1 = (ImageView) view.findViewById(R.id.img_select_2);//下车点选择
        tvRidingCancel = (TextView) view.findViewById(R.id.tv_riding_cancel);//取消
        tvRidingSure = (TextView) view.findViewById(R.id.tv_riding_sure);//确认
        mLlDismiss = (RelativeLayout) view.findViewById(R.id.ll_dismiss);//关闭界面

    }

    /**
     * 上下车点
     *
     * @param popUp
     * @param popDown
     */
    public void setUpAndDown(List<BusTraditionPopEntity> popUp, List<BusTraditionPopEntity> popDown) {
        this.popUp = popUp;
        this.popDown = popDown;
        if (upAndDownType == 1) {
            setUpStation(popUp);
        } else if (upAndDownType == 2) {
            setDownStation(popDown);
        }
    }

    /**
     * 上车点
     *
     * @param popUp
     */
    private void setUpStation(List<BusTraditionPopEntity> popUp) {
        setUpOrDown(true);
        type = 1;
        upAndDownType = 1;
        if (popUpCopy == null) {
            popUpCopy = new ArrayList<>();
            try {
                for (BusTraditionPopEntity busTraditionPopEntity : popUp)
                    popUpCopy.add(busTraditionPopEntity.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        tSelectPop.replaceAll(popUpCopy);
    }

    /**
     * 下车点
     *
     * @param popDown
     */
    private void setDownStation(List<BusTraditionPopEntity> popDown) {
        setUpOrDown(false);
        type = 2;
        upAndDownType = 2;
        if (popDownCopy == null) {
            popDownCopy = new ArrayList<>();
            try {
                for (BusTraditionPopEntity busTraditionPopEntity : popDown)
                    popDownCopy.add(busTraditionPopEntity.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        tSelectPop.replaceAll(popDownCopy);
    }

    /**
     * 时间段
     *
     * @param popTimeSection
     */
    public void setTimeSection(List<BusTraditionPopEntity> popTimeSection) {
        this.popTimeSection = popTimeSection;
        type = 3;
        if (popTimeSectionCopy == null){
            popTimeSectionCopy = new ArrayList<>();
            try {
                for (BusTraditionPopEntity busTraditionPopEntity : popTimeSection)
                    popTimeSectionCopy.add(busTraditionPopEntity.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        tSelectPop.replaceAll(popTimeSectionCopy);
        goneUpAndDown(false);
    }

    /**
     * 时间段
     *
     * @param popSortPrice
     */
    public void setSortPrice(List<BusTraditionPopEntity> popSortPrice) {
        this.popSortPrice = popSortPrice;
        type = 4;
        if (popSortPriceCopy == null){
            popSortPriceCopy = new ArrayList<>();
            try {
                for (BusTraditionPopEntity busTraditionPopEntity : popSortPrice)
                    popSortPriceCopy.add(busTraditionPopEntity.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }


        tSelectPop.replaceAll(popSortPriceCopy);
        goneUpAndDown(false);
    }

    /**
     * @param isUp
     */
    private void setUpOrDown(boolean isUp) {
        if (isUp) {
            imgSelect.setVisibility(View.VISIBLE);
            imgSelect1.setVisibility(View.GONE);
        } else {
            imgSelect.setVisibility(View.GONE);
            imgSelect1.setVisibility(View.VISIBLE);
        }
        tvUpStation.setSelected(isUp);
        tvDownStation.setSelected(!isUp);
        goneUpAndDown(true);
    }


    /**
     * 隐藏 上下车控件
     */
    private void goneUpAndDown(boolean isVisible) {
        if (isVisible)
            llStation.setVisibility(View.VISIBLE);
        else
            llStation.setVisibility(View.GONE);
    }

    /**
     * 初始化对话框
     */
    @SuppressLint("InlinedApi")
    private void initPopMenu() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ActionBar.LayoutParams.MATCH_PARENT);

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

    public interface OnFiltersDataListener {
        void setOnFiltersDataListener(String upStation, String downStation, String timeSectionStart, String timeSectionEnd, int sortPrice);
    }
}
