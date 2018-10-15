package com.pays.yee;

public class YeeWxWAPPayApi
        extends YeePayApi {
    public static final String ID = "yeeWxWap";
    private static final String TITLE = "银宝微信WAP支付接口";

    public String getTitle() {
        return "银宝微信WAP支付接口";
    }

    public String getId() {
        return "yeeWxWap";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXINWAP");
    }
}
