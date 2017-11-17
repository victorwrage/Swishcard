package com.qhw.swishcardapp.view.customs;

import com.pos.api.Printer;
import com.qhw.swishcardapp.SwishCardApplication;

import java.util.Map;

/**
 * Created by Hello_world on 2017/9/15.
 */

public class PrinterTools {

    public static void DLL_PrnInit(Map<String, String> map) {
        Printer printer = SwishCardApplication.getInstance().getD2000Print();
        printer.DLL_PrnInit();
        printer.DLL_PrnSetFont((byte) 24, (byte) 24, (byte) 0x00);
        printer.DLL_PrnStr("                       万店通刷" + "\n");
        printer.DLL_PrnStr("******************************************" + "\n");
        printer.DLL_PrnStr("商户号:" + map.get("name") + "\n");
        printer.DLL_PrnStr("交易类型:扫码支付" + "\n");
        printer.DLL_PrnStr("支付渠道:" + map.get("paytype") + "\n");
        printer.DLL_PrnStr("订单号:" + map.get("order_id") + "\n");
        printer.DLL_PrnStr("日期:" + map.get("time") + "\n");
        printer.DLL_PrnStr("金额:RMB " + map.get("money") + "\n");
        printer.DLL_PrnStr("备注:" + "\n");
        printer.DLL_PrnStr("您的24H商户贴身管家" + "\n");
        printer.DLL_PrnStr("客服热线: 8008208820" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("        客户签名:" + "\n");
        printer.DLL_PrnStr("******************************************" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStr("" + "\n");
        printer.DLL_PrnStart();
    }

}
