package com.pays.zsage;

public class ZsageAliPayApi
        extends ZsageWxPayApi {
    public static final String ID = "zsageAli";
    private static final String TITLE = "泽圣支付宝支付接口";

    public String getTitle() {
        return "泽圣支付宝支付接口";
    }

    public String getId() {
        return "zsageAli";
    }

    protected void init() {
        addBank("ALIPAY", "30");
        addBankCredit("ALIPAY", "30");
    }
}
