package com.wxws.myticket.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.order.entity.OrderListEvent;
import com.wxws.myticket.order.widgets.OrderlistTypePopwindow;

import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeAll;
import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeBaoche;
import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeBus;
import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeDirect;
import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeTrain;
import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeYuezu;
import static com.wxws.myticket.common.constants.ModuleConstants.validTypeHistory;
import static com.wxws.myticket.common.constants.ModuleConstants.validTypeInvalid;
import static com.wxws.myticket.common.constants.ModuleConstants.validTypeRecent;
import static com.wxws.myticket.common.constants.ModuleConstants.validTypeValid;

/**
 * desc: 订单列表fragment
 * Date: 2016-10-31 18:07
 *
 * @author jiangyan
 */

public class OrderFragment extends BaseFragment {

    private ImageView mIvArrow;
    private OrderlistTypePopwindow mTypePop;
    private int defOrderType = 0;// 订单列表最先显示哪个订单
    private boolean defIsRecent = true;// 最近订单还是历史订单
    private int defValidType = 1;// 订单列表类型。

    private TextView mTvTitle;
    private RadioButton mRbRecent;
    private RadioButton mRbHistory;

    private Fragment mCurrentFrag;// 当前显示的fragment
    private static final String TAG = "OrderFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, null);
        initView();

        return view;
    }

    private void initView() {

        // 订单类型筛选
        mTvTitle = (TextView) view.findViewById(R.id.rv_orderlist_title);
        LinearLayout llChange = (LinearLayout) view.findViewById(R.id.ll_orderlist_shaixuan);
        mIvArrow = (ImageView) view.findViewById(R.id.iv_orderlist_arrow);

        llChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击切换火车票/汽车票/包车/约租车
                mIvArrow.setBackgroundResource(R.mipmap.icon_orderlist_shaixuan_up);

                mTypePop.showAtBottom(view.findViewById(R.id.rl_orderlist_titlelayout));
            }
        });

        if (mTypePop == null) {
            mTypePop = new OrderlistTypePopwindow(getActivity(), defOrderType);
        }
        mTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mIvArrow.setBackgroundResource(R.mipmap.icon_orderlist_shaixuan_down);
            }
        });

        // 最近订单还是历史订单
        RadioGroup rgRorH = (RadioGroup) view.findViewById(R.id.rg_orderlist);

        mRbRecent = (RadioButton) view.findViewById(R.id.rb_orderlist_recent);
        mRbHistory = (RadioButton) view.findViewById(R.id.rb_orderlist_history);

        rgRorH.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_orderlist_recent:
                        // 显示最近订单
                        defIsRecent = true;
                        break;
                    case R.id.rb_orderlist_history:
                        // 显示历史订单
                        defIsRecent = false;
                        break;
                }
                updateView();
            }
        });

        updateView();

    }

    private void updateView() {

        if (defOrderType == orderTypeAll) {
            defValidType = defIsRecent ? validTypeValid : validTypeInvalid;
        } else {
            defValidType = defIsRecent ? validTypeRecent : validTypeHistory;
        }

        switch (defOrderType) {
            case orderTypeAll:
                mTvTitle.setText("全部订单");
                break;
            case orderTypeBus:
                mTvTitle.setText("汽车票订单");
                break;
            case orderTypeTrain:
                mTvTitle.setText("火车票订单");
                break;
            case orderTypeDirect:
                mTvTitle.setText("直通车订单");
                break;
            case orderTypeBaoche:
                mTvTitle.setText("我要包车订单");
                break;
            case orderTypeYuezu:
                mTvTitle.setText("约租车订单");
                break;
        }

        switch (defValidType) {
            case validTypeValid:
            case validTypeInvalid:
                mRbRecent.setText("有效订单");
                mRbHistory.setText("无效订单");
                break;
            case validTypeHistory:
            case validTypeRecent:
                mRbRecent.setText("最近订单");
                mRbHistory.setText("历史订单");
                break;
        }

        if (defIsRecent) {
            mRbRecent.setBackgroundResource(R.drawable.bg_orderlist_rorh);
            mRbHistory.setBackgroundResource(R.color.white);
            mRbRecent.setChecked(true);
            mRbHistory.setChecked(false);

        } else {
            mRbRecent.setBackgroundResource(R.color.white);
            mRbHistory.setBackgroundResource(R.drawable.bg_orderlist_rorh);
            mRbRecent.setChecked(false);
            mRbHistory.setChecked(true);
        }

        initOrderFrag();

    }

    // defOrderType + "" + defValidType 作为tag，如果已经存在实例则直接切换，不存在则生成一个实例。
    // 简化代码。。。
    private void initOrderFrag() {

        Fragment toFragment = getChildFragmentManager().findFragmentByTag(defOrderType + "" + defValidType);

        if (toFragment == null) {

            toFragment = CommonOrderListFragment.getInstance(defOrderType, defValidType);
        }

        switchFrag(toFragment);

    }

    // 将defOrderType + "" + defValidType作为fragment的tag，方便后续fragment切换
    public void switchFrag(Fragment toFragment) {

        if (toFragment.isAdded()) {
            getChildFragmentManager().beginTransaction().hide(mCurrentFrag).show(toFragment).commit();
        } else {
            if (mCurrentFrag == null) {
                getChildFragmentManager().beginTransaction().add(R.id.fl_orderlist_container, toFragment, defOrderType + "" + defValidType).show(toFragment).commit();
            } else {
                getChildFragmentManager().beginTransaction().hide(mCurrentFrag).add(R.id.fl_orderlist_container, toFragment, defOrderType + "" + defValidType).show(toFragment).commit();
            }
        }

        mCurrentFrag = toFragment;
    }

    @Subscribe
    public void updateListType(OrderListEvent event) {
        defOrderType = event.getType();

        if (event.is_recent_changed()) {
            defIsRecent = event.is_recent();
        }
        updateView();
    }
}
