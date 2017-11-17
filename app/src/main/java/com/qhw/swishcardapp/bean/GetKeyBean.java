package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/7/21.
 */

public class GetKeyBean {

    /**
     * errCode : 0
     * errMsg : success
     * key : qMBsS2bVEap2qRGdKowBjvma3LdPS9E2LgRm1UcKXd6LPEp3ZKTHtgeYfsdX4OHP
     */

    private int errCode;
    private String errMsg;
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
