package com.pays.w5;

public class W5RsaAliScanPayApi
        extends W5RsaScanPayApi {
    private static final String TITLE = "W付RSA支付宝扫码支付接口";
    public static final String ID = "w5RsaAliScan";

    public String getTitle() {
        return "W付RSA支付宝扫码支付接口";
    }

    public String getId() {
        return "w5RsaAliScan";
    }

    protected void init() {
        addBank("ALIPAY", "alipay_scan");
    }
}
