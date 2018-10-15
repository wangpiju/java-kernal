package com.pays.cnw;

public class CnwWxPayApi
        extends CnwPayApi {
    public static final String ID = "cnwWx";
    private static final String TITLE = "银商通微信支付接口";

    public String getTitle() {
        return "银商通微信支付接口";
    }

    public String getId() {
        return "cnwWx";
    }

    protected void init() {
        addBank("WEIXIN", "76001");
    }
}
