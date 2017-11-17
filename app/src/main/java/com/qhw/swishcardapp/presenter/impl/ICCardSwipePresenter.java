package com.qhw.swishcardapp.presenter.impl;

import android.os.Handler;
import android.os.Message;

import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.BasePresenter;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.bean.Pack8583Icc;
import com.qhw.swishcardapp.model.callback.NoneValueOperationListener;
import com.qhw.swishcardapp.model.socket.ClientLaunch;
import com.qhw.swishcardapp.retrofirapi.ConnectonPospService;
import com.qhw.swishcardapp.utils.DateUtils;
import com.qhw.swishcardapp.utils.FormatUrlUtils;
import com.qhw.swishcardapp.utils.IccUtils;
import com.qhw.swishcardapp.utils.MD5Utils;
import com.qhw.swishcardapp.utils.McrUtils;
import com.qhw.swishcardapp.utils.Utils;
import com.qhw.swishcardapp.view.activitys.ICCardSwipeActivity;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Hello_world on 2017/9/3.
 */

public class ICCardSwipePresenter extends BasePresenter {
    private ICCardSwipeActivity activity;
    private Handler promptHandler;

    private ClientLaunch clientLaunch;
    private ConnectonPospService service;
    private String time;
    private String serial;
    private Executor executor;

    public ICCardSwipePresenter(ICCardSwipeActivity activity, Handler promptHandler) {
        this.activity = activity;
        this.promptHandler = promptHandler;
        clientLaunch = new ClientLaunch();
        executor = Executors.newSingleThreadScheduledExecutor();
        service = SwishCardApplication.getInstance().retrofit.create(ConnectonPospService.class);
    }

    /**
     * 开启刷卡器
     */
    public void startSwish() {
        executor.execute(() ->IccUtils.getInstance().checkIccCard(promptHandler, SwishCardApplication.getInstance().getD2000Icc()));
    }


    /**
     * 银联交易
     *
     * @param info
     * @param passWord
     * @param serial
     */
    public void cupPayment(final String[] info, final String passWord, final String serial) {
        this.serial = serial;
        initLoadingDialog(activity, "正在交易中");
        showLoadingDialog();
        final String[] cardArr = info[0].split("=");
        Pack8583Icc pack8583 = new Pack8583Icc();
        pack8583.setHeader(AppConstants.HEAD_8583);
        pack8583.setMessageType(AppConstants.CONSUME_MESSAGE_TYPE_8583);

        pack8583.setBody(2, cardArr[0]); // 账号
        pack8583.setBody(3, "000000");
        pack8583.setBody(4, SwishCardApplication.getInstance().preferenceUtils.getMoney());
        pack8583.setBody(11, serial);
        pack8583.setBody(22, "051");
        pack8583.setBody(25, "00");
        pack8583.setBody(26, "12");

        pack8583.setBody(
                35,
                McrUtils.makeTrack(Utils.mag2data(cardArr),
                        SwishCardApplication.getInstance().preferenceUtils.getTPK()));// 二磁道信息
        pack8583.setBody(41, SwishCardApplication.getInstance().preferenceUtils.getTermianl());
        pack8583.setBody(42, SwishCardApplication.getInstance().preferenceUtils.getTenant());
        pack8583.setBody(49, "156");
        pack8583.setBody(
                52,
                McrUtils.makePIN(passWord, cardArr[0],
                        SwishCardApplication.getInstance().preferenceUtils.getPIN()));// PIN密码
       // pack8583.setBody(53, "2000000000000000");
        pack8583.setBody(53, "2600000000000000");
        pack8583.setBody(55, info[1]);
        pack8583.setBody(60, "22000001");
        pack8583.setBody(
                64,
                McrUtils.makeMAC(SwishCardApplication.getInstance().preferenceUtils.getMAC(),
                        pack8583.packBody()));// MAC
        String packResult = pack8583.pack();
        Observable.just(packResult)
                .map(s ->
                        clientLaunch.Client(packResult, new NoneValueOperationListener() {
                            @Override
                            public void responseEventCallBack() {
                                dismissLoadingDialog();
                            }

                            @Override
                            public void responseEventCallBack(String value) {

                            }
                        }))
                .subscribeOn(Schedulers.io())
                .onErrorReturn(s -> {
                    dismissLoadingDialog();
                    return "" + s;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    dismissLoadingDialog();
                    KLog.v("cupPayment--" + s);
                    if (s != null) {
                        String payMentResult = "";
                        if (s.length() > 50) {
                            String termianl = Utils.convertStringToHex(SwishCardApplication.getInstance().preferenceUtils.getTermianl());
                            String tenant = Utils.convertStringToHex(SwishCardApplication.getInstance().preferenceUtils.getTenant());

                            KLog.v("termianl-" + termianl);
                            KLog.v("tenant-" + tenant);
                            String value = termianl + tenant;

                            //String[] strArr = s.replace(value,",").split(",");
                            int count = s.indexOf(termianl);
                            String r = s.substring(0, count);
//                            KLog.v("count-" + count);
                            String result = r.substring(r.length() - 4, r.length());

                            KLog.v("result-" + result + "---"+new String(Utils.str2Bcd(result)));
                            if (result.equals("3030")) {
                                payMentResult = "1";

                            } else {
                                payMentResult = "0";
                            }
                        } else {
                            payMentResult = "0";
                        }
                        time = DateUtils.getDate();//获取交易时间
                        addSerialNumber(payMentResult);
                    } else {
                        toast(activity, "pos中心异常，请退出重试");
                    }
                });
    }

    /**
     * 添加一笔交易流水
     *
     * @param is_success
     */
    public void addSerialNumber(final String is_success) {
        if (is_success.equals("1")) {
            initLoadingDialog(activity, "交易成功");
        } else {
            initLoadingDialog(activity, "交易失败");
        }
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getTenant());
        map.put("device_id", SwishCardApplication.getInstance().preferenceUtils.getTermianl());
        map.put("trade_num", serial);
        map.put("is_success", is_success);
        map.put("money", SwishCardApplication.getInstance().preferenceUtils.getMoney());
        map.put("trade_time", time);
        map.put("batch_no", SwishCardApplication.getInstance().preferenceUtils.getBatchNo());
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + SwishCardApplication.getInstance().preferenceUtils.getKey();
        String sign = MD5Utils.md5(result);//最终结果
        service.addBn(SwishCardApplication.getInstance().preferenceUtils.getTenant(),
                SwishCardApplication.getInstance().preferenceUtils.getTermianl(), sign, serial, is_success,
                SwishCardApplication.getInstance().preferenceUtils.getMoney(), time, SwishCardApplication.getInstance().preferenceUtils.getBatchNo())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    dismissLoadingDialog();
                    if (s != null) {
                        Message message = promptHandler.obtainMessage();
                        message.what = 0x10;
                        message.obj = is_success;
                        promptHandler.sendMessage(message);
                    }
                });
    }
}
