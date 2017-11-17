package com.qhw.swishcardapp.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.model.callback.NoneValueOperationListener;


/**
 * Created by Hello_world on 2016/12/7.
 */

public class PayResultConfirmDialog extends Dialog implements View.OnClickListener {

    private TextView tv_update;
    private LinearLayout ll_value;
    private TextView tv_state;
    private View view;
    private Context context;
    private String result;
    private NoneValueOperationListener noneValueOperationListener;

    public PayResultConfirmDialog(Context context, String result, NoneValueOperationListener noneValueOperationListener) {
        super(context);
        this.context = context;
        this.result = result;
        this.noneValueOperationListener = noneValueOperationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payresultconfirm);
        init();
    }

    private void init() {
        ll_value = (LinearLayout) findViewById(R.id.ll_dialog_value);
        tv_update = (TextView) findViewById(R.id.tv_dialog_update);
        tv_state = (TextView) findViewById(R.id.tv_paystate);
        view = (View) findViewById(R.id.view_state);
        tv_update.setOnClickListener(this);

        if (result.equals("0")) {
            tv_state.setTextColor(Color.parseColor("#E81212"));
            tv_state.setText("支付失败");
            view.setBackgroundColor(Color.parseColor("#E81212"));
            tv_update.setBackgroundColor(Color.parseColor("#E81212"));
        } else if (result.equals("2")) {
            tv_state.setTextColor(Color.parseColor("#E81212"));
            tv_state.setText("支付超时");
            view.setBackgroundColor(Color.parseColor("#E81212"));
            tv_update.setBackgroundColor(Color.parseColor("#E81212"));
        }
        this.setCancelable(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_update:
                dismiss();
                noneValueOperationListener.responseEventCallBack();
                break;
        }
    }


}
