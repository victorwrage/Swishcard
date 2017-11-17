package com.qhw.swishcardapp.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.presenter.impl.CupCardPaymentSelectionPresenter;
import com.qhw.swishcardapp.view.activitys.ICCardSwipeActivity;
import com.qhw.swishcardapp.view.activitys.MagneticStripeCardSwipeActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Hello_world on 2017/7/8.
 */

public class BottomDialog implements View.OnClickListener {

    private Dialog mCameraDialog;
    private Context context;
    private EditText et_pass;
    private Button btn_comdit;
    private Timer timer;
    private int type;

    public BottomDialog(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        mCameraDialog = new Dialog(context, R.style.my_dialog);
        View root = LayoutInflater.from(context).inflate(
                R.layout.dialog_pass, null);

        et_pass = (EditText) root.findViewById(R.id.et_pass);
        btn_comdit = (Button) root.findViewById(R.id.btn_comdit);
        btn_comdit.setOnClickListener(this);

        Activity activity = (Activity) context;
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager windowManager = activity.getWindowManager();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (WindowManager.LayoutParams.MATCH_PARENT); // 宽度
        root.measure(0, 0);
        lp.height = windowManager.getDefaultDisplay().getHeight() / 2;
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.setCanceledOnTouchOutside(true);

        timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) et_pass.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(et_pass, 0);
                               timer.cancel();
                           }
                       },
                500);
    }

    public void show(int type) {
        this.type = type;
        mCameraDialog.show();
    }

    public void dismiss() {
        mCameraDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_comdit:
                String pass = et_pass.getText().toString().trim();
                if (!pass.equals("")) {
                    if (pass.length() == 6) {
                        switch (type){
                            case CupCardPaymentSelectionPresenter.TYPE_MAGNETIC:
                                MagneticStripeCardSwipeActivity activity = (MagneticStripeCardSwipeActivity) context;
                                activity.responseEventCallBack(pass);
                                break;
                            case CupCardPaymentSelectionPresenter.TYPE_ICC:
                                ICCardSwipeActivity activity2 = (ICCardSwipeActivity) context;
                                activity2.responseEventCallBack(pass);
                                break;
                        }

                        dismiss();
                    } else {
                        Toast.makeText(context, "密码长度必须为6", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
