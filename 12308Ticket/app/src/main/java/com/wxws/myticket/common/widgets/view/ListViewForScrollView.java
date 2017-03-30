package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * desc: 适配scrollviwe 重新测量
 * Date: 2016/10/21 11:21
 *
 * @auther: lixiangxiang
 */
public class ListViewForScrollView  extends ListView{
    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
