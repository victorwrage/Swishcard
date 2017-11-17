package com.qhw.swishcardapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.qhw.swishcardapp.utils.ToastController;

/**
 * Created by Hello_world on 2017/9/3.
 */

public class BasePresenter {

    private BuildBean buildBean;

    public void initLoadingDialog(Context mContext, String message) {
        buildBean = DialogUIUtils.showMdLoading(mContext, message,
                true, false, false, true);
    }

    public void showLoadingDialog() {
        buildBean.show();
    }

    public void dismissLoadingDialog() {
        DialogUIUtils.dismiss(buildBean);
    }

    public void toast(Context mContext, String message) {
        Toast toast = Toast.makeText(mContext, message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ToastController.show(toast, 1500);
    }

    public void openActivity(Activity activity, Class<?> pls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, pls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (code != -1) {
            activity.startActivityForResult(intent, code);
        } else {
            activity.startActivity(intent);
        }

    }

}
