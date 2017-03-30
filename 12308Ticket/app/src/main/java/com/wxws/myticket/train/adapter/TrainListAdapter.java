package com.wxws.myticket.train.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.widgets.view.GridViewForScrollView;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;
import com.wxws.myticket.train.activity.ChooseSeatTypeActivity;
import com.wxws.myticket.train.entity.SeatListEntity;
import com.wxws.myticket.train.entity.SeatSortModel;
import com.wxws.myticket.train.entity.TrainListModel;
import com.wxws.myticket.train.entity.TrainStationInfoModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * desc: 火车票 车次适配器
 * Date: 2016-07-26 13:45
 *
 * @auther: lixiangxiang
 */
public class TrainListAdapter extends SimpleBaseAdapter {


    private TrainListModel trainListModel;
    private OnShowStationInfo onShowStationInfo;
    private List<TrainStationInfoModel.DataBean> listStation;
    private List<SeatListEntity> mergeAnother;
    private String startDate;
    private boolean isHaveTicket = true;//默认显示为余票
    private boolean[] isSort = new boolean[600];//是否排序过
    private List[] data3 = new List[600];
    private Context context;
    private List<TrainListModel> data1  = new ArrayList<>();
    private Boolean mFlag_isendorse = false;// 是否是改签流程
    private String mOrderId;
    private String id;//子订单编号
    private Bundle mTicketListBundle;

    public TrainListAdapter(Context context) {
        super(context, null);
        this.context = context;
        listStation = new ArrayList<>();
        mergeAnother = new ArrayList<>();
    }

    public void setOnShowStationInfo(OnShowStationInfo onShowStationInfo) {
        this.onShowStationInfo = onShowStationInfo;
    }

