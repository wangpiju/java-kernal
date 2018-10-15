package com.pays.juxin;

public class JuxinAliScanPayApi
        extends JuxinPayApi {
    public static final String ID = "juxinAliScan";
    private static final String TITLE = "聚鑫支付宝扫码支付接口";

    public String getTitle() {
        return "聚鑫支付宝扫码支付接口";
    }

    public String getId() {
        return "juxinAliScan";
    }

    protected String getPayType() {
        return "30";
    }

    protected void init() {
        addBank("ALIPAY", "");
        addBankCredit("ALIPAY", "");
    }
}
