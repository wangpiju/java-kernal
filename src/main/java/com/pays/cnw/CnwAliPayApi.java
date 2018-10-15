package com.pays.cnw;

public class CnwAliPayApi
        extends CnwPayApi {
    public static final String ID = "cnwAli";
    private static final String TITLE = "银商通支付宝支付接口";

    public String getTitle() {
        return "银商通支付宝支付接口";
    }

    public String getId() {
        return "cnwAli";
    }

    protected void init() {
        addBank("ALIPAY", "76003");
    }
}
