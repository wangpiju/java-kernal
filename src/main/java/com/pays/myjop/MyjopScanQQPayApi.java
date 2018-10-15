package com.pays.myjop;

public class MyjopScanQQPayApi
        extends MyjopScanPayApi {
    public static final String ID = "myjopScanQQ";
    private static final String TITLE = "创瑞宝QQ扫码支付接口";

    public String getTitle() {
        return "创瑞宝QQ扫码支付接口";
    }

    public String getId() {
        return "myjopScanQQ";
    }

    protected void init() {
        addBank("QQ", "");
        addBankCredit("QQ", "");
    }
}
