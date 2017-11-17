package com.qhw.swishcardapp.retrofirapi;

import com.qhw.swishcardapp.AppConstants;
import com.qhw.swishcardapp.bean.AddBnBean;
import com.qhw.swishcardapp.bean.GetKeyBean;
import com.qhw.swishcardapp.bean.GetPcBean;
import com.qhw.swishcardapp.bean.SearchBnBean;
import com.qhw.swishcardapp.bean.UpdateBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by Hello_world on 2017/7/21.
 */

public interface ConnectonPospService {
    @POST(AppConstants.URL_GETKEY)
    @FormUrlEncoded
    Observable<GetKeyBean> getDecryptionKey(@Field("merchant_id") String merchant_id,
                                            @Field("device_id") String device_id);

    @POST(AppConstants.URL_ADDBN)
    @FormUrlEncoded
    Observable<AddBnBean>
    addBn(@Field("merchant_id") String merchant_id,
          @Field("device_id") String device_id,
          @Field("sign") String sign,
          @Field("trade_num") String trade_num,
          @Field("is_success") String is_success,
          @Field("money") String money,
          @Field("trade_time") String trade_time,
          @Field("batch_no") String batch_no);


    @POST(AppConstants.URL_SEARCHBN)
    @FormUrlEncoded
    Observable<SearchBnBean> searchBn(@Field("merchant_id") String merchant_id,
                                      @Field("device_id") String device_id,
                                      @Field("sign") String sign);

    @POST(AppConstants.URL_GETPC)
    @FormUrlEncoded
    Observable<GetPcBean> getBatchNumber(@Field("merchant_id") String merchant_id,
                                         @Field("device_id") String device_id,
                                         @Field("sign") String sign);

    @POST(AppConstants.URL_GET_YINGYONG_LIST)
    @FormUrlEncoded
    Observable<UpdateBean> getYingYong(@Field("shop_id") String shop_id,
                                       @Field("status") String status);

}
