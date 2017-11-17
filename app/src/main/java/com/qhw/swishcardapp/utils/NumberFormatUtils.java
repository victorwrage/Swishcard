package com.qhw.swishcardapp.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hello_world on 2017/9/3.
 */

public class NumberFormatUtils {


    /**
     * 数字计算
     *
     * @param paramChar
     */
    public static void textBtn(Context mContext, char paramChar, TextView tv_money) {
        StringBuffer localStringBuffer = new StringBuffer();
        if (tv_money.getText().toString().length() < 15) {
            if ((paramChar >= '0') && (paramChar <= '9')) {
                localStringBuffer.append(tv_money.getText().toString());
                localStringBuffer.append(paramChar);
                String rateStr = localStringBuffer.toString();
                if (rateStr.indexOf(".") != -1) {
                    // 获取小数点的位置
                    int num = 0;
                    num = rateStr.indexOf(".");

                    // 获取小数点后面的数字 是否有两位 不足两位补足两位
                    String dianAfter = rateStr.substring(0, num + 1);
                    String afterData = rateStr.replace(dianAfter, "");
                    if (afterData.length() < 2) {
                        tv_money.setText(tv_money.getText()
                                .toString() + paramChar);
                        return;
                    } else {
                        afterData = afterData;
                    }
                    tv_money.setText(rateStr.substring(0, num) + "."
                            + afterData.substring(0, 2));
                    return;
                } else {
                    tv_money.setText(tv_money.getText().toString()
                            + paramChar);
                    return;

                }
            } else {
                tv_money.setText(tv_money.getText().toString()
                        + paramChar);
                return;
            }

        } else {
            Toast.makeText(mContext, "您输入的金额过长!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
