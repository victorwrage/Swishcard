package com.qhw.swishcardapp.presenter.impl;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.BasePresenter;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.bean.Pack8583;
import com.qhw.swishcardapp.bean.UpdateBean;
import com.qhw.swishcardapp.model.callback.NoneValueOperationListener;
import com.qhw.swishcardapp.model.socket.ClientLaunch;
import com.qhw.swishcardapp.retrofirapi.ConnectonPospService;
import com.qhw.swishcardapp.utils.DateUtils;
import com.qhw.swishcardapp.utils.FormatUrlUtils;
import com.qhw.swishcardapp.utils.IsNetWorkUtils;
import com.qhw.swishcardapp.utils.MD5Utils;
import com.qhw.swishcardapp.utils.SignUtils;
import com.qhw.swishcardapp.view.activitys.CupCardPaymentSelectionActivity;
import com.qhw.swishcardapp.view.activitys.CupPaymentConfigurationActivity;
import com.qhw.swishcardapp.view.activitys.MainActivity;
import com.qhw.swishcardapp.view.activitys.MobilePaymentConfigurationActivity;
import com.qhw.swishcardapp.view.dialogs.UpdateDialog;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Hello_world on 2017/9/3.
 */

public class MainPresenter extends BasePresenter {

    private MainActivity activity;
    private ClientLaunch clientLaunch;
    private ConnectonPospService connectionPospService;
    private final String SOCKET_CONNECTION_INTERRUPT_PROMPT = "连接pos中心中断,请退出重试";
    private final String SIGN_SUCCESS_PROMPT = "签到成功";
    private final String SIGN_FAILD_PROMPT = "签到失败，密钥错误";
    private Handler handler = new Handler();
    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        clientLaunch = new ClientLaunch();
        connectionPospService = SwishCardApplication.getInstance().retrofit.create(ConnectonPospService.class);
    }

    /**
     * posp签到
     */
    public void sign() {

        Pack8583 pack8583 = new Pack8583();
        pack8583.setHeader(AppConstants.HEAD_8583);
        pack8583.setMessageType(AppConstants.SIGN_MESSAGE_TYPE_8583);
        pack8583.setBody(41, SwishCardApplication.getInstance().preferenceUtils.getTermianl()); //终端
        pack8583.setBody(42, SwishCardApplication.getInstance().preferenceUtils.getTenant()); //商户
        pack8583.setBody(60, "00000001004");
        pack8583.setBody(63, "001");
        String packResult = pack8583.pack();

        Observable.just(packResult)
                .map(s-> clientLaunch.Client(s, new NoneValueOperationListener() {
                    @Override
                    public void responseEventCallBack() {
                        dismissLoadingDialog();
                        handler.post(()->toast(activity, SOCKET_CONNECTION_INTERRUPT_PROMPT));
                    }

                    @Override
                    public void responseEventCallBack(String value) {

                    }
                })).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    dismissLoadingDialog();
                    if (!s.equals("")) {
                        if (s.length() > 50) {
                            int len = s.length();
                            String workKey = s.substring(len - 120, len);
                            String[] keys = SignUtils.createKey(workKey);
                            if (!keys[0].equals("") && !keys[1].equals("") && !keys[2].equals("")) {
                                SwishCardApplication.getInstance().preferenceUtils.setPIN(keys[0]);
                                KLog.v("SIGN------"+keys[1]);
                                String temp = keys[1];
                                String str = keys[1].replace(temp.substring(temp.length()-16),"0000000000000000");
                                KLog.v("SIGN----str--"+str);
                                SwishCardApplication.getInstance().preferenceUtils.setMAC(str);
                                SwishCardApplication.getInstance().preferenceUtils.setTPK(keys[2]);
                                SwishCardApplication.getInstance().preferenceUtils.setSignDate(DateUtils.getDateNoHour());
                                toast(activity, SIGN_SUCCESS_PROMPT);
                                swishCard();//去刷卡
                            } else {
                                toast(activity, SIGN_FAILD_PROMPT);
                            }
                        } else {
                            toast(activity, SOCKET_CONNECTION_INTERRUPT_PROMPT);
                        }
                    } else {
                        toast(activity, SOCKET_CONNECTION_INTERRUPT_PROMPT);
                    }
                });
    }

    /**
     * 获取批次号
     */
    public void getBatchNumber() {
        initLoadingDialog(activity, "正在签到");
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();
        map.put("merchant_id", SwishCardApplication.getInstance().preferenceUtils.getTenant());
        map.put("device_id", SwishCardApplication.getInstance().preferenceUtils.getTermianl());
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + SwishCardApplication.getInstance().preferenceUtils.getKey();
        String sign = MD5Utils.md5(result);//最终结果
        connectionPospService.getBatchNumber(SwishCardApplication.getInstance().preferenceUtils.getTenant(),
                SwishCardApplication.getInstance().preferenceUtils.getTermianl(), sign)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    if (s != null) {
                        int errCode = s.getErrCode();
                        String errMsg = s.getErrMsg();
                        if (errCode == 0) {
                            SwishCardApplication.getInstance().preferenceUtils.setBatchNo(s.getBatch_no());
                            sign();
                        } else {
                            dismissLoadingDialog();
                            toast(activity, errMsg);
                        }
                    } else {
                        dismissLoadingDialog();
                    }
                });
    }


    /**
     * 移动支付
     *
     * @param pclass
     * @param payType
     * @param money
     */
    public void mobilePay(Class<?> pclass, String payType, String money) {
        if (!SwishCardApplication.getInstance().preferenceUtils.getMerchant_id().equals("")) {
            if ((money != null)
                    && (!money.isEmpty())
                    && (Float.valueOf(money) != 0)) {
                if (Double.valueOf(money) >= 0.01) {
                    //支付
                    SwishCardApplication.getInstance().preferenceUtils.setMoney(money);
                    SwishCardApplication.getInstance().preferenceUtils.setMobileType(payType);
                    openActivity(activity, pclass, null, -1);
                } else {
                    toast(activity, "移动支付金额必须大于0.01元");
                }
            } else {
                toast(activity, "请输入的大于0的金额 !");
            }
        } else {
            openActivity(activity, MobilePaymentConfigurationActivity.class, null, -1);
            toast(activity, "请先设置参数");
        }
    }

    /**
     * 银联支付1
     *
     * @param money
     */
    public void CardPay(String money) {
        if (!SwishCardApplication.getInstance().preferenceUtils.getTermianl().equals("") &&
                !SwishCardApplication.getInstance().preferenceUtils.getTenant().equals("") &&
                !SwishCardApplication.getInstance().preferenceUtils.getKey().equals("")) {
            if ((money != null)
                    && (!money.isEmpty())
                    && (Float.valueOf(money) != 0)) {
                if (Double.valueOf(money) >= 10) {
                    SwishCardApplication.getInstance().preferenceUtils.setMoney(money);
                    swishCard();
                } else {
                    toast(activity, "刷卡金额必须大于10元");
                }
            } else {
                toast(activity, "请输入的大于0的金额 !");
            }
        } else {
            openActivity(activity, CupPaymentConfigurationActivity.class, null, -1);
            toast(activity, "请先设置参数");
        }
    }

    /**
     * 银联支付2
     */
    private void swishCard() {
        if (SwishCardApplication.getInstance().preferenceUtils.getSignDate().equals(DateUtils.getDateNoHour())) {
           // getBatchNumber();
            openActivity(activity, CupCardPaymentSelectionActivity.class, null, -1);
        } else {
            Boolean flag = IsNetWorkUtils.isNetWorkCheck(activity);
            if (flag == true) {
                getBatchNumber();
            } else {
                toast(activity, "请先打开网络");
            }
        }
    }


    public void checkingUpdate() {
        connectionPospService.getYingYong("111", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    if (s != null) {
                        String msg = s.getErrmsg();
                        if (msg.equals("success")) {
                            List<UpdateBean.ContentBean> list = s.getContent();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getName().equals("万店通刷")) {
                                    String verCode = list.get(i).getApp_version();
                                    String path = list.get(i).getApp_url();
                                    Boolean flag = isCheckUpdate(verCode);
                                    if (flag == true) {
                                        UpdateDialog dialog = new UpdateDialog(activity, path);
                                        dialog.show();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 判断版本号
     *
     * @param code
     * @return
     */
    public Boolean isCheckUpdate(String code) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), 0);
            if (info.versionCode < Integer.valueOf(code)) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
