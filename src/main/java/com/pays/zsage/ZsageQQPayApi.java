package com.pays.zsage;

public class ZsageQQPayApi
        extends ZsageWxPayApi {
    public static final String ID = "zsageQQ";
    private static final String TITLE = "泽圣QQ支付接口";

    public String getTitle() {
        return "泽圣QQ支付接口";
    }

    public String getId() {
        return "zsageQQ";
    }

    protected void init() {
        addBank("QQ", "31");
        addBankCredit("QQ", "31");
    }
}
