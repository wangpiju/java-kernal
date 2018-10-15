package com.pays.juxin;

public class JuxinQQScanPayApi
        extends JuxinPayApi {
    public static final String ID = "juxinQQScan";
    private static final String TITLE = "聚鑫QQ钱包扫码支付接口";

    public String getTitle() {
        return "聚鑫QQ钱包扫码支付接口";
    }

    public String getId() {
        return "juxinQQScan";
    }

    protected String getPayType() {
        return "60";
    }

    protected void init() {
        addBank("QQ", "");
        addBankCredit("QQ", "");
    }
}
