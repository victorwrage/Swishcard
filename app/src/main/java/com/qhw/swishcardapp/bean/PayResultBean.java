package com.qhw.swishcardapp.bean;

/**
 * Created by Hello_world on 2017/7/13.
 */

public class PayResultBean {

    /**
     * errcode : 0
     * errmsg : success
     * orderinfo : {"order_id":"207011707131722279019","agent_id":"270241","merchant_id":"617071103153513","create_time":"2017-07-13 17:22:27","pay_money":"36.00","procedure_fee":"0.00","t0_fee":"0.00","wr_fee":"0.00","state":"4","cashier":"15768274336","top_agent_id":"270241","source":"1","trantp":"0","aid":"1001499412100883"}
     * sign : f3213f9c9dfd18d38a21024041475a43
     */

    private int errcode;
    private String errmsg;
    /**
     * order_id : 207011707131722279019
     * agent_id : 270241
     * merchant_id : 617071103153513
     * create_time : 2017-07-13 17:22:27
     * pay_money : 36.00
     * procedure_fee : 0.00
     * t0_fee : 0.00
     * wr_fee : 0.00
     * state : 4
     * cashier : 15768274336
     * top_agent_id : 270241
     * source : 1
     * trantp : 0
     * aid : 1001499412100883
     */

    private OrderinfoBean orderinfo;
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

    public OrderinfoBean getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(OrderinfoBean orderinfo) {
        this.orderinfo = orderinfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class OrderinfoBean {
        private String order_id;
        private String agent_id;
        private String merchant_id;
        private String create_time;
        private String pay_money;
        private String pay_time;
        private String procedure_fee;

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        private String t0_fee;
        private String wr_fee;
        private String state;
        private String cashier;
        private String top_agent_id;
        private String source;
        private String trantp;
        private String aid;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(String agent_id) {
            this.agent_id = agent_id;
        }

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
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

        public String getProcedure_fee() {
            return procedure_fee;
        }

        public void setProcedure_fee(String procedure_fee) {
            this.procedure_fee = procedure_fee;
        }

        public String getT0_fee() {
            return t0_fee;
        }

        public void setT0_fee(String t0_fee) {
            this.t0_fee = t0_fee;
        }

        public String getWr_fee() {
            return wr_fee;
        }

        public void setWr_fee(String wr_fee) {
            this.wr_fee = wr_fee;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCashier() {
            return cashier;
        }

        public void setCashier(String cashier) {
            this.cashier = cashier;
        }

        public String getTop_agent_id() {
            return top_agent_id;
        }

        public void setTop_agent_id(String top_agent_id) {
            this.top_agent_id = top_agent_id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTrantp() {
            return trantp;
        }

        public void setTrantp(String trantp) {
            this.trantp = trantp;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }
    }
}
