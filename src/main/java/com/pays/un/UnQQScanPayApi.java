package com.pays.un;

import com.pays.PayApiParam;

public class UnQQScanPayApi
        extends UnPayApi {
    public static final String ID = "unQQScan";
    private static final String TITLE = "乐天付QQ扫码支付接口";

    public String getTitle() {
        return "乐天付QQ扫码支付接口";
    }

    public String getId() {
        return "unQQScan";
    }

    protected void init() {
        addBank("QQ", "993");
        addBankCredit("QQ", "993");
    }

    protected String getTypeid(PayApiParam param) {
        return "100";
    }
}
