package com.wxws.myticket.train.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.app.MainActivity;
import com.wxws.myticket.common.utils.ThreadManager;

import java.lang.ref.WeakReference;



/**
 * desc: 火车票抢票中dialog
 * Date: 2016-07-28 14:37
 *
 * @auther: jiangyan
 */
public class BookingDialog extends Dialog implements View.OnClickListener {

    private Activity mContext;
    private ImageView imgAnimation;
    private TextView tvNum;
    private Button btnCancel;
    private LinearLayout llRobTicket;
    private LinearLayout llNoRob;
    private int failure = 1;
    private Animation animation;
    private InnerHandler innerHandler;

    public BookingDialog(Activity context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_book_ing);
        BookingDialog.this.setCanceledOnTouchOutside(false);
        BookingDialog.this.setCancelable(false);
        initView();
        initData();
    }

    private void initData() {

        innerHandler = new InnerHandler(BookingDialog.this);

        animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_rotate_ticket);

        LinearInterpolator rotate = new LinearInterpolator();

        animation.setInterpolator(rotate);

        imgAnimation.startAnimation(animation);

        btnCancel.setOnClickListener(this);
    }

    private void initView() {
        imgAnimation = (ImageView) findViewById(R.id.img_animation);
        tvNum = (TextView) findViewById(R.id.tv_num);
        llNoRob = (LinearLayout) findViewById(R.id.ll_no_rob);
        llRobTicket = (LinearLayout) findViewById(R.id.ll_rob_ticket);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
    }

    /**
     * 抢票次数
     *
     * @param count
     */
    public void setTicketCount(int count) {
        tvNum.setText(String.format("已抢票%s次", count));
    }


    //初始化view
    public void setInitView() {
        llRobTicket.setVisibility(View.VISIBLE);
        llNoRob.setVisibility(View.GONE);
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 关闭 flag
     */
    public void colseFlag() {
        flag = false;
        iDialog = 0;
    }

    @Override
    public void onClick(View v) {

        switch (failure) {
            case 1:
                iDialog = 0;
                flag = false;
                animation.cancel();
                BookingDialog.this.dismiss();
                break;
            default:
                iDialog = 0;
                animation.cancel();
                flag = false;
                BookingDialog.this.dismiss();
                Intent intent = new Intent(mContext, MainActivity.class);
//                EventBus.getDefault().post(new BaseEvent(BaseEvent.VIEW_ORDER));
//                PreferencesUtils.putInt(mContext,PreferencesUtils.PREFERENCE_NAME_DATA, Constants.FIRSTENTERORDER,OrderListTypeEvent.TRAINTYPE);
//                EventBus.getDefault().post(new OrderListTypeEvent(OrderListTypeEvent.TRAINTYPE)); // 跳转到火车票界面
                mContext.startActivity(intent);
                mContext.finish();
                break;
        }
    }

    long time2;
    int iDialog = 0;
    boolean flag = true;
    long sub;

    public void refreshDialog(String content) {
        flag = true;
        failure = 0;
        iDialog = 0;
        setTicketCount(iDialog);
        imgAnimation.startAnimation(animation);
        btnCancel.setText(content);
        time2 = System.currentTimeMillis();
        ThreadManager.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    long time1 = System.currentTimeMillis();
                    sub = (time1 - time2) / 1000;
                    if (sub >= 3) {
                        iDialog++;
                        time2 = time1;
                        innerHandler.sendEmptyMessage(0);
                    }
                }
            }
        });
    }


    private static class InnerHandler extends Handler {

        WeakReference<BookingDialog> weakReference;

        public InnerHandler(BookingDialog bookingDialog) {
            this.weakReference = new WeakReference<>(bookingDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            BookingDialog mContext = weakReference.get();
            if (mContext == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    mContext.setTicketCount(mContext.iDialog);
                    break;
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        innerHandler.removeMessages(0);
    }
}