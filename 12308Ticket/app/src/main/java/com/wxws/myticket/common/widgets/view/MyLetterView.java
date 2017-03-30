package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wxws.myticket.common.utils.function.ScreenUtil;

/**
 * @author ljf
 * @version 2015年5月6日 下午6:22:04
 */
public class MyLetterView extends View {
    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    Context mContext;

    public MyLetterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public MyLetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MyLetterView(Context context) {
        super(context);
        this.mContext = context;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / 24;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            float yPos = singleHeight * i + singleHeight;
                if (null != b[i]) {
                    paint.setTextSize(ScreenUtil.dip2px(mContext, 12f));
                    float xPos = width / 2 - paint.measureText(b[i]) / 2;
                    paint.setColor(Color.parseColor("#8b8b8b"));
                    if (i == choose) {
                        paint.setColor(Color.parseColor("#00b9ef"));
                        paint.setFakeBoldText(true);
                    }
                    canvas.drawText(b[i], xPos, yPos, paint);
                }
            paint.reset();
        }

    }

    private boolean showBkg = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * 24);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
         void onTouchingLetterChanged(String s);
    }

}
