package com.qhw.swishcardapp.view.activitys;


import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;

import butterknife.BindView;
import butterknife.OnClick;

public class MobilePaymentConfigurationActivity extends BaseActivity {

    @BindView(R.id.et_tenant)
    EditText et_tenant;
    @BindView(R.id.btn_setting)
    Button btn_setting;
    private final String EMPTY_TIPS = "商户编号不能为空";
    private final String LOADING_HINT = "设置中,请稍后";
    private final String SUCCESS_TIPS = "设置成功";


    @Override
    public int activityContentView() {
        return R.layout.activity_mobile_payment_configuration;
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

    @OnClick(R.id.btn_setting)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                final String merchant = et_tenant.getText().toString().trim();
                if (merchant.equals("")) {
                    toast(EMPTY_TIPS);
                } else {
                    initLoadingDialog(LOADING_HINT);
                    showLoadingDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            SwishCardApplication.getInstance().preferenceUtils.setMerchant_id(merchant);
                            toast(SUCCESS_TIPS);
                            finish();
                        }
                    }, 2000);
                }
                break;
        }
    }
}
