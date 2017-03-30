package com.wxws.myticket.order.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.bus.activity.PayActivity;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshBase;
import com.wxws.myticket.common.widgets.view.pull.PullToRefreshListView;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.order.activity.BaocheOrderdetailsActivity;
import com.wxws.myticket.order.activity.BusOrderDetailsActivity;
import com.wxws.myticket.order.activity.RentcarOrderDetailsActivity;
import com.wxws.myticket.order.activity.TrainOrderDetailsActivity;
import com.wxws.myticket.order.adapter.CommonOrderAdapter;
import com.wxws.myticket.order.entity.CommonOrderListResponse;
import com.wxws.myticket.order.entity.OrderListResponsePara;
import com.wxws.myticket.order.widgets.CommonDialog;
import com.wxws.myticket.train.activity.TrainPayActivity;

import java.util.ArrayList;
import java.util.List;

import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeAirCraftBus;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeAppoint;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeBus;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeCityLine;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeGangao;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeRentCar;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeTrain;
import static com.wxws.myticket.common.constants.ModuleConstants.bizTypeTravel;

/**
 * desc: 普通类订单列表，包括全部订单、汽车票订单、直通车订单、我要包车订单、约租车订单。
 * Date: 2016-10-31 18:07
 *
 * @author jiangyan
 */
public class CommonOrderListFragment extends BaseFragment {

    private int pageIndex = 1;
    final private static int pageSize = 10;// 一页10条数据
    private int total = 0;

    private PullToRefreshListView mPlvListview;
    private ListView mRefreshListView;
    private List<OrderListResponsePara> mList = new ArrayList<OrderListResponsePara>();

    private CommonOrderAdapter mAdapter;
    private int mOrderType;//0：全部订单(status 只会存在0和1) 1：汽车票订单 2：火车票订单 3：直通车订单 4：我要包车订单 5：约租车订单
    private int mValidType;//0 失效订单、1 有效订单、2最近订单、3历史订单

    boolean flag_empty = false;// 无返回数据
    boolean flag_show = true;// 当前fragment是否显示

    public static CommonOrderListFragment getInstance(int orderType, int validType) {//TODO type

        CommonOrderListFragment fragment = new CommonOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderType", orderType);
        bundle.putInt("validType", validType);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderType = getArguments().getInt("orderType");
        mValidType = getArguments().getInt("validType");
        Log.e("mudo", "orderType:" + mOrderType + ":" + mValidType);
        RxBus.getInstance().register(CommonOrderListFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commonorder, null);
        bindLoadingView();
        initView();

        if (checkLogin()) {
            pageIndex = 1;
            getData();
        } else {
            showErrorOrNoData(getString(R.string.tips_login_order), R.string.go_login, R.mipmap.tip1, true);
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //优化第一次进入无数据的时候，自动刷新。如果一直有数据，则通过listview的下拉刷新实现刷新。
        if (!hidden) {//显示出来的时候
            if (flag_empty && mList.size() == 0) {// 如果第一次请求无数据，则每次进页面刷新
                pageIndex = 1;
                getData();
            }
        }
        flag_show = !hidden;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(CommonOrderListFragment.this);
    }

    private void initView() {
        // 初始化数据
        mPlvListview = (PullToRefreshListView) view.findViewById(R.id.plv_listview);
        initPullRefreshListView();

        mAdapter.setOnButtonClickListener(new CommonOrderAdapter.onButtonClickListener() {
            @Override
            public void deleteOrder(final String orderId) {

                new CommonDialog(getActivity()).showDialog("删除提示", "确定删除该订单？", new DialogCallBackListener() {

                    @Override
                    public void doPositiveClick() {
                        deleteOrderToServer(orderId);
                    }

                    @Override
                    public void doPositiveClick(String string) {
                    }

                    @Override
                    public void doNegativeClick() {
                    }
                });
            }

            @Override
            public void cancelOrder(final String orderId) {

                new CommonDialog(getActivity()).showDialog("取消提示", "确定取消该订单？", new DialogCallBackListener() {

                    @Override
                    public void doPositiveClick() {
                        cancelOrderToServer(orderId);
                    }

                    @Override
                    public void doPositiveClick(String string) {
                    }

                    @Override
                    public void doNegativeClick() {
                    }
                });
            }

            @Override
            public void payOrder(String orderId, String bizType) {
                if (bizType.equals(bizTypeTrain)) {// 火车票支付

                    Intent intent = new Intent(getActivity(), TrainPayActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("bizType", bizType);
                    startActivityWithAnim(intent);

                } else {// 其他支付跳转
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("bizType", bizType);
                    startActivityWithAnim(intent);
                }
            }
        });
    }

    private void deleteOrderToServer(String orderId) {

        HttpRequestManager.getInstance().deleteOrder(orderId, new ApiSubscriber<JsonResult<String>>(mActivity, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    pageIndex = 1;
                    getData();
                }
                ToastManager.getInstance().showToast(getActivity(), result.getResultMsg());
            }
        });
    }

