package com.qhw.swishcardapp.view.activitys;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.pos.api.Printer;
import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.bean.PayResultBean;
import com.qhw.swishcardapp.callback.DialogCallback;
import com.qhw.swishcardapp.utils.D2000V1ScanInitUtils;
import com.qhw.swishcardapp.utils.FormatUrlUtils;
import com.qhw.swishcardapp.utils.MD5Utils;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 重打印
 *
 * @author xyl
 * @date 2017-11-10
 */
public class ScanAndPrintActivity extends BaseActivity {

    @BindView(R.id.order_detail)
    TextView order_detail;
    @BindView(R.id.order_tip)
    TextView order_tip;

    private Executor executor;
    D2000V1ScanInitUtils d2000V1ScanInitUtils;
    private final static int SCAN_CLOSED = 20;
    Printer printer;
    private HashMap<String, String> order_map = new HashMap<>();


    private Handler promptHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    KLog.v("scan-txt:" + (String) msg.obj);
                    sendData((String) msg.obj);
                    break;
                case SCAN_CLOSED:
                    //        if (fragment1 != null) fragment1.openPrinter();
                    break;
                default:
                    break;
            }
        }


    };
    private boolean isDeviceInit = false;

    /**
     * 请求查询
     *
     * @param code
     */
    private void sendData(String code) {
        //code = "Qianhaiwei2017111018053124765028";
        //  code = "Qianhaiwei2017111017392343430656";
        //code = "112001711101805312726";

        String merchant_id = SwishCardApplication.getInstance().preferenceUtils.getMerchant_id();
        Map<String, String> map = new HashMap<>();
        map.put("aid", AppConstants.AID);
        map.put("merchant_id", merchant_id);
        map.put("up_order_id", code);
        String result = FormatUrlUtils.formatUrlMap(map, false, false) + "&key=" + AppConstants.KEY;
        String sign = MD5Utils.md5(result);//最终结果

        map.put("sign", sign);
        JSONObject jsonObject = new JSONObject(map);

        OkGo.<PayResultBean>get(AppConstants.URL_IP + AppConstants.URL_ORDER_S + "&aid=" + AppConstants.AID
                + "&merchant_id=" + merchant_id + "&up_order_id=" + code + "&sign=" + sign)//
                .tag(ScanAndPrintActivity.this)//
                //  .upJson( jsonObject)//
                .execute(new DialogCallback<PayResultBean>(ScanAndPrintActivity.this) {
                    @Override
                    public void onSuccess(Response<PayResultBean> response) {
                        PayResultBean payResultBean = response.body();
                        if (payResultBean.getErrmsg().equals("success")) {
                            order_tip.setText("---支付详情(点击打印)---");
                            String order_id = payResultBean.getOrderinfo().getOrder_id();
                            String type = payResultBean.getOrderinfo().getSource();
                            if (type.equals("0")) {
                                type = "微信支付";
                            } else if (type.equals("1")) {
                                type = "支付宝支付";
                            }
                            String create_time = payResultBean.getOrderinfo().getCreate_time();
                            String pay_time = payResultBean.getOrderinfo().getPay_time();
                            String pay_money = payResultBean.getOrderinfo().getPay_money();
                            order_map.put("name", merchant_id);
                            order_map.put("paytype", type);
                            order_map.put("order_id", order_id);
                            order_map.put("time", pay_time);
                            order_map.put("money", pay_money);

                            String str = "商户号:" + order_map.get("name") + "\n"
                                    + "交易类型:扫码支付" + "\n"
                                    + "支付渠道:" + order_map.get("paytype") + "\n"
                                    + "订单号:" + order_map.get("order_id") + "\n"
                                    + "日期:" + order_map.get("time") + "\n"
                                    + "金额:RMB " + order_map.get("money") + "\n";

                            order_detail.setText(str);
                            showPrintDialog();
                        } else {
                            startScan();
                            order_tip.setText("---没有此笔交易---");
                        }
                    }

                    @Override
                    public void onError(Response<PayResultBean> response) {
                        super.onError(response);
                        startScan();
                        order_tip.setText("---网络错误,请重试---");
                    }
                });
    }

    private void showPrintDialog() {
        new MaterialDialog.Builder(ScanAndPrintActivity.this)
                .title("提示")
                .content("是否打印?")
                .positiveText("确定")
                .negativeText("取消")
                .onNegative((materialDialog, dialogAction) -> startScan())
                .onPositive((materialDialog, dialogAction) -> openPrinter())
                .autoDismiss(true)
                .cancelable(false)
                .show();
    }

    @OnClick( R.id.order_detail)
    public void onClcik(View view) {
        switch (view.getId()) {
            case  R.id.order_detail:
                showPrintDialog();
                break;
        }
    }

    public void openPrinter() {
        initLoadingDialog("请等待打印完成");
        showLoadingDialog();
        printer = new Printer(this, bRet -> executor.execute(() -> {
            int iRet = -1;
            iRet = printer.DLL_PrnInit();
            KLog.v("iRet" + iRet);
            if (iRet == 0) {
                printContent();
            } else {
                toast( "打开打印机错误");
            }
        }));
        printContent();
        promptHandler.postDelayed(() -> {
            dismissLoadingDialog();
            toast("打印完成");
            finish();
        }, 5000);
    }

    protected void finalize() throws Throwable { super.finalize();
        KLog.v("====LeakActivity has been recycled!");
    }

    private void printContent() {
        printer.DLL_PrnInit();
        printer.DLL_PrnSetFont((byte) 24, (byte) 24, (byte) 0x00);
        printer.DLL_PrnStr("                       万店通刷" + "\n");
        printer.DLL_PrnStr("******************************************" + "\n");
        printer.DLL_PrnStr("商户号:" + order_map.get("name") + "\n");
        printer.DLL_PrnStr("交易类型:扫码支付" + "\n");
        printer.DLL_PrnStr("支付渠道:" + order_map.get("paytype") + "\n");
        printer.DLL_PrnStr("订单号:" + order_map.get("order_id") + "\n");
        printer.DLL_PrnStr("日期:" + order_map.get("time") + "\n");
        printer.DLL_PrnStr("金额:RMB " + order_map.get("money") + "\n");
        printer.DLL_PrnStr("备注:" + "\n");
        printer.DLL_PrnStr("您的24H商户贴身管家" + "\n");
        printer.DLL_PrnStr("客服热线: 8008208820" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("        客户签名:" + "\n");
        printer.DLL_PrnStr("*********************复*******************" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConstants.ENABLE_SCAN_PRINT && isDeviceInit) {
            initLoadingDialog("请稍等");
            showLoadingDialog();
            initDevice();
            promptHandler.postDelayed(() -> {
                dismissLoadingDialog();
            }, 5000);
        }
    }

    private void startScan() {
        initLoadingDialog("请稍等");
        showLoadingDialog();
        promptHandler.postDelayed(() -> {
            dismissLoadingDialog();
            executor.execute(() -> {
                d2000V1ScanInitUtils.open();
                d2000V1ScanInitUtils.d2000V1ScanOpen();
            });
        }, 2000);
    }

    @Override
    public int activityContentView() {
        return R.layout.activity_pay_scan_printer;
    }

    @Override
    public void activityOnStart() {

    }

    @Override
    public void activityOnRestart() {

    }

    @Override
    public void activityonOnResume() {
        ImmersionBar.with(this).statusBarColor(R.color.colorAccent).fitsSystemWindows(true) .init();
    }

    @Override
    public void activityOnDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        OkGo.getInstance().cancelTag(this);
        if (AppConstants.ENABLE_SCAN_PRINT) {
            if (d2000V1ScanInitUtils != null) {
                d2000V1ScanInitUtils.close();
                if (printer != null) {
                    printer.DLL_PrnRelease();
                }
                d2000V1ScanInitUtils.exitActivity();
                d2000V1ScanInitUtils = null;
            }
        }
    }


    @Override
    public void activityOnPause() {

    }

    @Override
    public void activityOnStop() {

    }

    @Override
    public void initializedData() {
        executor = Executors.newSingleThreadScheduledExecutor();
        if (AppConstants.ENABLE_SCAN_PRINT) {
            initDevice();
        }
    }

    /**
     * 初始化设备
     */
    private void initDevice() {
        executor.execute(() -> {
            KLog.v("initDevice");
            d2000V1ScanInitUtils = D2000V1ScanInitUtils.getInstance(ScanAndPrintActivity.this, promptHandler);
            if (!d2000V1ScanInitUtils.getStart()) {
                d2000V1ScanInitUtils.open();
            }
            d2000V1ScanInitUtils.d2000V1ScanOpen();
            isDeviceInit = true;
        });
    }

    @Override
    public void netWorkRequests() {

    }

}
