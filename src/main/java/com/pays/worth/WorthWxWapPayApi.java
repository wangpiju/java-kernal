package com.pays.worth;

import com.pays.PayApiParam;

import java.util.Map;

public class WorthWxWapPayApi
        extends WorthWxPayApi {
    public static final String ID = "worthWxWap";
    private static final String TITLE = "华势在线微信Wap支付接口";

    public String getTitle() {
        return "华势在线微信Wap支付接口";
    }

    public String getId() {
        return "worthWxWap";
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("isApp", "app");

        return maps;
    }
}
