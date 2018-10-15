package com.pays.jf;

public class JfAliPayApi
        extends JfWxPayApi {
    public static final String ID = "jfAli";
    private static final String TITLE = "极付-支付宝接口";

    public String getTitle() {
        return "极付-支付宝接口";
    }

    public String getId() {
        return "jfAli";
    }

    protected void init() {
        addBank("ALIPAY", "alipay");
        addBankCredit("ALIPAY", "alipay");
    }
}
