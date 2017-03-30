package com.wxws.myticket.bus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.entity.BaocheLocalInfo;
import com.wxws.myticket.bus.entity.BaochePOIEntity;
import com.wxws.myticket.bus.entity.GetMapCityEvent;
import com.wxws.myticket.bus.entity.GetPOIEvent;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.InputMethod;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.widgets.view.ClearEditText;

import java.util.ArrayList;


/**
 * desc: 包车POI搜索
 * Date: 2016-11-18 14:19
 *
 * @author jiangyan
 */
public class BaochePOIActivity extends BaseActivity implements TextWatcher, OnGetGeoCoderResultListener, BDLocationListener {
    private ListView listview;
    private ArrayList<BaochePOIEntity> list = new ArrayList<BaochePOIEntity>();
    TextView tvCity;
    ClearEditText etSearch;
    ImageButton imgBtnDel;
    TextView tvCancel;
    TextView tvNokeyword;
    PoiAdapter mPoiAdapter;
    SuggestionSearch mSuggestionSearch;
    String city = "深圳";
    private int editId;
    GeoCoder mGeoCoder;
    private LocationClient mLocationClient;
    private BaocheLocalInfo mLocalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        Intent intent = getIntent();
        editId = intent.getIntExtra("editId", 0);
        mLocalInfo = (BaocheLocalInfo) PreferencesUtils.readDataObject(this, ConfigConstants.LOCATIONINFO);

        if (mLocalInfo == null) {
            mLocalInfo = new BaocheLocalInfo();
        }

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(poiListener);
        if (!StringUtils.isNullOrEmpty(mLocalInfo.getCity())) {
            city = mLocalInfo.getCity();
        }

