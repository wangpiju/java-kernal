package com.pays.yom;

public class YomYlPayApi
        extends YomPayApi {
    public static final String ID = "yomWx";
    private static final String TITLE = "优付微信支付接口";

    public String getTitle() {
        return "优付微信支付接口";
    }

    public String getId() {
        return "yomWx";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("BOC", "BOC");
        addBank("COMM", "BOCOM");
        addBank("CCB", "CCB");
        addBank("ICBC", "ICBC");
        addBank("PSBC", "PSBC");
        addBank("CMB", "CMBC");
        addBank("SPDB", "SPDB");
        addBank("CEB", "CEBBANK");
        addBank("CITIC", "ECITIC");
        addBank("SPA", "PINGAN");
        addBank("CMBC", "CMBCS");
        addBank("HXB", "HXB");
        addBank("GDB", "CGB");
        addBank("BOB", "BCCB");
        addBank("CIB", "CIB");
    }
}
