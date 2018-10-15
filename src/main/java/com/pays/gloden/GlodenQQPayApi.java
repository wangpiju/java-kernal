package com.pays.gloden;

public class GlodenQQPayApi
        extends GlodenScanPayApi {
    public static final String ID = "glodenQQ";
    private static final String TITLE = "GlodenQQ扫码支付接口";

    public String getTitle() {
        return "GlodenQQ扫码支付接口";
    }

    public String getId() {
        return "glodenQQ";
    }

    protected void init() {
        addBank("QQ", "190001");
        addBankCredit("QQ", "190001");
    }
}
