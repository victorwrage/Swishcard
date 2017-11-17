package com.qhw.swishcardapp.retrofirapi;


import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.bean.AuCodePayBean;
import com.qhw.swishcardapp.bean.ErroBean;
import com.qhw.swishcardapp.bean.PayResultBean;
import com.qhw.swishcardapp.bean.QrCodeBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by Hello_world on 2017/7/13.
 */

public interface QrPayService {
    /**
     * 生成二维码支付
     *
     * @param aid
     * @param sign
     * @param merchant_id
     * @param pay_money
     * @param pay_type
     * @param trade_type
     * @return
     */
    @POST(AppConstants.URL_QRPAY)
    @FormUrlEncoded
    Observable<QrCodeBean> pay(@Field("aid") String aid,
                               @Field("sign") String sign,
                               @Field("merchant_id") String merchant_id,
                               @Field("pay_money") String pay_money,
                               @Field("pay_type") String pay_type,
                               @Field("trade_type") String trade_type
    );

    /**
     * 订单查询
     *
     * @param aid
     * @param sign
     * @param merchant_id
     * @param order_id
     * @return
     */

    @POST(AppConstants.URL_ORDER_S)
    @FormUrlEncoded
    Observable<PayResultBean> result(@Field("aid") String aid,
                                     @Field("sign") String sign,
                                     @Field("merchant_id") String merchant_id,
                                     @Field("order_id") String order_id
    );


    /**
     * 扫码支付
     *
     * @param aid
     * @param sign
     * @param merchant_id
     * @param pay_money
     * @param pay_type
     * @param trade_type
     * @return
     */
    @POST(AppConstants.URL_AUPAY)
    @FormUrlEncoded
    Observable<AuCodePayBean> auPay(@Field("aid") String aid,
                                    @Field("sign") String sign,
                                    @Field("merchant_id") String merchant_id,
                                    @Field("pay_money") String pay_money,
                                    @Field("pay_type") String pay_type,
                                    @Field("trade_type") String trade_type,
                                    @Field("auth_code") String auth_code
    );


    /**
     * 扫码支付查询接口
     *
     * @param aid
     * @param sign
     * @param order_id
     * @param merchant_id
     * @param out_trade_no
     * @param pay_type
     * @param pay_money
     * @param trade_type
     * @return
     */
    @POST(AppConstants.URL_AUQUE)
    @FormUrlEncoded
    Observable<ErroBean> auQue(@Field("aid") String aid,
                               @Field("sign") String sign,
                               @Field("order_id") String order_id,
                               @Field("merchant_id") String merchant_id,
                               @Field("out_trade_no") String out_trade_no,
                               @Field("pay_type") String pay_type,
                               @Field("pay_money") String pay_money,
                               @Field("trade_type") String trade_type
    );

}
