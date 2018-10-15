package com.pays.mobao;

public class MobaoZfbPayApi
        extends MobaoPayApi {
    public static final String ID = "mobaoZfb";
    private static final String TITLE = "摩宝支付宝支付接口";

    public String getTitle() {
        return "摩宝支付宝支付接口";
    }

    public String getId() {
        return "mobaoZfb";
    }

    protected void init() {
        addBankCredit("ALIPAY", "ZHIFUBAO");
    }
}
