package com.pays.un;

import com.pays.PayApiParam;

public class UnAliScanPayApi
        extends UnPayApi {
    public static final String ID = "unAliScan";
    private static final String TITLE = "乐天付支付宝扫码支付接口";

    public String getTitle() {
        return "乐天付支付宝扫码支付接口";
    }

    public String getId() {
        return "unAliScan";
    }

    protected void init() {
        addBank("ALIPAY", "992");
        addBankCredit("ALIPAY", "992");
    }

    protected String getTypeid(PayApiParam param) {
        return "98";
    }
}
