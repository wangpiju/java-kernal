package com.pays.jf;

public class JfQQPayApi
        extends JfWxPayApi {
    public static final String ID = "jfQq";
    private static final String TITLE = "极付-QQ钱包接口";

    public String getTitle() {
        return "极付-QQ钱包接口";
    }

    public String getId() {
        return "jfQq";
    }

    protected void init() {
        addBank("QQ", "qqcode");
        addBankCredit("QQ", "qqcode");
    }
}
