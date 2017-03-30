package com.wxws.myticket.my.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.wxws.myticket.R;
import com.wxws.myticket.my.Entity.CertificateEntity;
import com.wxws.myticket.my.adapter.EditPassengerTypeAdapter;

import java.util.List;


/**
 * desc:  编辑联系人
 * Date: 2016/8/8 14:48
 *
 * @auther: lixiangxiang
 */
public class EditPassengersDialog extends Dialog {

    private Context mContext;
    private View view;
    private List<CertificateEntity> listType;
    private OnSelectPassengerListener onSelectPassengerListener;

    public EditPassengersDialog(Context context, List<CertificateEntity> listType) {
        super(context, R.style.dialog);
        this.listType = listType;
        this.mContext = context;
    }

    public void setOnPassengerListener(OnSelectPassengerListener onSelectPassengerListener) {
        this.onSelectPassengerListener = onSelectPassengerListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_passengers_type);
        initView();
    }

    private void initView() {
        ListView lsPassenger = (ListView) findViewById(R.id.ls_passenger);
        EditPassengerTypeAdapter eAdapter = new EditPassengerTypeAdapter(mContext, listType);
        lsPassenger.setAdapter(eAdapter);
        lsPassenger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditPassengersDialog.this.dismiss();
                onSelectPassengerListener.setOnSelectPassengerListener(listType.get(position));
            }
        });
    }

    public interface OnSelectPassengerListener {

        void setOnSelectPassengerListener(CertificateEntity passengerType);
    }
}
