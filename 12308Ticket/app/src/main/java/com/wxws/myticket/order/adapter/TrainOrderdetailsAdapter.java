package com.wxws.myticket.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.order.activity.TrainEndorseActivity;
import com.wxws.myticket.order.activity.TrainOrderDetailsActivity;
import com.wxws.myticket.order.entity.EndorseTicketModel;
import com.wxws.myticket.order.entity.RefundModel;
import com.wxws.myticket.order.entity.TrainOrderDetailsTicketModel;
import com.wxws.myticket.order.entity.TrainOrderdetailsResponseModel;
import com.wxws.myticket.order.utils.SeatTypeUtils;
import com.wxws.myticket.order.utils.TrainOrderStatusUtils;
import com.wxws.myticket.order.widgets.EndorseDialog;
import com.wxws.myticket.order.widgets.RefundDialog;
import com.wxws.myticket.order.widgets.TrainFailureDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * Date: 2016-11-02 13:59
 *
 * @author jiangyan
 */
public class TrainOrderdetailsAdapter extends SimpleBaseAdapter implements EndorseDialog.OnChooseEndorseTypeListener{
    private Context mContext;
    private String mOrderId;
    private String mFromstationCode;
    private String mFromstation;
    private int mType;// 一般票还是改签票 1为一般，2为改签
    private String id;//订单子id 编号
    private TrainOrderdetailsResponseModel mOrderResponseInfo;//
    private EndorseDialog mEndorseDialog;

    public TrainOrderdetailsAdapter(Context context, String orderid, int type) {
        super(context, null);
        this.mContext = context;
        this.mOrderId = orderid;
        this.mType = type;
    }

    public void setFromstationCode(String fromstationcode, String fromstation) {
        this.mFromstationCode = fromstationcode;
        this.mFromstation = fromstation;
    }

    public void refreshData(List<TrainOrderDetailsTicketModel> ticketsinfoList) {
        replaceAll(ticketsinfoList);
    }

    public void setOrder(TrainOrderdetailsResponseModel mOrderResponseInfo) {
        this.mOrderResponseInfo = mOrderResponseInfo;
    }


