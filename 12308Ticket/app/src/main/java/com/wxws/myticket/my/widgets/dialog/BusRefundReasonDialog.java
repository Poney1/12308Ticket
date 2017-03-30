package com.wxws.myticket.my.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * Date: 2016-11-15 17:17
 *
 * @author jiangyan
 */
public class BusRefundReasonDialog extends Dialog {

    private Context mContext;
    private List<String> mListData = new ArrayList<>();
    private onReasonListener mListener;

    public BusRefundReasonDialog(Context context,List<String> reasonList) {
        super(context, R.style.dialog);
        this.mContext = context;
        mListData.clear();
        mListData.addAll(reasonList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_busrefundreason);
        initView();
    }

    private void initView() {
        ListView lvReasonList = (ListView) findViewById(R.id.lv_refundreason_list);

        lvReasonList.setAdapter(new ReasonAdapter(mContext, mListData));

        lvReasonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
                Log.e("mudo", mListData.get(position));
                mListener.choosedReason(position);
                dismiss();
            }
        });
    }

    class ReasonAdapter extends SimpleBaseAdapter {

        public ReasonAdapter(Context context, List data) {
            super(context, data);
        }

        @Override
        public int getItemResource() {
            return R.layout.item_refundreason;
        }

        @Override
        public View getItemView(int position, View convertView, ViewHolder holder) {

            TextView tvReason = (TextView) holder.getView(R.id.tv_refundreason_reason);

            tvReason.setText(data.get(position).toString());
            return convertView;
        }
    }

    public interface onReasonListener {
        void choosedReason(int reason);
    }

    public void setOnReasonListener(onReasonListener listener) {
        this.mListener = listener;
    }


}