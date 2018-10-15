package com.pays.juxin;

public class JuxinWxWapPayApi
        extends JuxinPayApi {
    public static final String ID = "juxinWxWap";
    private static final String TITLE = "聚鑫微信wap支付接口";

    public String getTitle() {
        return "聚鑫微信wap支付接口";
    }

    public String getId() {
        return "juxinWxWap";
    }

    protected String getPayType() {
        return "20";
    }

    protected void init() {
        addBank("WEIXIN", "");
        addBankCredit("WEIXIN", "");
    }
}
