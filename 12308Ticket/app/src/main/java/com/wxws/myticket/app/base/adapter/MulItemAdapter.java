package com.wxws.myticket.app.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * Date: 2016/10/17 11:00
 *
 * @auther: lixiangxiang
 */
public abstract class MulItemAdapter<T> extends BaseAdapter {

    protected List<T> data;
    private Context context;
    private MulItemTypeSupport mulItemTypeSupport;

    public MulItemAdapter(Context context, List<T> data, MulItemTypeSupport mulItemTypeSupport) {
        this.data = data;
        this.context = context;
        this.mulItemTypeSupport = mulItemTypeSupport;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= data.size()) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (mulItemTypeSupport != null)
            return mulItemTypeSupport.getViewTypeCount();
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mulItemTypeSupport !=null)
            return  mulItemTypeSupport.getItemViewType(position,data.get(position));
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MulViewHolder holder = null;
        int layoutId = mulItemTypeSupport.getLayoutId(position,data.get(position));
        if (convertView == null) {
            convertView = View.inflate(context, layoutId, null);
            holder = new MulViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MulViewHolder) convertView.getTag();
        }
        return getItemView(holder, position, convertView);
    }

    public abstract View getItemView(MulViewHolder holder, int position, View view);

    /**
     * 添加elem中的所有数据
     *
     * @param elem list数据
     */
    public void addAll(List elem) {
        if (elem == null) {
            elem = new ArrayList();
        }
        data.addAll(elem);
        notifyDataSetChanged();
    }

    /**
     * 删除某个数据
     *
     * @param index 指定位置
     */
    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }


    /**
     * 替换所有数据
     *
     * @param elem list数据
     */
    public void replaceAll(List elem) {
        data.clear();
        if (elem != null) {
            data.addAll(elem);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    protected class MulViewHolder {
        private SparseArray<View> views = new SparseArray<>();
        private View convertView;

        public MulViewHolder(View view) {
            this.convertView = view;
        }

        public <T extends View> T getView(int layoutId) {

            View view = views.get(layoutId);
            if (view == null) {
                view = convertView.findViewById(layoutId);
                views.put(layoutId, view);
            }
            return (T) view;
        }
    }
}
