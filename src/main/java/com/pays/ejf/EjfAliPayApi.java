package com.pays.ejf;

public class EjfAliPayApi
        extends EjfWxPayApi {
    public static final String ID = "ejfAli";
    private static final String TITLE = "易捷付-支付宝接口";
    private static final String PAY_TYPE = "Alipay";

    public String getTitle() {
        return "易捷付-支付宝接口";
    }

    public String getId() {
        return "ejfAli";
    }

    protected String getPayType() {
        return "Alipay";
    }

    protected void init() {
        addBank("ALIPAY", "");
        addBankCredit("ALIPAY", "");
    }
}
