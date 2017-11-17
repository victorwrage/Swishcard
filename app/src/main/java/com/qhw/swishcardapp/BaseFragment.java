package com.qhw.swishcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.qhw.swishcardapp.utils.ToastController;

/**
 * Created by Hello_world on 2017/9/3.
 */

public class BaseFragment extends Fragment {

    private BuildBean buildBean;

    public void showToast(String message) {
        Toast toast = Toast.makeText(this.getActivity().getApplicationContext(), message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ToastController.show(toast, 1500);
    }

    public void openActivity(Class<?> pClass, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), pClass);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (code != -1) {
            this.getActivity().startActivityForResult(intent, code);
        } else {
            this.getActivity().startActivity(intent);
        }

    }

    public void initLoadingDialog(String message) {
        buildBean = DialogUIUtils.showMdLoading(this.getActivity(), message,
                true, false, false, true);
    }

    public void showLoadingDialog() {
        buildBean.show();
    }

    public void dismissLoadingDialog() {
        DialogUIUtils.dismiss(buildBean);
    }


}
