package com.pays.yee;

public class YeeYlPayApi
        extends YeePayApi {
    public static final String ID = "yeeYl";
    private static final String TITLE = "银宝银联支付接口";

    public String getTitle() {
        return "银宝银联支付接口";
    }

    public String getId() {
        return "yeeYl";
    }

    protected void init() {
        addBank("ABC", "ABC");
        addBank("ICBC", "ICBC");
        addBank("CCB", "CCB");
        addBank("COMM", "BOCO");
        addBank("BOC", "BOC");
        addBank("CMB", "CMB");
        addBank("CMBC", "CMBC");
        addBank("CEB", "CEB");
        addBank("BOB", "BCCB");
        addBank("SHB", "SHB");
        addBank("NBCB", "NBCB");
        addBank("HXB", "HXB");
        addBank("CIB", "CIB");
        addBank("PSBC", "PSBS");
        addBank("SPA", "PINGANBANK");
        addBank("SPDB", "SPDB");
        addBank("BEA", "HKBEA");
        addBank("CITIC", "CTTIC");
        addBank("NJCB", "NJCB");
        addBank("CBHB", "CBHB");
        addBank("GDB", "GDB");
        addBank("BJRCB", "BJRCB");
        addBank("SRCB", "SRCB");
        addBank("SDB", "SDB");
    }
}
