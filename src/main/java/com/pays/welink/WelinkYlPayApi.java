package com.pays.welink;

import com.pays.PayApiParam;

import java.util.Map;

public class WelinkYlPayApi
        extends WelinkPayApi {
    public static final String ID = "welinkYl";
    private static final String TITLE = "welink银联快捷支付接口";

    public String getTitle() {
        return "welink银联快捷支付接口";
    }

    public String getId() {
        return "welinkYl";
    }

    protected void init() {
        addBank("RMBJJK", "QPAY_UNIONPAY");
        addBankCredit("RMBJJK", "QPAY_UNIONPAY");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("pay_type", "QUICK_PAY");

        return maps;
    }
}
