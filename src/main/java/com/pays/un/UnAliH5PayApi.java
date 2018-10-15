package com.pays.un;

import com.pays.PayApiParam;

public class UnAliH5PayApi
        extends UnPayApi {
    public static final String ID = "unAliH5";
    private static final String TITLE = "乐天付支付宝H5支付接口";

    public String getTitle() {
        return "乐天付支付宝H5支付接口";
    }

    public String getId() {
        return "unAliH5";
    }

    protected void init() {
        addBank("ALIPAY", "1006");
        addBankCredit("ALIPAY", "1006");
    }

    protected String getTypeid(PayApiParam param) {
        return "980";
    }
}
