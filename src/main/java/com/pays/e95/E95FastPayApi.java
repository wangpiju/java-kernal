package com.pays.e95;

public class E95FastPayApi
        extends E95PayApi {
    public static final String ID = "e95fast";
    private static final String TITLE = "双乾-快捷接口";
    private static final String PAY_TYPE = "KJPAY";

    public String getTitle() {
        return "双乾-快捷接口";
    }

    public String getId() {
        return "e95fast";
    }

    protected String getPayType() {
        return "KJPAY";
    }
}