    private void cancelOrderToServer(String orderId) {

        HttpRequestManager.getInstance().cancelOrder(orderId, new ApiSubscriber<JsonResult<String>>(mActivity, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    pageIndex = 1;
                    getData();
                }
                ToastManager.getInstance().showToast(getActivity(), result.getResultMsg());
            }
        });
    }

    public void initPullRefreshListView() {
        mPlvListview.setMode(PullToRefreshBase.Mode.BOTH);
        mPlvListview.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                    refreshView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pullUpToRefresh));
                    refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.releaseToRefresh));
                    refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
                    int currentSize = 0;

                    if (mList != null) {
                        currentSize = mList.size();
                    }
                    refreshView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel(String.format(getString(R.string.total_info), total + "", currentSize + ""));

                    refreshView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.pullDownToRefresh));
                    refreshView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.releaseToRefresh));
                    refreshView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.loading));
                }
            }
        });

        mPlvListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (pageIndex + 1 > Math.ceil((double) total / pageSize)) {
                    ToastManager.getInstance().showToast(mActivity, R.string.finish_all);
                    refreshHandler.sendEmptyMessage(1);
                } else {
                    pageIndex++;
                    getData();
                }
            }
        });

        mRefreshListView = mPlvListview.getRefreshableView();
        mAdapter = new CommonOrderAdapter(getActivity());
        mRefreshListView.setAdapter(mAdapter);

        mRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();

                switch (mList.get(position - 1).getBizType()) {
                    case bizTypeBus://汽车票
                        intent.setClass(getActivity(), BusOrderDetailsActivity.class);
                        break;
                    case bizTypeTravel:// 旅游快线      直通车
                    case bizTypeCityLine:// 城际专线    直通车
                    case bizTypeAirCraftBus:// 机场大巴 直通车
                    case bizTypeGangao:// 港澳专线      直通车
                        intent.setClass(getActivity(), BusOrderDetailsActivity.class);
                        break;
                    case bizTypeAppoint://约租车
                        intent.setClass(getActivity(), RentcarOrderDetailsActivity.class);
                        break;
                    case bizTypeRentCar://我要包车
                        intent.setClass(getActivity(), BaocheOrderdetailsActivity.class);
                        break;
                    case bizTypeTrain://火车票
                        intent.setClass(getActivity(), TrainOrderDetailsActivity.class);
                        break;
                }
                intent.putExtra("orderId", mList.get(position - 1).getOrderId());// 有header，减1处理
                intent.putExtra("bizType", mList.get(position - 1).getBizType());
                mActivity.startActivityWithAnim(intent);

            }
        });
    }

    private void getData() {

        getUserInfo();
        if (mList == null || mList.size() == 0) {
            showLoading();
        } else {
            if (flag_show) {
                mLoadingPopWindow.showLoadingDialog();
            }

        }

        HttpRequestManager.getInstance().getOrderList(mValidType + "", mOrderType + "", pageIndex + "", pageSize + "", new ApiSubscriber<JsonResult<CommonOrderListResponse>>(getActivity(), ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<CommonOrderListResponse> result) {
                if (refreshHandler != null) {
                    refreshHandler.sendEmptyMessage(1);
                }

                mLoadingPopWindow.dismissLoadingDialog();
                if ("0000".equals(result.getResultCode())) {
                    if (pageIndex == 1) {
                        mList.clear();
                        dismissLoading();
                    }
                    if (null != result.getObject() && result.getObject().getRows().size() != 0) {

                        flag_empty = false;
                        mList.addAll(result.getObject().getRows());
                        total = result.getObject().getTotal();
                        initData();
                    } else {
                        flag_empty = true;
                        if (mList.size() == 0) { // 如果页面一直没数据，则显示empty，否则提示加载空数据。
                            showErrorOrNoData("", R.mipmap.base_message_order_null);
                        } else {
                            ToastManager.getInstance().showToast(getActivity(), result.getResultMsg());
                        }

                    }
                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_refresh_again, R.mipmap.base_not_network, true);
                }
            }
        });

    }

    Handler refreshHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (mPlvListview != null) {
                mPlvListview.onRefreshComplete();
            }
            return false;
        }
    });

    public void initData() {

        if (mList.size() != 0) {
            if (mValidType == 0) {
                mAdapter.refreshData(mList, true);
            } else {
                mAdapter.refreshData(mList, false);
            }

        }
    }

    @Subscribe
    public void refreshData(CommonEvent event) {
        Log.e("mudo", "orderlist:getevent" + event.getFlag());
        if (event.getFlag() == CommonEvent.LOGIN_SUCCESS) {
            pageIndex = 1;
            getData();
        }
        if (event.getFlag() == CommonEvent.LOGIN_LOGOUT) {
            showErrorOrNoData(getString(R.string.tips_login_order), R.string.go_login, R.mipmap.tip1, true);
        }
    }

    @Override
    protected void clickReload() {
        super.clickReload();
        pageIndex = 1;
        getData();
    }
}