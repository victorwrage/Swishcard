package com.qhw.swishcardapp.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pos.api.Icc;
import com.pos.emvcore.EmvCore;
import com.pos.emvcore.beans.AidList;
import com.pos.emvcore.beans.AppList;
import com.pos.emvcore.beans.CAPK;
import com.pos.emvcore.beans.ExceptPan;
import com.pos.emvcore.beans.TermParam;
import com.qhw.swishcardapp.SwishCardApplication;
import com.qhw.swishcardapp.device.d2000v.ByteUtil;
import com.socks.library.KLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hello_world on 2017/7/21.
 */

public class IccUtils {
    public static final int CARD_READ_ERROR = 0;
    public static final int CARD_READ_OK = CARD_READ_ERROR + 1;
    private Timer mIccCheckCardTimer = null;
    private TimerTask mIccCheckCardTimerTask = null;

    static IccUtils instance;

    private EmvCore mEmvCore = null;

    private int mAuthAmt = 0;
    private int mCashBackAmt = 0;


    public static IccUtils getInstance() {
        synchronized (IccUtils.class) {
            if (instance == null) {
                instance = new IccUtils();
            }
            return instance;
        }
    }

    /**
     * 写入AID
     */
    public void addAid() {
        int iRet = -1;
        AidList app = null;
        app = new AidList();
        app.AppName = new byte[]{0x56, 0x49, 0x53, 0x41};
        app.AppNameLen = 4;

        app.AidLen = 7;
        app.AID = new byte[]{(byte) 0xA0, 0x00, 0x00, 0x03, 0x33, 0x01, 0x01};
        app.SelFlag = EmvCoreContants.PART_MATCH;
        app.Priority = 0;
        app.TargetPer = 0;
        app.MaxTargetPer = 0;
        app.FloorLimitCheck = 1;
        app.RandTransSel = 1;
        app.VelocityCheck = 1;
        app.FloorLimit = 2000;
        app.Threshold = 0;
        app.TACDenial = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
        app.TACOnline = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
        app.TACDefault = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
        app.AcquierId = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, 0x10};
        app.dDOL = new byte[]{0x03, (byte) 0x9F, 0x37, 0x04};
        app.dDOLLen = 4;
        app.tDOL = new byte[]{0x0F, (byte) 0x9F, 0x02, 0x06, 0x5F, 0x2A,
                0x02, (byte) 0x9A, 0x03, (byte) 0x9C, 0x01, (byte) 0x95, 0x05,
                (byte) 0x9F, 0x37, 0x04};
        app.tDOLLen = 16;
        app.Version = new byte[]{0x00, (byte) 0x96};
        app.RiskManData = new byte[]{0x01, 0x01};
        app.RiskManDataLen = 2;

        app.MerchName = new byte[]{0x53, 0x48, 0x4F, 0x50, 0x31};
        app.MerchNameLen = 5;
        app.MerchCateCode = new byte[]{0x12, 0x34};
        app.MerchId = new byte[]{0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
        app.TermId = new byte[]{0x46, 0x72, 0x6F, 0x6E, 0x74, 0x31, 0x32,
                0x33};
        app.TransCurrCode = new byte[]{0x01, 0x56};

        app.TransCurrExp = 2;
        app.ReferCurrExp = 2;
        app.ReferCurrCon = 1000;
        app.ReferCurrCode = new byte[]{0x01, 0x56};

        iRet = SwishCardApplication.getInstance().getEmvCore().EmvLibAddApp(app);
        KLog.e("addresult", iRet + "");

    }

    public void addCAPK() {
        int iRet = -1;
        AidList app = null;
        CAPK capk = null;
        ExceptPan pan = null;
        TermParam termParam = null;
        byte[] checksum = null;

        capk = new CAPK();
        capk.RID = Utils.str2Bcd("A000000333");
        capk.KeyID = (byte) 03;
        capk.HashInd = (byte) 1;
        capk.ArithInd = (byte) 1;
        capk.ModulLen = (byte) 176;
        capk.Modul = Utils
                .str2Bcd("B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33D");
        capk.ExponentLen = (byte) 1;
        capk.Exponent = Utils.str2Bcd("3");
        capk.ExpDate = Utils.str2Bcd("20181019");
        capk.CheckSum = Utils
                .str2Bcd("87f0cd7c0e86f38f89a66f8c47071a8b88586f26");
        KLog.v("addCAPK--"+("A000000333"+"03"+"B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33D"+"3").hashCode());
        iRet = SwishCardApplication.getInstance().getEmvCore().EmvLibAddCapk(capk);

//        KLog.e("capkresult", iRet + "");
    }

