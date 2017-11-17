package com.qhw.swishcardapp.bean;

import android.util.Log;

import com.qhw.swishcardapp.utils.Utils;
import com.socks.library.KLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author xyl 2017/11/09
 */
public class Pack8583Icc {
    public Map<Integer, String> map;
    private String header;
    private String messageType;
    private int[] bit1 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit2 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit3 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit4 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit5 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit6 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit7 = {0, 0, 0, 0, 0, 0, 0, 0};
    private int[] bit8 = {0, 0, 0, 0, 0, 0, 0, 0};


    public Pack8583Icc() {
        map = new HashMap<Integer, String>();
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setBody(int key, String value) {
        int len = value.length();
        switch (key) {
            case 2:
                String result2 = len + value;
                if (isOddNumber(result2) == true) {
                    result2 += "0";
                }
                map.put(key, result2);
                break;
            case 3:
                String result3 = value;
                if (isOddNumber(result3) == true) {
                    result3 += "0";
                }
                map.put(key, result3);
                break;
            case 4:
                double d = Double.valueOf(value) / 1000000000;
                String a = String.format("%.11f", d);
                String r = a.replaceAll("\\.", "");
                map.put(key, r);
                break;
            case 11:
                String result11 = value;
                if (isOddNumber(result11) == true) {
                    result11 += "0";
                }
                map.put(key, result11);
                break;
            case 22:
                String result22 = value;
                if (isOddNumber(result22) == true) {
                    result22 += "0";
                }
                map.put(key, result22);
                break;
            case 25:
                String result25 = value;
                if (isOddNumber(result25) == true) {
                    result25 += "0";
                }
                map.put(key, result25);
                break;
            case 26:
                String result26 = value;
                if (isOddNumber(result26) == true) {
                    result26 += "0";
                }
                map.put(key, result26);
                break;
            case 35:
                String result35 = value;
                int lenth35 = value.length();
                if (isOddNumber(result35) == true) {
                    result35 += "0";
                }
                map.put(key, lenth35 + result35);
                break;
            case 41:// 终端代码
                map.put(key, Utils.convertStringToHex(value));
                break;
            case 42:// 商户代码
                map.put(key, Utils.convertStringToHex(value));
                break;
            case 49:
                map.put(key, Utils.convertStringToHex(value));
                break;
            case 52:
                map.put(key, value);
                break;
            case 53:
                String result53 = value;
                if (isOddNumber(result53) == true) {
                    result53 += "0";
                }
                map.put(key, result53);
                break;
            case 55:

               String result55 = value;
               //String start55 = getPackLenth(value.length());
                if (isOddNumber(result55) == true) {
                    result55 += "0";
                }
                map.put(key,  result55);
                break;
            case 60:// 自定义域
                String result60 = value;
                String start60 = getPackLenth(value.length());
                if (isOddNumber(result60) == true) {
                    result60 += "0";
                }
                map.put(key, start60 + result60);
                break;
            case 63:
                String start63 = getPackLenth(value.length());
                String result63 = Utils.convertStringToHex(value);
                map.put(key, start63 + result63);
                break;
            case 64:// 自定义域
                map.put(key, value);
                break;
        }
    }

    private boolean isOddNumber(String value) {
        if (value.length() % 2 == 0) {
            return false;
        }
        return true;
    }

    /**
     * 变长计算长度
     *
     * @param lenth
     * @return
     */
    private String getPackLenth(int lenth) {
        String start = "";
        if (lenth < 10) {
            start = "000";
        } else if (lenth >= 10 && lenth < 100) {
            start = "00";
        } else if (lenth >= 100 && lenth < 1000) {
            start = "0";
        } else if (lenth >= 1000) {
            start = "";
        }
        String result = start + lenth;
        return result;
    }

    /**
     * 计算位图************************************************************
     *
     * @return
     */
    public String getBitmap() {

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            countBitmap((Integer) key);
        }
        return getResult();
    }

    private String getResult() {
        int[][] arrys = {bit1, bit2, bit3, bit4, bit5, bit6, bit7, bit8};
        StringBuffer sb = new StringBuffer();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < arrys.length; i++) {
            for (int j = 0; j < arrys[i].length; j++) {
                sb.append(arrys[i][j]);
            }
            result.append(Utils.binaryString2hexString(sb.toString()));
            sb.setLength(0);
        }
        return result.toString();
    }

    private void countBitmap(int key) {
        if (key >= 1 && key <= 8) {
            countBitmapInit(1, key, 1);
        } else if (key >= 9 && key <= 16) {
            countBitmapInit(9, key, 2);
        } else if (key >= 17 && key <= 24) {
            countBitmapInit(17, key, 3);
        } else if (key >= 25 && key <= 32) {
            countBitmapInit(25, key, 4);
        } else if (key >= 33 && key <= 40) {
            countBitmapInit(33, key, 5);
        } else if (key >= 41 && key <= 48) {
            countBitmapInit(41, key, 6);
        } else if (key >= 49 && key <= 56) {
            countBitmapInit(49, key, 7);
        } else if (key >= 57 && key <= 64) {
            countBitmapInit(57, key, 8);
        }
    }

    private void countBitmapInit(int start, int key, int index) {
        for (int i = start; i < start + 8; i++) {
            if (key == i) {
                switch (index) {
                    case 1:
                        bit1[i - start] = 1;
                        break;
                    case 2:
                        bit2[i - start] = 1;
                        break;
                    case 3:
                        bit3[i - start] = 1;
                        break;
                    case 4:
                        bit4[i - start] = 1;
                        break;
                    case 5:
                        bit5[i - start] = 1;
                        break;
                    case 6:
                        bit6[i - start] = 1;
                        break;
                    case 7:
                        bit7[i - start] = 1;
                        break;
                    case 8:
                        bit8[i - start] = 1;
                        break;
                }
            }
        }
    }

    /**************************** 以上是位图算法 *********************************/
    /**
     * 输出报文
     */
    public String pack() {
        StringBuffer stringBuffer = new StringBuffer();// 报文体
        Object[] key = map.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            Log.e("第" + key[i], String.valueOf(map.get(key[i])));
            stringBuffer.append(String.valueOf(map.get(key[i])));
        }
        String packs = header + messageType + getBitmap()
                + stringBuffer.toString();
        int len = packs.length() / 2;
        KLog.v("pack--"+ Integer.toHexString(len));
        String lenth = "0" + Integer.toHexString(len);
        String result = lenth + header + messageType + getBitmap()
                + stringBuffer.toString();
        KLog.v("result"+result);
        return  result;
    }

    /**
     * 输出除了64域的内容体
     *
     * @return
     */
    public String packBody() {
        StringBuffer stringBuffer = new StringBuffer();// 报文体
        //stringBuffer.append(messageType);
        Object[] key = map.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            if ((Integer) key[i] != 64) {
                stringBuffer.append(String.valueOf(map.get(key[i])));
            }
        }
        String result = messageType + getBitmap() + stringBuffer.toString();
        return result;
    }
}