    public void setBookType(boolean isEndorse, String orderId, Bundle ticketlistBundle, String id) {
        this.mFlag_isendorse = isEndorse;
        this.mOrderId = orderId;
        this.mTicketListBundle = ticketlistBundle;
        this.id = id;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_train_list;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        trainListModel = data1.get(position);
        TextView tvTrainNumber = (TextView) holder.getView(R.id.tv_train_number);
        TextView tvWaitTime = (TextView) holder.getView(R.id.tv_wait_time);
        TextView tvStartTime = (TextView) holder.getView(R.id.tv_start_time);
        TextView tvEndTime = (TextView) holder.getView(R.id.tv_end_time);
        TextView tvStart = (TextView) holder.getView(R.id.tv_start);
        TextView tvEnd = (TextView) holder.getView(R.id.tv_end);

        final RelativeLayout llExplainDesc = (RelativeLayout) holder.getView(R.id.ll_explain_desc);
        final LinearLayout llListHeader = (LinearLayout) holder.getView(R.id.ll_list_header);
        LinearLayout llItem = (LinearLayout) holder.getView(R.id.ll_item);
        final ListViewForScrollView trainList = (ListViewForScrollView) holder.getView(R.id.train_list);
        GridViewForScrollView gridBusiness = (GridViewForScrollView) holder.getView(R.id.grid_business);

        if (!isSort[position]) {
            isSort[position] = true;
        }
        mergeAnother = getMergeSeatList();
        SeatAdapter seatAdapter = new SeatAdapter(mContext, mergeAnother, isHaveTicket);
        boolean  isHaveThree =false;
        boolean  isPriceLength = false;
        for (SeatListEntity seatListBean : mergeAnother){
            if (seatListBean.getName().contains("高级软卧")){
                isHaveThree = true;
            }
            if (seatListBean.getPrice()>100000 && seatListBean.getPrice() %100 !=0){
                isPriceLength = true;
            }
        }
        if (isHaveThree || isPriceLength){
            gridBusiness.setNumColumns(3);
        }else {
            gridBusiness.setNumColumns(4);
        }
        gridBusiness.setAdapter(seatAdapter);

        final TrainListDescriptionAdapter tDescription = new TrainListDescriptionAdapter(context, data3[position]);
        trainList.setAdapter(tDescription);

        tvTrainNumber.setText(trainListModel.getTrainCode());
        tvWaitTime.setText(minuteToHour(Integer.parseInt(trainListModel.getRunTimeMinute())));
        tvStartTime.setText(trainListModel.getStartTime());
        tvEndTime.setText(trainListModel.getArriveTime());
        tvStart.setText(trainListModel.getFromStationName());
        tvEnd.setText(trainListModel.getToStationName());
        llExplainDesc.setSelected(trainListModel.isSelected());
        if (trainListModel.isSelected()) {
            trainList.setVisibility(View.VISIBLE);
            llListHeader.setVisibility(View.VISIBLE);
        } else {
            trainList.setVisibility(View.GONE);
            llListHeader.setVisibility(View.GONE);
        }
        llExplainDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data1.get(position).isSelected()) {
                    llExplainDesc.setSelected(true);
                    trainList.setVisibility(View.VISIBLE);
                    llListHeader.setVisibility(View.VISIBLE);
                    data1.get(position).setSelected(true);
                    onShowStationInfo.ShowStationInfo(data3[position],tDescription, data1.get(position).getTrainCode(), data1.get(position).getFromStationCode(), data1.get(position).getToStationCode(), data1.get(position).getTrainNo(),data1.get(position).getTrainStartDate(), position);
                } else {
                    data1.get(position).setSelected(false);
                    trainList.setVisibility(View.GONE);
                    llListHeader.setVisibility(View.GONE);
                    llExplainDesc.setSelected(false);
                }
            }
        });

        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainListModel trainListModel1 = data1.get(position);
                Intent intent = new Intent(mContext, ChooseSeatTypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", trainListModel1);
                bundle.putString("startDate", startDate);

                intent.putExtra("trainItem", bundle);
                if (mFlag_isendorse) {
                    intent.putExtra("isendorse", mFlag_isendorse);
                    intent.putExtra("orderid", mOrderId);
                    intent.putExtra("id", id);
                    intent.putExtra("ticketbundle", mTicketListBundle);
                }
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    //获取合并座位后的 集合
    private List<SeatListEntity> getMergeSeatList() {
        List<SeatListEntity> merge = trainListModel.getSeatList();
        List<SeatListEntity> merge1 = new ArrayList<>();
        int softNum = 0;
        int softPrice = 0;
        int softIndex = 0;
        String hardName = mContext.getString(R.string.train_list_hard_type);
        String softName = mContext.getString(R.string.train_list_type_soft);
        int hardPrice = 0;
        int hardIndex = 0;
        int hardNum = 0;
        int length = merge.size();
        for (int i = 0; i < length; i++) {
            if (merge.get(i) == null) {
                continue;
            }
            if (merge.get(i).getName() == null) {
                continue;
            }
            if (merge.get(i).getNum() >= 0) {
                if (merge.get(i).getName().contains(mContext.getString(R.string.train_list_hard_type))) {
                    hardNum = hardNum + merge.get(i).getNum();
                    if (mContext.getString(R.string.train_list_hard_type_up).equals(merge.get(i).getName())) {
                        hardPrice = merge.get(i).getPrice();
                        hardIndex = merge.get(i).getIndex();
                    }
                } else if (merge.get(i).getName().contains(mContext.getString(R.string.train_list_type_soft)) && !merge.get(i).getName().equals(
                        mContext.getString(R.string.train_list_high_type_soft))) {
                    softNum = softNum + merge.get(i).getNum();
                    if (mContext.getString(R.string.train_list_type_soft_up).equals(merge.get(i).getName())) {
                        softPrice = merge.get(i).getPrice();
                        softIndex = merge.get(i).getIndex();
                    }
                } else {
                    merge1.add(merge.get(i));
                }
            }
        }
        if (softIndex != 0) {
            SeatListEntity soft = new SeatListEntity();
            soft.setIndex(softIndex);
            soft.setNum(softNum);
            soft.setName(softName);
            soft.setPrice(softPrice);
            merge1.add(soft);
        }
        if (hardIndex != 0) {
            SeatListEntity hard = new SeatListEntity();
            hard.setIndex(hardIndex);
            hard.setNum(hardNum);
            hard.setName(hardName);
            hard.setPrice(hardPrice);
            merge1.add(hard);
        }

        //数据进行排序
        Collections.sort(merge1, new SeatSortModel());
        return merge1;
    }

    public interface OnShowStationInfo {
        void ShowStationInfo(List<TrainStationInfoModel.DataBean> dataBeen2, TrainListDescriptionAdapter tDescription, String code, String fromStationCode, String toStationCode, String trainNo, String trainStartDate, int position);
    }

    //设置出发时间
    public void setDate(String startDate) {
        this.startDate = startDate;
    }

    //票价 余票切换
    public void setHaveTicket(boolean isHaveTicket) {
        this.isHaveTicket = isHaveTicket;
        this.notifyDataSetChanged();
    }

    public void setOnlyTicket(List<TrainListModel> isOnlyTicket) {
        replaceAll(isOnlyTicket);
        data1.clear();
        if (isOnlyTicket != null) {
            data1.addAll(isOnlyTicket);
        }
    }

    private String minuteToHour(int minute) {
        String hourAndMinute = "";

        String hour = "";

        String minute1 = "";
        if (minute / 60 < 10) {
            hour = String.format("0%s", minute / 60);
        } else {
            hour = String.valueOf(minute / 60);
        }

        if (minute % 60 < 10) {
            minute1 = String.format("0%s", minute % 60);
        } else {
            minute1 = String.valueOf(minute % 60);
        }
        hourAndMinute = String.format("%s:%s", hour, minute1);

        return hourAndMinute;
    }

    public void replaceCurrentData(List elem){
        data1.clear();
        if (elem != null) {
            data1.addAll(elem);
        }
    }

    /**
     * 刷新
     *
     * @param adapter
     * @param dataBeanList
     * @param position
     */
    public void refreshThis(TrainListDescriptionAdapter adapter, List<TrainStationInfoModel.DataBean> dataBeanList, int position) {
        data3[position] = dataBeanList;
        adapter.replaceAll(dataBeanList);
    }
}
