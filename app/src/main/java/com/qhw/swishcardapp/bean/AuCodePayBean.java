package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/9/14.
 */

public class AuCodePayBean {

    /**
     * errcode : 2
     * errmsg : 哈哈
     * aid : 122212121
     * merchant_id : 43437349
     * order_id : 374943784397349
     * create_time : 2017-09-14 13:12:12
     * pay_money : 0.01
     * auth_code : 232323233223
     * sign : ajdweiojdeoiu239e82390ej23o3j
     */

    private int errcode;
    private String errmsg;
    private String aid;
    private String merchant_id;
    private String order_id;
    private String create_time;
    private String pay_money;
    private String auth_code;
    private String sign;
    private String mach_order_id;
    private String trade_type;
    private String out_trade_no;
    private String pay_type;

    public String getMach_order_id() {
        return mach_order_id;
    }

    public void setMach_order_id(String mach_order_id) {
        this.mach_order_id = mach_order_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
