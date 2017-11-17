package com.qhw.swishcardapp;

/**
 * Created by Hello_world on 2017/9/3.
 */

public class AppConstants {

    public static final String DATA_8583 = "0000000000000000";
    public static final String HEAD_8583 = "6000060000603000000000";
    public static final String SIGN_MESSAGE_TYPE_8583 = "0800";//签到消息类型
    public static final String CONSUME_MESSAGE_TYPE_8583 = "0200";//消费消息类型
    public static final String URL_IP = "http://qr.china-ndl.com/mmcp/";


    public static final String URL_GETKEY = "index.php?g=Api&m=Trade&a=getKey";//获取密钥
    public static final String URL_ADDBN = "index.php?g=Api&m=Trade&a=addTrade";//添加流水
    public static final String URL_SEARCHBN = "index.php?g=Api&m=Trade&a=getTrade";//获取流水
    public static final String URL_GETPC = "index.php?g=Api&m=Trade&a=sign";//获取批次号

    public static final String URL_QRPAY = "index.php?g=Api&m=Pays&a=qrpay";//二维码支付
    public static final String URL_ORDER_S = "index.php?g=Api&m=Pays&a=orderquery";//订单查询
    public static final String URL_AUPAY = "index.php?g=Api&m=Pays&a=aupay";//扫码支付
    public static final String URL_AUQUE = "index.php?g=Api&m=Pays&a=auque";//扫码查询支付结果

    public static final String KEY = "YxeFA92tRSBjcwuqaWngQmTcUakRk5XyhdJfeKsgaum8Eb9m9TdYqV9Ihk5nvsKG";//keymmcp
    public static final String AID = "170110435801";//aid
    //获取应用列表(旧)
    public final static String URL_GET_YINGYONG_LIST = "http://wdt.qianhaiwei.com/cyy/index.php?g=Api&m=AppManage&a=ListShopApps";
    public  static boolean ENABLE_SCAN_PRINT = false;
}
