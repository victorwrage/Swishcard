package com.qhw.swishcardapp.utils;

/**
 * Created by Hello_world on 2017/7/21.
 */

public class McrUtils {

    /**
     * 磁道信息
     *
     * @param track2
     * @param trackKey
     * @return
     */
    public static String makeTrack(String track2, String trackKey) {
        String result = JCEHandler.encryptTrack2Data(track2, trackKey);
        return result;
    }

    /**
     * 芯片卡磁道信息
     *
     * @param track2
     * @param trackKey
     * @return
     */
    public static String makeTrack2(String track2, String trackKey) {
        String result = JCEHandler.encryptTrack2Data2(track2, trackKey);
        return result;
    }

    /**
     * PIN加密
     *
     * @return
     */
    public static String makePIN(String password, String card, String pinKey) {
        byte[] b = PINUtils.process(password, card);
        String result = JCEHandler.encryptData(Utils.bcd2Str(b), pinKey);
        return result;
    }

    /**
     * MAC加密
     *
     * @param macKey
     * @param posReport
     * @return
     */
    public static String makeMAC(String macKey, String posReport) {
        String result = macCheck(macKey, posReport);
        return result;
    }



    /**
     * @param macKey mac密钥
     * @param data   加密报文
     * @return
     */
    public static String macCheck(String macKey, String data) {
        byte[] k = Utils.str2Bcd(macKey);
        byte[] b = Utils.str2Bcd(data);
        String result = Utils.bcd2Str(MacEcbUtils.getMacs(k, b));
      //  String result = Utils.bcd2Str(MacEcbUtils.javaHLL(k, data));
        return result;
    }



}
