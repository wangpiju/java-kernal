package com.pays.nxiaoka;

public class NxiaokaAliScanPayApi
        extends NxiaokaPayApi {
    public static final String ID = "nxiaokaAliScan";
    private static final String TITLE = "网富通支付宝扫码接口";

    public String getTitle() {
        return "网富通支付宝扫码接口";
    }

    public String getId() {
        return "nxiaokaAliScan";
    }

    protected void init() {
        addBank("ALIPAYSCAN", "ALIPAYSCAN");
    }
}
