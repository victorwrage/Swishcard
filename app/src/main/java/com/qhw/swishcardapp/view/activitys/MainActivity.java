package com.qhw.swishcardapp.view.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.presenter.impl.MainPresenter;
import com.qhw.swishcardapp.utils.NumberFormatUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_menu)
    ImageView iv_menu;

    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.ll_0)
    LinearLayout ll_0;
    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.ll_4)
    LinearLayout ll_4;
    @BindView(R.id.ll_5)
    LinearLayout ll_5;
    @BindView(R.id.ll_6)
    LinearLayout ll_6;
    @BindView(R.id.ll_7)
    LinearLayout ll_7;
    @BindView(R.id.ll_8)
    LinearLayout ll_8;
    @BindView(R.id.ll_9)
    LinearLayout ll_9;
    @BindView(R.id.ll_dian)
    LinearLayout ll_dian;
    @BindView(R.id.ll_clear)
    LinearLayout ll_clear;
    @BindView(R.id.ll_swish)
    LinearLayout ll_swish;
    @BindView(R.id.ll_wx)
    LinearLayout ll_wx;
    @BindView(R.id.ll_zfb)
    LinearLayout ll_zfb;
    @BindView(R.id.ll_qq)
    LinearLayout ll_qq;
    private String type = "";
    private String money = "";
    private String payType = "";
    private MainPresenter presenter;

    @Override
    public int activityContentView() {
        return R.layout.activity_main;
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
    }

    @Override
    public void activityOnPause() {

    }

    @Override
    public void activityOnStop() {

    }



    @Override
    public void initializedData() {
        presenter = new MainPresenter(this);
        getAppData();//获取外接app传入数值
        determineOpenType();//判断app打开类型
        initScannerPrinter();
    }

    /**
     * 判断是否为D2000
     */
    private void initScannerPrinter() {
        String model = android.os.Build.MODEL;
        if (model.equals("D2000")) {
            AppConstants.ENABLE_SCAN_PRINT = true;
        }
    }
    /**
     * 获取别的app传递过来的参数
     */
    private void getAppData() {
        Intent intent = getIntent();
        if (intent.getFlags() == 101) {
            Bundle bundle = intent.getBundleExtra("bundle");
            type = bundle.getString("type").trim();
            money = bundle.getString("money").trim();
            payType = bundle.getString("payType").trim();
        }
    }


    /**
     * 处理别的app传递获取的参数
     */
    private void determineOpenType() {
        if (type.equals("")) {
   //         tv_money.setText("");
            SwishCardApplication.getInstance().preferenceUtils.setIsThirdPartyAccess("0");
        } else {
            SwishCardApplication.getInstance().preferenceUtils.setIsThirdPartyAccess("1");
            tv_money.setText(money);
            closeBtn();
            if (type.equals("0")) {//刷卡
                presenter.CardPay(money);
            } else if (type.equals("1")) {//移动支付
                presenter.mobilePay(PayDimensionalCodeActivity.class, payType, money);
            }
        }
    }

    @Override
    public void netWorkRequests() {
        presenter.checkingUpdate();
    }

    @OnClick({R.id.iv_menu, R.id.ll_0, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.ll_7, R.id.ll_8
            , R.id.ll_9, R.id.ll_dian, R.id.ll_clear, R.id.ll_swish, R.id.ll_wx, R.id.ll_zfb, R.id.ll_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                openActivity(EnvironmentConfigurationActivity.class, null, -1);
                break;
            case R.id.ll_0:
                NumberFormatUtils.textBtn(this, '0', tv_money);
                break;
            case R.id.ll_1:
                NumberFormatUtils.textBtn(this, '1', tv_money);
                break;
            case R.id.ll_2:
                NumberFormatUtils.textBtn(this, '2', tv_money);
                break;
            case R.id.ll_3:
                NumberFormatUtils.textBtn(this, '3', tv_money);
                break;
            case R.id.ll_4:
                NumberFormatUtils.textBtn(this, '4', tv_money);
                break;
            case R.id.ll_5:
                NumberFormatUtils.textBtn(this, '5', tv_money);
                break;
            case R.id.ll_6:
                NumberFormatUtils.textBtn(this, '6', tv_money);
                break;
            case R.id.ll_7:
                NumberFormatUtils.textBtn(this, '7', tv_money);
                break;
            case R.id.ll_8:
                NumberFormatUtils.textBtn(this, '8', tv_money);
                break;
            case R.id.ll_9:
                NumberFormatUtils.textBtn(this, '9', tv_money);
                break;
            case R.id.ll_dian:
                NumberFormatUtils.textBtn(this, '.', tv_money);
                break;
            case R.id.ll_clear:
                tv_money.setText("");
                break;
            case R.id.ll_swish:
                presenter.CardPay(tv_money.getText().toString().trim());
                break;
            case R.id.ll_wx:
                presenter.mobilePay(PayDimensionalCodeActivity.class, "0", tv_money.getText().toString().trim());
                break;
            case R.id.ll_zfb:
                presenter.mobilePay(PayDimensionalCodeActivity.class, "1", tv_money.getText().toString().trim());
                break;
            case R.id.ll_qq:
                presenter.mobilePay(PayDimensionalCodeActivity.class, "2", tv_money.getText().toString().trim());
                break;
        }
    }


    /**
     * 关闭按钮
     */
    private void closeBtn() {
        ll_7.setEnabled(false);
        ll_8.setEnabled(false);
        ll_9.setEnabled(false);
        ll_4.setEnabled(false);
        ll_5.setEnabled(false);
        ll_6.setEnabled(false);
        ll_1.setEnabled(false);
        ll_2.setEnabled(false);
        ll_3.setEnabled(false);
        ll_0.setEnabled(false);
        ll_dian.setEnabled(false);
        ll_clear.setEnabled(false);
    }


}
