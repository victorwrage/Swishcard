package com.qhw.swishcardapp.presenter.impl;

import android.os.Bundle;

import com.qhw.swishcardapp.BasePresenter;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.retrofirapi.ConnectonPospService;
import com.qhw.swishcardapp.utils.FormatUrlUtils;
import com.qhw.swishcardapp.utils.MD5Utils;
import com.qhw.swishcardapp.view.activitys.CupCardPaymentSelectionActivity;
import com.qhw.swishcardapp.view.activitys.ICCardSwipeActivity;
import com.qhw.swishcardapp.view.activitys.MagneticStripeCardSwipeActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Hello_world on 2017/9/3.
 */

public class CupCardPaymentSelectionPresenter extends BasePresenter {
    public static final int TYPE_MAGNETIC = 0;//磁卡
    public static final int TYPE_ICC = TYPE_MAGNETIC + 1;//芯片卡

    protected CupCardPaymentSelectionActivity activity;
    private ConnectonPospService connectonPospService;

    public CupCardPaymentSelectionPresenter(CupCardPaymentSelectionActivity activity) {
        this.activity = activity;
        connectonPospService = SwishCardApplication.getInstance().retrofit.create(ConnectonPospService.class);
    }

    /**
     * 获取交易流水号并跳转至刷卡页面
     */
    public void getSerialNumber(final int type) {
        initLoadingDialog(activity, "正在配置刷卡环境...");
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getTenant());
        map.put("device_id", SwishCardApplication.getInstance().preferenceUtils.getTermianl());
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + SwishCardApplication.getInstance().preferenceUtils.getKey();
        String sign = MD5Utils.md5(result);//最终结果
        connectonPospService.searchBn(SwishCardApplication.getInstance().preferenceUtils.getTenant(),
                SwishCardApplication.getInstance().preferenceUtils.getTermianl(), sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    dismissLoadingDialog();
                    if (s != null) {
                        int errCode = s.getErrCode();
                        String errMsg = s.getErrMsg();
                        if (errCode == 0) {
                            String serial = s.getContent().getTrade_num();
                            int bnInt = Integer.valueOf(serial) + 1;
                            String serialResult = String.format("%06d", bnInt);
                            Bundle bundle = new Bundle();
                            bundle.putString("serial", serialResult);
                            switch (type) {
                                case TYPE_MAGNETIC:
                                    openActivity(activity, MagneticStripeCardSwipeActivity.class, bundle, -1);
                                    break;
                                case TYPE_ICC:
                                    openActivity(activity, ICCardSwipeActivity.class, bundle, -1);
                                    break;
                            }

                        } else if (errCode == 100) {
                            Bundle bundle = new Bundle();
                            bundle.putString("serial", "000001");
                            switch (type) {
                                case TYPE_MAGNETIC:
                                    openActivity(activity, MagneticStripeCardSwipeActivity.class, bundle, -1);
                                    break;
                                case TYPE_ICC:
                                    openActivity(activity, ICCardSwipeActivity.class, bundle, -1);
                                    break;
                            }
                        } else {
                            toast(activity, errMsg);
                        }
                    }
                });


    }
}
