package com.wxws.myticket.my.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.CommonEvent;

/**
 * desc: 优惠口令兑换窗口
 * Date: 2016-11-09 16:21
 *
 * @author jiangyan
 */
public class CouponKeyDialog extends Dialog {

    private TextView mTvCancel;
    private TextView mTvSure;
    private EditText mEditText;
    private Context mContext;

    public CouponKeyDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_couponkey);
        initView();
        initData();
    }

    private void initView() {
        CouponKeyDialog.this.setCanceledOnTouchOutside(false);
        mEditText = (EditText) findViewById(R.id.et_input);
        mTvSure = (TextView) findViewById(R.id.tv_sure);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
    }

    private void initData() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponKeyDialog.this.dismiss();
                InputMethod.closeInputMethod((Activity) mContext);
            }
        });

        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon = mEditText.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(coupon)) {
                    ToastManager.getInstance().showToast(mContext, mContext.getString(R.string.coupon_use_code_null));
                    return;
                }
                if (!StringUtils.isEnglishAndNum(coupon)) {
                    ToastManager.getInstance().showToast(mContext, mContext.getString(R.string.coupon_use_code));
                    return;
                }

                InputMethod.closeInputMethod((Activity) mContext);

                confirmCoupon(coupon);

            }
        });
    }

    private void confirmCoupon(String code) {

        HttpRequestManager.getInstance().isCoupon(code, new ApiSubscriber<JsonResult<String>>(mContext) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    dismiss();
                    RxBus.getInstance().post(new CommonEvent(CommonEvent.COUPON_REFRESH));
                }
                ToastManager.getInstance().showToast(mContext, result.getResultMsg());
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mEditText != null) {
            mEditText.setText("");// 关闭的时候，清除掉使用过的口令
        }
    }
}