package com.pays.rfu;

public class RfuMobilePayApi
        extends RfuPayApi {
    public static final String ID = "rfuMobile";
    private static final String TITLE = "锐付Mobile支付接口";

    public String getTitle() {
        return "锐付Mobile支付接口";
    }

    public String getId() {
        return "rfuMobile";
    }

    protected void init() {
        addBankCredit("MOBILE", "MOBILE");
    }
}
