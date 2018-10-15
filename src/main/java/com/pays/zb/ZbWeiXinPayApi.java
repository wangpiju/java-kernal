package com.pays.zb;

public class ZbWeiXinPayApi
        extends ZbPayApi {
    public static final String ID = "zbWx";
    private static final String TITLE = "众宝微信接口";

    protected void init() {
        addBankCredit("WEIXIN", "1004");
    }

    public String getTitle() {
        return "众宝微信接口";
    }

    public String getId() {
        return "zbWx";
    }
}
