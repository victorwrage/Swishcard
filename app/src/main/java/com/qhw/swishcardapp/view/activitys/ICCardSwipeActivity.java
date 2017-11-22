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
import com.qhw.swishcardapp.presenter.impl.ICCardSwipePresenter;
import com.qhw.swishcardapp.utils.IccUtils;
import com.qhw.swishcardapp.utils.IsNetWorkUtils;
import com.qhw.swishcardapp.view.dialogs.BottomDialog;
import com.qhw.swishcardapp.view.dialogs.PayResultConfirmDialog;
import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ICCardSwipeActivity extends BaseActivity implements NoneValueOperationListener {
    private final int TYPE_CARD_CANCEL = 0;
    private final int TYPE_CARD_FAIL = TYPE_CARD_CANCEL + 1;
    private int TIMEOUT = 30;
    private Disposable disposable;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    private String serial;//流水号
    private String cardInfo;
    private String[] info;
    private Boolean exit = false;
    private ICCardSwipePresenter icCardSwipePresenter;
    private String type;

    private Handler promptHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IccUtils.CARD_READ_ERROR:
                    toast("读卡失败");
                    KLog.v("CARD_READ_ERROR:" + (String) msg.obj);
                    postDelayed(()-> icCardSwipePresenter.startSwish(),500);
                    break;
                case IccUtils.CARD_READ_OK:
                    exit = true;
                    disposable.dispose();

                    tv_msg.setText("刷卡成功，请输入密码");
                    info = (String[]) msg.obj;
                    cardInfo = ((String[]) msg.obj)[0];
                    if (cardInfo.indexOf("=") != -1) {
                        if (!cardInfo.equals("")) {
                            if (!cardInfo.equals("faild")) {
                                exit = true;
                                BottomDialog bottomDialog = new BottomDialog(ICCardSwipeActivity.this);
                                bottomDialog.show(CupCardPaymentSelectionPresenter.TYPE_ICC);
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
                    postDelayed(()-> icCardSwipePresenter.startSwish(),500);
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
                    resultConfirmDialog = new PayResultConfirmDialog(ICCardSwipeActivity.this, type, new NoneValueOperationListener() {
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
        return R.layout.activity_ic_card_swipe;
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
        promptHandler.removeMessages(0x03,TYPE_CARD_FAIL);

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
        KLog.v("initializedData-"+SwishCardApplication.getInstance().preferenceUtils.getMAC());
        Intent intent = getIntent();
        serial = intent.getBundleExtra("bundle").getString("serial");
        icCardSwipePresenter = new ICCardSwipePresenter(this, promptHandler);

    }

    @Override
    public void netWorkRequests() {
        tv_msg.setText("正在通信");
        timer(TYPE_CARD_CANCEL);
        icCardSwipePresenter.startSwish();
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
        Boolean flag = IsNetWorkUtils.isNetWorkCheck(ICCardSwipeActivity.this);
        if (flag == true) {
            timer(TYPE_CARD_FAIL);
            icCardSwipePresenter.cupPayment(info, value, serial);
        } else {
            toast("请先打开网络");
        }
    }


    /**
     * 初始化计时器
     **/
    private void timer(int type) {
        int limit = TIMEOUT;
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(limit + 1)
                .map(s -> limit - s.intValue())
                .take(limit + 1)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> complete(type))
                .subscribe(s -> next( type,s));

    }

    /**
     * 计时器倒数完成
     * @param type
     */
    private void complete(int type) {
        switch (type){
            case TYPE_CARD_CANCEL:
                /*exit = true;
                tv_msg.setText("刷卡超时,请退出重试");*/
                finish();
                break;
            case TYPE_CARD_FAIL:
                if(resultConfirmDialog !=null) {
                    resultConfirmDialog.hide();
                }
                icCardSwipePresenter.dismissLoadingDialog();
                finish();
                break;
        }

    }

    /**
     * 倒数计时
     * @param type
     * @param s
     */
    private void next(int type, int s) {
        switch (type){
            case TYPE_CARD_CANCEL:
                if (s >= 10) {
                    tv_msg.setText(s + "S");
                } else {
                    tv_msg.setText("0" + s + "S");
                }
                break;
            case TYPE_CARD_FAIL:

                break;
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
