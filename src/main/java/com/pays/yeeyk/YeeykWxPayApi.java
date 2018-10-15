package com.pays.yeeyk;

public class YeeykWxPayApi
        extends YeeykPayApi {
    public static final String ID = "yeeykWx";
    private static final String TITLE = "易游酷微信支付接口";

    public String getTitle() {
        return "易游酷微信支付接口";
    }

    public String getId() {
        return "yeeykWx";
    }

    protected void init() {
        addBank("WEIXIN", "WX");
    }
}
