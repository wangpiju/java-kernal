package com.pays.juxin;

public class JuxinWxPayApi
        extends JuxinPayApi {
    public static final String ID = "juxinWx";
    private static final String TITLE = "聚鑫微信支付接口";

    public String getTitle() {
        return "聚鑫微信支付接口";
    }

    public String getId() {
        return "juxinWx";
    }

    protected String getPayType() {
        return "10";
    }

    protected void init() {
        addBank("WEIXIN", "");
        addBankCredit("WEIXIN", "");
    }
}
