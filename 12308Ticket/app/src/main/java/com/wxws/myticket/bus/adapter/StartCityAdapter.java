package com.wxws.myticket.bus.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.bus.activity.CityActivity;
import com.wxws.myticket.bus.configs.Constants;
import com.wxws.myticket.bus.entity.CityDisplayEntity;
import com.wxws.myticket.bus.rxentity.CitySelectEvent;
import com.wxws.myticket.bus.rxentity.SelectCityEvent;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.entity.BaseEvent;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;
import com.wxws.myticket.common.widgets.view.GridViewForScrollView;

import java.util.List;


/**
 * desc: 火车票出发城市
 * Date:
 *
 * @auther: lixiangxiang
 */
public class StartCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CityDisplayEntity> listHistory;
    private List<CityDisplayEntity> listHot;
    private List<CityDisplayEntity> listStart;

    private Activity context;
    private LayoutInflater mLayoutInflater;
    private HistoryViewHolder historyViewHolder;
    private HotViewHolder hotViewHolder;
    private StartCityHolder startCityHolder;
    private String origin;
    private String clazz;//类名
    private HistoryCityAdapter historyAdapter;
    private HistoryCityAdapter hot;

    public StartCityAdapter(Activity context, List<CityDisplayEntity> listHistory, List<CityDisplayEntity> listHot, List<CityDisplayEntity> listStart,String origin,String clazz) {
        this.context = context;
        this.listHistory = listHistory;
        this.listHot = listHot;
        this.listStart = listStart;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.origin = origin;
        this.clazz = clazz ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = mLayoutInflater.inflate(R.layout.item_recycler_history, parent, false);
                historyViewHolder = new HistoryViewHolder(view);
                return historyViewHolder;
            case 1:
                if (origin.equals(Constants.CITYSTART)) {
                    view = mLayoutInflater.inflate(R.layout.item_recycler_hot, parent, false);
                    hotViewHolder = new HotViewHolder(view);
                    return hotViewHolder;
                }
            default:
                view = mLayoutInflater.inflate(R.layout.item_recycler_city, parent, false);
                startCityHolder = new StartCityHolder(view);
                return startCityHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                historyAdapter = new HistoryCityAdapter(context, listHistory);
                historyViewHolder.rlHistory.setAdapter(historyAdapter);
                historyAdapter.setOrigin(origin);
                break;
            case 1:
                if (origin.equals(Constants.CITYSTART)) {
                    hot = new HistoryCityAdapter(context, listHot);
                    hot.setOrigin(origin);
                    hotViewHolder.rlHot.setAdapter(hot);
                    break;
                }
            default:
                 final   int  newPosition ;
                if (origin.equals(Constants.CITYSTART)){
                    newPosition = position - 2;
                }else {
                    newPosition = position - 1;
                }
                if (listStart.get(newPosition).isGroup()) {
                    startCityHolder.tvShowFirst.setText(listStart.get(newPosition).getCity());
                    startCityHolder.tvShowCity.setVisibility(View.GONE);
                    startCityHolder.tvShowFirst.setVisibility(View.VISIBLE);
                } else {
                    startCityHolder.tvShowCity.setVisibility(View.VISIBLE);
                    startCityHolder.tvShowFirst.setVisibility(View.GONE);
                    startCityHolder.tvShowCity.setText(listStart.get(newPosition).getCity());
                }
                startCityHolder.tvShowCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CityActivity.origin.equals(origin)) {
                            ListPreferencesSave.getInstance().saveSingleCityHistory(context, listStart.get(newPosition), CityActivity.origin);
                            RxBus.getInstance().post(new SelectCityEvent(listStart.get(newPosition).getCity(), CityActivity.origin, listStart.get(newPosition).getCityId()));
                           if (!"CityLineActivity".equals(clazz)){
                               context.finish();
                           }

                        } else {
                            ListPreferencesSave.getInstance().saveSingleCityHistory(context, listStart.get(newPosition), origin);
                            RxBus.getInstance().post(new CitySelectEvent(listStart.get(newPosition).getCity()));
                            if (!"CityLineActivity".equals(clazz)){
                                context.finish();
                            }
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (origin.equals(Constants.CITYSTART)){
            return 2 + listStart.size();
        }else {
            return 1 + listStart.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private static class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHistory;
        public GridViewForScrollView rlHistory;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            tvHistory = (TextView) itemView.findViewById(R.id.tv_history);
            rlHistory = (GridViewForScrollView) itemView.findViewById(R.id.rl_history);
        }
    }

    private static class HotViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvHot;
        private final GridViewForScrollView rlHot;

        public HotViewHolder(View itemView) {
            super(itemView);
            tvHot = (TextView) itemView.findViewById(R.id.tv_hot);
            rlHot = (GridViewForScrollView) itemView.findViewById(R.id.rl_hot);
        }
    }

    private static class StartCityHolder extends RecyclerView.ViewHolder {

        private final TextView tvShowFirst;
        private final TextView tvShowCity;

        public StartCityHolder(View itemView) {
            super(itemView);
            tvShowFirst = (TextView) itemView.findViewById(R.id.tv_show_first);
            tvShowCity = (TextView) itemView.findViewById(R.id.tv_show_city);
        }

    }
}
