package com.pays.yee;

public class YeeTenPayApi
        extends YeePayApi {
    public static final String ID = "yeeTen";
    private static final String TITLE = "银宝财付通支付接口";

    public String getTitle() {
        return "银宝财付通支付接口";
    }

    public String getId() {
        return "yeeTen";
    }

    protected void init() {
        addBank("TENPAY", "TENPAY");
    }
}