        initView();
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(mLocationClient);
        mLocationClient.start();

    }

    private void initView() {
        bindLoadingView();
        tvCity = (TextView) findViewById(R.id.tvCity);
        etSearch = (ClearEditText) findViewById(R.id.etSearch);
        imgBtnDel = (ImageButton) findViewById(R.id.imgBtnDel);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvNokeyword = (TextView) findViewById(R.id.tvNokeyword);
        listview = (ListView) findViewById(R.id.pullList);
        tvCity.setText(city);
        etSearch.addTextChangedListener(this);
        tvCity.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        if (editId == R.id.tv_baoche_startplace) {
            etSearch.setHint("你要从哪出发");
        } else {
            etSearch.setHint("你要去哪儿");
        }

        mPoiAdapter = new PoiAdapter(this);
        listview.setAdapter(mPoiAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                finish();
                break;
            case R.id.tvCity:
                startActivityWithAnim(BaocheMapCityActivity.class);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0) {
            if (!StringUtils.isNullOrEmpty(city)) {
                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(s.toString()));
            }
            showLoading();
        } else {
            if (mLocalInfo != null && city != null && city.equals(mLocalInfo.getCity())) {
                //第一次打开的根据定位获取周边POI
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(mLocalInfo.getLatitude(), mLocalInfo.getLongitude())));
                showLoading();
            } else {
                mLlLayoutLoading.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                tvNokeyword.setText("请在上面搜索框输入关键字");
                tvNokeyword.setVisibility(View.VISIBLE);
            }
        }

    }

    OnGetSuggestionResultListener poiListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult result) {
            mLlLayoutLoading.setVisibility(View.GONE);
            //获取POI检索结果
            if (result != null) {
                list.clear();
                if (result.getAllSuggestions() != null && result.getAllSuggestions().size() > 0) {
                    listview.setVisibility(View.VISIBLE);
                    tvNokeyword.setVisibility(View.GONE);
                    for (SuggestionResult.SuggestionInfo suggestionInfo : result.getAllSuggestions()) {
                        if (suggestionInfo.pt != null && suggestionInfo.pt.latitude != 0 && suggestionInfo.pt.longitude != 0) {
                            list.add(new BaochePOIEntity(suggestionInfo.key, suggestionInfo.district, suggestionInfo.city, suggestionInfo.district, suggestionInfo.pt));
                        }
                    }
                    mPoiAdapter.reload(list);
                } else {
                    listview.setVisibility(View.GONE);
                    tvNokeyword.setVisibility(View.VISIBLE);
                    tvNokeyword.setText(String.format("未找到%s,换个关键字试试", etSearch.getText()));
                }
            } else {
                listview.setVisibility(View.GONE);
                tvNokeyword.setVisibility(View.VISIBLE);
                tvNokeyword.setText(String.format("未找到%s,换个关键字试试", etSearch.getText()));
            }
        }
    };

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        mLlLayoutLoading.setVisibility(View.GONE);
        if (reverseGeoCodeResult.getPoiList() != null && reverseGeoCodeResult.getPoiList().size() > 0) {
            listview.setVisibility(View.VISIBLE);
            list.clear();
            if (reverseGeoCodeResult.getPoiList().size() > 0) {
                tvNokeyword.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                for (PoiInfo poiInfo : reverseGeoCodeResult.getPoiList()) {
                    list.add(new BaochePOIEntity(poiInfo.name, poiInfo.address, poiInfo.city, poiInfo.address, poiInfo.location));
                }

                mPoiAdapter.reload(list);
            } else {
                listview.setVisibility(View.GONE);
                tvNokeyword.setText("请在上面搜索框输入关键字");
                tvNokeyword.setVisibility(View.VISIBLE);
            }
        } else {
            listview.setVisibility(View.GONE);
            tvNokeyword.setText("请在上面搜索框输入关键字");
            tvNokeyword.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            String city = bdLocation.getCity();
            double lat = bdLocation.getLatitude();
            double lng = bdLocation.getLongitude();
            if (!StringUtils.isNullOrEmpty(city)) {
                mLocalInfo.setCity(city.replace("市", ""));
            }
            String province = bdLocation.getProvince();
            if (!StringUtils.isNullOrEmpty(province)) {
                mLocalInfo.setProvince(province);
            }
            mLocalInfo.setLatitude(lat);
            mLocalInfo.setLongitude(lng);

            PreferencesUtils.saveDataObject(this, ConfigConstants.LOCATIONINFO, mLocalInfo);

            //第一次打开的根据定位获取周边POI
            mGeoCoder = GeoCoder.newInstance();
            mGeoCoder.setOnGetGeoCodeResultListener(this);
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(new LatLng(mLocalInfo.getLatitude(), mLocalInfo.getLongitude())));
            mLocationClient.stop();
        }
    }

    private class PoiAdapter extends BaseAdapter {
        private Context context;
        ArrayList<BaochePOIEntity> mPoiInfos;

        public PoiAdapter(Context context) {
            super();
            this.context = context;
        }

        public void reload(ArrayList<BaochePOIEntity> poiInfos) {
            mPoiInfos = poiInfos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mPoiInfos == null ? 0 : mPoiInfos.size();
        }

        @Override
        public BaochePOIEntity getItem(int position) {
            return mPoiInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_poi_list, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final BaochePOIEntity poiInfo = getItem(position);
            holder.tvTitle.setText(poiInfo.getKey());
            holder.tvAddress.setText(poiInfo.getAddress());
            if (StringUtils.isNullOrEmpty(poiInfo.getCity()) && !StringUtils.isNullOrEmpty(city)) {
                poiInfo.setCity(city);
            }
            holder.layoutcontainer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    RxBus.getInstance().post(new GetPOIEvent(editId, poiInfo));
                    InputMethod.closeInputMethod(BaochePOIActivity.this);
                    finish();
                }
            });
            return convertView;
        }


        public class ViewHolder {
            public final TextView tvTitle;
            public final TextView tvAddress;
            public final LinearLayout layoutcontainer;
            public final View root;

            public ViewHolder(View root) {
                tvTitle = (TextView) root.findViewById(R.id.tvTitle);
                tvAddress = (TextView) root.findViewById(R.id.tvAddress);
                layoutcontainer = (LinearLayout) root.findViewById(R.id.layout_container);
                this.root = root;
            }
        }
    }

    @Subscribe
    public void getMapCityEvent(GetMapCityEvent event) {
        if (event != null && !StringUtils.isNullOrEmpty(event.getCityName())) {
            tvCity.setText(event.getCityName());
            city = event.getCityName();
            etSearch.setText("");
            if (mLocalInfo != null && city.equals(mLocalInfo.getCity())) {
                //第一次打开的根据定位获取周边POI
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(mLocalInfo.getLatitude(), mLocalInfo.getLongitude())));
                showLoading();
            } else {
                mLlLayoutLoading.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                tvNokeyword.setText("请在上面搜索框输入关键字");
                tvNokeyword.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(this);
        mSuggestionSearch.destroy();
        super.onDestroy();
    }
}
