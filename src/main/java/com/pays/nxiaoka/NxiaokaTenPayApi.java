package com.pays.nxiaoka;

public class NxiaokaTenPayApi
        extends NxiaokaPayApi {
    public static final String ID = "nxiaokaTen";
    private static final String TITLE = "网富通财付通接口";

    public String getTitle() {
        return "网富通财付通接口";
    }

    public String getId() {
        return "nxiaokaTen";
    }

    protected void init() {
        addBank("TENPAY", "TENPAY");
        addBankCredit("TENPAY", "TENPAYWAP");
    }
}
