package com.pays.juxin;

public class JuxinAliWapPayApi
        extends JuxinPayApi {
    public static final String ID = "juxinAliWap";
    private static final String TITLE = "聚鑫支付宝wap支付接口";

    public String getTitle() {
        return "聚鑫支付宝wap支付接口";
    }

    public String getId() {
        return "juxinAliWap";
    }

    protected String getPayType() {
        return "40";
    }

    protected void init() {
        addBank("ALIPAY", "");
        addBankCredit("ALIPAY", "");
    }
}
