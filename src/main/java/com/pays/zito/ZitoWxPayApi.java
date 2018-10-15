package com.pays.zito;

public class ZitoWxPayApi
        extends ZitoPayApi {
    public static final String ID = "zitoWx";
    private static final String TITLE = "融智付微信支付接口";

    public String getTitle() {
        return "融智付微信支付接口";
    }

    public String getId() {
        return "zitoWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXIN");
    }
}
