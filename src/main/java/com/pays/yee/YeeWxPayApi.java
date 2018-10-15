package com.pays.yee;

public class YeeWxPayApi
        extends YeePayApi {
    public static final String ID = "yeeWx";
    private static final String TITLE = "银宝微信支付接口";

    public String getTitle() {
        return "银宝微信支付接口";
    }

    public String getId() {
        return "yeeWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
    }
}
