package com.pays.welink;

import com.pays.PayApiParam;

import java.util.Map;

public class WelinkWxPayApi
        extends WelinkPayApi {
    public static final String ID = "welinkWx";
    private static final String TITLE = "welink微信支付接口";

    public String getTitle() {
        return "welink微信支付接口";
    }

    public String getId() {
        return "welinkWx";
    }

    protected void init() {
        addBank("WEIXIN", "WEIXIN_QRCODE");
        addBankCredit("WEIXIN", "WEIXIN_QRCODE");
    }

    protected Map<String, String> getParamMaps(PayApiParam param) {
        Map<String, String> maps = super.getParamMaps(param);
        maps.put("pay_type", "WXPAY");

        return maps;
    }
}
