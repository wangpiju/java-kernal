package com.pays.xingxin;

public class XingxinAliPayApi
        extends XingxinWxPayApi {
    public static final String ID = "xingxinAli";
    private static final String TITLE = "兴信支付宝扫码支付接口";

    public String getTitle() {
        return "兴信支付宝扫码支付接口";
    }

    public String getId() {
        return "xingxinAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY");
        addBankCredit("ALIPAY", "ALIPAY");
    }
}
