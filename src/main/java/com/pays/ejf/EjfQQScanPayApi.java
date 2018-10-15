package com.pays.ejf;

public class EjfQQScanPayApi
        extends EjfWxPayApi {
    public static final String ID = "ejfQQScan";
    private static final String TITLE = "易捷付-QQ扫码接口";
    private static final String PAY_TYPE = "QQ";

    public String getTitle() {
        return "易捷付-QQ扫码接口";
    }

    public String getId() {
        return "ejfQQScan";
    }

    protected String getPayType() {
        return "QQ";
    }

    protected void init() {
        addBank("QQ", "");
        addBankCredit("QQ", "");
    }
}
