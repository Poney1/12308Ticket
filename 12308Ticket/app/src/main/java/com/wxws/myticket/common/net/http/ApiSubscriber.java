package com.wxws.myticket.common.net.http;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import com.wxws.myticket.R;
import com.wxws.myticket.app.base.BaseActivity;
import com.wxws.myticket.app.base.BaseFragment;
import com.wxws.myticket.common.utils.LogUtils;
import com.wxws.myticket.common.utils.ToastManager;
import com.wxws.myticket.my.activity.LoginActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * desc: 订阅时自动显示progressbar的订阅者，必须在主线程订阅
 * 对请求进行统一的错误处理
 * 所有的 请求
 * Date: 2016/10/8 15:51
 *
 * @auther: lixiangxiang
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    public static final int NETPROGRESSLOADING = 1;
    public static final int NETVIEWLOADING = 2;
    public static final int NETNULL = 3;
    public static final int NETVIEWLOADINGFRAGMENT = 4;

    private int state = 1;
    private Context context;
    private BaseFragment fragment;

    public ApiSubscriber(@NonNull Context context) {
        this(context, 1);
    }

    public ApiSubscriber(@NonNull Context context, int state) {
        this.state = state;
        this.context = context;
    }

    /**
     * fragment 使用 网络加载 使用这个构造方法
     *
     * @param fragment
     * @param state    NETVIEWLOADINGFRAGMENT = 4
     */
    public ApiSubscriber(BaseFragment fragment, int state) {
        this.state = state;
        this.fragment = fragment;
    }

    @Override
    public void onStart() {
        switch (state) {
            case NETPROGRESSLOADING:
                if (context != null)
                    ((BaseActivity) context).mLoadingPopWindow.showLoadingDialog();
                break;
            case NETVIEWLOADING:
                if (context != null)
                    ((BaseActivity) context).showLoading();
                break;
            case NETVIEWLOADINGFRAGMENT:
                if (fragment != null)
                    fragment.showLoading();
                break;
            default:
                break;
        }

    }

    @Override
    public void onCompleted() {
        switch (state) {
            case NETPROGRESSLOADING:
                if (context != null)
                    ((BaseActivity) context).mLoadingPopWindow.dismissLoadingDialog();
                break;
            case NETVIEWLOADING:
                if (context != null)
                    ((BaseActivity) context).dismissLoading();
                break;
            case NETVIEWLOADINGFRAGMENT:
                if (fragment != null)
                    fragment.dismissLoading();
                break;
            default:
                break;
        }
    }

    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    public void onError(Throwable e) {
        Log.e("mudo", "what error？" + e.toString());

        if (context != null) {
            ((BaseActivity) context).mLoadingPopWindow.dismissLoadingDialog();
        }
        if (isServerProblem(e)) {
            handlerError(R.string.net_abnormal_error,R.string.net_refresh_again,R.mipmap.base_not_network,true);
        }else if (e instanceof ApiException) {
            switch (((ApiException) e).getCode()){
                case 500:
                case 404:
                case 422:
                    handlerError(R.string.net_server_error,R.string.net_refresh_again,R.mipmap.base_not_network,true);
                    break;
                case 401:
                case 302:
                default:
                    handlerError(R.string.net_timeout_error,R.string.net_refresh_again,R.mipmap.base_not_network,true);
                    break;
            }
        }  else if (isNetworkProblem(e)) {
            handlerError(R.string.net_off_error,R.string.net_open_settings,R.mipmap.base_not_network,true);
        } else {
            handlerError(R.string.net_abnormal_error,R.string.net_click_reload,R.mipmap.base_not_network,true);
        }
    }

    //服务端错误
    private   boolean isServerProblem(Throwable error) {
        return (error instanceof ServiceConnection) || (error instanceof ConnectException);
    }

    //处理异常
    private void  handlerError(@StringRes int idString1, @StringRes int idString2, @DrawableRes int idRes , boolean isShow){
        switch (state) {
            case NETPROGRESSLOADING:
                if (context != null)
                    ToastManager.getInstance().showToast(context, context.getString(idString1));
                break;
            case NETVIEWLOADINGFRAGMENT:
                if (fragment != null)
                    fragment.showErrorOrNoData(fragment.getString(idString1),idString2, idRes ,isShow);
                break;
            case NETVIEWLOADING:
                if ( context != null)
                    fragment.showErrorOrNoData(context.getString(idString1), idString2, idRes,isShow);
                break;
            default:
                break;
        }
    }
    //网络错误
    private   boolean isNetworkProblem(Throwable error) {
        return (error instanceof SocketTimeoutException) || (error instanceof HttpException);
    }
}