    /**
     * 检查卡槽
     */
    public void checkIccCard( Handler handle, final Icc mIcc) {
        KLog.v("Check Card, please waiting..." + handle);
        int iRet = -1;

        iRet = mIcc.DLL_IccCheck((byte) 0);
        KLog.v("DLL_IccCheck iRet: " + iRet);
        if (0 == iRet) {
            if (null != mIccCheckCardTimer) {
                mIccCheckCardTimer.cancel();
                mIccCheckCardTimer = null;
            }
            if (null != mIccCheckCardTimerTask) {
                mIccCheckCardTimerTask.cancel();
                mIccCheckCardTimerTask = null;
            }

            mAuthAmt = (int)Math.round( Double.parseDouble(SwishCardApplication.getInstance().preferenceUtils.getMoney())* 100);
            mCashBackAmt = 0;

            KLog.v("mAuthAmt" + mAuthAmt + "---mCashBackAmt" + mCashBackAmt);
            iRet = tranProcess(mAuthAmt, mCashBackAmt);
            mIcc.DLL_IccClose((byte) 0);

            if (0 != iRet) {
                Message msg = handle.obtainMessage();
                msg.obj = displayErrorInfo(iRet);
                msg.what = CARD_READ_ERROR;
                handle.sendMessage(msg);
            } else {
                display55TransResult(handle);
            }
        } else {
            handle.sendEmptyMessageDelayed(0x03, 500);
        }
    }

    /**
     * 55域报文
     *
     * @param handle
     */
    private void display55TransResult(Handler handle) {
        byte[] byTVR = new byte[5];
        int[] iOutLen = new int[1];
        int iRet = -1;
        String strTemp = null;
        strTemp = "55域报文:\r\n";
        String result = "";
        String str;
        byte[] by9F26 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F26, by9F26, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F26-->"
                    + ByteUtil.bytearrayToHexString(by9F26, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F26).substring(0, iOutLen[0] * 2);
            result += "9F26" + "08" + str;
        }

