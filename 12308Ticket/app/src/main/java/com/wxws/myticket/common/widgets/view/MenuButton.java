package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxws.myticket.R;

/**
 * desc: 主页的自定义button
 * Date: 2016/10/9 10:56
 *
 * @auther: lixiangxiang
 */
public class MenuButton extends RelativeLayout {

    /**
     * menu的文字
     */
    private TextView mTvMenu;
    /**
     * menu的图片
     */
    private ImageView mIVMenu;


    public MenuButton(Context context) {
        super(context);
        init(context, null);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 设置选中状态
     */
    public void setSelected(boolean selected){
        mTvMenu.setSelected(selected);
        mIVMenu.setSelected(selected);
    }

    /**
     * 设置文字
     * @param text
     */
    public void setText(String text){
        mTvMenu.setText(text);
    }

    /**
     * 初始化View
     */
    private void init(Context context,  AttributeSet attrs){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view =inflater.inflate(R.layout.menu_button, this, true);

        mTvMenu = (TextView) view.findViewById(R.id.menu_name);
        mIVMenu= (ImageView) view.findViewById(R.id.menu_image);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.MenuButton);

        int nameRes = array.getResourceId(R.styleable.MenuButton_menuText, -1);
        int imageRes = array.getResourceId(R.styleable.MenuButton_menuImage,-1);

        if (nameRes != -1) {
            mTvMenu.setText(nameRes);
        }
        if (imageRes != -1) {
            mIVMenu.setImageResource(imageRes);
        }
        array.recycle();
    }
}
