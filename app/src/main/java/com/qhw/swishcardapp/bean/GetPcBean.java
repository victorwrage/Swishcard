package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/7/21.
 */

public class GetPcBean {


    /**
     * errCode : 0
     * errMsg : success
     * batch_no : 20170721001
     */

    private int errCode;
    private String errMsg;
    private String batch_no;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }
}

