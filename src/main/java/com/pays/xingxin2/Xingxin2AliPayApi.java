package com.pays.xingxin2;

public class Xingxin2AliPayApi
        extends Xingxin2WxPayApi {
    public static final String ID = "xingxin2Ali";
    private static final String TITLE = "兴信（新）支付宝扫码支付接口";

    public String getTitle() {
        return "兴信（新）支付宝扫码支付接口";
    }

    public String getId() {
        return "xingxin2Ali";
    }

    protected void init() {
        addBank("ALIPAY", "1");
        addBankCredit("ALIPAY", "1");
    }
}
