package com.qhw.swishcardapp.view.activitys;


import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;

import butterknife.BindView;
import butterknife.OnClick;

public class EnvironmentConfigurationActivity extends BaseActivity {

    @BindView(R.id.ll_stock_mobile_pay)
    LinearLayout ll_stock_mobile_pay;

    @BindView(R.id.ll_stock_bank_pay)
    LinearLayout ll_stock_bank_pay;
    @BindView(R.id.ll_print_pay)
    LinearLayout ll_print_pay;

    @Override
    public int activityContentView() {
        return R.layout.activity_environment_configuration;
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

    }

    @Override
    public void netWorkRequests() {

    }

    @OnClick({R.id.ll_stock_mobile_pay, R.id.ll_stock_bank_pay,R.id.ll_print_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_stock_mobile_pay:
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("需要验证您的身份")
                        .input("请输入密码", "", false, (dialog, input) -> {

                        })
                        .onPositive((dialog, which) -> {
                            if (dialog.getInputEditText().getText().toString().trim().equals("zdv666")) {
                                openActivity(MobilePaymentConfigurationActivity.class, null, -1);
                            } else {
                                toast("密码错误");
                            }
                        })
                        .autoDismiss(true)
                        .show();

                break;
            case R.id.ll_stock_bank_pay:
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("需要验证您的身份")
                        .input("请输入密码", "", false, (dialog, input) -> {

                        })
                        .onPositive((dialog, which) -> {
                            if (dialog.getInputEditText().getText().toString().trim().equals("zdv666")) {
                                openActivity(MobilePaymentConfigurationActivity.class, null, -1);
                            } else {
                                toast("密码错误");
                            }
                        })
                        .autoDismiss(true)
                        .show();

                break;
            case R.id.ll_print_pay:
                openActivity(ScanAndPrintActivity.class, null, 0);
                break;
        }
    }
}
