package com.qhw.swishcardapp.presenter.impl;

import android.os.Handler;

import com.qhw.swishcardapp.BasePresenter;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.retrofirapi.ConnectonPospService;
import com.qhw.swishcardapp.view.activitys.CupPaymentConfigurationActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Hello_world on 2017/9/3.
 */

public class CupPayMentConfigurationPresenter extends BasePresenter {
    private CupPaymentConfigurationActivity activity;
    private ConnectonPospService connectonPospService;

    public CupPayMentConfigurationPresenter(CupPaymentConfigurationActivity activity) {
        this.activity = activity;
        connectonPospService = SwishCardApplication.getInstance().retrofit.create(ConnectonPospService.class);
    }

    /**
     * 下载工作密钥
     *
     * @param tenant
     * @param termianl
     */
    public void downLoadDecryptionKeys(final String tenant, final String termianl) {
        initLoadingDialog(activity, "获取工作密钥中");
        showLoadingDialog();
        connectonPospService.getDecryptionKey(tenant, termianl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s != null) {
                        dismissLoadingDialog();
                        int errCode = s.getErrCode();
                        String errMsg = s.getErrMsg();
                        if (errCode == 0) {
                            initLoadingDialog(activity, "配置参数中");
                            showLoadingDialog();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SwishCardApplication.getInstance().preferenceUtils.setTermianl(termianl);
                                    SwishCardApplication.getInstance().preferenceUtils.setTenant(tenant);
                                    SwishCardApplication.getInstance().preferenceUtils.setKey(s.getKey());
                                    SwishCardApplication.getInstance().preferenceUtils.setSignDate("");
                                    dismissLoadingDialog();
                                    toast(activity, "设置成功");
                                    activity.finish();
                                }
                            }, 2000);
                        } else if (errCode == 100) {
                            toast(activity, errMsg);
                        } else {
                            toast(activity, errMsg);
                        }
                    }
                });
    }


}
