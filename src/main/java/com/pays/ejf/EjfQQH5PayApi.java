package com.pays.ejf;

public class EjfQQH5PayApi
        extends EjfWxPayApi {
    public static final String ID = "ejfQQH5";
    private static final String TITLE = "易捷付-QQH5接口";
    private static final String PAY_TYPE = "QQH5";

    public String getTitle() {
        return "易捷付-QQH5接口";
    }

    public String getId() {
        return "ejfQQH5";
    }

    protected String getPayType() {
        return "QQH5";
    }

    protected void init() {
        addBank("QQ", "");
        addBankCredit("QQ", "");
    }
}
