package com.qhw.swishcardapp.view.activitys;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.presenter.impl.CupPayMentConfigurationPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class CupPaymentConfigurationActivity extends BaseActivity {

    @BindView(R.id.et_termianl)
    EditText et_termianl;
    @BindView(R.id.et_tenant)
    EditText et_tenant;
    @BindView(R.id.btn_setting)
    Button btn_setting;
    private final String EMPTY_TERMIANL_TIPS = "终端号不能为空";
    private final String EMPTY_TENANT_TIPS = "商户号不能为空";
    private String termianl;
    private String tenant;
    private CupPayMentConfigurationPresenter payMentConfigurationPresenter;


    @Override
    public int activityContentView() {
        return R.layout.activity_cup_payment_configuration;
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
        payMentConfigurationPresenter = new CupPayMentConfigurationPresenter(this);
    }

    @Override
    public void netWorkRequests() {

    }

    @OnClick(R.id.btn_setting)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                termianl = et_termianl.getText().toString().trim();
                tenant = et_tenant.getText().toString().trim();
                if (termianl.equals("")) {
                    toast(EMPTY_TERMIANL_TIPS);
                } else if (tenant.equals("")) {
                    toast(EMPTY_TENANT_TIPS);
                } else {
                    payMentConfigurationPresenter.downLoadDecryptionKeys(tenant, termianl);
                }
                break;
        }
    }
}
