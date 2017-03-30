package com.wxws.myticket.train.adapter;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.common.utils.DateTimeUtil;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.train.entity.ChildInfoModel;
import com.wxws.myticket.train.entity.TrainInsuranceModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * desc:
 * Date: 2016-07-26 16:54
 *
 * @auther: lixiangxiang
 */

public class TrainInsuranceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private int mType;
    private int mNum;
    private int mCount;
    private int mChildInsuranceSize; // 有保险信息的儿童数量
    private List<TrainInsuranceModel> mInsuranceList = new ArrayList<>();
    private List<ChildInfoModel> mChildInfoList = new ArrayList<>();
    private OnCheckboxChoosedListener mChoosedListener;
    private String mSingleChoice = "男孩";

    private int cursorPos;
    private String inputAfterText;
    private boolean resetText;

    public TrainInsuranceAdapter(Context context, int type, int num, int insuranceSize) {

        this.mContext = context;
        this.mType = type; // 0 为儿童，1为成人。
        this.mNum = num;// 保险的数目
        this.mCount = 0;
        this.mChildInsuranceSize = insuranceSize;
    }

    public void refreshData(List<TrainInsuranceModel> insuranceList, List<ChildInfoModel> childinfoList) {
        mInsuranceList.clear();
        mInsuranceList.addAll(insuranceList);

        boolean should_addchild = false;
        for (int i = 0; i < mInsuranceList.size(); i++) {
            if (mInsuranceList.get(i).isFlag_checkbox()) {
                should_addchild = true;
                break;
            }
        }

        if (mType == 0 && should_addchild) {

            this.mCount = mInsuranceList.size() + mNum;
            if (childinfoList.size() == 0) {
                for (int i = 0; i < mNum; i++) {
                    ChildInfoModel model = new ChildInfoModel();
                    model.setFlag_editbirth(false);
                    model.setName("");
                    model.setBirthday("");
                    model.setGender(1);
                    mChildInfoList.add(model);
                }

            } else {
                mChildInfoList.clear();

                if (mChildInfoList.size() < mNum) {

                    for (int i = 0; i < mNum; i++) {

                        if (i < childinfoList.size()) {
                            mChildInfoList.add(childinfoList.get(i));
                        } else {
                            ChildInfoModel model = new ChildInfoModel();
                            model.setFlag_editbirth(false);
                            model.setName("");
                            model.setBirthday("");
                            model.setGender(1);
                            mChildInfoList.add(model);
                        }

                    }

                }
            }

        } else {
            this.mCount = mInsuranceList.size();
        }

        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                ChildInfoeHolder childInfoeHolder = new ChildInfoeHolder(LayoutInflater.from(mContext).inflate(R.layout.item_child_insurance, parent, false));
                return childInfoeHolder;
            case 1:
                InsuranceHolder insuranceHolder = new InsuranceHolder(LayoutInflater.from(mContext).inflate(R.layout.item_traininsurance, parent, false));
                return insuranceHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position < mInsuranceList.size()) {
            ((InsuranceHolder) holder).tv_name.setText(mInsuranceList.get(position).getInsuranceName());
            ((InsuranceHolder) holder).tv_desc.setText(mInsuranceList.get(position).getInsuranceDesc());
            ((InsuranceHolder) holder).tv_price.setText("¥" + (mInsuranceList.get(position).getMoney() * 0.01) + "x" + mNum);

            if (mInsuranceList.get(position).isFlag_checkbox()) {
                ((InsuranceHolder) holder).cb_ischoosed.setChecked(true);
            } else {
                ((InsuranceHolder) holder).cb_ischoosed.setChecked(false);
            }

            ((InsuranceHolder) holder).cb_ischoosed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        mChoosedListener.choosed(position, mType);
                        if(mType == 0){
                            for(TrainInsuranceModel model : mInsuranceList){
                                model.setFlag_checkbox(false);
                            }
                        }
                        mInsuranceList.get(position).setFlag_checkbox(true);

                        notifyDataSetChanged();

                    } else {
                        ((CheckBox) v).setChecked(true);
                        if (mType == 1) {
                            mInsuranceList.get(position).setFlag_checkbox(true);
                            mChoosedListener.choosed(position, mType);

                        } else {
                            mInsuranceList.get(position).setFlag_checkbox(true); // 只能单选，如果想不购买，可以直接点击不购买保险信息，而不用再这里处理
                        }

                    }
                }
            });
        } else {

            ((ChildInfoeHolder) holder).tv_num.setText("第" + (position - mInsuranceList.size() + 1) + "位儿童");

            if (mChildInfoList.size() != 0) {

                if (mChildInsuranceSize > (position - mInsuranceList.size())) {

                    ((ChildInfoeHolder) holder).tv_gender.setText(mChildInfoList.get(position - mInsuranceList.size()).getGender() == 1 ? "男孩" : "女孩");
                    ((ChildInfoeHolder) holder).et_name.setText(mChildInfoList.get(position - mInsuranceList.size()).getName());
                    ((ChildInfoeHolder) holder).tv_date.setText(mChildInfoList.get(position - mInsuranceList.size()).getBirthday());

                }

            }

            ((ChildInfoeHolder) holder).tv_gender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
                    final String[] singleItems = new String[]{"男孩", "女孩"};

                    mDialogBuilder.setSingleChoiceItems(singleItems, ((ChildInfoeHolder) holder).tv_gender.getText().toString().equals("男孩") ? 0 : 1,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSingleChoice = singleItems[which];
                                    return;
                                }
                            });
                    mSingleChoice = singleItems[0];
                    mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ChildInfoeHolder) holder).tv_gender.setText(mSingleChoice);

                            mChildInfoList.get(position - mInsuranceList.size()).setGender(mSingleChoice.equals("男孩") ? 1 : 2);
                        }
                    });
                    mDialogBuilder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });

                    mDialogBuilder.create().show();

                }
            });

            ((ChildInfoeHolder) holder).tv_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mYear, mMonth, mDay;
                    Calendar c = Calendar.getInstance();

                    if (mChildInfoList.get(position - mInsuranceList.size()).isFlag_editbirth()) {

                        String dateStr = mChildInfoList.get(position - mInsuranceList.size()).getBirthday();
                        Date date = DateTimeUtil.StringToDate(dateStr, "yyyy-MM-dd");
                        c.setTime(date);

                    }
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog pickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            String month = new DecimalFormat("00").format(monthOfYear + 1); // 选择器会默认从0开始，不要问为什么，安卓提供就是这样的。
                            String day = new DecimalFormat("00").format(dayOfMonth);
                            String birthday = year + "-" + month + "-" + day;

                            ((ChildInfoeHolder) holder).tv_date.setText(birthday);
                            mChildInfoList.get(position - mInsuranceList.size()).setBirthday(birthday);
                            mChildInfoList.get(position - mInsuranceList.size()).setFlag_editbirth(true);

                        }
                    }, mYear, mMonth, mDay);

                    pickerDialog.getDatePicker().setMaxDate(new Date().getTime());

                    pickerDialog.show();

                }
            });


            ((ChildInfoeHolder) holder).et_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (!resetText) {
                        cursorPos = ((ChildInfoeHolder) holder).et_name.getSelectionEnd();
                        inputAfterText = s.toString();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!resetText) {
                        if (count >= 2) {//表情符号的字符长度最小为2
                            CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                            if (containsEmoji(input.toString())) {
                                resetText = true;
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                ((ChildInfoeHolder) holder).et_name.setText(inputAfterText);
                                CharSequence text = ((ChildInfoeHolder) holder).et_name.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            }
                        }
                    } else {
                        resetText = false;
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!containsEmoji(s.toString())) {
                        mChildInfoList.get(position - mInsuranceList.size()).setName(s.toString());
                    }

                }
            });
        }

    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    @Override
    public int getItemCount() {
        return mCount; // 如果有点击了，则数据为mcount+mnum，即多几条item用于
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mInsuranceList.size()) {
            return 1; // 保险类型视图
        } else {
            return 0; // 儿童信息填写视图
        }
    }

    public List<ChildInfoModel> getChildInfoList() {

        return mChildInfoList;
    }

    public boolean checkDataComplete() {

        boolean flag_birth = true;
        boolean flag_name = true;
        for (int i = 0; i < mChildInfoList.size(); i++) {
            if (0 == mChildInfoList.get(i).getName().length()) {
                ToastManager.getInstance().showToast(mContext, "第" + (i + 1) + "位儿童的姓名尚未填写，请完善");
                flag_name = false;
                break;
            } else if (0 == mChildInfoList.get(i).getBirthday().length()) {
                ToastManager.getInstance().showToast(mContext, "第" + (i + 1) + "位儿童的出生日期尚未填写，请完善");
                flag_birth = false;
                break;
            }
        }
        return flag_birth && flag_name;
    }

    public static class InsuranceHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_desc;
        public TextView tv_price;
        public CheckBox cb_ischoosed;

        public InsuranceHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_iteminsurance_name);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_iteminsurance_desc);
            tv_price = (TextView) itemView.findViewById(R.id.tv_iteminsurance_num);
            cb_ischoosed = (CheckBox) itemView.findViewById(R.id.cb_iteminsurance_ischoosed);
        }
    }

    class ChildInfoeHolder extends RecyclerView.ViewHolder {

        public TextView tv_date;
        public TextView tv_num;
        public EditText et_name;
        public TextView tv_gender;

        public ChildInfoeHolder(View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_childinsurace_date);
            tv_num = (TextView) itemView.findViewById(R.id.tv_itemchildinfo_num);
            et_name = (EditText) itemView.findViewById(R.id.et_childindurance_name);
            tv_gender = (TextView) itemView.findViewById(R.id.tv_childinsurance_gender);
        }
    }

    public interface OnCheckboxChoosedListener {
        void choosed(int position, int type);
    }

    public void setOnCheckboxChoosedListener(OnCheckboxChoosedListener listener) {
        this.mChoosedListener = listener;
    }

}

