package com.qhw.swishcardapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    private final String PREFERENCE_NAME = "saveInfo";
    private final String PREF_PIN = "pin";
    private final String PREF_MAC = "mac";
    private final String PREF_TRACK = "track";
    private final String PREF_TERMINAL = "terminal";//终端
    private final String PREF_TENANT = "tenant";//商户
    private final String PREF_MONEY = "money";//交易金额
    private final String PREF_SIGNDATE = "signDate";//签到日期
    private final String PREF_KEY = "key";//密钥
    private final String PREF_PC = "pc";//批次号
    private final String PREF_MB_MERCHANT = "merchant_id";//移动支付商户号
    private final String PREF_ORDER_ID = "order_id";
    private final String PREF_MOBILE_PAY_TYPE = "mobile_pay_type";
    private final String PREF_THIRD_PARTY_ACCESS = "thirdPartyAccess";
    private final String PREF_OUT_TRADE_NO = "out_trade_no";


    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }


    /**
     * 设置终端
     *
     * @param value
     */
    public void setTermianl(String value) {
        editor.putString(PREF_TERMINAL, value);
        editor.commit();
    }

    /**
     * 获取终端
     *
     * @return
     */
    public String getTermianl() {
        return mSharedPreferences.getString(PREF_TERMINAL, "");
    }


    /**
     * 设置商户号
     *
     * @param value
     */
    public void setTenant(String value) {
        editor.putString(PREF_TENANT, value);
        editor.commit();
    }

    /**
     * 获取商户号
     *
     * @return
     */
    public String getTenant() {
        return mSharedPreferences.getString(PREF_TENANT, "");
    }


    /**
     * 设置TRACK密钥
     */
    public void setTPK(String secret) {
        editor.putString(PREF_TRACK, secret);
        editor.commit();
    }

    public String getTPK() {
        return mSharedPreferences.getString(PREF_TRACK, "");
    }

    /**
     * 设置MAC密钥
     */
    public void setMAC(String secret) {
        editor.putString(PREF_MAC, secret);
        editor.commit();
    }

    public String getMAC() {
        return mSharedPreferences.getString(PREF_MAC, "");
    }

    /**
     * 设置PIN密钥
     */
    public void setPIN(String secret) {
        editor.putString(PREF_PIN, secret);
        editor.commit();
    }

    public String getPIN() {
        return mSharedPreferences.getString(PREF_PIN, "");
    }


    /**
     * 设置交易金额
     */
    public void setMoney(String money) {
        editor.putString(PREF_MONEY, money);
        editor.commit();
    }

    public String getMoney() {
        return mSharedPreferences.getString(PREF_MONEY, "");
    }


    /**
     * 批次号
     */
    public void setBatchNo(String bn) {
        editor.putString(PREF_PC, bn);
        editor.commit();
    }

    public String getBatchNo() {
        return mSharedPreferences.getString(PREF_PC, "");
    }


    /**
     * 签到时间(年月日)
     *
     * @param SignDate
     */
    public void setSignDate(String SignDate) {
        editor.putString(PREF_SIGNDATE, SignDate);
        editor.commit();
    }


    public String getSignDate() {
        return mSharedPreferences.getString(PREF_SIGNDATE, "");
    }

    /**
     * 主密钥
     *
     * @param key
     */
    public void setKey(String key) {
        editor.putString(PREF_KEY, key);
        editor.commit();
    }

    public String getKey() {
        return mSharedPreferences.getString(PREF_KEY, "");
    }

    /**
     * 移动支付商户编号
     *
     * @param merchant_id
     */
    public void setMerchant_id(String merchant_id) {
        editor.putString(PREF_MB_MERCHANT, merchant_id);
        editor.commit();
    }

    public String getMerchant_id() {
        return mSharedPreferences.getString(PREF_MB_MERCHANT, "");
    }


    /**
     * 移动支付方式
     */
    public void setMobileType(String type) {
        editor.putString(PREF_MOBILE_PAY_TYPE, type);
        editor.commit();
    }

    public String getMobileType() {
        return mSharedPreferences.getString(PREF_MOBILE_PAY_TYPE, "");
    }

    /**
     * 订单号
     */
    public void setOrder_id(String order_id) {
        editor.putString(PREF_ORDER_ID, order_id);
        editor.commit();
    }

    public String getOrder_id() {
        return mSharedPreferences.getString(PREF_ORDER_ID, "");
    }

    /**
     * 是否有外接(外部app接入)
     */
    public void setIsThirdPartyAccess(String state) {
        editor.putString(PREF_THIRD_PARTY_ACCESS, state);
        editor.commit();
    }

    public String getIsThirdPartyAccess() {
        return mSharedPreferences.getString(PREF_THIRD_PARTY_ACCESS, "");
    }

    /**
     * 上游订单号
     */
    public void setOutTradeNo(String state) {
        editor.putString(PREF_OUT_TRADE_NO, state);
        editor.commit();
    }

    public String getOutTradeNo() {
        return mSharedPreferences.getString(PREF_OUT_TRADE_NO, "");
    }

}
