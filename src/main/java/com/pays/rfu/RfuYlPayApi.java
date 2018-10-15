package com.pays.rfu;

public class RfuYlPayApi
        extends RfuPayApi {
    public static final String ID = "rfuYl";
    private static final String TITLE = "锐付银联支付接口";

    public String getTitle() {
        return "锐付银联支付接口";
    }

    public String getId() {
        return "rfuYl";
    }

    protected void init() {
        addBank("PSBC", "01000000");
        addBank("ICBC", "01020000");
        addBank("ABC", "01030000");
        addBank("BOC", "01040000");
        addBank("CCB", "01050000");
        addBank("COMM", "03010000");
        addBank("CITIC", "03020000");
        addBank("CEB", "03030000");
        addBank("HXB", "03040000");
        addBank("CMBC", "03050000");
        addBank("GDB", "03060000");
        addBank("SPA", "03070000");
        addBank("CMB", "03080000");
        addBank("CIB", "03090000");
        addBank("SPDB", "03100000");
        addBank("BEA", "03200000");
        addBank("BOB", "04031000");
        addBank("NJCB", "04243010");
        addBank("SRCB", "65012900");
    }
}