        byte[] by9F27 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F27, by9F27, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F27-->"
                    + ByteUtil.bytearrayToHexString(by9F27, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F27).substring(0, iOutLen[0] * 2);
            result += "9F27" + "01" + str;
         //   result += "9F27" + "0180";
        }

        byte[] by9F10 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F10, by9F10, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F10-->"
                    + ByteUtil.bytearrayToHexString(by9F10, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F10).substring(0, iOutLen[0] * 2);
            KLog.v("by9F10-result-" + result + "---"+new String(by9F10));
            int len = str.length() / 2;
            String value;
            if (len < 10) {
                value = "9F10" + "0" + len + str;
            } else {
                //value = "9F10" + len + str;
                value = "9F10" + Integer.toHexString(len) + str;
            }
            result += value;
        }

        byte[] by9F02 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F02, by9F02, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F02-->"
                    + ByteUtil.bytearrayToHexString(by9F02, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F02).substring(0, iOutLen[0] * 2);
            result += "9F02" + "06" + str;
        }

        byte[] by5F2A = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x5F2A, by5F2A, iOutLen);
        if (0 == iRet) {
            strTemp += ("by5F2A-->"
                    + ByteUtil.bytearrayToHexString(by5F2A, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by5F2A).substring(0, iOutLen[0] * 2);
            result += "5F2A" + "02" + str;
        }

        byte[] by82 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x82, by82, iOutLen);
        if (0 == iRet) {
            strTemp += ("by82-->"
                    + ByteUtil.bytearrayToHexString(by82, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by82).substring(0, iOutLen[0] * 2);
            result += "82" + "02" + str;
        }

      /*  byte[] by91 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x91, by91, iOutLen);
        if (0 == iRet) {
            strTemp += ("by91-->"
                    + ByteUtil.bytearrayToHexString(by91, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by91).substring(0, iOutLen[0] * 2);
            result += "91" + "02" + str;
        }*/

        byte[] by9F1A = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F1A, by9F1A, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F1A-->"
                    + ByteUtil.bytearrayToHexString(by9F1A, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F1A).substring(0, iOutLen[0] * 2);
            result += "9F1A" + "02" + str;
         //     result += "9F1A" + "020156";
        }

        byte[] by9F03 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F03, by9F03, iOutLen);
        str = "9F0306000000000000";
        if (0 == iRet) {
            strTemp += ("by9F03-->"
                    + ByteUtil.bytearrayToHexString(by9F03, iOutLen[0]) + "\r\n");

            str = Utils.bcd2Str(by9F03).substring(0, iOutLen[0] * 2);
        }
        result += str;

        byte[] by9F33 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F33, by9F33, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F33-->"
                    + ByteUtil.bytearrayToHexString(by9F33, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F33).substring(0, iOutLen[0] * 2);
            result += "9F33" + "03" + str;
        }

        byte[] by9F34 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F34, by9F34, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F34-->"
                    + ByteUtil.bytearrayToHexString(by9F34, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F34).substring(0, iOutLen[0] * 2);
            result += "9F34" + "03" + str;
        }

        byte[] by9F35 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F35, by9F35, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F35-->"
                    + ByteUtil.bytearrayToHexString(by9F35, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F35).substring(0, iOutLen[0] * 2);
            result += "9F35" + "01" + str;
        }

        byte[] by9F1E = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F1E, by9F1E, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F1E-->"
                    + ByteUtil.bytearrayToHexString(by9F1E, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F1E).substring(0, iOutLen[0] * 2);
            result += "9F1E" + "08" + str;
        }

        byte[] by84 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x84, by84, iOutLen);
        if (0 == iRet) {
            strTemp += ("by84-->"
                    + ByteUtil.bytearrayToHexString(by84, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by84).substring(0, iOutLen[0] * 2);
            int len = str.length() / 2;
            String value;
            if (len < 10) {
                value = "84" + "0" + len + str;
            } else {
                value = "84" + Integer.toHexString(len) + str;
            }
            result += value;
        }

        byte[] by9F09 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F09, by9F09, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F09-->"
                    + ByteUtil.bytearrayToHexString(by9F09, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F09).substring(0, iOutLen[0] * 2);
            result += "9F09" + "02" + str;
        }

        byte[] by9F37 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F37, by9F37, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F37-->"
                    + ByteUtil.bytearrayToHexString(by9F37, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F37).substring(0, iOutLen[0] * 2);
            result += "9F37" + "04" + str;
        }

        byte[] by9F36 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F36, by9F36, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F36-->"
                    + ByteUtil.bytearrayToHexString(by9F36, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F36).substring(0, iOutLen[0] * 2);
            result += "9F36" + "02" + str;
        }

        byte[] by95 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x95, by95, iOutLen);
        if (0 == iRet) {
            strTemp += ("by95-->"
                    + ByteUtil.bytearrayToHexString(by95, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by95).substring(0, iOutLen[0] * 2);
            result += "95" + "05" + str;
        }

        byte[] by9A = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9A, by9A, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9A-->"
                    + ByteUtil.bytearrayToHexString(by9A, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9A).substring(0, iOutLen[0] * 2);
            result += "9A" + "03" + str;
        }

        byte[] by9C = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9C, by9C, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9C-->"
                    + ByteUtil.bytearrayToHexString(by9C, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9C).substring(0, iOutLen[0] * 2);
            result += "9C" + "01" + str;
        }

        byte[] by9F41 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F41, by9F41, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F41-->"
                    + ByteUtil.bytearrayToHexString(by9F41, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F41).substring(0, iOutLen[0] * 2);
            int len = str.length() / 2;
            String value;
            KLog.v("by9F41=====" + str);
            if (len < 10) {
                value = "9F41" + "0" + len + str;
            } else {
                value = "9F41" + Integer.toHexString(len) + str;
            }
            KLog.v("by9F41--VALUE-" + value);
            result += value;
        }

        byte[] by9F63 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x9F63, by9F63, iOutLen);
        if (0 == iRet) {
            strTemp += ("by9F63-->"
                    + ByteUtil.bytearrayToHexString(by9F63, iOutLen[0]) + "\r\n");
            str = Utils.bcd2Str(by9F63).substring(0, iOutLen[0] * 2);
            int len = str.length() / 2;
            KLog.v("by9F63====" + str);
            String value;
            if (len <= 10) {
                value = "9F63" + "0" + len + str;
            } else {
                //value = "9F63" +  len  + str;
                value = "9F63" + Integer.toHexString(len) + str;
            }
            KLog.v("by9F63====" + value + "--len--" + len);
            result += value;
        }

        String track57 = "";
        byte[] by57 = new byte[255];
        iRet = mEmvCore.EmvLibGetTLV((short) 0x57, by57, iOutLen);
        if (0 == iRet) {
            strTemp += ("by57-->"
                    + ByteUtil.bytearrayToHexString(by57, iOutLen[0]) + "\r\n");
            track57 = Utils.bcd2Str(by57).substring(0, iOutLen[0] * 2)
                    .replace("D", "=");
            if (track57.substring(track57.length() - 1).equals("F")) {
                track57 = track57.substring(0, track57.length() - 1);
            }
        }

        Log.e("track", strTemp);
        // String cardinfo[] = track57.split("=");
        String cardinfo = track57;
        Log.e("result", "0" + result.length() / 2 + result);
      /*  String[] msg = { cardinfo.split("=")[0], track57,
                "0" + result.length() / 2 + result };*/
        Log.e("track57", cardinfo);

        int flag = mEmvCore.EmvLibGetSignFlag();
        if (1 == flag) {
            strTemp += ("Signature flag: " + flag + "\r\n");
        }
        //    String[] info = {cardinfo,"0" + result.length() / 2 + result };
        String[] info = {cardinfo, "0" + result.length() / 2 + result};
        if (null != strTemp) {
            Message message = new Message();
            message.what = CARD_READ_OK;
            message.obj = info;
            handle.sendMessage(message);

        }
    }


    /**
     * 返回最后的Value的长度
     *
     * @param hexString
     * @param position
     * @return
     */
    private LPositon getUnionLAndPosition(String hexString, int position) {
        String firstByteString = hexString.substring(position, position + 2);
        int i = Integer.parseInt(firstByteString, 16);
        String hexLength = "";
        if (((i >>> 7) & 1) == 0) {
            hexLength = hexString.substring(position, position + 2);
            position = position + 2;
        } else {
            // 当最左侧的bit位为1的时候，取得后7bit的值，
            int _L_Len = i & 127;
            position = position + 2;
            hexLength = hexString.substring(position, position + _L_Len * 2);
            // position表示第一个字节，后面的表示有多少个字节来表示后面的Value值
            position = position + _L_Len * 2;
        }
        return new LPositon(Integer.parseInt(hexLength, 16), position);
    }

    class LPositon {
        private int _vL;
        private int _position;

        public LPositon(int _vL, int position) {
            this._vL = _vL;
            this._position = position;
        }

        public int get_vL() {
            return _vL;
        }

        public void set_vL(int _vL) {
            this._vL = _vL;
        }

        public int get_position() {
            return _position;
        }

        public void set_position(int _position) {
            this._position = _position;
        }

    }

    private int tranProcess(int authAmt, int cashbackAmt) {
        int iRet = -1;
        KLog.v("Transacion processing, please waiting...");
        mEmvCore = SwishCardApplication.getInstance().getEmvCore();
        SwishCardApplication.getInstance().miTransCount++;
        AppList[] appList = new AppList[16];
        int[] listSize = new int[1];
        for (int i = 0; i < 16; i++) {
            appList[i] = new AppList();
        }
        //    KLog.v("tranProcess---EmvLibBuildCandidateList");
        iRet = mEmvCore.EmvLibBuildCandidateList(0, appList, listSize);
        //    KLog.v("tranProcess---iRet1-" + iRet);
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibSelectApp(0);
        //    KLog.v("tranProcess---iRet2-" + iRet);
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibInitTransaction(0, SwishCardApplication.getInstance().miTransCount, authAmt, cashbackAmt);
//        KLog.v("tranProcess---iRet3-" + iRet);
        if (0 != iRet &&
                EmvCoreContants.ERR_APPSEL != iRet)
            return iRet;
        while (EmvCoreContants.ERR_APPSEL == iRet) {
            iRet = mEmvCore.EmvLibGetNewCandidateList(appList, listSize);
            if (0 != iRet)
                return iRet;

            iRet = mEmvCore.EmvLibSelectApp(0);
            if (0 != iRet)
                return iRet;

            iRet = mEmvCore.EmvLibInitTransaction(0, 1, authAmt, cashbackAmt);
            if (0 != iRet &&
                    EmvCoreContants.ERR_APPSEL != iRet)
                return iRet;
        }
        iRet = mEmvCore.EmvLibReadAppData();
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibCheckExceptPan();
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibTermRiskManage();
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibOfflineAuth();
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibProcRestriction();
        if (0 != iRet)
            return iRet;
        iRet = mEmvCore.EmvLibCardholderVerify();
        if (0 != iRet)
            return iRet;
        int acType = 0;
        acType = mEmvCore.EmvLibTermActAnalysis();
        if (EmvCoreContants.AC_AAC == acType) {
            iRet = mEmvCore.EmvLib1stGenAC(acType);
            return iRet;
        } else {
            iRet = mEmvCore.EmvLib1stGenAC(acType);
            if (iRet <= 0)
                return iRet;

            acType = iRet;
            //online process
            //online flow
            byte[] authData = new byte[]{0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x2A, 0x2B};
            if (authData.length > 0) {
                mEmvCore.EmvLibHostAuth(authData, authData.length);
            }
            byte[] scriptBuf = new byte[]{0x71, 0x12, (byte) 0x9F, 0x18, 0x04, 0x11, 0x22, 0x33, 0x44, (byte) 0x86, 0x09, (byte) 0x84, 0x24, 0x00, 0x00, 0x04, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD};
            if (scriptBuf.length > 0) {
                mEmvCore.EmvLibScriptProcessing((byte) 0x71, scriptBuf, scriptBuf.length);
            }

            byte[] rspCode = new byte[]{0x30, 0x30};
            byte[] authCode = new byte[]{0x31, 0x32, 0x33, 0x34, 0x35, 0x36};
            iRet = mEmvCore.EmvLib2stGenAC(acType, EmvCoreContants.ONLINE_APPROVE, rspCode, authCode, authCode.length);

            scriptBuf = new byte[]{0x72, 0x12, (byte) 0x9F, 0x18, 0x04, 0x11, 0x22, 0x33, 0x44, (byte) 0x86, 0x09, (byte) 0x84, 0x24, 0x00, 0x00, 0x04, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD};
            if (scriptBuf.length > 0) {
                mEmvCore.EmvLibScriptProcessing((byte) 0x72, scriptBuf, scriptBuf.length);
            }

            return iRet;
        }
    }

    private String displayErrorInfo(int errRet) {
        String strTemp = null;

        switch (errRet) {
            case EmvCoreContants.ERR_EMVRSP:
                strTemp = "Trans Error";
                break;
            case EmvCoreContants.ERR_APPBLOCK:
                strTemp = "App Blocked";
                break;
            case EmvCoreContants.ERR_NOAPP:
                strTemp = "Trans Error";
                break;
            case EmvCoreContants.ERR_TIMEOUT:
                strTemp = "Time  Out";
                break;
            case EmvCoreContants.ERR_EMVDATA:
                strTemp = "Trans Error";
                break;
            case EmvCoreContants.ERR_NOTACCEPT:
                strTemp = "Not Accepted,Trans Terminated";
                break;
            case EmvCoreContants.ERR_EMVDENIAL:
                strTemp = "Trans Declined";
                break;
            case EmvCoreContants.ERR_KEYEXP:
                strTemp = "Public key expired";
                break;
            case EmvCoreContants.ERR_NOPINPAD:
                strTemp = "No Pinpad";
                break;
            case EmvCoreContants.ERR_NOPIN:
                strTemp = "No PIN";
                break;
            case EmvCoreContants.ERR_CAPKCHECKSUM:
                strTemp = "CA checksum error";
                break;
            case EmvCoreContants.ERR_NOTFOUND:
                strTemp = "Can't get specified tag";
                break;
            case EmvCoreContants.ERR_NODATA:
                strTemp = "Get tag data failed";
                break;
            case EmvCoreContants.ERR_OVERFLOW:
                strTemp = "Memory overflow";
                break;
            case EmvCoreContants.ERR_ICCRESET:
                strTemp = "Card Read Err";
                break;
            case EmvCoreContants.ERR_ICCCMD:
                strTemp = "Card Read Err";
                break;
            case EmvCoreContants.ERR_ICCBLOCK:
                strTemp = "Card Blocked";
                break;
            case EmvCoreContants.ERR_ICCNORECORD:
                strTemp = "IC card no record";
                break;
            case EmvCoreContants.ERR_GPORSP:
                strTemp = "Trans Error";
                break;
            case EmvCoreContants.ERR_APPSEL:
                strTemp = "App select error";
                break;
            case EmvCoreContants.ERR_JNIPARAM:
                strTemp = "JNI parameter error";
                break;
        }

        if (null != strTemp) {
            strTemp = "unknown error";
        }
        return strTemp;
    }

    /**
     * PIN加密
     *
     * @return
     */
    public String makePIN(String password, String card, String pinKey) {
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
    public String makeMAC(String macKey, String posReport) {
        String result = macCheck(macKey, posReport);
        return result;
    }

    /**
     * @param macKey mac密钥
     * @param data   加密报文
     * @return
     */
    public String macCheck(String macKey, String data) {
        byte[] k = Utils.str2Bcd(macKey);
        byte[] b = Utils.str2Bcd(data);
        String result = Utils.bcd2Str(MacEcbUtils.getMac(k, b));
        //  String result = Utils.bcd2Str(MacEcbUtils.javaHLL(k, data));
        return result;
    }


}
