package com.qhw.swishcardapp.utils;


import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.SwishCardApplication;

/**
 * Created by Hello_world on 2017/7/21.
 */

public class SignUtils {


    public static String[] createKey(String workKeys) {
        String[] keys = new String[3];

        int lenth = workKeys.length() / 3;
        String PinInit = workKeys.substring(0, lenth);
        String MacInit = workKeys.substring(lenth, lenth * 2);
        String TdkInit = workKeys.substring(lenth * 2, lenth * 3);
        String checkSumPin = PinInit.substring(PinInit.length() - 8,
                PinInit.length());
        String PinKeyInit = PinInit.substring(0, PinInit.length() - 8);
        String pinKey = checkSumFrom3Des(PinKeyInit, checkSumPin);// 校验PIN返回值
        if (!pinKey.equals("")) {
            keys[0] = pinKey;
        } else {
            keys[0] = "";
        }

        String checkSumMac = MacInit.substring(PinInit.length() - 8,
                PinInit.length());
        String MacKeyInit = MacInit.substring(0, PinInit.length() - 8);
        String macKey = checkSumFromDes(MacKeyInit, checkSumMac);// 校验MAC返回值
        if (!macKey.equals("")) {
            keys[1] = macKey;
        } else {
            keys[1] = "";
        }

        String checkSumTdk = TdkInit.substring(PinInit.length() - 8,
                PinInit.length());
        String TdkKeyInit = TdkInit.substring(0, PinInit.length() - 8);
        String tdkKey = checkSumFrom3Des(TdkKeyInit, checkSumTdk);// 校验TDK返回值
        if (!tdkKey.equals("")) {
            keys[2] = tdkKey;
        } else {
            keys[2] = "";
        }
        return keys;
    }

    /**
     * 用3Des加密校验PIN和TDK
     *
     * @param PinKeyInit
     * @param checkSumPin
     * @return
     */
    public static String checkSumFrom3Des(String PinKeyInit, String checkSumPin) {
        String decryption = JCEHandler.decryptData(PinKeyInit,
                SwishCardApplication.getInstance().preferenceUtils.getKey());
        String encrypt = JCEHandler.encryptData(AppConstants.DATA_8583, decryption);
        String subEncrypt = encrypt.substring(0, 8);
        if (subEncrypt.equals(checkSumPin)) {
            return decryption;
        }
        return "";
    }

    /**
     * 用Des加密校验Mac
     *
     * @param PinKeyInit
     * @param checkSumPin
     * @return
     */
    public static String checkSumFromDes(String PinKeyInit, String checkSumPin) {
        String decryption = JCEHandler.decryptData(PinKeyInit,
                SwishCardApplication.getInstance().preferenceUtils.getKey());
        byte[] bDecryption = Utils.str2Bcd(decryption);// 密钥
        byte[] bData = Utils.str2Bcd(AppConstants.DATA_8583);
        byte[] bCheck = DesUtils.encrypt(bData, bDecryption);
        String encrypt = Utils.bcd2Str(bCheck);
        String subEncrypt = encrypt.substring(0, 8);
        if (subEncrypt.equals(checkSumPin)) {
            return decryption;
        }
        return "";
    }
}
