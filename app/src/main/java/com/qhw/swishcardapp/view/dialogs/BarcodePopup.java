package com.qhw.swishcardapp.view.dialogs;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.pos.api.Scan;
import com.qhw.swishcardapp.R;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.view.activitys.PayDimensionalCodeActivity;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Hello_world on 2017/2/27.
 */

public class BarcodePopup extends PopupWindow {
    private PayDimensionalCodeActivity activity;
    private ImageView iv_barcode;
    private WindowManager.LayoutParams lp;
    private Scan scan;
    private static final int RECORD_PROMPT_MSG = 0x06;
    private static final int TIMER_START = 0x00;
    private static final int TIMER_STOP = 0x01;
    private static final int TIMER_OUT = 0x02;
    private boolean timeout_flag = false;
    private int count = 30;
    private Timer timer;
    private TimerTask task;
    private Handler promptHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RECORD_PROMPT_MSG:
                    Bundle b = msg.getData();
                    final String code = b.getString("MSG").trim();
                    buildBarcode("283148344748374837");
                    promptHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.auCodeCallBack(code);
                        }
                    }, 1000);
                    break;
                case TIMER_START:
                    timer = new Timer();
                    timeout_flag = false;
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            count--;
                            if (count == 0) {
                                Message message = new Message();
                                message.what = TIMER_OUT;
                                promptHandler.sendMessage(message);
                            }
                        }
                    };
                    timer.schedule(task, 200, 2000);
                    break;
                case TIMER_STOP:
                    lp.alpha = 1f;
                    activity.getWindow().setAttributes(lp);
                    cancleTimer();
                    break;
                case TIMER_OUT:
                    timeout_flag = true;
                    activity.payResultInfoShow("2", "faild");
                    break;
                default:
                    break;
            }
        }
    };

    public void startTimer() {
        Message msg = new Message();
        msg.what = TIMER_START;

        promptHandler.sendMessage(msg);
    }

    public void stopTimer() {
        Message msg = new Message();
        msg.what = TIMER_STOP;

        promptHandler.sendMessage(msg);
    }


    public BarcodePopup(PayDimensionalCodeActivity activity) {
        this.activity = activity;
        scan = SwishCardApplication.getInstance().getD2000Scan();
        init(activity);
        new Thread() {
            public void run() {
                int iRet = -1;
                String data[] = new String[100];
                iRet = scan.DLL_ScanOpen();
                startTimer();
                while (true) {
                    iRet = scan.DLL_ScanRead(data);
                    if (iRet > 0) break;
                    if (timeout_flag) break;
                }
                if (iRet > 0) {
                    SendPromptMsg(data[0]);
                }
                scan.DLL_ScanClose();
                stopTimer();
            }
        }.start();
    }


    public void SendPromptMsg(String strInfo) {
        Message msg = new Message();
        msg.what = RECORD_PROMPT_MSG;
        Bundle b = new Bundle();
        b.putString("MSG", strInfo);
        msg.setData(b);
        promptHandler.sendMessage(msg);
    }

    private void init(PayDimensionalCodeActivity context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_barcode, null);
        iv_barcode = (ImageView) contentView.findViewById(R.id.iv_barcode);
        this.setContentView(contentView);
        setWidth(400);
        setHeight(250);

        this.setFocusable(false);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        lp = context.getWindow().getAttributes();
        lp.alpha = 0.7f;
        context.getWindow().setAttributes(lp);
    }


    public void showPopupWindow() {
        if (!this.isShowing()) {
            showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    private void buildBarcode(String barcode) {
        for (int i = 0; i < barcode.length(); i++) {
            int c = barcode.charAt(i);
            if ((19968 <= c && c < 40623)) {
                Toast.makeText(activity, "条码中不能有中文", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        Bitmap bmp = null;
        try {
            if (barcode != null && !"".equals(barcode)) {
                bmp = CreateOneDCode(barcode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp != null) {
            iv_barcode.setImageBitmap(bmp);
        }
    }

    public Bitmap CreateOneDCode(String content) throws Exception {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 600, 150);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public void cancleTimer() {

        timeout_flag = true;

        if (timer != null) {
            timer.cancel();
        }

        if (task != null) {
            task.cancel();
        }
        BarcodePopup.this.dismiss();
    }


}
