package com.wxws.myticket.order.widgets;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.wxws.myticket.R;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.order.entity.OrderListEvent;

/**
 * desc:
 * Date: 2016-07-28 09:58
 *
 * @auther: jiangyan
 */
public class OrderlistTypePopwindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private View view;

    private int mType;// 1全部，2汽车票，3火车票，4直通车，5我要包车，6约租车
    private CheckBox mCbQBDD;
    private CheckBox mCbQCPDD;
    private CheckBox mCbHCPDD;
    private CheckBox mCbZTCDD;
    private CheckBox mCbWYBCDD;
    private CheckBox mCbYZCDD;

    public OrderlistTypePopwindow(Context context, int type) {
        super(context);
        this.mContext = context;
        this.mType = type;
        initView();
        initPop();
    }

    public void setType(int type) {
        this.mType = type;
        initTypeView();
    }

    public void initView() {
        view = View.inflate(mContext, R.layout.pop_orderlist_type, null);

        mCbQBDD = (CheckBox) view.findViewById(R.id.cb_choosetype_qbdd);
        mCbQCPDD = (CheckBox) view.findViewById(R.id.cb_choosetype_qcpdd);
        mCbHCPDD = (CheckBox) view.findViewById(R.id.cb_choosetype_hcpdd);
        mCbZTCDD = (CheckBox) view.findViewById(R.id.cb_choosetype_ztcdd);
        mCbWYBCDD = (CheckBox) view.findViewById(R.id.cb_choosetype_wybcdd);
        mCbYZCDD = (CheckBox) view.findViewById(R.id.cb_choosetype_yzcdd);

        mCbQBDD.setOnClickListener(this);
        mCbQCPDD.setOnClickListener(this);
        mCbHCPDD.setOnClickListener(this);
        mCbZTCDD.setOnClickListener(this);
        mCbWYBCDD.setOnClickListener(this);
        mCbYZCDD.setOnClickListener(this);

        initTypeView();

        RelativeLayout ll_dismiss = (RelativeLayout) view.findViewById(R.id.rl_choosetype_close);

        ll_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initTypeView() {
        switch (mType) {
            case ModuleConstants.orderTypeAll:
                mCbQBDD.setChecked(true);
                mCbQCPDD.setChecked(false);
                mCbHCPDD.setChecked(false);
                mCbZTCDD.setChecked(false);
                mCbWYBCDD.setChecked(false);
                mCbYZCDD.setChecked(false);
                break;
            case ModuleConstants.orderTypeBus:
                mCbQBDD.setChecked(false);
                mCbQCPDD.setChecked(true);
                mCbHCPDD.setChecked(false);
                mCbZTCDD.setChecked(false);
                mCbWYBCDD.setChecked(false);
                mCbYZCDD.setChecked(false);
                break;
            case ModuleConstants.orderTypeTrain:
                mCbQBDD.setChecked(false);
                mCbQCPDD.setChecked(false);
                mCbHCPDD.setChecked(true);
                mCbZTCDD.setChecked(false);
                mCbWYBCDD.setChecked(false);
                mCbYZCDD.setChecked(false);
                break;
            case ModuleConstants.orderTypeDirect:
                mCbQBDD.setChecked(false);
                mCbQCPDD.setChecked(false);
                mCbHCPDD.setChecked(false);
                mCbZTCDD.setChecked(true);
                mCbWYBCDD.setChecked(false);
                mCbYZCDD.setChecked(false);
                break;
            case ModuleConstants.orderTypeBaoche:
                mCbQBDD.setChecked(false);
                mCbQCPDD.setChecked(false);
                mCbHCPDD.setChecked(false);
                mCbZTCDD.setChecked(false);
                mCbWYBCDD.setChecked(true);
                mCbYZCDD.setChecked(false);
                break;
            case ModuleConstants.orderTypeYuezu:
                mCbQBDD.setChecked(false);
                mCbQCPDD.setChecked(false);
                mCbHCPDD.setChecked(false);
                mCbZTCDD.setChecked(false);
                mCbWYBCDD.setChecked(false);
                mCbYZCDD.setChecked(true);
                break;
        }
    }

    private void initPop() {

        this.setContentView(view);
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        this.setFocusable(false);
        this.setAnimationStyle(R.style.AnimationPreview);
        ColorDrawable dw = new ColorDrawable(Color.argb(39, 0, 0, 0));
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
    }

    public void showAtBottom(View view) {

        int height = view.getMeasuredHeight();
        Rect outRect = new Rect();
        if ((mContext) != null && !((Activity) mContext).isFinishing()) {
            ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            this.setHeight(outRect.height() - height);

            showAtLocation(view, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 2 * height);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_choosetype_qbdd:
                mType = ModuleConstants.orderTypeAll;
                break;
            case R.id.cb_choosetype_qcpdd:
                mType = ModuleConstants.orderTypeBus;
                break;
            case R.id.cb_choosetype_hcpdd:
                mType = ModuleConstants.orderTypeTrain;
                break;
            case R.id.cb_choosetype_ztcdd:
                mType = ModuleConstants.orderTypeDirect;
                break;
            case R.id.cb_choosetype_wybcdd:
                mType = ModuleConstants.orderTypeBaoche;
                break;
            case R.id.cb_choosetype_yzcdd:
                mType = ModuleConstants.orderTypeYuezu;
                break;
        }
        initTypeView();
        RxBus.getInstance().post(new OrderListEvent(mType));
        dismiss();

    }
}