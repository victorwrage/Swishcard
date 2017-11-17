package com.qhw.swishcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.qhw.swishcardapp.utils.ToastController;

import butterknife.ButterKnife;

/**
 * Created by Hello_world on 2017/9/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private BuildBean buildBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityContentView());
        ButterKnife.bind(this);
        initializedData();
        netWorkRequests();
        //判断是否有第三方app接入
        if (SwishCardApplication.getInstance().preferenceUtils.getIsThirdPartyAccess().equals("1")) {
            SwishCardApplication.getInstance().addActivity(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityOnStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        activityOnRestart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        activityonOnResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityOnPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        activityOnStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityOnDestroy();
    }

    public abstract int activityContentView(

    );

    public abstract void activityOnStart(

    );

    public abstract void activityOnRestart(

    );

    public abstract void activityonOnResume(

    );

    public abstract void activityOnPause(

    );

    public abstract void activityOnStop(

    );

    public abstract void activityOnDestroy(

    );

    public abstract void initializedData(

    );

    public abstract void netWorkRequests(

    );


    public void initLoadingDialog(String message) {
        buildBean = DialogUIUtils.showMdLoading(this, message,
                true, false, false, true);
    }

    public void showLoadingDialog() {
        buildBean.show();
    }

    public void dismissLoadingDialog() {
        DialogUIUtils.dismiss(buildBean);
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ToastController.show(toast, 1500);
    }


    public void openActivity(Class<?> pls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(this, pls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (code != -1) {
            startActivityForResult(intent, code);
        } else {
            startActivity(intent);
        }

    }
}
