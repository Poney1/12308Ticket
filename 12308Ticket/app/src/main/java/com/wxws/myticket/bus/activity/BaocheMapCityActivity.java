package com.wxws.myticket.bus.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.adapter.BaocheSearchAdapter;
import com.wxws.myticket.bus.dao.MapCityHelper;
import com.wxws.myticket.bus.entity.BaocheCityInfo;
import com.wxws.myticket.bus.entity.BaocheMapCity;
import com.wxws.myticket.bus.entity.GetMapCityEvent;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.widgets.view.MyLetterView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * desc: 包车城市选择
 * Date: 2016-11-18 14:19
 *
 * @author jiangyan
 */
public class BaocheMapCityActivity extends BaseActivity implements MyLetterView.OnTouchingLetterChangedListener, TextWatcher {

    // Content View Elements
    private EditText etSearch;
    private ListView list;
    private MyLetterView letterview;
    private TextView tvLetter, tvNokeyword;
    private ListView listSearch;
    private ImageButton imgBtnDel;
    private FrameLayout layout_main;

    // End Of Content View Elements
    private OverlayThread overlayThread = new OverlayThread();
    private Adapter mAdapter;
    private BaocheSearchAdapter searchAdapter;
    private String tag = "MapCityActivity";

    ArrayList<BaocheCityInfo> mMapCities = new ArrayList<BaocheCityInfo>();

