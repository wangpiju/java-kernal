package com.pays.rfu;

public class RfuWapPayApi
        extends RfuPayApi {
    public static final String ID = "rfuWap";
    private static final String TITLE = "锐付Wap支付接口";

    public String getTitle() {
        return "锐付Wap支付接口";
    }

    public String getId() {
        return "rfuWap";
    }

    protected void init() {
        addBankCredit("WAP", "MWEB");
    }
}
