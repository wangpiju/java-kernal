package com.pays.xingxin;

public class XingxinQQPayApi
        extends XingxinWxPayApi {
    public static final String ID = "xingxinQQ";
    private static final String TITLE = "兴信QQ扫码支付接口";

    public String getTitle() {
        return "兴信QQ扫码支付接口";
    }

    public String getId() {
        return "xingxinQQ";
    }

    protected void init() {
        addBank("QQ", "QQ");
        addBankCredit("QQ", "QQ");
    }
}
