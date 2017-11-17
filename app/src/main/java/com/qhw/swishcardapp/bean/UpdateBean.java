package com.qhw.swishcardapp.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hello_world on 2017/8/1.
 */

public class UpdateBean {


    /**
     * errcode : 200
     * errmsg : success
     * content : [{"id":"1","name":"万店通","logo":null,"distribution":"4234","app_name":"com.qhw.shopmanagerapp.application.MyApplication","package":"com.qhw.shopmanagerapp","app_url":"http://wdt.qianhaiwei.com/ThinkCmf/public/Api/APK/wdt1.1.4_114_jiagu_sign.apk","app_version":"14","app_version_code":"1.1.4","app_type":null,"status":"0","code":"201","auditor":"","createtime":"2017-07-22 00:02:11","installed":"0","shop_id":"111","company_id":null,"type":"1"},{"id":"2","name":"百宝","logo":null,"distribution":"32423","app_name":"com.o2obaibao.baibaoapp.ApplicationApp","package":"com.o2obaibao.baibaoapp","app_url":"http://wdt.qianhaiwei.com/ThinkCmf/public/Api/APK/baibao.apk","app_version":"1.0","app_version_code":"40","app_type":null,"status":"0","code":"202","auditor":"222","createtime":"2017-05-04 15:13:57","installed":"0","shop_id":"111","company_id":null,"type":"1"},{"id":"6","name":"测试Demo1","logo":null,"distribution":"4234","app_name":"com.qhw.shopmanagerapp.application.MyApplication","package":"com.qhw.shopmanagerapp","app_url":"http://www.baidu.com/","app_version":"7","app_version_code":"1.0.7","app_type":null,"status":"0","code":"201","auditor":"","createtime":"2017-05-26 17:30:21","installed":"0","shop_id":"111","company_id":null,"type":"0"},{"id":"7","name":"测试Demo2","logo":null,"distribution":"32423","app_name":"com.o2obaibao.baibaoapp.ApplicationApp","package":"com.o2obaibao.baibaoapp","app_url":"http://www.baidu.com/","app_version":"1.0","app_version_code":"40","app_type":null,"status":"0","code":"202","auditor":"222","createtime":"2017-05-26 17:30:19","installed":"0","shop_id":"111","company_id":null,"type":"0"},{"id":"11","name":"中大威配送宝","logo":null,"distribution":"32423","app_name":"com.ndlan.distribution","package":"com.ndlan.distribution.activity","app_url":"http://wdt.qianhaiwei.com/ThinkCmf/public/Api/APK/Distribution.apk","app_version":"1","app_version_code":"1.0","app_type":null,"status":"0","code":"202","auditor":"222","createtime":"2017-05-26 17:29:49","installed":"0","shop_id":"111","company_id":null,"type":"1"}]
     */

    private int errcode;
    private String errmsg;
    /**
     * id : 1
     * name : 万店通
     * logo : null
     * distribution : 4234
     * app_name : com.qhw.shopmanagerapp.application.MyApplication
     * package : com.qhw.shopmanagerapp
     * app_url : http://wdt.qianhaiwei.com/ThinkCmf/public/Api/APK/wdt1.1.4_114_jiagu_sign.apk
     * app_version : 14
     * app_version_code : 1.1.4
     * app_type : null
     * status : 0
     * code : 201
     * auditor :
     * createtime : 2017-07-22 00:02:11
     * installed : 0
     * shop_id : 111
     * company_id : null
     * type : 1
     */

    private List<ContentBean> content;

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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        private String id;
        private String name;
        private Object logo;
        private String distribution;
        private String app_name;
        @SerializedName("package")
        private String packageX;
        private String app_url;
        private String app_version;
        private String app_version_code;
        private Object app_type;
        private String status;
        private String code;
        private String auditor;
        private String createtime;
        private String installed;
        private String shop_id;
        private Object company_id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getLogo() {
            return logo;
        }

        public void setLogo(Object logo) {
            this.logo = logo;
        }

        public String getDistribution() {
            return distribution;
        }

        public void setDistribution(String distribution) {
            this.distribution = distribution;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getApp_url() {
            return app_url;
        }

        public void setApp_url(String app_url) {
            this.app_url = app_url;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public String getApp_version_code() {
            return app_version_code;
        }

        public void setApp_version_code(String app_version_code) {
            this.app_version_code = app_version_code;
        }

        public Object getApp_type() {
            return app_type;
        }

        public void setApp_type(Object app_type) {
            this.app_type = app_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAuditor() {
            return auditor;
        }

        public void setAuditor(String auditor) {
            this.auditor = auditor;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getInstalled() {
            return installed;
        }

        public void setInstalled(String installed) {
            this.installed = installed;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public Object getCompany_id() {
            return company_id;
        }

        public void setCompany_id(Object company_id) {
            this.company_id = company_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
