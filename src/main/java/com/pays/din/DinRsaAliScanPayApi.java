package com.pays.din;

public class DinRsaAliScanPayApi
        extends DinRsaScanPayApi {
    private static final String TITLE = "智付RSA支付宝扫码支付接口";
    public static final String ID = "dinRsaAliScan";

    public String getTitle() {
        return "智付RSA支付宝扫码支付接口";
    }

    public String getId() {
        return "dinRsaAliScan";
    }

    protected void init() {
        addBank("ALIPAY", "alipay_scan");
    }
}
