package com.pays.eka;

public class EkaTenPayApi
        extends EkaPayApi {
    public static final String ID = "ekaTen";
    private static final String TITLE = "千网财付通支付接口";

    public String getTitle() {
        return "千网财付通支付接口";
    }

    public String getId() {
        return "ekaTen";
    }

    protected void init() {
        addBank("WEIXIN", "993");
        addBankCredit("WEIXIN", "993");
    }
}
