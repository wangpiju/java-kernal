package com.pays.ttflash;

import com.pays.PayApiParam;

import java.util.Map;

public class TtflashWxPayApi
        extends TtflashPayApi {
    public static final String ID = "ttflashWx";
    private static final String TITLE = "闪付微信支付接口";

    public String getTitle() {
        return "闪付微信支付接口";
    }

    public String getId() {
        return "ttflashWx";
    }

    protected void init() {
        addBank("WEIXIN", "1004");

        addBankCredit("WEIXIN", "1007");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        if (param.getIsMobile().booleanValue()) {
            maps.put("type", "1007");
        } else {
            maps.put("type", "1004");
        }
        return maps;
    }
}
