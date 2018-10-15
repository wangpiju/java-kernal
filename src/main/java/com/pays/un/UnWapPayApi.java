package com.pays.un;

import com.pays.PayApiParam;

public class UnWapPayApi
        extends UnPayApi {
    public static final String ID = "unWap";
    private static final String TITLE = "乐天付WAP支付接口";

    public String getTitle() {
        return "乐天付WAP支付接口";
    }

    public String getId() {
        return "unWap";
    }

    protected void init() {
        addBank("RMBJJK", "1005");
        addBankCredit("RMBJJK", "1005");
    }

    protected String getTypeid(PayApiParam param) {
        return "1020";
    }
}