    @Override
    public int getItemResource() {
        return R.layout.item_traindetails_ticketsinfo;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        Button bt_refund = (Button) holder.getView(R.id.bt_itemtraindetails_refund);
        Button bt_endorse = (Button) holder.getView(R.id.bt_itemtraindetails_endorse);

        TextView tv_name = (TextView) holder.getView(R.id.tv_itemorderdetails_name);
        TextView tv_tickettype = (TextView) holder.getView(R.id.tv_itemorderdetails_tickettype);
        TextView tv_identityno = (TextView) holder.getView(R.id.tv_itemorderdetails_identino);
        TextView tv_ticketstatus = (TextView) holder.getView(R.id.tv_itemorderdetails_ticketstatus);
        TextView tv_seattype = (TextView) holder.getView(R.id.tv_itemorderdetails_seattype);
        TextView tv_seatprice = (TextView) holder.getView(R.id.tv_itemorderdetails_seatprice);
        TextView tv_seatinfo = (TextView) holder.getView(R.id.tv_itemorderdetails_seatinfo);

        final TrainOrderDetailsTicketModel orderDetailsTicketModel = (TrainOrderDetailsTicketModel) getItem(position);

        if (mType == 1) {
            tv_ticketstatus.setText(TrainOrderStatusUtils.getTicketStatus(orderDetailsTicketModel.getOrderStatus()));
        } else {
            tv_ticketstatus.setText(TrainOrderStatusUtils.getEndorseTicketStatus(orderDetailsTicketModel.getOrderStatus()));
        }

        switch (orderDetailsTicketModel.getOrderStatus()) {
            case 4:
            case 9:
            case 11:
            case 17:
            case 38:
            case 41:
            case 70:
                tv_name.setTextColor(Color.parseColor("#999999"));
                tv_tickettype.setBackgroundResource(R.drawable.bg_traindeails_tickettype_gray);
                tv_identityno.setTextColor(Color.parseColor("#999999"));
                tv_ticketstatus.setTextColor(Color.parseColor("#999999"));
                tv_seattype.setTextColor(Color.parseColor("#999999"));
                tv_seatprice.setTextColor(Color.parseColor("#999999"));
                tv_seatinfo.setTextColor(Color.parseColor("#999999"));

                break;
            default:
                tv_name.setTextColor(Color.parseColor("#333333"));
                tv_tickettype.setBackgroundResource(R.drawable.bg_traindeails_tickettype);
                tv_identityno.setTextColor(Color.parseColor("#666666"));
                tv_ticketstatus.setTextColor(Color.parseColor("#F03F3F"));
                tv_seattype.setTextColor(Color.parseColor("#666666"));
                tv_seatprice.setTextColor(Color.parseColor("#666666"));
                tv_seatinfo.setTextColor(Color.parseColor("#666666"));

                break;
        }


        tv_name.setText(orderDetailsTicketModel.getName());
        tv_identityno.setText(orderDetailsTicketModel.getCertNo());
        tv_seattype.setText(SeatTypeUtils.getSeatName(orderDetailsTicketModel.getSeatType()));
        tv_seatprice.setText("¥" + orderDetailsTicketModel.getTicketAmount() * 0.01);
        tv_seatinfo.setText(orderDetailsTicketModel.getSeatInfo());

        switch (orderDetailsTicketModel.getTicketType()) {
            case TrainOrderDetailsActivity.ADULT_TICKET:
                tv_tickettype.setText("成人票");
                break;
            case TrainOrderDetailsActivity.CHILD_TICKET:
                tv_tickettype.setText("儿童票");
                break;
            case TrainOrderDetailsActivity.STUDENT_TICKET:
                tv_tickettype.setText("学生票");
                break;
            case TrainOrderDetailsActivity.SOLDIER_TICKET:
                tv_tickettype.setText("残军票");
                break;
        }

        bt_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyToRefund(orderDetailsTicketModel.getOrderDetailId());
            }
        });

        bt_endorse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mEndorseDialog == null) {
                    mEndorseDialog = new EndorseDialog(mContext, position);
                }
                mEndorseDialog.setOnChooseEndorseTypeListener(TrainOrderdetailsAdapter.this);
                mEndorseDialog.show();

            }
        });

        if (orderDetailsTicketModel.getAllowRefund() == 1) {
            bt_refund.setVisibility(View.VISIBLE);
        } else {
            bt_refund.setVisibility(View.GONE);
        }

        if (orderDetailsTicketModel.getAllowChange() == 1) {
            bt_endorse.setVisibility(View.VISIBLE);
        } else {
            bt_endorse.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public void endorseType(int type, int position) {

        if (mEndorseDialog != null && mEndorseDialog.isShowing()) {
            mEndorseDialog.dismiss();
        }
        Intent endorseIntent = new Intent(mContext, TrainEndorseActivity.class);
        endorseIntent.putExtra("orderid", mOrderId);
        endorseIntent.putExtra("fromstation", mFromstation);
        endorseIntent.putExtra("fromstationcode", mFromstationCode);
        List<EndorseTicketModel> ticketlist = new ArrayList<>();

        if (type == 1) {
            EndorseTicketModel model = new EndorseTicketModel();
            model.setId(((TrainOrderDetailsTicketModel) data.get(position)).getOrderDetailId());
            ticketlist.add(model);

        } else {

            for (int i = 0; i < data.size(); i++) {

                if (((TrainOrderDetailsTicketModel) data.get(i)).getAllowChange() == 1) { // 允许改签的，才将数据记录,只需要遍历外部的ticketlist里面的数据

                    EndorseTicketModel model = new EndorseTicketModel();
                    model.setId(((TrainOrderDetailsTicketModel) data.get(i)).getOrderDetailId());
                    ticketlist.add(model);
                }

            }
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketlist", (Serializable) ticketlist);
        endorseIntent.putExtra("ticketbundle", bundle);

        ((TrainOrderDetailsActivity) mContext).startActivityWithAnim(endorseIntent);

    }

    public void applyToRefund(String listid) {

        final String mListId = listid;
        HttpRequestManager.getInstance().trainApplyRefund(mOrderId, mListId, new ApiSubscriber<JsonResult<RefundModel>>(mContext,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<RefundModel> result) {
                if ("0000".equals(result.getResultCode())) {

                    RefundDialog refundDialog = new RefundDialog(mContext, result.getObject(), mOrderId, mListId);
                    refundDialog.show();

                } else {

                    TrainFailureDialog dialog = new TrainFailureDialog(mContext, "退票申请失败", result.getResultMsg());
                    dialog.show();
                }
            }
        });

    }

}