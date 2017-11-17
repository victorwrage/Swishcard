package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/7/13.
 */

public class QrCodeBean {

    /**
     * errcode : 0
     * errmsg : success
     * aid : 1001499412100883
     * merchant_id : 617071103153513
     * order_id : 207001707131626483856
     * create_time : 2017-07-13 16:26:48
     * pay_money : 25
     * qrcode : weixin://wxpay/bizpayurl?pr=UamlXVs
     * sign : 934a051043e05d839f2825db88dcf179
     */

    private int errcode;
    private String errmsg;
    private String aid;
    private String merchant_id;
    private String order_id;
    private String create_time;
    private String pay_money;
    private String qrcode;
    private String sign;

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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
