package com.pays.w5;

public class W5RsaQQScanPayApi
        extends W5RsaScanPayApi {
    private static final String TITLE = "W付RSAQQ扫码支付接口";
    public static final String ID = "w5RsaQQScan";

    public String getTitle() {
        return "W付RSAQQ扫码支付接口";
    }

    public String getId() {
        return "w5RsaQQScan";
    }

    protected void init() {
        addBank("QQ", "tenpay_scan");
    }
}
