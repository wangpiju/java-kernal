package com.pays.w5;

public class W5RsaWxScanPayApi
        extends W5RsaScanPayApi {
    private static final String TITLE = "W付RSA微信扫码支付接口";
    public static final String ID = "w5RsaWxScan";

    public String getTitle() {
        return "W付RSA微信扫码支付接口";
    }

    public String getId() {
        return "w5RsaWxScan";
    }

    protected void init() {
        addBank("WEIXIN", "weixin_scan");
    }
}
