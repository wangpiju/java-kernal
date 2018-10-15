package com.pays.w5;

public class W5AliScanPayApi
        extends W5WxScanPayApi {
    public static final String ID = "w5AliScan";
    private static final String TITLE = "W5支付宝扫码支付接口";

    public String getTitle() {
        return "W5支付宝扫码支付接口";
    }

    public String getId() {
        return "w5AliScan";
    }

    protected void init() {
        addBank("ALIPAY", "1");
    }
}
