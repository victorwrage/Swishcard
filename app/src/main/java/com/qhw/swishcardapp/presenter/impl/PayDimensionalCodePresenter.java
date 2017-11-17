package com.qhw.swishcardapp.presenter.impl;


import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.BasePresenter;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.retrofirapi.QrPayService;
import com.qhw.swishcardapp.utils.FormatUrlUtils;
import com.qhw.swishcardapp.utils.MD5Utils;
import com.qhw.swishcardapp.view.activitys.PayDimensionalCodeActivity;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Hello_world on 2017/9/3.
 */

public class PayDimensionalCodePresenter extends BasePresenter {
    private PayDimensionalCodeActivity activity;
    private QrPayService qrPayService;

    public PayDimensionalCodePresenter(PayDimensionalCodeActivity activity) {
        this.activity = activity;
        qrPayService = SwishCardApplication.getInstance().retrofit.create(QrPayService.class);
    }

    /**
     * 生成支付二维码
     */
    public void generateDimentsionalCode() {
        initLoadingDialog(activity, "生成支付二维码中");
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();
        map.put("aid", AppConstants.AID);
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getMerchant_id());
        map.put("pay_money", SwishCardApplication.getInstance().preferenceUtils.getMoney());
        map.put("pay_type", SwishCardApplication.getInstance().preferenceUtils.getMobileType());
        map.put("trade_type", "1");
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + AppConstants.KEY;
        String sign = MD5Utils.md5(result);//最终结果
        qrPayService.pay(AppConstants.AID, sign,
                SwishCardApplication.getInstance().preferenceUtils.getMerchant_id(),
                SwishCardApplication.getInstance().preferenceUtils.getMoney(),
                SwishCardApplication.getInstance().preferenceUtils.getMobileType(),
                "1")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    dismissLoadingDialog();
                    if (s != null) {
                        int errCode = s.getErrcode();
                        String errMsg = s.getErrmsg();
                        if (errCode == 0) {
                            String order_id = s.getOrder_id();
                            SwishCardApplication.getInstance().preferenceUtils.setOrder_id(order_id);
                            String qrCode = s.getQrcode();
                            activity.responseEventCallBack(qrCode);
                        } else {
                            toast(activity, errMsg);
                        }
                    }
                });
    }

    /**
     * 扫码支付
     *
     * @param payCode
     */
    public void scanPay(String payCode) {
        initLoadingDialog(activity, "支付中");
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();
        map.put("aid", AppConstants.AID);
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getMerchant_id());
        map.put("pay_money", SwishCardApplication.getInstance().preferenceUtils.getMoney());
        map.put("pay_type", SwishCardApplication.getInstance().preferenceUtils.getMobileType());
        map.put("trade_type", "1");
        map.put("auth_code", payCode);
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + AppConstants.KEY;
        String sign = MD5Utils.md5(result);//最终结果
        qrPayService.auPay(AppConstants.AID, sign, SwishCardApplication.getInstance().preferenceUtils.getMerchant_id(),
                SwishCardApplication.getInstance().preferenceUtils.getMoney(), SwishCardApplication.getInstance().preferenceUtils.getMobileType()
                , "1", payCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    dismissLoadingDialog();
                    Logger.e(new Gson().toJson(s));
                    if (s != null) {
                        int errCode = s.getErrcode();
                        if (errCode == 0) {
                            activity.successCallBack();
                        } else if (errCode == 200) {//去查询
                            String order_id = s.getOrder_id();
                            String outTradeNo = s.getOut_trade_no();
                            KLog.v("order_id--"+order_id+"outTradeNo--"+outTradeNo);
                            SwishCardApplication.getInstance().preferenceUtils.setOrder_id(order_id);
                            SwishCardApplication.getInstance().preferenceUtils.setOutTradeNo(outTradeNo);
                            activity.checkingPayRequest();
                        } else {
                            activity.faildCallBack();
                        }
                    }
                });
    }


    /**
     * 查询二维码支付订单
     */
    public void checkingOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("aid", AppConstants.AID);
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getMerchant_id());
        map.put("order_id", SwishCardApplication.getInstance().preferenceUtils.getOrder_id());
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + AppConstants.KEY;
        String sign = MD5Utils.md5(result);//最终结果
        qrPayService.result(AppConstants.AID, sign, SwishCardApplication.getInstance().preferenceUtils.getMerchant_id(),
                SwishCardApplication.getInstance().preferenceUtils.getOrder_id())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    if (s != null) {
                        int errCode = s.getErrcode();
                        if (errCode == 0) {
                            String state = s.getOrderinfo().getState();
                            if (state.equals("0")) {
                                activity.responseEventCallBack();
                            }
                        }
                    }
                });
    }


    /**
     * 查询扫码支付
     */
    public void checkingScanOrder() {
        Map<String, String> map = new HashMap<>();
        map.put("aid", AppConstants.AID);
        map.put("order_id", SwishCardApplication.getInstance().preferenceUtils.getOrder_id());
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getMerchant_id());
        map.put("out_trade_no", SwishCardApplication.getInstance().preferenceUtils.getOutTradeNo());
        map.put("pay_type", SwishCardApplication.getInstance().preferenceUtils.getMobileType());
        map.put("pay_money", SwishCardApplication.getInstance().preferenceUtils.getMoney());
        map.put("trade_type", "1");
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + AppConstants.KEY;
        String sign = MD5Utils.md5(result);//最终结果
        qrPayService.auQue(AppConstants.AID, sign, SwishCardApplication.getInstance().preferenceUtils.getOrder_id(),
                SwishCardApplication.getInstance().preferenceUtils.getMerchant_id(), SwishCardApplication.getInstance().preferenceUtils.getOutTradeNo(),
                SwishCardApplication.getInstance().preferenceUtils.getMobileType(), SwishCardApplication.getInstance().preferenceUtils.getMoney(), "1")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    Logger.e(new Gson().toJson(s));
                    if (s != null) {
                        int errCode = s.getErrcode();
                        if (errCode == 0) {
                            activity.checkingPayResult(0);
                        } else if (errCode == 200) {

                        } else {
                            activity.checkingPayResult(1);
                        }
                    }
                });
    }
}
