package com.wxws.myticket.my.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.common.constants.ConfigConstants;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.BitmapUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.store.PreferencesUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.LoginResponsePara;
import com.wxws.myticket.my.widgets.popwin.PhotoPopupWindow;
import com.wxws.myticket.my.widgets.view.PersonCommonLayout;

/**
 * desc: 个人资料界面
 * Date: 2016-10-31 14:51
 *
 * @author jiangyan
 */
public class AccountActivity extends BaseActivity {

    private static int RESULT_LOAD_PICTURE = 4;
    private static int RESULT_LOAD_IMAGE = 2;

    private LoginResponsePara mUserInfo;
    private PersonCommonLayout mPclName;
    private PersonCommonLayout mPclCertNo;
    private PersonCommonLayout mPclPhone;
    private SimpleDraweeView mSdvIcon;
    private LinearLayout mLlIcon;
    private PersonCommonLayout mPclBind;
    private PersonCommonLayout mPcl12306;
    private PhotoPopupWindow mPhotoWindow;
    private String mLocalPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        RxBus.getInstance().register(this);

        initCommonView();
        initView();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void initCommonView() {
        bindTitleViews();
        bindLoadingView();
        barTitle.setText("个人资料");
    }

    private void initView() {
        mLlIcon = (LinearLayout) findViewById(R.id.ll_account_icon);
        mSdvIcon = (SimpleDraweeView) findViewById(R.id.sdv_account_icon);
        mPclName = (PersonCommonLayout) findViewById(R.id.pcl_account_name);
        mPclCertNo = (PersonCommonLayout) findViewById(R.id.pcl_account_certno);
        mPclPhone = (PersonCommonLayout) findViewById(R.id.pcl_account_phone);
        mPclBind = (PersonCommonLayout) findViewById(R.id.pcl_account_bind);
        mPcl12306 = (PersonCommonLayout) findViewById(R.id.pcl_account_12306);

        mPhotoWindow = new PhotoPopupWindow(this, mLlIcon);

        mLlIcon.setOnClickListener(this);
        mPclName.setOnClickListener(this);
        mPclCertNo.setOnClickListener(this);
        mPclPhone.setOnClickListener(this);
        mPclBind.setOnClickListener(this);
        mPcl12306.setOnClickListener(this);
    }

    private void getData() {
        showLoading();
        HttpRequestManager.getInstance().getUserInfo(new ApiSubscriber<JsonResult<LoginResponsePara>>(AccountActivity.this, ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<LoginResponsePara> result) {
                if ("0000".equals(result.getResultCode())) {
                    mUserInfo = result.getObject();
                    PreferencesUtils.saveDataObject(AccountActivity.this, ConfigConstants.USERINFO, mUserInfo);
                    initData();
                    dismissLoading();
                } else {
                    showErrorOrNoData(result.getResultMsg(), R.string.net_click_reload, R.mipmap.base_not_network,true);
                }
            }
        });
    }

    private void initData() {
        mSdvIcon.setImageURI(mUserInfo.getPhoto());
        mPclName.setTextInfo(mUserInfo.getNickName());
        mPclCertNo.setTextInfo(mUserInfo.getCertNo());
        mPclPhone.setTextInfo(mUserInfo.getMobilePhone());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_account_icon: // 头像
                if (new BitmapUtils(AccountActivity.this).isHasSdcard()) {
                    mPhotoWindow.show();
                } else {
                    ToastManager.getInstance().showToast(AccountActivity.this, "请插入内存卡");
                }
                break;
            case R.id.pcl_account_name: // 姓名
                Intent nameIntent = new Intent(AccountActivity.this, UpdateNameActivity.class);
                nameIntent.putExtra("type", 0);// 0为姓名，1为身份证号
                startActivityWithAnim(nameIntent);
                break;
            case R.id.pcl_account_certno:// 身份证号
                Intent idNoIntent = new Intent(AccountActivity.this, UpdateNameActivity.class);
                idNoIntent.putExtra("type", 1);// 0为姓名，1为身份证号
                startActivityWithAnim(idNoIntent);
                break;
            case R.id.pcl_account_phone:// 手机号码
                startActivityWithAnim(UpdatePhoneActivity.class);
                break;
            case R.id.pcl_account_bind:// 绑定
                startActivityWithAnim(ThridBindActivity.class);
                break;
            case R.id.pcl_account_12306:// 12306账号管理
                startActivityWithAnim(TrainAccountManageActivity.class);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapUtils bitmapUtil = new BitmapUtils(this);
        // 相册选择
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path;
            if (uri != null && null != data) {

                if (uri.getScheme().equals("file")) {
                    path = uri.getPath();
                } else {
                    Cursor cursor = AccountActivity.this.getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    path = cursor.getString(1);
                }
                if (path == null) {
                    ToastManager.getInstance().showToast(this, "请重新选择图片");
                } else {
                    // mDialog.showProgressDialog(R.string.uploading); TODO
                    savaAndUpload(path);
                }

            }

            // 拍照
        } else if (requestCode == RESULT_LOAD_PICTURE && resultCode == RESULT_OK) {
            String fileName = mPhotoWindow.getFileName();// 拍照时候的名字
            try {
                //TODO
                // mDialog.showProgressDialog(R.string.uploading);
                savaAndUpload(bitmapUtil.getSavePath() + fileName);
            } catch (Exception e) {
            }
        }
    }


    public void savaAndUpload(String fileName) {

        // TODO
        BitmapUtils bitmapUtil = new BitmapUtils(this);
        Bitmap bitmap = bitmapUtil.getBitmap(fileName, 400, 800);
        Bitmap b = ThumbnailUtils.extractThumbnail(bitmap, 160, 160, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        long time = System.currentTimeMillis();
        String imgName = time + String.valueOf((int) ((Math.random() * 9 + 1) * 1000)) + ".png";

        // 本地路径
        mLocalPath = bitmapUtil.getSavePath() + imgName;

        bitmapUtil.creatLocalFile(b, mLocalPath);// 压缩图片放本地

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap = null;
            b = null;
        }

        uploadFile();
    }

    private void uploadFile() {
        // TODO
        HttpRequestManager.getInstance().upLoadIcon(mLocalPath, new ApiSubscriber<JsonResult<String>>(AccountActivity.this, ApiSubscriber.NETPROGRESSLOADING) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    //TODO
                    getData();
                } else {
                    ToastManager.getInstance().showToast(getApplicationContext(),result.getResultMsg());
                }
            }
        });
    }

    @Override
    public void clickReload() {
        super.clickReload();
        getData();
    }

    @Subscribe
    public void refreshData(CommonEvent event){
        if(event.getFlag() == CommonEvent.LOGIN_SUCCESS){
            getData();
        }
    }
}