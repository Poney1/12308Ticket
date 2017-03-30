package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.TrainAccountInfo;
import com.wxws.myticket.my.Entity.TrainLoginResponseModel;
import com.wxws.myticket.order.widgets.CommonDialog;

import static com.wxws.myticket.my.Entity.CommonEvent.TRAIN_LOGINFAILURE;
import static com.wxws.myticket.my.Entity.CommonEvent.TRAIN_LOGINSUCCESS;

/**
 * desc: 12306 账号管理界面
 * Date: 2016-07-28 15:15
 *
 * @auther: jiangyan
 */
public class TrainAccountManageActivity extends BaseActivity {

    private TrainAccountInfo mTrainAccount;
    private TextView tv_account;
    private TextView tv_logout;
    private LinearLayout ll_change;
    private TextView tv_tips;
    private final static int REQUEST_LOGIN = 100;
    private CommonDialog mDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trainaccountmanage);
        RxBus.getInstance().register(this);
        initCommonView();

        initView();

        if (checkLogin()) {
            getAccount();
        } else {
            //TODO 跳转登录页面
            showErrorOrNoData("您尚未登录，请登录", R.string.go_login, R.mipmap.tip1, true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void initCommonView() {
        bindLoadingView();
        bindTitleViews();
        barTitle.setText(getString(R.string.train_account_title));
    }

    private void initView() {
        tv_account = (TextView) findViewById(R.id.tv_trainaccount_account);
        tv_tips = (TextView) findViewById(R.id.tv_trainaccount_tips);
        tv_logout = (TextView) findViewById(R.id.tv_trainaccount_logout);
        ll_change = (LinearLayout) findViewById(R.id.ll_trainaaccount_change);

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAccount();
            }
        });

        ll_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithAnim(TrainLoginActivity.class);
            }
        });
    }

    private void logoutAccount() {

        if (mDialog == null) {
            mDialog = new CommonDialog(TrainAccountManageActivity.this);
        }

        mDialog.showDialog("解绑提示", "亲，您确定要解绑12306账号吗", new DialogCallBackListener() {
            @Override
            public void doPositiveClick(String string) {
            }

            @Override
            public void doPositiveClick() {
                unBindAccount();
            }

            @Override
            public void doNegativeClick() {
                if (mDialog != null) {
                    mDialog.dissmissDialog();
                }

            }
        });


    }

    private void updateCacheData() {

        mTrainAccount = (TrainAccountInfo) PreferencesUtils.readDataObject(TrainAccountManageActivity.this, ConfigConstants.TRAIN_LOGININFO);
        if (mTrainAccount == null) {
            showLoading();
            showErrorOrNoData(getString(R.string.train_account_nodatatips), R.string.train_account_login, R.mipmap.base_message_order_null, true);
        } else {
            dismissLoading();
            tv_account.setText(mTrainAccount.getAccount());
            tv_tips.setVisibility(View.GONE);
        }

    }

    private void unBindAccount() {

        HttpRequestManager.getInstance().unbindTrain(mTrainAccount.getTrainId(), new ApiSubscriber<JsonResult<String>>(TrainAccountManageActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    PreferencesUtils.saveDataObject(TrainAccountManageActivity.this, ConfigConstants.TRAIN_LOGININFO, null);
                    RxBus.getInstance().post(new CommonEvent(TRAIN_LOGINSUCCESS));
                    getAccount();
                } else {
                    ToastManager.getInstance().showToast(TrainAccountManageActivity.this, "解绑失败，请确认网络后重试");
                }
            }
        });
    }

    private void getAccount() {
        showLoading();
        mTrainAccount = (TrainAccountInfo) PreferencesUtils.readDataObject(TrainAccountManageActivity.this, ConfigConstants.TRAIN_LOGININFO);
        HttpRequestManager.getInstance().trainAccount(new ApiSubscriber<JsonResult<TrainLoginResponseModel>>(TrainAccountManageActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<TrainLoginResponseModel> result) {
                if ("0000".equals(result.getResultCode())) {

                    if (null != result.getObject()) {
                        if (null == mTrainAccount || (!mTrainAccount.getTrainId().equals(result.getObject().getId()))) {

                            mTrainAccount = new TrainAccountInfo();
                            mTrainAccount.setAccount(result.getObject().getTrainAccount());
                            mTrainAccount.setHasbind(true);
                            mTrainAccount.setTrainId(result.getObject().getId());
                            if (result.getObject().getIsPass() == 1) {
                                mTrainAccount.setPass(true);
                            } else {
                                mTrainAccount.setPass(false);
                            }

                            PreferencesUtils.saveDataObject(TrainAccountManageActivity.this, ConfigConstants.TRAIN_LOGININFO, mTrainAccount);
                        }

                        tv_account.setText(result.getObject().getTrainAccount());

                        if (result.getObject().getIsPass() == 1) {
                            tv_tips.setVisibility(View.GONE);
                        } else {
                            RxBus.getInstance().post(new CommonEvent(TRAIN_LOGINFAILURE));
                            tv_tips.setText("(账号或密码错误,点击重新绑定)");
                            tv_tips.setVisibility(View.VISIBLE);
                        }

                        dismissLoading();
                    } else {
                        PreferencesUtils.saveDataObject(TrainAccountManageActivity.this, ConfigConstants.TRAIN_LOGININFO, null);
                        //RxBus.getInstance().post(new CommonEvent(TRAIN_LOGINSUCCESS));
                        showErrorOrNoData(getString(R.string.train_account_nodatatips), R.string.train_account_login, R.mipmap.base_message_order_null, true);
                    }
                }
            }
        });

    }

    @Override
    public void clickReload() {
        super.clickReload();
        getAccount();
    }

    @Subscribe
    public void refresh(CommonEvent event) {
        if (event.getFlag() == TRAIN_LOGINSUCCESS) {
            updateCacheData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN && resultCode == 400) {
            getAccount();
        } else {
            // 没有登录成功则dismiss
            finish();
        }
    }
}