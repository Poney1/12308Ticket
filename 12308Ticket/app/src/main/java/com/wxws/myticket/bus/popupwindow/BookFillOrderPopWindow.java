package com.wxws.myticket.bus.popupwindow;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.bus.entity.FillOrderDescriptionEntity;
import com.wxws.myticket.common.utils.ArithUtils;

/**
 * desc: 价格详情
 * Date: 2016-07-07 14:49
 *
 * @auther: lixiangxiang
 */
public class BookFillOrderPopWindow extends PopupWindow {

    private FragmentActivity activity;
    private View view;
    private TextView mTvFullCount;
    private TextView mTvHalfCount;
    private TextView mTvQuanCount;
    private TextView tvHalfPrice;
    private RelativeLayout rlDismiss;//点击外部消失
    private LinearLayout llFull;
    private LinearLayout llHalf;
    private LinearLayout llInsurance;
    private LinearLayout llService;
    private LinearLayout llCoupon;
    private TextView mTvInsurance;
    private TextView mTvFeeCount;

    public BookFillOrderPopWindow(FragmentActivity activity) {
        super(activity);
        this.activity = activity;
        bindViews();
        initPopMenu();
    }

    private void bindViews() {
        view = activity.getLayoutInflater().inflate(R.layout.pop_book_tradition_price, null);
        mTvFullCount = (TextView) view.findViewById(R.id.tv_full_count);

        llFull = (LinearLayout) view.findViewById(R.id.ll_full);
        llHalf = (LinearLayout) view.findViewById(R.id.ll_half);
        llInsurance = (LinearLayout) view.findViewById(R.id.ll_insurance);
        llService = (LinearLayout) view.findViewById(R.id.ll_service);
        llCoupon = (LinearLayout) view.findViewById(R.id.ll_coupon);

        mTvHalfCount = (TextView) view.findViewById(R.id.tv_half_count);
        mTvInsurance = (TextView) view.findViewById(R.id.tv_insurance_count);
        mTvFeeCount = (TextView) view.findViewById(R.id.tv_fee_count);
        mTvQuanCount = (TextView) view.findViewById(R.id.tv_quan_count);
        rlDismiss = (RelativeLayout) view.findViewById(R.id.rl_dismiss);
        rlDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BookFillOrderPopWindow.this.dismiss();
            }
        });
    }

    /**
     * 设置数据
     */
    public void setDataForPop(FillOrderDescriptionEntity bd){

        if ("-0.01".equals(bd.getHalfPrice())){
           llHalf.setVisibility(View.GONE);
        }else {
            llHalf.setVisibility(View.VISIBLE);
            mTvHalfCount.setText(String.format(activity.getString(R.string.bookTicket_pop_count),bd.getHalfPrice(),bd.getHalfCount()));
        }
        if (-1 == bd.getInsurance() ){
            llInsurance.setVisibility(View.GONE);
        }else {
            llInsurance.setVisibility(View.VISIBLE);
            double money = ArithUtils.div(bd.getInsurance(),100.00);
            mTvInsurance.setText(String.format(activity.getString(R.string.bookTicket_pop_count),String.valueOf(money),bd.getCount()));
        }
        if (-1 == bd.getServiceFee()){
            llService.setVisibility(View.GONE);
        }else {
            llService.setVisibility(View.VISIBLE);
            double fee = ArithUtils.div(bd.getServiceFee(),100.00);
            mTvFeeCount.setText(String.format(activity.getString(R.string.bookTicket_pop_count),String.valueOf(fee),bd.getCount()));
        }
        if ("0".equals(bd.getCoupon())){
            llCoupon.setVisibility(View.GONE);
        }else {
            llCoupon.setVisibility(View.VISIBLE);
            mTvQuanCount.setText(String.format(activity.getString(R.string.bookTicket_pop_coupon),bd.getCoupon()));
        }
        mTvFullCount.setText(String.format(activity.getString(R.string.bookTicket_pop_count),bd.getFullPrice(),bd.getFullCount()));

    }

    /**
     * 初始化对话框
     */
    @SuppressLint("InlinedApi")
    private void initPopMenu() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);

        this.setFocusable(false);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.argb(39, 0, 0, 0));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 打开或者关闭弹出框
     *
     * @param v
     */
    public void showPop(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        int height = v.getMeasuredHeight();

        Rect outRect = new Rect();
        if (activity != null && !activity.isFinishing()) {
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            this.setHeight(outRect.height() - height);
            showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, height+62);
        }
    }

}
