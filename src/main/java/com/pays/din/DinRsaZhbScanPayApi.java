package com.pays.din;

public class DinRsaZhbScanPayApi
        extends DinRsaScanPayApi {
    private static final String TITLE = "智付RSA智慧宝扫码支付接口";
    public static final String ID = "dinRsaZhbScan";

    public String getTitle() {
        return "智付RSA智慧宝扫码支付接口";
    }

    public String getId() {
        return "dinRsaZhbScan";
    }

    protected void init() {
        addBank("ZHBPAY", "zhb_scan");
    }
}
