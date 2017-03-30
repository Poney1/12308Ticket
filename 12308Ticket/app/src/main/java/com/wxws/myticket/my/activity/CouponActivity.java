package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.H5UrlConstants;
import com.wxws.myticket.my.fragment.CouponFragment;
import com.wxws.myticket.my.widgets.dialog.CouponKeyDialog;

/**
 * desc: 优惠券界面
 * Date: 2016-10-31 14:48
 *
 * @author jiangyan
 */
public class CouponActivity extends BaseActivity {

    private RadioButton mRbEff;
    private RadioButton mRbUnEff;
    private int mType = 1;// 1为可用，0为失效
    private Fragment mCurrentFrag;
    private CouponFragment mEffFrag;
    private CouponFragment mUnEffFrag;
    private CouponKeyDialog mCouponKeyDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        getPreData();
        initTitle();
        mCouponKeyDialog = new CouponKeyDialog(CouponActivity.this, R.style.dialog);

        initView();

        initFrag();
    }

    private void getPreData() {
        mType = getIntent().getIntExtra("use",1);
    }

    private void initTitle() {
        bindTitleViews();
        barTitle.setText("我的优惠券");
        tvRight.setText("优惠口令");
        flTitleBarRight.setVisibility(View.VISIBLE);
    }

    private void initView() {

        FrameLayout flContainer = (FrameLayout) findViewById(R.id.fl_coupon_container);
        LinearLayout llRules = (LinearLayout) findViewById(R.id.ll_coupon_rules);

        RadioGroup rgType = (RadioGroup) findViewById(R.id.rg_coupon);
        mRbEff = (RadioButton) findViewById(R.id.rb_coupon_eff);
        mRbUnEff = (RadioButton) findViewById(R.id.rb_coupon_uneff);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_coupon_eff:
                        mType = 1;
                        break;
                    case R.id.rb_coupon_uneff:
                        mType = 0;
                        break;
                }
                initFrag();
            }
        });

        llRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO H5地址,优惠券使用规则
                Intent intent = new Intent(CouponActivity.this, H5CommonActivity.class);
                intent.putExtra("URL", H5UrlConstants.COUPON_RULES);
                startActivityWithAnim(intent);
            }
        });

        flTitleBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCouponKeyDialog == null){
                    mCouponKeyDialog = new CouponKeyDialog(CouponActivity.this, R.style.dialog);
                }
                mCouponKeyDialog.show();

            }
        });



    }

    private void initFrag() {
        switch (mType) {
            case 1:
                mRbEff.setBackgroundResource(R.drawable.bg_orderlist_rorh);
                mRbUnEff.setBackgroundResource(R.color.white);
                mRbEff.setChecked(true);
                mRbUnEff.setChecked(false);

                if (mEffFrag == null) {
                    mEffFrag = CouponFragment.getInstance(1);
                }
                switchFrag(mEffFrag);

                break;
            case 0:
                mRbEff.setBackgroundResource(R.color.white);
                mRbUnEff.setBackgroundResource(R.drawable.bg_orderlist_rorh);
                mRbEff.setChecked(false);
                mRbUnEff.setChecked(true);

                if (mUnEffFrag == null) {
                    mUnEffFrag = CouponFragment.getInstance(0);
                }

                switchFrag(mUnEffFrag);
                break;
        }

    }

    public void switchFrag(Fragment toFragment) {

        if (toFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(mCurrentFrag).show(toFragment).commit();
        } else {
            if (mCurrentFrag == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.fl_coupon_container, toFragment).show(toFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mCurrentFrag).add(R.id.fl_coupon_container, toFragment).show(toFragment).commit();
            }
        }

        mCurrentFrag = toFragment;
    }
}