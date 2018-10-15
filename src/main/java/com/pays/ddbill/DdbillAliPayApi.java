package com.pays.ddbill;

public class DdbillAliPayApi
        extends DdbillWxPayApi {
    private static final String TITLE = "多得宝-支付宝扫码接口";
    public static final String ID = "ddbillAli";
    private static final String SERVICE_TYPE = "alipay_scan";

    protected String getServiceType() {
        return "alipay_scan";
    }

    public String getTitle() {
        return "多得宝-支付宝扫码接口";
    }

    public String getId() {
        return "ddbillAli";
    }

    protected void init() {
        addBank("ALIPAY", "alipay_scan");
        addBankCredit("ALIPAY", "alipay_scan");
    }
}
