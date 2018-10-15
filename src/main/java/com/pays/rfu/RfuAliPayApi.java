package com.pays.rfu;

public class RfuAliPayApi
        extends RfuPayApi {
    public static final String ID = "rfuAli";
    private static final String TITLE = "锐付支付宝支付接口";

    public String getTitle() {
        return "锐付支付宝支付接口";
    }

    public String getId() {
        return "rfuAli";
    }

    protected void init() {
        addBankCredit("ALIPAY", "ALIPAY");
    }
}
