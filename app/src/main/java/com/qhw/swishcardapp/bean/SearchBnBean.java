package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/7/21.
 */

public class SearchBnBean {


    /**
     * errCode : 0
     * errMsg : success
     * content : {"id":"2","merchant_id":"10001","device_id":"1101","trade_num":"20170720153726102","is_success":"1","money":"36.45","trade_time":"2017-07-20 15:37:26","batch_no":"20170721001"}
     */

    private int errCode;
    private String errMsg;
    /**
     * id : 2
     * merchant_id : 10001
     * device_id : 1101
     * trade_num : 20170720153726102
     * is_success : 1
     * money : 36.45
     * trade_time : 2017-07-20 15:37:26
     * batch_no : 20170721001
     */

    private ContentBean content;

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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        private String id;
        private String merchant_id;
        private String device_id;
        private String trade_num;
        private String is_success;
        private String money;
        private String trade_time;
        private String batch_no;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getTrade_num() {
            return trade_num;
        }

        public void setTrade_num(String trade_num) {
            this.trade_num = trade_num;
        }

        public String getIs_success() {
            return is_success;
        }

        public void setIs_success(String is_success) {
            this.is_success = is_success;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTrade_time() {
            return trade_time;
        }

        public void setTrade_time(String trade_time) {
            this.trade_time = trade_time;
        }

        public String getBatch_no() {
            return batch_no;
        }

        public void setBatch_no(String batch_no) {
            this.batch_no = batch_no;
        }
    }
}
