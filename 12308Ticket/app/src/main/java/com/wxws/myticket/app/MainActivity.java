package com.wxws.myticket.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.LocationEntity;
import com.wxws.myticket.bus.fragment.ActiveFragment;
import com.wxws.myticket.common.rx.bus.EventThread;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.function.SystemUtils;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.MenuButton;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.activity.LoginActivity;
import com.wxws.myticket.my.fragment.PersonFragment;
import com.wxws.myticket.order.entity.OrderListEvent;
import com.wxws.myticket.order.fragment.OrderFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements BDLocationListener {

    //fragment hide 管理
    private int currentFragment = R.id.main_menu;
    private String fragmentId = "fragment_id";
    private Map<Integer, Fragment> fragmentMap;
    //menu 选中与否
    private MenuButton[] menu;
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(AppApplication.getInstance().getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerLocation();

        RxBus.getInstance().register(MainActivity.this);
        //
        initView(savedInstanceState);

        initData();

    }

    private void initData() {
    }

    private void registerLocation() {
        locationClient = new LocationClient(MainActivity.this);
        locationClient.registerLocationListener(this);
        SystemUtils.getInstance().InitLocation(locationClient);
        locationClient.start();
    }

    private void initView(Bundle savedInstanceState) {
        //初始化数组
        menu = new MenuButton[4];
        fragmentMap = new HashMap<>();

        MenuButton main = getViewById(R.id.main_menu);
        menu[0] = main;
        main.setOnClickListener(this);
        MenuButton active = getViewById(R.id.active_menu);
        menu[1] = active;
        active.setOnClickListener(this);
        MenuButton order = getViewById(R.id.order_menu);
        menu[2] = order;
        order.setOnClickListener(this);
        MenuButton person = getViewById(R.id.person_menu);
        menu[3] = person;
        person.setOnClickListener(this);

        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getInt(fragmentId);
        }
        switchFragment(currentFragment);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.active_menu:
            case R.id.order_menu:
            case R.id.person_menu:
            case R.id.main_menu:
                switchFragment(v.getId());
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(fragmentId, currentFragment);
    }

    /**
     * 选中的按钮
     *
     * @param id R.id.main_menu  R.id.active_menu: R.id.order_menu: R.id.person_menu:
     */
    private void selectMenu(int id) {
        for (int j = 0; j < 4; j++) {
            if (menu[j].getId() == id) {
                menu[j].setSelected(true);
            } else {
                menu[j].setSelected(false);
            }
        }
    }

    /**
     * 展示fragment 均需要 调用此方法
     *
     * @param id R.id.main_menu  R.id.active_menu: R.id.order_menu: R.id.person_menu:
     */
    private void switchFragment(int id) {
        selectMenu(id);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragmentId(transaction, currentFragment);
        switch (id) {
            case R.id.main_menu:
            case R.id.active_menu:
            case R.id.order_menu:
            case R.id.person_menu:
                showFragment(transaction, id);
                currentFragment = id;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏不需要展示的fragment
     */
    private void hideFragmentId(FragmentTransaction transaction, int fragmentId) {
        Fragment fragment = fragmentMap.get(fragmentId);
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    /**
     * 展示 id 对应的 fragment
     *
     * @param transaction
     * @param fragmentId
     */
    private void showFragment(FragmentTransaction transaction, int fragmentId) {
        Fragment fragment = fragmentMap.get(fragmentId);
        if (fragment == null) {
            switch (fragmentId) {
                case R.id.main_menu:
                    fragment = new IndexFragment();
                    transaction.add(R.id.fl_content, fragment, "ticket");
                    break;
                case R.id.active_menu:
                    fragment = new ActiveFragment();
                    transaction.add(R.id.fl_content, fragment, "active");
                    break;
                case R.id.order_menu:
                    fragment = new OrderFragment();
                    transaction.add(R.id.fl_content, fragment, "order");
                    break;
                case R.id.person_menu:
                    fragment = new PersonFragment();
                    transaction.add(R.id.fl_content, fragment, "person");
                    break;
            }
            fragmentMap.put(fragmentId, fragment);
        }
        transaction.show(fragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.unRegisterLocationListener(this);
        locationClient.stop();
        RxBus.getInstance().unRegister(MainActivity.this);
    }

    //是否退出
    private boolean isExit = false;

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true;
            ToastManager.getInstance().showToast(MainActivity.this, R.string.back);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2500);
        } else {
            StackManager.finishAll();
        }
    }

    @Subscribe
    public void updatePswEvent(CommonEvent event) {
        if (event.getFlag() == CommonEvent.UPDATE_PSW) {
            startActivityWithAnim(LoginActivity.class);
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (!StringUtils.isNullOrEmpty(bdLocation.getAddrStr())) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.setCity(bdLocation.getCity());
            locationEntity.setAddr(bdLocation.getAddrStr());
            locationEntity.setLat(bdLocation.getLatitude());
            locationEntity.setLng(bdLocation.getLongitude());
            PreferencesUtils.saveDataObject(MainActivity.this, Constants.LocationInfo, locationEntity);
        } else {
            PreferencesUtils.saveDataObject(MainActivity.this, Constants.LocationInfo, null);
        }
    }

   /* @Subscribe (thread = EventThread.TRAMPOLINE)
    public void jumpToOrder(OrderListEvent event) {
        switchFragment(R.id.order_menu);
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("isPay",false))
            switchFragment(R.id.order_menu);
    }
}
