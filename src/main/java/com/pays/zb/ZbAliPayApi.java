package com.pays.zb;

public class ZbAliPayApi
        extends ZbPayApi {
    public static final String ID = "zbAli";
    private static final String TITLE = "众宝支付宝接口";

    protected void init() {
        addBankCredit("ALIPAY", "992");
    }

    public String getTitle() {
        return "众宝支付宝接口";
    }

    public String getId() {
        return "zbAli";
    }
}
