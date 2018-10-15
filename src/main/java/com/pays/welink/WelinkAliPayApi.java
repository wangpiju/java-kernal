package com.pays.welink;

import com.pays.PayApiParam;

import java.util.Map;

public class WelinkAliPayApi
        extends WelinkPayApi {
    public static final String ID = "welinkAli";
    private static final String TITLE = "welink支付宝支付接口";

    public String getTitle() {
        return "welink支付宝支付接口";
    }

    public String getId() {
        return "welinkAli";
    }

    protected void init() {
        addBank("ALIPAY", "ALIPAY_QRCODE");
        addBankCredit("ALIPAY", "ALIPAY_QRCODE");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("pay_type", "ALIPAY");

        return maps;
    }
}
