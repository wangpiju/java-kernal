package com.pays.nxiaoka;

public class NxiaokaQQPayApi
        extends NxiaokaPayApi {
    public static final String ID = "nxiaokaQQ";
    private static final String TITLE = "网富通QQ钱包接口";

    public String getTitle() {
        return "网富通QQ钱包接口";
    }

    public String getId() {
        return "nxiaokaQQ";
    }

    protected void init() {
        addBank("QQ", "QQ");
        addBankCredit("QQ", "QQWAP");
    }
}
