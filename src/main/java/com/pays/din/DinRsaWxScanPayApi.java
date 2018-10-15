package com.pays.din;

public class DinRsaWxScanPayApi
        extends DinRsaScanPayApi {
    private static final String TITLE = "智付RSA微信扫码支付接口";
    public static final String ID = "dinRsaWxScan";

    public String getTitle() {
        return "智付RSA微信扫码支付接口";
    }

    public String getId() {
        return "dinRsaWxScan";
    }

    protected void init() {
        addBank("WEIXIN", "weixin_scan");
    }
}
