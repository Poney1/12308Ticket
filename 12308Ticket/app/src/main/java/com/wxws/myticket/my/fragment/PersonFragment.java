package com.wxws.myticket.my.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.common.net.http.ApiSubscriber;
import com.wxws.myticket.common.net.http.HttpRequestManager;
import com.wxws.myticket.common.net.http.JsonResult;
import com.wxws.myticket.common.rx.bus.RxBus;
import com.wxws.myticket.common.rx.bus.Subscribe;
import com.wxws.myticket.common.utils.StringUtils;
import com.wxws.myticket.my.Entity.CommonEvent;
import com.wxws.myticket.my.Entity.ShareInfo;
import com.wxws.myticket.my.activity.AccountActivity;
import com.wxws.myticket.my.activity.CouponActivity;
import com.wxws.myticket.my.activity.FeedbackActivity;
import com.wxws.myticket.my.activity.H5HelpCenterActivity;
import com.wxws.myticket.my.activity.LoginActivity;
import com.wxws.myticket.my.activity.MessageActivity;
import com.wxws.myticket.my.activity.PassengersActivity;
import com.wxws.myticket.my.activity.QuestionActivity;
import com.wxws.myticket.my.activity.SettingActivity;
import com.wxws.myticket.my.activity.WalletActivity;
import com.wxws.myticket.my.widgets.view.PersonCommonLayout;
import com.wxws.myticket.order.entity.OrderListEvent;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;

import static com.wxws.myticket.common.constants.ModuleConstants.orderTypeAll;
import static com.wxws.myticket.my.activity.PassengersActivity.SELECT_PASSENGER;

