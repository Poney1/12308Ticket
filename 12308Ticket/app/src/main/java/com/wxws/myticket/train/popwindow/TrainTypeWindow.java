package com.wxws.myticket.train.popwindow;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.common.widgets.view.GridViewForScrollView;
import com.wxws.myticket.train.adapter.TrainSeatTypeAdapter;
import com.wxws.myticket.train.adapter.TrainSelectTypeAdapter;
import com.wxws.myticket.train.entity.PopTimeSelectModel;
import com.wxws.myticket.train.entity.SeatSelectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 火车票类型选择
 * Date: 2016/11/17 14:06
 *
 * @auther: lixiangxiang
 */
public class TrainTypeWindow extends PopupWindow {
    private Activity activity;
    private View view;
    private LinearLayout llDismiss;
    private TextView tvCancel;
    private TextView tvSure;
    private LinearLayout llHeader;
    private GridViewForScrollView gridType;
    private GridViewForScrollView gridSeat;
    private GridViewForScrollView gridStation;
    private GridViewForScrollView gridStationDown;
    private GridViewForScrollView gridStartTime;
    private GridViewForScrollView gridArriveTime;

    private List<PopTimeSelectModel> listType;//车型
    private List<PopTimeSelectModel> listTypeCopy;//车型
    private List<SeatSelectModel> listSeat;//座位
    private List<SeatSelectModel> listSeatCopy;//座位
    private List<PopTimeSelectModel> listStation;//上车点
    private List<PopTimeSelectModel> listStationCopy;//上车点
    private List<PopTimeSelectModel> listDown;//下车点
    private List<PopTimeSelectModel> listDownCopy;//下车点
    private List<PopTimeSelectModel> startTime;//出发时间
    private List<PopTimeSelectModel> startTimeCopy;//出发时间
    private List<PopTimeSelectModel> endTime;//到达时间
    private List<PopTimeSelectModel> endTimeCopy;//到达时间

    //选车类型
    private List<String> trainType;
    private List<String> trainTypeCopy;
    private List<String> trainSeat;
    private List<String> trainSeatCopy;
    private List<String> trainStart;
    private List<String> trainStartCopy;
    private List<String> trainEndStation;
    private List<String> trainEndStationCopy;
    private List<String> trainStartTime;
    private List<String> trainStartTimeCopy;
    private List<String> trainArriveTime;
    private List<String> trainArriveTimeCopy;
    private OnTypeParameterSelect onTypeParamListener;//参数
    //
    private TrainSelectTypeAdapter trainTypeAdapter;
    private TrainSeatTypeAdapter trainSeatAdapter;
    private TrainSelectTypeAdapter trainUpAdapter;
    private TrainSelectTypeAdapter trainDownAdapter;
    private TrainSelectTypeAdapter trainStartAdapter;
    private TrainSelectTypeAdapter trainEndAdapter;

    private boolean listChangeCount = true;//第一次缓存数据

    public TrainTypeWindow(Activity activity) {

        this.activity = activity;
        this.listType = new ArrayList<>();
        this.listSeat = new ArrayList<>();
        this.listStation = new ArrayList<>();
        this.listDown = new ArrayList<>();
        this.startTime = new ArrayList<>();
        this.endTime = new ArrayList<>();

        listTypeCopy = new ArrayList<>();
        listSeatCopy = new ArrayList<>();
        listStationCopy = new ArrayList<>();
        listDownCopy = new ArrayList<>();
        startTimeCopy = new ArrayList<>();
        endTimeCopy = new ArrayList<>();

        bindViews();

        bindData();

        bindClick();

        initPopMenu();

    }

    public void setOnTypeParamListener(OnTypeParameterSelect onTypeParamListener) {
        this.onTypeParamListener = onTypeParamListener;
    }

