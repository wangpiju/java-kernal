package com.pays.yeeyk;

public class YeeykAliPayApi
        extends YeeykPayApi {
    public static final String ID = "yeeykAli";
    private static final String TITLE = "易游酷支付宝支付接口";

    public String getTitle() {
        return "易游酷支付宝支付接口";
    }

    public String getId() {
        return "yeeykAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
    }
}
