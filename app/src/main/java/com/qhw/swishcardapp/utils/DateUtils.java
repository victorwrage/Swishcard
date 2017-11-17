package com.qhw.swishcardapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat sf = null;

    // 将字符串转为时间戳

    public static String getTime(String user_time) {
        String re_time = null;
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {

            d = sf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    // 将时间戳转为字符串
    public static String TimeStampDate(String time) {
        Long timestamp = Long.parseLong(time) * 1000;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(timestamp);
        return date;
    }

    /* 获取系统时间 格式为："yyyy/MM/dd " */
    public static String getCurrentDate() {
        Date date = new Date();
        sf = new SimpleDateFormat("HH:mm:ss");
        return sf.format(date);
    }

    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    public static String getDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 保存当前签到的时间
     *
     * @return
     */
    public static String getDateNoHour() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }


}
