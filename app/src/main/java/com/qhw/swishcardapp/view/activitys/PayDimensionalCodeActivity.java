package com.qhw.swishcardapp.view.activitys;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.model.callback.NoneValueOperationListener;
import com.qhw.swishcardapp.model.callback.PayResultOnNextListener;
import com.qhw.swishcardapp.presenter.impl.PayDimensionalCodePresenter;
import com.qhw.swishcardapp.utils.DateUtils;
import com.qhw.swishcardapp.view.customs.PrinterTools;
import com.qhw.swishcardapp.view.dialogs.BarcodePopup;
import com.qhw.swishcardapp.view.dialogs.PayResultConfirmDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class PayDimensionalCodeActivity extends BaseActivity implements NoneValueOperationListener, PayResultOnNextListener {

    @BindView(R.id.iv_qrCode)
    ImageView iv_qrCode;
    @BindView(R.id.iv_scan)
    ImageView iv_scan;
    private PayDimensionalCodePresenter payDimensionalCodePresenter;
    private final String CHECKINGRESULT_HINT = "正在查询支付结果";
    private Timer timer;
    private Timer checkingScanTimer;
    private PayResultConfirmDialog resultConfirmDialog;
    private CheckingOrderTimerTask checkingOrderTimerTask;
    private int count = 30;
    private int scanCount = 30;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11://支付超时回调处理
                    showLoadingDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            payResultInfoShow("2", "faild");
                        }
                    }, 2000);

                    break;
                case 0x22:
                    dismissLoadingDialog();
                    payResultInfoShow("2", "faild");
                    break;
            }
        }
    };
    private BarcodePopup barcodePopup;

    @Override
    public int activityContentView() {
        return R.layout.activity_pay_dimensional_code;
    }

    @Override
    public void activityOnStart() {

    }

    @Override
    public void activityOnRestart() {

    }


    @Override
    public void activityOnPause() {

    }

    @Override
    public void activityOnStop() {

    }
    @Override
    public void activityonOnResume() {

        ImmersionBar.with(this).statusBarColor(R.color.colorAccent).fitsSystemWindows(true) .init();
    }
    @Override
    public void activityOnDestroy() {
        ImmersionBar.with(this).destroy();
        if (timer != null) {
            timer.cancel();
        }
        if (checkingOrderTimerTask != null) {
            checkingOrderTimerTask.cancel();
        }

        if (resultConfirmDialog != null) {
            resultConfirmDialog.dismiss();
        }

        if (barcodePopup != null) {
            barcodePopup.cancleTimer();
        }

        if (checkingScanTimer != null) {
            checkingScanTimer.cancel();
        }
    }

    @Override
    public void initializedData() {
        payDimensionalCodePresenter = new PayDimensionalCodePresenter(this);
        SwishCardApplication.getInstance().getD2000Scan().DLL_ScanOpen();
        SwishCardApplication.getInstance().getD2000Print();
    }

    @Override
    public void netWorkRequests() {
        payDimensionalCodePresenter.generateDimentsionalCode();
    }

    /**
     * 支付成功回调处理
     */
    @Override
    public void responseEventCallBack() {
        showLoadingDialog();
        if (timer != null) {
            timer.cancel();
        }
        if (checkingOrderTimerTask != null) {
            checkingOrderTimerTask.cancel();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                payResultInfoShow("1", "success");
            }
        }, 2000);

    }

    /**
     * 生成二维码回调处理
     *
     * @param value
     */
    @Override
    public void responseEventCallBack(String value) {
        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(value, 300, Color.parseColor("#000000"));
        iv_qrCode.setImageBitmap(bitmap);
        //开始查询支付结果
        initLoadingDialog(CHECKINGRESULT_HINT);
        timer = new Timer();
        checkingOrderTimerTask = new CheckingOrderTimerTask();
        timer.schedule(checkingOrderTimerTask, 200, 2000);
    }

    @OnClick(R.id.iv_scan)
    public void onClcik(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                barcodePopup = new BarcodePopup(this);
                barcodePopup.showPopupWindow();
                break;
        }
    }

    /**
     * 扫码支付成功回调
     */
    @Override
    public void successCallBack() {
        payResultInfoShow("1", "success");
    }

    /**
     * 扫码支付失败回调
     */
    @Override
    public void faildCallBack() {
        payResultInfoShow("0", "faild");
    }

    /**
     * 扫码支付
     *
     * @param code
     */
    @Override
    public void auCodeCallBack(String code) {
        payDimensionalCodePresenter.scanPay(code);
    }

    /**
     * 支付请求查询
     */
    @Override
    public void checkingPayRequest() {
        initLoadingDialog("支付查询中");
        showLoadingDialog();
        checkingScanTimer = new Timer();
        checkingScanTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                scanCount--;
                if (scanCount == 0) {
                    mHandler.sendEmptyMessage(0x22);
                    checkingScanTimer.cancel();
                }
                payDimensionalCodePresenter.checkingScanOrder();
            }
        }, 200, 2000);
    }

    /**
     * 扫码密码支付查询结果回调
     */
    @Override
    public void checkingPayResult(int code) {
        if (checkingScanTimer != null) {
            checkingScanTimer.cancel();
        }
        dismissLoadingDialog();
        if (code == 0) {
            successCallBack();
        } else {
            faildCallBack();
        }
    }

    /**
     * 查询订单支付结果
     */
    class CheckingOrderTimerTask extends TimerTask {

        @Override
        public void run() {
         /*   if (count == 0) {
                if (checkingOrderTimerTask != null) {
                    checkingOrderTimerTask.cancel();
                }

                if (timer != null) {
                    timer.cancel();
                }
                mHandler.sendEmptyMessage(0x11);
            }
            count--;*/
            payDimensionalCodePresenter.checkingOrder();
        }
    }


    /**
     * 支付情况显示
     *
     * @param type
     * @param result
     */
    public void payResultInfoShow(String type, final String result) {
        resultConfirmDialog = new PayResultConfirmDialog(PayDimensionalCodeActivity.this, type, new NoneValueOperationListener() {
            @Override
            public void responseEventCallBack() {
                if (SwishCardApplication.getInstance().preferenceUtils.getIsThirdPartyAccess().equals("1")) {
                    Intent intent = new Intent();
                    intent.setAction("com.qhw.shopmanagerapp.pay.action");
                    Bundle bundle = new Bundle();
                    bundle.putString("result", result);
                    bundle.putString("type", String.valueOf(Integer.valueOf(SwishCardApplication.getInstance().preferenceUtils.getMobileType()) + 1));
                    intent.putExtra("bundle", bundle);
                    sendBroadcast(intent);
                    SwishCardApplication.getInstance().finishActivity();
                } else {
                    if (result.equals("success")) {
                        Map<String, String> mprintMap = new HashMap<>();
                        String payType = SwishCardApplication.getInstance().preferenceUtils.getMobileType();
                        if (payType.equals("0")) {
                            payType = "微信支付";
                        } else if (payType.equals("1")) {
                            payType = "支付宝支付";
                        } else if (payType.equals("2")) {
                            payType = "QQ钱包支付";
                        }
                        mprintMap.put("name", SwishCardApplication.getInstance().preferenceUtils.getMerchant_id());
                        mprintMap.put("paytype", payType);
                        mprintMap.put("order_id", SwishCardApplication.getInstance().preferenceUtils.getOrder_id());
                        mprintMap.put("time", DateUtils.getDate());
                        mprintMap.put("money", SwishCardApplication.getInstance().preferenceUtils.getMoney());
                        PrinterTools.DLL_PrnInit(mprintMap);
                        PrinterTools.DLL_PrnInit(mprintMap);
                    }
                    finish();
                }
            }

            @Override
            public void responseEventCallBack(String value) {

            }
        });
        resultConfirmDialog.show();
    }

}