    private void bindData() {

        trainTypeAdapter = new TrainSelectTypeAdapter(activity, listType);
        trainSeatAdapter = new TrainSeatTypeAdapter(activity, listSeat);
        trainUpAdapter = new TrainSelectTypeAdapter(activity, listStation);
        trainDownAdapter = new TrainSelectTypeAdapter(activity, listDown);
        trainStartAdapter = new TrainSelectTypeAdapter(activity, startTime);
        trainEndAdapter = new TrainSelectTypeAdapter(activity, endTime);

        gridType.setAdapter(trainTypeAdapter);
        gridSeat.setAdapter(trainSeatAdapter);
        gridStation.setAdapter(trainUpAdapter);
        gridStationDown.setAdapter(trainDownAdapter);
        gridArriveTime.setAdapter(trainEndAdapter);
        gridStartTime.setAdapter(trainStartAdapter);


        trainType = new ArrayList<>();
        trainSeat = new ArrayList<>();
        trainStart = new ArrayList<>();
        trainEndStation = new ArrayList<>();
        trainArriveTime = new ArrayList<>();
        trainStartTime = new ArrayList<>();

        trainTypeCopy = new ArrayList<>();
        trainSeatCopy = new ArrayList<>();
        trainStartCopy = new ArrayList<>();
        trainEndStationCopy = new ArrayList<>();
        trainArriveTimeCopy = new ArrayList<>();
        trainStartTimeCopy = new ArrayList<>();
    }

