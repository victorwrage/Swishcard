package com.qhw.swishcardapp.model.callback;

/**
 * Created by Hello_world on 2017/9/14.
 */

public interface PayResultOnNextListener {

    void successCallBack();

    void faildCallBack();

    void auCodeCallBack(String code);

    void checkingPayRequest();

    void checkingPayResult(int code);

}
