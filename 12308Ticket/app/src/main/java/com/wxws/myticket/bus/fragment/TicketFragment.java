package com.wxws.myticket.bus.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.AppApplication;
import com.wxws.myticket.app.StackManager;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.bus.activity.BusTicketActivity;
import com.wxws.myticket.bus.activity.CityActivity;
import com.wxws.myticket.bus.activity.SelectDateActivity;
import com.wxws.myticket.bus.adapter.BusHistoryAdapter;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.dao.BusStartCityHelper;
import com.wxws.myticket.bus.dialog.RetPacketDialog;
import com.wxws.myticket.bus.entity.ActiveEntity;
import com.wxws.myticket.bus.entity.BusHistory;
import com.wxws.myticket.bus.rxentity.SelectCityEvent;
import com.wxws.myticket.bus.rxentity.SelectDayEvent;
import com.wxws.myticket.common.constants.ModuleConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.function.ManifestMetaDataUtil;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;
import com.wxws.myticket.bus.dialog.UpdateVersionDialog;
import com.wxws.myticket.common.widgets.view.ViewQueryLine;
import com.wxws.myticket.common.rx.bus.EventThread;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.common.widgets.view.GridViewForScrollView;
import com.wxws.myticket.common.widgets.view.ListViewForScrollView;

import java.util.List;

/**
 * desc: 汽车票首页
 * Date: 2016/10/9 15:29
 *
 * @auther: lixiangxiang
 */
public  class TicketFragment extends BaseFragment {

    private ViewQueryLine viewQueryLine;
    private ListViewForScrollView lsBusHistory;
    private GridViewForScrollView gvIntroduce;

    //历史城市
    private List<BusHistory> mListHistory;
    private BusHistoryAdapter busHistoryAdapter;

    //出发到达城市id
    private String startId;
    private String endId;
    private String fromCityName;
    private RetPacketDialog mRetPacketDialog;
    private UpdateVersionDialog mUpdateVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(TicketFragment.this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ticket, null);
        initView();

        initData();

        loadData();

        bindClick();

        checkNewVersion();

        alertAD();

