package com.pays.mm;

public class MMAliPayApi
        extends MMPayApi {
    public static final String ID = "mmAli";
    private static final String TITLE = "MM支付宝接口";

    public String getTitle() {
        return "MM支付宝接口";
    }

    public String getId() {
        return "mmAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
        addBankCredit("ALIPAY", "ALIPAY");
    }
}
