package com.pays.un;

import com.pays.PayApiParam;

public class UnYlScanPayApi
        extends UnPayApi {
    public static final String ID = "unYlScan";
    private static final String TITLE = "乐天付银联扫码支付接口";

    public String getTitle() {
        return "乐天付银联扫码支付接口";
    }

    public String getId() {
        return "unYlScan";
    }

    protected void init() {
        addBank("RMBJJK", "1021");
        addBankCredit("RMBJJK", "1021");
    }

    protected String getTypeid(PayApiParam param) {
        return "101";
    }
}
