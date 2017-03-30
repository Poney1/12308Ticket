package com.wxws.myticket.my.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.CouponResponsePara;
import com.wxws.myticket.my.activity.H5CouponActivity;
import com.wxws.myticket.my.adapter.CouponAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 优惠券显示页面
 * Date: 2016-11-04 15:53
 *
 * @author jiangyan
 */
public class CouponFragment extends BaseFragment {


    private int mType;// 1为可用，0为失效
    private CouponAdapter mAdapter;
    private List<CouponResponsePara> mCouponResponseParaList = new ArrayList<>();

    public static CouponFragment getInstance(int type) {

        CouponFragment fragment = new CouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(this);
        mType = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coupon, null);

        bindLoadingView();
        initView();

        if (checkLogin()) {
            getData();
        } else {
            showErrorOrNoData(getString(R.string.tips_login_order), R.string.go_login, R.mipmap.tip1, true);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void initView() {
        ListView lvCouponList = (ListView) view.findViewById(R.id.lv_couponfrag_list);

        mAdapter = new CouponAdapter(getActivity());
        lvCouponList.setAdapter(mAdapter);
        lvCouponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (mCouponResponseParaList != null) {
                    CouponResponsePara para = mCouponResponseParaList.get(position);
                    Intent intent = new Intent(getActivity(), H5CouponActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("couponPara", para);
                    intent.putExtra("couponParaBundle", bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void getData() {
        showLoading();
        getUserInfo();
        String mobiePhone = null == userInfo.getMobilePhone() ? "" : userInfo.getMobilePhone();
        if (!StringUtils.isPhone(mobiePhone)) {
            mobiePhone = "";
        }
        HttpRequestManager.getInstance().getCoupon(mobiePhone, mType + "", new ApiSubscriber<JsonResult<List<CouponResponsePara>>>(CouponFragment.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<List<CouponResponsePara>> result) {
                if ("0000".equals(result.getResultCode())) {

                    if (result.getObject() != null && result.getObject().size() > 0) {
                        dismissLoading();
                        mAdapter.refreshData(result.getObject());
                        mCouponResponseParaList.clear();
                        mCouponResponseParaList.addAll(result.getObject());
                    } else {
                        showErrorOrNoData("", R.mipmap.base_coupon_null);
                    }

                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network, true);
                }
            }
        });
    }

    @Subscribe
    public void refreshData(CommonEvent event) {
        if (event.getFlag() == CommonEvent.COUPON_REFRESH || event.getFlag() == CommonEvent.LOGIN_SUCCESS) {
            if (checkLogin()) {
                getData();
            } else {
                showErrorOrNoData(getString(R.string.tips_login_order), R.string.go_login, R.mipmap.tip1, true);
            }
        }
    }


}