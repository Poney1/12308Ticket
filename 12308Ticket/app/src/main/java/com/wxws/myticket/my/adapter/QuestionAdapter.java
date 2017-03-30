package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.my.Entity.QuestionResponsePara;

import java.util.List;

/**
 * desc:
 * Date: 2016-11-07 18:30
 *
 * @author jiangyan
 */
public class QuestionAdapter extends SimpleBaseAdapter {

    public QuestionAdapter(Context context) {
        super(context, null);
    }

    public void refreshData(List<QuestionResponsePara> data) {
        Log.e("mudo", "获取的列表：" + data.size());
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_question;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView tvQuestion = (TextView) holder.getView(R.id.tv_question_question);
        TextView tvAsktime = (TextView) holder.getView(R.id.tv_question_asktime);
        TextView tvIsSolve = (TextView) holder.getView(R.id.tv_question_issolve);
        TextView tvAnswerTime = (TextView) holder.getView(R.id.tv_question_anwsertime);

        QuestionResponsePara para = (QuestionResponsePara) data.get(position);

        tvQuestion.setText(para.getQuestion());
        tvAsktime.setText(para.getCreateTime());
        tvIsSolve.setText(para.getStatus().equals("0") ? "未解答" : "已解答");
        tvAnswerTime.setText(para.getCreateTime());//TODO 缺少回答时间

        return convertView;
    }
}