package com.pays.un;

import com.pays.PayApiParam;

public class UnWxH5PayApi
        extends UnPayApi {
    public static final String ID = "unWxH5";
    private static final String TITLE = "乐天付微信H5支付接口";

    public String getTitle() {
        return "乐天付微信H5支付接口";
    }

    public String getId() {
        return "unWxH5";
    }

    protected void init() {
        addBank("WEIXIN", "1007");
        addBankCredit("WEIXIN", "1007");
    }

    protected String getTypeid(PayApiParam param) {
        return "990";
    }
}
