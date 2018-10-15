package com.pays.daddy;

public class DaddyNocardPayApi
        extends DaddyWxPayApi {
    public static final String ID = "daddyNocard";
    private static final String TITLE = "DADDY无卡支付接口";

    public String getTitle() {
        return "DADDY无卡支付接口";
    }

    public String getId() {
        return "daddyNocard";
    }

    protected void init() {
        addBank("NOCARD", "51");
    }
}
