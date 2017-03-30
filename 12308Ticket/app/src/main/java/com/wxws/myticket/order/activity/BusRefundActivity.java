package com.wxws.myticket.order.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.widgets.dialog.BusRefundReasonDialog;
import com.wxws.myticket.order.adapter.BusRefundAdapter;
import com.wxws.myticket.order.entity.ApplyRefundRequestPara;
import com.wxws.myticket.order.entity.BusRefundModel;
import com.wxws.myticket.order.entity.RefundPriceResponsePara;
import com.wxws.myticket.order.widgets.CommonListView;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 汽车票、直通车 车票退票退款页面
 * Date: 2016-11-04 10:51
 *
 * @author jiangyan
 */
public class BusRefundActivity extends BaseActivity {

    private BusRefundAdapter mAdapter;
    private TextView mTvPrice;
    private TextView mTvNum;
    private TextView mTvReason;
    private EditText mEtDetails;
    private List<String> mListData;
    private BusRefundReasonDialog mDialog;

    private int mReturnTicketWay;// 0 按票退，1 按订单退
    private int mRefundType = -1;// 选中的退款原因
    private String mTicketIdList;// 按票退的list
    private String mOrderId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busrefund);

        initCommonView();

        initView();

        getDataFromPre();

        mListData = new ArrayList<>();

        mListData.add("预订车票错了,重新购买");
        mListData.add("出行有变,更换其他车次");
        mListData.add("我还是换购火车票");
        mListData.add("我还是换购飞机票");
        mListData.add("不想购买了");
        mListData.add("其他原因");

        mDialog = new BusRefundReasonDialog(BusRefundActivity.this, mListData);
        mDialog.setOnReasonListener(new BusRefundReasonDialog.onReasonListener() {
            @Override
            public void choosedReason(int reason) {
                mTvReason.setText(mListData.get(reason));
                mRefundType = reason;
            }
        });

    }

    private void getDataFromPre() {
        mOrderId = getIntent().getStringExtra("orderId");
        List<String> ticketList = getIntent().getStringArrayListExtra("ticketList");
        if (null == ticketList || 0 == ticketList.size()) { // 如果有乘客信息，则为按票退，没有则为按订单退
            mReturnTicketWay = 1;
        } else {
            mReturnTicketWay = 0;
        }
        getRefundPrice(ticketList);// TODO 默认全部选中
    }

    private void initCommonView() {
        bindTitleViews();
        barTitle.setText("申请退款");
    }

    private void initView() {

        CommonListView clvList = (CommonListView) findViewById(R.id.clv_busrefund_passengerlist);
        mTvReason = (TextView) findViewById(R.id.tv_busrefund_reason);
        mEtDetails = (EditText) findViewById(R.id.et_busrefund_details);
        mTvPrice = (TextView) findViewById(R.id.tv_busrefund_price);
        mTvNum = (TextView) findViewById(R.id.tv_busrefund_num);
        Button btSubmit = (Button) findViewById(R.id.bt_busrefund_submit);

        mAdapter = new BusRefundAdapter(BusRefundActivity.this);
        clvList.setAdapter(mAdapter);

        mAdapter.setOnChoosedRefundListener(new BusRefundAdapter.OnChoosedRefundListener() {
            @Override
            public void choosedRefund(List<String> ticketIdList) {

                getRefundPrice(ticketIdList);
            }
        });

        mTvReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog == null) {
                    mDialog = new BusRefundReasonDialog(BusRefundActivity.this, mListData);
                }

                mDialog.show();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyToServer();
            }
        });

        // TODO 测试数据
        List<BusRefundModel> testlist = new ArrayList<>();

        testlist.add(new BusRefundModel("123", "张三", "1754922228562", "已取票", true));
        testlist.add(new BusRefundModel("231", "李四", "2415161568784", "已取票", true));
        testlist.add(new BusRefundModel("321", "王五", "3664511564564", "已取票", true));

        mAdapter.refreshData(testlist);

    }

    private void applyToServer() {
        // TODO 申请退款,接口有疑问，后续处理。

        String refundReason = null == mEtDetails.getText().toString() ? "" : mEtDetails.getText().toString();

        ApplyRefundRequestPara para = new ApplyRefundRequestPara();
        para.setOrderId(mOrderId);
        para.setTicketIdList(mTicketIdList);
        para.setReturnTicketWay(mReturnTicketWay + "");
        para.setRefundType(mRefundType + "");
        para.setRefundReason(refundReason);
        para.setRefundMoney(mTvPrice.getText().toString());
        para.setTicketCount(mTvNum.getText().toString());

        HttpRequestManager.getInstance().applyRefund(para, new ApiSubscriber<JsonResult<String>>(BusRefundActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    // TODO 发出事件，退款成功，订单详情需要刷新，订单列表需要刷新
                    RxBus.getInstance().post(new CommonEvent(CommonEvent.REFUND_ENDORSE_SUCCESS));
                    finish();
                } else {
                    ToastManager.getInstance().showToast(BusRefundActivity.this, result.getResultMsg());
                }
            }
        });

    }

    private void getRefundPrice(List<String> ticketIdList) {

        mTicketIdList = JSON.toJSONString(ticketIdList);
        mTvNum.setText(ticketIdList.size() + "");

        HttpRequestManager.getInstance().caculateRefundPrice(mOrderId, mTicketIdList, mReturnTicketWay + "", ticketIdList.size() + "", new ApiSubscriber<JsonResult<RefundPriceResponsePara>>(BusRefundActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<RefundPriceResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {

                    mTvPrice.setText("" + result.getObject().getRefundMoney() * 0.01);

                }
            }
        });
    }
}