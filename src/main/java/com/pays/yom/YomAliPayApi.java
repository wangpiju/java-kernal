package com.pays.yom;

public class YomAliPayApi
        extends YomPayApi {
    public static final String ID = "yomAli";
    private static final String TITLE = "优付支付宝支付接口";

    public String getTitle() {
        return "优付支付宝支付接口";
    }

    public String getId() {
        return "yomAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
    }
}
