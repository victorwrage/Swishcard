package com.qhw.swishcardapp.utils;

import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hello_world on 2017/5/17.
 */

/**
 * 自定义Toast显示时间
 */
public class ToastController {
    public static void show(final Toast toast,final int cnt){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        },cnt);
    }
}
