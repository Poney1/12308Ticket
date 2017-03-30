package com.wxws.myticket.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.bus.adapter.ActiveAdapter;
import com.wxws.myticket.bus.entity.ActiveEntity;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.my.activity.H5CommonActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 活动展示页
 * Date: 2016/10/9 15:00
 *
 * @auther: lixiangxiang
 */
public class ActiveFragment extends BaseFragment {

    private ListView lsActive;
    private ActiveAdapter aAdapter;
    private List<ActiveEntity>  activeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_active,null);

        initView();

        initData();

        bindClick();
        return view;
    }

    private void bindClick() {
        lsActive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < activeList.size()){
                    Intent  intent  = new Intent(getActivity(), H5CommonActivity.class);
                    intent.putExtra("URL",activeList.get(position).getClickUrl());
                    startActivityWithAnim(intent);
                }
            }
        });
    }

    private void initData() {
        activeList = new ArrayList<>();
        aAdapter = new ActiveAdapter(mActivity,activeList);
        lsActive.setAdapter(aAdapter);

        barTitle.setText("活动");
        clickReload();
    }

    private void initView() {
        bindTitleViews();
        bindLoadingView();

        lsActive = (ListView) view.findViewById(R.id.ls_active);
    }

    protected void clickReload(){
        HttpRequestManager.getInstance().getActiveContent(new ApiSubscriber<JsonResult<List<ActiveEntity>>>(this,ApiSubscriber.NETVIEWLOADINGFRAGMENT){

            @Override
            public void onNext(JsonResult<List<ActiveEntity>> activeEntityJsonResult) {
                if ("0000".equals(activeEntityJsonResult.getResultCode())){
                        if (activeEntityJsonResult.getObject()!=null){
                            activeList = activeEntityJsonResult.getObject();
                            aAdapter.replaceAll(activeList);
                        }
                }
            }
        });
    }
}
