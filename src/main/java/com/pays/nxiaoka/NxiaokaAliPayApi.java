package com.pays.nxiaoka;

public class NxiaokaAliPayApi
        extends NxiaokaPayApi {
    public static final String ID = "nxiaokaAli";
    private static final String TITLE = "网富通支付宝接口";

    public String getTitle() {
        return "网富通支付宝接口";
    }

    public String getId() {
        return "nxiaokaAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
        addBankCredit("ALIPAY", "ALIPAYWAP");
    }
}
