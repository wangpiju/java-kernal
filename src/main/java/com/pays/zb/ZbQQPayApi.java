package com.pays.zb;

public class ZbQQPayApi
        extends ZbPayApi {
    public static final String ID = "zbQQ";
    private static final String TITLE = "众宝QQ接口";

    protected void init() {
        addBankCredit("QQ", "993");
    }

    public String getTitle() {
        return "众宝QQ接口";
    }

    public String getId() {
        return "zbQQ";
    }
}
