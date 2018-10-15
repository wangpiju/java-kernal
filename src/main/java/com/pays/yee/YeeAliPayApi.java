package com.pays.yee;

public class YeeAliPayApi
        extends YeePayApi {
    public static final String ID = "yeeAli";
    private static final String TITLE = "银宝支付宝支付接口";

    public String getTitle() {
        return "银宝支付宝支付接口";
    }

    public String getId() {
        return "yeeAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
    }
}
