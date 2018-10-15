package com.pays.zito;

public class ZitoAliPayApi
        extends ZitoPayApi {
    public static final String ID = "zitoAli";
    private static final String TITLE = "融智付支付宝支付接口";

    public String getTitle() {
        return "融智付支付宝支付接口";
    }

    public String getId() {
        return "zitoAli";
    }

    protected void init() {
        addBank("ALIPAY", "ZHIFUBAO");
        addBankCredit("ALIPAY", "ZHIFUBAO");
    }
}
