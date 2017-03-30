package com.wxws.myticket.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.bus.fragment.DirectFragment;
import com.wxws.myticket.bus.fragment.TicketFragment;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.train.fragment.TrainSearchFragment;
import com.wxws.myticket.train.rxentity.TicketEvent;

import java.util.HashMap;

/**
 * desc: 首页切换
 * Date: 2016-07-26 09:41
 *
 * @auther: lixiangxiang
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout mFlIndex;
    private TextView mTvTrain;
    private TextView mTvTicket;
    private TextView mTvTicketDirect;
    private TextView[] mTextView ;//按钮

    private int currentId = 0;//当前id
    private int shouldShowId = R.id.tv_ticket;//应该展示那个fragment
    private String fragmentId = "fragmentId";//保存id
    private HashMap<Integer,Fragment> fragHashMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            shouldShowId = savedInstanceState.getInt(fragmentId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_index, null);

        bindView();

        bindData();

        return view;
    }

    private void bindData() {
        fragHashMap  = new HashMap<>();
        mTextView = new TextView[3];
        mTextView[0] = mTvTicket;
        mTextView[1] = mTvTicketDirect;
        mTextView[2] = mTvTrain;

        showFragment(shouldShowId);//初始化
        mTvTicket.setOnClickListener(this);
        mTvTicketDirect.setOnClickListener(this);
        mTvTrain.setOnClickListener(this);

    }

    private void bindView() {

        mFlIndex = (FrameLayout) view.findViewById(R.id.fl_index);

        //切换头
        mTvTicket = (TextView) view.findViewById(R.id.tv_ticket);
        mTvTicketDirect = (TextView) view.findViewById(R.id.tv_ticket_direct);
        mTvTrain = (TextView) view.findViewById(R.id.tv_train);

    }


    /**
     * 选中火车票
     *
     * @param id 选中的
     */
    private void getSelectHeader(int id) {
        for (int i =0 ;i<3 ;i++){
            if (id == mTextView[i].getId()){
                mTextView[i] .setSelected(true);
            }else {
                mTextView[i] .setSelected(false);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ticket:
            case R.id.tv_ticket_direct:
            case R.id.tv_train:
                showFragment(v.getId());
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 展示fragment
     * @param id
     */
    private void showFragment(int id) {
        getSelectHeader(id);
        FragmentManager mFragmentManager = getChildFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        hideFragment(mTransaction,currentId);
        switch (id) {
            case R.id.tv_ticket:
            case R.id.tv_ticket_direct:
            case R.id.tv_train:
                switchFragment(mTransaction,id);
                currentId = id;
                break;
        }
        mTransaction.commit();
    }

    /**
     * 隐藏当前fragment
     * @param mTransaction
     * @param id
     */
    private void hideFragment(FragmentTransaction mTransaction,@IdRes int id){
        Fragment fragment  = fragHashMap.get(id);
        if (fragment!=null){
            mTransaction.hide(fragment);
        }
    }

    /**
     * 切换fragment
     * @param id    R.id.tv_ticket:case R.id.tv_ticket_direct: case R.id.tv_train:
     */
    private void switchFragment(FragmentTransaction mTransaction,@IdRes int id){
        Fragment fragment  = fragHashMap.get(id);
        if (fragment == null){
            switch (id){
                case R.id.tv_ticket:
                    fragment = new TicketFragment();
                    break;
                case R.id.tv_ticket_direct:
                    fragment = new DirectFragment();
                    break;
                case R.id.tv_train:
                    fragment = new TrainSearchFragment();
                    break;
            }
            mTransaction.add(R.id.fl_index,fragment);
        }
        fragHashMap.put(id,fragment);
        mTransaction.show(fragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(fragmentId,shouldShowId);
    }

    //切换为RX
    @Subscribe
    public void onChangeFragment(TicketEvent event){
        if (event.getType() == 1) { // 火车票
            showFragment(R.id.tv_train);
        } else if (event.getType() == 2) { // 汽车票
            showFragment(R.id.tv_ticket);
        }
    }
}
