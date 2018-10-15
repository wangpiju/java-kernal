package com.pays.myjop;

public class MyjopScanYlPayApi
        extends MyjopScanPayApi {
    public static final String ID = "myjopScanYl";
    private static final String TITLE = "创瑞宝银联扫码支付接口";

    public String getTitle() {
        return "创瑞宝银联扫码支付接口";
    }

    public String getId() {
        return "myjopScanYl";
    }

    protected void init() {
        addBank("RMBJJK", "");
        addBankCredit("RMBJJK", "");
    }
}
