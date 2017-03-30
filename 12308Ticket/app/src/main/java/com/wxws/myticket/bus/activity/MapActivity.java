package com.wxws.myticket.bus.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.entity.DirectStopoverStationList;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 地图类
 * Date: 2016/11/5 10:25
 *
 * @auther: lixiangxiang
 */
public class MapActivity extends BaseActivity implements OnGetRoutePlanResultListener ,BaiduMap.OnMapLoadedCallback {

    private MapView mapView;
    private Button btnLocation;
    private BaiduMap mBaiduMap;
    private int zoom = 14;
    private LocationClient mLocationClient;
    private ArrayList<DirectStopoverStationList> stationLists;
    private RoutePlanSearch search;
    private RouteLine routeLine = null;
    private MyDrivingRouteOverlay routeOverlayManager;
    // 浏览路线节点相关
    private int nodeIndex = -1;// 节点索引,供浏览节点时使用

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getPreData();

        initView();

        initData();

        bindClick();
    }

    private void bindClick() {
        // 初始化搜索模块，注册事件监听
        search = RoutePlanSearch.newInstance();
        search.setOnGetRoutePlanResultListener(this);
    }

    private void getPreData() {
        stationLists = (ArrayList<DirectStopoverStationList>) getIntent().getSerializableExtra("stopOverList");

        if (!(stationLists != null && stationLists.size()>0)){
            finish();
        }
    }


    private void initData() {
    }

    private void initMap() {
        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(false);//隐藏缩放按钮
        MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(zoom);
        mBaiduMap.setMapStatus(ms);
        mBaiduMap.setOnMapLoadedCallback(this);
    }

    private void initView() {

        bindTitleViews();

        mapView = (MapView) findViewById(R.id.mapview);
        btnLocation = (Button) findViewById(R.id.btn_my_location);
        barTitle.setText("途径路线");

        initMap();
    }


    private Marker loadMarker(final LatLng latLng, DirectStopoverStationList stationEntity,int position) {
        try {
            Marker marker = null;
            // 定义Maker坐标点
            // 构建MarkerOption，用于在地图上添加Marker
            BitmapDescriptor bitmapDescriptor = null;

          if (position ==  0) {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_start);
            } else if (position == (stationLists.size()-1)) {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_end);
            } else {
                if ("0".equals(stationEntity.getType())) {
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_ups);
                } else if ("1".equals(stationEntity.getType())){
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_downs);
                }
            }
            OverlayOptions overlayoption = new MarkerOptions().position(latLng).icon(bitmapDescriptor).title(stationEntity.getStationName()).zIndex(1000);
            // 在地图上添加Marker，并显示
            if (overlayoption != null && mBaiduMap != null) {
                marker = (Marker) mBaiduMap.addOverlay(overlayoption);
            }
            mBaiduMap.setOnMarkerClickListener(markerClickListener);
            return marker;
        } catch (Exception ex) {
            return null;
        }
    }

    //点击marker事件

    private InfoWindow mInfoWindow;
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(final Marker marker) {
            Button button = new Button(getApplicationContext());
            button.setTextColor(getResources().getColor(R.color.c_333333));
            button.setTextSize(14);
            button.setBackgroundResource(R.drawable.popup);
            InfoWindow.OnInfoWindowClickListener listener = null;
            if (!StringUtils.isNullOrEmpty(marker.getTitle())) {
                button.setText(marker.getTitle());
                listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        mBaiduMap.hideInfoWindow();
                    }
                };
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), marker.getPosition(), -100, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);
            }
            return true;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

  // 定制 走路RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.via_point);
        }

        @Override
        public int getLineColor() {
            return Color.parseColor("#00a1ec");
        }

        @Override
        public boolean onRouteNodeClick(int i) {
            return super.onRouteNodeClick(i);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.via_point);
        }

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        mLoadingPopWindow.dismissLoadingDialog();
        if (drivingRouteResult != null && drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            routeLine = drivingRouteResult.getRouteLines().get(0);
            MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(
                    mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlayManager = overlay;
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        } else {
            ToastManager.getInstance().showToast(MapActivity.this,"test");
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void onMapLoaded() {
        searchButtonProcess();
    }

    public void searchButtonProcess(){
        // 设置起终点信息
        PlanNode stNode = null;
        PlanNode enNode = null;
        List<PlanNode> wayPoints = new ArrayList<PlanNode>();
        if (stationLists != null && stationLists.size() > 0) {
            int i = 0;
            for (DirectStopoverStationList stationEntity : stationLists) {
                Double lat = 0d;
                Double lng = 0d;
                if (!StringUtils.isNullOrEmpty(stationEntity.getLatitude())) {
                    lat = Double.parseDouble(stationEntity.getLatitude());

                }
                if (!StringUtils.isNullOrEmpty(stationEntity.getLongitude())) {
                    lng = Double.parseDouble(stationEntity.getLongitude());
                }
                if (lat != 0 && lng != 0) {
                    if (i == 0) {
                        stNode = PlanNode.withLocation(new LatLng(lat, lng));
                    } else if (i == stationLists.size() - 1) {
                        enNode = PlanNode.withLocation(new LatLng(lat, lng));
                    } else {
                        wayPoints.add(PlanNode.withLocation(new LatLng(lat, lng)));
                    }
                    loadMarker(new LatLng(lat, lng), stationEntity,i);
                }
                ++i;
            }
            if (enNode == null && wayPoints.size() > 0) {
                enNode = wayPoints.get(wayPoints.size() - 1);
            }
            // 重置浏览节点的路线数据
            routeLine = null;
            if (stNode != null && enNode != null) {
                // 实际使用中请对起点终点城市进行正确的设定
                search.drivingSearch((new DrivingRoutePlanOption()).from(stNode).passBy(wayPoints).policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST)
                        .to(enNode));
                mLoadingPopWindow.showLoadingDialog();
            }
        }
    }

}
