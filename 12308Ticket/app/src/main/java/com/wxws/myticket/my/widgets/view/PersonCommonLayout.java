package com.wxws.myticket.my.widgets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;

/**
 * desc: 个人中心通用模块，后续根据UI重新设计属性等。
 * Date: 2016-10-25 14:06
 *
 * @author jiangyan
 */
public class PersonCommonLayout extends LinearLayout {

    private Context mContext;
    private ImageView mIvIcon;
    private TextView mTvText;
    private TextView mTvInto;

    public PersonCommonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(attrs);
    }

    public PersonCommonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(attrs);

    }

    private void initView(AttributeSet attrs) {

        View rootView = View.inflate(mContext, R.layout.view_personcommonlayout, this);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs
                , R.styleable.PersonCommonLayout);

        mIvIcon = (ImageView) rootView.findViewById(R.id.iv_icon);
        mTvText = (TextView) rootView.findViewById(R.id.tv_text);
        mTvInto = (TextView) rootView.findViewById(R.id.tv_into);

        int imgSrc = typedArray.getResourceId(R.styleable.PersonCommonLayout_icon, 0);//TODO 设置默认icon
        if (imgSrc != 0) {
            mIvIcon.setVisibility(VISIBLE);
            mIvIcon.setBackgroundResource(imgSrc);
        } else {
            mIvIcon.setVisibility(GONE);
        }

        String textStr = typedArray.getString(R.styleable.PersonCommonLayout_text);
        mTvText.setText(textStr);

        String textInfo = typedArray.getString(R.styleable.PersonCommonLayout_textinfo);
        int color = typedArray.getColor(R.styleable.PersonCommonLayout_textinfocolor, getResources().getColor(R.color.c_999999));
        mTvInto.setText(textInfo);
        mTvInto.setTextColor(color);

        typedArray.recycle();
    }

    /**
     * 设置右边数据
     *
     * @param textInfo
     */
    public void setTextInfo(String textInfo) {
        mTvInto.setText(textInfo);
    }


}