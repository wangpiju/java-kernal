package com.pays.juxin;

public class JuxinQQWapPayApi
        extends JuxinPayApi {
    public static final String ID = "juxinQQWap";
    private static final String TITLE = "聚鑫QQ钱包wap支付接口";

    public String getTitle() {
        return "聚鑫QQ钱包wap支付接口";
    }

    public String getId() {
        return "juxinQQWap";
    }

    protected String getPayType() {
        return "70";
    }

    protected void init() {
        addBank("QQ", "");
        addBankCredit("QQ", "");
    }
}