    InnerHandler mHandler;
    ArrayList<String> firstCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_city);
        mHandler = new InnerHandler(this);
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("选择城市");
        bindViews();
    }

    private void bindViews() {
        layout_main = (FrameLayout) findViewById(R.id.layout_main);
        etSearch = (EditText) findViewById(R.id.etSearch);
        list = (ListView) findViewById(R.id.list);
        letterview = (MyLetterView) findViewById(R.id.letterview);
        tvLetter = (TextView) findViewById(R.id.tvLetter);
        tvNokeyword = (TextView) findViewById(R.id.tvNokeyword);
        listSearch = (ListView) findViewById(R.id.listSearch);
        imgBtnDel = (ImageButton) findViewById(R.id.imgBtnDel);
        letterview.setOnTouchingLetterChangedListener(this);

        mAdapter = new Adapter(this);
        list.setAdapter(mAdapter);

        searchAdapter = new BaocheSearchAdapter(this, true);
        listSearch.setAdapter(searchAdapter);

        etSearch.addTextChangedListener(this);
        imgBtnDel.setOnClickListener(this);
        loadMapCity();
    }

    /**
     * 加载出发城市列表
     */
    private void loadMapCity() {
        List<BaocheMapCity> templist = MapCityHelper.getInstance().getList();
        if (templist == null || templist.size() == 0) {
            getMapCityFromWeb();
            return;
        }
        ArrayList<String> firstCharacters = MapCityHelper.getInstance().getShouPinList();
        MyLetterView.b = firstCharacters.toArray(new String[firstCharacters.size()]);
        for (int i = 0; i < firstCharacters.size(); ++i) {
            if (!StringUtils.isNullOrEmpty(firstCharacters.get(i)) && !"常用".equals(firstCharacters.get(i)) && !"热门".equals(firstCharacters.get(i))) {
                BaocheCityInfo c = new BaocheCityInfo();
                c.setGroup(true);
                c.setGroupName(firstCharacters.get(i));
                mMapCities.add(c);
                if (templist != null && templist.size() > 0) {
                    Iterator<BaocheMapCity> iterator = templist.iterator();
                    while (iterator.hasNext()) {
                        BaocheMapCity s = iterator.next();
                        if (!StringUtils.isNullOrEmpty(s.getInitial()) && firstCharacters.get(i).equals(s.getInitial())) {
                            c = new BaocheCityInfo();
                            c.setGroup(false);
                            c.setCityName(s.getName());
                            mMapCities.add(c);
                            iterator.remove();
                        }
                    }
                }
            }
        }
        Message msg = Message.obtain();
        msg.what = 2;
        mHandler.sendMessage(msg);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgBtnDel:
                etSearch.setText("");
                layout_main.setVisibility(View.VISIBLE);
                imgBtnDel.setVisibility(View.INVISIBLE);
                listSearch.setVisibility(View.GONE);
                tvNokeyword.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 获取web城市
     */
    private void getMapCityFromWeb() {

        HttpRequestManager.getInstance().getBaocheList(new ApiSubscriber<JsonResult<List<BaocheMapCity>>>(BaocheMapCityActivity.this,ApiSubscriber.NETNULL) {
            @Override
            public void onNext(final JsonResult<List<BaocheMapCity>> result) {
                if ("0000".equals(result.getResultCode())) {
                    new Thread() {

                        @Override
                        public void run() {
                            super.run();
                            List<BaocheMapCity> list = new ArrayList<BaocheMapCity>();
                            if (result.getObject() == null || result.getObject().size() <= 0) {
                                Message msg = Message.obtain();
                                msg.what = 3;
                                mHandler.sendMessage(msg);
                                return;
                            }
                            list.addAll(result.getObject());
                            firstCharacters = new ArrayList<String>();
                            mMapCities = new ArrayList<BaocheCityInfo>();
                            Iterator<BaocheMapCity> iterator = list.iterator();
                            while (iterator.hasNext()) {
                                BaocheMapCity mapCity = iterator.next();
                                if (StringUtils.isNullOrEmpty(mapCity.getInitial()) || StringUtils.isNullOrEmpty(mapCity.getFull_pinyin()) || mapCity.getParentId().startsWith("7") || mapCity.getParentId().startsWith("8")) {
                                    iterator.remove();
                                    continue;
                                }
                                if (mapCity.getInitial().length() > 0) {
                                    String gName = mapCity.getInitial().toUpperCase();
                                    if (!firstCharacters.contains(gName)) {
                                        firstCharacters.add(gName);
                                        BaocheCityInfo c = new BaocheCityInfo();
                                        c.setGroup(true);
                                        c.setGroupName(gName);
                                        mMapCities.add(c);
                                    }
                                    BaocheCityInfo c = new BaocheCityInfo();
                                    c.setGroup(false);
                                    c.setGroupName(gName);
                                    c.setCityName(mapCity.getName());
                                    mMapCities.add(c);
                                }
                            }

                            if (firstCharacters != null) {
                                Collections.sort(firstCharacters);
                            }
                            if (mMapCities != null) {
                                Collections.sort(mMapCities, new SortByGroupName());
                            }

                            if (firstCharacters != null && firstCharacters.size() > 0 && mMapCities != null && mMapCities.size() > 0) {
                                Message msg = Message.obtain();
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                                MapCityHelper.getInstance().addAll(list);
                            } else {
                                //没有数据
                                Message msg = Message.obtain();
                                msg.what = 3;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();

                } else {
                    showErrorOrNoData(getString(R.string.loaddata_fail), R.string.click_return, R.mipmap.tip1);
                }
            }
        });
    }

    static class InnerHandler extends Handler {
        WeakReference<BaocheMapCityActivity> activityWeakReference;

        public InnerHandler(BaocheMapCityActivity cityActivity) {
            this.activityWeakReference = new WeakReference<BaocheMapCityActivity>(cityActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaocheMapCityActivity cityActivity = activityWeakReference.get();
            if (cityActivity == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    MyLetterView.b = cityActivity.firstCharacters.toArray(new String[cityActivity.firstCharacters.size()]);
                    cityActivity.mAdapter.reload(cityActivity.mMapCities);
                    cityActivity.mLlLayoutLoading.setVisibility(View.GONE);
                    break;
                case 2:
                    cityActivity.mAdapter.reload(cityActivity.mMapCities);
                    cityActivity.mLlLayoutLoading.setVisibility(View.GONE);
                    break;
                case 3:
                    cityActivity.showErrorOrNoData(cityActivity.getString(R.string.query_station_fail), R.string.click_return, R.mipmap.tip1);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onTouchingLetterChanged(String s) {
        tvLetter.setText(s);
        tvLetter.setVisibility(View.VISIBLE);
        int position = alphaIndexer(s);
        if (position >= 0 && position < list.getCount()) {
            list.setSelection(position);
        }
        mHandler.removeCallbacks(overlayThread);
        mHandler.postDelayed(overlayThread, 1000);
    }


    public int alphaIndexer(String s) {
        int i = 0;
        while (i < mMapCities.size()) {
            if (mMapCities.get(i).isGroup() && mMapCities.get(i).getGroupName().equals(s)) {
                break;
            }
            ++i;
        }
        return i;
    }

    private class OverlayThread implements Runnable {
        public void run() {
            tvLetter.setVisibility(View.GONE);
        }
    }

    private class Adapter extends BaseAdapter {
        private Context context;
        ArrayList<BaocheCityInfo> cityList;

        public Adapter(Context context) {
            super();
            this.context = context;
        }

        public void reload(ArrayList<BaocheCityInfo> citys) {
            if (cityList != null) {
                cityList.clear();
            }
            cityList = citys;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return cityList == null ? 0 : cityList.size();
        }

        @Override
        public BaocheCityInfo getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_city_list, parent, false);
                holder = new ViewHolder();
                holder.tvGroupTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
                holder.tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
                holder.layout_container = (LinearLayout) convertView.findViewById(R.id.layout_container);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final BaocheCityInfo item = getItem(position);
            if (item.isGroup()) {
                holder.tvGroupTitle.setVisibility(View.VISIBLE);
                holder.tvCityName.setVisibility(View.GONE);
                holder.tvGroupTitle.setText(item.getGroupName());
            } else {
                holder.tvCityName.setVisibility(View.VISIBLE);
                holder.tvGroupTitle.setVisibility(View.GONE);
                holder.tvCityName.setText(item.getCityName());
            }

            holder.layout_container.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 不是点击组名则跳转返回
                    if (!item.isGroup()) {
                        RxBus.getInstance().post(new GetMapCityEvent(item.getCityName()));
                        finish();
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView tvGroupTitle;
            private TextView tvCityName;
            private LinearLayout layout_container;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(tag, "beforeTextChanged:" + s.toString() + ",start=" + start + ",after=" + after + ",count=" + count);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(tag, "onTextChanged:" + s.toString() + ",start=" + start + ",before=" + before + ",count=" + count);
    }

    @Override
    public void afterTextChanged(Editable e) {
        Log.d(tag, "afterTextChanged:" + e.toString());
        layout_main.setVisibility(View.GONE);
        imgBtnDel.setVisibility(View.VISIBLE);
        listSearch.setVisibility(View.VISIBLE);

        // 在此写搜索事件
        new SearchTask().execute(e.toString());
    }

    class SearchTask extends AsyncTask<String, Void, ArrayList<BaocheCityInfo>> {

        @Override
        protected ArrayList<BaocheCityInfo> doInBackground(String... params) {
            ArrayList<BaocheCityInfo> citys = new ArrayList<BaocheCityInfo>();

            List<BaocheMapCity> startcitys = MapCityHelper.getInstance().getListByStr(params[0]);
            if (startcitys != null) {
                for (BaocheMapCity s : startcitys) {
                    BaocheCityInfo c = new BaocheCityInfo();
                    c.setGroup(false);
                    c.setCityName(s.getName());
                    citys.add(c);
                }
            }


            return citys;
        }

        @Override
        protected void onPostExecute(ArrayList<BaocheCityInfo> result) {
            super.onPostExecute(result);
            if (result != null && result.size() > 0) {
                searchAdapter.reloadData(result);
                tvNokeyword.setVisibility(View.GONE);
                listSearch.setVisibility(View.VISIBLE);

            } else {
                listSearch.setVisibility(View.GONE);
                tvNokeyword.setVisibility(View.VISIBLE);
                tvNokeyword.setText(String.format("未找到与%s相关的城市", etSearch.getText()));
            }

        }

    }

    @Override
    protected void onDestroy() {
       //TODO  mWebAPI.cancelAllRequest();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    /**
     * 点击重试
     */
    public void clickReload() {
        getMapCityFromWeb();
    }

    private class SortByGroupName implements Comparator {
        @Override
        public int compare(java.lang.Object lhs, java.lang.Object rhs) {
            BaocheCityInfo s1 = (BaocheCityInfo) lhs;
            BaocheCityInfo s2 = (BaocheCityInfo) rhs;
            return s1.getGroupName().compareTo(s2.getGroupName());
        }
    }

}