    private void bindClick() {

        llDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainTypeWindow.this.dismiss();
            }
        });
        gridType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                trainType = removeRepeatItem(trainType, listType.get(position).getName());
                if (!listType.get(position).isSelect()) {
                    listType.get(position).setSelect(true);
                } else {
                    listType.get(position).setSelect(false);
                }
                trainTypeAdapter.notifyDataSetChanged();
            }
        });

        gridSeat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainSeat = removeRepeatItem(trainSeat, listSeat.get(position).getName());
                if (!listSeat.get(position).getIsSelect()) {
                    listSeat.get(position).setIsSelect(true);
                } else {
                    listSeat.get(position).setIsSelect(false);
                }
                trainSeatAdapter.notifyDataSetChanged();
            }
        });

        gridStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainStart = removeRepeatItem(trainStart, listStation.get(position).getName());
                if (!listStation.get(position).isSelect()) {
                    listStation.get(position).setSelect(true);
                } else {
                    listStation.get(position).setSelect(false);
                }
                trainUpAdapter.notifyDataSetChanged();
            }
        });

        gridStationDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainEndStation = removeRepeatItem(trainEndStation, listDown.get(position).getName());
                if (!listDown.get(position).isSelect()) {
                    listDown.get(position).setSelect(true);
                } else {
                    listDown.get(position).setSelect(false);
                }
                trainDownAdapter.notifyDataSetChanged();
            }
        });

        gridArriveTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainArriveTime = removeRepeatItem(trainArriveTime, endTime.get(position).getName());
                if (!endTime.get(position).isSelect()) {
                    endTime.get(position).setSelect(true);
                } else {
                    endTime.get(position).setSelect(false);
                }
                trainEndAdapter.notifyDataSetChanged();

            }
        });

        gridStartTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainStartTime = removeRepeatItem(trainStartTime, startTime.get(position).getName());
                if (!startTime.get(position).isSelect()) {
                    startTime.get(position).setSelect(true);
                } else {
                    startTime.get(position).setSelect(false);
                }
                trainStartAdapter.notifyDataSetChanged();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainTypeWindow.this.dismiss();
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                onTypeParamListener.setOnTypeParameterSelect(trainType, trainSeat, trainStart, trainEndStation, trainStartTime, trainArriveTime);
                TrainTypeWindow.this.dismiss();
            }
        });

    }

    //更新 选择
    private void updateShaixuan() {
        trainType.clear();
        trainSeat.clear();
        trainStart.clear();
        trainEndStation.clear();
        trainArriveTime.clear();
        trainStartTime.clear();

        trainType.addAll(trainTypeCopy);
        trainSeat.addAll(trainSeatCopy);
        trainStart.addAll(trainStartCopy);
        trainEndStation.addAll(trainEndStationCopy);
        trainArriveTime.addAll(trainArriveTimeCopy);
        trainStartTime.addAll(trainStartTimeCopy);
    }

    //刷新window
    public void refreshWindow() {
        listDown.clear();
        listSeat.clear();
        listType.clear();
        listStation.clear();
        endTime.clear();
        startTime.clear();

        updateShaixuan();
        try {
            for (SeatSelectModel seatSelectModel : listSeatCopy) {
                listSeat.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : listTypeCopy) {
                listType.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : listStationCopy) {
                listStation.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : listDownCopy) {
                listDown.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : startTimeCopy) {
                startTime.add(seatSelectModel.clone());
            }
            for (PopTimeSelectModel seatSelectModel : endTimeCopy) {
                endTime.add(seatSelectModel.clone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        trainDownAdapter.notifyDataSetChanged();
        trainSeatAdapter.notifyDataSetChanged();
        trainTypeAdapter.notifyDataSetChanged();
        trainUpAdapter.notifyDataSetChanged();
        trainEndAdapter.notifyDataSetChanged();
        trainStartAdapter.notifyDataSetChanged();
    }

    //缓存副本
    private void saveData() {

        listTypeCopy.clear();
        listSeatCopy.clear();
        listStationCopy.clear();
        listDownCopy.clear();
        startTimeCopy.clear();
        endTimeCopy.clear();

        trainTypeCopy.clear();
        trainSeatCopy.clear();
        trainStartCopy.clear();
        trainEndStationCopy.clear();
        trainArriveTimeCopy.clear();
        trainStartTimeCopy.clear();

        trainTypeCopy.addAll(trainType);
        trainSeatCopy.addAll(trainSeat);
        trainStartCopy.addAll(trainStart);
        trainEndStationCopy.addAll(trainEndStation);
        trainArriveTimeCopy.addAll(trainArriveTime);
        trainStartTimeCopy.addAll(trainStartTime);

        try {
            for (SeatSelectModel seatSelectModel : listSeat) {
                listSeatCopy.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : listType) {
                listTypeCopy.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : listStation) {
                listStationCopy.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : listDown) {
                listDownCopy.add(seatSelectModel.clone());
            }

            for (PopTimeSelectModel seatSelectModel : startTime) {
                startTimeCopy.add(seatSelectModel.clone());
            }
            for (PopTimeSelectModel seatSelectModel : endTime) {
                endTimeCopy.add(seatSelectModel.clone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 移除还是添加
     *
     * @param list
     * @param add
     * @return
     */
    private List<String> removeRepeatItem(List<String> list, String add) {
        boolean isAdd = false;
        int position = 0;

        if (list == null) {
            list = new ArrayList<>();
        }

        if (list.size() == 0) {
            list.add(add);
            return list;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(add)) {
                isAdd = true;
                position = i;
            }
        }
        if (isAdd) {
            list.remove(position);
        } else {
            list.add(add);
        }
        return list;
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

    private void bindViews() {

        view = activity.getLayoutInflater().inflate(R.layout.pop_train_type, null);
        llDismiss = (LinearLayout) view.findViewById(R.id.ll_dismiss);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvSure = (TextView) view.findViewById(R.id.tv_sure);
        llHeader = (LinearLayout) view.findViewById(R.id.ll_header);
        gridType = (GridViewForScrollView) view.findViewById(R.id.grid_type);
        gridSeat = (GridViewForScrollView) view.findViewById(R.id.grid_seat);
        gridStation = (GridViewForScrollView) view.findViewById(R.id.grid_station);
        gridStationDown = (GridViewForScrollView) view.findViewById(R.id.grid_station_down);
        gridStartTime = (GridViewForScrollView) view.findViewById(R.id.grid_start_time);
        gridArriveTime = (GridViewForScrollView) view.findViewById(R.id.grid_arrive_time);
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

    public void setShaixuanData(List<PopTimeSelectModel> listType, List<SeatSelectModel> listSeat, List<PopTimeSelectModel> listStation, List<PopTimeSelectModel> listDown,
                                List<PopTimeSelectModel> startTime, List<PopTimeSelectModel> endTime) {
        this.listType = listType;
        this.listSeat = listSeat;
        this.listStation = listStation;
        this.listDown = listDown;
        this.startTime = startTime;
        this.endTime = endTime;

        if (listChangeCount) {
            listChangeCount = false;
            saveData();
        }
        trainTypeAdapter.replaceAll(listType);
        trainSeatAdapter.replaceAll(listSeat);
        trainUpAdapter.replaceAll(listStation);
        trainDownAdapter.replaceAll(listDown);
        trainStartAdapter.replaceAll(startTime);
        trainEndAdapter.replaceAll(endTime);
    }

    public interface OnTypeParameterSelect {
        void setOnTypeParameterSelect(List<String> trainType, List<String> trainSeat, List<String> trainStart, List<String> trainEndStation, List<String> trainStartTime, List<String> trainArriveTime);
    }
}
