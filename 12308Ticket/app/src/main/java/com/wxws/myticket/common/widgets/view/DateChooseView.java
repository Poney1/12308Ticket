package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wxws.myticket.R;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;

/**
 * desc: 前一天 后一天
 * Date: 2016-10-10 14:58
 *
 * @author jiangyan
 */
public class DateChooseView extends LinearLayout {

    private TextView tvPre;
    private TextView tvDate;
    private TextView tvNext;

    private Context mContext;
    private String mCurrentDate;// 当前控件日期

    private DateChooseViewClickListener mClickListener;
    private String year;
    private String month;
    private String day;

    public DateChooseView(Context context) {
        super(context);
        initView(context);
    }

    public DateChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DateChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View rootView = View.inflate(context, R.layout.view_datechoose, this);
        tvPre = (TextView) rootView.findViewById(R.id.tv_pre);
        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        tvNext = (TextView) rootView.findViewById(R.id.tv_next);

        tvPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String preDate = DateTimeUtil.addDay(mCurrentDate, -1);
                if (DateTimeUtil.Lessthantoday(preDate)) {
                    ToastManager.getInstance().showToast(mContext, "亲，不能超过预售期哦");
                    return;
                }
                mCurrentDate = preDate;
                setMiddleDate(mCurrentDate);

                mClickListener.preClick(mCurrentDate);
            }
        });

        tvDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.dateClick(year,month,day);
            }
        });

        tvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String nextDate = DateTimeUtil.addDay(mCurrentDate, 1);
                if (DateTimeUtil.Morethanpredate(nextDate, 60 - 1)) {
                    ToastManager.getInstance().showToast(mContext, "亲，不能超过预售期哦");
                    return;
                }
                mCurrentDate = nextDate;
                setMiddleDate(mCurrentDate);

                mClickListener.nextClick(mCurrentDate);
            }
        });

    }

    //设定中间显示的时间
    public void setMiddleDate(@NonNull String dataStr)  {
        try {
            String[] splits = dataStr.split("-");
            year = splits[0];
            month = splits[1];
            day = splits[2];
            if (tvDate != null) {
                tvDate.setText(DateTimeUtil.getSpChineseMMDD(dataStr));
                mCurrentDate = dataStr;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface DateChooseViewClickListener {
        void preClick(String date);

        void dateClick(String year,String month,String day);

        void nextClick(String date);
    }

    public void setDateChooseViewClickListener(DateChooseViewClickListener listener) {
        this.mClickListener = listener;
    }
}