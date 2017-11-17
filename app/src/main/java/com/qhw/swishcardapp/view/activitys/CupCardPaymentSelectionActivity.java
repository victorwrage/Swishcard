package com.qhw.swishcardapp.view.activitys;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.qhw.swishcardapp.BaseActivity;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.presenter.impl.CupCardPaymentSelectionPresenter;
import com.qhw.swishcardapp.utils.IccUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CupCardPaymentSelectionActivity extends BaseActivity {

    @BindView(R.id.iv_track_card)
    ImageView iv_track_card;

    @BindView(R.id.iv_ic_card)
    ImageView iv_ic_card;

    @BindView(R.id.tv_money)
    TextView tv_money;


    private CupCardPaymentSelectionPresenter cupCardPaymentSelectionPresenter;

    @Override
    public int activityContentView() {
        return R.layout.activity_cup_card_payment_selection;
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
    }

    @Override
    public void initializedData() {
        cupCardPaymentSelectionPresenter = new CupCardPaymentSelectionPresenter(this);
        tv_money.setText("￥" + SwishCardApplication.getInstance().preferenceUtils.getMoney());
    }

    @Override
    public void netWorkRequests() {

    }

    @OnClick({R.id.iv_track_card, R.id.iv_ic_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_track_card:
                SwishCardApplication.getInstance().getD2000Mcr().DLL_McrOpen();
                cupCardPaymentSelectionPresenter.getSerialNumber(CupCardPaymentSelectionPresenter.TYPE_MAGNETIC);
                break;
            case R.id.iv_ic_card:
                IccUtils.getInstance().addAid();
                IccUtils.getInstance().addCAPK();

                cupCardPaymentSelectionPresenter.getSerialNumber(CupCardPaymentSelectionPresenter.TYPE_ICC);
                // toast("正在开发，敬请期待");
                break;
        }
    }

}
