package com.pays.din;

public class DinRsaWxPayApi
        extends DinRsaPayApi {
    public static final String ID = "dinRsaWx";
    private static final String TITLE = "智付RSA-S微信支付接口";

    public String getTitle() {
        return "智付RSA-S微信支付接口";
    }

    public String getId() {
        return "dinRsaWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXIN");
    }
}
