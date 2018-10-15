package com.pays.ttflash;

public class TtflashYlPayApi
        extends TtflashPayApi {
    public static final String ID = "ttflashYl";
    private static final String TITLE = "闪付银联支付接口";

    public String getTitle() {
        return "闪付银联支付接口";
    }

    public String getId() {
        return "ttflashYl";
    }

    protected void init() {
        addBank("ABC", "964");
        addBank("ICBC", "967");
        addBank("CCB", "965");
        addBank("COMM", "981");
        addBank("BOC", "963");
        addBank("CMB", "970");
        addBank("CMBC", "980");
        addBank("CEB", "986");
        addBank("BOB", "989");
        addBank("CIB", "972");
        addBank("PSBC", "971");
        addBank("SPA", "978");
        addBank("SPDB", "977");
        addBank("BEA", "987");
        addBank("CITIC", "962");
        addBank("NJCB", "979");
        addBank("GDB", "985");
        addBank("SRCB", "976");
        addBank("HZB", "983");
    }
}
