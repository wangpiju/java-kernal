package com.pays.yom;

public class YomWxPayApi
        extends YomPayApi {
    public static final String ID = "yomWx";
    private static final String TITLE = "优付微信支付接口";

    public String getTitle() {
        return "优付微信支付接口";
    }

    public String getId() {
        return "yomWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
    }
}
