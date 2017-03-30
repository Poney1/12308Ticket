package com.wxws.myticket.train.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.store.ListPreferencesSave;
import com.wxws.myticket.common.widgets.view.GridViewForScrollView;
import com.wxws.myticket.train.activity.TrainCityActivity;
import com.wxws.myticket.train.entity.TrainRealCityModel;
import com.wxws.myticket.train.rxentity.TrainCityEvent;

import java.util.List;


/**
 * desc: 火车票出发城市
 * Date:
 *
 * @auther: lixiangxiang
 */
public class TrainStartCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TrainRealCityModel> listHistory;
    private List<TrainRealCityModel> listHot;
    private List<TrainRealCityModel> listStart;

    private Activity context;
    private LayoutInflater mLayoutInflater;
    private HistoryViewHolder historyViewHolder;
    private HotViewHolder hotViewHolder;
    private StartCityHolder startCityHolder;

    public TrainStartCityAdapter(Activity context, List<TrainRealCityModel> listHistory, List<TrainRealCityModel> listHot, List<TrainRealCityModel> listStart) {
        this.context = context;
        this.listHistory = listHistory;
        this.listHot = listHot;
        this.listStart = listStart;
        this.mLayoutInflater = LayoutInflater.from(context);
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
                view = mLayoutInflater.inflate(R.layout.item_recycler_hot, parent, false);
                hotViewHolder = new HotViewHolder(view);
                return hotViewHolder;
            default:
                view = mLayoutInflater.inflate(R.layout.item_recycler_city, parent, false);
                startCityHolder = new StartCityHolder(view);
                return startCityHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (position) {
            case 0:
                TrainHistoryAdapter train = new TrainHistoryAdapter(context, listHistory);
                historyViewHolder.rlHistory.setAdapter(train);
                break;
            case 1:
                TrainHistoryAdapter trainHot = new TrainHistoryAdapter(context, listHot);
                hotViewHolder.rlHot.setAdapter(trainHot);
                break;
            default:
                if (listStart.get(position - 2).isGroup()) {
                    startCityHolder.tvShowFirst.setText(listStart.get(position - 2).getCity());
                    startCityHolder.tvShowCity.setVisibility(View.GONE);
                    startCityHolder.tvShowFirst.setVisibility(View.VISIBLE);
                } else {
                    startCityHolder.tvShowCity.setVisibility(View.VISIBLE);
                    startCityHolder.tvShowFirst.setVisibility(View.GONE);
                    startCityHolder.tvShowCity.setText(listStart.get(position - 2).getCity());
                }
                startCityHolder.tvShowCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListPreferencesSave.getInstance().saveTrainCityHistory(context, listStart.get(position - 2));
                        RxBus.getInstance().post(new TrainCityEvent(TrainCityActivity.SELECTCITY, listStart.get(position - 2).getCity(), listStart.get(position - 2).getCode()));
                        context.finish();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2 + listStart.size();
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