/**
 * desc: 个人中心页面
 * Date: 2016-11-04 15:53
 *
 * @author jiangyan
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener {

    private ShareInfo shares;
    private DisplayImageOptions options;
    private SimpleDraweeView mIvIcon;
    private TextView mTvName;
    private TextView mTvTips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, null);
        RxBus.getInstance().register(this);
        getShareData();

        initView();
        initData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    private void initView() {
        ImageView ivSetting = (ImageView) view.findViewById(R.id.iv_fperson_setting);
        ImageView ivMessage = (ImageView) view.findViewById(R.id.iv_fperson_message);

        mIvIcon = (SimpleDraweeView) view.findViewById(R.id.iv_fperson_icon);
        mTvName = (TextView) view.findViewById(R.id.tv_fperson_name);
        mTvTips = (TextView) view.findViewById(R.id.tv_fperson_tips);

        LinearLayout llLogin = (LinearLayout) view.findViewById(R.id.ll_fperson_login);
        LinearLayout llWallet = (LinearLayout) view.findViewById(R.id.ll_fperson_wallet);
        LinearLayout llCoupon = (LinearLayout) view.findViewById(R.id.ll_fperson_coupon);

        PersonCommonLayout pclOrder = (PersonCommonLayout) view.findViewById(R.id.pcl_fperson_order);
        PersonCommonLayout pclPassenger = (PersonCommonLayout) view.findViewById(R.id.pcl_fperson_passenger);
        PersonCommonLayout pclShare = (PersonCommonLayout) view.findViewById(R.id.pcl_fperson_share);
        PersonCommonLayout pclAsk = (PersonCommonLayout) view.findViewById(R.id.pcl_fperson_ask);
        PersonCommonLayout pclFeedback = (PersonCommonLayout) view.findViewById(R.id.pcl_fperson_feedback);
        PersonCommonLayout pclHelpcenter = (PersonCommonLayout) view.findViewById(R.id.pcl_fperson_helpcenter);

        ivSetting.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        llLogin.setOnClickListener(this);

        llWallet.setOnClickListener(this);
        llCoupon.setOnClickListener(this);

        pclOrder.setOnClickListener(this);
        pclPassenger.setOnClickListener(this);
        pclShare.setOnClickListener(this);
        pclAsk.setOnClickListener(this);
        pclFeedback.setOnClickListener(this);
        pclHelpcenter.setOnClickListener(this);

    }

    private void initData() {
        if (checkLogin()) {
            if (null == userInfo.getPhoto()) { // 防止返回空
                mIvIcon.setImageResource(R.mipmap.my_pic);
            } else {
                mIvIcon.setImageURI(userInfo.getPhoto());
            }

            mTvName.setText(null == userInfo.getNickName() ? userInfo.getMobilePhone() : userInfo.getNickName());
            mTvTips.setText("欢迎来到12308票务平台");
        } else {
            mIvIcon.setImageResource(R.mipmap.my_pic);
            mTvName.setText(getString(R.string.myinfo_title));
            mTvTips.setText(getString(R.string.myinfo_subtitle));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_fperson_setting:// 设置界面
                mActivity.startActivityWithAnim(SettingActivity.class);
                break;
            case R.id.iv_fperson_message:// 消息界面
                mActivity.startActivityWithAnim(MessageActivity.class);
                break;
            case R.id.ll_fperson_login:// 登录或者个人信息界面
                if (checkLogin()) {
                    mActivity.startActivityWithAnim(AccountActivity.class);
                } else {
                    mActivity.startActivityWithAnim(LoginActivity.class);
                }
                break;
            case R.id.ll_fperson_wallet:// TODO 我的钱包
                mActivity.startActivityWithAnim(WalletActivity.class);
                break;
            case R.id.ll_fperson_coupon:// 优惠券
                if (checkLogin()) {
                    mActivity.startActivityWithAnim(CouponActivity.class);
                } else {
                    mActivity.startActivityWithAnim(LoginActivity.class);
                }
                break;
            case R.id.pcl_fperson_order:
                RxBus.getInstance().post(new OrderListEvent(orderTypeAll, true));
                break;
            case R.id.pcl_fperson_passenger:// todo 常用联系人
                if (checkLogin()) {

                    Intent pasIntent = new Intent(getActivity(), PassengersActivity.class);
                    pasIntent.putExtra("from", SELECT_PASSENGER);
                    mActivity.startActivityWithAnim(pasIntent);

                } else {
                    mActivity.startActivityWithAnim(LoginActivity.class);
                }
                break;
            case R.id.pcl_fperson_share:

                if (shares != null) {
                    showShare(shares);
                } else {
                    getShareData();
                }
                break;
            case R.id.pcl_fperson_ask://我的提问

                if (checkLogin()) {
                    mActivity.startActivityWithAnim(QuestionActivity.class);
                } else {
                    mActivity.startActivityWithAnim(LoginActivity.class);
                }
                break;
            case R.id.pcl_fperson_feedback:// 意见反馈

                if (checkLogin()) {
                    mActivity.startActivityWithAnim(FeedbackActivity.class);
                } else {
                    mActivity.startActivityWithAnim(LoginActivity.class);
                }
                break;
            case R.id.pcl_fperson_helpcenter:// TODO 帮助中心,直接跳转h5，需要写交互方法
                mActivity.startActivityWithAnim(H5HelpCenterActivity.class);
                break;
            default:
                break;
        }
    }


    private void getShareData() {
        HttpRequestManager.getInstance().getShare("0", new ApiSubscriber<JsonResult<ShareInfo>>(getActivity(), ApiSubscriber.NETNULL) {
            @Override
            public void onNext(JsonResult<ShareInfo> result) {
                if ("0000".equals(result.getResultCode())) {
                    shares = result.getObject();
                }
            }
        });
    }

    //执行分享
    private void showShare(final ShareInfo shares) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setSilent(true);//隐藏图文编辑界面
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(shares.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(shares.getUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shares.getContent());
        /** imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段 */
        if (!StringUtils.isNullOrEmpty(shares.getPhoto())) {
            oks.setImageUrl(shares.getPhoto());
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shares.getUrl());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("12308全国汽车票");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.12308.com");
        //通过OneKeyShareCallback来修改不同平台分享的内容
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (SinaWeibo.NAME.equals(platform.getName())) {
                    paramsToShare.setText(shares.getContent() + " " + shares.getUrl());
                }
            }
        });
        // 启动分享GUI
        oks.show(getActivity());
    }

    @Subscribe
    public void refreshData(CommonEvent event) {

        if (event.getFlag() == CommonEvent.LOGIN_SUCCESS || event.getFlag() == CommonEvent.LOGIN_LOGOUT) {
            initData();
        }
    }
}