        return view;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch ( msg.what){
                case 0:
                    mRetPacketDialog.dismiss();
                    break;
            }
            return false;
        }
    }) ;

    /**
     * 检测新版本
     */
    private void checkNewVersion() {
        mUpdateVersion = new UpdateVersionDialog(mActivity,R.style.dialog);
        getVersionInfo();
        if (mVersionInfo != null) {
            // 线上版本大于本地版本
            if (mVersionInfo.getVersionCode() > ManifestMetaDataUtil.getVersionCode(AppApplication.getInstance().getApplicationContext())) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format(getString(R.string.new_version_info), mVersionInfo.getVersionName()));
                sb.append("\n\n");
                sb.append(mVersionInfo.getAlertMsg());
                if (AppApplication.getInstance().isShowUpdate) {
                    mUpdateVersion.setCanceledOnTouchOutside(false);
                    mUpdateVersion.show();
                    // 强制更新
                    if ("1".equals(mVersionInfo.getIsForce())) {
                        mUpdateVersion.setData(mVersionInfo.getAlertTitle(), sb.toString(), true, mVersionInfo.getDownLoadUrl());
                        mUpdateVersion.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (!mUpdateVersion.isCancel){
                                    StackManager.exit();
                                }
                            }
                        });
                    } else {
                        mUpdateVersion.setData(mVersionInfo.getAlertTitle(), sb.toString(), false, mVersionInfo.getDownLoadUrl());
                    }
                }
            }
        }
    }
    //弹出广告
    private void alertAD(){
        if (AppApplication.getInstance().isAlertAD){
            HttpRequestManager.getInstance().getAlertAd(new ApiSubscriber<JsonResult<ActiveEntity>>(this,ApiSubscriber.NETNULL) {
                @Override
                public void onNext(JsonResult<ActiveEntity> activeEntityJsonResult) {
                    if ("0000".equals(activeEntityJsonResult.getResultCode())){
                        if (activeEntityJsonResult.getObject()!=null && !StringUtils.isNullOrEmpty(activeEntityJsonResult.getObject().getImageUrl())){
                           boolean alert =  PreferencesUtils.getDataBoolean(getActivity(),activeEntityJsonResult.getObject().getImageUrl(),true);
                            mRetPacketDialog = new RetPacketDialog(getActivity(), R.style.dialog,activeEntityJsonResult.getObject().getImageUrl(),
                                    activeEntityJsonResult.getObject().getClickUrl());
                            if (alert){
                                mRetPacketDialog.setCanceledOnTouchOutside(false);
                                Window win = mRetPacketDialog.getWindow();
                                win.getDecorView().setPadding(0, 0, 0, 0);
                                WindowManager.LayoutParams lp = win.getAttributes();
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                win.setAttributes(lp);
                                mRetPacketDialog.show();
                                handler.sendEmptyMessageDelayed(0,activeEntityJsonResult.getObject().getAutoClose()*1000);
                            }
                        }
                    }
                }
            });
        }
    }

    private void bindClick() {
        lsBusHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListHistory!=null && mListHistory.size()>0){

                    endId = mListHistory.get(position).getEndCityId();
                    startId = mListHistory.get(position).getStartCityId();
                    Intent intent = new Intent(getActivity(), BusTicketActivity.class);
                    intent.putExtra("startCity", mListHistory.get(position).getStartCity());
                    intent.putExtra("startId",startId);
                    intent.putExtra("endCity", mListHistory.get(position).getEndCity());
                    intent.putExtra("endId", endId);
                    intent.putExtra("departDate", viewQueryLine.getStartDate());
                    intent.putExtra("cityTag", BusTicketActivity.CITYTRADITIONALTAG);
                    intent.putExtra("bizType", ModuleConstants.bizTypeBus);
                    getActivity().startActivity(intent);

                    mListHistory = ListPreferencesSave.getInstance().saveTicketFragHistory(getActivity(),mListHistory.get(position));
                    busHistoryAdapter.replaceAll(mListHistory);
                }
            }
        });
    }

    private void loadData() {
        //获取搜索历史城市
        mListHistory = (List<BusHistory>) PreferencesUtils.readDataObject(getActivity(), Constants.HISTORYCITY);
        busHistoryAdapter = new BusHistoryAdapter(getActivity(), mListHistory);
        lsBusHistory.setAdapter(busHistoryAdapter);
        if (mListHistory!=null && mListHistory.size()>0){
            BusHistory busHistory = mListHistory.get(0);
            viewQueryLine.setStartCity(busHistory.getStartCity());
            viewQueryLine.setEndCity(busHistory.getEndCity());
            startId = busHistory.getStartCityId();
            fromCityName =  busHistory.getStartCity();
            endId = busHistory.getEndCityId();
        }
    }

    private void initData() {
        mUpdateVersion = new UpdateVersionDialog(mActivity,R.style.dialog);
        viewQueryLine.setOnCityTimeSelectListener(new ViewQueryLine.OnCityTimeSelectListener() {
            @Override
            public void startCitySelect() {
                Intent intent = new Intent(getActivity(), CityActivity.class);
                intent.putExtra("cityId", Constants.BUSSTARTCITY);
                intent.putExtra("inputName",getActivity().getString(R.string.bus_start_city));
                getActivity().startActivity(intent);
            }

            @Override
            public void endCitySelect() {
                if (!BusStartCityHelper.getInstance().isHaveCity(viewQueryLine.getStartCity())){
                    viewQueryLine.setStartCity("");
                    ToastManager.getInstance().showToast(getActivity(), String.format(getActivity().getString(R.string.ll_view_not_dredge),viewQueryLine.getStartCity()));
                }else {
                    Intent intent = new Intent(getActivity(), CityActivity.class);
                    intent.putExtra("cityId", Constants.BUSENDCITY);
                    intent.putExtra("inputName",getActivity().getString(R.string.bus_arrive_city));
                    intent.putExtra("fromCityId",startId);
                    intent.putExtra("fromCityName", fromCityName);
                    startActivityWithAnim(intent);
                }
            }

            @Override
            public void changeData() {

            }

            @Override
            public void timeSelect(String year, String month, String day) {
                Intent intent = new Intent(getActivity(), SelectDateActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("tag", SelectDateActivity.BUSTRADITIONALTAG);
                getActivity().startActivity(intent);
            }

            @Override
            public void queryLine(String startCity, String endCity, String date) {

                BusHistory busHistory =  new BusHistory();
                busHistory.setEndCity(endCity);
                busHistory.setEndCityId(endId);
                busHistory.setStartCity(startCity);
                busHistory.setStartCityId(startId);
                mListHistory = ListPreferencesSave.getInstance().saveTicketFragHistory(getActivity(),busHistory);
                busHistoryAdapter.replaceAll(mListHistory);

                Intent intent = new Intent(getActivity(), BusTicketActivity.class);
                intent.putExtra("startCity", startCity);
                intent.putExtra("startId",startId);
                intent.putExtra("endCity", endCity);
                intent.putExtra("endId", endId);
                intent.putExtra("departDate", date);
                intent.putExtra("cityTag", BusTicketActivity.CITYTRADITIONALTAG);
                intent.putExtra("bizType", ModuleConstants.bizTypeBus);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initView() {
        viewQueryLine = (ViewQueryLine) view.findViewById(R.id.vq_bus_city);
        gvIntroduce = (GridViewForScrollView) view.findViewById(R.id.gv_introduce);
        lsBusHistory = (ListViewForScrollView) view.findViewById(R.id.ls_bus_history);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onRecievedDate(SelectDayEvent event) {
        if (SelectDateActivity.BUSTRADITIONALTAG.equals(event.getTag())) {
            StringBuilder startDate = new StringBuilder();
            startDate.append(event.getYear()).append("-").append(event.getMonth()).append("-").append(event.getDay());
            viewQueryLine.setDate(startDate.toString());
        }
    }

    @Subscribe (thread = EventThread.MAIN_THREAD)
    public void onCitySelectEvent(SelectCityEvent cityEvent){
        if (cityEvent.getOrigin().equals(Constants.CITYSTART)){
            viewQueryLine.setStartCity(cityEvent.getCityName());
            startId = cityEvent.getCityId();
            fromCityName =  cityEvent.getCityName();
        }else if (cityEvent.getOrigin().equals(Constants.CITYEND)){
            viewQueryLine.setEndCity(cityEvent.getCityName());
            endId = cityEvent.getCityId();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(TicketFragment.this);
    }
}
