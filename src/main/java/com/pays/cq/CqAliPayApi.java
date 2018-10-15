package com.pays.cq;

public class CqAliPayApi
        extends CqWxPayApi {
    public static final String ID = "cqAli";
    private static final String TITLE = "超道-支付宝接口";

    public String getTitle() {
        return "超道-支付宝接口";
    }

    public String getId() {
        return "cqAli";
    }

    protected void init() {
        addBank("ALIPAY", "180001");
        addBankCredit("ALIPAY", "180001");
    }
}
