package com.pays.zhih;

public class ZhihAliPayApi
        extends ZhihWxPayApi {
    private static final String TITLE = "智汇付-支付宝扫码接口";
    public static final String ID = "zhihAli";
    private static final String SERVICE_TYPE = "alipay_scan";

    protected String getServiceType() {
        return "alipay_scan";
    }

    public String getTitle() {
        return "智汇付-支付宝扫码接口";
    }

    public String getId() {
        return "zhihAli";
    }

    protected void init() {
        addBank("ALIPAY", "alipay_scan");
        addBankCredit("ALIPAY", "alipay_scan");
    }
}
