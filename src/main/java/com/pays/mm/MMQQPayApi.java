package com.pays.mm;

public class MMQQPayApi
        extends MMPayApi {
    public static final String ID = "mmQQ";
    private static final String TITLE = "MMQQ接口";

    public String getTitle() {
        return "MMQQ接口";
    }

    public String getId() {
        return "mmQQ";
    }

    protected void init() {
        addBank("QQ", "QQPAY");
        addBankCredit("QQ", "QQPAY");
    }
}
