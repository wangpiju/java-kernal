package com.pays.un;

import com.pays.PayApiParam;

public class UnWxScanPayApi
        extends UnPayApi {
    public static final String ID = "unWxScan";
    private static final String TITLE = "乐天付微信扫码支付接口";

    public String getTitle() {
        return "乐天付微信扫码支付接口";
    }

    public String getId() {
        return "unWxScan";
    }

    protected void init() {
        addBank("WEIXIN", "991");
        addBankCredit("WEIXIN", "991");
    }

    protected String getTypeid(PayApiParam param) {
        return "99";
    }
}
