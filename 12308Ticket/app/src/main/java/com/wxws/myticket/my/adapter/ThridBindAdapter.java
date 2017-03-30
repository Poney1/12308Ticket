package com.wxws.myticket.my.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.adapter.SimpleBaseAdapter;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.common.utils.UrlEncodeUtils;
import com.wxws.myticket.my.Entity.BindListResponsePara;
import com.wxws.myticket.my.Entity.CommonEvent;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * desc:
 * Date: 2016-11-07 17:48
 *
 * @author jiangyan
 */
public class ThridBindAdapter extends SimpleBaseAdapter {

    public Context mContext;
    private int mThridType;

    public ThridBindAdapter(Context context) {
        super(context, null);
        this.mContext = context;
    }

    public void refreshData(List<BindListResponsePara> data) {
        replaceAll(data);
        notifyDataChanged();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_thridbind;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        ImageView ivIcon = (ImageView) holder.getView(R.id.iv_thridbind_icon);
        TextView tvName = (TextView) holder.getView(R.id.tv_thridbind_name);
        Button btIsBind = (Button) holder.getView(R.id.bt_thridbind_isbind);

        final BindListResponsePara para = (BindListResponsePara) data.get(position);
        switch (para.getType()) {
            case 1:
                ivIcon.setBackgroundResource(R.mipmap.weixin_login_normal);
                break;
            case 2:
                ivIcon.setBackgroundResource(R.mipmap.qq_login_normal);
                break;
            case 3:
                ivIcon.setBackgroundResource(R.mipmap.weibo_login_normal);
                break;
        }

        tvName.setText(para.getName());
        if (para.getBind() == 1) {
            btIsBind.setText("解绑");
            btIsBind.setSelected(false);
        } else {
            btIsBind.setText("绑定");
            btIsBind.setSelected(true);
        }

        btIsBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("mudo","点击开始绑定或者解绑");
                switch (((Button) v).getText().toString()) {
                    case "解绑":
                        unbind(para);
                        break;
                    case "绑定":
                        bind(para);
                        break;
                }

            }
        });

        return convertView;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String openId = bundle.getString("userId");
            String userName = bundle.getString("userName");
            String userIcon = bundle.getString("userIcon");

            bindToServer(mThridType+"",openId, userName, userIcon);
        }
    };

    private void unbind(BindListResponsePara para) {

        HttpRequestManager.getInstance().unbindThrid(para.getType() + "", para.getOpenId() + "", new ApiSubscriber<JsonResult<String>>(mContext) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    RxBus.getInstance().post(new CommonEvent(CommonEvent.THRIDBIND_REFRESH));
                }
                ToastManager.getInstance().showToast(mContext, result.getResultMsg());
            }
        });
    }

    private void bind(BindListResponsePara para) {
        //1微信；2 QQ；3微博

        switch (para.getType()) {
            case 1:
                if (ShareSDK.getPlatform(mContext, Wechat.NAME).isClientValid()) {
                    mThridType = 1;
                    doOauthVerify(Wechat.NAME);
                } else {
                    ToastManager.getInstance().showToast(mContext, R.string.not_install_qq_login);
                }
                break;
            case 2:
                if (ShareSDK.getPlatform(mContext, QZone.NAME).isClientValid()) {
                    mThridType = 2;
                    doOauthVerify(QZone.NAME);
                } else {
                    ToastManager.getInstance().showToast(mContext, R.string.not_install_qq_login);
                }
                break;
            case 3:
                mThridType = 3;
                doOauthVerify(QZone.NAME);
                break;
        }

    }

    /**
     * 第三方登录授权
     */
    private void doOauthVerify(final String plat) {


        Platform platform = ShareSDK.getPlatform(plat);

        // 每次点击都去请求授权，每次点击可以切换账号信息
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }

        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {


                String userId, userIcon, userName;
                userId = platform.getDb().getUserId();
                userIcon = platform.getDb().getUserIcon();
                userName = platform.getDb().getUserName();

                if (!StringUtils.isHaveSpecial(userName)) {
                    userName = "ticket12308";
                }

                if (StringUtils.isNullOrEmpty(userId)) {

                    ToastManager.getInstance().showToast(mContext, "获取用户信息失败,请重试");
                } else {
                    Message msg = mHandler.obtainMessage();

                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    bundle.putString("userName", userName);
                    bundle.putString("userIcon", userIcon);
                    msg.setData(bundle);
                    mHandler.sendMessageDelayed(msg, 200);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastManager.getInstance().showToast(mContext, "授权错误:" + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastManager.getInstance().showToast(mContext, "授权取消");
            }
        });
        //获取用户资料
        platform.showUser(null);
    }

    private void bindToServer(String openType, String openId, String nickName, String photo) {
        String codeName = UrlEncodeUtils.doubleUrlEncode(nickName);
        String codePhoto = UrlEncodeUtils.doubleUrlEncode(photo);

        HttpRequestManager.getInstance().bindThrid(openType, openId, codeName, codePhoto, new ApiSubscriber<JsonResult<String>>(mContext) {
            @Override
            public void onNext(JsonResult<String> result) {
                if ("0000".equals(result.getResultCode())) {
                    RxBus.getInstance().post(new CommonEvent(CommonEvent.THRIDBIND_REFRESH));
                }
                ToastManager.getInstance().showToast(mContext, result.getResultMsg());
            }
        });

    }
}