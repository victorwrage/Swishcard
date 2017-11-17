package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/9/15.
 */

public class ErroBean {

    /**
     * errcode : 0
     * errmsg : 支付成功
     */

    private int errcode;
    private String errmsg;

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
}
