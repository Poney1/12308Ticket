package com.wxws.myticket.order.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.order.activity.TrainOrderDetailsActivity;
import com.wxws.myticket.order.entity.RefundConfirmModel;
import com.wxws.myticket.order.entity.RefundModel;

/**
 * desc:
 * Date: 2016-11-02 15:05
 *
 * @author jiangyan
 */
public class RefundDialog extends Dialog {

    private TextView tv_percent;
    private TextView tv_refundfee;
    private TextView tv_amount;
    private TextView tv_insurancefee;
    private RefundModel mRefundInfo;
    private Context mContext;
    private Button bt_notrefund;
    private Button bt_refund;
    private String mOrderId;
    private String mTicketId;
    private TextView tv_twotips;

    public RefundDialog(Context context, RefundModel refundInfo, String orderid, String ticketid) {
        super(context, R.style.dialog);
        mContext = context;
        mRefundInfo = refundInfo;
        mOrderId = orderid;
        mTicketId = ticketid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_refund);
        initView();
        initData();
    }

    private void initView() {
        tv_percent = (TextView) findViewById(R.id.tv_refunddialog_percent);
        tv_refundfee = (TextView) findViewById(R.id.tv_refunddialog_refundfee);
        tv_amount = (TextView) findViewById(R.id.tv_refunddialog_amount);
        tv_insurancefee = (TextView) findViewById(R.id.tv_refunddialog_insurancefee);
        tv_twotips = (TextView) findViewById(R.id.tv_refunddialog_two);

        bt_notrefund = (Button) findViewById(R.id.bt_notrefund);
        bt_refund = (Button) findViewById(R.id.bt_refund);

        bt_notrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        bt_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confimRefund();
            }
        });
    }

    public void initData() {
        tv_percent.setText(mRefundInfo.getRefundRate() + "%");
        tv_refundfee.setText(mRefundInfo.getRefundFee() * 0.01 + "");
        tv_amount.setText(mRefundInfo.getAmount() * 0.01 + "");
        tv_insurancefee.setText(mRefundInfo.getInsuranceAmount() * 0.01 + "");

        if ((mRefundInfo.getRefundFee() * 0.01) < 2.0) {
            tv_twotips.setVisibility(View.GONE);
        } else {
            tv_twotips.setVisibility(View.VISIBLE);
        }
    }

    public void confimRefund() {
        dismiss();
        View container = ((TrainOrderDetailsActivity) mContext).findViewById(R.id.ll_orderdetails_container);
        ((TrainOrderDetailsActivity) mContext).mLoadingPopWindow.showLoadingDialog();
        confirmRefundToServer();
    }

    private void confirmRefundToServer() {
        HttpRequestManager.getInstance().trainConfirmRefund(mOrderId, mTicketId, new ApiSubscriber<JsonResult<RefundConfirmModel>>(mContext,ApiSubscriber.NETVIEWLOADING) {
            @Override
            public void onNext(JsonResult<RefundConfirmModel> result) {
                ((TrainOrderDetailsActivity) mContext).dismissLoading();
                if ("0000".equals(result.getResultCode())) {
                    ToastManager.getInstance().showToast(mContext, "已申请退票");
                    // TODO 改用事件，退票成功后发出事件
                    //EventBus.getDefault().post(new RefundOrEndorseEvent(true));
                } else {
                    ToastManager.getInstance().showToast(mContext, result.getResultMsg());
                }
            }
        });
    }
}