package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapViewLayoutParams;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.Entity.PassengerInfoEntity;
import com.wxws.myticket.my.activity.EditPassengersActivity;
import com.wxws.myticket.my.activity.PassengersActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * desc: 乘车人列表
 * Date: 2016/11/7 16:06
 *
 * @auther: lixiangxiang
 */
public class PassengerListAdapter extends SimpleBaseAdapter<PassengerInfoEntity> {

    private Context context;
    private int flag;//来源
    private List<PassengerInfoEntity> select;
    private SwipeListView mSwipeListView;
    private int  selectFlag = 0 ;//单选还是多选
    private List<PassengerInfoEntity> data;
    private boolean isTrain = false ;//是否是火车票选择乘客

    public PassengerListAdapter(Context context,int flag,List<PassengerInfoEntity> select,SwipeListView mSwipeListView, List<PassengerInfoEntity> data) {
        super(context, data);
        this.context = context ;
        this.flag = flag;
        this.select = select ;
        this.mSwipeListView = mSwipeListView;
        this.data = data;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_passenger_list;
    }

    @Override
    public View getItemView(final int position, final View convertView, ViewHolder holder) {

        final PassengerInfoEntity passengerInfoEntity = (PassengerInfoEntity) getItem(position);

        LinearLayout llContain = holder.getView(R.id.layout_contain);
        FrameLayout flCheck = holder.getView(R.id.fl_check);
        TextView tvAdd = holder.getView(R.id.tv_add);
        LinearLayout llContent = holder.getView(R.id.layout_content);
        final TextView tvCheckBox = holder.getView(R.id.checkbox);
        TextView tvFullName = holder.getView(R.id.tv_full_name);//姓名
        tvFullName.setText(passengerInfoEntity.getPassengerName());
        TextView tvPassengerCertifyType = holder.getView(R.id.tv_passenger_certifytype);//证件类型
        TextView tvIdentityCard = holder.getView(R.id.tv_identity_card);//证件证件号码
        tvIdentityCard.setText(passengerInfoEntity.getCertificateNo());
        ImageView tvEdit = holder.getView(R.id.tv_edit);//编辑联系人
        Button btnDel = holder.getView(R.id.btn_del);//删除联系人

        if (StringUtils.isNullOrEmpty(passengerInfoEntity.getCertificateNo())){
            tvAdd.setVisibility(View.VISIBLE);
        }else {
            tvAdd.setVisibility(View.GONE);
        }
        switch (passengerInfoEntity.getCertificateType()) { //01:二代身份证 02第一代身份证；03护照；04港澳通行证；05台湾通行证
            case "01":
            case "02":
                tvPassengerCertifyType.setText("身份证： ");
                break;
            case "03":
                tvPassengerCertifyType.setText("护照： ");
                break;
            case "04":
                tvPassengerCertifyType.setText("港澳通行证： ");
                break;
            case "05":
                tvPassengerCertifyType.setText("台湾通行证： ");
                break;
            default:
                break;
        }

        //删除联系人
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequestManager.getInstance().delContact(passengerInfoEntity.getPassengerId(), new ApiSubscriber<JsonResult<Void>>(context,ApiSubscriber.NETPROGRESSLOADING) {
                    @Override
                    public void onNext(JsonResult<Void> voidJsonResult) {
                        if ("0000".equals(voidJsonResult.getResultCode())){
                            for (int i =0 ; i <data.size() ; i++){
                                if (data.get(i).getPassengerId().equals(passengerInfoEntity.getPassengerId())){
                                    PassengerListAdapter.this.remove(i);
                                }
                                break;
                            }
                        }else {
                            ToastManager.getInstance().showToast(context,voidJsonResult.getResultMsg());
                        }
                    }
                });
            }
        });

        if (flag == PassengersActivity.TRADITIONALPASSENGER){
            tvCheckBox.setBackgroundResource(R.drawable.slt_passenger_checkbox);
            tvCheckBox.setVisibility(View.VISIBLE);
            boolean isSelect = false;
            if (select!=null){
                Iterator<PassengerInfoEntity> iterator = select.iterator();
                while (iterator.hasNext()){
                    PassengerInfoEntity pe = iterator.next();
                    if (passengerInfoEntity.getPassengerId() .equals(pe.getPassengerId())){
                        isSelect = true;
                        break;
                    }
                }
            }

            isSelectPassenger(tvCheckBox,isSelect);

            passengerInfoEntity.setSelect(isSelect);
        }

      tvEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPassengersActivity.class);
                intent.putExtra("edit", PassengersActivity.TRADITIONALPASSENGER);
                intent.putExtra("passenger", passengerInfoEntity);
                ((BaseActivity)context).startActivityWithAnim(intent);
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPassengersActivity.class);
                intent.putExtra("edit", PassengersActivity.TRADITIONALPASSENGER);
                intent.putExtra("passenger", passengerInfoEntity);
                ((BaseActivity)context).startActivityWithAnim(intent);
            }
        });
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectPassenger(tvCheckBox,position,passengerInfoEntity);
            }
        });
        tvCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectPassenger(tvCheckBox,position,passengerInfoEntity);
            }
        });
        return convertView;
    }


    private void setSelectPassenger(TextView textView,int position,PassengerInfoEntity passengerInfoEntity){

        if (position >=data.size()){
            return;
        }
        if (selectFlag == PassengersActivity.SELECTSINGLE){
            PassengerInfoEntity  passengerInfo = null;
            Iterator<PassengerInfoEntity> iterator = data.iterator();
            while (iterator.hasNext()){
                if (passengerInfoEntity.getPassengerId().equals(data.get(position).getPassengerId())){
                    passengerInfo = data.get(position);
                    passengerInfo.setSelect(true);
                    textView.setBackgroundResource(R.mipmap.bus_ticket_shaixuan_draw);
                    passengerInfoEntity.setSelect(true);
                }else {
                    textView.setBackgroundResource(R.mipmap.bus_ticket_shaixuan_draw_normal);
                    passengerInfoEntity.setSelect(false);
                }
            }
            isSelectPassenger(textView,passengerInfoEntity.isSelect());
            RxBus.getInstance().post(passengerInfo);
            ((BaseActivity)context).finish();
            return;
        }else if (selectFlag == PassengersActivity.SELECTMUL){
            if (select == null){
                select = new ArrayList<>();
            }
            if (!data.get(position).isSelect()){
                if (isTrain){
                    if (select.size()>=5){
                        ToastManager.getInstance().showToast(context,context.getString(R.string.passenger_5));
                        return;
                    }
                }else {
                    if (select.size() >= 3){
                        ToastManager.getInstance().showToast(context,context.getString(R.string.passenger_3));
                        return;
                    }
                }
                select.add(data.get(position));
                data.get(position).setSelect(true);
            }else {
                for (int i = 0;i<select.size(); i++){
                    if (passengerInfoEntity.getPassengerId().equals(select.get(i).getPassengerId())){
                        select.remove(i);
                        break;
                    }
                }
                // 取消选择
                data.get(position).setSelect(false);
            }
            isSelectPassenger(textView,data.get(position).isSelect());
        }
    }

    //多选
    public void postPassenger(){
        if (selectFlag == PassengersActivity.SELECTMUL){
            RxBus.getInstance().post(select);
        }
    }
    /**
     * 按钮控制是否选中
     * @param textView
     * @param isSelect
     */
    private void isSelectPassenger(TextView textView,boolean isSelect){
        if (isSelect){
            textView.setBackgroundResource(R.mipmap.bus_ticket_shaixuan_draw);
        }else {
            textView.setBackgroundResource(R.mipmap.bus_ticket_shaixuan_draw_normal);
        }
    }
    //单选或者多选
    public void setSelectFlag(int selectFlag){
        this.selectFlag = selectFlag ;
    }

    public void  replaceAll(List data){
        this.data = data ;
        super.replaceAll(data);
    }
}
