package com.pays.worth;

public class WorthAliPayApi
        extends WorthPayApi {
    public static final String ID = "worthAli";
    private static final String TITLE = "华势在线支付宝支付接口";

    public String getTitle() {
        return "华势在线支付宝支付接口";
    }

    public String getId() {
        return "worthAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
        addBankCredit("ALIPAY", "ALIPAY");
    }
}
