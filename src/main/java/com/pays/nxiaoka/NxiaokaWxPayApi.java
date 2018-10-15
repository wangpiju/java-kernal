package com.pays.nxiaoka;

public class NxiaokaWxPayApi
        extends NxiaokaPayApi {
    public static final String ID = "nxiaokaWx";
    private static final String TITLE = "网富通微信接口";

    public String getTitle() {
        return "网富通微信接口";
    }

    public String getId() {
        return "nxiaokaWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN");
        addBankCredit("WEIXIN", "WEIXINWAP");
    }
}
