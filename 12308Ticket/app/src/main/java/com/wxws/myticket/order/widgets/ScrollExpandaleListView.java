package com.wxws.myticket.order.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * desc:
 * Date: 2016-11-02 15:25
 *
 * @author jiangyan
 */
public class ScrollExpandaleListView extends ExpandableListView {

    public ScrollExpandaleListView(Context context) {
        super(context);
    }

    public ScrollExpandaleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollExpandaleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollExpandaleListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                View.MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}