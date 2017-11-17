package com.qhw.swishcardapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Hello_world on 2016/12/12.
 */

public class IsNetWorkUtils {
    public static Boolean isNetWorkCheck(Activity activity) {
        Boolean success = false;
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (NetworkInfo.State.CONNECTED == state) {
            success = true;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (NetworkInfo.State.CONNECTED == state) {
            success = true;
        }
        return success;
    }

}
