package com.pays.xingxin2;

public class Xingxin2QQPayApi
        extends Xingxin2WxPayApi {
    public static final String ID = "xingxin2QQ";
    private static final String TITLE = "兴信（新）QQ扫码支付接口";

    public String getTitle() {
        return "兴信（新）QQ扫码支付接口";
    }

    public String getId() {
        return "xingxin2QQ";
    }

    protected void init() {
        addBank("QQ", "3");
        addBankCredit("QQ", "3");
    }
}
