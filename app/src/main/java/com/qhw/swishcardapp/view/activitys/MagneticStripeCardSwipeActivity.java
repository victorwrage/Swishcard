package com.qhw.swishcardapp.view.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.model.callback.NoneValueOperationListener;
import com.qhw.swishcardapp.presenter.impl.CupCardPaymentSelectionPresenter;
import com.qhw.swishcardapp.presenter.impl.MagneticStripeCardSwipePresenter;
import com.qhw.swishcardapp.utils.IsNetWorkUtils;
import com.qhw.swishcardapp.view.dialogs.BottomDialog;
import com.qhw.swishcardapp.view.dialogs.PayResultConfirmDialog;

import butterknife.BindView;

public class MagneticStripeCardSwipeActivity extends BaseActivity implements NoneValueOperationListener {

    @BindView(R.id.tv_msg)
    TextView tv_msg;
    private String serial;//流水号
    private String cardInfo;
    private Boolean exit = false;
    private MagneticStripeCardSwipePresenter magneticStripeCardSwipePresenter;
    private String type;
    private Handler promptHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x02:
                    Bundle b = msg.getData();
                    cardInfo = b.getString("MSG");
                    if (cardInfo.indexOf("=") != -1) {
                        if (!cardInfo.equals("")) {
                            if (!cardInfo.equals("faild")) {
                                exit = true;
                                BottomDialog bottomDialog = new BottomDialog(MagneticStripeCardSwipeActivity.this);
                                bottomDialog.show(CupCardPaymentSelectionPresenter.TYPE_MAGNETIC);
                            } else {
                                finish();
                                toast("刷卡方式错误,请重试");
                            }
                        } else {
                            finish();
                            toast("刷卡方式错误,请重试");
                        }
                    } else {
                        finish();
                        toast("刷卡方式错误,请重试");
                    }
                    break;
                case 0x03:
                    finish();
                    toast("刷卡器打开失败，请重试");
                    break;
                case 0x04:
                    finish();
                    toast("读取失败，请重试");
                    break;
                case 0x05:
                    toast("开始刷卡");
                    break;
                case 0x06:
                    String times = (String) msg.obj;
                    tv_msg.setText(times + "s");
                    break;
                case 0x07:
                    tv_msg.setText("刷卡超时,请退出重试");
                    exit = true;
                    break;
                case 0x10:
                    type = (String) msg.obj;
                    resultConfirmDialog = new PayResultConfirmDialog(MagneticStripeCardSwipeActivity.this, type, new NoneValueOperationListener() {
                        @Override
                        public void responseEventCallBack() {
                            if (SwishCardApplication.getInstance().preferenceUtils.getIsThirdPartyAccess().equals("1")) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                intent.setAction("com.qhw.shopmanagerapp.pay.action");
                                if (type.equals("0")) {
                                    bundle.putString("result", "faild");
                                } else {
                                    bundle.putString("result", "success");
                                }
                                bundle.putString("type", "0");
                                intent.putExtra("bundle", bundle);
                                sendBroadcast(intent);
                                SwishCardApplication.getInstance().finishActivity();
                            } else {
                                finish();
                            }
                        }

                        @Override
                        public void responseEventCallBack(String value) {

                        }
                    });
                    resultConfirmDialog.show();
                    break;
            }
        }
    };
    private PayResultConfirmDialog resultConfirmDialog;


    @Override
    public int activityContentView() {
        return R.layout.activity_magnetic_stripe_card_swipe;
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
        ImmersionBar.with(this).destroy();
        if (resultConfirmDialog != null) {
            resultConfirmDialog.dismiss();
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
        Intent intent = getIntent();
        serial = intent.getBundleExtra("bundle").getString("serial");
        magneticStripeCardSwipePresenter = new MagneticStripeCardSwipePresenter(this, promptHandler);

    }

    @Override
    public void netWorkRequests() {
        magneticStripeCardSwipePresenter.startSwish();
    }


    @Override
    public void responseEventCallBack() {

    }

    /**
     * 密码回调
     *
     * @param value
     */
    @Override
    public void responseEventCallBack(String value) {
        Boolean flag = IsNetWorkUtils.isNetWorkCheck(MagneticStripeCardSwipeActivity.this);
        if (flag == true) {
            magneticStripeCardSwipePresenter.cupPayment(cardInfo, value, serial);
        } else {
            toast("请先打开网络");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (exit == true) {
                finish();
            } else {
                toast("刷卡进行中，禁止退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
