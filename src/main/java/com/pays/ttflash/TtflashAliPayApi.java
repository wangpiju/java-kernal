package com.pays.ttflash;

import com.pays.PayApiParam;

import java.util.Map;

public class TtflashAliPayApi
        extends TtflashPayApi {
    public static final String ID = "ttflashAli";
    private static final String TITLE = "闪付支付宝支付接口";

    public String getTitle() {
        return "闪付支付宝支付接口";
    }

    public String getId() {
        return "ttflashAli";
    }

    protected void init() {
        addBank("ALIPAY", "992");

        addBankCredit("ALIPAY", "1006");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        if (param.getIsMobile().booleanValue()) {
            maps.put("type", "1006");
        } else {
            maps.put("type", "992");
        }
        return maps;
    }
}
