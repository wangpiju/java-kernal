package com.pays.eka;

public class EkaAliPayApi
        extends EkaPayApi {
    public static final String ID = "ekaAli";
    private static final String TITLE = "千网支付宝支付接口";

    public String getTitle() {
        return "千网支付宝支付接口";
    }

    public String getId() {
        return "ekaAli";
    }

    protected void init() {
        addBank("ALIPAY", "992");
        addBankCredit("ALIPAY", "992");
    }
}
