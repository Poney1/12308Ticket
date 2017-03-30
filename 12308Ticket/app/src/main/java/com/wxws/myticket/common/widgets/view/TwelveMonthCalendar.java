package com.wxws.myticket.common.widgets.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.bus.interfaces.OnDaySelectListener;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.common.widgets.view.GridViewForScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TwelveMonthCalendar extends LinearLayout {

	private Context context;

	View view;
	private GridViewForScrollView gv;
	public calendarGridViewAdapter gridViewAdapter;
	private List<String> gvList;

	private String choiceMonth;// 选中的年月
	private String choiceYear;
	private HashMap<String, String> hashMap;
	private String selected = "-1";

	private OnDaySelectListener callBack;// 回调函数
	/**
	 * 构造函数
	 * @param context
	 */
	public TwelveMonthCalendar(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 构造函数
	 * @param context
	 */
	public TwelveMonthCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/**
	 * 初始化日期以及view等控件
	 */
	private void init() {
		gvList = new ArrayList<>();
		view = LayoutInflater.from(context).inflate(R.layout.comm_calendar, this, true);// 获取布局，开始初始化
		gv = (GridViewForScrollView) view.findViewById(R.id.gv_calendar);
		gridViewAdapter = new calendarGridViewAdapter();
		gv.setAdapter(gridViewAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
				String choiceDay = (String) adapterView.getAdapter().getItem(position);
				if (!"".equals(choiceDay)) {
//					sd
					gridViewAdapter.notifyDataSetChanged();
					if (callBack != null) {// 调用回调函数回调数据
						callBack.onDaySelectListener(choiceDay);
					}
				}
			}
		});
	}

	private String selectday = "";

	public String getSelectItem() {
		return selectday;
	}

	public void setSelectItem(String selectday) {
		this.selectday = selectday;
	}

	public String getMonth() {
		return choiceMonth;
	}

	public String getYear() {
		return choiceYear;
	}

	public void setDateData(Date date, HashMap<String, String> hashmap) {
		this.hashMap = hashmap;
		init();
		final Calendar cal = Calendar.getInstance();// 获取日历实例
		selectday = cal.get(Calendar.DAY_OF_MONTH) + "";
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);// cal设置当前day为当前月第一天
		choiceMonth = (cal.get(Calendar.MONTH) + 1) + "";
		choiceYear = cal.get(Calendar.YEAR) + "";
		int tempSum = countNeedHowMuchEmpety(cal);// 获取当前月第一天为星期几
		int dayNumInMonth = getDayNumInMonth(cal);// 获取当前月有多少天
		setGvListData(tempSum, dayNumInMonth);
	}

	/**
	 * 为gridview中添加需要展示的数据
	 */
	private void setGvListData(int tempSum, int dayNumInMonth) {
		gvList.clear();
		for (int i = 0; i < tempSum; i++) {
			gvList.add("");
		}

		for (int j = 1; j <= dayNumInMonth; j++) {
			gvList.add(String.valueOf(j));
		}

		gridViewAdapter.notifyDataSetChanged();
	}

	/**
	 * 获取当前月的总共天数
	 * @param cal
	 * @return
	 */
	private int getDayNumInMonth(Calendar cal) {
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 获取当前月第一天在第一个礼拜的第几天，得出第一天是星期几
	 * @param cal
	 * @return
	 */
	private int countNeedHowMuchEmpety(Calendar cal) {
		int firstDayInWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return firstDayInWeek;
	}

	/**
	 * gridview中adapter的viewholder
	 * @author Administrator
	 */
	static class GrideViewHolder {

		TextView tvDay;
		TextView tvDesc;
		LinearLayout layout_day;
	}

	/**
	 * gridview的adapter
	 * @author Administrator
	 */
	public class calendarGridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return gvList.size();
		}

		@Override
		public String getItem(int position) {
			return gvList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GrideViewHolder holder;
			if (convertView == null) {
				holder = new GrideViewHolder();
				convertView = inflate(context, R.layout.common_calendar_gridview_item, null);
				holder.layout_day = (LinearLayout) convertView.findViewById(R.id.layout_day);
				holder.tvDay = (TextView) convertView.findViewById(R.id.tv_calendar_day);
				holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
				convertView.setTag(holder);
			}
			else {
				holder = (GrideViewHolder) convertView.getTag();
			}
			holder.tvDay.setText(getItem(position));
			if (!getItem(position).equals("")) {
				String key = choiceYear + choiceMonth + getItem(position);
				LogUtils.d("key=========1-"+key);
				LogUtils.d("DateTimeUtil.getTomorrow()"+ DateTimeUtil.getTomorrow());
				if (hashMap.containsKey(key)) {
					if (selectday.equals(getItem(position))) {
						holder.layout_day.setBackgroundResource(R.drawable.shape_red_circle);
						holder.tvDay.setTextColor(getResources().getColor(android.R.color.white));
					} else if (DateTimeUtil.getToday().equals(key)) {
						holder.tvDay.setTextColor(getResources().getColor(R.color.app_style));
						holder.tvDesc.setVisibility(View.GONE);
						holder.tvDesc.setTextColor(getResources().getColor(android.R.color.white));
						holder.tvDay.setText("今天");
					} else if (DateTimeUtil.getTomorrow().equals(key)) {
						holder.tvDay.setTextColor(getResources().getColor(R.color.app_style));
						holder.tvDesc.setVisibility(View.GONE);
						holder.tvDesc.setTextColor(getResources().getColor(android.R.color.white));
						holder.tvDay.setText("明天");
					}else if(DateTimeUtil.getTheDayData().equals(key)){//后天
						holder.tvDay.setTextColor(getResources().getColor(R.color.app_style));
						holder.tvDesc.setVisibility(View.GONE);
						holder.tvDesc.setTextColor(getResources().getColor(android.R.color.white));
						holder.tvDay.setText("后天");
					} else {
						holder.tvDay.setTextColor(getResources().getColor(R.color.app_style));
					}
				} else {
					holder.tvDay.setTextColor(getResources().getColor(R.color.c_cccccc));
				}
			}
			return convertView;
		}

	}

	/**
	 * 自定义监听接口设置对象
	 * @param o
	 */
	public void setOnDaySelectListener(OnDaySelectListener o) {
		callBack = o;
	}

	public Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
}
