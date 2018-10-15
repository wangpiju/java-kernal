package com.pays.mm;

public class MMWxPayApi
        extends MMPayApi {
    public static final String ID = "mmWx";
    private static final String TITLE = "MM微信接口";

    public String getTitle() {
        return "MM微信接口";
    }

    public String getId() {
        return "mmWx";
    }

    protected void init() {
        addBank("WEIXIN", "WXPAY");
        addBankCredit("WEIXIN", "WXPAY");
    }
}
