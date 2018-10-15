package com.pays.worth;

public class WorthWxPayApi
        extends WorthPayApi {
    public static final String ID = "worthWx";
    private static final String TITLE = "华势在线微信支付接口";

    public String getTitle() {
        return "华势在线微信支付接口";
    }

    public String getId() {
        return "worthWx";
    }

    protected void init() {
        addBank("WEIXIN", "WXPAY");
        addBankCredit("WEIXIN", "WXPAY");
    }
}
