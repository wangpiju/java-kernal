package com.pays.myjop;

public class MyjopScanWxPayApi
        extends MyjopScanPayApi {
    public static final String ID = "myjopScanWx";
    private static final String TITLE = "创瑞宝微信扫码支付接口";

    public String getTitle() {
        return "创瑞宝微信扫码支付接口";
    }

    public String getId() {
        return "myjopScanWx";
    }

    protected void init() {
        addBank("WEIXIN", "");
        addBankCredit("WEIXIN", "");
    }
}
