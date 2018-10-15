package com.pays.xingxin;

public class XingxinYlPayApi
        extends XingxinWxPayApi {
    public static final String ID = "xingxinYl";
    private static final String TITLE = "兴信银联支付接口";

    public String getTitle() {
        return "兴信银联支付接口";
    }

    public String getId() {
        return "xingxinYl";
    }

    protected void init() {
        addBank("RMBJJK", "UNIPAY");
        addBankCredit("RMBJJK", "UNIPAY");
    }
}
