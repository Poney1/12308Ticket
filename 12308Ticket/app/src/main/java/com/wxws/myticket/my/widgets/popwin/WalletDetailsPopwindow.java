package com.wxws.myticket.my.widgets.popwin;

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
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.my.Entity.WalletTypeEvent;

/**
 * desc: 选择全部、收入、支出
 * Date: 2016-11-24 15:32
 *
 * @author jiangyan
 */
public class WalletDetailsPopwindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private View view;

    private int mType;// 1 全部，2 收入，3 支出
    private CheckBox mCbAll;
    private CheckBox mCbIncome;
    private CheckBox mCbExpense;

    public WalletDetailsPopwindow(Context context, int type) {
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
        view = View.inflate(mContext, R.layout.pop_walletdetails, null);

        mCbAll = (CheckBox) view.findViewById(R.id.cb_walletdetails_all);
        mCbIncome = (CheckBox) view.findViewById(R.id.cb_walletdetails_income);
        mCbExpense = (CheckBox) view.findViewById(R.id.cb_walletdetails_expense);

        mCbAll.setOnClickListener(this);
        mCbIncome.setOnClickListener(this);
        mCbExpense.setOnClickListener(this);

        initTypeView();

        RelativeLayout ll_dismiss = (RelativeLayout) view.findViewById(R.id.rl_walletdetails_close);

        ll_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initTypeView() {
        switch (mType) {
            case 1:// 全部
                mCbAll.setChecked(true);
                mCbIncome.setChecked(false);
                mCbExpense.setChecked(false);
                break;
            case 2:// 收入
                mCbAll.setChecked(false);
                mCbIncome.setChecked(true);
                mCbExpense.setChecked(false);
                break;
            case 3://支出
                mCbAll.setChecked(false);
                mCbIncome.setChecked(false);
                mCbExpense.setChecked(true);
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
            case R.id.cb_walletdetails_all:
                mType = 1;
                break;
            case R.id.cb_walletdetails_income:
                mType = 2;
                break;
            case R.id.cb_walletdetails_expense:
                mType = 3;
                break;

        }
        initTypeView();
        RxBus.getInstance().post(new WalletTypeEvent(mType));
        dismiss();

    }
}