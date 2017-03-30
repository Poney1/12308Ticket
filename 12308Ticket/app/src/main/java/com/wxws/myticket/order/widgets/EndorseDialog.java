package com.wxws.myticket.order.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wxws.myticket.R;

/**
 * desc:
 * Date: 2016-11-02 14:00
 *
 * @author jiangyan
 */
public class EndorseDialog extends Dialog {
    private OnChooseEndorseTypeListener mOnChooseEndorseTypeListener;
    private int mPosition;
    public EndorseDialog(Context context, int position) {
        super(context, R.style.dialog);
        this.mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_endorse);

        initView();
    }

    public void initView(){
        Button bt_oneticket = (Button) findViewById(R.id.bt_endorsedialog_oneticket);
        Button bt_allticket = (Button) findViewById(R.id.bt_endorsedialog_allticket);
        Button bt_cancel = (Button) findViewById(R.id.bt_endorsedialog_cancel);

        bt_oneticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChooseEndorseTypeListener.endorseType(1,mPosition);
                dismiss();
            }
        });

        bt_allticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChooseEndorseTypeListener.endorseType(2,mPosition);
                dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnChooseEndorseTypeListener{
        void endorseType(int type,int position);// 1为当前票，2为全部能改签的票
    }

    public void setOnChooseEndorseTypeListener(OnChooseEndorseTypeListener listener){
        this.mOnChooseEndorseTypeListener = listener;
    }
}