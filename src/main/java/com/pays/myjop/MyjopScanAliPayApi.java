package com.pays.myjop;

public class MyjopScanAliPayApi
        extends MyjopScanPayApi {
    public static final String ID = "myjopScanAli";
    private static final String TITLE = "创瑞宝支付宝扫码支付接口";

    public String getTitle() {
        return "创瑞宝支付宝扫码支付接口";
    }

    public String getId() {
        return "myjopScanAli";
    }

    protected void init() {
        addBank("ALIPAY", "");
        addBankCredit("ALIPAY", "");
    }
}
