package com.pays.rfu;

public class RfuWxPayApi
        extends RfuPayApi {
    public static final String ID = "rfuWx";
    private static final String TITLE = "锐付微信支付接口";

    public String getTitle() {
        return "锐付微信支付接口";
    }

    public String getId() {
        return "rfuWx";
    }

    protected void init() {
        addBankCredit("WEIXIN", "WECHAT");
    }
}
