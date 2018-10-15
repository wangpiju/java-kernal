package com.pays.mobao;

public class MobaoWxPayApi
        extends MobaoPayApi {
    public static final String ID = "mobaoWx";
    private static final String TITLE = "摩宝微信支付接口";

    public String getTitle() {
        return "摩宝微信支付接口";
    }

    public String getId() {
        return "mobaoWx";
    }

    protected void init() {
        addBankCredit("WEIXIN", "WEIXIN");
    }
}
