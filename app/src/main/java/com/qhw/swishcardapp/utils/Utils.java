package com.qhw.swishcardapp.utils;


import com.socks.library.KLog;

public class Utils {
    /**
     * 十六进制字符串转化为字节数组
     */
    public static String bcd2Str(byte[] b) {
        char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(b.length * 2);

        for (int i = 0; i < b.length; ++i) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }

        return sb.toString();
    }

    /**
     * 字节数组转化为十六进制字符串
     *
     * @param asc
     * @return
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte[] abt = new byte[len];
        if (len >= 2) {
            len /= 2;
        }

        byte[] bbt = new byte[len];
        abt = asc.getBytes();

        for (int p = 0; p < asc.length() / 2; ++p) {
            int j;
            if (abt[2 * p] >= 97 && abt[2 * p] <= 122) {
                j = abt[2 * p] - 97 + 10;
            } else if (abt[2 * p] >= 65 && abt[2 * p] <= 90) {
                j = abt[2 * p] - 65 + 10;
            } else {
                j = abt[2 * p] - 48;
            }

            int k;
            if (abt[2 * p + 1] >= 97 && abt[2 * p + 1] <= 122) {
                k = abt[2 * p + 1] - 97 + 10;
            } else if (abt[2 * p + 1] >= 65 && abt[2 * p + 1] <= 90) {
                k = abt[2 * p + 1] - 65 + 10;
            } else {
                k = abt[2 * p + 1] - 48;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 二磁道末尾加零
     */
    public static String mag2data(String[] cardArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (cardArr[1].length() < 16) {
            for (int s = 0; s < 16; s++) {
                if (s >= cardArr[1].length()) {
                    stringBuffer.append('0');
                } else {
                    stringBuffer.append(cardArr[1].charAt(s));
                }
            }
            KLog.v("mag2data--" + cardArr[0] + "=" + stringBuffer);
            return cardArr[0] + "=" + stringBuffer;
        }
//        KLog.v("mag2data--" + cardArr[0] + "=" + cardArr[1]);
        return cardArr[0] + "=" + cardArr[1];
    }

    /**
     * ASCII码转为16进制
     *
     * @param str
     * @return
     */
    public static String convertStringToHex(String str) {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }

    /**
     * 16进制转为ASCII码
     *
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    /**
     * 二进制转16进制
     *
     * @param bString
     * @return
     */
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     * 16进制转二进制
     *
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(
                    hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
}
