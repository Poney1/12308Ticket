package com.wxws.myticket.order.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.order.entity.OperatTicketListModel;
import com.wxws.myticket.order.entity.OperateHistoryModel;
import com.wxws.myticket.order.entity.OperateListDataModel;
import com.wxws.myticket.order.entity.TicketOperateResponse;
import com.wxws.myticket.order.utils.TrainOrderStatusUtils;
import com.wxws.myticket.order.widgets.CommonListView;
import com.wxws.myticket.order.widgets.ScrollExpandaleListView;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 火车票操作详情
 * Date: 2016-11-02 14:23
 *
 * @author jiangyan
 */
public class TrainOperateDetailsActivity extends BaseActivity {

    private CommonListView lv_ticketlist;
    private ScrollExpandaleListView elv_operatelist;
    private String mOrderId;
    private String mOrderNumber;
    private List<OperateHistoryModel> mOperateHistoryList;
    private TextView tv_ordernum;
    private TextView tv_amount;
    private TextView tv_status;
    private TextView tv_refundinfo;
    private List<OperateListDataModel> mOperateRealList;

    private TicketOperateResponse mOperateInfo;
    private List<OperatTicketListModel> ticketListInfo;
    private TextView tv_type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_operate_details);
        bindTitleViews();
        bindLoadingView();

        barTitle.setText("操作详情");

        getDateFromPre();
        initView();
        getOperateHistory();

    }

    public void initView() {
        lv_ticketlist = (CommonListView) findViewById(R.id.lv_operate_statuslist);
        elv_operatelist = (ScrollExpandaleListView) findViewById(R.id.elv_operate_list);

        tv_type = (TextView) findViewById(R.id.tv_operate_ordertype);
        tv_ordernum = (TextView) findViewById(R.id.tv_operate_ordernumber);
        tv_amount = (TextView) findViewById(R.id.tv_operate_totalamount);
        tv_status = (TextView) findViewById(R.id.tv_operate_ticketstatus);
        tv_refundinfo = (TextView) findViewById(R.id.tv_operate_refunddetails);

    }

    public void getDateFromPre() {
        mOrderId = getIntent().getStringExtra("orderid");
        mOrderNumber = getIntent().getStringExtra("ordernum");
    }

    public void getOperateHistory() {
        HttpRequestManager.getInstance().getTrainOperateDetails(mOrderId, new ApiSubscriber<JsonResult<TicketOperateResponse>>(TrainOperateDetailsActivity.this,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<TicketOperateResponse> result) {
                if ("0000".equals(result.getResultCode())) {

                    mOperateInfo = result.getObject();
                    mOperateHistoryList = result.getObject().getOperationList();
                    showLoading();
                    dealWithData();
                    initData();

                } else {
                    showErrorOrNoData(getString(R.string.loaddata_fail), R.string.click_reload,R.mipmap.base_message_order_null);
                }
            }
        });

    }

    public void clickReload() {
        getOperateHistory();
    }

    public void dealWithData() {

        mOperateRealList = new ArrayList<OperateListDataModel>();
        List<String> tipsList = new ArrayList<String>();


        for (int i = 0; i < mOperateHistoryList.size(); i++) {

            String createtime = DateTimeUtil.getDateyyyyMMdd(mOperateHistoryList.get(i).getTime());

            if (tipsList.contains(createtime)) {
                OperateHistoryModel history = mOperateHistoryList.get(i);
                mOperateRealList.get(tipsList.indexOf(createtime)).getHistoryList().add(history);

            } else {
                OperateListDataModel model = new OperateListDataModel();
                model.setDate(createtime);
                OperateHistoryModel history = mOperateHistoryList.get(i);
                List<OperateHistoryModel> historyList = new ArrayList<>();
                historyList.add(history);

                model.setHistoryList(historyList);

                mOperateRealList.add(model);
                tipsList.add(createtime);
            }

        }

        // 处理改签一般订单里ticketlist票信息
        ticketListInfo = new ArrayList<>();
        if (null != mOperateInfo.getTicketList() && 0 != mOperateInfo.getTicketList().size()) {

            for (OperatTicketListModel ticketinfo : mOperateInfo.getTicketList()) {

                OperatTicketListModel model = new OperatTicketListModel();
                model.setOrderDetailId(ticketinfo.getOrderDetailId());
                model.setFromtime(mOperateInfo.getFromTime());
                model.setTotime(mOperateInfo.getToTime());
                model.setFromstation(mOperateInfo.getFromStationName());
                model.setTostation(mOperateInfo.getToStationName());
                model.setName(ticketinfo.getName());
                model.setOrderStatus(ticketinfo.getOrderStatus());
                model.setTicketStatus(ticketinfo.getTicketStatus());
                model.setType(0);

                ticketListInfo.add(model);
            }

        }

        if (null != mOperateInfo.getChangeOrderList() && 0 != mOperateInfo.getChangeOrderList().size()) {
            for (int i = 0; i < mOperateInfo.getChangeOrderList().size(); i++) {
                if (null != mOperateInfo.getChangeOrderList() && 0 != mOperateInfo.getChangeOrderList().size()) {

                    for (OperatTicketListModel ticketinfo : mOperateInfo.getChangeOrderList().get(i).getTicketList()) {

                        OperatTicketListModel model = new OperatTicketListModel();
                        model.setOrderDetailId(ticketinfo.getOrderDetailId());
                        model.setFromtime(mOperateInfo.getFromTime());
                        model.setTotime(mOperateInfo.getToTime());
                        model.setFromstation(mOperateInfo.getFromStationName());
                        model.setTostation(mOperateInfo.getToStationName());
                        model.setName(ticketinfo.getName());
                        model.setOrderStatus(ticketinfo.getOrderStatus());
                        model.setTicketStatus(ticketinfo.getTicketStatus());
                        model.setType(1);

                        ticketListInfo.add(model);
                    }
                }
            }
        }

    }

    public void initData() {

        if (null == mOrderNumber || "".equals(mOrderNumber)) {
            tv_type.setText("订单号：");
            tv_ordernum.setText(mOrderId);
        } else {
            tv_type.setText("取票号：");
            tv_ordernum.setText(mOrderNumber);
        }

        if (null == mOperateInfo.getRefundAmountInfo() || "".equals(mOperateInfo.getRefundAmountInfo())) {
            tv_refundinfo.setVisibility(View.GONE);
        } else {

            tv_refundinfo.setText(mOperateInfo.getRefundAmountInfo());
            tv_refundinfo.setVisibility(View.VISIBLE);

        }

        tv_amount.setText("¥" + (mOperateInfo.getAmount() * 0.01));

        tv_status.setText(TrainOrderStatusUtils.getCommonStatus(mOperateInfo.getOrderStatus()));

        lv_ticketlist.setAdapter(new OperateAdapter());

        OperateDetailsAdapter mDetailsAdapter = new OperateDetailsAdapter();
        elv_operatelist.setAdapter(mDetailsAdapter);

        // 默认展开
        for (int i = 0; i < mDetailsAdapter.getGroupCount(); i++) {
            elv_operatelist.expandGroup(i);
        }
    }

    class OperateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ticketListInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TicketHolder viewHolder;
            if (convertView == null) {
                viewHolder = new TicketHolder();
                convertView = View.inflate(TrainOperateDetailsActivity.this, R.layout.item_operatedetails_statuslist, null);

                viewHolder.tv_fromdate = (TextView) convertView.findViewById(R.id.tv_ticket_fromdate);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_ticket_name);
                viewHolder.tv_fromtime = (TextView) convertView.findViewById(R.id.tv_ticket_fromtime);
                viewHolder.tv_fromstation = (TextView) convertView.findViewById(R.id.tv_ticket_fromstation);
                viewHolder.tv_totime = (TextView) convertView.findViewById(R.id.tv_ticket_totime);
                viewHolder.tv_tostation = (TextView) convertView.findViewById(R.id.tv_ticket_tostation);
                viewHolder.tv_ticketstatus = (TextView) convertView.findViewById(R.id.tv_ticket_ticketstatus);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TicketHolder) convertView.getTag();
            }

            viewHolder.tv_fromdate.setText(DateTimeUtil.getDateMMDD(ticketListInfo.get(position).getFromtime()));
            viewHolder.tv_name.setText(ticketListInfo.get(position).getName());
            viewHolder.tv_fromtime.setText(DateTimeUtil.getDateHHMM(ticketListInfo.get(position).getFromtime()));
            viewHolder.tv_fromstation.setText(ticketListInfo.get(position).getFromstation());
            viewHolder.tv_totime.setText(DateTimeUtil.getDateHHMM(ticketListInfo.get(position).getTotime()));
            viewHolder.tv_tostation.setText(ticketListInfo.get(position).getTostation());

            if (ticketListInfo.get(position).getType() == 0) {
                viewHolder.tv_ticketstatus.setText(TrainOrderStatusUtils.getTicketStatus(ticketListInfo.get(position).getOrderStatus()));
            } else {
                viewHolder.tv_ticketstatus.setText(TrainOrderStatusUtils.getEndorseTicketStatus(ticketListInfo.get(position).getOrderStatus()));
            }

            switch (viewHolder.tv_ticketstatus.getText().toString()) {
                case "订单关闭":
                case "出票失败":
                case "改签中":
                case "改签失败":
                case "已改签":
                case "退票中":
                case "退票失败":
                case "待退款":
                case "退款中":
                case "退款失败":
                case "退款成功"://一般票

                case "改签取消":// 改签
                    viewHolder.tv_fromdate.setTextColor(Color.parseColor("#999999"));
                    viewHolder.tv_name.setTextColor(Color.parseColor("#999999"));
                    viewHolder.tv_fromtime.setTextColor(Color.parseColor("#999999"));
                    viewHolder.tv_fromstation.setTextColor(Color.parseColor("#999999"));
                    viewHolder.tv_totime.setTextColor(Color.parseColor("#999999"));
                    viewHolder.tv_tostation.setTextColor(Color.parseColor("#999999"));
                    viewHolder.tv_ticketstatus.setTextColor(Color.parseColor("#999999"));
                    break;
                default:
                    viewHolder.tv_fromdate.setTextColor(Color.parseColor("#333333"));
                    viewHolder.tv_name.setTextColor(Color.parseColor("#333333"));
                    viewHolder.tv_fromtime.setTextColor(Color.parseColor("#333333"));
                    viewHolder.tv_fromstation.setTextColor(Color.parseColor("#333333"));
                    viewHolder.tv_totime.setTextColor(Color.parseColor("#333333"));
                    viewHolder.tv_tostation.setTextColor(Color.parseColor("#333333"));
                    viewHolder.tv_ticketstatus.setTextColor(Color.parseColor("#333333"));
                    break;
            }
            return convertView;
        }
    }

    class TicketHolder {
        TextView tv_fromdate;
        TextView tv_name;
        TextView tv_fromtime;
        TextView tv_fromstation;
        TextView tv_totime;
        TextView tv_tostation;
        TextView tv_ticketstatus;
    }


    class OperateDetailsAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mOperateRealList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mOperateRealList.get(groupPosition).getHistoryList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mOperateRealList.get(groupPosition).getDate();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mOperateRealList.get(groupPosition).getHistoryList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View groupview = View.inflate(TrainOperateDetailsActivity.this, R.layout.item_operatedeatils_groupview, null);
            TextView tv_date = (TextView) groupview.findViewById(R.id.tv_group_date);
            tv_date.setText(mOperateRealList.get(groupPosition).getDate());
            return groupview;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View childview = View.inflate(TrainOperateDetailsActivity.this, R.layout.item_operatedetails_childview, null);

            TextView tv_time = (TextView) childview.findViewById(R.id.tv_child_time);
            TextView tv_info = (TextView) childview.findViewById(R.id.tv_child_info);

            tv_time.setText(DateTimeUtil.getDateHHMM(mOperateRealList.get(groupPosition).getHistoryList().get(childPosition).getTime()));
            tv_info.setText(mOperateRealList.get(groupPosition).getHistoryList().get(childPosition).getAction());

            return childview;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}