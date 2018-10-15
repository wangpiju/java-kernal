package com.pays.mobao;

public class MobaoYlPayApi
        extends MobaoPayApi {
    public static final String ID = "mobaoYl";
    private static final String TITLE = "摩宝银联支付接口";

    public String getTitle() {
        return "摩宝银联支付接口";
    }

    public String getId() {
        return "mobaoYl";
    }

    protected void init() {
        addBankCredit("RMBJJK", "RMBJJK");
    }
}
